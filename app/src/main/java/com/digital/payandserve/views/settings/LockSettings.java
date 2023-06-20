package com.digital.payandserve.views.settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.amirarcane.lockscreen.activity.EnterPinActivity;
import com.digital.payandserve.R;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.SharedPrefs;

public class LockSettings extends AppCompatActivity {
    private SwitchCompat btnSwitch;
    private RelativeLayout secChangePin;
    private TextView tvTitle;
    private ImageView imgBack;

    private void init() {
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> finish());
        btnSwitch = findViewById(R.id.switchBtn);
        tvTitle = findViewById(R.id.toolbartext);
        tvTitle.setText("Manage App Lock");
        secChangePin = findViewById(R.id.secChangePin);
        btnSwitch.setChecked(MyUtil.isNN(SharedPrefs.getValue(this, SharedPrefs.IS_ALLOWED_APP_LOCK)));
        btnSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                SharedPrefs.setValue(this, SharedPrefs.IS_ALLOWED_APP_LOCK, "1");
            else
                SharedPrefs.setValue(this, SharedPrefs.IS_ALLOWED_APP_LOCK, "");
        });

        secChangePin.setOnClickListener(v -> {
            Intent intent = EnterPinActivity.getIntent(this, true);
            startActivity(intent);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_lock_options);
        init();
    }
}
