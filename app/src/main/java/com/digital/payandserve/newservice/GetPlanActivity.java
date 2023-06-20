package com.digital.payandserve.newservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.ExtensionFunction;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.utill.SharedPrefs;
import com.digital.payandserve.views.DashBoardActivity;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class GetPlanActivity extends AppCompatActivity {

    RecyclerView getPlanRecycler;
    Button confirmBtn, retryBtn;

    String mobileNumber, token, userId;

    private void init() {

        getPlanRecycler = (RecyclerView) findViewById(R.id.getPlanRecycler);
        confirmBtn = (Button) findViewById(R.id.confirmBtn);
        retryBtn = (Button) findViewById(R.id.retryBtn);

    }

    List<GetPlanListModel> planListData = new ArrayList<>();
    String loanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_plan);

        init();

        SharedPreferences sh = getSharedPreferences("loan", Context.MODE_PRIVATE);
        loanId = sh.getString("loanId", "");

        if (loanId.isEmpty()) {

            Intent intent = getIntent();
            mobileNumber = intent.getStringExtra("mobileNumber");
            loanId=mobileNumber;

        }

        token = SharedPrefs.getValue(this, SharedPrefs.APP_TOKEN);
        userId = SharedPrefs.getValue(this, SharedPrefs.USER_ID);

        Log.d("getPlan", "" + mobileNumber);

        getPlan();

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPlan();
            }
        });

    }

    private void getPlan() {

        if (AppManager.isOnline(GetPlanActivity.this)) {

            String url = Constants.URL.BASE_URL + "api/android/paylater/payment";
            getPlanList(url);
            getPlanRecycler.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.GONE);

        } else {
            getPlanRecycler.setVisibility(View.GONE);
            retryBtn.setVisibility(View.VISIBLE);
            Toast.makeText((Context) GetPlanActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void getPlanList(String url) {

        planListData.clear();

        ProgressDialog pDialog = new ProgressDialog((Context) this);
        pDialog.setMessage("PLease wait...");
        pDialog.show();


        RequestQueue queue = Volley.newRequestQueue((Context) GetPlanActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pDialog.dismiss();

                Log.d("OnBoard onSuccess :", "" + response);

                try {
                    String msg = "some error accour";
                    JSONObject jsonObject = new JSONObject(response);
                    String statuscode = jsonObject.getString("statuscode");
                    String status = jsonObject.getString("status");
                    String plans = jsonObject.getString("plans");

                    if (jsonObject.has("message"))
                        msg = jsonObject.getString("message");

                    if (statuscode.equalsIgnoreCase("TXN")) {
                        // ExtensionFunction.showToast(this, msg);
                        Log.d("OnBoard Param if :", "" + msg);
                        Log.d("OnBoard plans :", "" + plans);

                        JSONArray jsonArray = jsonObject.getJSONArray("plans");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject arrayJSONObject = jsonArray.getJSONObject(i);
                            Log.d("OnBoard for :", "" + arrayJSONObject);

                            String id = arrayJSONObject.getString("id");
                            String name = arrayJSONObject.getString("name");
                            String minAmount = arrayJSONObject.getString("minAmount");
                            String maxAmount = arrayJSONObject.getString("maxAmount");
                            String interestRate = arrayJSONObject.getString("interestRate");
                            String emiTotal = arrayJSONObject.getString("emiTotal");
                            String emiAmount = arrayJSONObject.getString("emiAmount");
                            String processingFee = arrayJSONObject.getString("processingFee");
                            String description = arrayJSONObject.getString("description");

                            Log.d("OnBoard for :", "" + minAmount);
                            Log.d("OnBoard for :", "" + id);


                            planListData.add(new GetPlanListModel(id, name, minAmount, maxAmount, interestRate, emiAmount, emiTotal, processingFee, description));

                            GetPlanAdapter adapter = new GetPlanAdapter(GetPlanActivity.this, planListData, loanId);
                            getPlanRecycler.setAdapter(adapter);
                            getPlanRecycler.setLayoutManager(new LinearLayoutManager((Context) GetPlanActivity.this));

                        }


                    } else {

                        ExtensionFunction.showToast(GetPlanActivity.this, "" + msg);
                    }

                } catch (JSONException e) {
                    pDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                // method to handle errors.
                Toast.makeText((Context) GetPlanActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<String, String>();

                map.put("user_id", userId);
                map.put("type", "getplan");
                map.put("mobile", loanId);
                map.put("apptoken", token);
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent((Context) this, DashBoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}