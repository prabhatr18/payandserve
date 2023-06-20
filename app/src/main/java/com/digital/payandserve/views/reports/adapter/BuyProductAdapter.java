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
import com.digital.payandserve.views.reports.model.BuyProductModel;

import java.util.List;

public class BuyProductAdapter extends RecyclerView.Adapter<BuyProductAdapter.MyViewHolder> {
    private Context mContext;
    private List<BuyProductModel> dataList;
    private String basePath;

    public BuyProductAdapter(Context mContext, List<BuyProductModel> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_report_product_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        BuyProductModel model = dataList.get(position);
        holder.tvId.setText(model.getId());
        holder.TVbalance.setText(model.getBalance());
        holder.tvprice.setText(model.getPrice());
        holder.tvnumber.setText(model.getNumber());
        holder.tvproduct.setText(model.getProduct());
        holder.tvStatus.setText(model.getStatus());
        holder.tvDateTime.setText(model.getCreatedAt());
        if (model.getStatus().equalsIgnoreCase("approved")) {
            holder.tvStatus.setBackground(mContext.getResources().getDrawable(R.drawable.success_border_green));
        } else {
            holder.tvStatus.setBackground(mContext.getResources().getDrawable(R.drawable.primary_border_red));
        }

        holder.btnInvoice.setOnClickListener(v -> {
            AppHandler.initBuyProductFundRequestReportReportData(model, mContext);
            Intent i = new Intent(mContext, ReportInvoice.class);
            i.putExtra("status", model.getStatus());
            i.putExtra("remark", "");
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
        public TextView tvId, TVbalance, tvproduct, tvnumber, tvprice, tvStatus, tvDateTime;
        public ImageView btnInvoice, btnShare;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            tvId = view.findViewById(R.id.tvId);
            cardView = view.findViewById(R.id.cardView);
            TVbalance = view.findViewById(R.id.TVbalance);
            tvproduct = view.findViewById(R.id.tvproduct);
            tvnumber = view.findViewById(R.id.tvnumber);
            tvprice = view.findViewById(R.id.tvprice);
//            tvType = view.findViewById(R.id.tvType);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvDateTime = view.findViewById(R.id.tvDateTime);
            btnInvoice = view.findViewById(R.id.btnInvoice);
            btnShare = view.findViewById(R.id.btnShare);
        }
    }

}