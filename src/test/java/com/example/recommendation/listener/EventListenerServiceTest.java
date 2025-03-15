package com.example.recommendation.listener;

import com.example.recommendation.service.RecommendationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Test class for {@link EventListenerService}.
 * This class tests the functionality of the EventListenerService, including handling Kafka events
 * and processing user activity to refine recommendations.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EventListenerService Tests")
public class EventListenerServiceTest {

    @Mock
    private RecommendationService recommendationService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EventListenerService eventListenerService;

    @BeforeEach
    void setUp() {
        // Initialize the ObjectMapper mock
        eventListenerService = new EventListenerService(recommendationService, objectMapper);
    }

    /**
     * Tests the {@link EventListenerService#handleUserActivity(ConsumerRecord)} method.
     * Verifies that a user activity event is processed correctly for a product click event.
     */
    @Test
    @DisplayName("Should process USER_CLICKED_PRODUCT event and update user preferences")
    void testHandleUserActivity_UserClickedProduct() throws Exception {
        // Arrange
        String eventData = "{\"userId\": 1, \"eventType\": \"USER_CLICKED_PRODUCT\", \"productId\": 101}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("user-activity-events", 0, 0, "key", eventData);

        JsonNode eventJson = mock(JsonNode.class);
        JsonNode userIdNode = mock(JsonNode.class);
        JsonNode eventTypeNode = mock(JsonNode.class);
        JsonNode productIdNode = mock(JsonNode.class);
        when(objectMapper.readTree(eventData)).thenReturn(eventJson);
        when(eventJson.get("userId")).thenReturn(userIdNode);
        when(eventJson.get("eventType")).thenReturn(eventTypeNode);
        when(eventJson.get("productId")).thenReturn(productIdNode);
        when(userIdNode.asLong()).thenReturn(1L);
        when(eventTypeNode.asText()).thenReturn("USER_CLICKED_PRODUCT");
        when(productIdNode.asLong()).thenReturn(101L);

        // Act
        eventListenerService.handleUserActivity(record);

        // Assert
        verify(recommendationService, times(1)).updateUserPreferences(1L, List.of(101L));
    }

    /**
     * Tests the {@link EventListenerService#handleUserActivity(ConsumerRecord)} method.
     * Verifies that a user activity event is processed correctly for a product purchase event.
     */
    @Test
    @DisplayName("Should process USER_BOUGHT_PRODUCT event and update user preferences")
    void testHandleUserActivity_UserBoughtProduct() throws Exception {
        // Arrange
        String eventData = "{\"userId\": 2, \"eventType\": \"USER_BOUGHT_PRODUCT\", \"productId\": 202}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("user-activity-events", 0, 0, "key", eventData);

        JsonNode eventJson = mock(JsonNode.class);
        JsonNode userIdNode = mock(JsonNode.class);
        JsonNode eventTypeNode = mock(JsonNode.class);
        JsonNode productIdNode = mock(JsonNode.class);
        when(objectMapper.readTree(eventData)).thenReturn(eventJson);
        when(eventJson.get("userId")).thenReturn(userIdNode);
        when(eventJson.get("eventType")).thenReturn(eventTypeNode);
        when(eventJson.get("productId")).thenReturn(productIdNode);
        when(userIdNode.asLong()).thenReturn(2L);
        when(eventTypeNode.asText()).thenReturn("USER_BOUGHT_PRODUCT");
        when(productIdNode.asLong()).thenReturn(202L);

        // Act
        eventListenerService.handleUserActivity(record);

        // Assert
        verify(recommendationService, times(1)).updateUserPreferences(2L, List.of(202L));
    }

    /**
     * Tests the {@link EventListenerService#handleUserActivity(ConsumerRecord)} method.
     * Verifies that a user activity event is processed correctly for a category search event.
     */
    @Test
    @DisplayName("Should process USER_SEARCHED_CATEGORY event and log the category")
    void testHandleUserActivity_UserSearchedCategory() throws Exception {
        // Arrange
        String eventData = "{\"userId\": 3, \"eventType\": \"USER_SEARCHED_CATEGORY\", \"category\": \"electronics\"}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("user-activity-events", 0, 0, "key", eventData);

        JsonNode eventJson = mock(JsonNode.class);
        JsonNode userIdNode = mock(JsonNode.class);
        JsonNode eventTypeNode = mock(JsonNode.class);
        JsonNode categoryNode = mock(JsonNode.class);
        when(objectMapper.readTree(eventData)).thenReturn(eventJson);
        when(eventJson.get("userId")).thenReturn(userIdNode);
        when(eventJson.get("eventType")).thenReturn(eventTypeNode);
        when(eventJson.get("category")).thenReturn(categoryNode);
        when(userIdNode.asLong()).thenReturn(3L);
        when(eventTypeNode.asText()).thenReturn("USER_SEARCHED_CATEGORY");
        when(categoryNode.asText()).thenReturn("electronics");

        // Act
        eventListenerService.handleUserActivity(record);

        // Assert
        verify(recommendationService, never()).updateUserPreferences(anyLong(), anyList());
    }

    /**
     * Tests the {@link EventListenerService#handleUserActivity(ConsumerRecord)} method.
     * Verifies that an unknown event type is logged as a warning.
     */
    @Test
    @DisplayName("Should log a warning for unknown event types")
    void testHandleUserActivity_UnknownEventType() throws Exception {
        // Arrange
        String eventData = "{\"userId\": 4, \"eventType\": \"UNKNOWN_EVENT\", \"productId\": 404}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("user-activity-events", 0, 0, "key", eventData);

        JsonNode eventJson = mock(JsonNode.class);
        JsonNode userIdNode = mock(JsonNode.class);
        JsonNode eventTypeNode = mock(JsonNode.class);
        JsonNode productIdNode = mock(JsonNode.class);
        when(objectMapper.readTree(eventData)).thenReturn(eventJson);
        when(eventJson.get("userId")).thenReturn(userIdNode);
        when(eventJson.get("eventType")).thenReturn(eventTypeNode);
        when(eventJson.get("productId")).thenReturn(productIdNode);
        when(userIdNode.asLong()).thenReturn(4L);
        when(eventTypeNode.asText()).thenReturn("UNKNOWN_EVENT");
        when(productIdNode.asLong()).thenReturn(404L);

        // Act
        eventListenerService.handleUserActivity(record);

        // Assert
        verify(recommendationService, never()).updateUserPreferences(anyLong(), anyList());
    }

    /**
     * Tests the {@link EventListenerService#handleUserActivity(ConsumerRecord)} method.
     * Verifies that an exception during event processing is logged as an error.
     */
    @Test
    @DisplayName("Should log an error when event processing fails")
    void testHandleUserActivity_EventProcessingFailure() throws Exception {
        // Arrange
        String eventData = "invalid-json";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("user-activity-events", 0, 0, "key", eventData);

        when(objectMapper.readTree(eventData)).thenThrow(new RuntimeException("Invalid JSON"));

        // Act
        eventListenerService.handleUserActivity(record);

        // Assert
        verify(recommendationService, never()).updateUserPreferences(anyLong(), anyList());
    }
}