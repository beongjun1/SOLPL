package com.example.solpl1.PointShop.category.item;

public class category_item {
    private String name;
    private int cost;
    private String date;
    private String imgUrl;
    private String key;

    public category_item(String name, int cost, String date, String imgUrl, String key) {
        this.name = name;
        this.cost = cost;
        this.date = date;
        this.imgUrl = imgUrl;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
