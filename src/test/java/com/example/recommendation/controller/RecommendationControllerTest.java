package com.example.recommendation.controller;

import com.example.recommendation.model.Product;
import com.example.recommendation.service.ProductService;
import com.example.recommendation.service.RecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the RecommendationController class.
 */
class RecommendationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RecommendationService recommendationService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private RecommendationController recommendationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(recommendationController).build();
    }

    /**
     * Test fetching recommendations for a user.
     */
    @Test
    @DisplayName("Should return recommendations for a user")
    void getRecommendations_Success() throws Exception {
        Long userId = 1L;
        List<Product> recommendations = Arrays.asList(new Product(101L, "Product A"), new Product(102L, "Product B"));

        when(recommendationService.getRecommendations(userId)).thenReturn(recommendations);

        mockMvc.perform(get("/api/recommendations/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(recommendationService, times(1)).getRecommendations(userId);
    }

    /**
     * Test updating user preferences with viewed products.
     */
    @Test
    @DisplayName("Should update user preferences successfully")
    void updateUserPreferences_Success() throws Exception {
        Long userId = 1L;
        List<Long> viewedProducts = Arrays.asList(201L, 202L, 203L);

        doNothing().when(recommendationService).updateUserPreferences(userId, viewedProducts);

        mockMvc.perform(post("/api/recommendations/user-preferences")
                        .param("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[201, 202, 203]"))
                .andExpect(status().isOk())
                .andExpect(content().string("User preferences updated successfully."));

        verify(recommendationService, times(1)).updateUserPreferences(userId, viewedProducts);
    }

    /**
     * Test fetching similar products.
     */
    @Test
    @DisplayName("Should return similar products for a given product ID")
    void getSimilarProducts_Success() throws Exception {
        Long productId = 301L;
        List<Product> similarProducts = Arrays.asList(new Product(302L, "Product B"), new Product(303L, "Product C"));

        when(productService.getSimilarProducts(productId)).thenReturn(similarProducts);

        mockMvc.perform(get("/api/recommendations/products/similar/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(productService, times(1)).getSimilarProducts(productId);
    }

    /**
     * Test filtering products with multiple criteria.
     */
    @Test
    @DisplayName("Should return filtered products based on criteria")
    void filterProducts_Success() throws Exception {
        List<Product> filteredProducts = Arrays.asList(new Product(401L, "Filtered Product A"));

        when(productService.filterProducts("Electronics", 100.0, 500.0, "BrandX")).thenReturn(filteredProducts);

        mockMvc.perform(get("/api/recommendations/products/filter")
                        .param("category", "Electronics")
                        .param("minPrice", "100")
                        .param("maxPrice", "500")
                        .param("brand", "BrandX"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(productService, times(1)).filterProducts("Electronics", 100.0, 500.0, "BrandX");
    }

    /**
     * Test handling of missing parameters in filterProducts.
     */
    @Test
    @DisplayName("Should return empty list when no filters are applied")
    void filterProducts_NoFilters() throws Exception {
        when(productService.filterProducts(null, null, null, null)).thenReturn(List.of());

        mockMvc.perform(get("/api/recommendations/products/filter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(productService, times(1)).filterProducts(null, null, null, null);
    }
}