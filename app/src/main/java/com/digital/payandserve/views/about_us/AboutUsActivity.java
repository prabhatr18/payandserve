package com.digital.payandserve.views.about_us;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.webkit.WebView;


import com.digital.payandserve.R;

public class AboutUsActivity extends AppCompatActivity {

    WebView aboutUsWebView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        aboutUsWebView = findViewById(R.id.aboutUsWebView);
        toolbar = findViewById(R.id.toolbar);

        Bundle bundle = getIntent().getExtras();

        if (bundle.getString("title")!= null){
            String title = bundle.getString("title");

            String url = bundle.getString("url");
            toolbar.setTitle(title);
            aboutUsWebView.loadUrl(url);
        }


    }
}