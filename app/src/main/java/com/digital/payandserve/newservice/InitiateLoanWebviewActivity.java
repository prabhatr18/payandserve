package com.digital.payandserve.newservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.digital.payandserve.R;
import com.digital.payandserve.views.DashBoardActivity;

public class InitiateLoanWebviewActivity extends AppCompatActivity {

    private WebView loanWebView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiate_loan_webview);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        progressBar = findViewById(R.id.progressBar);
        loanWebView = (WebView) findViewById(R.id.loanWebView);

        loanWebView.setWebViewClient(new WebViewClient());
        loanWebView.getSettings().setLoadsImagesAutomatically(true);
        loanWebView.getSettings().setJavaScriptEnabled(true);
        loanWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        loanWebView.loadUrl(url);
    }

    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent((Context) this, DashBoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}