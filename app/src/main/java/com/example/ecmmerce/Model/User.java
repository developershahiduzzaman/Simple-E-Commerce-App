package com.example.ecmmerce.Model;

import com.google.gson.annotations.SerializedName;

public class User {
    private int id;
    private String name;
    private String email;
    @SerializedName("email_verified_at")
    private String emailVerifiedAt;

    // Getters
    public String getEmailVerifiedAt() { return emailVerifiedAt; }
    public String getName() { return name; }
}
