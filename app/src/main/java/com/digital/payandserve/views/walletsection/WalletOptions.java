package com.digital.payandserve.views.walletsection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.digital.payandserve.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.SharedPrefs;
import com.digital.payandserve.views.reports.AllReports;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WalletOptions extends AppCompatActivity implements RequestResponseLis {
    private LinearLayout walletSecCon;
    private TextView tvMatmBalance, tvMainBalance, tvAepsBalance, tvKuberBalance;
    private RelativeLayout shimmer;
    private Context context;
    private CarouselView rvSlider;
    private LinearLayout div1, div2, div3,div4;
    private ImageView imgAllReport;

    private void init() {
        context = this;
        findViewById(R.id.walletSecCon).setVisibility(View.GONE);
        tvMatmBalance = findViewById(R.id.tvMatmBalance);
        tvMainBalance = findViewById(R.id.tvMainBalance);
        tvAepsBalance = findViewById(R.id.tvAepsBalance);
        tvKuberBalance = findViewById(R.id.tvKuberBalance);
        div1 = findViewById(R.id.div1);
        div2 = findViewById(R.id.div2);
        div3 = findViewById(R.id.div3);
        div4 = findViewById(R.id.div4);
        //tvReload = findViewById(R.id.tvReload);
        shimmer = findViewById(R.id.shimmer);
        rvSlider = findViewById(R.id.rvSlider);
        imgAllReport = findViewById(R.id.imgAllReport);
        initBalance();
        gridInitSlider();

        div1.setOnClickListener(v -> {
            Intent i = new Intent(WalletOptions.this, AepsMatmWalletReqest.class);
            i.putExtra("activity_type", "aeps");
            startActivity(i);
        });

        div2.setOnClickListener(v -> {
            Intent i = new Intent(WalletOptions.this, AepsMatmWalletReqest.class);
            i.putExtra("activity_type", "matm");
            startActivity(i);
        });


        div4.setOnClickListener(v -> {
            Intent i = new Intent(WalletOptions.this, AepsMatmWalletReqest.class);
            i.putExtra("activity_type", "kuber");
            startActivity(i);
        });

        div3.setOnClickListener(v -> {
            Intent i = new Intent(WalletOptions.this, WalletFundRequest.class);
            startActivity(i);
        });

        imgAllReport.setOnClickListener(v -> {
            Intent i = new Intent(WalletOptions.this, AllReports.class);
            startActivity(i);
        });

        //secAddMoney.setOnClickListener(v -> startActivity(new Intent(this, GatewayStatus.class)));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_section);
        init();

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
        return map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        shimmer.setVisibility(View.GONE);
        try {
            JSONObject jsonObject = new JSONObject(JSonResponse);
            JSONObject userObject = new JSONObject(jsonObject.getString("data"));
            if (userObject.has("mainwallet"))
                SharedPrefs.setValue(this, SharedPrefs.MAIN_WALLET, userObject.getString("mainwallet"));
            else
                SharedPrefs.setValue(this, SharedPrefs.MAIN_WALLET, userObject.getString("balance"));
            if (userObject.has("microatmbalance")) {
                SharedPrefs.setValue(this, SharedPrefs.MICRO_ATM_BALANCE, userObject.getString("microatmbalance"));
            } else {
                SharedPrefs.setValue(this, SharedPrefs.MICRO_ATM_BALANCE, "NO");
            }

            if (userObject.has("kuberwallet")) {
                SharedPrefs.setValue(this, SharedPrefs.KUBER_BALANCE, userObject.getString("kuberwallet"));
            } else {
                SharedPrefs.setValue(this, SharedPrefs.KUBER_BALANCE, "NO");
            }

            SharedPrefs.setValue(this, SharedPrefs.APES_BALANCE, userObject.getString("aepsbalance"));

            initBalance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        shimmer.setVisibility(View.GONE);
    }

    private void initBalance() {
        tvMainBalance.setText(MyUtil.formatWithRupee(this, SharedPrefs.getValue(context, SharedPrefs.MAIN_WALLET)));
        tvAepsBalance.setText(MyUtil.formatWithRupee(this, SharedPrefs.getValue(context, SharedPrefs.APES_BALANCE)));
        tvKuberBalance.setText(MyUtil.formatWithRupee(this, SharedPrefs.getValue(this, SharedPrefs.KUBER_BALANCE)));
        tvMatmBalance.setText("Fund Request");
        String mBal = SharedPrefs.getValue(context, SharedPrefs.MICRO_ATM_BALANCE);
        if (mBal != null && !mBal.equalsIgnoreCase("NO") && mBal.length() > 0)
            tvMatmBalance.setText(MyUtil.formatWithRupee(this, mBal));
    }

    private List<String> stringList;

    private void gridInitSlider() {
        stringList = new ArrayList<>();
        stringList.addAll(Arrays.asList(Constants.images));
        String is_avail_array = SharedPrefs.getValue(this, SharedPrefs.BANNERARRAY);
        if (MyUtil.isNN(is_avail_array)) {
            try {
                stringList = new ArrayList<>();
                JSONArray bannersJson = new JSONArray(is_avail_array);
                for (int i = 0; i < bannersJson.length(); i++) {
                    JSONObject obj = bannersJson.getJSONObject(i);
                    String url = Constants.URL.BASE_URL + "/public/" + obj.getString("value");
                    stringList.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        rvSlider.setPageCount(stringList.size());
        rvSlider.setImageListener(imageListener);
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            MyUtil.setGlideImage(stringList.get(position), imageView, context);
        }
    };
}
