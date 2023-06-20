package com.payment.aeps.fingpay_microatm.printer.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fingpay.microatmsdk.data.MiniStatementModel;
import com.payment.aeps.R;

import java.util.List;

public class InvoiceListStatementAdapter extends BaseAdapter {

    private List<MiniStatementModel> dataMap;
    private LayoutInflater layoutInflater = null;
    private Context context;

    public InvoiceListStatementAdapter(Context context, List<MiniStatementModel> dataMap) {
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
        TextView title;
        ImageView icon;

        public Holder(View rowView) {
            //value = (TextView) rowView.findViewById(R.id.tvValue);
            title = (TextView) rowView.findViewById(R.id.title);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SpannableStringBuilder builder;
        final Holder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.fing_invoice_list_row_statemnt, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        MiniStatementModel model = dataMap.get(position);
       // String data ="null";


        String data = "Date='" + model.getDate() + '\'' + ",  TxnType='" + model.getTxnType() + '\'' + ",  Amount='" +
                model.getAmount() + '\'' + ", Narration='" + model.getNarration() + '\'';


        holder.title.setText(data);
        return convertView;
    }

}
