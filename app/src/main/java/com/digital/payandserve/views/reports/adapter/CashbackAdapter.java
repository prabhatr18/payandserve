package com.digital.payandserve.views.reports.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.views.reports.model.CashbackModel;

import java.util.List;

public class CashbackAdapter extends RecyclerView.Adapter<CashbackAdapter.MyViewHolder> {
    private Context mContext;
    private List<CashbackModel> dataList;
    private String basePath;

    public CashbackAdapter(Context mContext, List<CashbackModel> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cashback_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        CashbackModel model = dataList.get(position);

        holder.tvId.setText(model.getId());
        holder.tvType.setText(model.getType());
        holder.tvService.setText(model.getService());
        holder.tvValue.setText(model.getValue());
        holder.tvCashType.setText(model.getCashbacktype());
        holder.tvCashValue.setText(model.getCashbackvalue());
        holder.tvValidFor.setText(model.getValidfor());
        holder.tvWalletMain.setText(model.getWallet());
        holder.tvDateTime.setText(model.getCreated_at());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvId, tvType, tvService, tvValue, tvCashType, tvCashValue, tvValidFor, tvWalletMain, tvDateTime;
        public ImageView btnShare;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);

            tvId = view.findViewById(R.id.tvId);
            tvService = view.findViewById(R.id.tvService);
            tvType = view.findViewById(R.id.tvType);
            tvValue = view.findViewById(R.id.tvValue);
            tvCashType = view.findViewById(R.id.tvCashType);
            tvCashValue = view.findViewById(R.id.tvCashValue);
            tvValidFor = view.findViewById(R.id.tvValidFor);
            tvWalletMain = view.findViewById(R.id.tvWalletMain);

            tvDateTime = view.findViewById(R.id.tvDateTime);
            btnShare = view.findViewById(R.id.btnShare);


        }
    }

}