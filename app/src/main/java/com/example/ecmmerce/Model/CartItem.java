package com.example.ecmmerce.Model;

import com.google.gson.annotations.SerializedName;

public class CartItem {
    @SerializedName("id")
    private int id;

    @SerializedName("product")
    private Product product;

    @SerializedName("quantity")
    private int quantity;

    // Constructor
    public CartItem(int id, Product product, int quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    // Getters
    public int getId() { return id; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }


    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}