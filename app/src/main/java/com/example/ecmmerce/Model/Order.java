package com.example.ecmmerce.Model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Order {
    private int id;
    private double total_amount;
    private String status;
    private String created_at;

    // Getters
    public int getId() { return id; }
    public double getTotalAmount() { return total_amount; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return created_at; }
}