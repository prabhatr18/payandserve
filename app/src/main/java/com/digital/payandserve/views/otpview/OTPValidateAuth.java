package com.digital.payandserve.views.otpview;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.MyUtil;

import java.util.HashMap;
import java.util.Map;

import in.aabhasjindal.otptextview.OtpTextView;

public class OTPValidateAuth extends AppCompatActivity implements RequestResponseLis {
    private String url;
    private Context context;
    private OtpTextView otpTextView;
    private TextView title;
    private Button btnProceed, btnResend;
    private String URL, USER_ID, PASSWORD, MSG;
    private int REQUEST_TYPE = 0;

    private void init() {
        context = this;
        USER_ID = getIntent().getStringExtra("USER_ID");
        PASSWORD = getIntent().getStringExtra("PASSWORD");
        MSG = getIntent().getStringExtra("MSG");
        otpTextView = findViewById(R.id.otp_view);
        btnProceed = findViewById(R.id.btnProceed);
        btnResend = findViewById(R.id.btnResend);
        title = findViewById(R.id.title);
        title.setText(MSG);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.whiteStatusBar(this);
        setContentView(R.layout.otp_validate_view);

        init();

        URL = Constants.URL.LOGIN;
        btnResend.setOnClickListener(v -> {
            REQUEST_TYPE = 0;
            networkCallUsingVolleyApi(URL);
        });

        btnProceed.setOnClickListener(v -> {
            REQUEST_TYPE = 1;
            networkCallUsingVolleyApi(URL);
        });
    }

    private void networkCallUsingVolleyApi(String url) {
        if (AppManager.isOnline(this)) {
            new VolleyNetworkCall(this, this, url, 1, param()).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSuccessRequest(String result) {
        String status = AppHandler.getStatus(result);
        if (status.equalsIgnoreCase("otp")) {
            otpTextView.setOTP("");
        } else {
            AppHandler.parseAuthRes(USER_ID ,result, this);
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", USER_ID);
        map.put("password", PASSWORD);
        if (REQUEST_TYPE == 1) {
            map.put("otp", otpTextView.getOTP());
        }
        return map;
    }
}
