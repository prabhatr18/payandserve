package com.digital.payandserve.views.dmt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

public class BenLIstAdapterPgout extends RecyclerView.Adapter<BenLIstAdapterPgout.MyViewHolder> {
    private Context mContext;
    private List<BenedatumPgPayout> dataList;
    private BenItemClick lis;

    public BenLIstAdapterPgout(Context mContext, List<BenedatumPgPayout> dataList, BenItemClick lis) {
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
        BenedatumPgPayout model = dataList.get(position);
        holder.tvTxnId.setText(model.getBeneId());
//        holder.tvMobile.setText(model.get());
        holder.tvName.setText(model.getName());
        holder.tvAccount.setText(model.getAccno());
        holder.tvBank.setText(model.getBankname());
        holder.tvBankIfsc.setText(model.getIfsc());

        Log.d("Response Bankname:", "" + model.getBankname());
        Log.d("Response name:", "" + model.getName());
        Log.d("Response ifsc:", "" + model.getIfsc());
        Log.d("Response acc:", "" + model.getAccno());
        Log.d("Response beneId:", "" + model.getBeneId());
        Log.d("Response bankId:", "" + model.getBankid());


        if (model.getVerified().equalsIgnoreCase("1")) {
            holder.tvStatusP.setText("Verify");
        } else {
            holder.tvStatusP.setText("Not Verify");
        }
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
        holder.btnProceed.setOnClickListener(v -> lis.benItemLisPgPayout(model));
        holder.btnDeleteP.setOnClickListener(v -> lis.benDelPGpayout(model));
//        if (model.getVerified().equalsIgnoreCase("0")) {
//            holder.btnProceed.setBackground(mContext.getResources().getDrawable(R.drawable.pending_border_orange));
//            holder.btnProceed.setText("Verify");
//
//        } else {
        holder.btnProceed.setBackground(mContext.getResources().getDrawable(R.drawable.success_border_green));
        holder.btnProceed.setText("Transfer");

        holder.btnValidateAc.setOnClickListener(v -> {

            String url = Constants.URL.BASE_URL+"api/android/pdmt/transaction";

            RequestQueue queue = Volley.newRequestQueue(mContext);

           /* ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Please wait...!");
            progressDialog.show();*/

            Log.d("Response URL :", "" + url);

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                  //  progressDialog.dismiss();
                    Log.d("Response :", "" + response);
                    //  Toast.makeText(context, "Data added to API", Toast.LENGTH_SHORT).show();
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("statuscode");

                        if (status.equals("TXN")) {
                            //  Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                            Log.d("Response :", "" + "Success");
                        } else {
                            Toast.makeText((Context) mContext, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                  //  progressDialog.dismiss();
                    Log.d("Response error:", "" + ""+error);

                    // method to handle errors.
                    //  Toast.makeText(context, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", SharedPrefs.getValue(mContext, SharedPrefs.USER_ID));
                    params.put("apptoken", SharedPrefs.getValue(mContext,SharedPrefs.APP_TOKEN));
                    params.put("type" , "accountverification");
                    params.put("mobile" , "9918881317");
                    params.put("benebank" , "1");
                    params.put("beneaccount" , "1");
                    params.put("beneifsc" , "1");
                    params.put("benename" , "1");
                    params.put("bene_id" , "1"+model.getBeneId());

                    Log.d("Response PARAMS :", "" + params);
                    return params;
                }
            };
            queue.add(request);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface BenItemClick {
        void benItemLisPgPayout(BenedatumPgPayout model);

        void benDelPGpayout(BenedatumPgPayout model);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTxnId, tvMobile, tvName, tvAccount, tvBank, tvStatusP,tvBankIfsc;
        public Button btnProceed, btnDeleteP,btnValidateAc;
        public LinearLayout secBankContainer, llMobile;

        public MyViewHolder(View view) {
            super(view);
            tvBankIfsc = view.findViewById(R.id.tvBankIfsc);
            btnValidateAc = view.findViewById(R.id.btnValidateAc);
            btnProceed = view.findViewById(R.id.btnProceed);
            tvStatusP = view.findViewById(R.id.tvStatusP);
            btnDeleteP = view.findViewById(R.id.btnDeleteP);
            tvTxnId = view.findViewById(R.id.tvTxnId);
            tvName = view.findViewById(R.id.tvName);
            tvMobile = view.findViewById(R.id.tvMobile);
            tvAccount = view.findViewById(R.id.tvAccount);
            tvBank = view.findViewById(R.id.tvBank);
            llMobile = view.findViewById(R.id.llMobile);
            secBankContainer = view.findViewById(R.id.secBankContainer);
        }
    }
}
