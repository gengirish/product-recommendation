package com.example.recommendation.controller;

import com.example.recommendation.model.Product;
import com.example.recommendation.service.ProductService;
import com.example.recommendation.service.RecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationController.class);

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private ProductService productService;

    /**
     * Fetch personalized recommendations for a user.
     *
     * @param userId The user ID
     * @return List of recommended product IDs
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<Product>> getRecommendations(@PathVariable Long userId) {
        logger.info("Fetching recommendations for userId: {}", userId);
        List<Product> recommendations = recommendationService.getRecommendations(userId);
        return ResponseEntity.ok(recommendations);
    }

    /**
     * Update user preferences based on viewed products.
     *
     * @param userId         The user ID
     * @param viewedProducts List of viewed product IDs
     * @return Success message
     */
    @PostMapping("/user-preferences")
    public ResponseEntity<String> updateUserPreferences(@RequestParam Long userId, @RequestBody List<Long> viewedProducts) {
        logger.info("Updating user preferences for userId: {}", userId);
        recommendationService.updateUserPreferences(userId, viewedProducts);
        return ResponseEntity.ok("User preferences updated successfully.");
    }

    /**
     * Fetch similar products for a given product.
     *
     * @param productId The product ID
     * @return List of similar product IDs
     */
    @GetMapping("/products/similar/{productId}")
    public ResponseEntity<List<Product>> getSimilarProducts(@PathVariable Long productId) {
        logger.info("Fetching similar products for productId: {}", productId);
        List<Product> similarProducts = productService.getSimilarProducts(productId);
        return ResponseEntity.ok(similarProducts);
    }

    /**
     * Get products based on filters.
     *
     * @param category Product category
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @param brand    Product brand
     * @return Filtered products
     */
    @GetMapping("/products/filter")
    public ResponseEntity<List<?>> filterProducts(@RequestParam(required = false) String category,
                                                  @RequestParam(required = false) Double minPrice,
                                                  @RequestParam(required = false) Double maxPrice,
                                                  @RequestParam(required = false) String brand) {
        logger.info("Filtering products - Category: {}, Min Price: {}, Max Price: {}, Brand: {}", category, minPrice, maxPrice, brand);
        List<?> filteredProducts = productService.filterProducts(category, minPrice, maxPrice, brand);
        return ResponseEntity.ok(filteredProducts);
    }
}