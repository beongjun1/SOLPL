package com.example.solpl1.mainPost.Models;

public class MainPost {
    private String post_id;
    private String title;   // 제목
    private String content; // 내용
    private String cost;    // 금액
    private String post_date;   // 게시글 날짜
    private String trip_date;   //  여행 날짜
    private String id_token;     // 작성자

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }

    public MainPost(String post_id, String title, String content, String cost, String post_date, String trip_date, String id_token) {
        this.post_id = post_id;
        this.title = title;
        this.content = content;
        this.cost = cost;
        this.post_date = post_date;
        this.trip_date = trip_date;
        this.id_token = id_token;
    }
    public MainPost(){}

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getTrip_date() {
        return trip_date;
    }

    public void setTrip_date(String trip_date) {
        this.trip_date = trip_date;
    }


}
