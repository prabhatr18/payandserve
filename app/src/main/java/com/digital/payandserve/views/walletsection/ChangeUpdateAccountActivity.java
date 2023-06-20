package com.digital.payandserve.views.walletsection;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.ExtensionFunction;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.utill.SharedPrefs;
import com.digital.payandserve.views.walletsection.model.ChangeUpdateModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChangeUpdateAccountActivity extends AppCompatActivity implements
        ChangeUpdateAccountAdapter.OnUpdateAccountListner, RequestResponseLis {

    private String accountType;
    private ArrayList<ChangeUpdateModel> list;
    private RecyclerView accountDetailRecycler;
    private ChangeUpdateAccountAdapter adapter;
    private ImageView imgClose;
    private FloatingActionButton fabButton;
    private String bank_name, account_no, ifsc_code;
    private String reportid, bankUsername, accountNum, IfscCod;
    private int REQUEST_CODE = 0;
    private String updateDetailPosition;
    private boolean isFromUpdate = true;
    private AlertDialog loaderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_update_account);

        accountDetailRecycler = findViewById(R.id.accountDetailRecycler);
        imgClose = findViewById(R.id.imgClose);
        fabButton = findViewById(R.id.fabButton);

        imgClose.setOnClickListener(v -> {
            finish();
        });

        list = new ArrayList<>();
        accountDetailRecycler.setLayoutManager(new LinearLayoutManager(this));
        intitAccountRecycler(SharedPrefs.getValue(this, SharedPrefs.ACCOUNT),
                SharedPrefs.getValue(this, SharedPrefs.ACCOUNT2),
                SharedPrefs.getValue(this, SharedPrefs.ACCOUNT3));

        if (list.size() >= 3) {
            fabButton.setVisibility(View.GONE);
        }

        fabButton.setOnClickListener(v -> {
            REQUEST_CODE = 0;
            isFromUpdate = false;
            showUpdateDialog("", "", "", "", "create");
        });

    }

    private void intitAccountRecycler(String Account1, String Account2, String Account3) {
        list.clear();
        if (MyUtil.isNN(Account1)) {
            list.add(new ChangeUpdateModel(SharedPrefs.getValue(this,
                    SharedPrefs.BANK), SharedPrefs.getValue(this, SharedPrefs.ACCOUNT),
                    SharedPrefs.getValue(this, SharedPrefs.IFSC), "Account 1"));
        }

        if (MyUtil.isNN(Account2)) {
            list.add(new ChangeUpdateModel(SharedPrefs.getValue(this, SharedPrefs.BANK2),
                    SharedPrefs.getValue(this, SharedPrefs.ACCOUNT2),
                    SharedPrefs.getValue(this, SharedPrefs.IFSC2), "Account 2"));
        }

        if (MyUtil.isNN(Account3)) {
            list.add(new ChangeUpdateModel(SharedPrefs.getValue(this, SharedPrefs.BANK3),
                    SharedPrefs.getValue(this, SharedPrefs.ACCOUNT3),
                    SharedPrefs.getValue(this, SharedPrefs.IFSC3), "Account 3"));
        }

        adapter = new ChangeUpdateAccountAdapter(list, this);
        accountDetailRecycler.setAdapter(adapter);
    }

    @Override
    public void onMainLayoutClicked(int postion, String type, String bankName, String account, String ifsc) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("type", type);
        returnIntent.putExtra("bankName", bankName);
        returnIntent.putExtra("account", account);
        returnIntent.putExtra("ifsc", ifsc);
        setResult(101, returnIntent);
        finish();
    }

    @Override
    public void onUpdateButtonClicked(int postion, String type, String bankName, String account, String ifsc) {
        isFromUpdate = true;
        accountType = type;
        updateDetailPosition = String.valueOf(postion + 1);
        showUpdateDialog(type, bankName, account, ifsc, "update");
    }

    private void showUpdateDialog(String type, String bankName, String account, String ifsc, String from) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeUpdateAccountActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.update_account_dialog, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button btnProceed = dialogView.findViewById(R.id.btnProceed);
        EditText etBankName = dialogView.findViewById(R.id.etBankName);
        EditText etAccount = dialogView.findViewById(R.id.etAccount);
        EditText etIFSC = dialogView.findViewById(R.id.etIFSC);
        ImageView closeIcon = dialogView.findViewById(R.id.closeIcon);

        etBankName.setText(bankName);
        etAccount.setText(account);
        etIFSC.setText(ifsc);

        closeIcon.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        if (from.equals("update")) {
            btnProceed.setText("Update Account");
        } else {
            btnProceed.setText("Create Account");
        }

        btnProceed.setOnClickListener(v -> {
            bank_name = etBankName.getText().toString().trim();
            account_no = etAccount.getText().toString().trim();
            ifsc_code = etIFSC.getText().toString().trim();
            if (isValid()) {
                alertDialog.dismiss();
                initateAccountDetails();
            }
        });
    }

    private void initateAccountDetails() {
        REQUEST_CODE = 0;
        networkCallUsingVolleyApi(initateParam(), Constants.URL.FUND_REQUEST);
    }

    private Boolean isValid() {
        if (bank_name == null || bank_name.equals("")) {
            ExtensionFunction.showToast(this, "Enter Bank Name");
            return false;
        }

        if (account_no == null || account_no.equals("")) {
            ExtensionFunction.showToast(this, "Enter Account Number");
            return false;
        }

        if (ifsc_code == null || ifsc_code.equals("")) {
            ExtensionFunction.showToast(this, "Enter Ifsc Code");
            return false;
        }
        return true;
    }

    private void networkCallUsingVolleyApi(Map<String, String> map, String url) {
        if (AppManager.isOnline(this)) {
            new VolleyNetworkCall(this, this, url, 1, map, true).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    public Map<String, String> updateParam() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", SharedPrefs.getValue(this, SharedPrefs.USER_ID));
        map.put("apptoken", SharedPrefs.getValue(this, SharedPrefs.APP_TOKEN));
        map.put("type", "bankchange");
        map.put("transactionType", "update");
        if (isFromUpdate) {
            map.put("accountType", accountType.replace(" ", "").toLowerCase());
        }
        map.put("reportid", reportid);
        map.put("name", bankUsername);
        return map;
    }


    public Map<String, String> initateParam() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", SharedPrefs.getValue(this, SharedPrefs.USER_ID));
        map.put("apptoken", SharedPrefs.getValue(this, SharedPrefs.APP_TOKEN));
        map.put("type", "bankchange");
        map.put("transactionType", "initiate");
        map.put("bank", bank_name);
        map.put("account", account_no);
        map.put("ifsc", ifsc_code);
        return map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        Print.P("success " + JSonResponse);
        try {
            JSONObject jsonObject = new JSONObject(JSonResponse);
            if (REQUEST_CODE == 0) {
                Print.P("########### Add Account #############");
                reportid = jsonObject.getString("id");
                bankUsername = jsonObject.getString("name");
                updateAccountDetails();
            } else {
                Print.P("########### Update Account #############");
                String message = AppHandler.getMessage(JSonResponse);
                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                if (isFromUpdate) {
                    updateBankAccount();
                } else {
                    insertBankAccount();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateAccountDetails() {
        REQUEST_CODE = 1;
        networkCallUsingVolleyApi(updateParam(), Constants.URL.FUND_REQUEST);
    }

    private void updateBankAccount() {
        if (updateDetailPosition.equals("1")) {
            addBankDetails(account_no, "", bank_name, ifsc_code);
        } else {
            addBankDetails(account_no, updateDetailPosition, bank_name, ifsc_code);
        }
    }

    private void insertBankAccount() {
        if (!MyUtil.isNN(SharedPrefs.getValue(this, SharedPrefs.ACCOUNT))) {
            addBankDetails(account_no, "", bank_name, ifsc_code);
            Print.P("LOG1");
        } else if (!MyUtil.isNN(SharedPrefs.getValue(this, SharedPrefs.ACCOUNT2))) {
            addBankDetails(account_no, "2", bank_name, ifsc_code);
            Print.P("LOG2");
        } else {
            addBankDetails(account_no, "3", bank_name, ifsc_code);
            Print.P("LOG3");
        }
    }

    private void addBankDetails(String account, String num, String bank, String ifsc) {
        ExtensionFunction.showToast(this, "account " + account + "Num : " + num + "bank : " + bank + "Ifsc : " + ifsc);
        Print.P("account " + account + " Num : " + num + " bank : " + bank + " Ifsc : " + ifsc);
        SharedPrefs.setValue(this, SharedPrefs.ACCOUNT + num, account);
        SharedPrefs.setValue(this, SharedPrefs.BANK + num, bank);
        SharedPrefs.setValue(this, SharedPrefs.IFSC + num, ifsc);
        intitAccountRecycler(SharedPrefs.getValue(this, SharedPrefs.ACCOUNT),
                SharedPrefs.getValue(this, SharedPrefs.ACCOUNT2),
                SharedPrefs.getValue(this, SharedPrefs.ACCOUNT3));
    }

    @Override
    public void onFailRequest(String msg) {
        Print.P("Failure " + msg);
    }
}