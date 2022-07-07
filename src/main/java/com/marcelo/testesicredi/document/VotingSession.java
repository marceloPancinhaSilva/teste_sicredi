package com.marcelo.testesicredi.document;

import com.marcelo.testesicredi.helpers.VotingSessionStausEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document
public class VotingSession {

    @Id
    private String id;
    private Integer duration = 1;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String topicId;
    private String topicTitle;
    private String topicDescription;


    private List<Voter> approvers;

    private List<Voter> disapprovers;


    public VotingSession() {
    }

    public VotingSession(Integer duration, String topicId) {
        this.duration = duration;
        this.topicId = topicId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }

    public List<Voter> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<Voter> approvers) {
        this.approvers = approvers;
    }

    public List<Voter> getDisapprovers() {
        return disapprovers;
    }

    public void setDisapprovers(List<Voter> disapprovers) {
        this.disapprovers = disapprovers;
    }

    public void addApprover(Voter voter) {
        if (this.approvers == null)
            this.approvers = new ArrayList<>();
        this.approvers.add(voter);
    }

    public void addDisapprover(Voter voter) {
        if (this.disapprovers == null)
            this.disapprovers = new ArrayList<>();
        this.disapprovers.add(voter);
    }

    public Integer getApproversSize() {
        return (approvers == null) ? 0 : approvers.size();
    }

    public Integer getDisapproversSize() {
        return (disapprovers == null) ? 0 : disapprovers.size();
    }

    public String getStatus() {
        if (LocalDateTime.now().isBefore(this.startDate))
            return VotingSessionStausEnum.waiting.text;

        if (LocalDateTime.now().isAfter(this.endDate))
            return VotingSessionStausEnum.closed.text;

        return VotingSessionStausEnum.in_progress.text;
    }

    public String getResult() {
        if (!VotingSessionStausEnum.closed.text.equals(this.getStatus()))
            return VotingSessionStausEnum.waiting.text;

        if (this.getApproversSize() > this.getDisapproversSize())
            return VotingSessionStausEnum.approved.text;

        else if (this.getApproversSize() < this.getDisapproversSize())
            return VotingSessionStausEnum.disapproved.text;

        return VotingSessionStausEnum.tied.text;
    }
}
