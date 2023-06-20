package com.digital.payandserve.views.ourproducts;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.digital.payandserve.views.reports.model.BuyProductModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BuyProductsActivity extends AppCompatActivity
        implements RequestResponseLis, BuyProductAdapter.OnBuyClickListener {

    AlertDialog alert;
    int REQUEST_CODE = 0;
    String number, price, deviceType;
    RecyclerView productRecycler;
    BuyProductAdapter adapter;
    ArrayList<BuyProductListModel> productList;

    private void init() {
        productRecycler = findViewById(R.id.productRecycler);

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.transparentStatusBar(this);
        setContentView(R.layout.buyproduct_activity);
        init();

        REQUEST_CODE = 0;
        volleyNetworkCall(Constants.URL.PRODUCT_LIST);
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        Gson gson = new GsonBuilder().create();
        System.out.println("RES: " + JSonResponse);
        Log.d("RES", JSonResponse);
        try {
            JSONObject jsonObject = new JSONObject(JSonResponse);

            if (REQUEST_CODE == 0) {
                String imagePath = jsonObject.getString("imagepath");
                JSONArray dataArray = jsonObject.getJSONArray("data");
                productList = new ArrayList<>();
                productList.addAll(AppHandler.parseProductList(dataArray));
                adapter = new BuyProductAdapter(productList, imagePath, this);
                productRecycler.setAdapter(adapter);
            }

            if (REQUEST_CODE == 1) {
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("message");
                popUp(status, message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void popUp(String status, String message) {
        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setTitle(status)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> {
                    dialog.dismiss();
                    finish();
                });
        alert = builder.create();
        alert.setCancelable(false);
        alert.show();
    }

    @Override
    public void onFailRequest(String msg) {
//        Toast.makeText(context, "Network Request Error : " + msg, Toast.LENGTH_SHORT).show();
    }

    private Map<String, String> param() {
        String userId = SharedPrefs.getValue(this, SharedPrefs.USER_ID);
        String token = SharedPrefs.getValue(this, SharedPrefs.APP_TOKEN);
        Map<String, String> map = new HashMap<>();
        map.put("apptoken", token);
        map.put("user_id", userId);
        if (REQUEST_CODE == 1) {
            map.put("number", number);
            map.put("price", price);
            map.put("device_type", deviceType);
        }

        String json = new JSONObject(map).toString();
        Print.P("PARAM : " + json);
        return map;
    }

    private void volleyNetworkCall(String url) {
        if (AppManager.isOnline(this)) {
            new VolleyNetworkCall(this, this, url, 1, param()).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBuyBtnClicked(int position, BuyProductListModel data) {
        REQUEST_CODE = 1;
        number = String.valueOf(data.getBuyingQty());
        price = String.valueOf(data.getBuyingPrice());
        deviceType = data.getDevice_type();
        volleyNetworkCall(Constants.URL.BUY_PRODUCT);
    }
}
