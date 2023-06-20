package com.digital.payandserve.views.otpview;

import android.app.AlertDialog;
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
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.utill.SharedPrefs;

import java.util.HashMap;
import java.util.Map;

public class OTPPinReset extends AppCompatActivity implements RequestResponseLis {
    private Context context;
    private EditText otpTextView, etConfirmPin, etPin;
    private TextView title;
    private Button btnProceed, btnResend;
    private int REQUEST_TYPE = 0;

    private void init() {
        context = this;
        otpTextView = findViewById(R.id.etOTP);
        btnProceed = findViewById(R.id.btnProceed);
        btnResend = findViewById(R.id.btnResend);
        title = findViewById(R.id.title);
        etConfirmPin = findViewById(R.id.etConfirmPin);
        etPin = findViewById(R.id.etPin);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.whiteStatusBar(this);
        setContentView(R.layout.otp_reset_pin_view);

        init();

        btnResend.setOnClickListener(v -> {
            REQUEST_TYPE = 0;
            networkCallUsingVolleyApi(Constants.URL.PIN_GET_OTP);
        });

        btnProceed.setOnClickListener(v -> {
            REQUEST_TYPE = 1;
            if (isValid())
                networkCallUsingVolleyApi(Constants.URL.GENERATE_PIN);
        });
    }

    private boolean isValid() {
        if (etPin.getText().toString().length() < 4) {
            Toast.makeText(this, "Pin length should be in between 4 and 8", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etConfirmPin.getText().toString().length() < 4) {
            Toast.makeText(this, "Pin length should be in between 4 and 8", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!etPin.getText().toString().equals(etConfirmPin.getText().toString())) {
            Toast.makeText(this, "Both pin should be same", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (otpTextView.getText().toString().length() == 0) {
            Toast.makeText(this, "otp  is required", Toast.LENGTH_SHORT).show();
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
        try {
            String msg = AppHandler.getMessage(result);
            String status = AppHandler.getStatus(result);
            if (REQUEST_TYPE == 1 && status.equalsIgnoreCase("TXN")) {
                mShowDialog(msg);
            } else {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void mShowDialog(final String message) {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(message);
        builder1.setPositiveButton("OK", (dialog, i) -> {
            finish();
        });
        AlertDialog alert = builder1.create();
        alert.setCancelable(false);
        alert.show();
    }

    @Override
    public void onFailRequest(String msg) {
        Print.P(msg);
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", SharedPrefs.getValue(this, SharedPrefs.LOGIN_ID));
        if (REQUEST_TYPE == 1) {
            map.put("tpin", etPin.getText().toString());
            map.put("tpin_confirmation", etPin.getText().toString());
            map.put("otp", otpTextView.getText().toString());
        }
        return map;
    }
}
