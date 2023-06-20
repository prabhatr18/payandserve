package com.digital.payandserve.views.member.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.views.member.model.AppMember;
import com.digital.payandserve.views.reports.AEPSTransaction;
import com.digital.payandserve.views.reports.MainWalletFundReqStatement;

import java.util.List;

public class AllMemberAdapter extends RecyclerView.Adapter<AllMemberAdapter.ViewHolder> {

    private Context context;
    private List<AppMember> activityListModels;
    private ItemClick clickLis;

    public AllMemberAdapter(Context context, List<AppMember> activityListModels, ItemClick clickLis) {
        this.context = context;
        this.activityListModels = activityListModels;
        this.clickLis = clickLis;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_member_list_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        AppMember model = activityListModels.get(position);
        holder.tvCity.setText(model.getCity());
        holder.tvEmail.setText(model.getEmail());
        holder.tvMobile.setText(model.getMobile());
        holder.tvName.setText(model.getName());
        holder.tvParent.setText(model.getParents().replaceAll("<br>", "  "));
        holder.btnAepsReport.setOnClickListener(v -> {
            Intent i = new Intent(context, AEPSTransaction.class);
            i.putExtra("title", "AEPS Wallet Statement");
            i.putExtra("type", "awalletstatement");
            i.putExtra("agent", model.getId());
            context.startActivity(i);
        });

        holder.btnWalletReport.setOnClickListener(v -> {
            Intent i = new Intent(context, MainWalletFundReqStatement.class);
            i.putExtra("title", "Main Wallet Statement");
            i.putExtra("type", "fundrequest");
            i.putExtra("agent", model.getId());
            context.startActivity(i);
        });

        holder.imgRec.setOnClickListener(v -> clickLis.clickRec(model));
        holder.imgSend.setOnClickListener(v -> clickLis.clickSend(model));
    }

    @Override
    public int getItemCount() {
        return activityListModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvMobile, tvCity, tvParent;
        ImageView imgRec, btnAepsReport, btnWalletReport, imgSend;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvParent = itemView.findViewById(R.id.tvParent);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvMobile = itemView.findViewById(R.id.tvMobile);
            tvCity = itemView.findViewById(R.id.tvCity);
            imgRec = itemView.findViewById(R.id.btnRec);
            imgSend = itemView.findViewById(R.id.imgSend);
            btnAepsReport = itemView.findViewById(R.id.btnAepsReport);
            btnWalletReport = itemView.findViewById(R.id.btnWalletReport);
        }
    }

    public interface ItemClick {
        void clickRec(AppMember model);

        void clickSend(AppMember model);
    }
}
