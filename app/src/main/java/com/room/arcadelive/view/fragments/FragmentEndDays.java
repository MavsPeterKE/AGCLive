package com.room.arcadelive.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.room.arcadelive.R;
import com.room.arcadelive.databinding.FragmentEndDaysBinding;
import com.room.arcadelive.utils.ViewModelFactory;
import com.room.arcadelive.viewmodels.CompletedGamesViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class FragmentEndDays extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;

    public static FragmentEndDays newInstance() {
        return new FragmentEndDays();
    }

    FragmentEndDaysBinding fragmentEndDaysBinding;
    CompletedGamesViewModel viewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //viewModel = new ViewModelProvider(this, viewModelFactory).get(FragmentTwoViewModel.class);
        fragmentEndDaysBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_end_days, container, false);
        return fragmentEndDaysBinding.getRoot();
    }
}