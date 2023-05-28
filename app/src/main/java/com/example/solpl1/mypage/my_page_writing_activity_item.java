package com.example.solpl1.mypage;

import java.util.List;

public class my_page_writing_activity_item {
        private String cost;
        private String date;
        private String title;
        private String content;
        private String hashtag;
         private List<String> urlList;

        private String post_id;

    public my_page_writing_activity_item(String title, String content, String cost, String date, String hashtag, String idToken, List<String> urlList) {
        this.title = title;
        this.content = content;
        this.cost = cost;
        this.date = date;
        this.hashtag = hashtag;
        this.idToken = idToken;
        this.urlList = urlList;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }


    private String idToken; // Firebase Uid (고유 토큰정보)
    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }



    public my_page_writing_activity_item() {
        // Default constructor required for calls to DataSnapshot.getValue(Item.class)
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }




}
