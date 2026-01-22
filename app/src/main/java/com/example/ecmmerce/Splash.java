package com.example.ecmmerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLoginSession();
            }
        }, 2000);
    }

    private void checkLoginSession() {
        SharedPreferences sp = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);


        String token = sp.getString("token", "");

        if (!token.isEmpty()) {
            startActivity(new Intent(Splash.this, MainActivity.class));
        } else {

            startActivity(new Intent(Splash.this, Login.class));
        }
        finish();
    }
}