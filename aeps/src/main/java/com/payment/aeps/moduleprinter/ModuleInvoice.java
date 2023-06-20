package com.payment.aeps.moduleprinter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.payment.aeps.R;
import com.payment.aeps.moduleprinter.adapter.InvoiceListAdapter;
import com.payment.aeps.moduleprinter.adapter.StatementListAdapter;
import com.payment.aeps.objectmodel.InvoiceModel;
import com.payment.aeps.objectmodel.StatementModel;
import com.payment.aeps.util.Print;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModuleInvoice extends AppCompatActivity implements View.OnClickListener {

    private NestedScrollView scrollView;
    private Context context;
    private ImageView imgShare, imgPrint;
    private ImageView imgTxnStatus;
    private TextView tvTxnStatus, tvValue;
    private Button btnLog;
    private String INVOICE_RESPONSE;
    private String TRANSACTION_TYPE;
    private List<InvoiceModel> dataList;
    final ArrayList<PrintDataModel> list2;
    final ArrayList<PrintDataModel> list = list2 = new ArrayList<>();

    private LinearLayout transactionCon;
    private ListView listView;
    private List<StatementModel> statementModelList;

    public void init() {
        statementModelList = new ArrayList<>();
        listView = findViewById(R.id.listView2);
        transactionCon = findViewById(R.id.transactionCon);

        imgShare = findViewById(R.id.imgShare);
        imgPrint = findViewById(R.id.imgPrint);
        scrollView = findViewById(R.id.scrollView);
        tvValue = findViewById(R.id.tvValue);
        imgTxnStatus = findViewById(R.id.imgTxnStatus);
        tvTxnStatus = findViewById(R.id.tvTxnStatus);
        btnLog = findViewById(R.id.btnLog);
        dataList = new ArrayList<>();
        setupToolBar("Transaction Invoice");
        setLis();
        permissionCheck();
    }

    private void setupToolBar(String title) {
        TextView tvToolbar = findViewById(R.id.tvToolBarTitle);
        tvToolbar.setText(title);
        ImageView backImage = findViewById(R.id.imgBack);
        backImage.setOnClickListener(view -> finish());
    }

    private void setLis() {
        imgShare.setOnClickListener(this);
        imgPrint.setOnClickListener(this);
        btnLog.setOnClickListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_invoice_layout);
        init();
        context = ModuleInvoice.this;
        INVOICE_RESPONSE = getIntent().getStringExtra("invoice");
        TRANSACTION_TYPE = getIntent().getStringExtra("type");
        if (INVOICE_RESPONSE != null && INVOICE_RESPONSE.length() > 0) {
            parseRes();
        } else {
            Toast.makeText(context, "Invalid Response", Toast.LENGTH_SHORT).show();
        }

        if (TRANSACTION_TYPE != null && TRANSACTION_TYPE.equals("MS")) {
            findViewById(R.id.bottom).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.bottom).setVisibility(View.GONE);
        }
        findViewById(R.id.bottom).setVisibility(View.GONE);
    }

    private void statusChange(String status) {
        if (status.equalsIgnoreCase("success") || status.equalsIgnoreCase("TXN")) {
            tvTxnStatus.setText("Transaction Successful");
            imgTxnStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));
        } else {
            tvTxnStatus.setText("Transaction Failed");
            imgTxnStatus.setImageDrawable(getResources().getDrawable(R.drawable.m_cancel));
        }
    }

    String message;
    private void parseRes() {
        try {
            JSONObject res = new JSONObject(INVOICE_RESPONSE);
            String status = "";
            if (res.has("status"))
                status = res.getString("status");
            else if (res.has("statuscode"))
                status = res.getString("statuscode");
            dataList.add(new InvoiceModel("Status", status));
            statusChange(status);

            if (res.has("rrn")) {
                String rrn = res.getString("rrn");
                dataList.add(new InvoiceModel("RRN", rrn));
            }

            if (res.has("id")) {
                String id = res.getString("id");
                dataList.add(new InvoiceModel("Transaction ID", id));
            }

            if (res.has("txnid")) {
                String id = res.getString("txnid");
                dataList.add(new InvoiceModel("Transaction ID", id));
            }

            if (res.has("transactionType")) {
                String transactionType = res.getString("transactionType");
                dataList.add(new InvoiceModel("Type", transactionType));
            }

            if (res.has("title")) {
                String title = res.getString("title");
                dataList.add(new InvoiceModel("Title", title));
            }

            if (res.has("aadhar")) {
                String aadhar = res.getString("aadhar");
                dataList.add(new InvoiceModel("Aadhaar No", aadhar));
            }

            if (res.has("balance")) {
                String balance = res.getString("balance");
                dataList.add(new InvoiceModel("Balance", balance));
            }

            if (res.has("amount")) {
                String amount = res.getString("amount");
                dataList.add(new InvoiceModel("Amount", amount));
            }

            if (res.has("bank")) {
                String bank = res.getString("bank");
                dataList.add(new InvoiceModel("Bank", bank));
            }

            if (res.has("benename")) {
                String benename = res.getString("benename");
                dataList.add(new InvoiceModel("Benename", benename));
            }

            if (res.has("account")) {
                String account = res.getString("account");
                dataList.add(new InvoiceModel("Account", account));
            }

            if (res.has("date")) {
                String date = res.getString("date");
                dataList.add(new InvoiceModel("Date", date));
            }

            message = res.getString("message");
            tvValue.setText(message);
            initTransactionList();

            if (TRANSACTION_TYPE != null && TRANSACTION_TYPE.equals("MS")) {
                try {
                    Gson gson = new GsonBuilder().create();
                    JSONArray jsonArray = res.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            StatementModel model = gson.fromJson(object.toString(), StatementModel.class);
                            statementModelList.add(model);
                            Print.P("OBJ : \n" + object.toString());
                        }
                        Print.P("RES : \n" + "initStatementList");
                        initStatementList();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void initTransactionList() {
        try {
            InvoiceListAdapter adapter = new InvoiceListAdapter(this, dataList);
            ListView listView = findViewById(R.id.listView);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(context, "Invoice Generation Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void initStatementList() {
        Print.P("RES : \n" + "initStatementList Called");
        transactionCon.setVisibility(View.VISIBLE);
        try {
            StatementListAdapter adapter = new StatementListAdapter(this, statementModelList);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(context, "Invoice Generation Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public final void l() {
        if (Build.VERSION.SDK_INT >= 23 && this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED
                && this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        } else
            new wd().NUL(this.scrollView, this.context);
    }

    public final void permissionCheck() {
        if (Build.VERSION.SDK_INT >= 23 && this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED
                && this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
    }

    public final void k() {
        for (InvoiceModel m : dataList) {
            list.add(new PrintDataModel(m.getKey(), m.getValue()));
        }
        final PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);

        final String string = "E Banker Document";
        String s = "EBanker";

        final PrintManager printManager2 = printManager;
        final String s2 = string;
        final ArrayList<PrintDataModel> list5 = list2;
        list5.add(new PrintDataModel("Remark", message));
        printManager2.print(s2, new ud(this, list2, s, "app_name"), null);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgPrint) {
            k();
        } else if (view.getId() == R.id.imgShare) {
            l();
        } else if (view.getId() == R.id.btnLog) {
            shareLog();
        }
    }

    private void shareLog() {
        try {
            File logFile2 = new File(getFiles());
            if (logFile2.exists()) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                emailIntent.setType("vnd.android.cursor.dir/email");
                String to[] = {"sumitkumarx95@gmail.com"};
                emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                ArrayList<Uri> outputFileUri = new ArrayList<Uri>();
                if ((android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)) {

                    if (logFile2.exists()) {
                        outputFileUri.add(FileProvider.getUriForFile(this, this.getPackageName() + ".files", logFile2));
                    }//  outputFileUri.add
                } else {

                    if (logFile2.exists()) {
                        outputFileUri.add(Uri.fromFile(logFile2));
                    }
                }
                emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, outputFileUri);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "App Error log");
                if (emailIntent.resolveActivity(this.getPackageManager()) != null) {
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                } else {
                    Toast.makeText(this, "No email application is available to share error log file", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(context, "Log files are not existed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFiles() {
        File sd = new File(Environment.getExternalStorageDirectory() + "/SEC2PAY/LOG/");
        File[] sdFileList = sd.listFiles();
        if (sdFileList != null && sdFileList.length > 1) {
            Arrays.sort(sdFileList, (object1, object2) -> (int) (Math.min(object2.lastModified(), object1.lastModified())));
            return sdFileList[sdFileList.length - 1].getAbsolutePath();
        } else if (sdFileList != null && sdFileList.length == 1) {
            return sdFileList[sdFileList.length - 1].getAbsolutePath();
        } else {
            return "";
        }
    }
}


