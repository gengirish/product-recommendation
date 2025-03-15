package com.example.recommendation.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for the KafkaConfig configuration.
 */
class KafkaConfigTest {

    private AnnotationConfigApplicationContext context;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigApplicationContext(KafkaConfig.class);
    }

    /**
     * Test for the recommendationTopic bean.
     */
    @Test
    void testRecommendationTopic() {
        NewTopic topic = context.getBean(NewTopic.class);
        assertNotNull(topic, "The recommendationTopic bean should not be null");
    }
}