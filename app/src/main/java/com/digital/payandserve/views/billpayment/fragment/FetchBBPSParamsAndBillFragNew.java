package com.digital.payandserve.views.billpayment.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.views.billpayment.ShowBillFetched;
import com.digital.payandserve.views.billpayment.adapter.BillFetchParamListAdapter;
import com.digital.payandserve.views.billpayment.model.BillPayModel;
import com.digital.payandserve.views.billpayment.model.ParameItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FetchBBPSParamsAndBillFragNew extends Fragment implements RequestResponseLis,
        BillFetchParamListAdapter.GetListDataLis {

    private ListView listView;
    private List<ParameItem> dataList;
    private Button btnFetch;
    private BillFetchParamListAdapter adapter;
    private TextView tvTitle;
    private String provider_id, provider_name, provider_logo, type;
    private int REQUEST_TYPE = 0;
    private ProgressBar loader;
    private TextView tvMsg;
    private CardView noData;
    private Context context;
    private ImageView imgProvider;

    public FetchBBPSParamsAndBillFragNew(String provider_id, String provider_name, String provider_logo, String type) {
        this.provider_id = provider_id;
        this.provider_name = provider_name;
        this.provider_logo = provider_logo;
        this.type = type;
    }

    private void init(View view) {
        context = getActivity();
        listView = view.findViewById(R.id.listView);
        dataList = new ArrayList<>();
        adapter = new BillFetchParamListAdapter(getActivity(), dataList, this);
        listView.setAdapter(adapter);
        btnFetch = view.findViewById(R.id.btnProceed);
        tvTitle = view.findViewById(R.id.title);
        tvMsg = view.findViewById(R.id.tvMsg);
        noData = view.findViewById(R.id.noData);
        listView = view.findViewById(R.id.listView);
        imgProvider = view.findViewById(R.id.imgProvider);
        loader = view.findViewById(R.id.loader);

        tvTitle.setText(provider_name);
        MyUtil.setStaticProviderImg(provider_name, imgProvider, context, type);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bbps_fetch_bill_params, container, false);
        init(v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnFetch.setOnClickListener(view1 -> {
            if (isValid()) {
                REQUEST_TYPE = 1;
                networkCallUsingVolleyApi(Constants.URL.BBPS_BILL_PAY, dynamicParam());
            }
        });

        REQUEST_TYPE = 0;
        networkCallUsingVolleyApi(Constants.URL.BBPS_BILL_PAY_PARAM, providerParam(provider_id));
    }

    private void networkCallUsingVolleyApi(String url, Map<String, String> param) {
        if (AppManager.isOnline(context)) {
            showHideLoader(true);
            new VolleyNetworkCall(this, getActivity(), url, 1, param, false, false).netWorkCall();
        } else {
            Toast.makeText(context, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> providerParam(String provider) {
        Map<String, String> map = new HashMap<>();
        map.put("provider_id", provider);
        return map;
    }

    private Map<String, String> dynamicParam() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "validate");
        map.put("provider_id", provider_id);
        map.put("duedate", Constants.SHOWING_DATE_FORMAT.format(new Date()));
        for (int i = 0; i < dataList.size(); i++) {
            String key = "number" + i;
            map.put(key, dataList.get(i).getFieldInputValue());
        }
        return map;
    }

    private  String biller_name, amount, selected_due_date, transactionId;
    @Override
    public void onSuccessRequest(String JSonResponse) {
        showHideLoader(false);
        Print.P("Res : " + JSonResponse);
        Gson gson = new GsonBuilder().create();
        try {
            if (REQUEST_TYPE == 0) {
                Constants.BILL_MODEL = gson.fromJson(JSonResponse, BillPayModel.class);
                dataList.addAll(Constants.BILL_MODEL.getParame());
                adapter.notifyDataSetChanged();
                if (dataList.size() == 0)
                    errorView("Param are not available for this provider");
            } else {
                JSONObject jsonObject = new JSONObject(JSonResponse);
                if (jsonObject.has("message"))
                    Toast.makeText(context, AppHandler.getMessage(JSonResponse), Toast.LENGTH_SHORT).show();
                if (jsonObject.has("data") && !jsonObject.getString("data").equals("")) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    biller_name = data.getString("customername");
                    amount = data.getString("dueamount");
                    selected_due_date = data.getString("duedate");
                    if (data.has("TransactionId")) {
                        transactionId = data.getString("TransactionId");
                    }
                }
                Intent i = new Intent(getActivity(), ShowBillFetched.class);
                i.putExtra("amount", amount);
                i.putExtra("billerName", biller_name);
                i.putExtra("dueDate", selected_due_date);
                i.putExtra("amount", amount);
                i.putExtra("transactionId", transactionId);
                i.putExtra("provider_id", provider_id);
                i.putExtra("provider_name", provider_name);
                i.putExtra("provider_logo", provider_logo);
                i.putExtra("type", type);
                i.putExtra("bbps_type", String.valueOf(Constants.BBPS.NEW));
                startActivity(i);
            }
        } catch (Exception e) {
            errorView("Something went wrong please try again after some time : " + e);
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        showHideLoader(false);
        errorView(msg);
    }

    @Override
    public void getRecords(int position, ParameItem item) {
        dataList.set(position, item);
    }

    public boolean isValid() {
        boolean flag = true;
        for (ParameItem model : dataList) {
            String inputValue = model.getFieldInputValue();
            if (inputValue == null || inputValue.length() == 0) {
                Toast.makeText(getActivity(), model.getParamname().toLowerCase() + " " + "is required", Toast.LENGTH_LONG).show();
                flag = false;
                break;
            }
        }
        return flag;
    }

    private void errorView(String string) {
        tvMsg.setText(string);
        listView.setVisibility(View.GONE);
        noData.setVisibility(View.VISIBLE);
    }

    private void showHideLoader(boolean isShow) {
        if (isShow) {
            loader.setVisibility(View.VISIBLE);
        } else {
            loader.setVisibility(View.GONE);
        }
    }
}
