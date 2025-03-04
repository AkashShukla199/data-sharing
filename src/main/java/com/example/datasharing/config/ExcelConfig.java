package com.example.datasharing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.excel")
public class ExcelConfig {
    
    private String inputDirectory;
    private String processedDirectory;
    private String errorDirectory;
    private long pollingInterval;
    
    // Getters and Setters
    public String getInputDirectory() {
        return inputDirectory;
    }
    
    public void setInputDirectory(String inputDirectory) {
        this.inputDirectory = inputDirectory;
    }
    
    public String getProcessedDirectory() {
        return processedDirectory;
    }
    
    public void setProcessedDirectory(String processedDirectory) {
        this.processedDirectory = processedDirectory;
    }
    
    public String getErrorDirectory() {
        return errorDirectory;
    }
    
    public void setErrorDirectory(String errorDirectory) {
        this.errorDirectory = errorDirectory;
    }
    
    public long getPollingInterval() {
        return pollingInterval;
    }
    
    public void setPollingInterval(long pollingInterval) {
        this.pollingInterval = pollingInterval;
    }
    
    // Ensure directories exist
    public void init() {
        createDirectoryIfNotExists(inputDirectory);
        createDirectoryIfNotExists(processedDirectory);
        createDirectoryIfNotExists(errorDirectory);
    }
    
    private void createDirectoryIfNotExists(String directoryPath) {
        java.io.File directory = new java.io.File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}
