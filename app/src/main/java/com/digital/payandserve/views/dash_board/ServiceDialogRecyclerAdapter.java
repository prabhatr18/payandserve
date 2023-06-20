package com.digital.payandserve.views.dash_board;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.utill.ExtensionFunction;
import com.digital.payandserve.views.dash_board.model.ServiceDialogModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ServiceDialogRecyclerAdapter extends RecyclerView.Adapter<ServiceDialogRecyclerAdapter.ServiceDialog_VH> {

    ArrayList<ServiceDialogModel> serviceList;
    OnServiceDialogItemClicked listner;

    public ServiceDialogRecyclerAdapter(ArrayList<ServiceDialogModel> serviceList, OnServiceDialogItemClicked listner) {
        this.serviceList = serviceList;
        this.listner = listner;
    }

    @NonNull
    @NotNull
    @Override
    public ServiceDialog_VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.serive_dialog_item,parent,false);
        return new ServiceDialog_VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ServiceDialogRecyclerAdapter.ServiceDialog_VH holder, int position) {
        ServiceDialogModel data = serviceList.get(position);
        holder.serviceName.setText(data.getTitle());
        holder.serviceIc.setImageResource(data.getImgIc());
        holder.selectServiceRadio.setChecked(data.getSelected());


        holder.dialogItemLayout.setOnClickListener(v ->{
            listner.onItemDialogItemClicked(position,holder.selectServiceRadio.isSelected(),data.getTitle());

        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    class ServiceDialog_VH extends RecyclerView.ViewHolder{

        ImageView serviceIc;
        TextView serviceName;
        ConstraintLayout dialogItemLayout;
        RadioButton selectServiceRadio;

        public ServiceDialog_VH(@NonNull @NotNull View itemView) {
            super(itemView);

            serviceIc = itemView.findViewById(R.id.serviceIc);
            serviceName = itemView.findViewById(R.id.serviceName);
            dialogItemLayout = itemView.findViewById(R.id.dialogItemLayout);
            selectServiceRadio = itemView.findViewById(R.id.selectServiceRadio);
        }
    }

    public  interface OnServiceDialogItemClicked{
        void onItemDialogItemClicked(int position,Boolean flag,String vlaue);
    }
}
