package com.digital.payandserve.views.browseplan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.utill.SharedPrefs;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.views.browseplan.adapter.PlanAdapter;
import com.digital.payandserve.views.browseplan.listen.PlanSelectorLis;
import com.digital.payandserve.views.browseplan.model.DataItem;
import com.digital.payandserve.views.browseplan.model.GenericModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class ViewPlans extends AppCompatActivity implements RequestResponseLis,
        PlanSelectorLis {

    private RecyclerView planListView;
    private List<DataItem> planDataList, planDataListFilter;
    private List<String> titleList;
    private PlanAdapter adapterPlan;
    private BottomSheetBehavior<CardView> sheetBehavior;
    private String planValue, planDetail;
    private String OPERATOR, CONTACT, VALIDITY, TYPE, OPERATOR_NAME;
    private TextView tvPlan, tvDetail;
    private Button btnSelect;
    private ImageView imgProvider;
    private TabLayout tabLayout;
    private EditText etSearch;
    String  circle;


    private void init() {
        tabLayout = findViewById(R.id.tab);
        etSearch = findViewById(R.id.etSearch);
        imgProvider = findViewById(R.id.imgProvider);
        planListView = findViewById(R.id.planList);
        planDataList = new ArrayList<>();
        planDataListFilter = new ArrayList<>();
        titleList = new ArrayList<>();
        CardView bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        TextView forPlan = findViewById(R.id.forPlan);
        ImageView imgClose = findViewById(R.id.imgClose);
        imgClose.setOnClickListener(v -> finish());

        TYPE = getIntent().getStringExtra("type");
        CONTACT = getIntent().getStringExtra("mobile");
        circle = getIntent().getStringExtra("circle");
        // Toast.makeText(this, ""+CONTACT, Toast.LENGTH_SHORT).show();
        //  OPERATOR = getIntent().getStringExtra("provider_id");
        //  OPERATOR_NAME = getIntent().getStringExtra("provider_name");
        OPERATOR_NAME = getIntent().getStringExtra("providername");

        forPlan.setText(OPERATOR_NAME);
        // MyUtil.setStaticProviderImg(OPERATOR_NAME, imgProvider, this, TYPE);
        Constants.GLOBAL_POSITION = 0;
    }

    @SuppressLint("SetTextI18n")
    private void init(View view) {
        tvPlan = view.findViewById(R.id.tvPrice);
        tvDetail = view.findViewById(R.id.tvDetail);
        btnSelect = view.findViewById(R.id.btnSelect);
        String p = ViewPlans.this.getString(R.string.rupee) + planValue;
        tvPlan.setText(p);
        if (TYPE.equalsIgnoreCase("mobile"))
            tvDetail.setText(planDetail + "\n\nValidity - " + VALIDITY);
        else
            tvDetail.setText(planDetail + "\n\nPlan - " + VALIDITY);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MyUtil.whiteStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_plan);
        init();
        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                init(view);
                btnSelect.setOnClickListener(v -> {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("amount", planValue);
                    setResult(121, returnIntent);
                    finish();
                });

                ImageView btnCancel = view.findViewById(R.id.imgCancel);
                btnCancel.setOnClickListener(v -> {
                    findViewById(R.id.disableCon).setVisibility(View.GONE);
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                });
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    findViewById(R.id.disableCon).setVisibility(View.GONE);
                }
                if (v == 0.0) {
                    findViewById(R.id.disableCon).setVisibility(View.GONE);
                }
            }
        });

        String typeoffer = getIntent().getStringExtra("offertype");
        if (MyUtil.isNN(typeoffer)) {
       //     networkCallUsingVolleyApi(Constants.URL.R_OFFER, true);
        } else {
            networkCallUsingVolleyApi(Constants.URL.GET_PLAN, true);

        }
        // networkCallUsingVolleyApi(Constants.URL.GET_PLAN, true);
    }

    private void initTitleSlider() {
        for (String s : titleList) {
            tabLayout.addTab(tabLayout.newTab().setText(s));
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        LinearLayoutManager layoutManager = new LinearLayoutManager((Context) this, LinearLayoutManager.VERTICAL, false);
        planListView.setLayoutManager(layoutManager);
        reloadList(0);
        ontitleItemClick();
    }

    void reloadList(int index) {
        planDataListFilter.clear();
        String key = titleList.get(index);
        for (DataItem model : planDataList) {
            if (model.getParentText().equalsIgnoreCase(key)) {
                planDataListFilter.add(model);
            }
        }
        adapterPlan = new PlanAdapter(planDataListFilter, this, this, TYPE);
        planListView.setAdapter(adapterPlan);
        adapterPlan.notifyDataSetChanged();
    }

    public void ontitleItemClick() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                reloadList(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onButtonSelect(int p, DataItem model) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("amount", model.getAmount());
        setResult(121, returnIntent);
        finish();
    }

    @Override
    public void onImgExpand(int p, DataItem model) {
        planValue = model.getAmount();
        planDetail = model.getDetail();
        VALIDITY = model.getValidity();
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            findViewById(R.id.disableCon).setVisibility(View.VISIBLE);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            findViewById(R.id.disableCon).setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        try {
            if (JSonResponse.contains("Plan Not Available")) {
                errorView("Sorry plans are not available");
            } else {
                JSONObject jsonObjectMain = new JSONObject(JSonResponse);
                GenericModel model = AppHandler.parse(jsonObjectMain);
                titleList = model.getKeyList();
                planDataList = model.getDataList();
                initTitleSlider();
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorView("Sorry something went wrong please try again after some time");
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Toast.makeText((Context) this, "" + msg, Toast.LENGTH_SHORT).show();
    }

    private void networkCallUsingVolleyApi(String url, boolean isLoad) {
        if (AppManager.isOnline(this)) {
            new VolleyNetworkCall(this, this, url, 1, param(), isLoad).netWorkCall();
        } else {
            Toast.makeText((Context) this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {

        Map<String, String> map = new HashMap<>();
        map.put("user_id", SharedPrefs.getValue(getApplicationContext(),SharedPrefs.USER_ID));
        map.put("providername", OPERATOR_NAME);
        map.put("number", CONTACT);
        map.put("type", TYPE);
        map.put("circle", ""+circle);
        map.put("apptoken", SharedPrefs.getValue(getApplicationContext(),SharedPrefs.APP_TOKEN));
        // map.put("provider_id", getIntent().getStringExtra("provider_id"));

        return map;
    }

    private void errorView(String string) {
        TextView tvMsg = findViewById(R.id.tvMsg);
        tvMsg.setText(string);
        planListView.setVisibility(View.GONE);
        findViewById(R.id.noData).setVisibility(View.VISIBLE);
    }

}
