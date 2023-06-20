package com.digital.payandserve.network;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.airbnb.lottie.LottieAnimationView;
import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.utill.MyUtil;

public class SessionExpired extends AppCompatActivity {
    private LottieAnimationView lottie;
    private TextView tvStatus;
    private ImageView imgClose, imgShare, imgMinimise;
    private LinearLayout loaderCon;
    private String error = "";
    private AppCompatButton loginButton;

    private void init() {
        loaderCon = findViewById(R.id.loaderCon);
        imgShare = findViewById(R.id.imgShare);
        imgClose = findViewById(R.id.imgClose);
        tvStatus = findViewById(R.id.bottomTv);
        lottie = findViewById(R.id.LottieAnim);
        loginButton = findViewById(R.id.loginButton);
        imgMinimise = findViewById(R.id.imgMinimise);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.whiteStatusBar(this);
        setContentView(R.layout.loader_layout);
        init();
        showNetworkError("Your session has been expired please login again to use our services");
        showLogOutButton();
    }

    private void showNetworkError(String string) {
        ViewGroup.LayoutParams params = loaderCon.getLayoutParams();
        params.height = 400;
        params.width = 400;
        loaderCon.setLayoutParams(params);
        lottie.setAnimation(R.raw.network_error);
        imgClose.setVisibility(View.VISIBLE);
        imgShare.setVisibility(View.VISIBLE);
        imgMinimise.setVisibility(View.VISIBLE);
        tvStatus.setText(string);
    }

    private void showLogOutButton() {
        imgClose.setVisibility(View.GONE);
        imgShare.setVisibility(View.GONE);
        imgMinimise.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);
        loginButton.setOnClickListener(v -> {
            AppManager.getInstance().logoutFromServer(this, false);
        });
    }
}
