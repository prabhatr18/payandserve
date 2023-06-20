package com.digital.payandserve.views.dmt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.SharedPrefs;
import com.digital.payandserve.views.invoice.model.InvoiceModel;
import com.digital.payandserve.views.otpview.OTPPinReset;
import com.payment.aeps.network.VolleyGetNetworkCall;
import com.payment.aeps.util.Print;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PDMTTransaction extends AppCompatActivity implements RequestResponseLis {
    private static final String[] paths = {"Select mode...", "IMPS", "NEFT"};
    private static final String[] bank = {"Select bank...", "bank1", "bank2", "bank3"};
    Dialog dialog;
    Spinner etSelectMode;
    String mode = "";
    Spinner spinnerSelectBank;
    private String SENDER_NAME, SENDER_NUMBER, beneid, bankname, account, ifsc, name, bankid;
    private TextView tvDetails, tvGenPin, mTvWalletBalance;
    private EditText etAmount, etTPin;
    //    private BenModel model;
    private Button btnProceed;
    private ArrayList<LocalInvoiceModel> dataList;
    private String apiUrl = "", bankName = "";

    private void init() {

        SENDER_NAME = getIntent().getStringExtra("sender_name");
        SENDER_NUMBER = getIntent().getStringExtra("sender_number");
        beneid = getIntent().getStringExtra("beneid");
        bankname = getIntent().getStringExtra("bankname");
        account = getIntent().getStringExtra("account");
        ifsc = getIntent().getStringExtra("ifsc");
        bankid = getIntent().getStringExtra("bankid");
        name = getIntent().getStringExtra("name");
        tvDetails = findViewById(R.id.tvDetail);
        etTPin = findViewById(R.id.etTPin);
        tvGenPin = findViewById(R.id.tvGenPin);
        String details = "Sender Name : " + SENDER_NAME;
        details += "\nSender Number : " + SENDER_NUMBER;
        details += "\nBeneficiary Name : " + name;
        details += "\nAccount : " + account;
        if (MyUtil.isNN(bankname)) {
            details += "\nIFSC Code : " + ifsc;
            details += "\nBank Name : " + bankname;
        }
        tvDetails.setText(details);
        etAmount = findViewById(R.id.etAmount);
        etSelectMode = findViewById(R.id.etSelectMode);
        btnProceed = findViewById(R.id.btnProceed);
        apiUrl = getIntent().getStringExtra("apiUrl");
        TextView tvTitle = findViewById(R.id.toolbarTitle);
        if (apiUrl.contains("payoutdmt")) {
            tvTitle.setText("PG-Payout");
        }
        dataList = new ArrayList<>();

        mTvWalletBalance = findViewById(R.id.mTvWalletBalance);
        spinnerSelectBank = findViewById(R.id.spinnerSelectBank);
        // String walletBalance = SharedPrefs.getValue(this, SharedPrefs.PG_BALANCE);
        String walletBalance = SharedPrefs.getValue(this, SharedPrefs.MAIN_WALLET);
        mTvWalletBalance.setText("₹" + "" + walletBalance);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdmttransaction);

        init();

        btnProceed.setOnClickListener(v -> {
            if (isValid()) {
                showLoader(PDMTTransaction.this,
                        "Please confirm Bank, Account Number and amount, transfer will not reverse at any circumstances.");
            }
        });
        tvGenPin.setOnClickListener(v -> startActivity(new Intent(this, OTPPinReset.class)));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PDMTTransaction.this,
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etSelectMode.setAdapter(adapter);

        etSelectMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        // Toast.makeText(PDMTTransaction.this, "Select mode", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        mode = (String) parent.getItemAtPosition(position);
                        break;
                    case 2:
                        mode = (String) parent.getItemAtPosition(2);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapterBank = new ArrayAdapter<String>(PDMTTransaction.this,
                android.R.layout.simple_spinner_item, bank);

        adapterBank.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectBank.setAdapter(adapterBank);

        spinnerSelectBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        // Toast.makeText(PDMTTransaction.this, "Select mode", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        bankName = (String) parent.getItemAtPosition(position);
                        break;
                    case 2:
                        bankName = (String) parent.getItemAtPosition(2);
                        break;

                    case 3:
                        bankName = (String) parent.getItemAtPosition(3);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private boolean isValid() {
        if (etAmount.getText().toString().equals("")) {
            Toast.makeText(this, "Amount field is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mode.equals("") || mode.equalsIgnoreCase("Select mode...")) {
            Toast.makeText(this, "mode field is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (bankName.equals("") || bankName.equalsIgnoreCase("Select bank...")) {
            Toast.makeText(this, "Bank is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etTPin.getText().toString().equals("")) {
            Toast.makeText(this, "Pin field is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        /*try {
            if (Double.parseDouble(etAmount.getText().toString()) < 10 ||
                    Double.parseDouble(etAmount.getText().toString()) > 25000) {
                Toast.makeText(this, "Amount should be between 100 to 25000 ", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
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
        map.put("name", name);
        map.put("txntype", "");
        map.put("benename", name);
        map.put("beneid", beneid);
        map.put("pipe", bankName);
        map.put("amount", etAmount.getText().toString());
        map.put("pin", etTPin.getText().toString());
        if (MyUtil.isNN(bankid)) map.put("benebank", bankid);
        if (MyUtil.isNN(bankname)) map.put("benebankName", bankname);
        if (MyUtil.isNN(ifsc)) map.put("beneifsc", ifsc);
        map.put("benemobile", SENDER_NUMBER);
        map.put("mode", mode);
        if (MyUtil.isNN(account)) map.put("beneaccount", account);

        return map;
    }

    @SuppressLint("SetTextI18n")
    private void showLoader(Activity context, String msg) {
        dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dmt_confirmation_dialog);
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
        tvMobile.setText(account);
        TextView tvOperator = dialog.findViewById(R.id.tvOperator);
        tvOperator.setText(name + "\n" + bankname + "\n" + MyUtil.formatWithRupee(this, etAmount.getText().toString()));
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
            networkCallUsingVolleyApi(apiUrl, true);
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
            String rootStatus = AppHandler.getStatus(JSonResponse);
            String rootMessage = "";
            if (jsonObject.has("message")) {
                rootMessage = AppHandler.getMessage(JSonResponse);
            }
            dataList.clear();

            if (jsonObject.has("data")) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    String orderId = "NA", message = "NA", txnStatus = "NA", amount = "NA", utr = "NA";
                    JSONObject invoiceJson = jsonArray.getJSONObject(i);
                    if (invoiceJson.has("amount")) amount = invoiceJson.getString("amount");
                    if (invoiceJson.has("data")) invoiceJson = invoiceJson.getJSONObject("data");

                    if (invoiceJson.has("orderId")) orderId = invoiceJson.getString("orderId");
                    if (invoiceJson.has("payid")) orderId = invoiceJson.getString("payid");
                    if (invoiceJson.has("message")) message = invoiceJson.getString("message");
                    if (!AppHandler.checkStatus(invoiceJson.toString(), this) && rootMessage.length() == 0) {
                        rootMessage = invoiceJson.getString("message");
                    }
                    if (invoiceJson.has("rrn")) utr = invoiceJson.getString("rrn");
                    txnStatus = AppHandler.getStatus(invoiceJson.toString());
                    dataList.add(new LocalInvoiceModel(message, txnStatus, amount, orderId, utr));
                }
            }
            ArrayList<InvoiceModel> benDetails = new ArrayList<>();
            benDetails.add(new InvoiceModel("Name", name));
//            benDetails.add(new InvoiceModel("Phone Number", ));

            ArrayList<InvoiceModel> bankDetails = new ArrayList<>();
            if (MyUtil.isNN(bankname))
                bankDetails.add(new InvoiceModel("Bank", bankname));
            if (MyUtil.isNN(ifsc))
                bankDetails.add(new InvoiceModel("IFSC Code", ifsc));
            bankDetails.add(new InvoiceModel("Account No", account));
            bankDetails.add(new InvoiceModel("Txn Date", Constants.COMMON_DATE_FORMAT.format(new Date())));

            Intent i = new Intent(this, CompactReportInvoice.class);
            i.putExtra("remark", "" + rootMessage);
            i.putExtra("status", rootStatus);
            i.putExtra("remitter", "" + SENDER_NAME + "\n" + SENDER_NUMBER);
            i.putParcelableArrayListExtra("invoiceData", dataList);
            i.putParcelableArrayListExtra("benData", benDetails);
            i.putParcelableArrayListExtra("accountData", bankDetails);
            i.putExtra("amount", etAmount.getText().toString());
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