package com.digital.payandserve.views.billpayment.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.app.Constants;

import java.util.List;

public class TopHeadBillPaySlider extends RecyclerView.Adapter<TopHeadBillPaySlider.MyViewHolder> {
    private Context mContext;
    private List<String> dataList;
    private String basePath;

    public TopHeadBillPaySlider(Context mContext, List<String> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.top_bill_pay_slider_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final String model = dataList.get(position);
        holder.tvLabel.setText(model);
        if (Constants.GLOBAL_POSITION == position){
            holder.tvLabel.setBackground(mContext.getResources().getDrawable(R.drawable.item_slider_bill_pay_top_selected));
            holder.tvLabel.setTextColor(mContext.getResources().getColor(R.color.textRed));
        }else{
            holder.tvLabel.setBackground(mContext.getResources().getDrawable(R.drawable.item_slider_bill_pay_top_deselected));
            holder.tvLabel.setTextColor(mContext.getResources().getColor(R.color.textGray));
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvLabel;
        public MyViewHolder(View view) {
            super(view);
            tvLabel = view.findViewById(R.id.tvText);
        }
    }

}