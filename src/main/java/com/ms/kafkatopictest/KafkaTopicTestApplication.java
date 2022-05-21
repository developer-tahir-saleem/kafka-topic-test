package com.ms.kafkatopictest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.ms")
@EntityScan(basePackages={"com.ms.kafkatopictest.entitiy"})
@EnableJpaRepositories(basePackages={"com.ms.kafkatopictest.repository"})
public class KafkaTopicTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaTopicTestApplication.class, args);
	}

}
