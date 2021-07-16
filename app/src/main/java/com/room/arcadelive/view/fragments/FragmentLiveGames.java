package com.room.arcadelive.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.room.arcadelive.R;
import com.room.arcadelive.databinding.FragmentLiveGamesBinding;
import com.room.arcadelive.models.ScreenFirebaseModel;
import com.room.arcadelive.utils.Constants;
import com.room.arcadelive.utils.ViewModelFactory;
import com.room.arcadelive.view.adapters.LiveGamesAdapter;
import com.room.arcadelive.viewmodels.LiveGamesViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        fragmentLiveGamesBinding.setViewmodel(viewModel);
        fragmentLiveGamesBinding.executePendingBindings();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.DEFAULT_USER).child("gamelogs").child("all-active-games");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ScreenFirebaseModel> screenFirebaseModels = new ArrayList<>();
                for(DataSnapshot screenView : dataSnapshot.getChildren()) {
                   screenFirebaseModels.add(screenView.getValue(ScreenFirebaseModel.class));
                }
                Collections.sort(screenFirebaseModels, (screenFirebaseModel, t1) ->
                        Integer.compare(t1.screen.active?1:0,screenFirebaseModel.screen.active?1:0));
                viewModel.setScreenList(screenFirebaseModels);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                int x = 0;
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        return fragmentLiveGamesBinding.getRoot();
    }
}