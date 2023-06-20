package com.digital.payandserve.views.upi;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.digital.payandserve.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.views.invoice.ReportInvoice;
import com.digital.payandserve.views.invoice.model.InvoiceModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowQR extends AppCompatActivity implements RequestResponseLis {
    private String TXIN_ID, RRN, NAME, MOBILE, AMOUTN, TYPE;
    private ImageView img;
    private Button btnStatus;
    private TextView tvTimer;
    private long expireTimer = 1000 * 60 * 1;
    MyCountDownTimer myCountDownTimer;
    private TextView tvRetry;
    private int COUNT = 0;
    private boolean IS_SESSION_EXPIRE = false;

    private void init() {
        img = findViewById(R.id.imgQR);
        tvRetry = findViewById(R.id.tvRetry);
        TXIN_ID = getIntent().getStringExtra("txnId");
        RRN = getIntent().getStringExtra("rrn");
        NAME = getIntent().getStringExtra("name");
        MOBILE = getIntent().getStringExtra("mobile");
        AMOUTN = getIntent().getStringExtra("amount");
        TYPE = getIntent().getStringExtra("type");
        btnStatus = findViewById(R.id.btnStatus);
        tvTimer = findViewById(R.id.timer);
        MyUtil.setGlideImage(RRN, img, this);
        myCountDownTimer = new MyCountDownTimer(expireTimer, 1000);
        myCountDownTimer.start();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.whiteStatusBar(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;
        getWindow().setAttributes(params);
        setContentView(R.layout.upi_show_qr_popup);
        init();

        if (TYPE.equalsIgnoreCase("GQ")) {
            findViewById(R.id.secOne).setVisibility(View.VISIBLE);
            findViewById(R.id.secTwo).setVisibility(View.GONE);
        } else {
            findViewById(R.id.secOne).setVisibility(View.GONE);
            findViewById(R.id.secTwo).setVisibility(View.VISIBLE);
        }

        networkCallUsingVolleyApi(Constants.URL.UPI_CASH_STATUS, true);
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("txnid", TXIN_ID);
        return map;
    }

    private void networkCallUsingVolleyApi(String url, boolean isLoad) {
        if (AppManager.isOnline(this)) {
            new VolleyNetworkCall(this, this, url, 1, param(), false, false).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        Print.P("RES : " + JSonResponse);
        try {
            String status = AppHandler.getStatus(JSonResponse);
            if (status.equalsIgnoreCase("success") || status.equalsIgnoreCase("TXN")) {
                Constants.INVOICE_DATA = new ArrayList<>();
                Constants.INVOICE_DATA.add(new InvoiceModel("Txnid", TXIN_ID));
                Constants.INVOICE_DATA.add(new InvoiceModel("Name", NAME));
                Constants.INVOICE_DATA.add(new InvoiceModel("Mobile", MOBILE));
                Constants.INVOICE_DATA.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(this, AMOUTN)));
                Constants.INVOICE_DATA.add(new InvoiceModel("Type", TYPE));
                Constants.INVOICE_DATA.add(new InvoiceModel("Status", status));
                Intent i = new Intent(this, ReportInvoice.class);
                i.putExtra("status", status);
                String message = AppHandler.getMessage(JSonResponse);
                i.putExtra("remark", "" + message);
                startActivity(i);
                finish();
            } else if (status.equalsIgnoreCase("pending")) {
                COUNT++;
                tvRetry.setText(String.valueOf(COUNT));
                if (!IS_SESSION_EXPIRE)
                    pendingRetryCall(500);
            } else {
                findViewById(R.id.bottomBar).setVisibility(View.GONE);
                String message = AppHandler.getMessage(JSonResponse);
                String str = "Status : " + status + "\n\n" + message;
                popUp(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Print.P(msg);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            if (myCountDownTimer != null) {
                myCountDownTimer.cancel();
            }

            if (splashTimer != null) {
                splashTimer.interrupt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (myCountDownTimer != null) {
                myCountDownTimer.cancel();
            }

            if (splashTimer != null) {
                splashTimer.interrupt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long minutes = (millisUntilFinished / 1000) / 60;
            int seconds = (int) ((millisUntilFinished / 1000) % 60);
            String time = minutes + " : " + seconds;
            tvTimer.setText(time);
        }

        @Override
        public void onFinish() {
            String str = "Session Time Out";
            String msg = "Please check this transaction status in the report section";
            TextView tv = findViewById(R.id.tvMsg);
            LottieAnimationView lottie = findViewById(R.id.lottie);
            lottie.setAnimation(R.raw.payment_pending_file);
            tv.setText(msg);
            IS_SESSION_EXPIRE = true;
            popUp(str);
        }
    }

    private void popUp(String title) {
        AlertDialog alert;
        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(this,
                R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setTitle("Payment Status")
                .setMessage(title)
                .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> {
                    dialog.dismiss();
                    finish();
                });
        alert = builder.create();
        alert.setCancelable(false);
        alert.show();
    }

    Thread splashTimer;

    private void pendingRetryCall(final int maxTime) {
        splashTimer = new Thread() {
            public void run() {
                try {
                    int splashTime = 0;
                    while (splashTime < maxTime) {
                        Thread.sleep(700);
                        splashTime = splashTime + 100;
                    }
                    networkCallUsingVolleyApi(Constants.URL.UPI_CASH_STATUS, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        splashTimer.start();
    }
}
