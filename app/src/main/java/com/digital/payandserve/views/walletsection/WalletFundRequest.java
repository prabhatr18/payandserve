package com.digital.payandserve.views.walletsection;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.views.invoice.ReportInvoice;
import com.digital.payandserve.views.invoice.model.InvoiceModel;
import com.digital.payandserve.views.otpview.OTPPinReset;
import com.digital.payandserve.views.walletsection.model.ModeModel;
import com.digital.payandserve.views.walletsection.model.RequestBankModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WalletFundRequest extends AppCompatActivity implements RequestResponseLis {
    private EditText etBankName, etRefNo, etDate, etMode, etAmount, etTPin;
    private TextView tvBankDetail, tvGenPin;
    private Context context;
    private Button btnProceed;
    private List<RequestBankModel> bankList;
    private List<ModeModel> modeList;
    private int REQUEST_TYPE = 0;
    private String BANK_ID = "", MODE_ID = "";
    private RadioButton rbKuber, rbBank;
    private String SELECTED_MODE = "";

    private void init() {
        context = this;
        bankList = new ArrayList<>();
        modeList = new ArrayList<>();
        tvBankDetail = findViewById(R.id.tvBankDetail);
        rbKuber = findViewById(R.id.rbKuber);
        rbBank = findViewById(R.id.rbBank);
        tvGenPin = findViewById(R.id.tvGenPin);
        etBankName = findViewById(R.id.etBankName);
        etRefNo = findViewById(R.id.etRefNo);
        etDate = findViewById(R.id.etDate);
        etTPin = findViewById(R.id.etTPin);
        etMode = findViewById(R.id.etMode);
        etAmount = findViewById(R.id.etAmount);
        btnProceed = findViewById(R.id.btnProceed);
        etMode.setOnClickListener(v -> modePopup(2, modeList.size()));
        etBankName.setOnClickListener(v -> modePopup(1, bankList.size()));
        etDate.setText(Constants.COMMON_DATE_FORMAT.format(new Date()));
        etDate.setOnClickListener(arg0 -> {
            final AlertDialog.Builder adb = new AlertDialog.Builder(this);
            final View view = LayoutInflater.from(this).inflate(R.layout.date_picker, null);
            adb.setView(view);
            final Dialog dialog;
            adb.setPositiveButton("OK",
                    (dialog1, arg1) -> {
                        DatePicker etDatePicker = view.findViewById(R.id.datePicker1);
                        Calendar cal = GregorianCalendar.getInstance();
                        cal.set(etDatePicker.getYear(), etDatePicker.getMonth(), etDatePicker.getDayOfMonth());
                        Date date = null;
                        date = cal.getTime();
                        String approxDateString = Constants.COMMON_DATE_FORMAT.format(date);
                        etDate.setText(approxDateString);
                    });
            dialog = adb.create();
            dialog.show();
        });

        SELECTED_MODE = "BANK";
        rbKuber.setOnClickListener(v -> {
            SELECTED_MODE = "KUBER";
            findViewById(R.id.secBankSec).setVisibility(View.GONE);
        });

        rbBank.setOnClickListener(v -> {
            SELECTED_MODE = "BANK";
            findViewById(R.id.secBankSec).setVisibility(View.VISIBLE);
        });

        REQUEST_TYPE = 0;
        networkCallUsingVolleyApi(Constants.URL.FUND_REQUEST, true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_fund_request);
        init();

        btnProceed.setOnClickListener(v -> {
            if (isValid()) {
                REQUEST_TYPE = 1;
                networkCallUsingVolleyApi(Constants.URL.FUND_REQUEST, true);
            }
        });

        tvGenPin.setOnClickListener(v -> startActivity(new Intent(this, OTPPinReset.class)));
    }

    private void modePopup(int popupType, int size) {
        if (popupType == 1 && size == 0) {
            Toast.makeText(context, "Bank list is not available", Toast.LENGTH_SHORT).show();
            return;
        }
        if (popupType == 2 && size == 0) {
            Toast.makeText(context, "payment mode list is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] array = new String[size];
        String title = "";
        int i = 0;
        if (popupType == 1) {
            title = "Please select bank";
            for (RequestBankModel s : bankList) {
                array[i++] = s.getName();
            }
        } else {
            title = "Please select payment mode";
            for (ModeModel s : modeList) {
                array[i++] = s.getName();
            }
        }

        AlertDialog.Builder alert = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog);
        alert.setTitle(title);
        alert.setSingleChoiceItems(array, -1, (dialog, which) -> {
            if (popupType == 1) {
                RequestBankModel model = bankList.get(which);
                String bankDetails = "Account No : " + model.getAccount();
                bankDetails += "\nIFSC Code : " + model.getIfsc();
                bankDetails += "\nBranch : " + model.getBranch();
                tvBankDetail.setText(bankDetails);
                tvBankDetail.setVisibility(View.VISIBLE);
                etBankName.setText(model.getName());
                BANK_ID = model.getId();
            } else {
                ModeModel m = modeList.get(which);
                etMode.setText(m.getName());
                MODE_ID = m.getId();
            }
        });
        alert.setNegativeButton("OK", (dialog, which) -> dialog.dismiss());
        alert.show();
    }

    private boolean isValid() {
        if (SELECTED_MODE.equalsIgnoreCase("BANK")) {
            if (BANK_ID.equals("")) {
                Toast.makeText(this, "Please select bank", Toast.LENGTH_SHORT).show();
                return false;
            } else if (MODE_ID.equals("")) {
                Toast.makeText(this, "Please select payment mode", Toast.LENGTH_SHORT).show();
                return false;
            } else if (etDate.getText().toString().equals("")) {
                Toast.makeText(this, "Please select payment date", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (etAmount.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show();
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
        if (SELECTED_MODE.equalsIgnoreCase("KUBER")) {
            map.put("type", "maintokuber");
            map.put("amount", etAmount.getText().toString());
        } else {
            if (REQUEST_TYPE == 0)
                map.put("type", "getfundbank");
            else {
                map.put("type", "request");
                map.put("fundbank_id", BANK_ID);
                map.put("paymode", MODE_ID);
                map.put("paydate", etDate.getText().toString());
                map.put("amount", etAmount.getText().toString());
                if (etRefNo.getText().toString().length() > 0)
                    map.put("ref_no", etRefNo.getText().toString());
            }
        }
        map.put("pin", etTPin.getText().toString());
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
            if (REQUEST_TYPE == 0) {
                jsonObject = jsonObject.getJSONObject("data");
                JSONArray bankArray = jsonObject.getJSONArray("banks");
                bankList.addAll(AppHandler.parseBankListRes(this, bankArray));
                bankArray = jsonObject.getJSONArray("paymodes");
                modeList.addAll(AppHandler.parseModeListRes(this, bankArray));
            } else {
                String txnid = jsonObject.getString("txnid");
                Constants.INVOICE_DATA = new ArrayList<>();
                Constants.INVOICE_DATA.add(new InvoiceModel("Txn Id", txnid));
                if (SELECTED_MODE.equalsIgnoreCase("BANK"))
                    Constants.INVOICE_DATA.add(new InvoiceModel("Bank", etBankName.getText().toString()));
                Constants.INVOICE_DATA.add(new InvoiceModel("Ref Number", etRefNo.getText().toString()));
                Constants.INVOICE_DATA.add(new InvoiceModel("Date", Constants.COMMON_DATE_FORMAT.format(new Date())));
                Constants.INVOICE_DATA.add(new InvoiceModel("Trans Type", "Load Wallet Request"));
                Constants.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(this, etAmount.getText().toString())));
                Constants.INVOICE_DATA.add(new InvoiceModel("Status", "success"));
                Intent i = new Intent(this, ReportInvoice.class);
                i.putExtra("status", "success");
                i.putExtra("remark", "" + msg);
                startActivity(i);
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
