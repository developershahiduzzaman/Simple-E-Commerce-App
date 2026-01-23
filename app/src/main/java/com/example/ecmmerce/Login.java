package com.example.ecmmerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecmmerce.Model.LoginRequest;
import com.example.ecmmerce.Model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegisterLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterLink = findViewById(R.id.tvRegisterLink);

        tvRegisterLink.setOnClickListener(v -> startActivity(new Intent(Login.this, Register.class)));

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password);
            }
        });
    }

    private void loginUser(String email, String password){
        LoginRequest request = new LoginRequest(email, password);

        RetrofitClient.getApiService().loginUser(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    LoginResponse loginResponse = response.body();

                    // check verify
                    if (loginResponse.getUser() != null && loginResponse.getUser().getEmailVerifiedAt() == null) {
                        showVerificationDialog();
                        return;
                    }

                    // save token
                    String token = loginResponse.getToken();
                    SharedPreferences sp = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("token", token);
                    editor.apply();

                    Log.d("LOGIN_DEBUG", "Token Saved: " + token);
                    Toast.makeText(Login.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    if(loginResponse.isSuccess()){
                        startActivity(new Intent(Login.this, MainActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(Login.this, "Invalid credentials or Server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(Login.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    
    private void showVerificationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Email Verification Required")
                .setMessage("Your email is not verified yet. Please check your inbox and click the verification link.")
                .setPositiveButton("Open Email", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}