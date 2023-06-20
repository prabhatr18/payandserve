package com.digital.payandserve.views.reports;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.dataModel.MainLocalData;
import com.digital.payandserve.model.ActivityListModel;
import com.digital.payandserve.utill.RecyclerTouchListener;
import com.digital.payandserve.utill.SharedPrefs;
import com.digital.payandserve.views.reports.adapter.VerticalReportListAdapter;

public class AllReports extends AppCompatActivity {

    String matm;
    private String TITLE = "";
    private RecyclerView rvReport;

    private void init() {
        TITLE = getIntent().getStringExtra("title");
        rvReport = findViewById(R.id.reportList);
        initToolbar();
    }

    private void initToolbar() {
        TextView tvTitle = findViewById(R.id.toolbarTitle);
        //tvTitle.setText(TITLE);
        ImageView imgBack = findViewById(R.id.imgClose);
        imgBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_report_list);
        init();
        gridInitReport();

    }

    private void gridInitReport() {
        LinearLayoutManager nearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvReport.setLayoutManager(nearLayoutManager);
        rvReport.setItemAnimator(new DefaultItemAnimator());
        matm = SharedPrefs.getValue(this, SharedPrefs.MICRO_ATM_BALANCE);
        VerticalReportListAdapter reportAdapter = new VerticalReportListAdapter(AllReports.this,
                MainLocalData.getReportAllGridRecord(matm));
        rvReport.setAdapter(reportAdapter);
        rvReport.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                rvReport, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String v = MainLocalData.getReportAllGridRecord(matm).get(position).getTxt1();
                Intent intent = null;
                switch (v) {
                    case "Kiosk Banking Statement":
                        intent = new Intent(AllReports.this, AEPSTransaction.class);
                        intent.putExtra("title", "Kiosk Banking Statement");
                        intent.putExtra("type", "aepsstatement");
                        startActivity(intent);
                        break;
                    case "Billpay Statement":
                        intent = new Intent(AllReports.this, BillRechargeTransaction.class);
                        intent.putExtra("title", "Billpay Statement");
                        intent.putExtra("type", "billpaystatement");
                        startActivity(intent);
                        break;
                    case "Money Transfer Statement":
                        intent = new Intent(AllReports.this, DMTTransactionReport.class);
                        intent.putExtra("type", "dmtstatement");
                        intent.putExtra("title", "Money Transfer Statement");
                        startActivity(intent);
                        break;
                    case "Recharge Statement":
                        intent = new Intent(AllReports.this, BillRechargeTransaction.class);
                        intent.putExtra("type", "rechargestatement");
                        intent.putExtra("title", "Recharge Statement");
                        startActivity(intent);
                        break;
                    case "Card Statement":
                        intent = new Intent(AllReports.this, AEPSTransaction.class);
                        intent.putExtra("type", "matmstatement");
                        intent.putExtra("title", "Card Statement");
                        startActivity(intent);
                        break;
                    case "Wallet Balance":
                        intent = new Intent(AllReports.this, WalletStatement.class);
                        intent.putExtra("type", "accountstatement");
                        intent.putExtra("title", "Wallet Balance");
                        startActivity(intent);
                        break;
                    case "Kiosk Balance":
                        intent = new Intent(AllReports.this, AepsWalletStatement.class);
                        intent.putExtra("type", "awalletstatement");
                        intent.putExtra("title", "Kiosk Balance");
                        startActivity(intent);
                        break;
                    case "Card Balance":
                        intent = new Intent(AllReports.this, AepsWalletStatement.class);
                        intent.putExtra("type", "matmwalletstatement");
                        intent.putExtra("title", "Card Balance");
                        startActivity(intent);
                        break;
                    case "Kuber Balance":
                        intent = new Intent(AllReports.this, AepsWalletStatement.class);
                        intent.putExtra("type", "kuberwalletstatement");
                        intent.putExtra("title", "Kuber Balance");
                        startActivity(intent);
                        break;
                    case "Wallet Balance settlement":
                        intent = new Intent(AllReports.this, MainWalletFundReqStatement.class);
                        intent.putExtra("type", "fundrequest");
                        intent.putExtra("title", "Wallet Balance settlement");
                        startActivity(intent);
                        break;
                    case "Kiosk Balance settlement":
                        intent = new Intent(AllReports.this, AEPSFundRequest.class);
                        intent.putExtra("type", "aepsfundrequest");
                        intent.putExtra("title", "Kiosk Balance settlement");
                        startActivity(intent);
                        break;
                    case "Kuber Balance Settlement":
                        intent = new Intent(AllReports.this, AEPSFundRequest.class);
                        intent.putExtra("type", "kuberfundrequest");
                        intent.putExtra("title", "Kuber Balance Settlement");
                        startActivity(intent);
                        break;
                    case "Card Balance settlement":
                        intent = new Intent(AllReports.this, AEPSFundRequest.class);
                        intent.putExtra("type", "matmfundrequest");
                        intent.putExtra("title", "Card Balance settlement");
                        startActivity(intent);
                        break;
                    case "Self Shopping Report":
                        intent = new Intent(AllReports.this, BuyProductsHistory.class);
                        intent.putExtra("type", "matmbuystatement");
                        intent.putExtra("title", "Self Shopping Report");
                        startActivity(intent);
                        break;
                    case "Ticket Status":
                        intent = new Intent(AllReports.this, SupporTicektHistory.class);
                        intent.putExtra("type", "complainlist");
                        intent.putExtra("title", "Ticket Status");
                        startActivity(intent);
                        break;

                    case "Id Stock Report":
                        intent = new Intent(AllReports.this, IdStockReport.class);
                        intent.putExtra("type", "idstockstatement");
                        intent.putExtra("title", "Id Stock Report");
                        startActivity(intent);
                        break;

                    case "Cashback Statement":
                        intent = new Intent(AllReports.this, CashbackStatmentReport.class);
                        intent.putExtra("type", "cashbackstatement");
                        intent.putExtra("title", "Cashback Statement");
                        startActivity(intent);
                        break;

                    case "Aadhaar Pay Statement":
                        intent = new Intent(AllReports.this, AdhaarPayStatment.class);
                        intent.putExtra("type", "aadharpestatement");
                        intent.putExtra("title", "Aadhaar Pay Statement");
                        startActivity(intent);
                        break;
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

}
