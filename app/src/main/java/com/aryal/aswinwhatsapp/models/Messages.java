package com.aryal.aswinwhatsapp.models;

public class Messages {
    String uid,message,messageId;
    Long timeStramp;


    public Messages(String uid, String message, Long timeStramp) {
        this.uid = uid;
        this.message = message;
        this.timeStramp = timeStramp;
    }

    public Messages() {
    }

    public Messages(String uid, String message) {
        this.uid = uid;
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimeStramp() {
        return timeStramp;
    }

    public void setTimeStramp(Long timeStramp) {
        this.timeStramp = timeStramp;
    }
}

