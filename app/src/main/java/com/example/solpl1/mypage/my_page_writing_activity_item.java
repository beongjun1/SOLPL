package com.example.solpl1.mypage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class my_page_writing_activity_item {
        private String cost;
        private String date;
        private String title;
        private String content;
        private String hashtag;
        private ArrayList<String> urlList;
    private String post_id;


    public my_page_writing_activity_item() {
    }

    public my_page_writing_activity_item(String cost, String date, String title, String content, String hashtag, ArrayList<String> urlList, String post_id) {
        this.cost = cost;
        this.date = date;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
        this.urlList = urlList;
        this.post_id = post_id;
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

    public ArrayList<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(ArrayList<String> urlList) {
        this.urlList = urlList;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }
}
