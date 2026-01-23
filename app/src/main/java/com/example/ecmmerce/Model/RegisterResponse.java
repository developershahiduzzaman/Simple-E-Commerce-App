package com.example.ecmmerce.Model;

import com.google.gson.annotations.SerializedName;


public class RegisterResponse {
    private boolean success;
    private String message;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}
