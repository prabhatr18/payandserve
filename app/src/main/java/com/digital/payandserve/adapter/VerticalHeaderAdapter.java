package com.digital.payandserve.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.model.ActivityListModel;

import java.util.List;

public class VerticalHeaderAdapter extends RecyclerView.Adapter<VerticalHeaderAdapter.ViewHolder> {

    private Context context;
    private List<ActivityListModel> activityListModels;

    public VerticalHeaderAdapter(Context context, List<ActivityListModel> activityListModels) {
        this.context = context;
        this.activityListModels = activityListModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_transfermoney_row_vertical_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.name.setText(activityListModels.get(position).getTxt1());
        holder.image.setImageResource(activityListModels.get(position).getImage1());
    }

    @Override
    public int getItemCount() {
        return activityListModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
        }
    }
}
