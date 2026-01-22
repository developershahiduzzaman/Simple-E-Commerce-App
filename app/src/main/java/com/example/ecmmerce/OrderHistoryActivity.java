package com.example.ecmmerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecmmerce.Model.Order;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private ApiService apiService;
    private LinearLayout emptyView;
    ImageView homeIcon, accIcon, cartIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        recyclerView = findViewById(R.id.recyclerViewOrders);
        emptyView = findViewById(R.id.emptyView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Login Required!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        apiService = RetrofitClient.getApiService();
        loadOrderHistory(token);

        homeIcon = findViewById(R.id.btnHomeIcon);
        accIcon = findViewById(R.id.btnAccIcon);
        cartIcon = findViewById(R.id.btnCartIcon);

        homeIcon.setOnClickListener(v -> startActivity(new Intent(OrderHistoryActivity.this, MainActivity.class)));
        cartIcon.setOnClickListener(v -> startActivity(new Intent(OrderHistoryActivity.this, CartActivity.class)));
        accIcon.setOnClickListener(v -> {

            loadOrderHistory(token);
        });
    }

    private void loadOrderHistory(String token) {
        apiService.getOrderHistory("Bearer " + token).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Order> orderList = response.body();

                    if (orderList.isEmpty()) {

                        recyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    } else {

                        recyclerView.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);

                        adapter = new OrderAdapter(orderList);
                        recyclerView.setAdapter(adapter);
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(OrderHistoryActivity.this, "Session Expired!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                Toast.makeText(OrderHistoryActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}