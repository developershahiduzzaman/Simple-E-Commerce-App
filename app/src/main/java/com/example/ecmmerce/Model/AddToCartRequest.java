package com.example.ecmmerce.Model;

import com.google.gson.annotations.SerializedName;

public class AddToCartRequest {

    @SerializedName("product_id")
    private int productId;

    @SerializedName("quantity")
    private int quantity;

    public AddToCartRequest(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}