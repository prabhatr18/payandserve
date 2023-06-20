package com.digital.payandserve.views.billpayment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.utill.SharedPrefs;
import com.digital.payandserve.views.invoice.ReportInvoice;
import com.digital.payandserve.views.invoice.model.InvoiceModel;
import com.digital.payandserve.views.otpview.OTPPinReset;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class ShowBillFetched extends AppCompatActivity implements RequestResponseLis {
    private TextView tvProvider, tvDueDate, tvGenPin;
    private ImageView imgProvider, imgBack, imgPre;
    private Button btnProceed;
    private EditText etConsumerName, etAmount, etTPin;
    private String AMOUNT, BILLER_NAME, DUE_DATE, TRANSACTION_ID, PROVIDER_NAME, PROVIDER_LOGO,
            PROVIDER_ID, TYPE, BILL_NO, CONTACT;
    private int BBP_TYPE = Constants.BBPS.NEW;

    private void init() {
        etConsumerName = findViewById(R.id.etName);
        tvGenPin = findViewById(R.id.tvGenPin);
        imgProvider = findViewById(R.id.imgProvider);
        tvDueDate = findViewById(R.id.tvDueDate);
        tvProvider = findViewById(R.id.title);
        etAmount = findViewById(R.id.etAmount);
        etTPin = findViewById(R.id.etTPin);
        imgPre = findViewById(R.id.imgPre);
        btnProceed = findViewById(R.id.btnProceed);
        imgBack = findViewById(R.id.icBack);
        imgBack.setOnClickListener(v -> finish());
        AMOUNT = getIntent().getStringExtra("amount");
        BILLER_NAME = getIntent().getStringExtra("billerName");
        DUE_DATE = getIntent().getStringExtra("dueDate");
        TRANSACTION_ID = getIntent().getStringExtra("transactionId");
        PROVIDER_ID = getIntent().getStringExtra("provider_id");
        PROVIDER_NAME = getIntent().getStringExtra("provider_name");
        PROVIDER_LOGO = getIntent().getStringExtra("provider_logo");
        BILL_NO = getIntent().getStringExtra("bill_number");
        CONTACT = getIntent().getStringExtra("bill_contact");
        TYPE = getIntent().getStringExtra("type");
        BBP_TYPE = Integer.parseInt(getIntent().getStringExtra("bbps_type"));

        etAmount.setText(AMOUNT);
        etConsumerName.setText(BILLER_NAME);
        tvDueDate.setText(DUE_DATE);
        tvProvider.setText(PROVIDER_NAME);
        MyUtil.setProviderImage(PROVIDER_LOGO, imgProvider, PROVIDER_NAME, this, TYPE);
        MyUtil.setProviderImage(PROVIDER_LOGO, imgPre, PROVIDER_NAME, this, TYPE);

        tvDueDate.setFocusable(false);
        tvDueDate.setOnClickListener(arg0 -> {
            final AlertDialog.Builder adb = new AlertDialog.Builder(
                    ShowBillFetched.this);
            final View view = LayoutInflater.from(ShowBillFetched.this).inflate(R.layout.date_picker, null);
            adb.setView(view);
            final Dialog dialog;
            adb.setPositiveButton("OK",
                    (dialog1, arg1) -> {
                        DatePicker etDatePicker = view.findViewById(R.id.datePicker1);
                        Calendar cal = GregorianCalendar.getInstance();
                        cal.set(etDatePicker.getYear(), etDatePicker.getMonth(), etDatePicker.getDayOfMonth());
                        Date date = null;
                        date = cal.getTime();
                        String approxDateString = Constants.SHOWING_DATE_FORMAT.format(date);
                        tvDueDate.setText(approxDateString);
                    });
            dialog = adb.create();
            dialog.show();
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.whiteStatusBar(this);
        setContentView(R.layout.show_fetched_bill_new);
        init();

        btnProceed.setOnClickListener(v -> {
            if (isValid()) {
                showLoader(ShowBillFetched.this);
            }
        });

        tvGenPin.setOnClickListener(v -> startActivity(new Intent(this, OTPPinReset.class)));

    }

    private boolean isValid() {
        if (etConsumerName.getText() == null || etConsumerName.getText().length() == 0) {
            Toast.makeText(this, "Biller name is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etTPin.getText() == null || etTPin.getText().length() == 0) {
            Toast.makeText(this, "TPin is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (tvDueDate.getText() == null || tvDueDate.getText().length() == 0) {
            Toast.makeText(this, "Due date is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        String amount = etAmount.getText().toString();
        if (amount.length() == 0) {
            Toast.makeText(this, "Enter amount value", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            if (Double.parseDouble(amount) < 10) {
                Toast.makeText(this, "Amount value should be grater than 10 ", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("apptoken", SharedPrefs.getValue(this, SharedPrefs.APP_TOKEN));
        map.put("user_id", SharedPrefs.getValue(this, SharedPrefs.USER_ID));
        map.put("type", "payment");
        map.put("provider_id", PROVIDER_ID);
        map.put("biller", etConsumerName.getText().toString());
        map.put("duedate", tvDueDate.getText().toString());
        map.put("amount", etAmount.getText().toString());
        map.put("pin", etTPin.getText().toString());
        map.put("bu", "");
        if (TRANSACTION_ID != null && TRANSACTION_ID.length() > 0)
            map.put("TransactionId", TRANSACTION_ID);
        String approxDateString = Constants.SHOWING_DATE_FORMAT.format(new Date());
        map.put("dateTime", approxDateString);
        if (BBP_TYPE == Constants.BBPS.NEW) {
            if (Constants.BILL_MODEL != null) {
                for (int i = 0; i < Constants.BILL_MODEL.getParame().size(); i++) {
                    String key = "number" + i;
                    map.put(key, Constants.BILL_MODEL.getParame().get(i).getFieldInputValue());
                }
            }
        } else {
            map.put("number", BILL_NO);
            map.put("mobile", CONTACT);
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

    Dialog dialog;

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
        MyUtil.setProviderImage(PROVIDER_LOGO, imgProvider, PROVIDER_NAME, this, TYPE);
        TextView tvMobile = dialog.findViewById(R.id.tvMobile);
        tvMobile.setText(etConsumerName.getText().toString());
        TextView tvOperator = dialog.findViewById(R.id.tvOperator);
        tvOperator.setText("Amount : " + etAmount.getText().toString());
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
            //networkCallUsingVolleyApi(Constants.URL.MOBILE_RECHARGE_PAY, true);
            networkCallUsingVolleyApi(Constants.URL.BBPS_BILL_PAY, true);
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
            String message = jsonObject.getString("message");
            String txnId = jsonObject.getString("txnid");
            String rrn = jsonObject.getString("rrn");
            Constants.INVOICE_DATA = new ArrayList<>();
            Constants.INVOICE_DATA.add(new InvoiceModel("Txn Id", txnId));
            Constants.INVOICE_DATA.add(new InvoiceModel("RRN No", rrn));
            Constants.INVOICE_DATA.add(new InvoiceModel("Consumer Name", etConsumerName.getText().toString()));
            Constants.INVOICE_DATA.add(new InvoiceModel("Due Date", tvDueDate.getText().toString()));
            Constants.INVOICE_DATA.add(new InvoiceModel("Date", Constants.SHOWING_DATE_FORMAT.format(new Date())));
            Constants.INVOICE_DATA.add(new InvoiceModel("Provider ID", PROVIDER_ID));
            Constants.INVOICE_DATA.add(new InvoiceModel("Provider Name", PROVIDER_NAME));
            Constants.INVOICE_DATA.add(new InvoiceModel("Trans Type", TYPE));
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
}
