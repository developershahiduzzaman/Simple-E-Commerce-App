package com.example.ecmmerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private static final String TAG = "PAYMENT_DEBUG";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        webView = findViewById(R.id.paymentWebView);
        progressBar = findViewById(R.id.progressBar);

        String paymentUrl = getIntent().getStringExtra("payment_url");

        if (paymentUrl == null || paymentUrl.isEmpty()) {
            Toast.makeText(this, "Invalid payment URL", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // কুকি কনফিগারেশন
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(webView, true);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setSupportMultipleWindows(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        // Mixed Content Allow করা (HTTP ও HTTPS এর সমস্যা সমাধানে)
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // ইউজার এজেন্ট সেট করা (যাতে সার্ভার মনে করে এটি একটি ডেস্কটপ ব্রাউজার)
        settings.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (progressBar != null) {
                    progressBar.setProgress(newProgress);
                    progressBar.setVisibility(newProgress == 100 ? View.GONE : View.VISIBLE);
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // স্যান্ডবক্স মোডে SSL সমস্যা এড়াতে
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d(TAG, "Page Started: " + url);
                if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                return handleUrl(url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "Page Finished: " + url);
                if (progressBar != null) progressBar.setVisibility(View.GONE);

                // যদি ব্যাকেন্ডের সাকসেস পেজে সরাসরি পৌঁছে যায়
                handleUrl(url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.e(TAG, "WebView Error: " + error.getDescription());
            }
        });

        // ব্যাক প্রেস হ্যান্ডেলিং
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

        webView.loadUrl(paymentUrl);
    }

    private boolean handleUrl(String url) {
        Log.d(TAG, "Checking URL Logic: " + url);

        String lowercaseUrl = url.toLowerCase();

        if (lowercaseUrl.contains("payment/success")) {
            Log.d(TAG, "Success Detected! Redirecting...");

            Toast.makeText(this, "Order Placed Successfully!", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(PaymentActivity.this, OrderHistoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            finish();
            return true;
        }
        else if (lowercaseUrl.contains("payment/fail") || lowercaseUrl.contains("payment/cancel")) {
            Log.d(TAG, "Failure Detected!");
            Toast.makeText(this, "Payment Failed or Cancelled", Toast.LENGTH_LONG).show();
            finish();
            return true;
        }

        return false;
    }
}