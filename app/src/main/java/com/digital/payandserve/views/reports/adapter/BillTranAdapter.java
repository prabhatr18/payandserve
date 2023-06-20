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
import  com.digital.payandserve.views.reports.model.BillRechargeModel;
import  com.digital.payandserve.views.reports.status.CheckStatus;

import java.util.List;

public class BillTranAdapter extends RecyclerView.Adapter<BillTranAdapter.MyViewHolder> {
    private Context mContext;
    private List<BillRechargeModel> dataList;
    private String type;

    public BillTranAdapter(Context mContext, List<BillRechargeModel> dataList, String type) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_report_recharge_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        BillRechargeModel model = dataList.get(position);
        holder.tvTxnId.setText(model.getTxnid());
        holder.tvMobile.setText(model.getNumber());
        holder.tvCharge.setText(MyUtil.formatWithRupee(mContext, model.getCharge()));
        holder.tvBalance.setText(MyUtil.formatWithRupee(mContext, model.getBalance()));
        holder.TVAmount.setText(MyUtil.formatWithRupee(mContext, model.getAmount()));
        holder.tvProfit.setText(MyUtil.formatWithRupee(mContext, model.getProfit()));
        holder.tvStatus.setText(model.getStatus());
        holder.tvDateTime.setText(model.getCreatedAt());
        holder.btnConfirm.setOnClickListener(v -> {
            Intent i = new Intent(mContext, CheckStatus.class);
            i.putExtra("typeValue", "aeps");
            i.putExtra("id", model.getId());
            i.putExtra("txnId", model.getId());
            if (type.equalsIgnoreCase("billpaystatement"))
                i.putExtra("url", Constants.URL.BILLPAY_CHECK_STATUS);
            else
                i.putExtra("url", Constants.URL.RECHARGE_CHECK_STATUS);
            mContext.startActivity(i);
        });
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

        if (model.getStatus().contains("pending") || model.getStatus().contains("success") || model.getStatus().contains("initiated")) {
            holder.btnConfirm.setVisibility(View.VISIBLE);
        }else {
            holder.btnConfirm.setVisibility(View.GONE);
        }

        holder.btnInvoice.setOnClickListener(v -> {
            AppHandler.initBillReportData(model, mContext);
            Intent i = new Intent(mContext, ReportInvoice.class);
            i.putExtra("status", model.getStatus());
            i.putExtra("remark", "" + model.getRemark());
            mContext.startActivity(i);
        });



        holder.btnComplain.setOnClickListener(v -> {
            Intent i = new Intent(mContext, AppCompain.class);
            if(model.getStatus().equalsIgnoreCase("success")){
                i.putExtra("status", "resolved");
            }else if(model.getStatus().equalsIgnoreCase("initiated")){
                i.putExtra("status", "pending");
            }

            if (type.equalsIgnoreCase("billpaystatement"))
                i.putExtra("product", "billpay");
            else
                i.putExtra("product", "recharge");

            i.putExtra("tranId", model.getId());

            mContext.startActivity(i);
        });


        holder.btnShare.setOnClickListener(v -> {
            AppHandler.printInvoiceFromAdapter(holder.cardView, mContext);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTxnId, tvMobile, TVAmount, tvCharge, tvBalance, tvProfit, tvStatus,
                tvDateTime;
        public ImageView btnInvoice, btnShare, btnConfirm,btnComplain;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            tvTxnId = view.findViewById(R.id.tvTxnId);
            cardView = view.findViewById(R.id.cardView);
            tvMobile = view.findViewById(R.id.tvMobile);
            TVAmount = view.findViewById(R.id.TVAmount);
            tvCharge = view.findViewById(R.id.tvCharge);
            tvBalance = view.findViewById(R.id.tvBalance);
            tvProfit = view.findViewById(R.id.tvProfit);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvDateTime = view.findViewById(R.id.tvDateTime);
            btnInvoice = view.findViewById(R.id.btnInvoice);
            btnShare = view.findViewById(R.id.btnShare);
            btnConfirm = view.findViewById(R.id.btnConfirm);
            btnComplain = view.findViewById(R.id.btnComplain);
        }
    }

}