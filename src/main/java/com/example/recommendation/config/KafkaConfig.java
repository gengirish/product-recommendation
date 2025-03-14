package com.example.recommendation.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic recommendationTopic() {
        return new NewTopic("user-activity", 1, (short) 1);
    }
}
