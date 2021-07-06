package com.room.arcadelive.view.fragments;

import android.os.Bundle;
import android.text.format.DateFormat;
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
import com.room.arcadelive.databinding.FragmentCompletedGamesBinding;
import com.room.arcadelive.models.CompletedGame;
import com.room.arcadelive.models.ScreenFirebaseModel;
import com.room.arcadelive.utils.Constants;
import com.room.arcadelive.utils.Utils;
import com.room.arcadelive.utils.ViewModelFactory;
import com.room.arcadelive.viewmodels.CompletedGamesViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class FragmentCompletedGames extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;

    public static FragmentCompletedGames newInstance() {
        return new FragmentCompletedGames();
    }

    FragmentCompletedGamesBinding fragmentCompletedGamesBinding;
    CompletedGamesViewModel viewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(CompletedGamesViewModel.class);
        fragmentCompletedGamesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_completed_games, container, false);
        fragmentCompletedGamesBinding.setGameviewmodel(viewModel);
        fragmentCompletedGamesBinding.executePendingBindings();

        observeCompletedGames();
        return fragmentCompletedGamesBinding.getRoot();
    }

    private void observeCompletedGames() {
        Date todayDate = Utils.convertToDate(Utils.getTodayDate(Constants.DATE_FORMAT),Constants.DATE_FORMAT);
        String monthString  = (String) DateFormat.format("MMM",  todayDate); // Jun
        String year         = (String) DateFormat.format("yyyy", todayDate); // 2013
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.DEFAULT_USER)
                .child("gamelogs")
                .child("all-completed-Games")
                .child(monthString+"_"+year)
                .child(Utils.getTodayDate(Constants.DATE_FORMAT));

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<CompletedGame> completedGameList = new ArrayList<>();
                for(DataSnapshot gameModel : dataSnapshot.getChildren()) {
                    completedGameList.add(gameModel.getValue(CompletedGame.class));
                }
                viewModel.setGameList(completedGameList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                int x = 0;
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}