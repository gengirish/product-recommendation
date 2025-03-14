package com.example.recommendation.listener;

import com.example.recommendation.service.RecommendationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventListenerService {
    private static final Logger logger = LoggerFactory.getLogger(EventListenerService.class);

    @Autowired
    private RecommendationService recommendationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Listens to user activity events from Kafka.
     *
     * @param record The Kafka consumer record containing user activity data.
     */
    @KafkaListener(topics = "user-activity-events", groupId = "recommendation-group")
    public void handleUserActivity(ConsumerRecord<String, String> record) {
        logger.info("Received user activity event: {}", record.value());
        processUserActivity(record.value());
    }

    /**
     * Processes the user activity event to refine recommendations.
     *
     * @param eventData The event data as JSON string.
     */
    private void processUserActivity(String eventData) {
        try {
            JsonNode eventJson = objectMapper.readTree(eventData);
            Long userId = eventJson.get("userId").asLong();
            String eventType = eventJson.get("eventType").asText();
            Long productId = eventJson.get("productId").asLong();

            logger.info("Processing event - UserID: {}, EventType: {}, ProductID: {}", userId, eventType, productId);

            switch (eventType) {
                case "USER_CLICKED_PRODUCT":
                case "USER_ADDED_TO_CART":
                case "USER_BOUGHT_PRODUCT":
                    recommendationService.updateUserPreferences(userId, List.of(productId));
                    break;
                case "USER_SEARCHED_CATEGORY":
                    String category = eventJson.get("category").asText();
                    logger.info("User searched for category: {}", category);
                    break;
                default:
                    logger.warn("Unknown event type: {}", eventType);
            }
        } catch (Exception e) {
            logger.error("Error processing user activity event: {}", eventData, e);
        }
    }
}