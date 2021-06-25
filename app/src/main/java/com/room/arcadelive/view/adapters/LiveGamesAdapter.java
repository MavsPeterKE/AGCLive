package com.room.arcadelive.view.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.room.arcadelive.BR;
import com.room.arcadelive.models.ScreenFirebaseModel;
import com.room.arcadelive.viewmodels.LiveGamesViewModel;

import java.util.List;

public class LiveGamesAdapter extends RecyclerView.Adapter<LiveGamesAdapter.GenericViewHolder> {

    private int layoutId;
    private List<ScreenFirebaseModel> screenItemList;
    private LiveGamesViewModel viewModel;

    public LiveGamesAdapter(@LayoutRes int layoutId, LiveGamesViewModel viewModel) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
    }

    private int getLayoutIdForPosition(int position) {
        return layoutId;
    }

    @Override
    public int getItemCount() {
        return screenItemList == null ? 0 : screenItemList.size();
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

    public void screenList(List<ScreenFirebaseModel> screenItemLists) {
        this.screenItemList = screenItemLists;
        notifyDataSetChanged();
    }

    class GenericViewHolder extends RecyclerView.ViewHolder {
        final ViewDataBinding binding;

        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(LiveGamesViewModel viewModel, Integer position) {
            binding.setVariable(BR.viewmodel, viewModel);
            binding.setVariable(BR.screen, screenItemList.get(position));
            binding.executePendingBindings();
            //binding.getRoot().findViewById(R.id.layout_main).setOnClickListener(view -> viewModel.onScreenItemClick(screenItemList.get(position)));
        }

    }
}

