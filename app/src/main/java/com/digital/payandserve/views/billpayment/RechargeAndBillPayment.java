package com.digital.payandserve.views.billpayment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.RecyclerTouchListener;
import com.digital.payandserve.utill.SpeedyLinearLayoutManager;
import com.digital.payandserve.views.billpayment.adapter.TopHeadBillPaySlider;
import com.digital.payandserve.views.fragment.FragRechangeMobileFirst;
import com.digital.payandserve.views.operator.OperatorListFrag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RechargeAndBillPayment extends AppCompatActivity {

    public static boolean active = false;
    private RecyclerView listView;
    private List<String> tabData;
    private TopHeadBillPaySlider adapter;
    private String SELECTED_POSITION = "0";

    private void init() {
        listView = findViewById(R.id.tab);
        tabData = new ArrayList<>();
        ImageView imgClose = findViewById(R.id.icBack);
        imgClose.setOnClickListener(v -> finish());
        initReportSlider();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.explodeAnimation(this);
        MyUtil.whiteStatusBar(this);
        if (getIntent().getStringArrayExtra("position") == null)
            SELECTED_POSITION = getIntent().getStringExtra("position");
        setContentView(R.layout.recharge_and_bill_payment);
        init();
    }

    private void initReportSlider() {
        String[] service_array = getResources().getStringArray(R.array.bill_payment_services);
        tabData = Arrays.asList(service_array);
        adapter = new TopHeadBillPaySlider(this, tabData);
        listView.setLayoutManager(new SpeedyLinearLayoutManager(this, SpeedyLinearLayoutManager.HORIZONTAL, false));
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(adapter);
        listView.scrollToPosition(Integer.parseInt(SELECTED_POSITION));
        setFrag(SELECTED_POSITION);
        Constants.GLOBAL_POSITION = Integer.parseInt(SELECTED_POSITION);
        adapter.notifyDataSetChanged();
        listView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                listView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Constants.GLOBAL_POSITION = position;
                adapter.notifyDataSetChanged();
                setFrag(String.valueOf(position));
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.disallowAddToBackStack();
        transaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    private void setFrag(String position) {
        switch (position) {
            case "0":
                loadFragment(new FragRechangeMobileFirst());
                break;
            case "1":
                loadFragment(new OperatorListFrag("dth"));
                break;
            case "2":
                loadFragment(new OperatorListFrag("electricity"));
                break;
            case "3":
                loadFragment(new OperatorListFrag("lpggas"));
                break;
            case "4":
                loadFragment(new OperatorListFrag("landline"));
                break;
            case "5":
                loadFragment(new OperatorListFrag("broadband"));
                break;
            case "6":
                loadFragment(new OperatorListFrag("water"));
                break;
            case "7":
                loadFragment(new OperatorListFrag("loanrepay"));
                break;
            case "8":
                loadFragment(new OperatorListFrag("lifeinsurance"));
                break;
            case "9":
                loadFragment(new OperatorListFrag("fasttag"));
                break;
            case "10":
                loadFragment(new OperatorListFrag("loanrepay"));
                break;
            case "11":
                loadFragment(new OperatorListFrag("cable"));
                break;
            case "12":
                loadFragment(new OperatorListFrag("insurance"));
                break;
            case "13":
                loadFragment(new OperatorListFrag("schoolfees"));
                break;
            case "14":
                loadFragment(new OperatorListFrag("muncipal"));
                break;
            case "15":
                loadFragment(new OperatorListFrag("postpaid"));
                break;
            case "16":
                loadFragment(new OperatorListFrag("housing"));
                break;
        }
    }
}
