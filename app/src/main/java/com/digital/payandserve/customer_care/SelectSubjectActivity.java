package com.digital.payandserve.customer_care;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.customer_care.model.SelectSubjectModel;
import com.digital.payandserve.databinding.SearchWithListActivityBinding;
import com.digital.payandserve.databinding.SelectComplainActivityBinding;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.views.select_state_district.district_model.DistrictModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectSubjectActivity extends AppCompatActivity implements RequestResponseLis, SelectSubjectAdapter.OnSearchListClickListner {

    SelectComplainActivityBinding binding;
    SelectSubjectAdapter selectSubjectAdapter;
    String type, stateId;
    int REQUEST_TYPE = 0;
    private RecyclerView listView;
    private EditText etSearch;
    private List<SelectSubjectModel> dataList, dataListAll;

    private void init() {
        dataList = new ArrayList<>();
        dataListAll = new ArrayList<>();
        listView = binding.listView;
        etSearch = binding.etSearch;

        binding.icBack.setOnClickListener(v -> finish());


    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SelectComplainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                for (SelectSubjectModel d : dataListAll) {
                    if (d.getSubject().toLowerCase().contains(s.toString().toLowerCase())) {
                        dataList.add(d);
                    }
                }
                selectSubjectAdapter.UpdateList(dataList);

            }
        });
        REQUEST_TYPE = 0;
        networkCallUsingVolleyApi(Constants.URL.COMPLAIN_SUBJECT);



    }

    private void networkCallUsingVolleyApi(String url) {
        if (AppManager.isOnline(this)) {
            new VolleyNetworkCall(this, this, url, 1, param(), true).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        if (MyUtil.isNN(stateId))
            map.put("stateid", stateId);
        return map;
    }


    private void initList() {

        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setItemAnimator(new DefaultItemAnimator());

            selectSubjectAdapter = new SelectSubjectAdapter(this, dataList, this);
            listView.setAdapter(selectSubjectAdapter);
            selectSubjectAdapter.UpdateList(dataList);
        }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        Print.P("State Data : " + JSonResponse);

        try {
            if (REQUEST_TYPE == 0) {

                JSONObject object = new JSONObject(JSonResponse);
                if (object.has("data")) {
                    JSONArray subjectArray = object.getJSONArray("data");
                    dataList.addAll(AppHandler.parseSubjectList(this, subjectArray));
                    Collections.sort(dataList, SelectSubjectModel.comparator);
                    dataListAll.addAll(dataList);
                    initList();
                    if (dataList.size() == 0) {

                        errorView("Sorry Records are not available !");
                    } else {

                        listView.setVisibility(View.VISIBLE);
                        findViewById(R.id.noData).setVisibility(View.GONE);
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailRequest(String msg) {
        Print.P("OnFailRequest : " + msg);
        Toast.makeText(this, "On Failure", Toast.LENGTH_SHORT).show();
    }

    private void errorView(String string) {
        TextView tvMsg = findViewById(R.id.tvMsg);
        tvMsg.setText(string);
        listView.setVisibility(View.GONE);
        findViewById(R.id.noData).setVisibility(View.VISIBLE);
    }

    @Override
    public void onSearchItemClicked(int postion, String subject) {
//        ExtensionFunction.showToast(this,statecode);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("subject", subject);
        setResult(100, returnIntent);
        finish();
//        startActivity(new Intent(this, OnBoardingActivity.class));
    }

}
