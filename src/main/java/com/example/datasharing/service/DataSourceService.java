package com.example.datasharing.service;

import com.example.datasharing.model.DataRecord;

import java.util.List;

/**
 * Generic interface for data source operations.
 */
public interface DataSourceService {
    
    /**
     * Read data from the source.
     * 
     * @param source The source identifier (e.g., file path, connection name)
     * @return List of data records read from the source
     */
    List<DataRecord> readData(String source);
    
    /**
     * Write data to the target.
     * 
     * @param records List of data records to write
     * @param target The target identifier (e.g., file path, connection name)
     * @return Number of records successfully written
     */
    int writeData(List<DataRecord> records, String target);
}
