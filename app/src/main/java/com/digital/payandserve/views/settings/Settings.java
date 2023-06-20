package com.digital.payandserve.views.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.digital.payandserve.BuildConfig;
import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.utill.SharedPrefs;
import com.digital.payandserve.utill.ThemeHelper;
import com.digital.payandserve.views.otpview.OTPPinReset;
import com.digital.payandserve.views.otpview.PasswordReset;

public class Settings extends AppCompatActivity {
    private TextView tvManageAppLock, tvContactUS, tvAboutUs;
    private TextView tvLogoutTime, tvAppDetails, tvPasswordChange, tvPinChange;
    private ImageView imgBack;

    @SuppressLint("SetTextI18n")
    private void init() {
        tvManageAppLock = findViewById(R.id.tvManageAppLock);
        imgBack = findViewById(R.id.imgBack);
        tvPasswordChange = findViewById(R.id.tvPasswordChange);
        tvPinChange = findViewById(R.id.tvPinChange);
        imgBack.setOnClickListener(v -> finish());
        tvLogoutTime = findViewById(R.id.tvLogoutTime);
        tvLogoutTime.setText("App auto logout time is " + Constants.AUTO_LOGOUT_TIME + " minutes");

        tvContactUS = findViewById(R.id.tvContactUS);
        tvAppDetails = findViewById(R.id.tvAppDetails);
        String config = "Version Code : " + BuildConfig.VERSION_CODE;
        tvAppDetails.setText(config);
        tvAboutUs = findViewById(R.id.tvAboutUS);

        tvContactUS.setOnClickListener(v -> {
            Intent i = new Intent(Settings.this, WebViewPAge.class);
            i.putExtra("type", "Contact US");
            startActivity(i);
        });

        tvContactUS.setOnClickListener(v -> {
            Intent i = new Intent(Settings.this, WebViewPAge.class);
            i.putExtra("type", "About US");
            startActivity(i);
        });

        tvPasswordChange.setOnClickListener(v -> {
            Intent i = new Intent(Settings.this, PasswordReset.class);
            startActivity(i);
        });

        tvPinChange.setOnClickListener(v -> {
            Intent i = new Intent(Settings.this, OTPPinReset.class);
            startActivity(i);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();

        TextView tvTitle = findViewById(R.id.toolbartext);
        tvTitle.setText("Settings");

        tvManageAppLock.setOnClickListener(v -> {
            startActivity(new Intent(this, LockSettings.class));
        });

        View tv = findViewById(R.id.secTheam);
        tv.setOnClickListener(v -> themOptionPopup());
        TextView btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> AppManager.getInstance().logoutFromServer(this, false));
    }

    private void themOptionPopup() {
        String[] array = {"Normal", "Dark Mode"};
        String title = "Please select any one ui mode";
        AlertDialog.Builder alert = new MaterialAlertDialogBuilder(
                this, R.style.ThemeOverlay_App_MaterialAlertDialog);
        alert.setTitle(title);
        alert.setSingleChoiceItems(array, -1, (dialog, which) -> {
            if (which == 0) {
                SharedPrefs.setValue(this, SharedPrefs.APP_THEAM, "light");
                ThemeHelper.applyTheme("light");
            } else {
                ThemeHelper.applyTheme("dark");
                SharedPrefs.setValue(this, SharedPrefs.APP_THEAM, "dark");
            }
        });
        alert.setNegativeButton("OK", (dialog, which) -> dialog.dismiss());
        alert.setCancelable(false);
        alert.show();
    }
}
