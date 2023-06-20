package com.digital.payandserve.views.dmt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GRAdapter<T> extends RecyclerView.Adapter {

    private final List<T> itemList;
    private final int layoutId;
    private final OnItemBindListener<T> onItemBindListener;

    public GRAdapter(List<T> itemList, int layoutId, OnItemBindListener<T> onItemBindListener) {
        this.itemList = itemList;
        this.layoutId = layoutId;
        this.onItemBindListener = onItemBindListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) parent.getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
        return new GenericViewHolder(li.inflate(layoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (onItemBindListener != null) {
            onItemBindListener.onBind(holder.itemView, itemList.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface OnItemBindListener<I> {
        void onBind(View itemView, I item, int position);
    }

    class GenericViewHolder extends RecyclerView.ViewHolder {
        GenericViewHolder(View itemView) {
            super(itemView);
        }
    }
}