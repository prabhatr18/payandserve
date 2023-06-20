package com.digital.payandserve.views.contact;

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

import java.util.List;

public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.MyViewHolder> {
    private Context mContext;
    private List<ContactNumberModel> dataList;

    public ContactsListAdapter(Context mContext, List<ContactNumberModel> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    public static boolean isAlpha(String s) {
        if (s == null) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!(c >= 'A' && c <= 'Z') && !(c >= 'a' && c <= 'z')) {
                return false;
            }
        }
        return true;
    }

    public void UpdateList(List<ContactNumberModel> temp) {
        this.dataList = temp;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.android_contact_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ContactNumberModel model = dataList.get(position);
        holder.tvName.setText(model.getUserName());
        holder.tvContact.setText(model.getContact());
        holder.cardView.setCardBackgroundColor(model.getImg());
        try {
            if (isAlpha(model.getUserName().substring(0, 1))) {
                holder.tv.setText(model.getUserName().substring(0, 1));
            } else {
                holder.tv.setText(model.getUserName().substring(1, 2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvContact, tv;
        public ImageView btnInvoice, btnShare;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tv = view.findViewById(R.id.tv);
            tvContact = view.findViewById(R.id.tvContact);
            cardView = view.findViewById(R.id.imgCard);
        }
    }

}