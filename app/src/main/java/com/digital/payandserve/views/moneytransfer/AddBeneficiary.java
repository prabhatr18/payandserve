package com.digital.payandserve.views.moneytransfer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.views.operator.OperatorList;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddBeneficiary extends AppCompatActivity implements RequestResponseLis {
    private EditText etBankName, etBenMobile, etHolderName, etIFSC, etAccountNumber;
    private Context context;
    private Button btnProceed, btnValidate;
    private String BANK_ID, BANK_IFSC, SENDER_NUMBER, SENDER_NAME;
    private int REQUEST_TYPE = 0;
    private void init() {
        context = this;
        etBankName = findViewById(R.id.etBankName);
        etBenMobile = findViewById(R.id.etBenMobile);
        etHolderName = findViewById(R.id.etHolderName);
        etIFSC = findViewById(R.id.etIFSC);
        btnValidate = findViewById(R.id.btnValidate);
        etAccountNumber = findViewById(R.id.etAccountNumber);
        btnProceed = findViewById(R.id.btnProceed);
        SENDER_NAME = getIntent().getStringExtra("sender_name");
        SENDER_NUMBER = getIntent().getStringExtra("sender_number");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dmt_add_ben);
        init();
        btnProceed.setOnClickListener(v -> {
            if (isValid()) {
                REQUEST_TYPE = 0;
                networkCallUsingVolleyApi(Constants.URL.DMT_TRANSACTION, true);
            }
        });

        btnValidate.setOnClickListener(v -> {
            if (isValid()) {
                REQUEST_TYPE = 1;
                networkCallUsingVolleyApi(Constants.URL.DMT_TRANSACTION, true);
            }
        });
        etBankName.setOnClickListener(v -> {
            Intent i = new Intent(AddBeneficiary.this, OperatorList.class);
            i.putExtra("type", "getbank");
            startActivityForResult(i, 121);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 121) {
                if (data != null) {
                    String bankName = data.getStringExtra("name");
                    BANK_ID = data.getStringExtra("id");
                    BANK_IFSC = data.getStringExtra("ifsc");
                    etBankName.setText(bankName);
                    if (MyUtil.isNN(BANK_IFSC))
                        etIFSC.setText(BANK_IFSC);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValid() {
        if (BANK_ID == null || BANK_ID.equals("")) {
            Toast.makeText(this, "Please Select Bank", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etAccountNumber.getText().toString().equals("")) {
            Toast.makeText(this, "Enter bank account number..", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etHolderName.getText().toString().equals("")) {
            Toast.makeText(this, "Enter beneficiary name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etBenMobile.getText().toString().equals("")) {
            Toast.makeText(this, "Enter beneficiary mobile", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etIFSC.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter ifsc code", Toast.LENGTH_SHORT).show();
            return false;
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
        if (REQUEST_TYPE == 0){
            map.put("type", "addbeneficiary");
        }else {
            map.put("type", "accountverification");
        }

        map.put("mobile", SENDER_NUMBER);
        map.put("name", SENDER_NAME);
        map.put("benemobile", etBenMobile.getText().toString());
        map.put("benebank", BANK_ID);
        map.put("beneifsc", etIFSC.getText().toString());
        map.put("beneaccount", etAccountNumber.getText().toString());
        map.put("benename", etHolderName.getText().toString());
        return map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        try {
            if (REQUEST_TYPE == 1) {
                JSONObject jsonObject = new JSONObject(JSonResponse);
                if (jsonObject.has("benename")) {
                    etHolderName.setText(jsonObject.getString("benename"));
                }
            } else {
                Constants.IS_RELOAD_REQUEST = true;
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Print.P(msg);
    }
}
