package com.example.recommendation;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.example.recommendation.listener.EventListenerService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class SpringBootRecommendationApplicationTest {

	@MockBean
	private Consumer<String, String> kafkaConsumer;

	@MockBean
	private Producer<String, String> kafkaProducer;

	@MockBean
	private EventListenerService eventListenerService;

	private ListAppender<ILoggingEvent> logAppender;

	@BeforeEach
	void setUp() {
		Logger logger = (Logger) LoggerFactory.getLogger(SpringBootRecommendationApplication.class);
		logAppender = new ListAppender<>();
		logAppender.start();
		logger.addAppender(logAppender);
	}

	@Test
	void contextLoads() {
		// Verify that the application context loads successfully
		assertThat(logAppender.list)
				.extracting(ILoggingEvent::getFormattedMessage)
				.contains("Starting Spring Boot Recommendation Microservice...")
				.contains("Spring Boot Recommendation Microservice started successfully.");
	}
}