package com.payment.aeps.fingpay_microatm.printer;

import android.content.Context;
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
import com.payment.aeps.fingpay_microatm.FingSdkUtill.FingUtil;
import com.payment.aeps.fingpay_microatm.printer.adapter.InvoiceListStatementAdapter;
import com.payment.aeps.moduleprinter.PrintDataModel;

import java.util.ArrayList;

public class MiniStatementInvoice extends AppCompatActivity{

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
        context = MiniStatementInvoice.this;

        ImageView print = findViewById(R.id.imgPrint);
        print.setVisibility(View.GONE);
        ImageView share = findViewById(R.id.imgShare);

        type = getIntent().getStringExtra("type");

        imgCrossFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                k();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l();
            }
        });

        if (FingUtil.StatementList != null && !FingUtil.StatementList.isEmpty()) {
            initTransactionList();
        } else {
            Toast.makeText(context, "Invoice records are not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void initList() {
    }

    private void initTransactionList() {
        imgTxnStatus.setVisibility(View.GONE);
        tvTxnStatus.setVisibility(View.GONE);
        findViewById(R.id.transactionLbl).setVisibility(View.INVISIBLE);
        try {
            InvoiceListStatementAdapter adapter = new InvoiceListStatementAdapter(this, FingUtil.StatementList);
            ListView listView = findViewById(R.id.listView);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(context, "Invoice Generation Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            finish();
        }
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
}


