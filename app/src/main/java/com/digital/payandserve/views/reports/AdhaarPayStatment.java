package com.digital.payandserve.views.reports;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.utill.SharedPrefs;
import com.digital.payandserve.views.reports.adapter.AdhaarPayStatmentAdapter;
import com.digital.payandserve.views.reports.model.AdharPayStatmentModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdhaarPayStatment extends AppCompatActivity implements RequestResponseLis {

    private static final int REQUEST_COUNT = 0;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private List<AdharPayStatmentModel> dataList;
    private RecyclerView listView;
    private AdhaarPayStatmentAdapter adapter;
    private int PAGE_COUNT = 1;
    private int MAX_PAGE_COUNT = 1;
    private String TITLE = "", TYPE = "";
    private boolean loading = true;

    private void init() {
        TITLE = getIntent().getStringExtra("title");
        TYPE = getIntent().getStringExtra("type");
        listView = findViewById(R.id.reportList);
        dataList = new ArrayList<>();
        initReportSlider();
        initToolbar();
    }

    private void initToolbar() {
        TextView tvTitle = findViewById(R.id.toolbarTitle);
        tvTitle.setText(TITLE);
        LinearLayout imgPdf = findViewById(R.id.imgPdfCon);
        LinearLayout imgBackCon = findViewById(R.id.imgBackCon);
        LinearLayout imgFilter = findViewById(R.id.imgFilterCon);
        imgFilter.setOnClickListener(v -> {
            Intent i = new Intent(AdhaarPayStatment.this, FilterView.class);
            startActivity(i, ActivityOptions.makeSceneTransitionAnimation(AdhaarPayStatment.this).toBundle());
        });

//        imgPdf.setOnClickListener(v -> initReportPDFData());
        imgBackCon.setOnClickListener(v -> finish());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.explodeAnimation(this);
        clearFilter();

        setContentView(R.layout.activity_aeps_report_list);
        init();
        networkCallUsingVolleyApi(Constants.URL.REPORT, true);

    }

    private void initReportSlider() {
        adapter = new AdhaarPayStatmentAdapter(this, dataList);
        LinearLayoutManager mLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(adapter);

        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            if (PAGE_COUNT < MAX_PAGE_COUNT) {
                                PAGE_COUNT++;
                                loadMoreLoader(true);
                                networkCallUsingVolleyApi(Constants.URL.REPORT, false);
                            }
                            loading = true;
                        }
                    }
                }
            }
        });
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
        map.put("type", TYPE);
        map.put("start", String.valueOf(PAGE_COUNT));
        // add report filter
        String fromDate = SharedPrefs.getValue(this, SharedPrefs.FILTER_DATE_FROM);
        String toDate = SharedPrefs.getValue(this, SharedPrefs.FILTER_DATE_TO);
        String search = SharedPrefs.getValue(this, SharedPrefs.REPORT_SEARCH_TEXT);
        String status = SharedPrefs.getValue(this, SharedPrefs.FILTER_STATUS);
        Print.P("From " + fromDate + " To : " + toDate + " Search: " + search + " status" + status);
        if (MyUtil.isNN(fromDate))
            map.put("fromdate", fromDate);
        if (MyUtil.isNN(toDate))
            map.put("todate", toDate);
        if (MyUtil.isNN(search))
            map.put("searchtext", search);
        if (MyUtil.isNN(status))
            map.put("status", status);
        return map;
    }


    @Override
    public void onSuccessRequest(String JSonResponse) {
        loadMoreLoader(false);

        try {
            JSONObject object = new JSONObject(JSonResponse);
            if (object.has("data")) {
                int oldDataSize = dataList.size();
                JSONArray idStockArray = object.getJSONArray("data");
                MAX_PAGE_COUNT = object.getInt("pages");
                dataList.addAll(AppHandler.parseAadharPayList(idStockArray));
                adapter.notifyDataSetChanged();
                listView.setVerticalScrollbarPosition(oldDataSize);
                if (dataList.size() == 0) {
                    errorView("Sorry Records are not available !");
                } else {
                    listView.setVisibility(View.VISIBLE);
                    findViewById(R.id.noData).setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            errorView(AppHandler.parseExceptionMsg(e));
        }
    }

    @Override
    public void onFailRequest(String msg) {
        errorView(msg);
    }

    private void loadMoreLoader(boolean flag) {
        if (flag) {
            findViewById(R.id.loader).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.loader).setVisibility(View.GONE);
        }
    }

    private void errorView(String string) {
        TextView tvMsg = findViewById(R.id.tvMsg);
        tvMsg.setText(string);
        listView.setVisibility(View.GONE);
        findViewById(R.id.noData).setVisibility(View.VISIBLE);
    }
/*

    private void initReportPDFData() {
        String[] keys = {"ID", "Account", "Bank", "IFSC", "Amount", "Type", "Pay Type", "Payout Id", "Status"};
        List<List<InvoiceModel>> modelList = new ArrayList<>();
        for (AdharPayStatmentModel model : dataList) {
            List<InvoiceModel> invoiceModels = new ArrayList<>();
            invoiceModels.add(new InvoiceModel("Id", model.getId()));
            invoiceModels.add(new InvoiceModel("Account", model.getAccount()));
            invoiceModels.add(new InvoiceModel("Bank", model.getBank()));
            invoiceModels.add(new InvoiceModel("IFSC", model.getIfsc()));
            invoiceModels.add(new InvoiceModel("Amount", MyUtil.formatWithRupee(this, model.getAmount())));
            invoiceModels.add(new InvoiceModel("Type", model.getType()));
            invoiceModels.add(new InvoiceModel("Pay Type", model.getPayType()));
            invoiceModels.add(new InvoiceModel("Payout Id", model.getPayoutid()));
            invoiceModels.add(new InvoiceModel("Status", model.getStatus()));
            modelList.add(invoiceModels);
        }
        Constants.REPORT_PDF_MODEL = new ReportPDFModel(keys, modelList);
        Intent i = new Intent(this, PdfManager2.class);
        i.putExtra("title", TITLE);
        startActivity(i);
    }
*/

    @Override
    protected void onResume() {
        super.onResume();
        if (Constants.IS_RELOAD_REQUEST) {
            Constants.IS_RELOAD_REQUEST = false;
            PAGE_COUNT = 1;
            MAX_PAGE_COUNT = 1;
            dataList.clear();
            networkCallUsingVolleyApi(Constants.URL.REPORT, true);
        }
    }

    private void clearFilter() {
        SharedPrefs.setValue(this, SharedPrefs.FILTER_DATE_FROM, "");
        SharedPrefs.setValue(this, SharedPrefs.FILTER_DATE_TO, "");
        SharedPrefs.setValue(this, SharedPrefs.REPORT_SEARCH_TEXT, "");
        SharedPrefs.setValue(this, SharedPrefs.FILTER_STATUS, "");
    }
}
