package com.payment.aeps.fingpay_microatm.printer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.payment.aeps.R;
import com.payment.aeps.app.ModuleConstants;
import com.payment.aeps.fingpay_microatm.printer.adapter.InvoiceListAdapter;
import com.payment.aeps.moduleprinter.PrintDataModel;
import com.payment.aeps.network.VolleyGetNetworkCall;
import com.payment.aeps.util.ModuleSharedPrefs;
import com.payment.aeps.util.ModuleUtil;
import com.payment.aeps.util.Print;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Invoice extends AppCompatActivity implements VolleyGetNetworkCall.RequestResponseLis {

    NestedScrollView scrollView;
    TextView titlebar;
    Context context;
    private ImageView imgCrossFinish;
    private ImageView imgTxnStatus;
    private TextView tvTxnStatus;
    private String type;

    public void init() {
        imgCrossFinish = findViewById(R.id.imgCrossFinish);
        ImageView imgShare = findViewById(R.id.imgShare);
        scrollView = findViewById(R.id.scrollView);
        View zigzagtop = findViewById(R.id.zigzagtop);
        LinearLayout mainInvoice = findViewById(R.id.mainInvoice);
        ImageView imgPrint = findViewById(R.id.imgPrint);
        imgTxnStatus = findViewById(R.id.imgTxnStatus);
        tvTxnStatus = findViewById(R.id.tvTxnStatus);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fing_invoice_layout);
        init();
        context = Invoice.this;
        ImageView print = findViewById(R.id.imgPrint);
        ImageView share = findViewById(R.id.imgShare);
        type = getIntent().getStringExtra("type");
        imgCrossFinish.setOnClickListener(v -> finish());
        print.setOnClickListener(v -> k());
        share.setOnClickListener(v -> l());

        if (ModuleConstants.ReceiptMap != null && !ModuleConstants.ReceiptMap.isEmpty()) {
            initList();
            if (type != null && type.length() > 0) {
                initTransactionList();
            } else {
                findViewById(R.id.transactionLbl).setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(context, "Invoice records are not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void initList() {
        try {
            InvoiceListAdapter adapter = new InvoiceListAdapter(this, ModuleConstants.ReceiptMap);
            ListView listView = findViewById(R.id.listView);
            listView.setAdapter(adapter);
            String status = ModuleConstants.ReceiptMap.get("Status");
            String message = ModuleConstants.ReceiptMap.get("Message");
            tvTxnStatus.setText(message);
            if (status != null && status.equalsIgnoreCase("true")) {
                imgTxnStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));
            } else {
                imgTxnStatus.setImageDrawable(getResources().getDrawable(R.drawable.m_cancel));
            }
            networkCallUsingVolleyApi(ModuleConstants.FMATM_UPDATE);
        } catch (Exception e) {
            Toast.makeText(context, "Invoice Generation Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            finish();
        }
    }

    private void initTransactionList() {
        findViewById(R.id.transactionLbl).setVisibility(View.VISIBLE);
        TextView trnLbl = findViewById(R.id.transactionLbl);
        trnLbl.setOnClickListener(view -> {
            /*Intent i = new Intent(Invoice.this, MiniStatementInvoice.class);
            startActivity(i);*/
        });
    }

    public void setInvisible(RelativeLayout layout, View view) {
        layout.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
    }

    public final void l() {
        if (Build.VERSION.SDK_INT >= 23 && this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED
                && this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        } else
            new wd().NUL(this.scrollView, this.context);
    }

    public final void k() {

        final PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);

        final String string = "E Banker Document";

        String s = "EBanker";
        final ArrayList<PrintDataModel> list2;
        final ArrayList<PrintDataModel> list = list2 = new ArrayList<PrintDataModel>();

        String message = ModuleConstants.ReceiptMap.get("Message");
        ArrayList<String> keyList = (new ArrayList<String>(ModuleConstants.ReceiptMap.keySet()));
        for (int i = 0; i < keyList.size(); i++)
            list.add(new PrintDataModel(keyList.get(i), ModuleConstants.ReceiptMap.get(keyList.get(i))));

        final PrintManager printManager2 = printManager;
        final String s2 = string;
        final ArrayList<PrintDataModel> list5 = list2;
        list5.add(new PrintDataModel("Remark", message));
        printManager2.print(s2, new ud(this, list2, s, "app_name"), null);
    }

    private void networkCallUsingVolleyApi(String url) {
        if (ModuleUtil.isOnline(this)) {
            new VolleyGetNetworkCall(this, this, url, 1, param()).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.USER_ID));
        map.put("apptoken", ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.APP_TOKEN));
        String res = new JSONObject(ModuleConstants.ReceiptMap).toString();
        map.put("txn_id", ModuleConstants.FING_TXN_ID);
        map.put("response", res);
        Print.P("PARAM : \n " + new JSONObject(map).toString());
        Print.appendLog("---------------------------- NEW REQUEST ----------------------");
        Print.appendLog(new JSONObject(map).toString());
        return map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        Print.P(JSonResponse);
        Toast.makeText(context, "Records updated successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailRequest(String msg) {
        try {
            Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}


