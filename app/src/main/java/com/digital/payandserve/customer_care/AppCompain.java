package com.digital.payandserve.customer_care;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.ExtensionFunction;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.views.select_state_district.SearchWithListActivity;

import java.util.HashMap;
import java.util.Map;

public class AppCompain extends AppCompatActivity implements
        RequestResponseLis {
    private EditText etSubject, etDes;
    private String PRODUCT, TRAN_ID, STATUS;
    private Button btnProceed;
    private LinearLayout secMsg, secCall, secEmail;
    private String subName;

    private void init() {
        etSubject = findViewById(R.id.etSubject);
        etDes = findViewById(R.id.etDes);
        btnProceed = findViewById(R.id.btnProceed);
        PRODUCT = getIntent().getStringExtra("product");
        TRAN_ID = getIntent().getStringExtra("tranId");
        STATUS = getIntent().getStringExtra("status");
        supportSec();
    }

    private void supportSec() {
        if (MyUtil.isNN(TRAN_ID)) {
            findViewById(R.id.menuQuickLink).setVisibility(View.GONE);
        } else {
            findViewById(R.id.menuQuickLink).setVisibility(View.VISIBLE);
        }
        secMsg = findViewById(R.id.secMsg);
        secCall = findViewById(R.id.secCall);
        secEmail = findViewById(R.id.secEmail);
        secCall.setOnClickListener(v -> AppHandler.dialContactPhone("9504065205", this));
        secMsg.setOnClickListener(v -> AppHandler.openWhatsApp("9504065205", this));
        secEmail.setOnClickListener(v -> AppHandler.openGmail("info@payandserve.com", this));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.transparentStatusBar(this);
        setContentView(R.layout.activity_complain);
        init();

//        networkCallUsingVolleyApi(Constants.URL.COMPLAIN_SUBJECT, paramSupport());

        etSubject.setOnClickListener(v->{

            Intent intent = new Intent(this, SelectSubjectActivity.class);
            startActivityForResult(intent,110);
        });

        btnProceed.setOnClickListener(v -> {
            if (etSubject.getText().toString().length() > 0) {
                if (MyUtil.isNN(TRAN_ID)) {
                    networkCallUsingVolleyApi(Constants.URL.COMPLAIN, param());
                } else {
                    networkCallUsingVolleyApi(Constants.URL.CONTACT_SUPPORT, paramSupport());
                }
            } else {
                etSubject.setError("Please enter subject");
            }
        });
    }

    private void networkCallUsingVolleyApi(String url, Map<String, String> map) {
        if (AppManager.isOnline(this)) {
            new VolleyNetworkCall(this, this, url, 1, param(), true).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        if (MyUtil.isNN(PRODUCT))
            map.put("product", PRODUCT);
        map.put("subject", etSubject.getText().toString());
        if (MyUtil.isNN_ET(etDes))
            map.put("description", etDes.getText().toString());
        if (MyUtil.isNN(TRAN_ID))
            map.put("transaction_id", TRAN_ID);
        if (MyUtil.isNN(STATUS)) {
            map.put("status", STATUS);
            if (STATUS.equalsIgnoreCase("success"))
                map.put("status", "resolved");
        }
        return map;
    }

    private Map<String, String> paramSupport() {
        Map<String, String> map = new HashMap<>();
        map.put("title", etSubject.getText().toString());
        if (etDes.getText().toString().length() > 0)
            map.put("description", etDes.getText().toString());
        return map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        String message = AppHandler.getMessage(JSonResponse);
        popUp(message);
    }

    @Override
    public void onFailRequest(String msg) {
        Print.P(msg);
    }

    private void popUp(String msg) {
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Response")
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> {
                    dialog.dismiss();
                    Constants.IS_RELOAD_REQUEST = true;
                    finish();
                });
        alert = builder.create();
        alert.setCancelable(false);
        alert.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 110) {
                subName = data.getStringExtra("subject");
                etSubject.setText(subName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
