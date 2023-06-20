package com.digital.payandserve.views.otpview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import com.digital.payandserve.views.auth.LoginActivity;

import java.util.HashMap;
import java.util.Map;

public class PasswordReset extends AppCompatActivity implements RequestResponseLis {
    private Context context;
    private EditText etPasswordOld, etConfirmPin, etPin;
    private TextView title;
    private Button btnProceed;
    private int REQUEST_TYPE = 0;

    private void init() {
        context = this;
        etPasswordOld = findViewById(R.id.etPasswordOld);
        btnProceed = findViewById(R.id.btnProceed);
        title = findViewById(R.id.title);
        etConfirmPin = findViewById(R.id.etConfirmPin);
        etPin = findViewById(R.id.etPin);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.whiteStatusBar(this);
        setContentView(R.layout.otp_password_reset_view);
        init();
        btnProceed.setOnClickListener(v -> {
            REQUEST_TYPE = 1;
            if (isValid())
                networkCallUsingVolleyApi(Constants.URL.PASSWORD_CHANGE);
        });
    }

    private boolean isValid() {
        if (etPin.getText().toString().length() == 0) {
            Toast.makeText(this, "Please enter new password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etConfirmPin.getText().toString().length() == 0) {
            Toast.makeText(this, "Confirm password is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!etPin.getText().toString().equals(etConfirmPin.getText().toString())) {
            Toast.makeText(this, "Both password should be same", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etPasswordOld.getText().toString().length() == 0) {
            Toast.makeText(this, "old password  is required", Toast.LENGTH_SHORT).show();
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
            if (AppHandler.checkStatus(result, this)){
                mShowDialog(msg);
            }else{
                Toast.makeText(this, ""+msg, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void mShowDialog(final String message) {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(message);
        builder1.setPositiveButton("OK", (dialog, j) -> {
            dialog.dismiss();
            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
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
            map.put("oldpassword", etPasswordOld.getText().toString());
            map.put("password", etPin.getText().toString());
        }
        return map;
    }
}
