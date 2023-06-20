package com.digital.payandserve.views.otpview;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

public class OTPValidateForgetPassword extends AppCompatActivity implements RequestResponseLis {
    private Context context;
    private EditText otpTextView;
    private TextView title;
    private Button btnProceed, btnResend;
    private String URL, USER_ID, PASSWORD, MSG;
    private int REQUEST_TYPE = 0;
    private EditText etPassword;

    private void init() {
        context = this;
        USER_ID = getIntent().getStringExtra("USER_ID");
        otpTextView = findViewById(R.id.otp_view);
        btnProceed = findViewById(R.id.btnProceed);
        btnResend = findViewById(R.id.btnResend);
        title = findViewById(R.id.title);
        etPassword = findViewById(R.id.etPassword);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.whiteStatusBar(this);
        setContentView(R.layout.otp_forget_pass_view);

        init();

        btnResend.setOnClickListener(v -> {
            REQUEST_TYPE = 0;
            networkCallUsingVolleyApi(Constants.URL.PASSWORD_RESET_REQ);
        });

        btnProceed.setOnClickListener(v -> {
            REQUEST_TYPE = 1;
            if (isValid())
                networkCallUsingVolleyApi(Constants.URL.PASSWORD_RESET);
        });
    }

    private boolean isValid() {
        if (!MyUtil.isNN(otpTextView.getText().toString())) {
            Toast.makeText(context, "Please enter the otp", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!MyUtil.isNN(etPassword.getText().toString())) {
            Toast.makeText(context, "Please enter new password", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void networkCallUsingVolleyApi(String url) {
        if (AppManager.isOnline(this)) {
            new VolleyNetworkCall(this, this, url, 1, param(), true).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSuccessRequest(String result) {
        if (REQUEST_TYPE == 1) {
            finish();
        } else {
            Toast.makeText(context, "Otp has been send to your contact number", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", USER_ID);
        if (REQUEST_TYPE == 1) {
            map.put("token", otpTextView.getText().toString());
            map.put("password", etPassword.getText().toString());
        }
        return map;
    }
}
