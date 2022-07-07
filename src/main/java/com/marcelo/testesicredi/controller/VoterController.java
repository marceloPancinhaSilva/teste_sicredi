package com.marcelo.testesicredi.controller;

import com.marcelo.testesicredi.document.Voter;
import com.marcelo.testesicredi.services.VoterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/voter")
public class VoterController {

    @Autowired
    private VoterService voterService;

    @GetMapping
    public Flux<Voter> getAllVoters() {
        return voterService.findAll();
    }

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<Voter>> getVoterById(@PathVariable String id) {
        return voterService.findById(id)
                .map(voter -> ResponseEntity.ok(voter))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/save")
    public Mono<Voter> saveVoter(@RequestBody Voter voter) {
        return voterService.save(voter);
    }
}
