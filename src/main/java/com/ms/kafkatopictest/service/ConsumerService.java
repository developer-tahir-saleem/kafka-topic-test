package com.ms.kafkatopictest.service;


import com.ms.kafkatopictest.dto.ExampleDTO;
import com.ms.kafkatopictest.entitiy.ExampleEntity;
import com.ms.kafkatopictest.repository.ExampleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {
    Logger log = LoggerFactory.getLogger(ConsumerService.class);

    @Autowired
    private ExampleRepository exampleRepository;

//    @Autowired
//    ConsumerService(ExampleRepository exampleRepository) {
//        this.exampleRepository = exampleRepository;
//    }

    /**
     * Consume ExampleDTO on topic : TOPIC_EXAMPLE
     * Then save it in database.
     *
     * @param exampleDTO {@link ExampleDTO}
     */
    @KafkaListener(topics = "TOPIC_EXAMPLE", groupId = "consumer_example_dto")
    public void consumeExampleDTO(ExampleDTO exampleDTO)  {
        log.info("Received from topic=TOPIC_EXAMPLE ExampleDTO={}", exampleDTO);
        exampleRepository.save(convertToExampleEntity(exampleDTO));
        log.info("saved in database {}", exampleDTO);
    }

    /**
     * In Java world you should use an Mapper, or an dedicated service to do this.
     */
    public ExampleEntity convertToExampleEntity(ExampleDTO exampleDTO) {
        ExampleEntity exampleEntity = new ExampleEntity();
        exampleEntity.setDescription(exampleDTO.getDescription());
        exampleEntity.setName(exampleDTO.getName());
        return exampleEntity;
    }
}
