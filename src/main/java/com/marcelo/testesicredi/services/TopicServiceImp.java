package com.marcelo.testesicredi.services;

import com.marcelo.testesicredi.document.Topic;
import com.marcelo.testesicredi.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TopicServiceImp implements TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Override
    public Flux<Topic> findAll() {
        return topicRepository.findAll();
    }

    @Override
    public Mono<Topic> findById(String id) {
        return topicRepository.findById(id);
    }

    @Override
    public Mono<Topic> save(Topic topic) {
        return topicRepository.save(topic);
    }
}
