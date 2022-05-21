package com.ms.kafkatopictest.repository;

import com.ms.kafkatopictest.entitiy.ExampleEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExampleRepository extends CrudRepository<ExampleEntity, Long> {
    List<ExampleEntity> findAll();
}
