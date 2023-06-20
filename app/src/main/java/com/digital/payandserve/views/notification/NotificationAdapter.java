package com.digital.payandserve.views.notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.database.entity.NotificationTable;

import java.util.List;

public abstract class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private List<NotificationTable> activityListModels;

    public NotificationAdapter(Context context, List<NotificationTable> activityListModels) {
        this.context = context;
        this.activityListModels = activityListModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commition_data_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        NotificationTable mo = activityListModels.get(position);
        holder.tvData.setText(mo.getName() + "\n" + mo.getDetail());
        holder.imgDelete.setVisibility(View.VISIBLE);
        holder.imgDelete.setOnClickListener(v -> onClickDelete(mo));
    }

    @Override
    public int getItemCount() {
        return activityListModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvData;
        ImageView imgDelete;
        //ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            tvData = itemView.findViewById(R.id.tvData);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }

    abstract void onClickDelete(NotificationTable model);

    public void setData(List<NotificationTable> activityListModels){
        this.activityListModels = activityListModels;
        notifyDataSetChanged();
    }
}
