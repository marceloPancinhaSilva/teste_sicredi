package com.marcelo.testesicredi.services;

import com.marcelo.testesicredi.document.Topic;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TopicService {
    Flux<Topic> findAll();
    Mono<Topic> findById(String id);
    Mono<Topic> save(Topic topic);
}
