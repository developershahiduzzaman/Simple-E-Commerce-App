package com.example.ecmmerce.Model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("success")
    private boolean success;

    // --- এই লাইনটিই আসল সমাধান ---
    @SerializedName("access_token") // সার্ভারের নামের সাথে মিলতে হবে
    private String token;

    @SerializedName("message")
    private String message;

    @SerializedName("user_id")
    private int userId;

    // Default constructor
    public LoginResponse() {}

    // Full constructor
    public LoginResponse(boolean success, String token, String message, int userId) {
        this.success = success;
        this.token = token;
        this.message = message;
        this.userId = userId;
    }

    // Getters
    public boolean isSuccess() { return success; }
    public String getToken() { return token; }
    public String getMessage() { return message; }
    public int getUserId() { return userId; }

    // Setters
    public void setSuccess(boolean success) { this.success = success; }
    public void setToken(String token) { this.token = token; }
    public void setMessage(String message) { this.message = message; }
    public void setUserId(int userId) { this.userId = userId; }
}