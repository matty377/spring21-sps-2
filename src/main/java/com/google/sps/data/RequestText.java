package com.google.sps.data;


public class RequestText {
    private String sentText;

    public RequestText(String text) {
        this.sentText = text;
    }

    public String getText() {
        return sentText;
    }
}