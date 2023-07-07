package com.example.solpl1.mainPost;

public class NowPostModel {
    private int profile, postImage, save;
    private String name, content, comment,like;

    public NowPostModel(){}
    public NowPostModel(int profile, int postImage, int save, String name, String content, String comment, String like) {
        this.profile = profile;
        this.postImage = postImage;
        this.save = save;
        this.name = name;
        this.content = content;
        this.comment = comment;
        this.like = like;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public int getPostImage() {
        return postImage;
    }

    public void setPostImage(int postImage) {
        this.postImage = postImage;
    }

    public int getSave() {
        return save;
    }

    public void setSave(int save) {
        this.save = save;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }
}
