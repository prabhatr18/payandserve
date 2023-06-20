package com.digital.payandserve.views.reports.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.customer_care.AppCompain;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.views.invoice.ReportInvoice;
import com.digital.payandserve.views.reports.model.BillRechargeModel;
import com.digital.payandserve.views.reports.model.ticketModel.TicketModel;
import com.digital.payandserve.views.reports.status.CheckStatus;

import java.util.List;

public class SupportTicketTranAdapter extends RecyclerView.Adapter<SupportTicketTranAdapter.MyViewHolder> {
    private Context mContext;
    private List<TicketModel> dataList;

    public SupportTicketTranAdapter(Context mContext, List<TicketModel> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_report_support_ticket_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        TicketModel model = dataList.get(position);
        holder.tvId.setText(model.getId());
        holder.tvSubject.setText(model.getSubject());
        holder.tvDes.setText(model.getDescription());
        holder.tvUser.setText(model.getUser().getId());
        holder.tvMobile.setText(model.getUser().getMobile());
        holder.tvEmail.setText(model.getUser().getEmail());
        holder.tvStatus.setText(model.getStatus());
        holder.tvDateTime.setText(model.getCreatedAt());

        switch (model.getStatus()) {
            case "success":
            case "Success":
            case "RESOLVED":
            case "resolved":
                holder.tvStatus.setBackground(mContext.getResources().getDrawable(R.drawable.success_border_green));
                break;
            case "failed":
            case "failure":
            case "fail":
            case "Failed":
                holder.tvStatus.setBackground(mContext.getResources().getDrawable(R.drawable.primary_border_red));
                break;
            case "pending":
            case "Pending":
            case "reversed":
            case "refunded":
            case "REVERSED":
            case "REFUNDED":
                holder.tvStatus.setBackground(mContext.getResources().getDrawable(R.drawable.pending_border_orange));
        }

        holder.btnShare.setOnClickListener(v -> {
            AppHandler.printInvoiceFromAdapter(holder.cardView, mContext);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvId, tvSubject, tvDes, tvUser, tvMobile, tvEmail, tvDateTime,tvStatus;
        public ImageView btnShare;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            tvSubject = view.findViewById(R.id.tvSubject);
            tvStatus = view.findViewById(R.id.tvStatus);
            cardView = view.findViewById(R.id.cardView);
            tvId = view.findViewById(R.id.tvId);
            tvDes = view.findViewById(R.id.tvDes);
            tvUser = view.findViewById(R.id.tvUser);
            tvMobile = view.findViewById(R.id.tvMobile);
            tvEmail = view.findViewById(R.id.tvEmail);
            tvDateTime = view.findViewById(R.id.tvDateTime);
            tvDateTime = view.findViewById(R.id.tvDateTime);
            btnShare = view.findViewById(R.id.btnShare);
        }
    }

}