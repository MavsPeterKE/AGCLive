package com.room.arcadelive.view.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.room.arcadelive.BR;
import com.room.arcadelive.models.EndDayModel;
import com.room.arcadelive.viewmodels.EndDayViewModel;
import com.room.arcadelive.viewmodels.RevenueViewModel;

import java.util.List;

public class RevenueAdapter extends RecyclerView.Adapter<RevenueAdapter.GenericViewHolder> {

    private int layoutId;
    private List<EndDayModel> endDayModelList;
    private RevenueViewModel viewModel;

    public RevenueAdapter(@LayoutRes int layoutId, RevenueViewModel viewModel) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
    }

    private int getLayoutIdForPosition(int position) {
        return layoutId;
    }

    @Override
    public int getItemCount() {
        return endDayModelList == null ? 0 : endDayModelList.size();
    }

    @NonNull
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);

        return new GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        holder.bind(viewModel, position);
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    public void setEndDayList(List<EndDayModel> endDayModelList) {
        this.endDayModelList = endDayModelList;
        notifyDataSetChanged();
    }

    class GenericViewHolder extends RecyclerView.ViewHolder {
        final ViewDataBinding binding;

        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(RevenueViewModel viewModel, Integer position) {
            binding.setVariable(BR.viewmodel, viewModel);
            binding.setVariable(BR.endDay, endDayModelList.get(position));
            binding.executePendingBindings();
            //binding.getRoot().findViewById(R.id.layout_main).setOnClickListener(view -> viewModel.onScreenItemClick(screenItemList.get(position)));
        }

    }
}

