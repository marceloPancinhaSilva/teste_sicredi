package com.marcelo.testesicredi.repository;

import com.marcelo.testesicredi.document.Voter;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface VoterRepository extends ReactiveMongoRepository<Voter, String> {
    Mono<Voter> findByIdOrCpf(String idOrCpf, String idOrCpf2);
    Mono<Voter> findFirstByCpf(String cpf);
}
