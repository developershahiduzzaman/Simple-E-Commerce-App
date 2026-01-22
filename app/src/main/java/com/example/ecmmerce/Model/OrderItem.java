package com.example.ecmmerce.Model;

import com.google.gson.annotations.SerializedName;

public class OrderItem {
    @SerializedName("product")
    private Product product;

    @SerializedName("quantity")
    private int quantity;

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
