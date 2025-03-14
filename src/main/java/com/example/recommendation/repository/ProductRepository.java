package com.example.recommendation.repository;

import com.example.recommendation.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, Long> {
    /**
     * Finds products by category.
     *
     * @param category the category of the products.
     * @return a list of products in the given category.
     */
    List<Product> findByCategory(String category);

    /**
     * Finds products by brand.
     *
     * @param brand the brand of the products.
     * @return a list of products by the given brand.
     */
    List<Product> findByBrand(String brand);

    /**
     * Finds products within a specified price range.
     *
     * @param minPrice the minimum price.
     * @param maxPrice the maximum price.
     * @return a list of products within the given price range.
     */
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    /**
     * Finds similar products based on category and brand.
     *
     * @param category the category of the product.
     * @param brand    the brand of the product.
     * @return a list of similar products.
     */
    List<Product> findByCategoryAndBrand(String category, String brand);

    /**
     * Finds products based on filters: category, price range, and brand.
     *
     * @param category the category of the product.
     * @param minPrice the minimum price.
     * @param maxPrice the maximum price.
     * @param brand    the brand of the product.
     * @return a list of products matching the given filters.
     */
    List<Product> findByCategoryAndPriceBetweenAndBrand(String category, Double minPrice, Double maxPrice, String brand);

    /**
     * Finds popular products based on a predefined popularity metric (e.g., sales, views, ratings).
     *
     * @return a list of popular products.
     */
    List<Product> findTop10ByOrderByPopularityDesc();

    /**
     * Finds products by their IDs.
     *
     * @param productIds Array of product IDs.
     * @return A list of products with the given IDs.
     */
    List<Product> findByIdIn(long[] productIds);
}