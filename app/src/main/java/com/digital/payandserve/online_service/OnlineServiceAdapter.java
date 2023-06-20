package com.digital.payandserve.online_service;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.online_service.model.OnlienServiceModel;

import java.util.ArrayList;

public class OnlineServiceAdapter extends RecyclerView.Adapter<OnlineServiceAdapter.OnlineService_VH> {

    ArrayList<OnlienServiceModel> list;
    OnlineServiceClickListner listner;

    public OnlineServiceAdapter(ArrayList<OnlienServiceModel> list, OnlineServiceClickListner listner) {
        this.list = list;
        this.listner = listner;
    }

    @NonNull
    @Override
    public OnlineService_VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.our_service_item
                , parent, false);
        return new OnlineService_VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnlineService_VH holder, int position) {
            OnlienServiceModel data = list.get(position);

            holder.mainLayout.setOnClickListener(v -> {
                listner.onlineServiceItemClicked(position,data.getMsg(),data.getType(),data.getOpenInExternalBrowser());
            });

            holder.name.setText(data.getTitle());
        holder.image.setImageDrawable(data.getIcon());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class OnlineService_VH extends RecyclerView.ViewHolder{

        TextView name;
        LinearLayout mainLayout;
        ImageView image;
        public OnlineService_VH(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            image = itemView.findViewById(R.id.image);
        }
    }

    public interface OnlineServiceClickListner{
        void onlineServiceItemClicked(int postion,String msg,String type,Boolean openWithBrowser);
    }
}
