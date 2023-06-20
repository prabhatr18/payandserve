package com.digital.payandserve.views.moneytransfer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.views.moneytransfer.model.BenModel;

import java.util.List;

public class BenListAdapter extends RecyclerView.Adapter<BenListAdapter.MyViewHolder> {
    private Context mContext;
    private List<BenModel> dataList;
    private BenItemClick lis;

    public BenListAdapter(Context mContext, List<BenModel> dataList, BenItemClick lis) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.lis = lis;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dmt_ben_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        BenModel model = dataList.get(position);
        holder.tvTxnId.setText(model.getBeneid());
        holder.tvMobile.setText(model.getBenemobile());
        holder.tvName.setText(model.getBenename());
        holder.tvAccount.setText(model.getBeneaccount());
        holder.tvBank.setText(model.getBenebank());
        holder.btnProceed.setOnClickListener(v -> lis.benItemLis(model));
        if (model.getBenestatus().equalsIgnoreCase("NV")) {
            holder.btnProceed.setBackground(mContext.getResources().getDrawable(R.drawable.pending_border_orange));
            holder.btnProceed.setText("Varify");
        } else {
            holder.btnProceed.setBackground(mContext.getResources().getDrawable(R.drawable.success_border_green));
            holder.btnProceed.setText("Transfer");
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTxnId, tvMobile, tvName, tvAccount, tvBank;
        public Button btnProceed;

        public MyViewHolder(View view) {
            super(view);
            btnProceed = view.findViewById(R.id.btnProceed);
            tvTxnId = view.findViewById(R.id.tvTxnId);
            tvName = view.findViewById(R.id.tvName);
            tvMobile = view.findViewById(R.id.tvMobile);
            tvAccount = view.findViewById(R.id.tvAccount);
            tvBank = view.findViewById(R.id.tvBank);
        }
    }

    public interface BenItemClick {
        void benItemLis(BenModel model);
    }
}