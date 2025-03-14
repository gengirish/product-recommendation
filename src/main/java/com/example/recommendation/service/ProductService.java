package com.example.recommendation.service;

import com.example.recommendation.model.Product;
import com.example.recommendation.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    /**
     * Fetches product details by product ID.
     *
     * @param productId The product ID
     * @return Product details
     */
    public Product getProductDetails(Long productId) {
        logger.info("Fetching product details for productId: {}", productId);
        return productRepository.findById(productId).orElse(null);
    }

    /**
     * Fetches similar products based on product attributes.
     *
     * @param productId The product ID
     * @return List of similar product IDs
     */
    @Cacheable(value = "similarProducts", key = "#productId")
    public List<Product> getSimilarProducts(Long productId) {
        logger.info("Fetching similar products for productId: {}", productId);
        Product product = getProductDetails(productId);
        if (product == null) {
            logger.warn("Product not found for productId: {}", productId);
            return List.of();
        }
        return productRepository.findByCategoryAndBrand(product.getCategory(), product.getBrand());
    }

    /**
     * Retrieves products based on filter criteria.
     *
     * @param category Product category
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @param brand    Product brand
     * @return List of filtered products
     */
    public List<Product> filterProducts(String category, Double minPrice, Double maxPrice, String brand) {
        logger.info("Filtering products with category: {}, minPrice: {}, maxPrice: {}, brand: {}", category, minPrice, maxPrice, brand);
        return productRepository.findByCategoryAndPriceBetweenAndBrand(category, minPrice, maxPrice, brand);
    }

    /**
     * Fetches popular products for users with no activity.
     *
     * @return List of popular product IDs
     */
    @Cacheable(value = "popularProducts")
    public List<Product> getPopularProducts() {
        logger.info("Fetching popular products");
        return productRepository.findTop10ByOrderByPopularityDesc();
    }

    /**
     * Fetches products by their IDs.
     *
     * @param productIds Array of product IDs
     * @return List of products
     */
    public List<Product> getProductsByIds(long[] productIds) {
        return productRepository.findByIdIn(productIds);
    }

}