package com.payment.aeps;

import static com.payment.aeps.app.ModuleConstants.AEPS_TYPE;
import static com.payment.aeps.app.ModuleConstants.FING_AEPS;
import static com.payment.aeps.app.ModuleConstants.PAYSPRINT_AEPS;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.payment.aeps.activity.CashWithdraw;
import com.payment.aeps.activity.EKYCOTPVaidate;
import com.payment.aeps.app.ModuleConstants;
import com.payment.aeps.network.VolleyGetNetworkCall;
import com.payment.aeps.util.ModuleSharedPrefs;
import com.payment.aeps.util.ModuleUtil;
import com.payment.aeps.util.Print;
import com.paysprint.onboardinglib.activities.HostActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AepsDashboard extends ParentActivity implements View.OnClickListener, VolleyGetNetworkCall.RequestResponseLis {
    private String userId;
    private String appToken;
    private LinearLayout secCashWithDraw, secAadhaar, secBalanceEnquiry, secStatement, secKyc;
    private TextView tvDevice;

    private void init() {
        secCashWithDraw = findViewById(R.id.secCashWithDraw);
        secKyc = findViewById(R.id.secKyc);
        secAadhaar = findViewById(R.id.secAadhaar);
        secBalanceEnquiry = findViewById(R.id.secBalanceEnquiry);
        secStatement = findViewById(R.id.secStatement);
        tvDevice = findViewById(R.id.tvDevice);
        String selectedDevice = ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.SELECTED_DEVICE);
        if (selectedDevice != null && selectedDevice.length() > 0) {
            String device = "Selected Device  -  " + selectedDevice;
            tvDevice.setText(device);
        } else {
            deviceSelection();
        }
        userId = getIntent().getStringExtra("userId");
        appToken = getIntent().getStringExtra("appToken");
        String aepsType = getIntent().getStringExtra("aepsType");
        AEPS_TYPE = FING_AEPS;
        if (aepsType.equalsIgnoreCase(PAYSPRINT_AEPS)) {
            AEPS_TYPE = PAYSPRINT_AEPS;
        }
        ModuleSharedPrefs.setValue(this, ModuleSharedPrefs.USER_ID, userId);
        ModuleSharedPrefs.setValue(this, ModuleSharedPrefs.APP_TOKEN, appToken);
        setLis();
    }

    private void setLis() {
        tvDevice.setOnClickListener(this);
        secCashWithDraw.setOnClickListener(this);
        secAadhaar.setOnClickListener(this);
        secBalanceEnquiry.setOnClickListener(this);
        secStatement.setOnClickListener(this);
        secKyc.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_aeps_dashboard);
        init();

        if (!ModuleUtil.isAllValueAvailable(userId, appToken, this)) {
            finish();
        }

        ModuleSharedPrefs.setValue(AepsDashboard.this, ModuleSharedPrefs.IS_KYC_VARIFIED, "false");
        findViewById(R.id.secNotice).setVisibility(View.VISIBLE);
        if (AEPS_TYPE.equalsIgnoreCase(PAYSPRINT_AEPS)) {
            networkCallUsingVolleyApi(ModuleConstants.baseUrl + "raeps/getdata");
        } else {
            networkCallUsingVolleyApi(ModuleConstants.bankList);
        }
    }

    private void networkCallUsingVolleyApi(String url) {
        if (ModuleUtil.isOnline(this)) {
            new VolleyGetNetworkCall(this, this, url, 1, param()).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.USER_ID));
        map.put("apptoken", ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.APP_TOKEN));
        Print.P("BANK PARAM" + new JSONObject(map));
        return map;
    }


    private void deviceSelection() {
        String[] deviceType = new String[]{"Mantra", "Morpho", "Tatvik", "Startek", "SecuGen", "Evolute"};
        int from; //This must be declared as global !
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Please select one option");
        alert.setSingleChoiceItems(deviceType, -1, (dialog, which) -> {
            ModuleSharedPrefs.setValue(AepsDashboard.this, ModuleSharedPrefs.SELECTED_DEVICE, deviceType[which]);
            ModuleSharedPrefs.setValue(AepsDashboard.this, ModuleSharedPrefs.SELECTED_DEVICE_INDEX, String.valueOf(which));
            tvDevice.setText("Selected Device  -  " + deviceType[which]);
        });

        alert.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
        });
        alert.setCancelable(false);
        alert.show();
    }

    @Override
    public void onClick(View view) {
//        String kyc = ModuleSharedPrefs.getValue(AepsDashboard.this, ModuleSharedPrefs.IS_KYC_VARIFIED);
      //  String kyc = "success";

//        Log.d("KYC DATA :",""+kyc);

//        if (kyc == null || !kyc.equalsIgnoreCase("success")) {
//            Toast.makeText(this, "Your eKYC is " + kyc, Toast.LENGTH_SHORT).show();
//            Intent i = new Intent(AepsDashboard.this, EKYCOTPVaidate.class);
//            startActivity(i);
//        } else {
            if (view.getId() == R.id.tvDevice) {
                deviceSelection();
            } else if (view.getId() == R.id.secCashWithDraw) {
                Intent i = new Intent(AepsDashboard.this, CashWithdraw.class);
                i.putExtra("option", "3");
                startActivity(i);
            } else if (view.getId() == R.id.secAadhaar) {
                Intent i = new Intent(AepsDashboard.this, CashWithdraw.class);
                i.putExtra("option", "2");
                startActivity(i);
            } else if (view.getId() == R.id.secBalanceEnquiry) {
                Intent i = new Intent(AepsDashboard.this, CashWithdraw.class);
                i.putExtra("option", "1");
                startActivity(i);
            } else if (view.getId() == R.id.secStatement) {
                Intent i = new Intent(AepsDashboard.this, CashWithdraw.class);
                i.putExtra("option", "4");
                startActivity(i);
            }
//            else if (view.getId() == R.id.secKyc) {
//                Intent i = new Intent(AepsDashboard.this, EKYCOTPVaidate.class);
//                startActivity(i);
//            }
//        }
    }

    public boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            return false;
        }
        return true;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
