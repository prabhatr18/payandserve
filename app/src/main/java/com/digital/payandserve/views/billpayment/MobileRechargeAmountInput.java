package com.digital.payandserve.views.billpayment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.utill.SharedPrefs;
import com.digital.payandserve.views.browseplan.ViewPlans;
import com.digital.payandserve.views.invoice.ReportInvoice;
import com.digital.payandserve.views.invoice.model.InvoiceModel;
import com.digital.payandserve.views.otpview.OTPPinReset;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MobileRechargeAmountInput extends AppCompatActivity implements RequestResponseLis {
    public Context context;
    String circle;
    Dialog dialog;
    int API_CODE = 0;
    private TextView tvMobile;
    private TextView tvMobileCarrier;
    private TextView tvPlans, tvGenPin;
    private EditText etAmount, etTPin;
    private AppCompatButton btnProceed;
    private ImageView icBack, imgProvider;
    private String MOBILE, PROVIDER_NAME, PROVIDER_ID, URL, TYPE;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    private void init() {
        context = MobileRechargeAmountInput.this;
        tvMobile = findViewById(R.id.tvMobile);
        etTPin = findViewById(R.id.etTPin);
        tvGenPin = findViewById(R.id.tvGenPin);
        imgProvider = findViewById(R.id.imgProvider);
        tvMobileCarrier = findViewById(R.id.tvMobileCarrier);
        tvPlans = findViewById(R.id.tvPlans);
        etAmount = findViewById(R.id.etAmount);
        btnProceed = findViewById(R.id.btnProceed);
        icBack = findViewById(R.id.icBack);
        icBack.setOnClickListener(v -> finish());

        MOBILE = getIntent().getStringExtra("mobile");
        // CARRIER_INFO = getIntent().getStringExtra("provider_name");
        //  URL = getIntent().getStringExtra("provider_image");
        // CARRIER_ID = getIntent().getStringExtra("provider_id");
        TYPE = getIntent().getStringExtra("type");
        //getOperator();
        getMobileDetails();
        tvMobile.setText(MOBILE);
        // tvMobileCarrier.setText(CARRIER_INFO);
        // MyUtil.setProviderImage(URL, imgProvider, CARRIER_INFO, this, "mobile");
    }

    private void getMobileDetails() {
        API_CODE = 0;
        networkCallUsingVolleyApi(Constants.URL.GET_OPERATOR, true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.whiteStatusBar(this);
        setContentView(R.layout.mobile_amount_enter_activity);
        init();

        tvPlans.setOnClickListener(v -> {
            Intent i = new Intent((Context) MobileRechargeAmountInput.this, ViewPlans.class);
            /*i.putExtra("provider_id", CARRIER_ID);
            i.putExtra("provider_name", CARRIER_INFO);
            i.putExtra("mobile", MOBILE);
            i.putExtra("type", "mobile");
            i.putExtra("circle", circle);*/
            i.putExtra("provider_id", PROVIDER_ID);
            i.putExtra("user_id", SharedPrefs.getValue(getApplicationContext(), SharedPrefs.USER_ID));
            i.putExtra("providername", PROVIDER_NAME);
            i.putExtra("apptoken", SharedPrefs.getValue(getApplicationContext(), SharedPrefs.APP_TOKEN));
            i.putExtra("mobile", MOBILE);
            i.putExtra("circle", circle);
            i.putExtra("type", "mobile");

            startActivityForResult(i, 121);
        });

        btnProceed.setOnClickListener(v -> {
            EditText[] data = {etAmount, etTPin};
            if (AppHandler.editTextRequiredValidation(data)) {
                showLoader(this);
            }
        });

        tvGenPin.setOnClickListener(v -> startActivity(new Intent((Context) this, OTPPinReset.class)));
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

        if (API_CODE == 0) {
            // map.put("user_id", "" + SharedPrefs.getValue(getApplicationContext(), SharedPrefs.USER_ID));
            // map.put("apptoken", "" + SharedPrefs.getValue(getApplicationContext(), SharedPrefs.APP_TOKEN));
            map.put("number", "" + MOBILE);
            map.put("type", "mobile");

        } else {
            map.put("provider_id", PROVIDER_ID);
            map.put("amount", etAmount.getText().toString());
            map.put("number", MOBILE);
            map.put("pin", etTPin.getText().toString());
        }

        return map;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case 121:
                    if (data != null) {
                        String amount = data.getStringExtra("amount");
                        etAmount.setText(amount);
                        etAmount.setSelection(amount.length());
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        if (API_CODE == 0) {
            Log.d("Response URL:", "" + Constants.URL.GET_OPERATOR);
            Log.d("Response :", "" + JSonResponse);
            try {
                JSONObject jsonObject = new JSONObject(JSonResponse);

                if (jsonObject.has("status")) {
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("success")) {

                        JSONObject object =jsonObject.getJSONObject("data");
                        circle = jsonObject.getString("circle");
                        PROVIDER_NAME = jsonObject.getString("providername");
                        Log.d("Response :",PROVIDER_NAME+","+circle+","+ object.getString("id"));

                        if (object.has("id")) {
                            PROVIDER_ID = object.getString("id");
                        }
                        if (object.has("url")) {
                            URL = object.getString("url");
                        }
                        tvMobileCarrier.setText(PROVIDER_NAME + "," + circle);
                        MyUtil.setProviderImage(URL, imgProvider, PROVIDER_NAME + "," + circle, getApplicationContext(), "mobile");
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                JSONObject jsonObject = new JSONObject(JSonResponse);
                String message = jsonObject.getString("message");
                String txnId = jsonObject.getString("txnid");
                String rrn = jsonObject.getString("rrn");
                Constants.INVOICE_DATA = new ArrayList<>();
                Constants.INVOICE_DATA.add(new InvoiceModel("Txn Id", txnId));
                Constants.INVOICE_DATA.add(new InvoiceModel("RRN No", rrn));
                Constants.INVOICE_DATA.add(new InvoiceModel("Mobile Number", MOBILE));
                Constants.INVOICE_DATA.add(new InvoiceModel("Date", Constants.SHOWING_DATE_FORMAT.format(new Date())));
                Constants.INVOICE_DATA.add(new InvoiceModel("Provider ID", PROVIDER_ID));
                Constants.INVOICE_DATA.add(new InvoiceModel("Provider Name", PROVIDER_NAME));
//            Constants.INVOICE_DATA.add(new InvoiceModel("Trans Type", "Bill Payment"));
                Constants.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(this, etAmount.getText().toString())));
                Constants.INVOICE_DATA.add(new InvoiceModel("Status", "success"));
                Intent i = new Intent((Context) this, ReportInvoice.class);
                i.putExtra("status", "success");
                i.putExtra("remark", "" + message);
                startActivity(i);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Print.P(msg);
    }

    @SuppressLint("SetTextI18n")
    private void showLoader(Activity context) {
        dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pay_confirmation_dialog);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        ImageView imgProvider = dialog.findViewById(R.id.imgProvider);
        MyUtil.setProviderImage(URL, imgProvider, PROVIDER_NAME, this, "mobile");
        TextView tvMobile = dialog.findViewById(R.id.tvMobile);
        tvMobile.setText(MOBILE);
        TextView tvOperator = dialog.findViewById(R.id.tvOperator);
        tvOperator.setText(PROVIDER_NAME);
        ImageView imgEdit = dialog.findViewById(R.id.imgEdit);
        Button cancelBtn = dialog.findViewById(R.id.btnCancel);
        cancelBtn.setOnClickListener(v -> closeLoader());
        imgEdit.setOnClickListener(v -> {
            closeLoader();
            finish();
        });
        Button confirmButton = dialog.findViewById(R.id.btnConfirm);
        confirmButton.setOnClickListener(v -> {
            closeLoader();
            API_CODE = 1;
            networkCallUsingVolleyApi(Constants.URL.MOBILE_RECHARGE_PAY, true);
        });

        dialog.show();
    }

    private void closeLoader() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void getOperator() {
        mRequestQueue = Volley.newRequestQueue(this);
        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.POST, Constants.URL.GET_OPERATOR, new Response.Listener<String>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(String response) {

                Log.d("Response URL:", "" + Constants.URL.GET_OPERATOR);
                Log.d("Response :", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has("status")) {
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("success")) {
                            circle = jsonObject.getString("circle");
                            PROVIDER_NAME = jsonObject.getString("providername");
                            if (jsonObject.has("id")) {
                                PROVIDER_ID = jsonObject.getString("id");
                            }
                            if (jsonObject.has("url")) {
                                URL = jsonObject.getString("url");
                            }
                            tvMobileCarrier.setText(PROVIDER_NAME + "," + circle);
                            MyUtil.setProviderImage(URL, imgProvider, PROVIDER_NAME + "," + circle, getApplicationContext(), "mobile");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error :" + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", "" + SharedPrefs.getValue(getApplicationContext(), SharedPrefs.USER_ID));
                params.put("apptoken", "" + SharedPrefs.getValue(getApplicationContext(), SharedPrefs.APP_TOKEN));
                params.put("number", "" + MOBILE);
                params.put("type", "mobile");

                Log.d("Response PARAMS:", "" + params.toString());
                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
    }

}
