package com.example.solpl1.chat.Models;

import java.util.ArrayList;

public class MeetingModel {
    private String title;
    private String date;
    private String description;
    private ArrayList<String> users;

    public MeetingModel() {
    }

    public MeetingModel(String title, String date, String description, ArrayList<String> users) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.users = users;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
