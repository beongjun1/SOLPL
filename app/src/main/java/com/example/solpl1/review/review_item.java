package com.example.solpl1.review;

public class review_item {
    String review_id,title,content,imgUrl,date,user_idToken;
    float ratingBar;

    public review_item(String review_id, String title, String content, String imgUrl, String date, String user_idToken, float ratingBar) {
        this.review_id = review_id;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
        this.date = date;
        this.user_idToken = user_idToken;
        this.ratingBar = ratingBar;
    }

    public String getUser_idToken() {
        return user_idToken;
    }

    public void setUser_idToken(String user_idToken) {
        this.user_idToken = user_idToken;
    }

    public String getReview_id() {
        return review_id;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRatingBar() {
        return ratingBar;
    }

    public void setRatingBar(float ratingBar) {
        this.ratingBar = ratingBar;
    }
}
