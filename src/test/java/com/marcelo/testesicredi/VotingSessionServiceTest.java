package com.marcelo.testesicredi;

import com.marcelo.testesicredi.document.Topic;
import com.marcelo.testesicredi.document.Voter;
import com.marcelo.testesicredi.document.VotingSession;
import com.marcelo.testesicredi.helpers.VoteException;
import com.marcelo.testesicredi.helpers.VoteSessionException;
import com.marcelo.testesicredi.helpers.VotingSessionStausEnum;
import com.marcelo.testesicredi.repository.VotingSessionRepository;
import com.marcelo.testesicredi.services.TopicService;
import com.marcelo.testesicredi.services.VoterService;
import com.marcelo.testesicredi.services.VotingSessionServiceImp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
public class VotingSessionServiceTest {

    @TestConfiguration
    static class VotingSessionServiceTestConfiguration {
        @Bean
        public VotingSessionServiceImp votingSessionServiceImp() {
            return new VotingSessionServiceImp();
        }
    }

    @Autowired
    private VotingSessionServiceImp votingSessionService;

    @MockBean
    private TopicService topicService;
    @MockBean
    private VoterService voterService;
    @MockBean
    private VotingSessionRepository votingSessionRepository;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setup() {
        Topic topic = new Topic("xyz123", "Topic 1: Title Test", "Topic 1 Description Test");
        Mockito.when(topicService.findById("xyz123")).thenReturn(Mono.just(topic));
        Mockito.when(topicService.findById("null_123")).thenReturn(Mono.just(new Topic()));

        VotingSession votingSession = new VotingSession(5, topic.getId());
        votingSession.setId("session_xyz");
        votingSession.setTopicTitle(topic.getTitle());
        votingSession.setStartDate(LocalDateTime.now().minusMinutes(2));
        votingSession.setEndDate(LocalDateTime.now().plusMinutes(3));
        votingSession.addApprover(new Voter("Marcelo", "36108752084"));
        votingSession.addApprover(new Voter("Marjorie", "43985811032"));
        votingSession.addDisapprover(new Voter("Rafael", "55516855004"));
        Mockito.when(votingSessionRepository.findById("session_xyz")).thenReturn(Mono.just(votingSession));

        VotingSession votingClosed = new VotingSession(5, topic.getId());
        votingClosed.setId("closed_xyz");
        votingClosed.setStartDate(LocalDateTime.now().minusMinutes(25));
        votingClosed.setEndDate(LocalDateTime.now().minusMinutes(20));
        Mockito.when(votingSessionRepository.findById("closed_xyz")).thenReturn(Mono.just(votingClosed));

        VotingSession votingWaiting = new VotingSession(5, topic.getId());
        votingWaiting.setId("waiting_xyz");
        votingWaiting.setStartDate(LocalDateTime.now().plusMinutes(20));
        votingWaiting.setEndDate(LocalDateTime.now().plusMinutes(25));
        Mockito.when(votingSessionRepository.findById("waiting_xyz")).thenReturn(Mono.just(votingWaiting));
    }

    @Test
    public void createVotingSessionTest() {
        VotingSession vt = new VotingSession(2, "xyz123");

        VotingSession created = votingSessionService.createVotingSession(vt);

        Assert.assertEquals(created.getTopicTitle(), "Topic 1: Title Test");
        Assert.assertEquals(created.getStatus(), VotingSessionStausEnum.in_progress.text);
    }



    @Test
    public void whenExceptionThrown_createVotingSession() {
        exceptionRule.expect(VoteSessionException.class);
        exceptionRule.expectMessage("Erro: Tópico não encontrado ou vazio.");
        VotingSession vt = new VotingSession(2, "null_123");
        votingSessionService.createVotingSession(vt);
    }

    @Test
    public void whenExceptionThrown_votingSessionWaiting_gatValidatedVotingSession() {
        exceptionRule.expect(VoteException.class);
        exceptionRule.expectMessage("Erro: Votação não iniciada.");
        votingSessionService.gatValidatedVotingSession("waiting_xyz");
    }

    @Test
    public void whenExceptionThrown_votingSessionClosed_gatValidatedVotingSession() {
        exceptionRule.expect(VoteException.class);
        exceptionRule.expectMessage("Erro: Votação encerrada.");
        votingSessionService.gatValidatedVotingSession("closed_xyz");
    }

    @Test
    public void whenExceptionThrown_validateSingleVote_Approver() {
        exceptionRule.expect(VoteException.class);
        exceptionRule.expectMessage("Erro: CPF já votou nesta pauta.");

        VotingSession vt = votingSessionService.gatValidatedVotingSession("session_xyz");
        votingSessionService.validateSingleVote(vt, "36108752084");
    }

    @Test
    public void whenExceptionThrown_validateSingleVote_Disapprover() {
        exceptionRule.expect(VoteException.class);
        exceptionRule.expectMessage("Erro: CPF já votou nesta pauta.");

        VotingSession vt = votingSessionService.gatValidatedVotingSession("session_xyz");
        votingSessionService.validateSingleVote(vt, "55516855004");
    }
}
