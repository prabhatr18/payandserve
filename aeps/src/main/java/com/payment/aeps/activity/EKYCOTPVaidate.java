package com.payment.aeps.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.payment.aeps.R;
import com.payment.aeps.app.ModuleConstants;
import com.payment.aeps.network.VolleyGetNetworkCall;
import com.payment.aeps.util.ModuleSharedPrefs;
import com.payment.aeps.util.ModuleUtil;
import com.payment.aeps.util.Print;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EKYCOTPVaidate extends AppCompatActivity implements VolleyGetNetworkCall.RequestResponseLis {
    private String url;
    private Context context;
    private EditText otpTextView;
    private TextView title;
    private Button btnProceed;
    private String type = "useronboardotp";
    private int REQUEST_TYPE = 0;
    private String primaryKeyId = "";
    private String encodeFPTxnId = "";

    private void init() {
        context = this;
        setupToolBar();
        otpTextView = findViewById(R.id.otp_view);
        btnProceed = findViewById(R.id.btnProceed);
    }

    private void setupToolBar() {
        ////TextView tv = findViewById(R.id.tvToolBarTitle);
        //tv.setText("OTP Verification");
        ImageView backImage = findViewById(R.id.imgClose);
        backImage.setOnClickListener(view -> finish());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ekyc_otp_validation);

        init();

        String userId = getIntent().getStringExtra("userId");
        String appToken = getIntent().getStringExtra("appToken");
        String inputMobileNumber = getIntent().getStringExtra("mobile");

        ModuleSharedPrefs.setValue(this, ModuleSharedPrefs.USER_ID, userId);
        ModuleSharedPrefs.setValue(this, ModuleSharedPrefs.APP_TOKEN, appToken);
        ModuleSharedPrefs.setValue(this, ModuleSharedPrefs.LOGIN_ID, inputMobileNumber);

        btnProceed.setOnClickListener(v -> {
            if (otpTextView.getText().toString().length() > 3) {
                type = "useronboardvalidate";
                String url = ModuleConstants.EKYC_URL;
               /* if (AEPS_TYPE.equalsIgnoreCase(PAYSPRINT_AEPS)) {
                    url = ModuleConstants.baseUrl + "raeps/transaction";
                  //  url = ModuleConstants.baseUrl + "iaeps/transaction";
                }*/
                networkCallUsingVolleyApi(url);
            } else {
                Toast.makeText(context, "Please input valid otp", Toast.LENGTH_SHORT).show();
            }
        });


        deviceSelection();
       /* String deviceValue = ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.SELECTED_DEVICE_INDEX);
        if (deviceValue == null || deviceValue.length() == 0) {
            Toast.makeText(this, "Scanner device selection is required", Toast.LENGTH_SHORT).show();
            finish();
        } else {

            REQUEST_TYPE = 0;
            networkCallUsingVolleyApi(ModuleConstants.EKYC_URL);
        }*/

       /* String deviceValue = ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.SELECTED_DEVICE_INDEX);
        Log.d("deviceValue :",""+deviceValue);
        if (deviceValue == null || deviceValue.length() == 0) {
            Toast.makeText(this, "Scanner device selection is required", Toast.LENGTH_SHORT).show();
            finish();
        } else {

        }*/
    }

    private void deviceSelection() {

        String[] deviceType = new String[]{"Mantra", "Morpho", "Tatvik", "Startek", "SecuGen", "Evolute"};
        int from; //This must be declared as global !
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Please select one option");
        alert.setSingleChoiceItems(deviceType, -1, (dialog, which) -> {

            ModuleSharedPrefs.setValue(EKYCOTPVaidate.this, ModuleSharedPrefs.SELECTED_DEVICE, deviceType[which]);
            ModuleSharedPrefs.setValue(EKYCOTPVaidate.this, ModuleSharedPrefs.SELECTED_DEVICE_INDEX, String.valueOf(which));


        });

        alert.setPositiveButton("OK", (dialog, which) -> {
            String deviceId = ModuleSharedPrefs.getValue(EKYCOTPVaidate.this, ModuleSharedPrefs.SELECTED_DEVICE_INDEX);

            //  Toast.makeText(context, "" + deviceId, Toast.LENGTH_SHORT).show();

            if (deviceId.equals("-1")) {
                Toast.makeText(context, "Select device first...", Toast.LENGTH_SHORT).show();
                alert.setCancelable(false);
                alert.show();
            } else {
                REQUEST_TYPE = 0;
                String url = ModuleConstants.EKYC_URL;
                networkCallUsingVolleyApi(url);
                dialog.dismiss();
            }


        });
        alert.setCancelable(false);
        alert.show();
    }

    private void networkCallUsingVolleyApi(String url) {
        Print.P("URL : " + url);
        if (ModuleUtil.isOnline(this)) {
            new VolleyGetNetworkCall(this, this, url, 1, param()).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("transactionType", type);
        // map.put("user_id", ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.USER_ID));
        // map.put("token", ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.APP_TOKEN));
        map.put("user_id", ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.USER_ID));
        map.put("token", ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.APP_TOKEN));
        if (type.equalsIgnoreCase("useronboardvalidate")) {
            map.put("primaryKeyId", primaryKeyId);
            map.put("encodeFPTxnId", encodeFPTxnId);
            map.put("otp", otpTextView.getText().toString());
        }

        Print.P("PARAM : \n " + new JSONObject(map).toString());
        Print.appendLog("------- OTP validation screen ----------");
        Print.appendLog(new JSONObject(map).toString());
        return map;
    }


    @Override
    public void onSuccessRequest(String result) {
        Print.P("Response : " + result);
        Print.appendLog("------- Api Response ----------");
        Print.appendLog(result);
        try {
            JSONObject obj = new JSONObject(result);
            String message = obj.getString("message");
            String status = "";
            if (obj.has("status"))
                status = obj.getString("status");
            else {
                status = obj.getString("statuscode");
            }
            if (status.equalsIgnoreCase("TXNOTP")) {
                primaryKeyId = obj.getString("primaryKeyId");
                encodeFPTxnId = obj.getString("encodeFPTxnId");
                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
            } else if (status.equalsIgnoreCase("TXN")) {
                startActivity(new Intent(this, EKYCRegister.class)
                        .putExtra("primaryKeyId", primaryKeyId)
                        .putExtra("encodeFPTxnId", encodeFPTxnId));
                finish();
            } else {
                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
