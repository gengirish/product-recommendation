package com.example.recommendation.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tensorflow.SavedModelBundle;

import java.util.Properties;

@Configuration
public class RecommendationConfig {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationConfig.class);

    /**
     * Configures Kafka consumer settings for processing real-time events.
     */
    @Bean
    public KafkaConsumer<String, String> kafkaConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "recommendation-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new KafkaConsumer<>(props);
    }

    /**
     * Loads the TensorFlow saved model for AI-driven recommendations.
     */
    @Bean
    public SavedModelBundle recommendationModel() {
        logger.info("Loading TensorFlow model for recommendations...");
        return SavedModelBundle.load("models/recommendation_model", "serve");
    }
}