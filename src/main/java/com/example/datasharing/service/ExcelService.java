package com.example.datasharing.service;

import com.example.datasharing.config.ExcelConfig;
import com.example.datasharing.model.DataRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelService implements DataSourceService {

    private static final Logger log = LoggerFactory.getLogger(ExcelService.class);
    
    private final ExcelConfig excelConfig;
    
    public ExcelService(ExcelConfig excelConfig) {
        this.excelConfig = excelConfig;
    }
    
    @PostConstruct
    public void init() {
        excelConfig.init();
    }
    
    @Override
    public List<DataRecord> readData(String source) {
        List<DataRecord> records = new ArrayList<>();
        
        try {
            File excelFile = new File(source);
            FileInputStream fis = new FileInputStream(excelFile);
            
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet
            
            // Get header row (column names)
            Row headerRow = sheet.getRow(0);
            Map<Integer, String> columnMap = new HashMap<>();
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell != null) {
                    columnMap.put(i, cell.getStringCellValue());
                }
            }
            
            // Process data rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                DataRecord record = DataRecord.builder()
                        .sourceFile(excelFile.getName())
                        .sourceSystem("EXCEL")
                        .targetSystem("SAP")
                        .createdAt(LocalDateTime.now())
                        .status("PENDING")
                        .build();
                
                // Process each cell in the row
                for (int j = 0; j < columnMap.size(); j++) {
                    Cell cell = row.getCell(j);
                    String columnName = columnMap.get(j);
                    
                    if (cell != null && columnName != null) {
                        Object value = getCellValue(cell);
                        record.setValue(columnName, value);
                    }
                }
                
                records.add(record);
            }
            
            workbook.close();
            fis.close();
            
            // Move the file to processed directory
            moveFile(excelFile, excelConfig.getProcessedDirectory());
            
        } catch (Exception e) {
            log.error("Error reading Excel file: {}", source, e);
            try {
                moveFile(new File(source), excelConfig.getErrorDirectory());
            } catch (IOException ex) {
                log.error("Error moving file to error directory", ex);
            }
        }
        
        return records;
    }
    
    @Override
    public int writeData(List<DataRecord> records, String target) {
        // For this example, we'll implement writing back to Excel
        // In a real application, this might be used for error reporting or data export
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            
            // Assuming all records have the same structure, get column names from the first record
            if (!records.isEmpty()) {
                DataRecord firstRecord = records.get(0);
                int cellIdx = 0;
                
                // Add metadata columns
                headerRow.createCell(cellIdx++).setCellValue("ID");
                headerRow.createCell(cellIdx++).setCellValue("Source File");
                headerRow.createCell(cellIdx++).setCellValue("Source System");
                headerRow.createCell(cellIdx++).setCellValue("Target System");
                headerRow.createCell(cellIdx++).setCellValue("Status");
                
                // Add data columns
                for (String columnName : firstRecord.getData().keySet()) {
                    headerRow.createCell(cellIdx++).setCellValue(columnName);
                }
                
                // Add data rows
                int rowIdx = 1;
                for (DataRecord record : records) {
                    Row row = sheet.createRow(rowIdx++);
                    cellIdx = 0;
                    
                    // Add metadata
                    row.createCell(cellIdx++).setCellValue(record.getId() != null ? record.getId() : 0);
                    row.createCell(cellIdx++).setCellValue(record.getSourceFile());
                    row.createCell(cellIdx++).setCellValue(record.getSourceSystem());
                    row.createCell(cellIdx++).setCellValue(record.getTargetSystem());
                    row.createCell(cellIdx++).setCellValue(record.getStatus());
                    
                    // Add data
                    for (String columnName : firstRecord.getData().keySet()) {
                        Cell cell = row.createCell(cellIdx++);
                        Object value = record.getValue(columnName);
                        setCellValue(cell, value);
                    }
                }
            }
            
            // Write to file
            try (FileOutputStream fileOut = new FileOutputStream(target)) {
                workbook.write(fileOut);
            }
            
            return records.size();
        } catch (Exception e) {
            log.error("Error writing Excel file: {}", target, e);
            return 0;
        }
    }
    
    /**
     * Scans the input directory for Excel files and processes them.
     * 
     * @return List of all data records read from Excel files
     */
    public List<DataRecord> processInputDirectory() {
        List<DataRecord> allRecords = new ArrayList<>();
        File inputDir = new File(excelConfig.getInputDirectory());
        
        if (inputDir.exists() && inputDir.isDirectory()) {
            File[] files = inputDir.listFiles((dir, name) -> 
                name.toLowerCase().endsWith(".xlsx") || name.toLowerCase().endsWith(".xls"));
            
            if (files != null) {
                for (File file : files) {
                    log.info("Processing Excel file: {}", file.getName());
                    List<DataRecord> records = readData(file.getAbsolutePath());
                    allRecords.addAll(records);
                }
            }
        }
        
        return allRecords;
    }
    
    private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }
    
    private void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setBlank();
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof java.util.Date) {
            cell.setCellValue((java.util.Date) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }
    
    private void moveFile(File file, String targetDirectory) throws IOException {
        Path source = Paths.get(file.getAbsolutePath());
        Path target = Paths.get(targetDirectory, file.getName());
        Files.move(source, target);
    }
}
