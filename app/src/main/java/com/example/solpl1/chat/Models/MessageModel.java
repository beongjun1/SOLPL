package com.example.solpl1.chat.Models;

public class MessageModel {
    String userId, message;
    Long timestemp;

    public MessageModel() {
    }

    public MessageModel(String userId, String message, Long timestemp) {
        this.userId = userId;
        this.message = message;
        this.timestemp = timestemp;
    }

    public MessageModel(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestemp() {
        return timestemp;
    }

    public void setTimestemp(Long timestemp) {
        this.timestemp = timestemp;
    }
}
