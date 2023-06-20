package com.digital.payandserve.views.dmt;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.digital.payandserve.R;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BenLIstAdapterPdmt extends RecyclerView.Adapter<BenLIstAdapterPdmt.MyViewHolder> {
    private Context mContext;
    private List<Benedatum> dataList;
    private BenItemClick lis;
    private int REQUEST_TYPE = 0;
    private String BANK_ID, BANK_IFSC, SENDER_NUMBER, SENDER_NAME;
    private String TYPE = "";


    public BenLIstAdapterPdmt(Context mContext, List<Benedatum> dataList, BenItemClick lis) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.lis = lis;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dmt_ben_list_row_pdmt, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Benedatum model = dataList.get(position);
        holder.tvTxnId.setText(model.getBeneId());
       holder.tvMobile.setText(SharedPrefs.getValue(mContext,SharedPrefs.pdmtContact));
        holder.tvName.setText(model.getName());
        holder.tvAccount.setText(model.getAccno());
        holder.tvBank.setText(model.getBankname());
        holder.tvBankIfsc.setText(model.getIfsc());
//        if (MyUtil.isNN(model.getBenemobile())) {
//            holder.llMobile.setVisibility(View.VISIBLE);
//        } else {
//            holder.llMobile.setVisibility(View.GONE);
//        }
        if (MyUtil.isNN(model.getBankname())) {
            holder.secBankContainer.setVisibility(View.VISIBLE);
        } else {
            holder.secBankContainer.setVisibility(View.GONE);
        }
        holder.btnProceed.setOnClickListener(v -> lis.benItemLisP(model));
        holder.btnDeleteP.setOnClickListener(v -> lis.benDelP(model));

        holder.btnValidateAc.setOnClickListener(v -> {

            RequestQueue queue = Volley.newRequestQueue(mContext);
            REQUEST_TYPE = 1;
            TYPE = "accountverification";

            String url = Constants.URL.PDMT_TRANSACTION;

            ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Please wait...!");
            progressDialog.show();

            Log.d("Response URL :", "" + url);

            StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    progressDialog.dismiss();
                    Log.d("Response :", "" + response);
                   //  Toast.makeText(mContext, "Data added to API", Toast.LENGTH_SHORT).show();
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("statuscode");

                        if (status.equals("TXN")) {
                            Toast.makeText(mContext, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            Log.d("Response :", "" + "Success");
                        } else {
                            Toast.makeText((Context) mContext, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // method to handle errors.
                    progressDialog.dismiss();
                      Toast.makeText(mContext, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                   /* map.put("type", TYPE);
                    map.put("mobile", SENDER_NUMBER);
                    map.put("name", SENDER_NAME);
                    map.put("benemobile", holder.tvMobile.getText().toString());
                    map.put("benebank", BANK_ID);
                    map.put("beneifsc", holder.tvBankIfsc.getText().toString());
                    map.put("beneaccount", holder.tvAccount.getText().toString());
                    map.put("benename", holder.tvName.getText().toString());
                    map.put("bene_id", holder.tvTxnId.getText().toString());
*/
                    map.put("user_id", SharedPrefs.getValue(mContext, SharedPrefs.USER_ID));
                    map.put("apptoken", SharedPrefs.getValue(mContext,SharedPrefs.APP_TOKEN));
                    map.put("type" , "accountverification");
                    map.put("mobile" , ""+holder.tvMobile.getText().toString());
                    map.put("benebank" , ""+model.getBankid());
                    map.put("beneaccount" , ""+holder.tvAccount.getText().toString());
                    map.put("beneifsc" , ""+model.getIfsc());
                    map.put("benename" , ""+holder.tvName.getText().toString());
                    map.put("bene_id" , model.getBeneId());

                    Log.d("Response params:", "" + map);


                    return map;
                }
            };
            queue.add(request);


        });


        if (model.getVerified().equalsIgnoreCase("1")) {
            holder.tvBankStatus.setText("Verify");
        } else {
            holder.tvBankStatus.setText("Not Verify");
        }
        holder.btnProceed.setBackground(mContext.getResources().getDrawable(R.drawable.success_border_green));
        holder.btnProceed.setText("Transfer");

//        }
    }

   /* private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("type", TYPE);
        map.put("mobile", SENDER_NUMBER);
        map.put("name", SENDER_NAME);
        map.put("benemobile", etBenMobile.getText().toString());
        map.put("benebank", BANK_ID);
        map.put("beneifsc", etIFSC.getText().toString());
        map.put("beneaccount", tvAccount.getText().toString());
        map.put("benename", etHolderName.getText().toString());
        map.put("bene_id", tvTxnId);
        return map;
    }*/

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface BenItemClick {
        void benItemLisP(Benedatum model);

        void benDelP(Benedatum model);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTxnId, tvMobile, tvName, tvAccount, tvBank, tvBankStatus, tvBankIfsc;
        public Button btnProceed, btnDeleteP;
        public LinearLayout secBankContainer, llMobile;
        AppCompatButton btnValidateAc;

        public MyViewHolder(View view) {
            super(view);
            tvBankIfsc = view.findViewById(R.id.tvBankIfsc);
            btnValidateAc = view.findViewById(R.id.btnValidateAc);
            btnProceed = view.findViewById(R.id.btnProceed);
            btnDeleteP = view.findViewById(R.id.btnDeleteP);
            tvTxnId = view.findViewById(R.id.tvTxnId);
            tvName = view.findViewById(R.id.tvName);
            tvMobile = view.findViewById(R.id.tvMobile);
            tvAccount = view.findViewById(R.id.tvAccount);
            tvBank = view.findViewById(R.id.tvBank);
            llMobile = view.findViewById(R.id.llMobile);
            secBankContainer = view.findViewById(R.id.secBankContainer);
            tvBankStatus = view.findViewById(R.id.tvStatusP);
        }
    }
}
