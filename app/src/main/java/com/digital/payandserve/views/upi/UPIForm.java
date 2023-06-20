package com.digital.payandserve.views.upi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.digital.payandserve.R;
import com.google.android.material.textfield.TextInputLayout;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.views.reports.UpiTransactionReport;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UPIForm extends AppCompatActivity implements RequestResponseLis {
    private TextView serviceType;
    private EditText etName, etMobile, etUpi, etAmount;
    private TextInputLayout upiLayout;
    private String TYPE;
    private Button btnProceed;
    private ImageView imgAllReport;

    private void init() {
        etName = findViewById(R.id.etName);
        imgAllReport = findViewById(R.id.imgAllReport);
        serviceType = findViewById(R.id.serviceType);
        etMobile = findViewById(R.id.etMobile);
        etAmount = findViewById(R.id.etAmount);
        etUpi = findViewById(R.id.etUpi);
        upiLayout = findViewById(R.id.upiLayout);
        btnProceed = findViewById(R.id.btnProceed);
        TYPE = getIntent().getStringExtra("type");
        if (TYPE.equalsIgnoreCase("SR")) {
            upiLayout.setVisibility(View.VISIBLE);
            serviceType.setText("Type : Send Request");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upi_form);
        init();

        btnProceed.setOnClickListener(v -> {
            if (isValid()) {
                networkCallUsingVolleyApi(Constants.URL.UPI_CASH, true);
            }
        });
        imgAllReport.setOnClickListener(v -> startActivity(new Intent(this, UpiTransactionReport.class)));
    }

    private boolean isValid() {
        if (etName.getText().toString().equals("")) {
            Toast.makeText(this, "Name field is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etMobile.getText().toString().length() != 10) {
            Toast.makeText(this, "Please input valid mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TYPE.equalsIgnoreCase("SR") && etUpi.getText().toString().equals("")) {
            Toast.makeText(this, "Please input upi id", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etAmount.getText().toString().equals("")) {
            Toast.makeText(this, "Amount field is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            if (Double.parseDouble(etAmount.getText().toString()) < 1 ||
                    Double.parseDouble(etAmount.getText().toString()) > 2000) {
                Toast.makeText(this, "Amount value should be grater than 1 and less than 2000", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("transactionType", TYPE);
        map.put("mobileNumber", etMobile.getText().toString());
        map.put("username", etName.getText().toString());
        map.put("amount", etAmount.getText().toString());
        if (TYPE.equalsIgnoreCase("SR")) {
            map.put("upiID", etUpi.getText().toString());
        }
        return map;
    }

    private void networkCallUsingVolleyApi(String url, boolean isLoad) {
        if (AppManager.isOnline(this)) {
            new VolleyNetworkCall(this, this, url, 1, param(), isLoad).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(JSonResponse);
            String qrUrl = jsonObject.getString("rrn");
            String txnId = jsonObject.getString("txnid");
            String name = etName.getText().toString();
            String mobile = etMobile.getText().toString();
            String amount = etAmount.getText().toString();
            Intent i = new Intent(UPIForm.this, ShowQR.class);
            i.putExtra("rrn", qrUrl);
            i.putExtra("txnId", txnId);
            i.putExtra("name", name);
            i.putExtra("mobile", mobile);
            i.putExtra("amount", amount);
            i.putExtra("type", TYPE);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Print.P(msg);
    }


}
