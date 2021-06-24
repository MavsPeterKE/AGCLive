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
import com.room.arcadelive.databinding.FragmentLiveGamesBinding;
import com.room.arcadelive.utils.ViewModelFactory;
import com.room.arcadelive.viewmodels.LiveGamesViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class FragmentLiveGames extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;

    public static FragmentLiveGames newInstance() {
        return new FragmentLiveGames();
    }

    FragmentLiveGamesBinding fragmentLiveGamesBinding;
    LiveGamesViewModel viewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentLiveGamesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_live_games, container, false);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(LiveGamesViewModel.class);
        return fragmentLiveGamesBinding.getRoot();
    }
}