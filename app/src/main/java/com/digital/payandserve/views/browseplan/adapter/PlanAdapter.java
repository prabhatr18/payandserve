package com.digital.payandserve.views.browseplan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.views.browseplan.model.DataItem;
import com.digital.payandserve.views.browseplan.listen.PlanSelectorLis;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.MyViewHolder> {
    private List<DataItem> dataList;
    private Context context;
    private PlanSelectorLis listener;
    private  String type;

    public PlanAdapter(List<DataItem> dataList, Context context, PlanSelectorLis listener, String type) {
        this.dataList = dataList;
        this.context = context;
        this.listener = listener;
        this.type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_plan_row, parent, false);
        MyViewHolder gvh = new MyViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        DataItem model = dataList.get(position);
        holder.tvPlanPrice.setText(context.getString(R.string.rupee) + dataList.get(position).getAmount());

        String detail;
        if (type.equalsIgnoreCase("mobile")) {
            detail = model.getDetail() + " \n\nValidity - " + model.getValidity();
        } else {
            detail = model.getDetail() + " \n\nPlan - " + model.getValidity();
        }
        holder.tvDetail.setText(detail);

        holder.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonSelect(position, model);
            }
        });

        holder.imgExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onImgExpand(position, model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView holderCon;
        private TextView tvPlanPrice;
        private TextView btnSelect;
        private View line;
        private TextView tvDetail;
        private LinearLayout imgExpand;

        public MyViewHolder(View view) {
            super(view);
            holderCon = (CardView) view.findViewById(R.id.holderCon);
            tvPlanPrice = (TextView) view.findViewById(R.id.tvPlanPrice);
            btnSelect = view.findViewById(R.id.btnSelect);
            line = (View) view.findViewById(R.id.line);
            tvDetail = (TextView) view.findViewById(R.id.tvDetail);
            imgExpand =  view.findViewById(R.id.imgExpand);
        }

    }


}