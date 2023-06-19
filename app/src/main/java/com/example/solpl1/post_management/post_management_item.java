package com.example.solpl1.post_management;

import java.util.ArrayList;

public class post_management_item {
    private String postId;
    private String name;
    private String time;
    private String title;
    private String content;
    private ArrayList<String> imageUrl;
    private String profile_img;


    public post_management_item() {
    }

    public post_management_item(String postId, String name, String time, String title, String content, ArrayList<String> imageUrl, String profile_img) {
        this.postId = postId;
        this.name = name;
        this.time = time;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.profile_img = profile_img;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public ArrayList<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(ArrayList<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }
}
