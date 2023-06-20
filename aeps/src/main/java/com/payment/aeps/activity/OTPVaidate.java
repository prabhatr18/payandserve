package com.payment.aeps.activity;

import static com.payment.aeps.app.ModuleConstants.AEPS_TYPE;
import static com.payment.aeps.app.ModuleConstants.PAYSPRINT_AEPS;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.payment.aeps.R;
import com.payment.aeps.app.ModuleConstants;
import com.payment.aeps.moduleprinter.ModuleInvoice;
import com.payment.aeps.network.VolleyGetNetworkCall;
import com.payment.aeps.util.ModuleUtil;
import com.payment.aeps.util.Print;

import org.json.JSONObject;

import java.util.Map;

import in.aabhasjindal.otptextview.OtpTextView;

public class OTPVaidate extends AppCompatActivity implements VolleyGetNetworkCall.RequestResponseLis {
    private String url;
    private Context context;
    private OtpTextView otpTextView;
    private TextView title;
    private Button btnProceed;
    private String type = "otpvalidate";
    private BottomSheetBehavior sheetBehavior;
    private int REQUEST_TYPE = 0;

    private void init() {
        context = this;
        setupToolBar();
        otpTextView = findViewById(R.id.otp_view);
        btnProceed = findViewById(R.id.btnProceed);
        title = findViewById(R.id.title);
        String mobile = "Please type the verification code send to " + getIntent().getStringExtra("mobile");
        title.setText(mobile);

        RelativeLayout bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
    }

    private void setupToolBar() {
        TextView tv = findViewById(R.id.tvToolBarTitle);
        tv.setText("OTP Verification");
        ImageView backImage = findViewById(R.id.imgBack);
        backImage.setOnClickListener(view -> finish());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_validation);

        init();

        btnProceed.setOnClickListener(v -> {
            REQUEST_TYPE = 0;
            String url = ModuleConstants.CASH_DEPO_API;
            if (AEPS_TYPE.equalsIgnoreCase(PAYSPRINT_AEPS)) {
                url = ModuleConstants.baseUrl + "raeps/cdo/transaction";
            }
            networkCallUsingVolleyApi(url);
        });

        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                TextView tvErrorLbl = view.findViewById(R.id.tvErrorLbl);
                TextView tvErrorDetail = view.findViewById(R.id.tvErrorDetail);
                ImageView img = view.findViewById(R.id.img);
                Button btnGoBack = view.findViewById(R.id.btnGoBack);
                ImageView btnCancel = view.findViewById(R.id.btnCancel);

                if (status.equalsIgnoreCase("TXN")) {
                    btnGoBack.setText("Proceed");
                    img.setImageDrawable(OTPVaidate.this.getResources().getDrawable(R.drawable.ic_profile));
                } else {
                    img.setImageDrawable(OTPVaidate.this.getResources().getDrawable(R.drawable.ic_complain));
                    btnGoBack.setText("Go Back");
                }

                String errLbl = "Status : " + status;
                tvErrorLbl.setText(errLbl);
                tvErrorDetail.setText(msgString);

                btnGoBack.setOnClickListener((View.OnClickListener) v -> {
                    if (status.equalsIgnoreCase("TXN")) {
                        REQUEST_TYPE = 1;
                        type = "cashdeposit";
                        networkCallUsingVolleyApi(ModuleConstants.CASH_DEPO_API);
                    } else {
                        findViewById(R.id.disableCon).setVisibility(View.GONE);
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        finish();
                    }
                });

                btnCancel.setOnClickListener(v -> {
                    findViewById(R.id.disableCon).setVisibility(View.GONE);
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    finish();
                });
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    findViewById(R.id.disableCon).setVisibility(View.GONE);
                }
            }
        });

    }

    private void networkCallUsingVolleyApi(String url) {
        if (ModuleUtil.isOnline(this)) {
            new VolleyGetNetworkCall(this, this, url, 1, param()).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {
        ModuleConstants.LOCAL_Map.put("transactionType", type);
        ModuleConstants.LOCAL_Map.put("otp", otpTextView.getOTP());
        ModuleConstants.LOCAL_Map.put("txnid", getIntent().getStringExtra("txnId"));
        if (txnId != null && txnId.length() > 0)
            ModuleConstants.LOCAL_Map.put("txnid", txnId);

        Print.P("PARAM : \n " + new JSONObject(ModuleConstants.LOCAL_Map).toString());
        Print.appendLog("------- OTP validation screen ----------");
        Print.appendLog(new JSONObject(ModuleConstants.LOCAL_Map).toString());
        return ModuleConstants.LOCAL_Map;
    }

    String status = "Not Available";
    String msgString = "Something went wrong";
    String txnId = "";

    @Override
    public void onSuccessRequest(String result) {
        Print.P("OTP Response : " + result);
        Print.appendLog("------- Api Response ----------");
        Print.appendLog(result);
        if (!result.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("TXN")) {
                    if (REQUEST_TYPE == 0) {
                        msgString = jsonObject.getString("message");
                        String benename = jsonObject.getString("benename");
                        String account = jsonObject.getString("account");
                        String amount = jsonObject.getString("amount");
                        txnId = jsonObject.getString("txnid");
                        msgString += "\n" + "Account No : " + account;
                        msgString += "\n" + "Ben. Name : " + benename;
                        msgString += "\n" + "Amount : " + amount;
                        msgString += "\n\n" + "Please confirm account details then proceed";
                        handleBottomSheet();
                    } else {
                        Intent i = new Intent(this, ModuleInvoice.class);
                        i.putExtra("invoice", result);
                        i.putExtra("type", "CD");
                        startActivity(i);
                        finish();
                    }
                } else {
                    handleBottomSheet();
                    if (jsonObject.has("message")) {
                        msgString = jsonObject.getString("message");
                        Toast.makeText(context, msgString, Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                handleBottomSheet();
            }
        } else {
            msgString = "Getting blank response";
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            handleBottomSheet();
        }

    }

    @Override
    public void onFailRequest(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private void handleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            findViewById(R.id.disableCon).setVisibility(View.VISIBLE);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            findViewById(R.id.disableCon).setVisibility(View.GONE);
        }
    }
}
