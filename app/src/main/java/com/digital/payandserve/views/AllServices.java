package com.digital.payandserve.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.adapter.HomeAdapter;
import com.digital.payandserve.adapter.VerticalHeaderAdapter;
import com.digital.payandserve.dataModel.MainLocalData;
import com.digital.payandserve.model.ActivityListModel;
import com.digital.payandserve.utill.RecyclerTouchListener;
import com.digital.payandserve.utill.SharedPrefs;
import com.digital.payandserve.views.billpayment.RechargeAndBillPayment;
import com.digital.payandserve.views.mhagram_aeps_matm.HandlerActivity;
import com.digital.payandserve.views.moneytransfer.DMTSearchAccount;

import java.util.List;

public class AllServices extends AppCompatActivity {
    private RecyclerView rvHome, headerList;
    private TextView tvBack;
    private Context context;

    private void init() {
        context = AllServices.this;
        headerList = findViewById(R.id.headerList);
        rvHome = findViewById(R.id.rvHome);
        tvBack = findViewById(R.id.tvBack);
        gridInit();
        topHeaderList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_services);
        init();
        animateRightGrid();
        tvBack.setOnClickListener(v -> finish());
    }

    // animation side panel
    private void animateRightGrid() {
        LinearLayout iconsGroup = findViewById(R.id.iconsGroup);
        Animation slideAnimation = AnimationUtils.loadAnimation(this, R.anim.left_to_right);
        iconsGroup.startAnimation(slideAnimation);
    }

    private void gridInit() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(AllServices.this, 3);
        rvHome.setLayoutManager(layoutManager);
        rvHome.setItemAnimator(new DefaultItemAnimator());
        // init homeMenu Grid
        final List<ActivityListModel> data = MainLocalData.getHomeGridAllRecord(this);
        //data.remove(0);
        //data.remove(0);
        HomeAdapter homeAdapter = new HomeAdapter(AllServices.this, data);
        rvHome.setAdapter(homeAdapter);
        rvHome.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                rvHome, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(AllServices.this, RechargeAndBillPayment.class);
                i.putExtra("position", data.get(position).getPosition());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void topHeaderList() {
        LinearLayoutManager nearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        headerList.setLayoutManager(nearLayoutManager);
        headerList.setItemAnimator(new DefaultItemAnimator());
        VerticalHeaderAdapter homeAdapter = new VerticalHeaderAdapter(AllServices.this, MainLocalData.getMoneyTransferGrid());
        headerList.setAdapter(homeAdapter);
        headerList.addOnItemTouchListener(new RecyclerTouchListener(this,
                headerList, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i;
                ActivityListModel model = MainLocalData.getMoneyTransferGrid().get(position);
                switch (model.getTxt1()) {
                    case "AEPS":
                        i = new Intent(AllServices.this, HandlerActivity.class);
                        i.putExtra("appUserId", SharedPrefs.getValue(AllServices.this, SharedPrefs.USER_ID));
                        i.putExtra("type", "aeps");
                        startActivityForResult(i, 5000);
                        break;
                    case "MATM":
                        i = new Intent(AllServices.this, HandlerActivity.class);
                        i.putExtra("appUserId", SharedPrefs.getValue(AllServices.this, SharedPrefs.USER_ID));
                        i.putExtra("type", "matm");
                        startActivityForResult(i, 5000);
                        break;
                    case "DMT":
                        startActivity(new Intent(context, DMTSearchAccount.class));
                        break;
                    case "PHONE":
                        i = new Intent(context, RechargeAndBillPayment.class);
                        i.putExtra("position", "0");
                        startActivity(i);
                        break;
                    case "DTH":
                        i = new Intent(context, RechargeAndBillPayment.class);
                        i.putExtra("position", "1");
                        startActivity(i);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }
}