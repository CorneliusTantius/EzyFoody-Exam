package com.corneliustantius.ezyfoody.models;

public class CartSelectionModel {
    Integer id;
    Integer qty;
    String name;
    Integer price;

    public CartSelectionModel(){ }
    public CartSelectionModel(Integer id, Integer qty, String name, Integer price){
        this.id = id;
        this.qty = qty;
        this.name = name;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
