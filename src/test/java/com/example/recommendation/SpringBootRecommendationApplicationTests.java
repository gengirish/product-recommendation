package com.example.recommendation;

import com.example.recommendation.service.RecommendationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class SpringBootRecommendationApplicationTests {

    @Autowired
    private SpringBootRecommendationApplication application;

    @MockBean
    private RecommendationService recommendationService;

    @Test
    public void testApplicationContextLoads() {
        // This test ensures that the Spring Boot application context loads successfully.
        assertNotNull(application);
    }

    @Test
    public void testLoadModelNotCalledInTestProfile() {
        // Verify that the loadModel method is NOT called when the "test" profile is active.
        verify(recommendationService, times(0)).loadModel(Mockito.anyString());
    }

}