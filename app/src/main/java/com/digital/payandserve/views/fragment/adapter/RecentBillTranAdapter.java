package com.digital.payandserve.views.fragment.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.views.reports.model.BillRechargeModel;

import java.util.List;

public class RecentBillTranAdapter extends RecyclerView.Adapter<RecentBillTranAdapter.MyViewHolder> {
    private Context mContext;
    private List<BillRechargeModel> dataList;
    private String basePath;

    public RecentBillTranAdapter(Context mContext, List<BillRechargeModel> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_recharge_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        BillRechargeModel model = dataList.get(position);
        holder.tvMobile.setText(model.getNumber());
        String txt = "Recharge of " + MyUtil.formatWithRupee(mContext, model.getAmount()) + " done on " + model.getCreatedAt();
        holder.TVAmount.setText(txt);

        switch (model.getStatus()) {
            case "success":
            case "Success":
                holder.statusView.setBackgroundColor(mContext.getResources().getColor(R.color.green));
                break;
            case "failed":
            case "failure":
            case "fail":
            case "Failed":
                holder.statusView.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                break;
            default:
                holder.statusView.setBackgroundColor(mContext.getResources().getColor(R.color.bg_yellow));
        }

        MyUtil.setStaticProviderImg(model.getProvidername(), holder.imgProvider, mContext, "mobile");
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMobile, TVAmount, tvDateTime;
        public ImageView imgProvider;
        public View statusView;

        public MyViewHolder(View view) {
            super(view);
            tvMobile = view.findViewById(R.id.tvMobile);
            statusView = view.findViewById(R.id.statusView);
            imgProvider = view.findViewById(R.id.imgProvider);
            TVAmount = view.findViewById(R.id.TVAmount);
            tvDateTime = view.findViewById(R.id.tvDateTime);
        }
    }

}