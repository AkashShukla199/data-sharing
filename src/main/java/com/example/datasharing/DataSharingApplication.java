package com.example.datasharing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DataSharingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataSharingApplication.class, args);
    }
}
