package com.digital.payandserve.views.reports.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import  com.digital.payandserve.R;
import  com.digital.payandserve.app.Constants;
import  com.digital.payandserve.customer_care.AppCompain;
import  com.digital.payandserve.utill.AppHandler;
import  com.digital.payandserve.utill.MyUtil;
import  com.digital.payandserve.views.invoice.ReportInvoice;
import  com.digital.payandserve.views.reports.model.DMTModel;
import  com.digital.payandserve.views.reports.status.CheckStatus;

import java.util.List;

public class DMTTranAdapter extends RecyclerView.Adapter<DMTTranAdapter.MyViewHolder> {
    private Context mContext;
    private List<DMTModel> dataList;
    private String basePath;

    public DMTTranAdapter(Context mContext, List<DMTModel> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_report_dmt_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        DMTModel model = dataList.get(position);
        holder.tvId.setText(model.getId());
        holder.tvTxnId.setText(model.getTxnid());
        holder.tvMobile.setText(model.getMobile());
        holder.tvNumber.setText(model.getNumber());
        holder.tvCharge.setText(MyUtil.formatWithRupee(mContext, model.getCharge()));
        holder.tvBalance.setText(MyUtil.formatWithRupee(mContext, model.getBalance()));
        holder.TVAmount.setText(MyUtil.formatWithRupee(mContext, model.getAmount()));
        holder.tvProfit.setText(MyUtil.formatWithRupee(mContext, model.getProfit()));
        holder.tvGST.setText(MyUtil.formatWithRupee(mContext, model.getGst()));
        holder.tvTDS.setText(MyUtil.formatWithRupee(mContext, model.getTds()));
        holder.tvType.setText(model.getTransType());
        holder.tvStatus.setText(model.getStatus());
        holder.tvDateTime.setText(model.getCreatedAt());

        if (model.getStatus().contains("pending") || model.getStatus().contains("success") || model.getStatus().contains("initiated")) {
            holder.btnConfirm.setVisibility(View.VISIBLE);
        }else {
            holder.btnConfirm.setVisibility(View.GONE);
        }
        holder.btnComplain.setVisibility(View.VISIBLE);

        switch (model.getStatus()) {
            case "success":
            case "Success":
                holder.tvStatus.setBackground(mContext.getResources().getDrawable(R.drawable.success_border_green));
                break;
            case "failed":
            case "failure":
            case "fail":
            case "Failed":
                holder.tvStatus.setBackground(mContext.getResources().getDrawable(R.drawable.primary_border_red));
                holder.btnComplain.setVisibility(View.GONE);
                break;
            case "pending":
            case "Pending":
            case "reversed":
            case "refunded":
            case "REVERSED":
            case "REFUNDED":
                holder.tvStatus.setBackground(mContext.getResources().getDrawable(R.drawable.pending_border_orange));
        }

        holder.btnInvoice.setOnClickListener(v -> {
            AppHandler.initDMTReportData(model, mContext);
            Intent i = new Intent(mContext, ReportInvoice.class);
            i.putExtra("status", model.getStatus());
            i.putExtra("remark", "" + model.getRemark());
            mContext.startActivity(i);
        });

        holder.btnShare.setOnClickListener(v -> {
            AppHandler.printInvoiceFromAdapter(holder.cardView, mContext);
        });

        holder.btnComplain.setOnClickListener(v -> {
            Intent i = new Intent(mContext, AppCompain.class);
            if(model.getStatus().equalsIgnoreCase("success")){
                i.putExtra("status", "resolved");
            }else if(model.getStatus().equalsIgnoreCase("initiated")){
                i.putExtra("status", "pending");
            }
            i.putExtra("tranId", model.getId());
            i.putExtra("product", "aeps");
            mContext.startActivity(i);
        });

        holder.btnConfirm.setOnClickListener(v -> {
            Intent i = new Intent(mContext, CheckStatus.class);
            i.putExtra("typeValue", "money");
            i.putExtra("id", model.getId());
            i.putExtra("txnId", model.getTxnid());
            i.putExtra("url", Constants.URL.REPORT_CHECK_STATUS);
            mContext.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvId, tvTxnId, tvMobile, tvNumber, tvGST, tvTDS, TVAmount, tvCharge,
                tvBalance, tvType, tvStatus, tvDateTime, tvProfit, tvApiName;
        public ImageView btnInvoice, btnShare, btnConfirm,btnComplain;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            tvId = view.findViewById(R.id.idValue);
            tvTxnId = view.findViewById(R.id.tvtidValue);
            tvNumber = view.findViewById(R.id.tvNumber);
            tvGST = view.findViewById(R.id.tvGST);
            tvTDS = view.findViewById(R.id.tvTDS);
            cardView = view.findViewById(R.id.cardView);
            tvMobile = view.findViewById(R.id.tvMobile);
            TVAmount = view.findViewById(R.id.tvAmount);
            tvCharge = view.findViewById(R.id.tvCharge);
            tvBalance = view.findViewById(R.id.tvBalance);
            tvType = view.findViewById(R.id.tvType);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvDateTime = view.findViewById(R.id.tvDateTime);
            btnInvoice = view.findViewById(R.id.btnInvoice);
            btnConfirm = view.findViewById(R.id.btnConfirm);
            btnShare = view.findViewById(R.id.btnShare);
            tvProfit = view.findViewById(R.id.tvProfit);
            btnComplain = view.findViewById(R.id.btnComplain);
        }
    }

}