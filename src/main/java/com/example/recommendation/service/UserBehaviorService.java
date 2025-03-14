package com.example.recommendation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserBehaviorService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String USER_ACTIVITY_KEY = "user:activity:";
    private static final long EXPIRATION_TIME = 7; // Store data for 7 days

    /**
     * Stores user activity in Redis for fast retrieval.
     *
     * @param userId         ID of the user
     * @param viewedProducts List of product IDs the user interacted with
     */
    public void storeUserActivity(Long userId, List<Long> viewedProducts) {
        String key = USER_ACTIVITY_KEY + userId;
        redisTemplate.opsForList().rightPushAll(key, viewedProducts.stream().map(String::valueOf).toArray(String[]::new));
        redisTemplate.expire(key, EXPIRATION_TIME, TimeUnit.DAYS);
    }

    /**
     * Retrieves the recent activity of a user.
     *
     * @param userId ID of the user
     * @return List of product IDs interacted with
     */
    public List<Long> fetchUserActivity(Long userId) {
        String key = USER_ACTIVITY_KEY + userId;
        List<String> storedData = redisTemplate.opsForList().range(key, 0, -1);
        return storedData != null ? storedData.stream().map(Long::valueOf).toList() : List.of();
    }
}