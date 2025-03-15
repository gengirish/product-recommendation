package com.example.recommendation.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for the MongoConfig configuration.
 */
class MongoConfigTest {

    private AnnotationConfigApplicationContext context;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigApplicationContext(MongoConfig.class);
    }

    /**
     * Test for the mongoTemplate bean.
     */
    @Test
    void testMongoTemplate() {
        MongoTemplate mongoTemplate = context.getBean(MongoTemplate.class);
        assertNotNull(mongoTemplate, "The mongoTemplate bean should not be null");
    }
}