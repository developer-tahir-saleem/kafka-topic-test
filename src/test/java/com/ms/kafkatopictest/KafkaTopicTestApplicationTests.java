package com.ms.kafkatopictest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

//@SpringBootTest
//class KafkaTopicTestApplicationTests {
//
//	@Test
//	void contextLoads() {
//	}
//
//}


@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(topics = {"TOPIC_EXAMPLE", "TOPIC_EXAMPLE_EXTERNE"})
class KafkaTopicTestApplicationTests {

	@Test
	void contextLoads() {
		assertDoesNotThrow(() -> KafkaTopicTestApplication.main(new String[]{}));
	}

}