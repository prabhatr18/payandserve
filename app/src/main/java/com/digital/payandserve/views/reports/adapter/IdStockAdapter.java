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
import com.digital.payandserve.views.reports.model.IdStockModel;

import java.util.List;

public class IdStockAdapter extends RecyclerView.Adapter<IdStockAdapter.MyViewHolder> {
    private Context mContext;
    private List<IdStockModel> dataList;
    private String basePath;

    public IdStockAdapter(Context mContext, List<IdStockModel> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.id_stock_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        IdStockModel model = dataList.get(position);

        holder.tvId.setText(model.getId());
        holder.tvUserDetail.setText(model.getDeductuseruser());
        holder.tvIdDebitFrom.setText(model.getUsername());
        holder.tvNumberOfId.setText(model.getNumberof_id());
        holder.tvIdType.setText(model.getId_type());
        holder.tvIdRemaining.setText(model.getId_count());
        holder.tvDateTime.setText(model.getCreated_at());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvId, tvUserDetail, tvIdDebitFrom, tvNumberOfId, tvIdType, tvIdRemaining, tvDateTime;
        public ImageView btnShare;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);

            tvId = view.findViewById(R.id.tvId);
            tvUserDetail = view.findViewById(R.id.tvUserDetail);
            tvIdDebitFrom = view.findViewById(R.id.tvIdDebitFrom);
            tvNumberOfId = view.findViewById(R.id.tvNumberOfId);
            tvIdType = view.findViewById(R.id.tvIdType);
            tvIdRemaining = view.findViewById(R.id.tvIdRemaining);
            tvDateTime = view.findViewById(R.id.tvDateTime);
            btnShare = view.findViewById(R.id.btnShare);


        }
    }

}