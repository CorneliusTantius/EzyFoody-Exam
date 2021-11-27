package com.corneliustantius.ezyfoody.models;

public class CartItemModel {
    Integer id;
    Integer productId;
    Integer quantity;

    public CartItemModel(){ }
    public CartItemModel(Integer id, Integer productId, Integer quantity){
        this.id = id;
        this.productId = id;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
