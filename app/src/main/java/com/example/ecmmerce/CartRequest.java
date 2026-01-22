package com.example.ecmmerce;

public class CartRequest {
    private int userId;
    private int productId;
    private int quantity;

    public CartRequest(int userId, int productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    // getters
    public int getUserId() { return userId; }
    public int getProductId() { return productId; }
    public int getQuantity() { return quantity; }
}
