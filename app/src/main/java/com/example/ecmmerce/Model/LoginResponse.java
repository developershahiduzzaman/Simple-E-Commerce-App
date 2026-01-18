package com.example.ecmmerce.Model;

public class LoginResponse {
    private boolean success;
    private String token;
    private String message;

    // Default constructor
    public LoginResponse() {}

    // Full constructor
    public LoginResponse(boolean success, String token, String message) {
        this.success = success;
        this.token = token;
        this.message = message;
    }

    // Getters
    public boolean isSuccess() { return success; }
    public String getToken() { return token; }
    public String getMessage() { return message; }

    // Setters
    public void setSuccess(boolean success) { this.success = success; }
    public void setToken(String token) { this.token = token; }
    public void setMessage(String message) { this.message = message; }
}
