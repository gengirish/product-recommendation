package com.example.recommendation.repository;

import com.example.recommendation.model.Product;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Testcontainers
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Disabled
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

    @BeforeAll
    static void startContainer() {
        mongoDBContainer.start();
    }

    @AfterAll
    static void stopContainer() {
        mongoDBContainer.stop();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        addTestProducts();
        assertThat(productRepository.count()).isEqualTo(3);
    }

    private void addTestProducts() {
        productRepository.saveAll(List.of(
                new Product(1L, "Laptop", "Electronics", "Dell", BigDecimal.TEN, "DESC1"),
                new Product(2L, "Smartphone", "Electronics", "Apple", BigDecimal.TEN, "DESC2"),
                new Product(3L, "Headphones", "Accessories", "Sony", BigDecimal.TEN, "DESC")
        ));
    }

    @Test
    @Order(1)
    void testFindByCategory() {
        List<Product> products = productRepository.findByCategory("Electronics");
        assertThat(products).hasSize(2);
    }

    @Test
    @Order(2)
    void testFindByBrand() {
        List<Product> products = productRepository.findByBrand("Sony");
        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("Headphones");
    }

    @Test
    @Order(3)
    void testFindByPriceBetween() {
        List<Product> products = productRepository.findByPriceBetween(200.0, 1300.0);
        assertThat(products).hasSize(2);
    }

    @Test
    @Order(4)
    void testFindByCategoryAndBrand() {
        List<Product> products = productRepository.findByCategoryAndBrand("Electronics", "Apple");
        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("Smartphone");
    }

    @Test
    @Order(5)
    void testFindTop10ByOrderByPopularityDesc() {
        List<Product> products = productRepository.findTop10ByOrderByPopularityDesc();
        assertThat(products.get(0).getPopularity()).isEqualTo(200);
    }
}
