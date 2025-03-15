package com.example.recommendation.service;

import com.example.recommendation.model.Product;
import com.example.recommendation.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Test")
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceTest.class);

    private Product sampleProduct;

    @BeforeEach
    public void setUp() {
        // Initialize a sample product for testing
        sampleProduct = new Product();
        sampleProduct.setId(1L);
        sampleProduct.setName("Sample Product");
        sampleProduct.setCategory("Electronics");
        sampleProduct.setBrand("Sample Brand");
        sampleProduct.setPrice(BigDecimal.TEN);
        sampleProduct.setPopularity(5L);
    }

    @Test
    @DisplayName("Should fetch product details for a valid product ID")
    public void testGetProductDetails_ValidProductId() {
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.of(sampleProduct));

        Product result = productService.getProductDetails(productId);

        assertNotNull(result, "Product details should not be null");
        assertEquals(sampleProduct.getId(), result.getId(), "Product ID should match");
        assertEquals(sampleProduct.getName(), result.getName(), "Product name should match");
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    @DisplayName("Should return null for an invalid product ID")
    public void testGetProductDetails_InvalidProductId() {
        Long productId = 999L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Product result = productService.getProductDetails(productId);

        assertNull(result, "Product details should be null for an invalid product ID");
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    @DisplayName("Should fetch similar products for a valid product ID")
    public void testGetSimilarProducts_ValidProductId() {
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.findByCategoryAndBrand(sampleProduct.getCategory(), sampleProduct.getBrand()))
                .thenReturn(List.of(sampleProduct));

        List<Product> similarProducts = productService.getSimilarProducts(productId);

        assertNotNull(similarProducts, "Similar products list should not be null");
        assertFalse(similarProducts.isEmpty(), "Similar products list should not be empty");
        assertEquals(1, similarProducts.size(), "There should be one similar product");
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).findByCategoryAndBrand(sampleProduct.getCategory(), sampleProduct.getBrand());
    }

    @Test
    @DisplayName("Should return an empty list for similar products when product ID is invalid")
    public void testGetSimilarProducts_InvalidProductId() {
        Long productId = 999L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        List<Product> similarProducts = productService.getSimilarProducts(productId);

        assertNotNull(similarProducts, "Similar products list should not be null");
        assertTrue(similarProducts.isEmpty(), "Similar products list should be empty for an invalid product ID");
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).findByCategoryAndBrand(anyString(), anyString());
    }

    @Test
    @DisplayName("Should filter products based on category, price range, and brand")
    public void testFilterProducts() {
        String category = "Electronics";
        Double minPrice = 50.0;
        Double maxPrice = 150.0;
        String brand = "Sample Brand";
        when(productRepository.findByCategoryAndPriceBetweenAndBrand(category, minPrice, maxPrice, brand))
                .thenReturn(List.of(sampleProduct));

        List<Product> filteredProducts = productService.filterProducts(category, minPrice, maxPrice, brand);

        assertNotNull(filteredProducts, "Filtered products list should not be null");
        assertFalse(filteredProducts.isEmpty(), "Filtered products list should not be empty");
        assertEquals(1, filteredProducts.size(), "There should be one filtered product");
        verify(productRepository, times(1)).findByCategoryAndPriceBetweenAndBrand(category, minPrice, maxPrice, brand);
    }

    @Test
    @DisplayName("Should fetch popular products")
    public void testGetPopularProducts() {
        when(productRepository.findTop10ByOrderByPopularityDesc()).thenReturn(List.of(sampleProduct));

        List<Product> popularProducts = productService.getPopularProducts();

        assertNotNull(popularProducts, "Popular products list should not be null");
        assertFalse(popularProducts.isEmpty(), "Popular products list should not be empty");
        assertEquals(1, popularProducts.size(), "There should be one popular product");
        verify(productRepository, times(1)).findTop10ByOrderByPopularityDesc();
    }

    @Test
    @DisplayName("Should return an empty list when no popular products are found")
    public void testGetPopularProducts_NoProductsFound() {
        when(productRepository.findTop10ByOrderByPopularityDesc()).thenReturn(Collections.emptyList());

        List<Product> popularProducts = productService.getPopularProducts();

        assertNotNull(popularProducts, "Popular products list should not be null");
        assertTrue(popularProducts.isEmpty(), "Popular products list should be empty");
        verify(productRepository, times(1)).findTop10ByOrderByPopularityDesc();
    }

    @Test
    @DisplayName("Should fetch products by their IDs")
    public void testGetProductsByIds() {
        long[] productIds = {1L, 2L, 3L};
        when(productRepository.findByIdIn(productIds)).thenReturn(List.of(sampleProduct));

        List<Product> products = productService.getProductsByIds(productIds);

        assertNotNull(products, "Products list should not be null");
        assertFalse(products.isEmpty(), "Products list should not be empty");
        assertEquals(1, products.size(), "There should be one product");
        verify(productRepository, times(1)).findByIdIn(productIds);
    }
}