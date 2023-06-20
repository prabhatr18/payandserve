package com.digital.payandserve.views.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.utill.RecyclerTouchListener;
import com.digital.payandserve.views.billpayment.MobileRechargeAmountInput;
import com.digital.payandserve.views.contact.AndroidContactList;
import com.digital.payandserve.views.fragment.adapter.RecentBillTranAdapter;
import com.digital.payandserve.views.operator.OperatorList;
import com.digital.payandserve.views.reports.BillRechargeTransaction;
import com.digital.payandserve.views.reports.model.BillRechargeModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragRechangeMobileFirst extends Fragment implements RequestResponseLis, View.OnClickListener {
    private List<BillRechargeModel> dataList;
    private RecyclerView listView;
    private Activity context;
    private RecentBillTranAdapter adapter;
    private ProgressBar loader;
    private RelativeLayout recentView;
    private ImageView imgContactBook;
    private EditText etNumber;
    private AppCompatButton btnProceed;
    private TextView tvShowAll;

    private void init(View view) {
        listView = view.findViewById(R.id.recentList);
        etNumber = view.findViewById(R.id.etNumber);
        imgContactBook = view.findViewById(R.id.imgContactBook);
        btnProceed = view.findViewById(R.id.btnProceed);
        recentView = view.findViewById(R.id.recentView);
        tvShowAll = view.findViewById(R.id.tvShowAll);
        loader = view.findViewById(R.id.loader);
        dataList = new ArrayList<>();
        context = getActivity();
        setLis();
    }

    private void setLis() {
        imgContactBook.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_mobile_recharge_number_first, container, false);
        init(v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initReportList();

        networkCallUsingVolleyApi(Constants.URL.REPORT, false);

        btnProceed.setOnClickListener(v -> {
            String n = etNumber.getText().toString();
            if (n != null && n.length() == 10) {
               /* Intent i = new Intent(getActivity(), OperatorList.class);
                i.putExtra("type", "mobile");
                i.putExtra("mobile", etNumber.getText().toString());
*/
                Intent i = new Intent(getActivity(), MobileRechargeAmountInput.class);
                i.putExtra("mobile", etNumber.getText().toString());
                i.putExtra("type", "Phone");
                startActivity(i);
            } else {
                Toast.makeText(context, "Valid Mobile number is required", Toast.LENGTH_SHORT).show();
            }
        });

        listView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                listView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                BillRechargeModel model = dataList.get(position);
                Intent i = new Intent(getActivity(), MobileRechargeAmountInput.class);
                i.putExtra("mobile", model.getMobile());
                i.putExtra("provider_name", model.getProvidername());
                i.putExtra("provider_id", model.getProviderId());
                i.putExtra("provider_image", "");
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        tvShowAll.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), BillRechargeTransaction.class);
            i.putExtra("title", "Recharge Statement");
            i.putExtra("type", "rechargestatement");
            startActivity(i);
        });
    }

    private void initReportList() {
        adapter = new RecentBillTranAdapter(context, dataList);
        LinearLayoutManager mLayoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(adapter);
    }

    private void networkCallUsingVolleyApi(String url, boolean isLoad) {
        if (AppManager.isOnline(context)) {
            loader.setVisibility(View.VISIBLE);
            new VolleyNetworkCall(this, context, url, 1, param(), isLoad).netWorkCall();
        } else {
            Toast.makeText(context, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();

        map.put("type", "rechargestatement");
        map.put("start", String.valueOf("1"));
        return map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        loader.setVisibility(View.GONE);
        try {
            JSONObject object = new JSONObject(JSonResponse);
            if (object.has("data")) {
                int oldDataSize = dataList.size();
                JSONArray bannerArray = object.getJSONArray("data");
                dataList.addAll(AppHandler.parseRecentBillRechargeData(context, bannerArray));
                adapter.notifyDataSetChanged();
                listView.setVerticalScrollbarPosition(oldDataSize);
                if (dataList.size() == 0) {
                    errorView("Sorry Records are not available !");
                } else {
                    recentView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            errorView(AppHandler.parseExceptionMsg(e));
        }
    }

    @Override
    public void onFailRequest(String msg) {
        loader.setVisibility(View.GONE);
        errorView(msg);
    }

    private void errorView(String string) {
        Toast.makeText(context, "Error : " + string, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgContactBook:
                startActivityForResult(new Intent(getActivity(), AndroidContactList.class), 100);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case 100:
                    String contact = data.getStringExtra("contact");
                    parseContact(contact);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseContact(String swissNumberStr) {
        try {
            Print.P("LOG : " + revString(swissNumberStr));
            etNumber.setText(revString(swissNumberStr));
            etNumber.setSelection(revString(swissNumberStr).length());
        } catch (Exception e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }
    }

    private String revString(String number) {
        number = number.replaceAll(" ", "");
        if (number.length() >= 10) {
            StringBuilder builder = new StringBuilder();
            builder.append(number);
            builder = builder.reverse();
            String revString = builder.substring(0, 10);
            builder.setLength(0);
            builder.append(revString);
            return builder.reverse().toString();
        } else {
            return number;
        }
    }
}