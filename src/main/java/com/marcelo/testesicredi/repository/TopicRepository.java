package com.marcelo.testesicredi.repository;

import com.marcelo.testesicredi.document.Topic;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TopicRepository extends ReactiveMongoRepository<Topic, String> {

}
