package com.digital.payandserve.customer_care;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.customer_care.model.SelectSubjectModel;
import com.digital.payandserve.databinding.StateListItemBinding;
import com.digital.payandserve.views.select_state_district.district_model.DistrictModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SelectSubjectAdapter extends RecyclerView.Adapter<SelectSubjectAdapter.SearchList_VH> {

    private Context mContext;
    private List<SelectSubjectModel> dataList;
    private List<DistrictModel> districtDataList;
    OnSearchListClickListner listner;
    private String type;

    public SelectSubjectAdapter(Context mContext, List<SelectSubjectModel> dataList, OnSearchListClickListner listner) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.listner = listner;

    }

    public SelectSubjectAdapter(List<DistrictModel> districtDataList, OnSearchListClickListner listner) {
        this.districtDataList = districtDataList;
        this.listner = listner;

    }

    public void UpdateList(List<SelectSubjectModel> temp) {
        this.dataList = temp;
        notifyDataSetChanged();
    }

    public void UpdateDistrictList(List<DistrictModel> temp) {
        this.districtDataList = temp;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public SearchList_VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        StateListItemBinding binding = StateListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SearchList_VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SelectSubjectAdapter.SearchList_VH holder, int position) {


            SelectSubjectModel data = dataList.get(position);
            holder.name.setText(data.getSubject());
            holder.listLayout.setOnClickListener(v -> {
                listner.onSearchItemClicked(position,data.getSubject());
            });
    }

    @Override
    public int getItemCount() {

            return dataList.size();

    }

    class SearchList_VH extends RecyclerView.ViewHolder {

        TextView name;
        ConstraintLayout listLayout;

        public SearchList_VH(StateListItemBinding binding) {
            super(binding.getRoot());
            name = binding.name;
            listLayout = binding.listLayout;

        }
    }

    interface OnSearchListClickListner {
        void onSearchItemClicked(int postion, String subject);

    }
}
