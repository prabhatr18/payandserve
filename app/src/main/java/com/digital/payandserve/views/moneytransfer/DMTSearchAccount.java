package com.digital.payandserve.views.moneytransfer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.Print;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DMTSearchAccount extends AppCompatActivity implements RequestResponseLis {
    private int REQUEST_TYPE = 0;
    private EditText etMobile, etName, etOTP;
    private Context context;
    private Button btnProceed;

    private void init() {
        context = this;
        etMobile = findViewById(R.id.etMobile);
        etName = findViewById(R.id.etName);
        etOTP = findViewById(R.id.etOTP);
        btnProceed = findViewById(R.id.btnProceed);

        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 9) {
                    REQUEST_TYPE = 0;
                    networkCallUsingVolleyApi(Constants.URL.DMT_TRANSACTION, true);
                } else {
                    visibilityCon(false);
                }
            }
        });

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dmt_account_search);
        init();

        btnProceed.setOnClickListener(v -> {
            if (isValid()) {
                REQUEST_TYPE = 1;
                networkCallUsingVolleyApi(Constants.URL.DMT_TRANSACTION, true);
            }
        });
    }

    private boolean isValid() {
        if (etMobile.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (REQUEST_TYPE == 1) {
            if (etName.getText().toString().equals("")) {
                Toast.makeText(this, "Please enter first name", Toast.LENGTH_SHORT).show();
                return false;
            } else if (etOTP.getText().toString().equals("")) {
                Toast.makeText(this, "Please enter otp", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void networkCallUsingVolleyApi(String url, boolean isLoad) {
        if (AppManager.isOnline(this)) {
            new VolleyNetworkCall(this, this, url, 1, param(), isLoad).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", etMobile.getText().toString());
        if (REQUEST_TYPE == 0)
            map.put("type", "verification");
        else {
            map.put("type", "registration");
            map.put("fname", etName.getText().toString());
            map.put("otp", etOTP.getText().toString());
        }
        return map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        try {
            String status = AppHandler.getStatus(JSonResponse);
            JSONObject jsonObject = new JSONObject(JSonResponse);
            if (REQUEST_TYPE == 0) {
                if (status.equalsIgnoreCase("RNF"))
                    visibilityCon(true);
                else{
                    String name = jsonObject.getString("name");
                    String totallimit = jsonObject.getString("totallimit");
                    String usedlimit = jsonObject.getString("usedlimit");
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(this, BenDetails.class);
                    bundle.putString("sender_number", etMobile.getText().toString());
                    bundle.putString("name", name);
                    bundle.putString("status", status);
                    bundle.putString("available_limit", totallimit);
                    bundle.putString("total_spend", usedlimit);
                    bundle.putString("json", JSonResponse);
                    bundle.putInt("tab", 1);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            } else {
                REQUEST_TYPE = 0;
                networkCallUsingVolleyApi(Constants.URL.DMT_TRANSACTION, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Print.P(msg);
    }

    private void visibilityCon(boolean b) {
        if (b) {
            btnProceed.setVisibility(View.VISIBLE);
            findViewById(R.id.nameCon).setVisibility(View.VISIBLE);
            findViewById(R.id.otpCon).setVisibility(View.VISIBLE);
        } else {
            btnProceed.setVisibility(View.GONE);
            findViewById(R.id.nameCon).setVisibility(View.GONE);
            findViewById(R.id.otpCon).setVisibility(View.GONE);
        }
    }
}
