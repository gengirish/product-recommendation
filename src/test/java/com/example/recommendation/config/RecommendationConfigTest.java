package com.example.recommendation.config;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.tensorflow.SavedModelBundle;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class RecommendationConfigTest {

    @Mock
    private SavedModelBundle savedModelBundle;

    @InjectMocks
    private RecommendationConfig recommendationConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test for Kafka consumer bean creation.
     */
    @Test
    void testKafkaConsumer() {
        KafkaConsumer<String, String> kafkaConsumer = recommendationConfig.kafkaConsumer();
        assertNotNull(kafkaConsumer);
    }

    /**
     * Test for TensorFlow model loading.
     */
    @Test
    @Disabled
    void testRecommendationModel() {
        when(savedModelBundle.load(anyString(), anyString())).thenReturn(savedModelBundle);
        SavedModelBundle model = recommendationConfig.recommendationModel();
        assertNotNull(model);
        verify(savedModelBundle, times(1)).load("models/recommendation_model", "serve");
    }
}