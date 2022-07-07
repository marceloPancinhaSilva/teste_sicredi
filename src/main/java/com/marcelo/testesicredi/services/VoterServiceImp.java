package com.marcelo.testesicredi.services;

import com.marcelo.testesicredi.document.Voter;
import com.marcelo.testesicredi.repository.VoterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VoterServiceImp implements VoterService {

    @Autowired
    private VoterRepository voterRepository;

    @Override
    public Flux<Voter> findAll() {
        return voterRepository.findAll();
    }

    @Override
    public Mono<Voter> findById(String id) {
        return voterRepository.findById(id);
    }

    private long timeout=60000;

    @Override
    public Mono<Voter> save(Voter voter) {

        if (voter.getId() == null || voter.getId().isEmpty()) {
            Mono<Voter> voterMono = voterRepository.findFirstByCpf(voter.getCpf());
            Voter temp = voterMono.toProcessor().block();
            if (temp != null)
                voter.setId(temp.getId());
        }

        return voterRepository.save(voter);
    }

    @Override
    public Mono<Voter> findByCpfOrId(String idOrCpf) {
        return voterRepository.findByIdOrCpf(idOrCpf, idOrCpf);
    }



}
