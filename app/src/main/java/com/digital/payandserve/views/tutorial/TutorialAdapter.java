package com.digital.payandserve.views.tutorial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.views.tutorial.model.TutorialModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TutorialAdapter extends RecyclerView.Adapter<TutorialAdapter.Tutorial_VH> {

    ArrayList<TutorialModel> tutorialList;
    OnTutorialItemClickListner listner;

    public TutorialAdapter(ArrayList<TutorialModel> tutorialList, OnTutorialItemClickListner listner) {
        this.tutorialList = tutorialList;
        this.listner = listner;
    }


    @NonNull
    @NotNull
    @Override
    public Tutorial_VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tutorial_item_view,parent,false);
        return new Tutorial_VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TutorialAdapter.Tutorial_VH holder, int position) {
        TutorialModel data = tutorialList.get(position);
        holder.videoTitle.setText(data.getTitle());
        holder.videoDescription.setText(data.getDescription());
        holder.tutorialCard.setOnClickListener(v -> {
            listner.onTutorialItemClicked(position,data.getValue());
        });
    }

    @Override
    public int getItemCount() {
        return tutorialList.size();
    }

    class Tutorial_VH extends RecyclerView.ViewHolder{

        CardView tutorialCard;
        TextView videoTitle,videoDescription;

        public Tutorial_VH(@NonNull @NotNull View itemView) {
            super(itemView);

            tutorialCard = itemView.findViewById(R.id.tutorialCard);
            videoTitle = itemView.findViewById(R.id.videoTitle);
            videoDescription = itemView.findViewById(R.id.videoDescription);

        }
    }

    interface OnTutorialItemClickListner{
        void onTutorialItemClicked(int position,String url);
    }
}


