package com.example.recommendation.config;

import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Configuration class for MongoDB.
 */
@Configuration
public class MongoConfig {

    /**
     * Creates a MongoTemplate bean for interacting with MongoDB.
     *
     * @return the MongoTemplate bean
     */
    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(MongoClients.create("mongodb://localhost:27017"), "recommendationdb");
    }
}