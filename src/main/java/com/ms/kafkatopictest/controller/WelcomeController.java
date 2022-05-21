package com.ms.kafkatopictest.controller;



import com.ms.kafkatopictest.dto.ExampleDTO;
import com.ms.kafkatopictest.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class WelcomeController {



    @Autowired
    private ProducerService producerService;

    @GetMapping("/test")
    public String welcome() {
        return "Welcome to Kafka Integration Testing";
    }

    @PostMapping("/Test")
    public ExampleDTO test(@RequestBody ExampleDTO feed) {
        producerService.send(feed);
        return feed;
    }





}