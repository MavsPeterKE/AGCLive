package com.room.arcadelive.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.room.arcadelive.R;
import com.room.arcadelive.databinding.FragmentCustomersBinding;
import com.room.arcadelive.utils.ViewModelFactory;
import com.room.arcadelive.viewmodels.CustomersViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class FragmentCustomers extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;
    private CustomersViewModel customersViewModel;
    private FragmentCustomersBinding fragmentCustomersBinding;

    public static FragmentCustomers newInstance() {
        return new FragmentCustomers();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        customersViewModel = new ViewModelProvider(this, viewModelFactory).get(CustomersViewModel.class);
        fragmentCustomersBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_customers, container, false);
        fragmentCustomersBinding.setModel(customersViewModel);
        fragmentCustomersBinding.executePendingBindings();
        return fragmentCustomersBinding.getRoot();
    }

}