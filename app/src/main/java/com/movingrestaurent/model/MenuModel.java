package com.movingrestaurent.model;

/**
 * Created by Android on 5/17/2016.
 */
public class MenuModel {
    String name;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    String category;
    String price;
}
