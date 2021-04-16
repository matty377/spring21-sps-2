package com.google.sps.data;

public class Message {
    private String sentMsg;
    private String sentUrl;

    public Message(String sentUrl, String sentMsg) {
        this.sentMsg = sentMsg;
        this.sentUrl = sentUrl;
    }

    public String getMsg() {
        return sentMsg;
    }

    public String getUrl() {
        return sentUrl;
    }
}