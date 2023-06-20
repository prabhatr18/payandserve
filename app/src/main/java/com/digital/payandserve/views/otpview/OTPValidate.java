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
import com.digital.payandserve.utill.MyUtil;

import java.util.HashMap;
import java.util.Map;

import in.aabhasjindal.otptextview.OtpTextView;

public class OTPValidate extends AppCompatActivity implements RequestResponseLis {
    private String url;
    private Context context;
    private OtpTextView otpTextView;
    private TextView title;
    private Button btnProceed, btnResend;
    private String TYPE, URL, BEN_MOBILE, SEDER_MOBILE, BEN_ACCOUNT;
    private int REQUEST_TYPE = 0;

    private void init() {
        context = this;
        TYPE = getIntent().getStringExtra("type");
        URL = getIntent().getStringExtra("url");
        BEN_MOBILE = getIntent().getStringExtra("ben_mobile");
        SEDER_MOBILE = getIntent().getStringExtra("sender_mobile");
        BEN_ACCOUNT = getIntent().getStringExtra("ben_account");
        otpTextView = findViewById(R.id.otp_view);
        btnProceed = findViewById(R.id.btnProceed);
        btnResend = findViewById(R.id.btnResend);
        title = findViewById(R.id.title);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.whiteStatusBar(this);
        setContentView(R.layout.otp_validate_view);

        init();

        btnResend.setOnClickListener(v -> {
            TYPE = "otp";
            networkCallUsingVolleyApi(URL);
        });

        btnProceed.setOnClickListener(v -> {
            TYPE = "beneverify";
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
        if (TYPE.equalsIgnoreCase("beneverify")) {
            Constants.IS_RELOAD_REQUEST = true;
            finish();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("type", TYPE);
        map.put("mobile", SEDER_MOBILE);
        if (TYPE.equalsIgnoreCase("beneverify")) {
            map.put("benemobile", BEN_MOBILE);
            map.put("beneaccount", BEN_ACCOUNT);
            map.put("otp", otpTextView.getOTP());
            if (MyUtil.isNN(Constants.DMT_RID))
                map.put("rid", Constants.DMT_RID);
        }
        return map;
    }
}
