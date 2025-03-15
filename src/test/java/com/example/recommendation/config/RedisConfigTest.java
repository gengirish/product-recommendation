package com.example.recommendation.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for the RedisConfig configuration.
 */
class RedisConfigTest {

    private AnnotationConfigApplicationContext context;

    /**
     * Sets up the application context before each test.
     */
    @BeforeEach
    void setUp() {
        context = new AnnotationConfigApplicationContext(RedisConfig.class);
    }

    /**
     * Tests the redisConnectionFactory bean.
     * Verifies that the redisConnectionFactory bean is not null.
     */
    @Test
    void testRedisConnectionFactory() {
        RedisConnectionFactory redisConnectionFactory = context.getBean(RedisConnectionFactory.class);
        assertNotNull(redisConnectionFactory, "The redisConnectionFactory bean should not be null");
    }

    /**
     * Tests the redisTemplate bean.
     * Verifies that the redisTemplate bean is not null.
     */
    @Test
    void testRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = context.getBean(RedisTemplate.class);
        assertNotNull(redisTemplate, "The redisTemplate bean should not be null");
    }
}