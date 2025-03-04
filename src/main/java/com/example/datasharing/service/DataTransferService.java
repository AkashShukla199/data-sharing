package com.example.datasharing.service;

import com.example.datasharing.model.DataRecord;
import com.example.datasharing.repository.DataRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service to orchestrate data transfer between Excel and SAP.
 */
@Service
public class DataTransferService {

    private static final Logger log = LoggerFactory.getLogger(DataTransferService.class);
    
    private final ExcelService excelService;
    private final SapService sapService;
    private final DataRecordRepository dataRecordRepository;
    
    public DataTransferService(ExcelService excelService, SapService sapService, 
                              DataRecordRepository dataRecordRepository) {
        this.excelService = excelService;
        this.sapService = sapService;
        this.dataRecordRepository = dataRecordRepository;
    }
    
    /**
     * Process all Excel files in the input directory and send data to SAP.
     * 
     * @return Number of records successfully processed
     */
    public int processExcelToSap() {
        log.info("Starting Excel to SAP data transfer process");
        
        // Read data from Excel files
        List<DataRecord> records = excelService.processInputDirectory();
        log.info("Read {} records from Excel files", records.size());
        
        if (records.isEmpty()) {
            log.info("No records to process");
            return 0;
        }
        
        // Save records to database
        records = dataRecordRepository.saveAll(records);
        log.info("Saved {} records to database", records.size());
        
        // Send data to SAP
        int successCount = sapService.writeData(records, "SAP");
        log.info("Successfully sent {} out of {} records to SAP", successCount, records.size());
        
        // Update records in database
        dataRecordRepository.saveAll(records);
        log.info("Updated record statuses in database");
        
        return successCount;
    }
    
    /**
     * Scheduled job to process Excel files and send data to SAP.
     * Runs every minute by default (configurable in application.yml).
     */
    @Scheduled(fixedDelayString = "${app.excel.polling-interval}")
    public void scheduledProcessing() {
        try {
            log.info("Running scheduled Excel to SAP data transfer at {}", LocalDateTime.now());
            int processedCount = processExcelToSap();
            log.info("Scheduled processing completed. Processed {} records", processedCount);
        } catch (Exception e) {
            log.error("Error in scheduled processing", e);
        }
    }
}
