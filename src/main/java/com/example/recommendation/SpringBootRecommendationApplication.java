package com.example.recommendation;

import com.example.recommendation.service.RecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableKafka
public class SpringBootRecommendationApplication {
    private static final Logger logger = LoggerFactory.getLogger(SpringBootRecommendationApplication.class);
    @Autowired
    private RecommendationService recommendationService;

    public static void main(String[] args) {
        logger.info("Starting Spring Boot Recommendation Microservice...");
        SpringApplication.run(SpringBootRecommendationApplication.class, args);
        logger.info("Spring Boot Recommendation Microservice started successfully.");
    }

    @PostConstruct
    @Profile("!test")
    public void loadModel() {
        recommendationService.loadModel("models/recommendation_model");
    }
}
