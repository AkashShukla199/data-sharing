package com.example.datasharing.controller;

import com.example.datasharing.model.DataRecord;
import com.example.datasharing.repository.DataRecordRepository;
import com.example.datasharing.service.DataTransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/data-transfer")
public class DataTransferController {

    private static final Logger log = LoggerFactory.getLogger(DataTransferController.class);
    
    private final DataTransferService dataTransferService;
    private final DataRecordRepository dataRecordRepository;
    
    public DataTransferController(DataTransferService dataTransferService, 
                                 DataRecordRepository dataRecordRepository) {
        this.dataTransferService = dataTransferService;
        this.dataRecordRepository = dataRecordRepository;
    }
    
    /**
     * Manually trigger the Excel to SAP data transfer process.
     * 
     * @return Response with process results
     */
    @PostMapping("/process")
    public ResponseEntity<Map<String, Object>> processExcelToSap() {
        log.info("Manual trigger of Excel to SAP data transfer");
        
        int processedCount = dataTransferService.processExcelToSap();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Data transfer process completed");
        response.put("recordsProcessed", processedCount);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get all data records with optional status filter.
     * 
     * @param status Optional status filter
     * @return List of data records
     */
    @GetMapping("/records")
    public ResponseEntity<List<DataRecord>> getRecords(
            @RequestParam(required = false) String status) {
        
        List<DataRecord> records;
        
        if (status != null && !status.isEmpty()) {
            records = dataRecordRepository.findByStatus(status);
        } else {
            records = dataRecordRepository.findAll();
        }
        
        return ResponseEntity.ok(records);
    }
    
    /**
     * Get statistics about data records.
     * 
     * @return Statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        long totalRecords = dataRecordRepository.count();
        long pendingRecords = dataRecordRepository.findByStatus("PENDING").size();
        long processedRecords = dataRecordRepository.findByStatus("PROCESSED").size();
        long errorRecords = dataRecordRepository.findByStatus("ERROR").size();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRecords", totalRecords);
        stats.put("pendingRecords", pendingRecords);
        stats.put("processedRecords", processedRecords);
        stats.put("errorRecords", errorRecords);
        
        return ResponseEntity.ok(stats);
    }
}
