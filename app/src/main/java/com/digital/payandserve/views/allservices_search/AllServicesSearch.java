package com.digital.payandserve.views.allservices_search;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.dataModel.MainLocalData;
import com.digital.payandserve.model.ActivityListModel;
import com.digital.payandserve.utill.RecyclerTouchListener;
import com.digital.payandserve.views.allservices_search.adapter.ServiceSearchAdapter;
import com.digital.payandserve.views.billpayment.RechargeAndBillPayment;

import java.util.ArrayList;
import java.util.List;

public class AllServicesSearch extends AppCompatActivity {
    public Context context;
    private Cursor cursor;
    private RecyclerView listView;
    private List<ActivityListModel> dataListAll, dataList;
    private ServiceSearchAdapter adapter;
    private EditText etSearch;

    private void init() {
        context = AllServicesSearch.this;
        dataList = new ArrayList<>();
        dataListAll = new ArrayList<>();
        listView = findViewById(R.id.listView);
        etSearch = findViewById(R.id.etSearch);
        ImageView icBack = findViewById(R.id.icBack);
        icBack.setOnClickListener(v -> finish());
        initList();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_with_list_activity);
        init();
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dataList = new ArrayList<>();
                for (ActivityListModel d : dataListAll) {
                    if (d.getTxt1().toLowerCase().contains(s.toString().toLowerCase())) {
                        dataList.add(d);
                    }
                }
                adapter.UpdateList(dataList);
            }
        });
    }

    // init homeMenu Grid
    private void initList() {
        dataList.addAll(MainLocalData.getHomeGridAllRecord(this));
        dataListAll.addAll(MainLocalData.getHomeGridAllRecord(this));
        LinearLayoutManager nearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(nearLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ServiceSearchAdapter(this, MainLocalData.getHomeGridAllRecord(this));
        listView.setAdapter(adapter);
        listView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                listView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(AllServicesSearch.this, RechargeAndBillPayment.class);
                i.putExtra("position", dataList.get(position).getPosition());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

}
