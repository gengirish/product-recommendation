package com.example.recommendation.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Kafka.
 */
@Configuration
public class KafkaConfig {

    /**
     * Creates a new Kafka topic for user activity.
     *
     * @return the new Kafka topic
     */
    @Bean
    public NewTopic recommendationTopic() {
        return new NewTopic("user-activity", 1, (short) 1);
    }
}