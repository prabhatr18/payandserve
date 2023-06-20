package com.digital.payandserve.views.operator.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.digital.payandserve.R;
import com.digital.payandserve.app.AppController;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.views.operator.model.OperatorModel;

import java.util.List;

public class OperatorListAdapter extends RecyclerView.Adapter<OperatorListAdapter.MyViewHolder> {
    private Context mContext;
    private List<OperatorModel> dataList;
    private String basePath;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private String type;

    public OperatorListAdapter(Context mContext, List<OperatorModel> dataList, String type) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.type = type;
    }

    public void UpdateList(List<OperatorModel> temp) {
        this.dataList = temp;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (type.equals("getbank")){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.operator_bank_row, parent, false);
        }else{
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.operator_item, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        OperatorModel model = dataList.get(position);
        holder.tvTitle.setText(model.getName());
        if (type.equalsIgnoreCase("getbank")) {
            holder.thumbnail.setImageDrawable(mContext.getDrawable(R.drawable.ic_bank_app));
            MyUtil.setGlideImage(model.getLogo(), holder.thumbnail, mContext);
        } else {
            MyUtil.setProviderImage(model.getLogo(), holder.thumbnail, model.getName(), mContext, type);
        }
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