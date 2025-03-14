package com.example.recommendation.dto;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing user preferences.
 */
public class UserPreferences {
    /**
     * The ID of the user.
     */
    private Long userId;

    /**
     * List of product IDs that the user has viewed.
     */
    private List<Long> viewedProducts;

    /**
     * Gets the user ID.
     *
     * @return the user ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the user ID.
     *
     * @param userId the user ID to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Gets the list of viewed product IDs.
     *
     * @return the list of viewed product IDs
     */
    public List<Long> getViewedProducts() {
        return viewedProducts;
    }

    /**
     * Sets the list of viewed product IDs.
     *
     * @param viewedProducts the list of viewed product IDs to set
     */
    public void setViewedProducts(List<Long> viewedProducts) {
        this.viewedProducts = viewedProducts;
    }
}