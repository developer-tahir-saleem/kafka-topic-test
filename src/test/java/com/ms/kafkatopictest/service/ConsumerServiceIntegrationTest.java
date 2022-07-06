package com.ms.kafkatopictest.service;


import com.ms.kafkatopictest.dto.ExampleDTO;
import com.ms.kafkatopictest.entitiy.ExampleEntity;
import com.ms.kafkatopictest.repository.ExampleRepository;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.awaitility.Durations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(topics = {"TOPIC_EXAMPLE", "TOPIC_EXAMPLE_EXTERNE"})
public class ConsumerServiceIntegrationTest {
    Logger log = LoggerFactory.getLogger(ConsumerServiceIntegrationTest.class);

    private static final String TOPIC_EXAMPLE = "TOPIC_EXAMPLE";

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private ConsumerService consumerService;

    public ExampleDTO mockExampleDTO(String name, String description) {
        ExampleDTO exampleDTO = new ExampleDTO();
        exampleDTO.setDescription(description);
        exampleDTO.setName(name);
        return exampleDTO;
    }

    /**
     * We verify the output in the topic. But also in the object variable .
     */
    @Test
    public void itShould_ConsumeCorrectExampleDTO_from_TOPIC_EXAMPLE_and_should_stateCorrectExampleEntityOnObjectVariable() throws ExecutionException, InterruptedException {
        // GIVEN  Prepare mock Data
        ExampleDTO exampleDTO = mockExampleDTO("Name 2", "Description 2");

        // Embedded kafka producer simulation
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker.getBrokersAsString());
        Producer<String, ExampleDTO> embeddedProducer = new KafkaProducer(producerProps, new StringSerializer(), new JsonSerializer<ExampleDTO>());
        // Print logs
        log.info("props {}", producerProps);

        // WHEN send mock data to embedded kafka Topic
        embeddedProducer.send(new ProducerRecord(TOPIC_EXAMPLE, "", exampleDTO));

        // THEN wait for ten second get from class consumerService class variable data match with mock send data
        await().atMost(Durations.TEN_SECONDS).untilAsserted(() -> {
            ExampleDTO getDataFromServiceClass = consumerService.verifyDto;
            assertEquals(exampleDTO.getDescription(), getDataFromServiceClass.getDescription());
            assertEquals(exampleDTO.getName(), getDataFromServiceClass.getName());
        });

        // END
        embeddedProducer.close();
    }


}