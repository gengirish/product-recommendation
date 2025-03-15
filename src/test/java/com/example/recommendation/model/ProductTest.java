package com.example.recommendation.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for the Product model.
 */
class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Test Product", "Category", "Brand", new BigDecimal("99.99"), "Description");
    }

    /**
     * Test for getting the product ID.
     */
    @Test
    void testGetId() {
        assertEquals(1L, product.getId());
    }

    /**
     * Test for setting the product ID.
     */
    @Test
    void testSetId() {
        product.setId(2L);
        assertEquals(2L, product.getId());
    }

    /**
     * Test for getting the product name.
     */
    @Test
    void testGetName() {
        assertEquals("Test Product", product.getName());
    }

    /**
     * Test for setting the product name.
     */
    @Test
    void testSetName() {
        product.setName("New Product");
        assertEquals("New Product", product.getName());
    }

    /**
     * Test for getting the product category.
     */
    @Test
    void testGetCategory() {
        assertEquals("Category", product.getCategory());
    }

    /**
     * Test for setting the product category.
     */
    @Test
    void testSetCategory() {
        product.setCategory("New Category");
        assertEquals("New Category", product.getCategory());
    }

    /**
     * Test for getting the product brand.
     */
    @Test
    void testGetBrand() {
        assertEquals("Brand", product.getBrand());
    }

    /**
     * Test for setting the product brand.
     */
    @Test
    void testSetBrand() {
        product.setBrand("New Brand");
        assertEquals("New Brand", product.getBrand());
    }

    /**
     * Test for getting the product price.
     */
    @Test
    void testGetPrice() {
        assertEquals(new BigDecimal("99.99"), product.getPrice());
    }

    /**
     * Test for setting the product price.
     */
    @Test
    void testSetPrice() {
        product.setPrice(new BigDecimal("199.99"));
        assertEquals(new BigDecimal("199.99"), product.getPrice());
    }

    /**
     * Test for getting the product description.
     */
    @Test
    void testGetDescription() {
        assertEquals("Description", product.getDescription());
    }

    /**
     * Test for setting the product description.
     */
    @Test
    void testSetDescription() {
        product.setDescription("New Description");
        assertEquals("New Description", product.getDescription());
    }

    /**
     * Test for getting the product popularity.
     */
    @Test
    void testGetPopularity() {
        product.setPopularity(100L);
        assertEquals(100L, product.getPopularity());
    }

    /**
     * Test for setting the product popularity.
     */
    @Test
    void testSetPopularity() {
        product.setPopularity(200L);
        assertEquals(200L, product.getPopularity());
    }

    /**
     * Test for the toString method.
     */
    @Test
    void testToString() {
        String expected = "Product{id=1, name='Test Product', category='Category', brand='Brand', price=99.99, description='Description'}";
        assertEquals(expected, product.toString());
    }
}