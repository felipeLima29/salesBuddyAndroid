package com.example.salesbuddy.model;

import java.io.Serializable;

public class ItemsSale implements Serializable {

    private String price;
    private String description;

    public ItemsSale(String price, String description) {
        this.price = price;
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
