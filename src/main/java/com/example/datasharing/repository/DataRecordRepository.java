package com.example.datasharing.repository;

import com.example.datasharing.model.DataRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataRecordRepository extends JpaRepository<DataRecord, Long> {
    
    List<DataRecord> findByStatus(String status);
    
    List<DataRecord> findBySourceFile(String sourceFile);
    
    List<DataRecord> findBySourceSystem(String sourceSystem);
    
    List<DataRecord> findByTargetSystem(String targetSystem);
}
