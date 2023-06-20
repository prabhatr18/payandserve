package com.digital.payandserve.views.moneytransfer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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

import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.views.invoice.ReportInvoice;
import com.digital.payandserve.views.invoice.model.InvoiceModel;
import com.digital.payandserve.views.moneytransfer.model.BenModel;
import com.digital.payandserve.views.otpview.OTPPinReset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DMTTransaction extends AppCompatActivity implements RequestResponseLis {
    private String SENDER_NAME, SENDER_NUMBER;
    private TextView tvDetails, tvGenPin;
    private EditText etAmount, etTPin;
    private BenModel model;
    private Button btnProceed;

    private void init() {
        SENDER_NAME = getIntent().getStringExtra("sender_name");
        SENDER_NUMBER = getIntent().getStringExtra("sender_number");
        tvDetails = findViewById(R.id.tvDetail);
        etTPin = findViewById(R.id.etTPin);
        tvGenPin = findViewById(R.id.tvGenPin);
        String details = "Sender Name : " + SENDER_NAME;
        details += "\nSender Number : " + SENDER_NUMBER;
        model = getIntent().getParcelableExtra("data");
        details += "\nBeneficiary Name : " + model.getBenename();
        details += "\nAccount Number : " + model.getBeneaccount();
        details += "\nIFSC Code : " + model.getBeneifsc();
        details += "\nBank Name : " + model.getBenebank();
        tvDetails.setText(details);
        etAmount = findViewById(R.id.etAmount);
        btnProceed = findViewById(R.id.btnProceed);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dmt_money_transfer);
        init();
        btnProceed.setOnClickListener(v -> {
            if (isValid()) {
                showLoader(DMTTransaction.this,
                        "Please confirm Bank, Account Number and amount, transfer will not reverse at any circumstances.");
            }
        });
        tvGenPin.setOnClickListener(v -> startActivity(new Intent(this, OTPPinReset.class)));
    }

    private boolean isValid() {
        if (etAmount.getText().toString().equals("")) {
            Toast.makeText(this, "Amount field is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etTPin.getText().toString().equals("")) {
            Toast.makeText(this, "Pin field is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            if (Double.parseDouble(etAmount.getText().toString()) < 10 ||
                    Double.parseDouble(etAmount.getText().toString()) > 25000) {
                Toast.makeText(this, "Amount should be between 100 to 25000 ", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        map.put("type", "transfer");
        map.put("mobile", SENDER_NUMBER);
        map.put("name", SENDER_NAME);
        map.put("benebank", model.getBenebankid());
        map.put("benebankName", model.getBenebank());
        map.put("beneifsc", model.getBeneifsc());
        map.put("benemobile", model.getBenemobile());
        map.put("beneaccount", model.getBeneaccount());
        map.put("txntype", "");
        map.put("benename", model.getBenename());
        map.put("beneid", model.getBeneid());
        map.put("amount", etAmount.getText().toString());
        map.put("pin", etTPin.getText().toString());
        return map;
    }


    Dialog dialog;

    @SuppressLint("SetTextI18n")
    private void showLoader(Activity context, String msg) {
        dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pay_confirmation_dialog);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        ImageView imgProvider = dialog.findViewById(R.id.imgProvider);
        imgProvider.setImageDrawable(getResources().getDrawable(R.drawable.ic_bank_app));
        TextView tvMobile = dialog.findViewById(R.id.tvMobile);
        TextView lbl = dialog.findViewById(R.id.lbl);
        lbl.setText(msg);
        tvMobile.setText(model.getBeneaccount());
        TextView tvOperator = dialog.findViewById(R.id.tvOperator);
        tvOperator.setText(model.getBenebank() + "\n" + MyUtil.formatWithRupee(this, etAmount.getText().toString()));
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
            networkCallUsingVolleyApi(Constants.URL.DMT_TRANSACTION, true);
        });

        dialog.show();
    }

    private void closeLoader() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(JSonResponse);
            String status = AppHandler.getStatus(JSonResponse);
            StringBuilder message = new StringBuilder(AppHandler.getMessage(JSonResponse));
            String ref;
            if (jsonObject.has("data")) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if (jsonObject1.has("rrn")) {
                        ref = jsonObject1.getString("rrn");
                    } else {
                        ref = jsonObject1.getString("message");
                    }
                    if (jsonObject1.has("statuscode"))
                        status = jsonObject1.getString("statuscode");
                    message.append("Ref - ").append(ref).append(" (").append(jsonObject1.getString("message")).append(")");
                }
            }
            Constants.INVOICE_DATA = new ArrayList<>();
            Constants.INVOICE_DATA.add(new InvoiceModel("Sender Number", SENDER_NUMBER));
            Constants.INVOICE_DATA.add(new InvoiceModel("Sender Name", SENDER_NAME));
            Constants.INVOICE_DATA.add(new InvoiceModel("Beneficiary Id", model.getBeneid()));
            Constants.INVOICE_DATA.add(new InvoiceModel("Beneficiary Name", model.getBenename()));
            Constants.INVOICE_DATA.add(new InvoiceModel("Beneficiary Number", model.getBenemobile()));
            Constants.INVOICE_DATA.add(new InvoiceModel("Account No", model.getBeneaccount()));
            Constants.INVOICE_DATA.add(new InvoiceModel("Bank", model.getBenebank()));
            Constants.INVOICE_DATA.add(new InvoiceModel("IFSC Code", model.getBeneifsc()));
            Constants.INVOICE_DATA.add(new InvoiceModel("Date", Constants.COMMON_DATE_FORMAT.format(new Date())));
            Constants.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(this, etAmount.getText().toString())));
            Constants.INVOICE_DATA.add(new InvoiceModel("Status", status));
            Intent i = new Intent(this, ReportInvoice.class);
            i.putExtra("status", status);
            i.putExtra("remark", "" + message);
            startActivity(i);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Print.P(msg);
    }
}
