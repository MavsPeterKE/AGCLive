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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.room.arcadelive.R;
import com.room.arcadelive.databinding.FragmentEndDaysBinding;
import com.room.arcadelive.models.CompletedGame;
import com.room.arcadelive.models.EndDayModel;
import com.room.arcadelive.utils.Constants;
import com.room.arcadelive.utils.Utils;
import com.room.arcadelive.utils.ViewModelFactory;
import com.room.arcadelive.viewmodels.EndDayViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

import static com.room.arcadelive.utils.Constants.DEFAULT_FIREBASE_TABLE;


public class FragmentEndDays extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;

    public static FragmentEndDays newInstance() {
        return new FragmentEndDays();
    }

    FragmentEndDaysBinding fragmentEndDaysBinding;
    EndDayViewModel viewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(EndDayViewModel.class);
        fragmentEndDaysBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_end_days, container, false);
        fragmentEndDaysBinding.setViewmodel(viewModel);
        fragmentEndDaysBinding.executePendingBindings();
        observeCompletedGames();
        //searchEoDByDate("2021-06-26");
        return fragmentEndDaysBinding.getRoot();
    }

    private void observeCompletedGames() {
        Date todayDate = Utils.convertToDate(Utils.getTodayDate(Constants.DATE_FORMAT), Constants.DATE_FORMAT);
        String monthString = (String) DateFormat.format("MMM", todayDate); // Jun
        String year = (String) DateFormat.format("yyyy", todayDate); // 2013
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("juja_cross_roads")
                .child("gamelogs")
                .child("-all-end-days")
                .child(monthString + "_" + year)
                .child(Utils.getTodayDate(Constants.DATE_FORMAT));

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             /*   List<EndDayModel> endDayModelList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    EndDayModel model = snapshot.getValue(EndDayModel.class);
                    endDayModelList.add(model);
                }
                viewModel.setEndDayList(endDayModelList);*/

                List<EndDayModel> endDayModelList = new ArrayList<>();
                EndDayModel dayModel = dataSnapshot.getValue(EndDayModel.class);
                endDayModelList.add(dayModel);
                if (dayModel!=null){
                    viewModel.setEndDayList(endDayModelList);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                int x = 0;
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void searchEoDByDate(String date){
        Date todayDate = Utils.convertToDate(Utils.getTodayDate(Constants.DATE_FORMAT),Constants.DATE_FORMAT);
        String monthString  = (String) DateFormat.format("MMM",  todayDate); // Jun
        String year         = (String) DateFormat.format("yyyy", todayDate); // 2013
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference(DEFAULT_FIREBASE_TABLE)
                .child("gamelogs")
                .child("-all-end-days")
                .child(monthString+"_"+year)
                .orderByKey().equalTo(date).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                List<EndDayModel> endDayModelList = new ArrayList<>();
                endDayModelList.add(dataSnapshot.getValue(EndDayModel.class));
                viewModel.setEndDayList(endDayModelList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                int x = 0;
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int x = 0;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                int x = 0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                int x = 0;
            }
        });

    }
}