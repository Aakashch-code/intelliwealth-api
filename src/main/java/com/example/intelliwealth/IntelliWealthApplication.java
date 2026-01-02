package com.example.intelliwealth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication

// PostgreSQL (Core)
@EntityScan(basePackages = "com.example.intelliwealth.core")
@EnableJpaRepositories(
        basePackages = "com.example.intelliwealth.core"
)

// MongoDB (Wealth + Protection + Fynix)
@EnableMongoRepositories(
        basePackages = {
                "com.example.intelliwealth.wealth",
                "com.example.intelliwealth.protection",
                "com.example.intelliwealth.fynix"
        }
)

public class IntelliWealthApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntelliWealthApplication.class, args);
    }
}
