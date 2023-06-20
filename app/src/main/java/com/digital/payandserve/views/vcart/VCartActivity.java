package com.digital.payandserve.views.vcart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.media.Image;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import com.digital.payandserve.R;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.SharedPrefs;

public class VCartActivity extends AppCompatActivity {
    WebView webview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);
        WebView.enableSlowWholeDocumentDraw();
        setContentView(R.layout.activity_vcart);

        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url");

         webview = findViewById(R.id.web_view);
        String userID = SharedPrefs.getValue(this, SharedPrefs.USER_ID);
        String userName = SharedPrefs.getValue(this, SharedPrefs.USER_NAME);
        webview.loadUrl(url);
        WebSettings webSettings = webview.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.getSaveFormData();
        webSettings.setDisplayZoomControls(false);
        webSettings.setUseWideViewPort(true);
       // webSettings.setAppCacheEnabled(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setGeolocationEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setLoadsImagesAutomatically(true);
        //webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.requestFocus();
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100) {
                    progressDialog.show();
                }
                if (progress == 100) {
                    progressDialog.dismiss();
                }
            }
        });

        ImageView share = findViewById(R.id.share);
        share.setOnClickListener(v -> {
            AppHandler.printInvoiceFromAdapter(webview, this);
        });
    }

    @Override
    public void onBackPressed() {
        if (webview != null && webview.canGoBack())
            webview.goBack();// if there is previous page open it
        else
            super.onBackPressed();//if there is no previous page, close app
    }
}