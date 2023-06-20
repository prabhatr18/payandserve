package com.digital.payandserve.views.billpayment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.views.browseplan.ViewPlans;
import com.digital.payandserve.views.invoice.ReportInvoice;
import com.digital.payandserve.views.invoice.model.InvoiceModel;
import com.digital.payandserve.views.otpview.OTPPinReset;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DTHAmountInput extends AppCompatActivity implements RequestResponseLis {
    public Context context;
    private TextView tvMobileCarrier;
    private TextView tvPlans, tvGenPin;
    private EditText etAmount, etCustomerId, etTPin;
    private AppCompatButton btnProceed;
    private ImageView icBack, imgProvider;
    private String CARRIER_INFO, CARRIER_ID, URL;

    private void init() {
        context = DTHAmountInput.this;
        etCustomerId = findViewById(R.id.etCustomerId);
        etTPin = findViewById(R.id.etTPin);
        tvGenPin = findViewById(R.id.tvGenPin);
        imgProvider = findViewById(R.id.imgProvider);
        tvMobileCarrier = findViewById(R.id.tvMobileCarrier);
        tvPlans = findViewById(R.id.tvPlans);
        etAmount = findViewById(R.id.etAmount);
        btnProceed = findViewById(R.id.btnProceed);
        icBack = findViewById(R.id.icBack);
        icBack.setOnClickListener(v -> finish());

        CARRIER_INFO = getIntent().getStringExtra("provider_name");
        URL = getIntent().getStringExtra("provider_image");
        CARRIER_ID = getIntent().getStringExtra("provider_id");

        tvMobileCarrier.setText(CARRIER_INFO);
        MyUtil.setProviderImage(URL, imgProvider, CARRIER_INFO, this, "dth");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.whiteStatusBar(this);
        setContentView(R.layout.dth_bill_pay);
        init();

        tvPlans.setOnClickListener(v -> {
            if (etCustomerId.getText().toString().length() > 0) {
                Intent i = new Intent(DTHAmountInput.this, ViewPlans.class);
                i.putExtra("provider_id", CARRIER_ID);
                i.putExtra("provider_name", CARRIER_INFO);
                i.putExtra("mobile", etCustomerId.getText().toString());
                i.putExtra("type", "dth");
                startActivityForResult(i, 121);
            } else {
                Toast.makeText(context, "Customer id is required", Toast.LENGTH_SHORT).show();
            }
        });

        btnProceed.setOnClickListener(v -> {
            EditText[] data = {etAmount, etTPin};
            if (AppHandler.editTextRequiredValidation(data)) {
                showLoader(this);
            }
        });

        tvGenPin.setOnClickListener(v -> startActivity(new Intent(this, OTPPinReset.class)));

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
        map.put("provider_id", CARRIER_ID);
        map.put("amount", etAmount.getText().toString());
        map.put("number", etCustomerId.getText().toString());
        map.put("pin", etTPin.getText().toString());
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
        try {
            JSONObject jsonObject = new JSONObject(JSonResponse);
            String message = jsonObject.getString("message");
            String txnId = jsonObject.getString("txnid");
            String rrn = jsonObject.getString("rrn");
            Constants.INVOICE_DATA = new ArrayList<>();
            Constants.INVOICE_DATA.add(new InvoiceModel("Txn Id", txnId));
            Constants.INVOICE_DATA.add(new InvoiceModel("Number", etCustomerId.getText().toString()));
            Constants.INVOICE_DATA.add(new InvoiceModel("RRN No", rrn));
            Constants.INVOICE_DATA.add(new InvoiceModel("Date", Constants.SHOWING_DATE_FORMAT.format(new Date())));
            Constants.INVOICE_DATA.add(new InvoiceModel("Provider ID", CARRIER_ID));
            Constants.INVOICE_DATA.add(new InvoiceModel("Provider Name", CARRIER_INFO));
//            Constants.INVOICE_DATA.add(new InvoiceModel("Trans Type", "Bill Payment"));
            Constants.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(this, etAmount.getText().toString())));
            Constants.INVOICE_DATA.add(new InvoiceModel("Status", "success"));
            Intent i = new Intent(this, ReportInvoice.class);
            i.putExtra("status", "success");
            i.putExtra("remark", "" + message);
            startActivity(i);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Print.P(msg);
    }

    Dialog dialog;

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
        MyUtil.setProviderImage(URL, imgProvider, CARRIER_INFO, this, "dth");
        TextView tvMobile = dialog.findViewById(R.id.tvMobile);
        tvMobile.setText(etCustomerId.getText().toString());
        TextView tvOperator = dialog.findViewById(R.id.tvOperator);
        tvOperator.setText(CARRIER_INFO);
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
            networkCallUsingVolleyApi(Constants.URL.MOBILE_RECHARGE_PAY, true);
        });
        dialog.show();
    }

    private void closeLoader() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