//        try {
//            String statuscode = "";
//            JSONObject obj = new JSONObject(JSonResponse);
//            statuscode = obj.getString("statuscode");
//            if (statuscode.equalsIgnoreCase("TXN")) {
//                if (obj.has("data")) obj = obj.getJSONObject("data");
//                if (obj.has("agent")) {
//                    String status = "";
//                    obj.getString("status");
//                    if (obj.isNull("agent")) {
//                        if (AEPS_TYPE.equalsIgnoreCase(PAYSPRINT_AEPS)) {
//                            openPSOnboarding();
//                        } else if (status.equalsIgnoreCase("rejected") || status.equalsIgnoreCase("pending")) {
//                            openPSOnboarding();
//                        } else {
//                            startActivity(new Intent(this, Onboard.class));
//                            finish();
//                        }
//
//                    }
//                }
//
//
//                JSONObject agentObj = obj.getJSONObject("agent");
//                if (AEPS_TYPE.equalsIgnoreCase(PAYSPRINT_AEPS))
//                    agentObj = agentObj.getJSONObject("user");
//                if (agentObj.has("everify") || agentObj.has("kyc")) {
//                    String kycStatus = "";
//                    if (agentObj.has("everify")) {
//                        kycStatus = agentObj.getString("everify");
//                    } else {
//                        kycStatus = agentObj.getString("kyc");
//                    }
//                    if (kycStatus.equalsIgnoreCase("success") || kycStatus.equalsIgnoreCase("verified")) {
//                        kycStatus = "success";
//                    }
//                    ModuleSharedPrefs.setValue(this, ModuleSharedPrefs.IS_KYC_VARIFIED, kycStatus);
//                    if (kycStatus.equalsIgnoreCase("success")) {
//                        findViewById(R.id.secNotice).setVisibility(View.GONE);
//                    } else {
//                        findViewById(R.id.secNotice).setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    Toast.makeText(this, "EKYC status is not available", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(this, "Agent record is not available", Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onFailRequest(String msg) {
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
    }

    private void openPSOnboarding() {
        new VolleyGetNetworkCall(new VolleyGetNetworkCall.RequestResponseLis() {
            @Override
            public void onSuccessRequest(String JSonResponse) {
                try {
                    JSONObject obj = new JSONObject(JSonResponse).getJSONObject("data");
                    Intent intent = new Intent(AepsDashboard.this, HostActivity.class);
                    intent.putExtra("pId", obj.getString("pId"));
                    intent.putExtra("pApiKey", obj.getString("pApiKey"));
                    intent.putExtra("mCode", obj.getString("mCode"));
                    intent.putExtra("mobile", obj.getString("mobile"));
                    intent.putExtra("lat", roundLoc(obj.getString("lat")));
                    intent.putExtra("lng", roundLoc(obj.getString("lng")));
                    intent.putExtra("firm", obj.getString("firm"));
                    intent.putExtra("email", obj.getString("email"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent, 999);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailRequest(String msg) {

            }
        }, this, ModuleConstants.SPRINT_ONBOARDING, 1, param()).netWorkCall();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999) {
            if (resultCode == Activity.RESULT_OK) {
                boolean status = data.getBooleanExtra("status", false);
                int response = data.getIntExtra("response", 0);
                String message = data.getStringExtra("message");
                String detailedResponse = "Status: " + status + ",  Response: " + response + ", Message: " + message;
                Toast.makeText(this, detailedResponse, Toast.LENGTH_LONG).show();
                //btnSubmit.snack(detailedResponse)
                //Log.i(logTag, detailedResponse)
            }
        }
    }

    private String roundLoc(String value) {
        return "" + Math.round(Double.parseDouble(value) * 100000d) / 100000d;
    }
}