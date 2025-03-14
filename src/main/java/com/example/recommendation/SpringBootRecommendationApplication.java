package com.example.recommendation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class SpringBootRecommendationApplication {
    private static final Logger logger = LoggerFactory.getLogger(SpringBootRecommendationApplication.class);

    public static void main(String[] args) {
        logger.info("Starting Spring Boot Recommendation Microservice...");
        SpringApplication.run(SpringBootRecommendationApplication.class, args);
        logger.info("Spring Boot Recommendation Microservice started successfully.");
    }
}
