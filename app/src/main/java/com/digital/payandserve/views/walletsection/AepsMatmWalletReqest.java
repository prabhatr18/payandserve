package com.digital.payandserve.views.walletsection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.ExtensionFunction;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.views.invoice.ReportInvoice;
import com.digital.payandserve.views.invoice.model.InvoiceModel;
import com.digital.payandserve.views.otpview.OTPPinReset;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AepsMatmWalletReqest extends AppCompatActivity implements RequestResponseLis {
    private EditText etMode, etAmount, etTPin;
    private TextView toolbarTitle, tvGenPin, etSelectAccount, tvBankDetail;
    private RadioButton rbWallet, rbBank, rbKuber;
    private Context context;
    private String MODE = "";
    private String FLAG = "";
    private String ACTIVITY_TYPE;
    private Button btnProceed;
    private String selectedIfsc = "", selectedBank = "", selectedAccount = "";
    private String bankType = "";

    private void init() {
        context = this;
        ACTIVITY_TYPE = getIntent().getStringExtra("activity_type");
        rbWallet = findViewById(R.id.rbWallet);
        rbKuber = findViewById(R.id.rbKuber);
        tvGenPin = findViewById(R.id.tvGenPin);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        etTPin = findViewById(R.id.etTPin);
        rbBank = findViewById(R.id.rbBank);
        etMode = findViewById(R.id.etMode);
        etAmount = findViewById(R.id.etAmount);
        btnProceed = findViewById(R.id.btnProceed);
        etSelectAccount = findViewById(R.id.etSelectAccount);
        tvBankDetail = findViewById(R.id.tvBankDetail);

        switch (ACTIVITY_TYPE) {
            case "aeps":
                toolbarTitle.setText(R.string.aeps_fun_request);
                findViewById(R.id.rbBank).setVisibility(View.VISIBLE);
                findViewById(R.id.rbKuber).setVisibility(View.VISIBLE);
                FLAG = "wallet";
                break;
            case "kuber":
                toolbarTitle.setText("Kuber Balance settlement");
                FLAG = "kuberwallet";
                findViewById(R.id.rbBank).setVisibility(View.GONE);
                findViewById(R.id.rbKuber).setVisibility(View.GONE);
                break;
            case "matm":
                toolbarTitle.setText(R.string.matm_fund_request);
                findViewById(R.id.rbBank).setVisibility(View.VISIBLE);
                findViewById(R.id.rbKuber).setVisibility(View.VISIBLE);
                FLAG = "matmwallet";
                break;
        }

        visibilityController();
        rbBank.setOnClickListener(v -> {
            if (ACTIVITY_TYPE.equalsIgnoreCase("aeps"))
                FLAG = "bank";
            else if (ACTIVITY_TYPE.equalsIgnoreCase("kuber"))
                FLAG = "kuberbank";
            else
                FLAG = "matmbank";
            visibilityController();
        });

        rbWallet.setOnClickListener(v -> {
            bankType = "";
            if (ACTIVITY_TYPE.equalsIgnoreCase("aeps"))
                FLAG = "wallet";
            else if (ACTIVITY_TYPE.equalsIgnoreCase("kuber"))
                FLAG = "kuberwallet";
            else
                FLAG = "matmwallet";
            visibilityController();
        });

        rbKuber.setOnClickListener(v -> {
            bankType = "";
            if (ACTIVITY_TYPE.equalsIgnoreCase("aeps"))
                FLAG = "aepskuber";
            else
                FLAG = "matmkuber";
            visibilityController();
        });

        etMode.setOnClickListener(v -> modePopup());

        etSelectAccount.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChangeUpdateAccountActivity.class);
            startActivityForResult(intent, 5000);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 5000) {
                ExtensionFunction.showToast(this, data.getStringExtra("account"));
                String type = data.getStringExtra("type");
                Print.P("Type : " + type);
                bankType = type;
                selectedBank = data.getStringExtra("bankName");
                selectedAccount = data.getStringExtra("account");
                selectedIfsc = data.getStringExtra("ifsc");
                tvBankDetail.setText("Type : " + type + "\n" + "Bank Name : " + selectedBank + "\n" +
                        "Account No : " + selectedAccount + "\n" + "IFSC : " + selectedIfsc);
                etSelectAccount.setText(selectedBank);
                tvBankDetail.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Print.P(e.toString());
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aeps_matm_fund_request);
        init();
        btnProceed.setOnClickListener(v -> {
            if (isValid()) {
                networkCallUsingVolleyApi(Constants.URL.FUND_REQUEST, true);
            }
        });

        tvGenPin.setOnClickListener(v -> startActivity(new Intent(this, OTPPinReset.class)));

    }

    private void modePopup() {
        final CharSequence[] choice = {"NEFT", "IMPS", "MANUAL"};
        AlertDialog.Builder alert = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog);
        alert.setTitle("Please select payment mode");
        alert.setSingleChoiceItems(choice, -1, (dialog, which) -> {
            String txt = choice[which].toString();
            MODE = txt;
            etMode.setText(txt);
        });
        alert.setNegativeButton("OK", (dialog, which) -> dialog.dismiss());
        alert.show();
    }


    private boolean isValid() {
        if (FLAG == null || FLAG.length() == 0) {
            Toast.makeText(context, "Please select bank or wallet", Toast.LENGTH_SHORT).show();
            return false;
        } else if (FLAG.contains("bank") && selectedBank.equals("")) {
            Toast.makeText(this, "Please enter bank name were amount deposited", Toast.LENGTH_SHORT).show();
            return false;
        } else if (FLAG.contains("bank") && selectedAccount.equals("")) {
            Toast.makeText(this, "Please enter account number in which amount deposited", Toast.LENGTH_SHORT).show();
            return false;
        } else if (FLAG.contains("bank") && selectedIfsc.equals("")) {
            Toast.makeText(this, "Please enter bank IFSC", Toast.LENGTH_SHORT).show();
            return false;
        } else if (FLAG.contains("bank") && !MyUtil.isNN(MODE)) {
            Toast.makeText(this, "Mode Selection is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etAmount.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter request amount", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etTPin.getText().toString().equals("")) {
            Toast.makeText(this, "TPin is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            if (Double.parseDouble(etAmount.getText().toString()) < 10) {
                Toast.makeText(this, "Amount value should be grater than 10 ", Toast.LENGTH_SHORT).show();
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
        map.put("type", FLAG);
        map.put("amount", etAmount.getText().toString());
        map.put("pin", etTPin.getText().toString());
        if (MyUtil.isNN(bankType))
            map.put("accountType", bankType.replace(" ", "").toLowerCase());
        if (MyUtil.isNN(selectedAccount))
            map.put("account", selectedAccount);
        if (MyUtil.isNN(selectedBank))
            map.put("bank", selectedBank);
        if (MyUtil.isNN(selectedIfsc))
            map.put("ifsc", selectedIfsc);
        if (MyUtil.isNN(MODE))
            map.put("mode", MODE);
        return map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        try {
            String msg;
            JSONObject jsonObject = new JSONObject(JSonResponse);
            if (jsonObject.has("message")) {
                msg = jsonObject.getString("message");
            } else {
                msg = "Something went wrong, try again";
            }
            String txnid = jsonObject.getString("txnid");
            Constants.INVOICE_DATA = new ArrayList<>();
            Constants.INVOICE_DATA.add(new InvoiceModel("Txn Id", txnid));
            if (ACTIVITY_TYPE.equalsIgnoreCase("aeps"))
                Constants.INVOICE_DATA.add(new InvoiceModel("Trans Type", "Aeps fund Request"));
            else if (ACTIVITY_TYPE.equalsIgnoreCase("kuber"))
                Constants.INVOICE_DATA.add(new InvoiceModel("Trans Type", "Kuber fund Request"));
            else
                Constants.INVOICE_DATA.add(new InvoiceModel("Trans Type", "Matm fund Request"));
            if (FLAG.contains("bank")) {
                Constants.INVOICE_DATA.add(new InvoiceModel("Bank", selectedBank));
                Constants.INVOICE_DATA.add(new InvoiceModel("Account Number", selectedAccount));
            }
            Constants.INVOICE_DATA.add(new InvoiceModel("Date", Constants.COMMON_DATE_FORMAT.format(new Date())));
            Constants.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(this, etAmount.getText().toString())));
            Constants.INVOICE_DATA.add(new InvoiceModel("Status", "success"));
            Intent i = new Intent(this, ReportInvoice.class);
            i.putExtra("status", "success");
            i.putExtra("remark", "" + msg);
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

    private void visibilityController() {
        if (FLAG.contains("bank")) {
            findViewById(R.id.secBank).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.secBank).setVisibility(View.GONE);
        }
    }
}
