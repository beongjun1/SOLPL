package com.example.solpl1;

public class my_page_item {
    String date;
    String loc;
    int resourceId;

    public my_page_item(int resourceId, String date, String loc) {
        this.date = date;
        this.loc= loc;
        this.resourceId = resourceId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}