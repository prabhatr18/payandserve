package com.payment.aeps.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.payment.aeps.ParentActivity;
import com.payment.aeps.R;
import com.payment.aeps.app.ModuleConstants;
import com.payment.aeps.network.VolleyGetNetworkCall;
import com.payment.aeps.util.DeviceDataModel;
import com.payment.aeps.util.ModuleSharedPrefs;
import com.payment.aeps.util.ModuleUtil;
import com.payment.aeps.util.Print;
import com.payment.aeps.util.utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CashDeposite extends ParentActivity implements View.OnClickListener, VolleyGetNetworkCall.RequestResponseLis {
    private EditText etMobile, etAmount;
    private EditText etAadhaar;
    private ImageView imgPlus, imgMinus;
    private TextView tvAmount1, tvAmount2, tvAmount3, tvAmount4, tvAmount5, tvBank;
    private long amount = 0;
    private String bankId, bankName;
    final private int BANK_CODE = 100;
    private Button btnProceed;
    String transactionType;
    private String userId;
    private String appToken;
    private static final int MY_PERMISSIONS = 2000;

    private void init() {
        userId = getIntent().getStringExtra("userId");
        appToken = getIntent().getStringExtra("appToken");
        ModuleSharedPrefs.setValue(this, ModuleSharedPrefs.USER_ID, userId);
        ModuleSharedPrefs.setValue(this, ModuleSharedPrefs.APP_TOKEN, appToken);

        etMobile = findViewById(R.id.etMobile);
        etAadhaar = findViewById(R.id.etAadhaar);
        tvAmount1 = findViewById(R.id.tvAmount1);
        tvAmount2 = findViewById(R.id.tvAmount2);
        tvAmount3 = findViewById(R.id.tvAmount3);
        tvAmount4 = findViewById(R.id.tvAmount4);
        tvAmount5 = findViewById(R.id.tvAmount5);
        imgPlus = findViewById(R.id.imgPlus);
        btnProceed = findViewById(R.id.btnProceed);
        etAmount = findViewById(R.id.etAmount);
        imgMinus = findViewById(R.id.imgMinus);
        tvBank = findViewById(R.id.tvBank);

        setClickLis();

        setupToolBar("Cash Deposit");
        transactionType = "BE";
    }

    private void setupToolBar(String title) {
        TextView tvToolbar = findViewById(R.id.tvToolBarTitle);
        tvToolbar.setText(title);
        ImageView backImage = findViewById(R.id.imgBack);
        backImage.setOnClickListener(view -> finish());
    }

    private void setClickLis() {
        tvAmount1.setOnClickListener(this);
        tvAmount2.setOnClickListener(this);
        tvAmount3.setOnClickListener(this);
        tvAmount4.setOnClickListener(this);
        tvAmount5.setOnClickListener(this);
        etAmount.setOnClickListener(this);
        imgPlus.setOnClickListener(this);
        imgMinus.setOnClickListener(this);
        tvBank.setOnClickListener(this);

        tvBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CashDeposite.this, BankList.class);
                startActivityForResult(i, BANK_CODE);
            }
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    networkCallUsingVolleyApi(ModuleConstants.CASH_DEPO_API);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cash_deposite);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        } else {
            init();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        int accessCourseLocation = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        int readPhoneState = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
        List<String> permissions = new ArrayList<>();
        if (accessCourseLocation != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (readPhoneState != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (permissions.size() > 0) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), MY_PERMISSIONS);
        } else {
            init();
        }
    }

    boolean permissionState;

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        boolean isAllPermissionGranted = false;
        switch (requestCode) {
            case MY_PERMISSIONS: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d("Permissions", "Permission Granted: " + permissions[i]);
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        if (!permissionState)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermission();
                                boolean showRationale = shouldShowRequestPermissionRationale(permissions[i]);
                                if (!showRationale) {
                                    permissionState = true;
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CashDeposite.this);
                                    alertDialogBuilder.setTitle("Permission Deny");
                                    alertDialogBuilder.setMessage("Application unable to run while " + permissions[i] + " permission deny! Open app setting and enable permission.");
                                    alertDialogBuilder
                                            .setCancelable(false)
                                            .setPositiveButton("Setting",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                            dialog.cancel();
                                                            Intent intent = new Intent();
                                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                            intent.setData(uri);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            }
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermission();
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    private boolean isValid() {
//        if (bankId == null || bankId.length() == 0) {
//            Toast.makeText(this, "Bank selection is required", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        if (etAadhaar == null || etAadhaar.getText().toString().length() == 0) {
            Toast.makeText(this, "Please provide valid account number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etMobile == null || etMobile.getText().toString().length() != 10) {
            Toast.makeText(this, "Please provide valid mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etAmount == null || etAmount.getText().toString().length() == 0) {
            Toast.makeText(this, "Please input valid amount string", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Double.parseDouble(etAmount.getText().toString()) < 100) {
            Toast.makeText(this, "Minimum amount should be 100", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void log(String log) {
        //Log.e("LOG: ", log);
    }

    @Override
    public void onClick(View view) {
        if (etAmount.getText().toString().length() > 0)
            amount = Long.parseLong(etAmount.getText().toString());
        if (view.getId() == R.id.tvAmount1) {
            amount = 100;
        } else if (view.getId() == R.id.tvAmount2) {
            amount = 500;
        } else if (view.getId() == R.id.tvAmount3) {
            amount = 1000;
        } else if (view.getId() == R.id.tvAmount4) {
            amount = 1500;
        } else if (view.getId() == R.id.tvAmount5) {
            amount = 2000;
        } else if (view.getId() == R.id.imgPlus) {
            amount++;
        } else if (view.getId() == R.id.imgMinus) {
            if (amount > 100)
                amount--;
        }
        setAmount();
    }

    private void setAmount() {
        etAmount.setText(String.valueOf(amount));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        DeviceDataModel dataModel;
        try {
            switch (requestCode) {
                case BANK_CODE:
                    bankId = data.getStringExtra("id");
                    bankName = data.getStringExtra("name");
                    tvBank.setText(bankName);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        ModuleConstants.LOCAL_Map = new HashMap<>();
        ModuleConstants.LOCAL_Map.put("user_id", ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.USER_ID));
        ModuleConstants.LOCAL_Map.put("apptoken", ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.APP_TOKEN));
        ModuleConstants.LOCAL_Map.put("transactionType", "sendotp");  //"value": "BE/CW/MS/M"
        ModuleConstants.LOCAL_Map.put("mobileNumber", etMobile.getText().toString());
        ModuleConstants.LOCAL_Map.put("accountNumber", etAadhaar.getText().toString());
        //ModuleConstants.LOCAL_Map.put("iinno", bankId);
        ModuleConstants.LOCAL_Map.put("iinno", "508534");
        ModuleConstants.LOCAL_Map.put("imei", utils.getImei(this));
        ModuleConstants.LOCAL_Map.put("transactionAmount", etAmount.getText().toString());
        Print.P("PARAM : \n " + new JSONObject(ModuleConstants.LOCAL_Map).toString());
        Print.appendLog("---------------------------- NEW Deposite REQUEST ----------------------");
        Print.appendLog(new JSONObject(ModuleConstants.LOCAL_Map).toString());
        return ModuleConstants.LOCAL_Map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        try {
            Print.appendLog(JSonResponse);
            Print.P("API RES : \n" + JSonResponse);
            JSONObject obj = new JSONObject(JSonResponse);
            String status = obj.getString("status");

            if (status.equalsIgnoreCase("TXN")) {
                String txnID = obj.getString("txnid");
                if (obj.has("message")) {
                    String msg = obj.getString("message");
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }
                if (txnID != null && txnID.length() > 0) {
                    Intent i = new Intent(CashDeposite.this, OTPVaidate.class);
                    i.putExtra("txnId", txnID);
                    i.putExtra("mobile", "+91" + etMobile.getText().toString());
                    startActivity(i);
                }
            } else {
                String msg = obj.getString("message");
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        showMsg(msg);
    }

    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}