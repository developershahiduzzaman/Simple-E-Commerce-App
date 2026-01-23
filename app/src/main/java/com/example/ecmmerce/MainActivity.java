package com.example.ecmmerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecmmerce.Model.Product;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductAdapter adapter;

    ImageView homeIcon;
    ImageView accIcon;
    ImageView cartIcon;
    ImageView btnLogoutIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrofit API call
        ApiService apiService = RetrofitClient.getApiService();

        apiService.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new ProductAdapter(response.body());
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(MainActivity.this, "No products found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        });

        homeIcon = findViewById(R.id.btnHomeIcon);
        accIcon = findViewById(R.id.btnAccIcon);
        cartIcon = findViewById(R.id.btnCartIcon);

        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));

            }
        });

        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));

            }
        });

        accIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OrderHistoryActivity.class));

            }
        });


        btnLogoutIcon = findViewById(R.id.btnLogoutIcon);

        btnLogoutIcon.setOnClickListener(v -> {
            showLogoutConfirmDialog();
        });


    }




    private void showLogoutConfirmDialog() {

        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    performLogout();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void performLogout() {
        SharedPreferences sp = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = "Bearer " + sp.getString("token", "");


        RetrofitClient.getApiService().logoutUser(token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                clearLocalDataAndRedirect();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                clearLocalDataAndRedirect();
            }
        });
    }

    private void clearLocalDataAndRedirect() {
        SharedPreferences sp = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        sp.edit().clear().apply();

        Intent intent = new Intent(MainActivity.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
