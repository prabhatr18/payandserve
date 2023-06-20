package com.digital.payandserve.views.operator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.GridSpacingItemDecoration;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.RecyclerTouchListener;
import com.digital.payandserve.views.billpayment.MobileRechargeAmountInput;
import com.digital.payandserve.views.operator.adapter.OperatorListAdapter;
import com.digital.payandserve.views.operator.model.OperatorModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperatorList extends AppCompatActivity implements RequestResponseLis {
    public Context context;
    private RecyclerView listView;
    private List<OperatorModel> dataListAll, dataList;
    private OperatorListAdapter adapter;
    private EditText etSearch;
    private String TYPE, Mobile;
    private ImageView icBack, imgProvider;

    private void init() {
        context = this;
        dataList = new ArrayList<>();
        dataListAll = new ArrayList<>();
        listView = findViewById(R.id.listView);
        imgProvider = findViewById(R.id.imgProvider);
        etSearch = findViewById(R.id.etSearch);
        icBack = findViewById(R.id.icBack);
        icBack.setOnClickListener(v -> finish());
        TYPE = getIntent().getStringExtra("type");
        Mobile = getIntent().getStringExtra("mobile");
        initList();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.whiteStatusBar(this);
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
                for (OperatorModel d : dataListAll) {
                    if (d.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                        dataList.add(d);
                    }
                }
                adapter.UpdateList(dataList);
            }
        });

        listView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                listView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                OperatorModel model = dataList.get(position);
                if (TYPE.equalsIgnoreCase("mobile")) {
                    Intent i = new Intent(OperatorList.this, MobileRechargeAmountInput.class);
                    i.putExtra("mobile", Mobile);
                    i.putExtra("type", "Phone");
                    i.putExtra("provider_name", model.getName());
                    i.putExtra("provider_id", model.getId());
                    i.putExtra("provider_image", model.getLogo());
                    startActivity(i);
                    finish();
                } else {
                    // Toast.makeText(context, model.getId()+":"+model.getMasterifsc(), Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("name", model.getName());
                    returnIntent.putExtra("id", model.getBankid());
                    returnIntent.putExtra("ifsc", model.getMasterifsc());
                    setResult(121, returnIntent);
                    finish();
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        if (TYPE.equalsIgnoreCase("getbank"))
            networkCallUsingVolleyApi(Constants.URL.DMT_TRANSACTION, true);
        else
            networkCallUsingVolleyApi(Constants.URL.PROVIDER, true);
    }

    private void networkCallUsingVolleyApi(String url, boolean isLoad) {
        if (AppManager.isOnline(this)) {
            new VolleyNetworkCall(this, this, url, 1, param(), isLoad).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private void initList() {
        adapter = new OperatorListAdapter(this, dataList, TYPE);
        if (TYPE.equals("getbank")) {
            LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            listView.setLayoutManager(mLayoutManager1);
        } else {
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
            listView.setLayoutManager(mLayoutManager);
            int i = AppManager.getInstance().dpToPx(2, this);
            listView.addItemDecoration(new GridSpacingItemDecoration(3, i, true));
        }
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(adapter);
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("type", TYPE);
        return map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        try {
            JSONObject object = new JSONObject(JSonResponse);
            if (object.has("data")) {
                JSONArray bannerArray = object.getJSONArray("data");
                dataList.addAll(AppHandler.parse(TYPE, bannerArray));
                Collections.sort(dataList, OperatorModel.comparator);
                dataListAll.addAll(dataList);
                adapter.UpdateList(dataList);
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

    private void errorView(String string) {
        TextView tvMsg = findViewById(R.id.tvMsg);
        tvMsg.setText(string);
        listView.setVisibility(View.GONE);
        findViewById(R.id.noData).setVisibility(View.VISIBLE);
    }
}
