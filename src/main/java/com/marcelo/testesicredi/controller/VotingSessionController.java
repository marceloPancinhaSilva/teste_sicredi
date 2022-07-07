package com.marcelo.testesicredi.controller;

import com.marcelo.testesicredi.document.VotingSession;
import com.marcelo.testesicredi.helpers.VoteException;
import com.marcelo.testesicredi.services.VotingSessionService;
import com.marcelo.testesicredi.viewmodel.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/voting-session")
public class VotingSessionController {

    @Autowired
    private VotingSessionService votingSessionService;

    @GetMapping
    public Flux<VotingSession> getAllVotingSessions() {
        return votingSessionService.findAll();
    }

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<VotingSession>> getVotingSessionById(@PathVariable String id) {
        return votingSessionService.findById(id)
                .map(votingSession -> ResponseEntity.ok(votingSession))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/create")
    public Mono<VotingSession> createVotingSession(@RequestBody VotingSession votingSession) {
        return votingSessionService.create(votingSession);
    }

    @PostMapping(value = "/vote")
    public Mono<VotingSession> voteInVotingSession(@RequestBody Vote vote) {
        try {
            return votingSessionService.vote(vote.votingSessionId, vote.idOrCpf, vote.vote);
        } catch (VoteException e) {
            return Mono.error(e);
        } catch (Exception e) {
            return Mono.error(new Exception("Internal erro."));
        }
    }
}
