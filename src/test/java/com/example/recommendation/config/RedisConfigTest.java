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

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigApplicationContext(RedisConfig.class);
    }

    /**
     * Test for the redisConnectionFactory bean.
     */
    @Test
    void testRedisConnectionFactory() {
        RedisConnectionFactory redisConnectionFactory = context.getBean(RedisConnectionFactory.class);
        assertNotNull(redisConnectionFactory, "The redisConnectionFactory bean should not be null");
    }

    /**
     * Test for the redisTemplate bean.
     */
    @Test
    void testRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = context.getBean(RedisTemplate.class);
        assertNotNull(redisTemplate, "The redisTemplate bean should not be null");
    }
}