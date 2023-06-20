package com.payment.aeps.fingpay_microatm;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.fingpay.microatmsdk.MicroAtmLoginScreen;
import com.fingpay.microatmsdk.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.payment.aeps.R;
import com.payment.aeps.app.ModuleConstants;
import com.payment.aeps.fingpay_microatm.FingSdkUtill.FingUtil;
import com.payment.aeps.fingpay_microatm.printer.Invoice;
import com.payment.aeps.fingpay_microatm.printer.MiniStatementInvoice;
import com.payment.aeps.network.VolleyGetNetworkCall;
import com.payment.aeps.permission.AppPermissions;
import com.payment.aeps.permission.PermissionHandler;
import com.payment.aeps.util.ModuleSharedPrefs;
import com.payment.aeps.util.ModuleUtil;
import com.payment.aeps.util.Print;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FingPayMicroAtm extends Activity implements VolleyGetNetworkCall.RequestResponseLis {
    public static final String SUPER_MERCHANT_ID = "2";
    private static final int CODE = 1;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE};
    String mobile;
    String amount;
    TextView type;
    private Context context;
    private String TYPE;
    private EditText merchIdEt, passwordEt, amountEt, remarksEt;
    private TextView tvMobile;
    private RadioGroup radioGroup;
    private Button fingPayBtn, historyBtn;
    private TextView respTv;
    private SharedPreferences permissionStatus;
    private String inputMobileNumber;

    public static boolean isValidString(String str) {
        if (str != null) {
            str = str.trim();
            return str.length() > 0;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fing_pay_activity);
        TYPE = getIntent().getStringExtra("type");
        inputMobileNumber = getIntent().getStringExtra("mobile");
        String userId = getIntent().getStringExtra("userId");
        String appToken = getIntent().getStringExtra("appToken");
        ModuleSharedPrefs.setValue(this, ModuleSharedPrefs.USER_ID, userId);
        ModuleSharedPrefs.setValue(this, ModuleSharedPrefs.APP_TOKEN, appToken);
        ModuleSharedPrefs.setValue(this, ModuleSharedPrefs.LOGIN_ID, inputMobileNumber);

        init();

        switch (TYPE) {
            case "MS":
            case "PR":
            case "CP":
            case "CA":
            case "BE":
                amountEt.setText("0");
                amountEt.setVisibility(View.GONE);
                findViewById(R.id.inputCon).setVisibility(View.GONE);
        }

        type.setText(FingUtil.getTypeName(TYPE));
        tvMobile.setText(inputMobileNumber);
        fingPayBtn.setOnClickListener(view -> {
            mobile = inputMobileNumber;
            amount = amountEt.getText().toString().trim();
            if (mobile.length() > 0 && amount.length() > 0) {
                initiateFingPayMATM();
            } else {
                Toast.makeText(context, "Mobile number is required", Toast.LENGTH_SHORT).show();
            }
        });

        AppPermissions.check(this, permissionsRequired, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {
            }
        });

        checkPermission();
    }

    private void checkPermission() {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(context)
                    .setMessage("GPS Location is not Enable")
                    .setPositiveButton("Location Setting", (paramDialogInterface, paramInt) ->
                            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    private void init() {
        permissionStatus = getSharedPreferences("microatm_sample", 0);
        context = FingPayMicroAtm.this;
        tvMobile = findViewById(R.id.et_mobile);
        amountEt = findViewById(R.id.et_amount);
        fingPayBtn = findViewById(R.id.btn_fingpay);
        type = findViewById(R.id.type);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ModuleConstants.ReceiptMap = new LinkedHashMap<>();
        ModuleConstants.InvoiceModelList = new ArrayList<>();
//        try {
        if (resultCode == RESULT_OK && requestCode == CODE) {
            boolean status = data.getBooleanExtra(Constants.TRANS_STATUS, false);
            String response = data.getStringExtra(Constants.MESSAGE);
            double transAmount = data.getDoubleExtra(Constants.TRANS_AMOUNT, 0);
            double balAmount = data.getDoubleExtra(Constants.BALANCE_AMOUNT, 0);
            String bankRrn = data.getStringExtra(Constants.RRN);
            String transType = data.getStringExtra(Constants.TRANS_TYPE);
            int type = data.getIntExtra(Constants.TYPE, Constants.CASH_WITHDRAWAL);
            String cardNum = data.getStringExtra(Constants.CARD_NUM);
            String bankName = data.getStringExtra(Constants.BANK_NAME);
            String cardType = data.getStringExtra(Constants.CARD_TYPE);
            String terminalId = data.getStringExtra(Constants.TERMINAL_ID);
            String fpId = data.getStringExtra(Constants.FP_TRANS_ID);
            // String transId = data.getStringExtra(Constants.TRANS_ID);
            //  String transId = data.getStringExtra(Constants.TRANS_ID);
            String responseCOde = data.getStringExtra(Constants.RESPONSE_CODE);
            // String Statuscode = data.getStringExtra(Constants.STATUS_CODE);
            String Statuscode = ".";
            Log.e("Data", "" + status + "\nResponse Code" + responseCOde + "\n Statuscode" + Statuscode + "\n" + response
                    + "\n" + transAmount + "\n" + balAmount + "\n" + bankRrn + "\n" + transType + "\n" + type + "\n" + cardNum + "\n" + bankName + "\n" + cardType
                    + "\n" + terminalId + "\n" + fpId + "\n" + "." + "\n" + response);

            //   Toast.makeText(this, "abhishek"+status+"\nResponse Code"+responseCOde+"\n Statuscode"+Statuscode+"\n"+response, Toast.LENGTH_LONG).show();


            if (status == true && responseCOde.equals("00")) {

                if (isValidString(response)) {
                    //  ModuleConstants.ReceiptMap.put("TransId", String.valueOf(transId));
                    //   ModuleConstants.ReceiptMap.put("TransId", "1");
                    // ModuleConstants.InvoiceModelList.add(new InvocieModel("." + String.valueOf(transId), "transactionId", "TransId"));
                    // ModuleConstants.InvoiceModelList.add(new InvocieModel("1" , "transactionId", "TransId"));

                    if (fpId != null && fpId.length() > 0) {
                        ModuleConstants.ReceiptMap.put("FpId", String.valueOf(fpId));
                        ModuleConstants.InvoiceModelList.add(new InvocieModel(String.valueOf(fpId), "fpId", "FpId"));
                    }
                    Log.e("tag :", "1");
                    //  ModuleConstants.ReceiptMap.put("StatusCode", String.valueOf(statusCode));
                    ModuleConstants.ReceiptMap.put("StatusCode", String.valueOf("."));
                    //  ModuleConstants.InvoiceModelList.add(new InvocieModel(String.valueOf(fpId), "statusCode", "statusCode"));
                    ModuleConstants.InvoiceModelList.add(new InvocieModel(".", "statusCode", "statusCode"));
/* if (statusCode != null && statusCode.length() > 0) {
                        ModuleConstants.ReceiptMap.put("StatusCode", String.valueOf(statusCode));
                        ModuleConstants.InvoiceModelList.add(new InvocieModel(String.valueOf(fpId), "statusCode", "statusCode"));
                    }*/

                    Log.e("tag :", "2");
                    if (terminalId != null && terminalId.length() > 0) {
                        ModuleConstants.ReceiptMap.put("TerminalId", String.valueOf(terminalId));
                        ModuleConstants.InvoiceModelList.add(new InvocieModel(String.valueOf(terminalId), "terminalId", "TerminalId"));
                    }
                    Log.e("tag :", "3");
                    ModuleConstants.ReceiptMap.put("Status", String.valueOf(status));
                    ModuleConstants.InvoiceModelList.add(new InvocieModel(String.valueOf(status), "status", "Status"));
                    Log.e("tag :", "4");
                    ModuleConstants.ReceiptMap.put("Message", String.valueOf(response));
                    ModuleConstants.InvoiceModelList.add(new InvocieModel(String.valueOf(response), "message", "Message"));
                    Log.e("tag :", "5");
                    if (bankRrn != null && bankRrn.length() > 0) {
                        ModuleConstants.ReceiptMap.put("BankRRN ", String.valueOf(bankRrn));
                        ModuleConstants.InvoiceModelList.add(new InvocieModel(String.valueOf(bankRrn), "bankRrn", "BankRRN"));
                    }
                    Log.e("tag :", "6");
                    if (transType != null && transType.length() > 0) {
                        ModuleConstants.ReceiptMap.put("TranType", String.valueOf(transType));
                        ModuleConstants.InvoiceModelList.add(new InvocieModel(String.valueOf(transType), "transType", "TranType"));
                    }
                    Log.e("tag :", "7");
                    ModuleConstants.ReceiptMap.put("Type", FingUtil.getTypeName(type));
                    ModuleConstants.InvoiceModelList.add(new InvocieModel(String.valueOf(type), "type", "Type"));
                    Log.e("tag :", "8");
                    if (cardNum != null && cardNum.length() > 0) {
                        ModuleConstants.ReceiptMap.put("CardNum", String.valueOf(cardNum));
                        ModuleConstants.InvoiceModelList.add(new InvocieModel(String.valueOf(cardNum), "cardNum", "CardNum"));
                    }
                    Log.e("tag :", "9");
                    if (cardType != null && cardType.length() > 0) {
                        ModuleConstants.ReceiptMap.put("CardType", String.valueOf(cardType));
                        ModuleConstants.InvoiceModelList.add(new InvocieModel(String.valueOf(cardType), "cardType", "CardType"));
                    }
                    Log.e("tag :", "10");
                    if (bankName != null && bankName.length() > 0) {
                        ModuleConstants.ReceiptMap.put("BankName", String.valueOf(bankName));
                        ModuleConstants.InvoiceModelList.add(new InvocieModel(String.valueOf(bankName), "bankName", "BankName"));
                    }
                    Log.e("tag :", "11");
                    ModuleConstants.ReceiptMap.put("TransAmount", String.valueOf(transAmount));
                    ModuleConstants.InvoiceModelList.add(new InvocieModel(String.valueOf(transAmount), "transAmount", "TransAmount"));
                    ModuleConstants.ReceiptMap.put("BalanceAmount", String.valueOf(balAmount));
                    ModuleConstants.InvoiceModelList.add(new InvocieModel(String.valueOf(balAmount), "balAmount", "BalanceAmount"));

                    if (type == Constants.MINI_STATEMENT) {
                        FingUtil.StatementList = data.getParcelableArrayListExtra(Constants.LIST);
                    }
                    Log.e("tag :", "12");
                    Intent i = null;
                    if (type == Constants.MINI_STATEMENT) {
                        Log.e("tag :", "13");
                        i = new Intent(this, MiniStatementInvoice.class);
                    } else {
                        Log.e("tag :", "14");
                        i = new Intent(this, Invoice.class);
                    }
                    startActivity(i);
                }
            } else if (status == false) {
                if (isValidString(response)) {

                    Log.e("tag :", "3");
                    ModuleConstants.ReceiptMap.put("Status", "Transaction failed");
                    ModuleConstants.InvoiceModelList.add(new InvocieModel("Transaction failed", "status", "Status"));
                    Log.e("tag :", "4");
                    ModuleConstants.ReceiptMap.put("Message", String.valueOf(response));
                    ModuleConstants.InvoiceModelList.add(new InvocieModel(String.valueOf(response), "message", "Message"));

                    Intent i = null;
                    if (type == Constants.MINI_STATEMENT) {
                        Log.e("tag :", "13");
                        i = new Intent(this, MiniStatementInvoice.class);
                    } else {
                        Log.e("tag :", "14");
                        i = new Intent(this, Invoice.class);
                    }
                    startActivity(i);
                }
            } else if (requestCode == REQUEST_PERMISSION_SETTING) {
                if (ActivityCompat.checkSelfPermission(FingPayMicroAtm.this, permissionsRequired[0]) ==
                        PackageManager.PERMISSION_GRANTED) {
                }
                Toast.makeText(context, "Permission Related Issue Accord", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private List<String> getUngrantedPermissions() {
        List<String> permissions = new ArrayList<>();
        for (String s : permissionsRequired) {
            if (ContextCompat.checkSelfPermission(context, s) != PackageManager.PERMISSION_GRANTED)
                permissions.add(s);
        }
        return permissions;
    }

    private void initiateFingPayMATM() {
        volleyNetworkCall(ModuleConstants.FMATM_INITIATE);
    }

    private Map<String, String> param() {
        String userId = ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.USER_ID);
        String token = ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.APP_TOKEN);
        Map<String, String> map = new HashMap<>();
        map.put("apptoken", token);
        map.put("user_id", userId);
        map.put("mobile", mobile);
        map.put("imei", FingUtil.getImei(this));
        map.put("transactionType", TYPE);
        map.put("amount", amount);
        map.put("remark", "Test");

        String json = new JSONObject(map).toString();
        Print.P("PARAM: " + json);
        return map;
    }

    private void volleyNetworkCall(String url) {
        Print.P("" + url);
        if (ModuleUtil.isOnline(this)) {
            new VolleyGetNetworkCall(this, this, url, 1, param()).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        Gson gson = new GsonBuilder().create();
        Print.P("RES: " + JSonResponse);
        try {
            JSONObject jsonObject = new JSONObject(JSonResponse);
            String status = jsonObject.getString("status");
            String message = jsonObject.getString("message");
            if (status.equalsIgnoreCase("TXN")) {
                FingPayInitiateModel model = gson.fromJson(jsonObject.getJSONObject("data").toString(),
                        FingPayInitiateModel.class);
                Intent intent = new Intent(FingPayMicroAtm.this, MicroAtmLoginScreen.class);
                intent.putExtra(Constants.MERCHANT_USERID, model.getMerchantId());
                intent.putExtra(Constants.MERCHANT_PASSWORD, model.getMerchantPassword());
                intent.putExtra(Constants.REMARKS, "Demo");
                intent.putExtra(Constants.AMOUNT_EDITABLE, false);
                intent.putExtra(Constants.TXN_ID, model.getTxnid());
                intent.putExtra(Constants.SUPER_MERCHANTID, model.getSuperMerchentId());
                intent.putExtra(Constants.LATITUDE, Double.parseDouble(model.getLat()));
                intent.putExtra(Constants.LONGITUDE, Double.parseDouble(model.getLon()));
                intent.putExtra(Constants.IMEI, FingUtil.getImei(this));
                intent.putExtra(Constants.AMOUNT, amount);
                intent.putExtra(Constants.MOBILE_NUMBER, mobile);
                ModuleConstants.FING_TXN_ID = model.getTxnid();
                switch (TYPE) {
                    case "CW":
                        intent.putExtra(Constants.TYPE, Constants.CASH_WITHDRAWAL);
                        break;
                    case "CD":
                        intent.putExtra(Constants.TYPE, Constants.CASH_DEPOSIT);
                        break;
                    case "BE":
                        intent.putExtra(Constants.TYPE, Constants.BALANCE_ENQUIRY);
                        break;
                    case "MS":
                        intent.putExtra(Constants.TYPE, Constants.MINI_STATEMENT);
                        break;
                    case "PR":
                        intent.putExtra(Constants.TYPE, Constants.PIN_RESET);
                        break;
                    case "CP":
                        intent.putExtra(Constants.TYPE, Constants.CHANGE_PIN);
                        break;
                    case "CA":
                        intent.putExtra(Constants.TYPE, Constants.CARD_ACTIVATION);
                        break;
                }
                intent.putExtra(Constants.MICROATM_MANUFACTURER, Constants.MoreFun);
                startActivityForResult(intent, CODE);
            } else {
                Toast.makeText(context, "1" + message, Toast.LENGTH_SHORT).show();
                Log.e("errorData", "" + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Toast.makeText(context, "Network Request Error : " + msg, Toast.LENGTH_SHORT).show();
    }
}
