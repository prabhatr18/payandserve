package com.digital.payandserve.views.slider;

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
import com.digital.payandserve.utill.MyUtil;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.MyViewHolder> {
    private Context mContext;
    private List<String> dataList;
    private String basePath;

    public SliderAdapter(Context mContext, List<String> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_slider_item_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        String model = dataList.get(position);
        MyUtil.setGlideImage(model, holder.imageView, mContext);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTxnId, tvMobile, tvBCID, TVAmount, tvCharge, tvBalance, tvType, tvStatus,
                tvDateTime;
        public ImageView imageView, btnShare;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
        }
    }

}