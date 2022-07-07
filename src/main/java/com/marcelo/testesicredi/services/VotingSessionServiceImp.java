package com.marcelo.testesicredi.services;

import com.marcelo.testesicredi.document.Topic;
import com.marcelo.testesicredi.document.Voter;
import com.marcelo.testesicredi.document.VotingSession;
import com.marcelo.testesicredi.helpers.VoteException;
import com.marcelo.testesicredi.helpers.VoteSessionException;
import com.marcelo.testesicredi.helpers.VotingSessionStausEnum;
import com.marcelo.testesicredi.repository.VotingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class VotingSessionServiceImp implements VotingSessionService {

    @Autowired
    private VotingSessionRepository votingSessionRepository;
    @Autowired
    private TopicService topicService;
    @Autowired
    private VoterService voterService;


    String voteApprover = "sim";
    String voteDisapprover = "não";

    @Override
    public Flux<VotingSession> findAll() {
        return votingSessionRepository.findAll();
    }

    @Override
    public Mono<VotingSession> findById(String id) {
        return votingSessionRepository.findById(id);
    }

    @Override
    public Mono<VotingSession> create(VotingSession votingSession) throws VoteSessionException{
        votingSession = createVotingSession(votingSession);
        return votingSessionRepository.save(votingSession);
    }

    public VotingSession createVotingSession(VotingSession votingSession) throws VoteSessionException{
        Mono<Topic> topicMono = topicService.findById(votingSession.getTopicId());
        Topic topic = topicMono.toProcessor().block();

        if (topic == null || topic.getTitle() == null)
            throw new VoteSessionException("Erro: Tópico não encontrado ou vazio.");

        votingSession.setStartDate(LocalDateTime.now());
        votingSession.setEndDate(LocalDateTime.now().plusMinutes(votingSession.getDuration()));

        votingSession.setTopicTitle(topic.getTitle());
        votingSession.setTopicDescription(topic.getDescription());

        return votingSession;
    }

    @Override
    public Mono<VotingSession> vote(String votingSessionId, String idOrCpf, String vote) throws VoteException {

        VotingSession votingSession = this.gatValidatedVotingSession(votingSessionId);

        Voter voter = this.gatValidatedVoter(idOrCpf);

        validateSingleVote(votingSession, voter.getCpf());

        if (vote.equalsIgnoreCase(voteApprover))
            votingSession.addApprover(voter);
        else if (vote.equalsIgnoreCase(voteDisapprover))
            votingSession.addDisapprover(voter);
        else
            throw new VoteException("Erro: Opção de voto invalida.");

        return votingSessionRepository.save(votingSession);
    }

    public void validateSingleVote(VotingSession votingSession, String voterCpf) {
        if ((votingSession.getApprovers() != null &&
             votingSession.getApprovers().stream().filter(e -> e.getCpf().equals(voterCpf)).findFirst().isPresent()) ||
            (votingSession.getDisapprovers() != null &&
             votingSession.getDisapprovers().stream().filter(e -> e.getCpf().equals(voterCpf)).findFirst().isPresent()))
            throw new VoteException("Erro: CPF já votou nesta pauta.");
    }

    public Voter gatValidatedVoter(String idOrCpf) {
        Mono<Voter> voterMono = voterService.findByCpfOrId(idOrCpf);
        Voter voter = voterMono.toProcessor().block();

        if (voter == null)
            throw new VoteException("Erro: CPF ou ID não encontrado.");

        return voter;
    }

    public VotingSession gatValidatedVotingSession(String votingSessionId) {
        Mono<VotingSession> votingSessionMono = votingSessionRepository.findById(votingSessionId);
        VotingSession votingSession = votingSessionMono.toProcessor().block();

        if (votingSession == null)
            throw new VoteException("Erro: Pauta não encontrata para votação.");

        if (votingSession.getStatus().equals(VotingSessionStausEnum.waiting.text))
            throw new VoteException("Erro: Votação não iniciada.");

        if (votingSession.getStatus().equals(VotingSessionStausEnum.closed.text))
            throw new VoteException("Erro: Votação encerrada.");

        return votingSession;
    }



}
