package com.digital.payandserve.views.allservices_search.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.model.ActivityListModel;

import java.util.List;

public class ServiceSearchAdapter extends RecyclerView.Adapter<ServiceSearchAdapter.MyViewHolder> {
    private Context mContext;
    private List<ActivityListModel> dataList;
    public ServiceSearchAdapter(Context mContext, List<ActivityListModel> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    public void UpdateList(List<ActivityListModel> temp) {
        this.dataList = temp;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.operator_bank_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ActivityListModel model = dataList.get(position);
        holder.tvTitle.setText(model.getTxt1());
        holder.thumbnail.setImageResource(model.getImage1());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            thumbnail = view.findViewById(R.id.thumbnail);
        }
    }
}