package com.example.ecmmerce;

import com.example.ecmmerce.Model.LoginRequest;
import com.example.ecmmerce.Model.LoginResponse;
import com.example.ecmmerce.Model.Product;
import com.example.ecmmerce.Model.RegisterRequest;
import com.example.ecmmerce.Model.RegisterResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST("login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);

    @POST("register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest request);


    @GET("products")
    Call<List<Product>> getProducts();
}
