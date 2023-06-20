package com.digital.payandserve.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.model.ActivityListModel;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private Context context;
    private List<ActivityListModel> activityListModels;
    public ItemClickGrid lis;

    public HomeAdapter(Context context, List<ActivityListModel> activityListModels) {
        this.context = context;
        this.activityListModels = activityListModels;
    }

    public HomeAdapter(Context context, List<ActivityListModel> activityListModels, ItemClickGrid lis) {
        this.context = context;
        this.activityListModels = activityListModels;
        this.lis = lis;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ActivityListModel mo = activityListModels.get(position);
        holder.name.setText(activityListModels.get(position).getTxt1());
        holder.image.setImageResource(activityListModels.get(position).getImage1());
        if (lis != null)
            holder.cardView.setOnClickListener(v -> {
                Animation a = AnimationUtils.loadAnimation(context, R.anim.zoom_effect_mitra);
                holder.image.startAnimation(a);
                //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(130, 130);
                //holder.image.setLayoutParams(layoutParams);
                //lis.gridItemClick(position, mo);
            });
    }

    @Override
    public int getItemCount() {
        return activityListModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;
        LinearLayout cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    public interface ItemClickGrid {
        void gridItemClick(int position, ActivityListModel m);
    }
}
