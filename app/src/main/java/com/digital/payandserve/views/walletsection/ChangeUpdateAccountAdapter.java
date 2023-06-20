package com.digital.payandserve.views.walletsection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.views.walletsection.model.ChangeUpdateModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChangeUpdateAccountAdapter extends RecyclerView.Adapter<ChangeUpdateAccountAdapter.Change_VH> {

    ArrayList<ChangeUpdateModel> list;
    OnUpdateAccountListner listner;

    public ChangeUpdateAccountAdapter(ArrayList<ChangeUpdateModel> list, OnUpdateAccountListner listner) {
        this.list = list;
        this.listner = listner;
    }

    @NonNull
    @NotNull
    @Override
    public Change_VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.change_account_item, parent, false);
        return new Change_VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChangeUpdateAccountAdapter.Change_VH holder, int position) {
        ChangeUpdateModel data = list.get(position);

        holder.tvBankType.setText(data.getType());
        holder.tvBankName.setText(data.getBankName());
        holder.tvAccount.setText(data.getAccountNumber());
        holder.tvIfsc.setText(data.getIfsc());

        holder.mainLayout.setOnClickListener(v -> {
            listner.onMainLayoutClicked(position, data.getType(), data.getBankName(), data.getAccountNumber(), data.getIfsc());
        });

        holder.updateAccount.setOnClickListener(v -> {
            listner.onUpdateButtonClicked(position, data.getType(), data.getBankName(), data.getAccountNumber(), data.getIfsc());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnUpdateAccountListner {

        void onMainLayoutClicked(int postion, String type, String bankName, String account, String ifsc);

        void onUpdateButtonClicked(int postion, String type, String bankName, String account, String ifsc);
    }

    class Change_VH extends RecyclerView.ViewHolder {

        TextView tvBankType, tvBankName, tvAccount, tvIfsc;
        LinearLayout mainLayout;
        Button updateAccount;

        public Change_VH(@NonNull @NotNull View itemView) {
            super(itemView);

            tvBankType = itemView.findViewById(R.id.tvBankType);
            tvBankName = itemView.findViewById(R.id.tvBankName);
            tvAccount = itemView.findViewById(R.id.tvAccount);
            tvIfsc = itemView.findViewById(R.id.tvIfsc);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            updateAccount = itemView.findViewById(R.id.updateAccount);


        }
    }
}
