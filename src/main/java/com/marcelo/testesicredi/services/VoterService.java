package com.marcelo.testesicredi.services;

import com.marcelo.testesicredi.document.Voter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VoterService {
    Flux<Voter> findAll();
    Mono<Voter> findById(String id);
    Mono<Voter> save(Voter voter);
    Mono<Voter> findByCpfOrId(String idOrCpf);
}
