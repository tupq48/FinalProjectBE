package com.app.final_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync()
@EnableCaching()
@EnableRetry
@EnableTransactionManagement()
public class FinalProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinalProjectApplication.class, args);
    }
}

