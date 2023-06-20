package com.payment.aeps.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.payment.aeps.R;
import com.payment.aeps.objectmodel.MStateModel;

import java.util.List;

public class StateListAdapter extends BaseAdapter {

    private List<MStateModel> dataList;
    private LayoutInflater layoutInflater = null;
    private Context context;

    public StateListAdapter(Context context, List<MStateModel> dataList) {
        this.context = context;
        this.dataList = dataList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 1;
    }

    public class Holder {
        TextView tvMenuText;
        ImageView icon;

        public Holder(View rowView) {
            tvMenuText = (TextView) rowView.findViewById(R.id.tvOperator);
            icon = rowView.findViewById(R.id.logo);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SpannableStringBuilder builder;
        final Holder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.module_bank_list_row, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final MStateModel model = dataList.get(position);
        holder.tvMenuText.setText(model.getStatename());
        holder.icon.setVisibility(View.GONE);

        convertView.setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("id", model.getStateid());
            returnIntent.putExtra("name", model.getStatename());
            ((Activity) context).setResult(100, returnIntent);
            ((Activity) context).finish();
        });
        return convertView;
    }

    public void UpdateList(List<MStateModel> item) {
        dataList = item;
        notifyDataSetChanged();
    }
}
