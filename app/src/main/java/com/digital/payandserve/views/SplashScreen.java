package com.digital.payandserve.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.digital.payandserve.R;
import com.digital.payandserve.intro_slider.IntroSliderActivity;
import com.digital.payandserve.permission.AppPermissions;
import com.digital.payandserve.permission.PermissionHandler;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.PrefLoginManager;
import com.digital.payandserve.utill.SharedPrefs;
import com.digital.payandserve.utill.ThemeHelper;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        String ui = SharedPrefs.getValue(this, SharedPrefs.APP_THEAM);
        if (MyUtil.isNN(ui))
            ThemeHelper.applyTheme(ui);
        startLoader(400);

    }

    Thread splashTimer;

    private void startLoader(final int maxTime) {
        splashTimer = new Thread() {
            public void run() {
                try {
                    int splashTime = 0;
                    int selector = 1;
                    while (splashTime < maxTime) {
                        Thread.sleep(700);
                        splashTime = splashTime + 100;
                    }

                    openActivity();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        splashTimer.start();
    }

    private void openActivity() {
        PrefLoginManager pref = new PrefLoginManager(this);
        String status = pref.getFarmerLoginRes();
        if (status != null && status.length() > 0) {
            startActivity(new Intent((Context) this, DashBoardActivity.class));
        } else {
            startLoginWithPerm();
        }
        finish();
    }

    private void startLoginWithPerm() {
        String[] permissions = {Manifest.permission.READ_PHONE_STATE};
        AppPermissions.check(this, permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                startActivity(new Intent((Context) SplashScreen.this, IntroSliderActivity.class));
            }
        });
    }
}
