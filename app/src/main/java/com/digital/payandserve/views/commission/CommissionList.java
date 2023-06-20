package com.digital.payandserve.views.commission;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.views.commission.gsonModel.CommissionModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CommissionList extends AppCompatActivity implements RequestResponseLis {
    ArrayList<String> keyList;
    ListView listView;
    private AlertDialog loaderDialog;
    private CommissionServiceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commition_list);
        Constants.commissionDataList = new ArrayList<>();
        initListView();

        networkCallUsingVolleyApi(Constants.URL.GET_COMMISSION, true);
    }

    private void initListView() {
        keyList = new ArrayList<>();
        listView = findViewById(R.id.listView);
        adapter = new CommissionServiceAdapter(this, keyList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CommissionList.this, CommissionDataList.class);
                intent.putExtra("key", keyList.get(i));
                startActivity(intent);
            }
        });
    }

    private void closeLoader() {
        if (loaderDialog != null && loaderDialog.isShowing()) {
            loaderDialog.dismiss();
        }
    }

    private void networkCallUsingVolleyApi(String url, boolean isLoad) {
        if (AppManager.isOnline(this)) {
            new VolleyNetworkCall(this, this, url, 1, param(), true).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        return map;
    }


    @Override
    public void onSuccessRequest(String JSonResponse) {
        Constants.commissionDataList.clear();
        keyList.clear();
        Gson gson = new GsonBuilder().create();
        closeLoader();
        try {
            JSONObject jsonObject = new JSONObject(JSonResponse);
            JSONObject commissionObj = jsonObject.getJSONObject("commission");
            Iterator<String> keys = commissionObj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONArray keyArray = commissionObj.getJSONArray(key);
                for (int i = 0; i < keyArray.length(); i++) {
                    JSONObject obj = keyArray.getJSONObject(i);
                    CommissionModel model = gson.fromJson(obj.toString(), CommissionModel.class);
                    model.setKeys(key);
                    Constants.commissionDataList.add(model);
                }
                keyList.add(key);
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        closeLoader();
        Toast.makeText(this, "Error: " + msg, Toast.LENGTH_SHORT).show();
    }
}