package com.marcelo.testesicredi.helpers;

public enum VotingSessionStausEnum {

    in_progress("In Progress"),
    closed("Closed"),
    waiting("Waiting"),
    approved("Approved"),
    disapproved("Disapproved"),
    tied("Tied");


    public String text;

    VotingSessionStausEnum(String text) {
        this.text = text;
    }
}
