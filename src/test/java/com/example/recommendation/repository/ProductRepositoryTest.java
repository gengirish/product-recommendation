package com.example.recommendation.repository;

import com.example.recommendation.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ProductRepository} class.
 */
@ExtendWith(SpringExtension.class)
@DataMongoTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private Product product1;
    private Product product2;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    public void setUp() {
        productRepository.deleteAll();

        product1 = new Product(1L, "Product 1", "Category 1", "Brand 1", BigDecimal.TEN, "desc1");
        product2 = new Product(2L, "Product 2", "Category 2", "Brand 2", BigDecimal.TEN, "desc2");

        productRepository.save(product1);
        productRepository.save(product2);
    }

    /**
     * Tests the findByCategory method.
     */
    @Test
    public void testFindByCategory() {
        List<Product> products = productRepository.findByCategory("Category 1");
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(product1, products.get(0));
    }

    /**
     * Tests the findByBrand method.
     */
    @Test
    public void testFindByBrand() {
        List<Product> products = productRepository.findByBrand("Brand 2");
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(product2, products.get(0));
    }

    /**
     * Tests the findByPriceBetween method.
     */
    @Test
    public void testFindByPriceBetween() {
        List<Product> products = productRepository.findByPriceBetween(50.0, 150.0);
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(product1, products.get(0));
    }

    /**
     * Tests the findByCategoryAndBrand method.
     */
    @Test
    public void testFindByCategoryAndBrand() {
        List<Product> products = productRepository.findByCategoryAndBrand("Category 1", "Brand 1");
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(product1, products.get(0));
    }

    /**
     * Tests the findByCategoryAndPriceBetweenAndBrand method.
     */
    @Test
    public void testFindByCategoryAndPriceBetweenAndBrand() {
        List<Product> products = productRepository.findByCategoryAndPriceBetweenAndBrand("Category 2", 150.0, 250.0, "Brand 2");
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(product2, products.get(0));
    }

    /**
     * Tests the findTop10ByOrderByPopularityDesc method.
     */
    @Test
    public void testFindTop10ByOrderByPopularityDesc() {
        List<Product> products = productRepository.findTop10ByOrderByPopularityDesc();
        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals(product2, products.get(0));
        assertEquals(product1, products.get(1));
    }

    /**
     * Tests the findByIdIn method.
     */
    @Test
    public void testFindByIdIn() {
        List<Product> products = productRepository.findByIdIn(new long[]{1L, 2L});
        assertNotNull(products);
        assertEquals(2, products.size());
        assertTrue(products.contains(product1));
        assertTrue(products.contains(product2));
    }
}