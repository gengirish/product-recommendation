package com.example.recommendation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserBehaviorServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ListOperations<String, String> listOperations;

    @InjectMocks
    private UserBehaviorService userBehaviorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForList()).thenReturn(listOperations);
    }

    /**
     * Test for storing user activity in Redis.
     */
    @Test
    void testStoreUserActivity() {
        Long userId = 1L;
        List<Long> viewedProducts = List.of(101L, 102L, 103L);
        String key = "user:activity:" + userId;

        userBehaviorService.storeUserActivity(userId, viewedProducts);

        verify(listOperations).rightPushAll(key, "101", "102", "103");
        verify(redisTemplate).expire(key, 7, TimeUnit.DAYS);
    }

    /**
     * Test for fetching user activity from Redis.
     */
    @Test
    void testFetchUserActivity() {
        Long userId = 1L;
        String key = "user:activity:" + userId;
        List<String> storedData = List.of("101", "102", "103");

        when(listOperations.range(key, 0, -1)).thenReturn(storedData);

        List<Long> result = userBehaviorService.fetchUserActivity(userId);

        assertEquals(List.of(101L, 102L, 103L), result);
    }
}