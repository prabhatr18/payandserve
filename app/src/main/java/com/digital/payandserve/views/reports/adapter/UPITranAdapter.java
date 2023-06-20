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
import  com.digital.payandserve.customer_care.AppCompain;
import  com.digital.payandserve.utill.AppHandler;
import  com.digital.payandserve.utill.MyUtil;
import  com.digital.payandserve.views.invoice.ReportInvoice;
import  com.digital.payandserve.views.reports.model.UPIModel;
import  com.digital.payandserve.views.reports.status.UPICheckStatus;

import java.util.List;

public class UPITranAdapter extends RecyclerView.Adapter<UPITranAdapter.MyViewHolder> {
    private Context mContext;
    private List<UPIModel> dataList;
    private String basePath;

    public UPITranAdapter(Context mContext, List<UPIModel> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.upi_transaction_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        UPIModel model = dataList.get(position);
        holder.tvId.setText(model.getId());
        holder.tvTxnId.setText(model.getTxnid());
        holder.tvMobile.setText(model.getMobile());
        holder.tvCharge.setText(MyUtil.formatWithRupee(mContext, model.getCharge()));
        holder.tvBalance.setText(MyUtil.formatWithRupee(mContext, model.getBalance()));
        holder.TVAmount.setText(MyUtil.formatWithRupee(mContext, model.getAmount()));
        holder.tvType.setText(model.getType());
        holder.tvStatus.setText(model.getStatus());
        holder.tvDateTime.setText(model.getCreatedAt());

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
            default:
                holder.tvStatus.setBackground(mContext.getResources().getDrawable(R.drawable.pending_border_orange));
        }

        holder.btnInvoice.setOnClickListener(v -> {
            AppHandler.initUPIReport(model, mContext);
            Intent i = new Intent(mContext, ReportInvoice.class);
            i.putExtra("status", model.getStatus());
            i.putExtra("remark", "" + model.getRemark());
            mContext.startActivity(i);
        });

        holder.btnShare.setOnClickListener(v -> {
            AppHandler.printInvoiceFromAdapter(holder.cardView, mContext);
        });

        holder.btnConfirm.setOnClickListener(v -> {
            Intent i = new Intent(mContext, UPICheckStatus.class);
            i.putExtra("txnId", model.getTxnid());
            mContext.startActivity(i);
        });
        holder.btnComplain.setVisibility(View.GONE);
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
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvId, tvTxnId, tvMobile, TVAmount, tvCharge,
                tvBalance, tvType, tvStatus, tvDateTime;
        public ImageView btnInvoice, btnShare, btnConfirm,btnComplain;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            tvId = view.findViewById(R.id.idValue);
            tvTxnId = view.findViewById(R.id.tvtidValue);
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
            btnComplain = view.findViewById(R.id.btnComplain);
        }
    }

}