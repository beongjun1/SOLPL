package com.example.solpl1.PointShop.category.item;

public class category_item {
    private String name;
    private String category;
    private int cost;
    private String store;
    private String date;
    private String imgUrl;
    private String key;

    public category_item(String name, String category, int cost, String store, String date, String imgUrl, String key) {
        this.name = name;
        this.category = category;
        this.cost = cost;
        this.store = store;
        this.date = date;
        this.imgUrl = imgUrl;
        this.key = key;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
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
