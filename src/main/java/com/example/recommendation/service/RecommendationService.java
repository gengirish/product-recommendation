package com.example.recommendation.service;

import com.example.recommendation.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.TensorFlowException;

import java.util.List;

@Service
public class RecommendationService {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);

    @Autowired
    private UserBehaviorService userBehaviorService;

    @Autowired
    private ProductService productService;

    private SavedModelBundle model;

    public void loadModel(String modelPath) {
        try {
            model = SavedModelBundle.load(modelPath, "serve");
            logger.info("TensorFlow model loaded successfully from {}", modelPath);
        } catch (TensorFlowException e) {
            logger.error("Failed to load TensorFlow model", e);
        }
    }

    @Cacheable(value = "recommendations", key = "#userId")
    public List<Product> getRecommendations(Long userId) {
        List<Long> viewedProducts = userBehaviorService.fetchUserActivity(userId);
        if (viewedProducts.isEmpty()) {
            logger.warn("No user activity found for user: {}. Returning popular products.", userId);
            return productService.getPopularProducts();
        }

        try (Session session = model.session()) {
            Tensor<Long> inputTensor = Tensor.create(viewedProducts.stream().mapToLong(Long::longValue).toArray(), Long.class);
            List<Tensor<?>> outputTensors = session.runner().feed("input_layer", inputTensor).fetch("output_layer").run();

            if (outputTensors.isEmpty()) {
                logger.error("No output from TensorFlow model for user: {}", userId);
                return List.of();
            }

            Tensor<Long> outputTensor = outputTensors.get(0).expect(Long.class);
            long[] recommendedProductIds = new long[(int) outputTensor.shape()[0]];
            outputTensor.copyTo(recommendedProductIds);
            outputTensor.close();

            return mapIdsToProducts(recommendedProductIds);
        } catch (Exception e) {
            logger.error("Error during recommendation computation for user: {}", userId, e);
            return List.of();
        }
    }

    private List<Product> mapIdsToProducts(long[] productIds) {
        return productService.getProductsByIds(productIds);
    }

    public void updateUserPreferences(Long userId, List<Long> viewedProducts) {
        userBehaviorService.storeUserActivity(userId, viewedProducts);
    }

    public void trainRecommendationModel() {
        try (Session session = model.session()) {
            logger.info("Starting model retraining...");
            session.runner().addTarget("train").run();
            logger.info("Model retraining completed successfully.");
        } catch (Exception e) {
            logger.error("Error during model retraining", e);
        }
    }
}