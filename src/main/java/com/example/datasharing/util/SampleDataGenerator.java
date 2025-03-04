package com.example.datasharing.util;

import com.example.datasharing.config.ExcelConfig;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class to generate sample data for testing.
 */
@Configuration
public class SampleDataGenerator {

    private static final Logger log = LoggerFactory.getLogger(SampleDataGenerator.class);
    
    private final ExcelConfig excelConfig;
    
    public SampleDataGenerator(ExcelConfig excelConfig) {
        this.excelConfig = excelConfig;
    }
    
    /**
     * Generate sample Excel files for testing.
     * This bean is only active when the "dev" profile is active.
     */
    @Bean
    @Profile("dev")
    public CommandLineRunner generateSampleData() {
        return args -> {
            log.info("Generating sample data for testing");
            
            // Ensure directories exist
            excelConfig.init();
            
            // Generate sample Excel file
            generateSampleExcelFile();
            
            log.info("Sample data generation completed");
        };
    }
    
    /**
     * Generate a sample Excel file with product data.
     */
    private void generateSampleExcelFile() throws IOException {
        String fileName = "sample_products.xlsx";
        Path sampleFilePath = Paths.get("src/main/resources/samples", fileName);
        Path targetFilePath = Paths.get(excelConfig.getInputDirectory(), fileName);
        
        // Create sample directory if it doesn't exist
        Files.createDirectories(sampleFilePath.getParent());
        
        // Define column names
        List<String> columns = Arrays.asList(
                "ProductID", "ProductName", "Category", "Price", "Quantity", "Supplier", "Description");
        
        // Define sample data
        List<List<Object>> data = Arrays.asList(
                Arrays.asList("P001", "Laptop", "Electronics", 1299.99, 10, "TechCorp", "High-performance laptop"),
                Arrays.asList("P002", "Smartphone", "Electronics", 799.99, 20, "MobileTech", "Latest smartphone model"),
                Arrays.asList("P003", "Desk Chair", "Furniture", 199.99, 15, "OfficePro", "Ergonomic office chair"),
                Arrays.asList("P004", "Coffee Maker", "Appliances", 89.99, 25, "HomeGoods", "Programmable coffee maker"),
                Arrays.asList("P005", "Headphones", "Electronics", 149.99, 30, "AudioTech", "Noise-cancelling headphones")
        );
        
        // Create workbook and sheet
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Products");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns.get(i));
            }
            
            // Create data rows
            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i + 1);
                List<Object> rowData = data.get(i);
                
                for (int j = 0; j < rowData.size(); j++) {
                    Cell cell = row.createCell(j);
                    Object value = rowData.get(j);
                    
                    if (value instanceof String) {
                        cell.setCellValue((String) value);
                    } else if (value instanceof Double) {
                        cell.setCellValue((Double) value);
                    } else if (value instanceof Integer) {
                        cell.setCellValue((Integer) value);
                    } else if (value instanceof Boolean) {
                        cell.setCellValue((Boolean) value);
                    }
                }
            }
            
            // Auto-size columns
            for (int i = 0; i < columns.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Write to file
            try (FileOutputStream fileOut = new FileOutputStream(sampleFilePath.toFile())) {
                workbook.write(fileOut);
            }
            
            log.info("Sample Excel file created at: {}", sampleFilePath);
            
            // Copy to input directory
            Files.createDirectories(targetFilePath.getParent());
            Files.copy(sampleFilePath, targetFilePath);
            
            log.info("Sample Excel file copied to input directory: {}", targetFilePath);
        }
    }
}
