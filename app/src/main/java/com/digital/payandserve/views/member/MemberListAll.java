package com.digital.payandserve.views.member;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.digital.payandserve.views.member.adapter.AllMemberAdapter;
import com.digital.payandserve.views.member.model.AppMember;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberListAll extends AppCompatActivity implements RequestResponseLis, AllMemberAdapter.ItemClick {

    private static final int REQUEST_COUNT = 0;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private List<AppMember> dataList;
    private RecyclerView listView;
    private AllMemberAdapter adapter;
    private int PAGE_COUNT = 1;
    private int MAX_PAGE_COUNT = 1;
    private String TYPE = "";
    private boolean loading = true;
    private Button btnAddMember;
    private int REQUEST_TYPE = 0;
    private String amount = "";
    private String remark = "";
    private String id = "";

    private void init() {
        TYPE = getIntent().getStringExtra("type");
        listView = findViewById(R.id.reportList);
        btnAddMember = findViewById(R.id.btnAddMember);
        dataList = new ArrayList<>();
        initReportSlider();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.explodeAnimation(this);
        setContentView(R.layout.add_member_list);
        init();

        REQUEST_TYPE = 0;
        networkCallUsingVolleyApi(Constants.URL.ALL_MEMBERS, true);

        btnAddMember.setOnClickListener(v -> {
            startActivity(new Intent(MemberListAll.this, AddMember.class));
        });
    }

    private void initReportSlider() {
        adapter = new AllMemberAdapter(this, dataList, this);
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
                                REQUEST_TYPE = 0;
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
        switch (REQUEST_TYPE) {
            case 0:
                map.put("type", TYPE);
                map.put("start", String.valueOf(PAGE_COUNT));
                break;
            case 1:
                map.put("type", "return");
                break;
            case 2:
                map.put("type", "transfer");
                break;
        }
        if (MyUtil.isNN(amount))
            map.put("amount", amount);
        if (MyUtil.isNN(remark))
            map.put("remark", remark);

        if (MyUtil.isNN(id)) map.put("id", id);

        return map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        loadMoreLoader(false);
        try {
            JSONObject object = new JSONObject(JSonResponse);
            switch (REQUEST_TYPE) {
                case 0:
                    if (object.has("data")) {
                        int oldDataSize = dataList.size();
                        JSONArray bannerArray = object.getJSONArray("data");
                        MAX_PAGE_COUNT = object.getInt("pages");
                        dataList.addAll(AppHandler.parseMemberRecord(this, bannerArray));
                        adapter.notifyDataSetChanged();
                        listView.setVerticalScrollbarPosition(oldDataSize);
                        if (dataList.size() == 0) {
                            errorView("Sorry Records are not available !");
                        } else {
                            listView.setVisibility(View.VISIBLE);
                            findViewById(R.id.noData).setVisibility(View.GONE);
                        }
                    }
                    break;
                default:
                    String message = AppHandler.getMessage(JSonResponse);
                    Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            errorView(AppHandler.parseExceptionMsg(e));
        }
    }

    @Override
    public void onFailRequest(String msg) {
        //errorView(msg);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (Constants.IS_RELOAD_REQUEST) {
            Constants.IS_RELOAD_REQUEST = false;
            PAGE_COUNT = 1;
            MAX_PAGE_COUNT = 1;
            dataList.clear();
            REQUEST_TYPE = 0;
            networkCallUsingVolleyApi(Constants.URL.REPORT, true);
        }
    }

    @Override
    public void clickRec(AppMember model) {
        REQUEST_TYPE = 1;
        id = model.getId();
        transferDialog("Return Fund");
    }

    @Override
    public void clickSend(AppMember model) {
        REQUEST_TYPE = 2;
        id = model.getId();
        transferDialog("Transfer Fund");
    }

    AlertDialog alertDialog;

    protected void transferDialog(String str) {
        LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2 = inflater2.inflate(R.layout.member_fund_popup, null);
        final EditText etAmount = v2.findViewById(R.id.etAmount);
        final EditText etRemark = v2.findViewById(R.id.etRemark);
        final ImageView imgClose = v2.findViewById(R.id.imgClose);
        final Button btnSubmit = v2.findViewById(R.id.btnSubmit);
        btnSubmit.setText(str);
        btnSubmit.setOnClickListener(v -> {
            if (AppManager.isOnline(MemberListAll.this)) {
                if (etAmount.getText().toString().equals("")) {
                    Toast.makeText(this, "Amount field is required", Toast.LENGTH_SHORT).show();
                } else {
                    amount = etAmount.getText().toString();
                    remark = etRemark.getText().toString();
                    networkCallUsingVolleyApi(Constants.URL.FUND_REQUEST, true);
                }
            } else {
                Toast.makeText(MemberListAll.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(MemberListAll.this);
        builder2.setCancelable(false);
        builder2.setView(v2);
        alertDialog = builder2.create();
        imgClose.setOnClickListener(v -> alertDialog.dismiss());
        alertDialog.show();
    }
}
