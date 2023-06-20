package com.digital.payandserve.views.dmt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.utill.SharedPrefs;
import com.digital.payandserve.views.moneytransfer.AddBeneficiary;
import com.digital.payandserve.views.moneytransfer.adapter.BenListAdapter;
import com.digital.payandserve.views.otpview.OTPValidate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PdmtBenilist extends AppCompatActivity implements RequestResponseLis, BenLIstAdapterPdmt.BenItemClick, BenLIstAdapterPgout.BenItemClick {
    private int REQUEST_TYPE = 0;
    private TextView tvContact, tvName, tvMax, tvMin;
    private Context context;
    private Button btnProceed;
    private RecyclerView listView;
    private List<Benedatum> benModelList;
    private List<BenedatumPgPayout> benModelListOne;
    private BenListAdapter adapter;
    private BenLIstAdapterPdmt adapterPdmt;
    private BenLIstAdapterPgout benLIstAdapterPgout;
    private String BEN_MOBILE, SENDER_MOBILE, BEN_ACCOUNT, SENDER_NAME;
    private String TYPE;
    private Button btnAddBen;
    private String apiUrl;
    private String deleteId = "";
    private String beneID = "";

    private void init() {
        context = this;
        benModelList = new ArrayList<>();
        benModelListOne = new ArrayList<>();
        listView = findViewById(R.id.benList);
        tvContact = findViewById(R.id.tvContact);
        tvName = findViewById(R.id.tvName);
        tvMax = findViewById(R.id.tvMax);
        tvMin = findViewById(R.id.tvMin);
        btnAddBen = findViewById(R.id.btnAddBen);


        btnAddBen.setOnClickListener(v -> {
            Print.P("URL : " + apiUrl);
            Intent i = new Intent(PdmtBenilist.this, AddBeneficiary.class);
            i.putExtra("sender_name", SENDER_NAME);
            i.putExtra("sender_number", SENDER_MOBILE);
            i.putExtra("apiUrl", apiUrl);
            startActivity(i);
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.transparentStatusBar(this);
        setContentView(R.layout.activity_pdmt_benilist);

        init();
        Bundle bundle = getIntent().getExtras();
        SENDER_MOBILE = bundle.getString("sender_number");
        SENDER_NAME = bundle.getString("name");
        String available_limit = bundle.getString("available_limit");
        String limit_spend = bundle.getString("total_spend");
        String json = bundle.getString("json");
        apiUrl = bundle.getString("apiUrl");
        String max = "TOTAL : " + MyUtil.formatWithRupee(this, available_limit);
        String min = "USED : " + MyUtil.formatWithRupee(this, limit_spend);
        tvMax.setText(max);
        tvMin.setText(min);
        tvName.setText(SENDER_NAME);
        tvContact.setText(SENDER_MOBILE);
        SharedPrefs.setValue(this,SharedPrefs.pdmtContact,SENDER_MOBILE);

        Log.d("Response json :",""+json);

        TYPE = "verification";
        updateRecord(json);
       // networkCallUsingVolleyApi(apiUrl, true);

    }

    private void initBenSlider() {
        if (getIntent().getStringExtra("dmtType").equalsIgnoreCase("PG-Payout")) {
            benLIstAdapterPgout = new BenLIstAdapterPgout(this, benModelListOne, this);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            listView.setLayoutManager(mLayoutManager);
            listView.setItemAnimator(new DefaultItemAnimator());
            listView.setAdapter(benLIstAdapterPgout);
        } else if (getIntent().getStringExtra("dmtType").equalsIgnoreCase("PDmt")) {
            adapterPdmt = new BenLIstAdapterPdmt(this, benModelList, this);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            listView.setLayoutManager(mLayoutManager);
            listView.setItemAnimator(new DefaultItemAnimator());
            listView.setAdapter(adapterPdmt);
        }

    }

    private boolean isValid() {
        return true;
    }

    private void networkCallUsingVolleyApi(String url, boolean isLoad) {
        if (AppManager.isOnline(this)) {
            new VolleyNetworkCall(this, this, url, 1, param(), isLoad).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        try {
            if (TYPE.equalsIgnoreCase("otp")) {
                Intent i = new Intent(PdmtBenilist.this, OTPValidate.class);
                i.putExtra("type", "otp");
                i.putExtra("url", apiUrl);
                i.putExtra("ben_mobile", BEN_MOBILE);
                i.putExtra("sender_mobile", SENDER_MOBILE);
                i.putExtra("ben_account", BEN_ACCOUNT);
                startActivity(i);
                TYPE = "";
            } else {
                updateRecord(JSonResponse);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Print.P(msg);
    }

    private void updateRecordOne(String json) {
        try {
            benModelListOne.clear();
            JSONObject mainObject = new JSONObject(json);
//            if (TYPE.equalsIgnoreCase("benedelete")) {
//                TYPE = "verification";
//                networkCallUsingVolleyApi(apiUrl, true);
//            } else {
            JSONObject jsonObject = mainObject.getJSONObject("data");
            String totallimit = "";
            if (jsonObject.has("bank2_limit")) {
                totallimit = jsonObject.getString("bank2_limit");
            } else {
                totallimit = jsonObject.getString("totallimit");
            }

            String usedlimit = "";
            if (jsonObject.has("bank2_limit")) {
                usedlimit = jsonObject.getString("bank2_limit");
            } else {
                usedlimit = jsonObject.getString("usedlimit");
            }

            String max = "TOTAL : " + MyUtil.formatWithRupee(this, totallimit);
            String min = "USED : " + MyUtil.formatWithRupee(this, usedlimit);
            tvMax.setText(max);
            tvMin.setText(min);

            JSONArray jsonArray = null;
            if (mainObject.has("beneficiary")) {
                jsonArray = mainObject.getJSONArray("beneficiary");
            } else {
                jsonArray = jsonObject.getJSONArray("benedata");
            }
//                benModelList.addAll(AppHandler.parseBenListP(this, jsonArray));
            benModelListOne.addAll(AppHandler.parseBenListPgOut(this, jsonArray));
//                if (benModelList.size() == 0) {
//                    errorView("No beneficiary record found");
//                }
            if (benModelListOne.size() == 0) {
                errorView("No beneficiary record found");
            }
            initBenSlider();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateRecord(String json) {
        try {
//            benModelList.clear();
            JSONObject mainObject = new JSONObject(json);
            if (TYPE.equalsIgnoreCase("benedelete")) {
                TYPE = "verification";
                networkCallUsingVolleyApi(apiUrl, true);
            } else if (TYPE.equalsIgnoreCase("benedeletepgpaout")) {
                TYPE = "verification";
                networkCallUsingVolleyApi(apiUrl, true);
            } else {
                JSONObject jsonObject = mainObject.getJSONObject("data");
                String totallimit = "";
                if (jsonObject.has("bank2_limit")) {
                    totallimit = jsonObject.getString("bank2_limit");
                } else {
                    totallimit = jsonObject.getString("totallimit");
                }

               // Toast.makeText(context, "json", Toast.LENGTH_SHORT).show();
                String usedlimit = "";
                if (jsonObject.has("bank2_limit")) {
                    usedlimit = jsonObject.getString("bank2_limit");
                } else {
                    usedlimit = jsonObject.getString("usedlimit");
                }

                String max = "TOTAL : " + MyUtil.formatWithRupee(this, totallimit);
                String min = "USED : " + MyUtil.formatWithRupee(this, usedlimit);
                tvMax.setText(max);
                tvMin.setText(min);

                JSONArray jsonArray = null;
                if (mainObject.has("benedata")) {
                    jsonArray = mainObject.getJSONArray("benedata");

                   // beneID = model.getBeneId();

                    for(int i=0; i < jsonArray.length(); i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        beneID = jsonObject1.getString("bene_id");

                        Log.d("Response BeneId :",""+beneID);
                       // Toast.makeText(context, ""+beneID, Toast.LENGTH_SHORT).show();
                    }

                  //  SharedPrefs.setValue(getApplicationContext(),SharedPrefs.BENE_ID,model.getBeneId());

                } else {
                    jsonArray = jsonObject.getJSONArray("beneficiary");
                }
                if (getIntent().getStringExtra("dmtType").equalsIgnoreCase("PG-Payout")) {
                    benModelListOne.clear();
                    benModelListOne.addAll(AppHandler.parseBenListPgOut(this, jsonArray));
                    if (benModelListOne.size() == 0) {
                        errorView("No beneficiary record found");
                    }
                } else if (getIntent().getStringExtra("dmtType").equalsIgnoreCase("PDmt")) {
                    benModelList.clear();
                    benModelList.addAll(AppHandler.parseBenListP(this, jsonArray));
                    if (benModelList.size() == 0) {
                        errorView("No beneficiary record found");
                    }
                }

                initBenSlider();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void benItemLisP(Benedatum model) {
//        BEN_MOBILE = model.getBenemobile();
        BEN_ACCOUNT = model.getAccno();
//        if (!model.getVerified().equalsIgnoreCase("0")) {
        Intent i = new Intent(this, PDMTTransaction.class);
        i.putExtra("beneid", model.getBeneId());
        i.putExtra("bankname", model.getBankname());
        i.putExtra("account", model.getAccno());
        i.putExtra("ifsc", model.getIfsc());
        i.putExtra("name", model.getName());
        i.putExtra("bankid", model.getBankid());
        i.putExtra("sender_name", SENDER_NAME);
        i.putExtra("sender_number", SENDER_MOBILE);
        i.putExtra("apiUrl", apiUrl);
        startActivity(i);
//        } else {
//            TYPE = "otp";
//            networkCallUsingVolleyApi(apiUrl, true);
//        }
    }

    @Override
    public void benItemLisPgPayout(BenedatumPgPayout model) {
//        BEN_MOBILE = model.getBenemobile();
        BEN_ACCOUNT = model.getAccno();
//        if (!model.getVerified().equalsIgnoreCase("0")) {
        Intent i = new Intent(this, PDMTTransaction.class);
        i.putExtra("beneid", model.getBeneId());
        i.putExtra("bankname", model.getBankname());
        i.putExtra("account", model.getAccno());
        i.putExtra("ifsc", model.getIfsc());
        i.putExtra("name", model.getName());
        i.putExtra("bankid", model.getBankid());
        i.putExtra("sender_name", SENDER_NAME);
        i.putExtra("sender_number", SENDER_MOBILE);
        i.putExtra("apiUrl", apiUrl);
        startActivity(i);
//        } else {
//            TYPE = "otp";
//            networkCallUsingVolleyApi(apiUrl, true);
//        }
    }


    //    @Override
//    public void benDel(BenModel model) {
//        TYPE = "benedelete";
//        deleteId = SENDER_MOBILE;
//        beneID = model.getBeneid();
//        networkCallUsingVolleyApi(Constants.URL.PDMT_TRANSACTION, true);
//    }
    @Override
    public void benDelP(Benedatum model) {
        TYPE = "benedelete";
        deleteId = SENDER_MOBILE;
        beneID = model.getBeneId();
       // SharedPrefs.setValue(getApplicationContext(),SharedPrefs.BENE_ID,model.getBeneId());
        networkCallUsingVolleyApi(Constants.URL.PDMT_TRANSACTION, true);
    }

    @Override
    public void benDelPGpayout(BenedatumPgPayout model) {
        TYPE = "benedeletepgpaout";
        beneID = model.getBeneId();
        networkCallUsingVolleyApi(Constants.URL.PGDMT_DELETE, true);
    }


    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("type", TYPE);
        if (TYPE.equalsIgnoreCase("otp") || TYPE.equalsIgnoreCase("verification")) {
//            map.put("type", TYPE);
            map.put("mobile", SENDER_MOBILE);
            if (MyUtil.isNN(Constants.DMT_RID))
                map.put("rid", Constants.DMT_RID);
        } else if (TYPE.equalsIgnoreCase("benedelete")) {
            map.put("rid", deleteId);
            map.put("bid", beneID);
            map.put("mobile", SENDER_MOBILE);
            map.put("rname", SENDER_NAME);
        } else if (TYPE.equalsIgnoreCase("benedeletepgpaout")) {
            map.put("id", beneID);
        } else {
            Toast.makeText(context, "Transafer Amount", Toast.LENGTH_SHORT).show();
        }
        return map;
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.noData).setVisibility(View.GONE);
        if (Constants.IS_RELOAD_REQUEST) {
            Constants.IS_RELOAD_REQUEST = false;
            TYPE = "verification";
            networkCallUsingVolleyApi(apiUrl, true);
        }
    }

    private void errorView(String string) {
        TextView tvMsg = findViewById(R.id.tvMsg);
        tvMsg.setText(string);
        findViewById(R.id.noData).setVisibility(View.VISIBLE);
    }
}