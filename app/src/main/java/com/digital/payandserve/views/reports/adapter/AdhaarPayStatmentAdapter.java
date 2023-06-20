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

import com.digital.payandserve.R;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.views.invoice.ReportInvoice;
import com.digital.payandserve.views.reports.model.AdharPayStatmentModel;

import java.util.List;

public class AdhaarPayStatmentAdapter extends RecyclerView.Adapter<AdhaarPayStatmentAdapter.MyViewHolder> {
    private Context mContext;
    private List<AdharPayStatmentModel> dataList;

    public AdhaarPayStatmentAdapter(Context mContext, List<AdharPayStatmentModel> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.aadhaar_statment_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        AdharPayStatmentModel model = dataList.get(position);

        holder.tvTxnId.setText(model.getTxnid());
        holder.tvName.setText(model.getUsername());
        holder.tvMobile.setText(model.getMobile());
        holder.tvAmount.setText(model.getAmount());
        holder.tvRefNo.setText(model.getRefno());
        holder.tvPayId.setText(model.getPayid());
        holder.tvCharge.setText(model.getCharge());
        holder.tvType.setText(model.getType());
        holder.tvStatus.setText(model.getStatus());
        holder.tvDateTime.setText(model.getCreatedAt());

        if (model.getStatus().equalsIgnoreCase("approved")||model.getStatus().equalsIgnoreCase("Success")) {
            holder.tvStatus.setBackground(mContext.getResources().getDrawable(R.drawable.success_border_green));
        } else {
            holder.tvStatus.setBackground(mContext.getResources().getDrawable(R.drawable.primary_border_red));
        }

        holder.btnShare.setOnClickListener(v -> {
            AppHandler.printInvoiceFromAdapter(holder.cardView, mContext);
        });

        holder.btnInvoice.setOnClickListener(v -> {
            AppHandler.initAadhaarReportData(model, mContext);
            Intent i = new Intent(mContext, ReportInvoice.class);
            i.putExtra("status", model.getStatus());
            i.putExtra("remark", "" + model.getRemark());
            mContext.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTxnId, tvName, tvMobile, tvAmount, tvRefNo, tvPayId, tvCharge, tvType, tvStatus, tvDateTime;
        public ImageView btnInvoice, btnShare,btnComplain;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);

            tvTxnId = view.findViewById(R.id.tvTxnId);
            tvName = view.findViewById(R.id.tvName);
            tvMobile = view.findViewById(R.id.tvMobile);
            tvAmount = view.findViewById(R.id.tvAmount);
            tvRefNo = view.findViewById(R.id.tvRefNo);
            tvPayId = view.findViewById(R.id.tvPayId);
            tvCharge = view.findViewById(R.id.tvCharge);
            tvType = view.findViewById(R.id.tvType);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvDateTime = view.findViewById(R.id.tvDateTime);

            btnInvoice = view.findViewById(R.id.btnInvoice);
            btnShare = view.findViewById(R.id.btnShare);
            btnComplain = view.findViewById(R.id.btnComplain);
            cardView = view.findViewById(R.id.cardView);
        }
    }

}