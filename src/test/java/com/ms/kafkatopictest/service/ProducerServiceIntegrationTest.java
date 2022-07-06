package com.ms.kafkatopictest.service;


import com.ms.kafkatopictest.dto.ExampleDTO;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(topics = {"TOPIC_EXAMPLE", "TOPIC_EXAMPLE_EXTERNE"})
public class ProducerServiceIntegrationTest {
    private static final String TOPIC_EXAMPLE_EXTERNE = "TOPIC_EXAMPLE_EXTERNE";

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private ProducerService producerService;

    public ExampleDTO mockExampleDTO(String name, String description) {
        ExampleDTO exampleDTO = new ExampleDTO();
        exampleDTO.setDescription(description);
        exampleDTO.setName(name);
        return exampleDTO;
    }

    /**
     * We verify the output in the topic. With an simulated consumer.
     */
    @Test
    public void itShould_ProduceCorrectExampleDTO_to_TOPIC_EXAMPLE_EXTERNE() {
        // GIVEN create mock data object
        ExampleDTO exampleDTO = mockExampleDTO("Name", "Description");
        // Setup Embedded kafka consumer simulation
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("group_consumer_test", "false", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        ConsumerFactory cf = new DefaultKafkaConsumerFactory<String, ExampleDTO>(consumerProps, new StringDeserializer(), new JsonDeserializer<>(ExampleDTO.class, false));
        Consumer<String, ExampleDTO> consumerServiceTest = cf.createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumerServiceTest, TOPIC_EXAMPLE_EXTERNE);

        // WHEN send mock data to our implemented producer kafka service class
        producerService.send(exampleDTO);
        // THEN the embedded kafka consumer topic received the data
        ConsumerRecord<String, ExampleDTO> consumerRecordOfExampleDTO = KafkaTestUtils.getSingleRecord(consumerServiceTest, TOPIC_EXAMPLE_EXTERNE);
        ExampleDTO valueReceived = consumerRecordOfExampleDTO.value();

        //AND the mock data match with consumer data
        assertEquals(exampleDTO.getDescription(), valueReceived.getDescription());
        assertEquals(exampleDTO.getName(), valueReceived.getName());
        //END
        consumerServiceTest.close();
    }
}