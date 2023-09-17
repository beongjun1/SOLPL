package com.example.solpl1.chat.Models;

public class ChatItem {
    private String chatRoomId;  // 채팅방 id
    private String title;   // 채팅방 제목
    private String chatRoomBy;    // 방장,가이드 이름
    private String description; // 채팅방 설명
    private long time;    // 마지막 채팅 시간
    private int userCountCurrent;   // 인원수 현재
    private int userCountMax;       // 인원수 최대

    public ChatItem(String chatRoomId, String title, String chatRoomBy, String description, long time, int userCountCurrent, int userCountMax) {
        this.chatRoomId = chatRoomId;
        this.title = title;
        this.chatRoomBy = chatRoomBy;
        this.description = description;
        this.time = time;
        this.userCountCurrent = userCountCurrent;
        this.userCountMax = userCountMax;
    }


    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }


    public  ChatItem(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChatRoomBy() {
        return chatRoomBy;
    }

    public void setChatRoomBy(String chatRoomBy) {
        this.chatRoomBy = chatRoomBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getUserCountCurrent() {
        return userCountCurrent;
    }

    public void setUserCountCurrent(int userCountCurrent) {
        this.userCountCurrent = userCountCurrent;
    }

    public int getUserCountMax() {
        return userCountMax;
    }

    public void setUserCountMax(int userCountMax) {
        this.userCountMax = userCountMax;
    }
}
