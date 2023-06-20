package com.digital.payandserve.views.dmt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.views.invoice.model.InvoiceModel;
import com.digital.payandserve.views.invoice.model.PrintDataModel;
import com.digital.payandserve.views.invoice.ud;

import java.util.ArrayList;

public class CompactReportInvoice extends AppCompatActivity implements View.OnClickListener {
    final ArrayList<PrintDataModel> list2;
    final ArrayList<PrintDataModel> list = list2 = new ArrayList<>();
    String message;
    private NestedScrollView scrollView;
    private Context context;
    private ImageView imgShare, imgPrint;
    private TextView tvMessage, tvRemiterDetails, tvTotal;
    private RelativeLayout top;
    private LinearLayout invoiceCon;

    public void init() {
        imgShare = findViewById(R.id.imgShare);
        invoiceCon = findViewById(R.id.invoiceCon);
        top = findViewById(R.id.top);
        imgPrint = findViewById(R.id.imgPrint);
        scrollView = findViewById(R.id.scrollView);
        tvMessage = findViewById(R.id.tvMessage);
        tvTotal = findViewById(R.id.tvTotal);
        tvRemiterDetails = findViewById(R.id.tvRemiterDetails);
        String remitterDetails = getIntent().getStringExtra("remitter");
        tvRemiterDetails.setText(remitterDetails);
        setLis();
        permissionCheck();
        setupToolBar();

        generateInvoice();
        showBenList();
        showAccountList();
    }

    private void setupToolBar() {
        ImageView backImage = findViewById(R.id.imgClose);
        backImage.setOnClickListener(view -> finish());
    }

    private void setLis() {
        imgShare.setOnClickListener(this);
        imgPrint.setOnClickListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.transparentStatusBar(this);
        setContentView(R.layout.dmt_report_invoice);
        init();
        context = this;
        parseRes();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void parseRes() {
        try {
            message = getIntent().getStringExtra("remark");
            if (message == null || message.length() == 0 || message.trim().equalsIgnoreCase("null")) {
                tvMessage.setVisibility(View.GONE);
            } else {
                tvMessage.setVisibility(View.VISIBLE);
                tvMessage.setText("Reason : "+message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public final void l() {
        if (Build.VERSION.SDK_INT >= 23 && this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED
                && this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        } else
            new Wd().NUL(this.invoiceCon, this.context);
    }

    public final void permissionCheck() {
        if (Build.VERSION.SDK_INT >= 23 && this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED
                && this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
    }

    public final void k() {
        for (InvoiceModel m : Constants.INVOICE_DATA) {
            list.add(new PrintDataModel(m.getKey(), m.getValue()));
        }
        final PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        final String string = "Banker Document";
        String s = "Banker";
        list2.add(new PrintDataModel("Remark", message));
        printManager.print(string, new ud(this, list2, s, "app_name"), null);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgPrint) {
           // Toast.makeText(context, "data", Toast.LENGTH_SHORT).show();
            k();
        } else if (view.getId() == R.id.imgShare) {
            l();
        }
    }

    private void generateInvoice() {
        ArrayList<LocalInvoiceModel> dataList = getIntent().getParcelableArrayListExtra("invoiceData");
        RecyclerView recyclerView = findViewById(R.id.rvInvoiceData);
        findViewById(R.id.secTransactionData).setVisibility(View.VISIBLE);
        findViewById(R.id.tvTotal).setVisibility(View.VISIBLE);
        recyclerView.setAdapter(new GRAdapter<>(dataList, R.layout.dmt_transaction_data_row_content,
                (itemView, item, position) -> {
                    TextView tvUTR = itemView.findViewById(R.id.tvUTR);
                    TextView tvOrderId = itemView.findViewById(R.id.tvOrderId);
                    TextView tvStatus = itemView.findViewById(R.id.tvStatus);
                    TextView tvAmount = itemView.findViewById(R.id.tvAmount);
                    tvUTR.setText(item.getUtr());
                    tvOrderId.setText(item.getOrderId());
                    tvStatus.setText(item.getStatus());
                    changeColor(item.getStatus(), tvStatus);
                    tvAmount.setText(MyUtil.formatWithRupee(this, item.getAmount()));
                }));
        String amount = "Total : " + MyUtil.formatWithRupee(this, getIntent().getStringExtra("amount"));
        tvTotal.setText(amount);
    }

    private void showBenList() {
        ArrayList<InvoiceModel> dataList = getIntent().getParcelableArrayListExtra("benData");
        RecyclerView recyclerView = findViewById(R.id.rvBenData);
        recyclerView.setAdapter(new GRAdapter<>(dataList, R.layout.dmt_invoice_row_layout,
                (itemView, item, position) -> {
                    TextView tvTitle = itemView.findViewById(R.id.title);
                    TextView tvValue = itemView.findViewById(R.id.tvValue);
                    tvTitle.setText(item.getKey());
                    tvValue.setText(item.getValue());
                }));
    }

    private void showAccountList() {
        ArrayList<InvoiceModel> dataList = getIntent().getParcelableArrayListExtra("accountData");
        RecyclerView recyclerView = findViewById(R.id.rvAccountData);
        recyclerView.setAdapter(new GRAdapter<>(dataList, R.layout.dmt_invoice_row_layout,
                (itemView, item, position) -> {
                    TextView tvTitle = itemView.findViewById(R.id.title);
                    TextView tvValue = itemView.findViewById(R.id.tvValue);
                    tvTitle.setText(item.getKey());
                    tvValue.setText(item.getValue());
                }));
    }

    private void changeColor(String status, TextView tvTxnStatus) {
        switch (status) {
            case "success":
            case "txn":
            case "TXN":
            case "approved":
                tvTxnStatus.setTextColor(context.getResources().getColor(R.color.textGreen));
                tvTxnStatus.setText("Success");
                break;
            case "pending":
            case "TUP":
                tvTxnStatus.setTextColor(context.getResources().getColor(R.color.text_yellow));
                tvTxnStatus.setText("Pending");
                break;
            case "rejected":
            case "failed":
            case "failure":
            case "fail":
            case "TXF":
            case "Failed":
                tvTxnStatus.setTextColor(context.getResources().getColor(R.color.textRed));
                tvTxnStatus.setText("Failed");
                break;
            default:
                tvTxnStatus.setTextColor(context.getResources().getColor(R.color.textBlack));
        }
    }
}


