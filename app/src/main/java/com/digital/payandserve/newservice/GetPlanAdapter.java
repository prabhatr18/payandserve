package com.digital.payandserve.newservice;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.utill.ExtensionFunction;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.utill.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetPlanAdapter extends RecyclerView.Adapter<GetPlanAdapter.ViewHolder> {

    private Context context;
    private List<GetPlanListModel> getPlanListModels;
    Dialog dialog;
    String loanId;

    public GetPlanAdapter(Context context, List<GetPlanListModel> getPlanListModels, String loanId) {
        this.context = context;
        this.getPlanListModels = getPlanListModels;
        this.loanId=loanId;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.get_plan_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        GetPlanListModel getPlanList = getPlanListModels.get(position);

        Log.d("OnBoard AdapterData :", getPlanList.description);

        holder.nameTv.setText(getPlanList.getName());
        holder.minMaxAmtTv.setText(getPlanList.getMinAmt() + "-" + getPlanList.getMaxAmt());
        holder.emiAmtTv.setText(getPlanList.getEmiAmt());
        holder.emiTotalTv.setText(getPlanList.getEmiTotal());
        holder.interestTv.setText(getPlanList.getInterest());
        holder.processingFeeTv.setText(getPlanList.getProcessingFee());
        holder.descriptionTv.setText(getPlanList.getDescription());

        holder.getLoanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.amountET.getText().toString().trim().equals("")){

                    ExtensionFunction.checkValidation(holder.amountET, "Enter loan amount");

                    Toast.makeText(context, "Enter loan amount", Toast.LENGTH_SHORT).show();

                }else {
                    int amt = Integer.parseInt(holder.amountET.getText().toString().trim());
                    int minAmt = Integer.parseInt(getPlanList.getMinAmt());
                    int maxAmt = Integer.parseInt(getPlanList.getMaxAmt());


                    if (AppManager.isOnline(context)) {

                        if (amt < minAmt) {

                            ExtensionFunction.showToast(context, "Amount should be greater or equal to " + minAmt);


                        } else if (amt > maxAmt) {

                            ExtensionFunction.showToast(context, "Amount should be less or equal to " + maxAmt);

                        } else {
                            String url = Constants.URL.BASE_URL + "api/android/paylater/payment";
                            //networkCallUsingVolleyApi(param(getPlanList.getId()), url);
                            initiateLoan(amt,getPlanList.getId(), url);

                        }


                    } else {
                        Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

    }

    private void initiateLoan(int amt, String id, String url) {


        showLoader((Activity) context);
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                closeLoader();
                Log.d("Loan onSuccess :", "" + response);

                try {
                    String msg = "some error accor";
                    JSONObject jsonObject = new JSONObject(response);
                    String statuscode = jsonObject.getString("statuscode");

                    if (statuscode.equalsIgnoreCase("TXN")) {

                        if (statuscode.equalsIgnoreCase("TXN")) {
                            msg = "Transaction Successful";

                            String redirecturl = jsonObject.getString("redirecturl");

                            Intent intent = new Intent(context, InitiateLoanWebviewActivity.class);
                            intent.putExtra("url", "" + redirecturl);
                            context.startActivity(intent);

                            Log.d("Loan Param if :", "" + redirecturl);

                        } else {
                            Log.d("Loan Param :", "" + msg);
                            Toast.makeText(context, "Error : " + msg, Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        ExtensionFunction.showToast(context, "" + msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeLoader();
                Toast.makeText(context, "some error accor please try again..", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                String token = SharedPrefs.getValue(context, SharedPrefs.APP_TOKEN);
                String userId = SharedPrefs.getValue(context, SharedPrefs.USER_ID);
                String loanAmt=String.valueOf(amt);

                Map<String, String> map = new HashMap<String, String>();

                map.put("user_id", userId);
                map.put("type", "loaninitiate");
                map.put("mobile", loanId);
                map.put("id", id);
                map.put("amount", loanAmt);
                map.put("apptoken", token);
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);

    }

    private void showLoader(Activity context) {
        Print.P("DIALOG" + "DIALOG OPEN");
        closeLoader();
        if (context.isDestroyed())
            return;
        dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loader_layout);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        LottieAnimationView lottie;
        TextView tvStatus;
        ImageView imgClose, imgShare, imgMinimise;
        LinearLayout loaderCon;
        String error = "";
        AppCompatButton loginButton;

        loaderCon = dialog.findViewById(R.id.loaderCon);
        imgShare = dialog.findViewById(R.id.imgShare);
        imgClose = dialog.findViewById(R.id.imgClose);
        tvStatus = dialog.findViewById(R.id.bottomTv);
        loginButton = dialog.findViewById(R.id.loginButton);
        imgMinimise = dialog.findViewById(R.id.imgMinimise);
        imgClose.setOnClickListener(v -> {
            closeLoader();
            context.finish();
        });
        imgMinimise.setOnClickListener(v -> closeLoader());
        loginButton.setVisibility(View.GONE);
        loginButton.setOnClickListener(v -> {
            closeLoader();
            AppManager.getInstance().logoutFromServer(context, false);
        });
        imgShare.setOnClickListener(v -> MyUtil.shareOnAllApps(context, ""));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void closeLoader() {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    @Override
    public int getItemCount() {
        return getPlanListModels.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv, minMaxAmtTv, interestTv, emiAmtTv, emiTotalTv, processingFeeTv, descriptionTv;
        TextView getLoanBtn;
        EditText amountET;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.nameTv);
            minMaxAmtTv = (TextView) itemView.findViewById(R.id.minMaxAmtTv);
            interestTv = (TextView) itemView.findViewById(R.id.interestTv);
            emiAmtTv = (TextView) itemView.findViewById(R.id.emiAmtTv);
            emiTotalTv = (TextView) itemView.findViewById(R.id.emiTotalTv);
            processingFeeTv = (TextView) itemView.findViewById(R.id.processingFeeTv);
            descriptionTv = (TextView) itemView.findViewById(R.id.descriptionTv);
            getLoanBtn = (TextView) itemView.findViewById(R.id.getLoanBtn);
            amountET = (EditText) itemView.findViewById(R.id.amountET);

        }
    }

}
