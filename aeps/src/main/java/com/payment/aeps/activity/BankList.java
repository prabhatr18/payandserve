package com.payment.aeps.activity;

import static com.payment.aeps.app.ModuleConstants.AEPS_TYPE;
import static com.payment.aeps.app.ModuleConstants.PAYSPRINT_AEPS;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.payment.aeps.R;
import com.payment.aeps.adapter.BankListAdapter;
import com.payment.aeps.app.ModuleConstants;
import com.payment.aeps.network.VolleyGetNetworkCall;
import com.payment.aeps.objectmodel.BankModel;
import com.payment.aeps.util.ModuleSharedPrefs;
import com.payment.aeps.util.ModuleUtil;
import com.payment.aeps.util.Print;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankList extends AppCompatActivity implements VolleyGetNetworkCall.RequestResponseLis {
    private String url, type;
    private ListView operatorListView;
    private List<BankModel> bankDataList;
    private BankListAdapter bankListAdapter;
    private Context context;
    private EditText etSearch;
    private Toolbar toolbar;

    private void init() {
        context = this;
        operatorListView = findViewById(R.id.operatorList);
        etSearch = findViewById(R.id.etSearch);
        bankDataList = new ArrayList<>();
        setupToolBar();
    }

    private void setupToolBar() {
        TextView tv = findViewById(R.id.tvToolBarTitle);
        tv.setText("Select Bank");
        ImageView backImage = findViewById(R.id.imgBack);
        backImage.setOnClickListener(view -> finish());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_bank_list);

        init();

        Print.P(url);

        bankListAdapter = new BankListAdapter(this, bankDataList);
        operatorListView.setAdapter(bankListAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (bankDataList != null) {
                    List<BankModel> temp = new ArrayList<>();
                    for (BankModel d : bankDataList) {
                        if (d.getBankName().toLowerCase().contains(s.toString().toLowerCase())) {
                            temp.add(d);
                        }
                    }
                    bankListAdapter.UpdateList(temp);
                }
            }
        });

        url = ModuleConstants.bankList;
        if (AEPS_TYPE.equalsIgnoreCase(PAYSPRINT_AEPS))
            url = ModuleConstants.baseUrl + "raeps/getdata";
        networkCallUsingVolleyApi(url);
    }

    private void networkCallUsingVolleyApi(String url) {
        if (ModuleUtil.isOnline(this)) {
            new VolleyGetNetworkCall(this, this, url, 1, param()).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.USER_ID));
        map.put("apptoken", ModuleSharedPrefs.getValue(this, ModuleSharedPrefs.APP_TOKEN));
        Print.P("BANK PARAM" + new JSONObject(map).toString());
        return map;
    }

    @Override
    public void onSuccessRequest(String result) {
        Gson gson = new GsonBuilder().create();
        if (!result.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (AEPS_TYPE.equalsIgnoreCase(PAYSPRINT_AEPS)){
                    jsonObject = jsonObject.getJSONObject("data");
                }
                JSONArray jsonArray = jsonObject.getJSONArray("bankName");
                Type type = new TypeToken<List<BankModel>>() {
                }.getType();
                bankDataList.addAll(gson.fromJson(jsonArray.toString(), type));
                bankListAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
