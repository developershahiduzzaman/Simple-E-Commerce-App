package com.example.ecmmerce;

import com.example.ecmmerce.Model.AddToCartRequest;
import com.example.ecmmerce.Model.CartItem;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import com.example.ecmmerce.Model.LoginRequest;
import com.example.ecmmerce.Model.LoginResponse;
import com.example.ecmmerce.Model.Order;
import com.example.ecmmerce.Model.Product;
import com.example.ecmmerce.Model.RegisterRequest;
import com.example.ecmmerce.Model.RegisterResponse;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @Headers("Accept: application/json")
    @POST("login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);
    @POST("register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest request);


    @GET("products")
    Call<List<Product>> getProducts();


    @GET("orders")
    Call<List<Order>> getOrders();

    @POST("cart/add")
    Call<ResponseBody> addToCart(
            @Header("Authorization") String token,
            @Body AddToCartRequest request
    );

    // --- Get Cart List ---
    @GET("cart")
    Call<List<CartItem>> getCart(
            @Header("Authorization") String token
    );


    @DELETE("cart/remove/{cartItemId}")
    Call<Void> removeFromCart(@Path("cartItemId") int cartItemId);

    @POST("order/place")
    Call<ResponseBody> placeOrder(
            @Header("Authorization") String token,
            @Body Map<String, Object> orderData
    );


    @FormUrlEncoded
    @PUT("cart/{id}")
    Call<ResponseBody> updateQuantity(
            @Header("Authorization") String token,
            @Path("id") int cartId,
            @Field("quantity") int quantity
    );

    @DELETE("cart/{id}")
    Call<ResponseBody> deleteCartItem(
            @Header("Authorization") String token,
            @Path("id") int cartId
    );

    @GET("orders/history")
    Call<List<Order>> getOrderHistory(@Header("Authorization") String token);
}


