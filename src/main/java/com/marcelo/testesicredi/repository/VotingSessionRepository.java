package com.marcelo.testesicredi.repository;

import com.marcelo.testesicredi.document.VotingSession;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface VotingSessionRepository extends ReactiveMongoRepository<VotingSession, String> {
}
