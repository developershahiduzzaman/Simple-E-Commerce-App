package com.example.ecmmerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecmmerce.Model.CartItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CartAdapter adapter;
    TextView tvTotalPrice;
    Button btnCheckout;
    List<CartItem> cartList = new ArrayList<>();
    ApiService apiService;
    ImageView homeIcon, accIcon, cartIcon;

    private LinearLayout emptyView;
    private View stickyBottomContainer;

    private double currentTotalAmount = 0.0;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        recyclerView = findViewById(R.id.recyclerViewCart);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);
        emptyView = findViewById(R.id.emptyView);
        stickyBottomContainer = findViewById(R.id.stickyBottomContainer);

        homeIcon = findViewById(R.id.btnHomeIcon);
        accIcon = findViewById(R.id.btnAccIcon);
        cartIcon = findViewById(R.id.btnCartIcon);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiService = RetrofitClient.getApiService();

        SharedPreferences sp = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        token = sp.getString("token", "");

        btnCheckout.setOnClickListener(v -> {
            if (token.isEmpty()) {
                Toast.makeText(this, "Login Required", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> orderData = new HashMap<>();
            orderData.put("total_amount", currentTotalAmount);

            apiService.placeOrder("Bearer " + token, orderData).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CartActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CartActivity.this, OrderHistoryActivity.class));
                        finish();
                    } else {
                        Toast.makeText(CartActivity.this, "Failed: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("OrderError", t.getMessage());
                }
            });
        });

        loadCart();

        homeIcon.setOnClickListener(v -> startActivity(new Intent(CartActivity.this, MainActivity.class)));
        cartIcon.setOnClickListener(v -> loadCart());
        accIcon.setOnClickListener(v -> startActivity(new Intent(CartActivity.this, OrderHistoryActivity.class)));
    }

    private void loadCart() {
        apiService.getCart("Bearer " + token).enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartList = response.body();

                    if (cartList.isEmpty()) {
                        updateUI(true);
                    } else {
                        updateUI(false);
                        adapter = new CartAdapter(CartActivity.this, cartList);
                        recyclerView.setAdapter(adapter);
                        calculateTotalPrice();
                    }
                } else {
                    updateUI(true);
                }
            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {
                updateUI(true);
                Toast.makeText(CartActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateUI(boolean isEmpty) {
        if (isEmpty) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);


            findViewById(R.id.layoutBottom).setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            findViewById(R.id.layoutBottom).setVisibility(View.VISIBLE);
        }
    }

    public void calculateTotalPrice() {
        double total = 0;
        for (CartItem item : cartList) {
            try {
                double price = Double.parseDouble(item.getProduct().getPrice());
                total += (price * item.getQuantity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        currentTotalAmount = total;
        tvTotalPrice.setText("Total: ৳ " + total);
    }
}