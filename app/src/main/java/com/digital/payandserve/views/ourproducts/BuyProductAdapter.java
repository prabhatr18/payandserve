package com.digital.payandserve.views.ourproducts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.utill.MyUtil;

import java.util.ArrayList;

public class BuyProductAdapter extends RecyclerView.Adapter<BuyProductAdapter.BuyProduct_VH> {

    ArrayList<BuyProductListModel> list;
    String imageBasePth;
    OnBuyClickListener listener;

    public BuyProductAdapter(ArrayList<BuyProductListModel> list, String imageBasePth, OnBuyClickListener listener) {
        this.list = list;
        this.imageBasePth = imageBasePth;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BuyProduct_VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_product_list_item, parent, false);
        return new BuyProduct_VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyProduct_VH holder, int position) {
        BuyProductListModel data = list.get(position);

        holder.tvPrice.setText("₹ " + data.getSelling_price() + " only");
        holder.tvProduct.setText(data.getName());
        holder.txtQty.setText(data.getBuyingQty() + "");


        holder.imgMinus.setOnClickListener(v -> {
            if (data.getBuyingQty() > 1) {
                int qty = data.getBuyingQty() - 1;
                data.setBuyingQty(qty);
                holder.txtQty.setText(data.getBuyingQty() + "");
                data.setBuyingPrice(data.getBuyingPrice() - Integer.parseInt(data.getSelling_price()));
                holder.tvPrice.setText("₹ " + data.getBuyingPrice() + " only");
            }

        });

        holder.imgAdd.setOnClickListener(v -> {
            if (data.getBuyingQty() < Integer.parseInt(data.getQty())) {
                int qty = data.getBuyingQty() + 1;
                data.setBuyingQty(qty);
                holder.txtQty.setText(data.getBuyingQty() + "");
                data.setBuyingPrice(Integer.parseInt(data.getSelling_price())*qty);
                holder.tvPrice.setText("₹ " + data.getBuyingPrice() + " only");
            }

        });

        MyUtil.setGlideImage(imageBasePth + "/" + data.getImage(), holder.categoryImgShow, holder.txtQty.getContext());

        holder.buyBtn.setOnClickListener(v -> {
            listener.onBuyBtnClicked(position,data);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class BuyProduct_VH extends RecyclerView.ViewHolder {

        ImageView categoryImgShow, imgMinus, imgAdd;
        TextView tvProduct, tvPrice, txtQty;
        Button buyBtn;

        public BuyProduct_VH(@NonNull View itemView) {
            super(itemView);

            categoryImgShow = itemView.findViewById(R.id.categoryImgShow);
            imgMinus = itemView.findViewById(R.id.imgMinus);
            imgAdd = itemView.findViewById(R.id.imgAdd);
            buyBtn = itemView.findViewById(R.id.buyBtn);
            tvProduct = itemView.findViewById(R.id.tvProduct);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            txtQty = itemView.findViewById(R.id.txtQty);

        }
    }

    public interface OnBuyClickListener {
        void onBuyBtnClicked(int position, BuyProductListModel data);
    }
}
