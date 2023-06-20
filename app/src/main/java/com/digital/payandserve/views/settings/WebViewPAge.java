package com.digital.payandserve.views.settings;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.digital.payandserve.R;

public class WebViewPAge extends AppCompatActivity {
    private ProgressBar spinner;
    private WebView webview;
    private ImageView imgBack;
    private TextView toolBarText;

    private void init() {
        webview = findViewById(R.id.webView);
        spinner = findViewById(R.id.progressBar1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.web_page_activity);
        webview = (WebView) findViewById(R.id.webView);
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        webview.setWebViewClient(new CustomWebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webview.loadUrl("https://colorlib.com/etc/cf/ContactFrom_v10/index.html");
    }


    class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon) {
            webview.setVisibility(webview.INVISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            spinner.setVisibility(View.GONE);
            view.setVisibility(webview.VISIBLE);
            super.onPageFinished(view, url);
        }
    }

}

