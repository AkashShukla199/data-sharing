package com.example.datasharing.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Generic data record that can represent data from Excel and be sent to SAP.
 * Uses a dynamic approach with a Map to store column values.
 */
@Entity
@Table(name = "data_records")
public class DataRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String sourceFile;
    private String sourceSystem;
    private String targetSystem;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private String status;
    private String errorMessage;
    
    @Transient // This field won't be persisted in the database
    private Map<String, Object> data = new HashMap<>();
    
    // Default constructor
    public DataRecord() {
    }
    
    // All-args constructor
    public DataRecord(Long id, String sourceFile, String sourceSystem, String targetSystem,
                     LocalDateTime createdAt, LocalDateTime processedAt, String status,
                     String errorMessage, Map<String, Object> data) {
        this.id = id;
        this.sourceFile = sourceFile;
        this.sourceSystem = sourceSystem;
        this.targetSystem = targetSystem;
        this.createdAt = createdAt;
        this.processedAt = processedAt;
        this.status = status;
        this.errorMessage = errorMessage;
        this.data = data != null ? data : new HashMap<>();
    }
    
    // Builder pattern
    public static DataRecordBuilder builder() {
        return new DataRecordBuilder();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getSourceFile() {
        return sourceFile;
    }
    
    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }
    
    public String getSourceSystem() {
        return sourceSystem;
    }
    
    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }
    
    public String getTargetSystem() {
        return targetSystem;
    }
    
    public void setTargetSystem(String targetSystem) {
        this.targetSystem = targetSystem;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
    
    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public Map<String, Object> getData() {
        return data;
    }
    
    public void setData(Map<String, Object> data) {
        this.data = data;
    }
    
    // Helper methods to get and set values by column name
    public Object getValue(String columnName) {
        return data.get(columnName);
    }
    
    public void setValue(String columnName, Object value) {
        data.put(columnName, value);
    }
    
    // Builder class
    public static class DataRecordBuilder {
        private Long id;
        private String sourceFile;
        private String sourceSystem;
        private String targetSystem;
        private LocalDateTime createdAt;
        private LocalDateTime processedAt;
        private String status;
        private String errorMessage;
        private Map<String, Object> data = new HashMap<>();
        
        public DataRecordBuilder id(Long id) {
            this.id = id;
            return this;
        }
        
        public DataRecordBuilder sourceFile(String sourceFile) {
            this.sourceFile = sourceFile;
            return this;
        }
        
        public DataRecordBuilder sourceSystem(String sourceSystem) {
            this.sourceSystem = sourceSystem;
            return this;
        }
        
        public DataRecordBuilder targetSystem(String targetSystem) {
            this.targetSystem = targetSystem;
            return this;
        }
        
        public DataRecordBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public DataRecordBuilder processedAt(LocalDateTime processedAt) {
            this.processedAt = processedAt;
            return this;
        }
        
        public DataRecordBuilder status(String status) {
            this.status = status;
            return this;
        }
        
        public DataRecordBuilder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }
        
        public DataRecordBuilder data(Map<String, Object> data) {
            this.data = data;
            return this;
        }
        
        public DataRecord build() {
            return new DataRecord(id, sourceFile, sourceSystem, targetSystem, 
                                 createdAt, processedAt, status, errorMessage, data);
        }
    }
}
