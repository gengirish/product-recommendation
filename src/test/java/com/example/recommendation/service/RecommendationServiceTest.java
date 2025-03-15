package com.example.recommendation.service;

import com.example.recommendation.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationServiceTest {

    @Mock
    private UserBehaviorService userBehaviorService;

    @Mock
    private ProductService productService;

    @Mock
    private SavedModelBundle model;

    @InjectMocks
    private RecommendationService recommendationService;

    private static final Logger logger = LoggerFactory.getLogger(RecommendationServiceTest.class);

    @Mock
    private Session session;

    @Mock
    private Session.Runner runner;

    @BeforeEach
    public void setUp() {
        lenient().when(model.session()).thenReturn(session);
        lenient().when(session.runner()).thenReturn(runner);
    }

    @Test
    public void testGetRecommendations_WithUserActivity() {
        Long userId = 1L;
        List<Long> viewedProducts = List.of(101L, 102L, 103L);
        long[] recommendedProductIds = {201L, 202L, 203L};
        List<Product> recommendedProducts = List.of(
                new Product(201L, "Product A"),
                new Product(202L, "Product B"),
                new Product(203L, "Product C")
        );

        when(userBehaviorService.fetchUserActivity(userId)).thenReturn(viewedProducts);
        when(runner.feed(anyString(), any(Tensor.class))).thenReturn(runner);
        when(runner.fetch(anyString())).thenReturn(runner);
        when(runner.run()).thenReturn(List.of(Tensor.create(recommendedProductIds, Long.class)));
        when(productService.getProductsByIds(recommendedProductIds)).thenReturn(recommendedProducts);

        List<Product> recommendations = recommendationService.getRecommendations(userId);

        assertNotNull(recommendations);
        assertEquals(3, recommendations.size());
        assertEquals(recommendedProducts, recommendations);
        verify(userBehaviorService, times(1)).fetchUserActivity(userId);
        verify(runner, times(1)).run();
    }

    @Test
    public void testGetRecommendations_NoUserActivity() {
        Long userId = 2L;
        List<Product> popularProducts = List.of(
                new Product(301L, "Product D"),
                new Product(302L, "Product E"),
                new Product(303L, "Product F")
        );

        when(userBehaviorService.fetchUserActivity(userId)).thenReturn(Collections.emptyList());
        when(productService.getPopularProducts()).thenReturn(popularProducts);

        List<Product> recommendations = recommendationService.getRecommendations(userId);

        assertNotNull(recommendations);
        assertEquals(popularProducts, recommendations);
        verify(userBehaviorService, times(1)).fetchUserActivity(userId);
        verify(productService, times(1)).getPopularProducts();
    }

    @Test
    public void testGetRecommendations_ModelFailure() {
        Long userId = 3L;
        List<Long> viewedProducts = List.of(101L, 102L, 103L);

        when(userBehaviorService.fetchUserActivity(userId)).thenReturn(viewedProducts);
        when(runner.feed(anyString(), any(Tensor.class))).thenReturn(runner);
        when(runner.fetch(anyString())).thenReturn(runner);
        when(runner.run()).thenThrow(new RuntimeException("Model failure"));

        List<Product> recommendations = recommendationService.getRecommendations(userId);

        assertNotNull(recommendations);
        assertTrue(recommendations.isEmpty());
        verify(userBehaviorService, times(1)).fetchUserActivity(userId);
        verify(runner, times(1)).run();
    }

    @Test
    public void testUpdateUserPreferences() {
        Long userId = 4L;
        List<Long> viewedProducts = List.of(101L, 102L, 103L);

        recommendationService.updateUserPreferences(userId, viewedProducts);

        verify(userBehaviorService, times(1)).storeUserActivity(userId, viewedProducts);
    }

    @Test
    public void testTrainRecommendationModel_Success() {
        when(runner.addTarget(anyString())).thenReturn(runner);

        recommendationService.trainRecommendationModel();

        verify(runner, times(1)).addTarget("train");
        verify(runner, times(1)).run();
    }

    @Test
    public void testTrainRecommendationModel_Failure() {
        when(runner.addTarget(anyString())).thenReturn(runner);
        when(runner.run()).thenThrow(new RuntimeException("Training failed"));

        recommendationService.trainRecommendationModel();

        verify(runner, times(1)).addTarget("train");
        verify(runner, times(1)).run();
    }
}