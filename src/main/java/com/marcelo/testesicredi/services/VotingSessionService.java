package com.marcelo.testesicredi.services;

import com.marcelo.testesicredi.document.VotingSession;
import com.marcelo.testesicredi.helpers.VoteException;
import com.marcelo.testesicredi.helpers.VoteSessionException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VotingSessionService {
    Flux<VotingSession> findAll();
    Mono<VotingSession> findById(String id);
    Mono<VotingSession> create(VotingSession votingSession) throws VoteSessionException;
    Mono<VotingSession> vote(String votingSessionId, String idOrCpf, String vote) throws VoteException;
}
