package com.digital.payandserve.views.operator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.GridSpacingItemDecoration;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.RecyclerTouchListener;
import com.digital.payandserve.views.billpayment.DTHAmountInput;
import com.digital.payandserve.views.billpayment.fragment.FetchBBPSParamsAndBillFragNew;
import com.digital.payandserve.views.billpayment.fragment.FetchBBPSParamsAndBillFragOld;
import com.digital.payandserve.views.operator.adapter.OperatorListAdapter;
import com.digital.payandserve.views.operator.model.OperatorModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperatorListFrag extends Fragment implements RequestResponseLis {
    public Context context;
    private RecyclerView listView;
    private List<OperatorModel> dataListAll, dataList;
    private OperatorListAdapter adapter;
    private EditText etSearch;
    private String TYPE;
    private ImageView icBack, imgProvider;
    private ProgressBar loader;
    private TextView tvMsg;
    private RelativeLayout noData;

    public OperatorListFrag(String type) {
        this.TYPE = type;
    }

    private void init(View view) {
        context = getActivity();
        dataList = new ArrayList<>();
        dataListAll = new ArrayList<>();
        tvMsg = view.findViewById(R.id.tvMsg);
        noData = view.findViewById(R.id.noData);
        listView = view.findViewById(R.id.listView);
        loader = view.findViewById(R.id.loader);
        imgProvider = view.findViewById(R.id.imgProvider);
        etSearch = view.findViewById(R.id.etSearch);
        //TYPE = "dth";
        view.findViewById(R.id.icBack).setVisibility(View.GONE);
        initList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_with_list_activity, container, false);
        init(v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        listView.addOnItemTouchListener(new RecyclerTouchListener(context,
                listView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                OperatorModel model = dataList.get(position);
                if (TYPE.equalsIgnoreCase("mobile") || TYPE.equalsIgnoreCase("dth")) {
                    Intent i = new Intent(getActivity(), DTHAmountInput.class);
                    i.putExtra("mobile", "");
                    i.putExtra("provider_name", model.getName());
                    i.putExtra("provider_id", model.getId());
                    i.putExtra("provider_image", model.getLogo());
                    startActivity(i);
                } else {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    if (Constants.BILL_PAYMENT_VERSION == 1) {
                        transaction.replace(R.id.frameLayout, new FetchBBPSParamsAndBillFragNew(model.getId(),
                                model.getName(), model.getLogo(), TYPE));
                    } else {
                        transaction.replace(R.id.frameLayout, new FetchBBPSParamsAndBillFragOld(model.getId(),
                                model.getName(), model.getLogo(), TYPE));
                    }
                    transaction.addToBackStack("operator");
                    transaction.commit();
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        networkCallUsingVolleyApi(Constants.URL.PROVIDER, false);
    }

    private void networkCallUsingVolleyApi(String url, boolean isLoad) {
        if (AppManager.isOnline(context)) {
            showHideLoader(true);
            new VolleyNetworkCall(this, getActivity(), url, 1, param(), isLoad).netWorkCall();
        } else {
            Toast.makeText(context, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private void initList() {
        adapter = new OperatorListAdapter(context, dataList, TYPE);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 3);
        listView.setLayoutManager(mLayoutManager);
        int i = AppManager.getInstance().dpToPx(2, context);
        listView.addItemDecoration(new GridSpacingItemDecoration(3, i, true));
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
        showHideLoader(false);
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
                    noData.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            errorView(AppHandler.parseExceptionMsg(e));
            errorView("Something went wrong in parsing");
        }
    }

    @Override
    public void onFailRequest(String msg) {
        showHideLoader(false);
        errorView(msg);
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
