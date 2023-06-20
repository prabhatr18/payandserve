package com.payment.aeps.moduleprinter.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.payment.aeps.R;
import com.payment.aeps.objectmodel.InvoiceModel;
import com.payment.aeps.objectmodel.StatementModel;

import java.util.List;

public class StatementListAdapter extends BaseAdapter {

    private List<StatementModel> dataMap;
    private LayoutInflater layoutInflater = null;
    private Context context;

    public StatementListAdapter(Context context, List<StatementModel> dataMap) {
        this.context = context;
        this.dataMap = dataMap;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataMap.size();
    }

    @Override
    public Object getItem(int i) {
        return dataMap.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 1;
    }

    public class Holder {
        TextView value, title, type;
        ImageView icon;

        public Holder(View rowView) {
            value = (TextView) rowView.findViewById(R.id.tvValue);
            title = (TextView) rowView.findViewById(R.id.title);
            type = (TextView) rowView.findViewById(R.id.type);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SpannableStringBuilder builder;
        final Holder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.m_statement_row_layout, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        StatementModel model = dataMap.get(position);
        holder.title.setText(model.getDate());
        holder.value.setText(model.getAmount());
        holder.type.setText(model.getTxnType());

        return convertView;
    }

}
