package com.marcelo.testesicredi.controller;

import com.marcelo.testesicredi.document.Topic;
import com.marcelo.testesicredi.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/topic")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping
    public Flux<Topic> getAllTopics() {
        return topicService.findAll();
    }

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<Topic>> getTopicById(@PathVariable String id) {
        return topicService.findById(id)
                .map(topic -> ResponseEntity.ok(topic))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/save")

    public Mono<Topic> saveTopic(@RequestBody Topic topic) {
        return topicService.save(topic);
    }
}
