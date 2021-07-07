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
import com.room.arcadelive.databinding.FragmentRevenueBinding;
import com.room.arcadelive.models.EndDayModel;
import com.room.arcadelive.models.Expense;
import com.room.arcadelive.utils.Constants;
import com.room.arcadelive.utils.Utils;
import com.room.arcadelive.utils.ViewModelFactory;
import com.room.arcadelive.viewmodels.RevenueViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class FragmentRevenue extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;

    public static FragmentRevenue newInstance() {
        return new FragmentRevenue();
    }

    FragmentRevenueBinding fragmentRevenueBinding;
    RevenueViewModel viewModel;
    private double totalSales = 0.0;
    private double totalExpenses;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(RevenueViewModel.class);
        fragmentRevenueBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_revenue, container, false);
        fragmentRevenueBinding.setViewmodel(viewModel);
        fragmentRevenueBinding.executePendingBindings();
        observeRevenueData();
        return fragmentRevenueBinding.getRoot();
    }

    private void observeRevenueData() {
        Date todayDate = Utils.convertToDate(Utils.getTodayDate(Constants.DATE_FORMAT), Constants.DATE_FORMAT);
        String monthString = (String) DateFormat.format("MMM", todayDate); // Jun
        String year = (String) DateFormat.format("yyyy", todayDate); // 2013
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String salesPeriod = monthString + "_" + year;
        DatabaseReference myRef = database.getReference(Constants.DEFAULT_USER)
                .child("gamelogs");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DataSnapshot endDaysSnapshot = dataSnapshot.child("-all-end-days").child(salesPeriod);
                DataSnapshot expenseDataSnapshot = dataSnapshot.child("expenses").child(salesPeriod);
                List<EndDayModel> endDayModelList = new ArrayList<>();
                for (DataSnapshot snapshot : endDaysSnapshot.getChildren()) {
                    endDayModelList.add(snapshot.getValue(EndDayModel.class));
                    calculateTotalSales(endDayModelList);
                }

                List<Expense> expenseList = new ArrayList<>();
                for (DataSnapshot snapshot : expenseDataSnapshot.getChildren()) {
                    expenseList.add( snapshot.getValue(Expense.class));
                    calculateTotalExpenses(expenseList);
                }

                viewModel.totalExpensesField.set(totalExpenses);
                viewModel.totalEndDayField.set(totalSales);
                viewModel.revenueField.set(totalSales-totalExpenses);
                Collections.sort(endDayModelList, (endDayModel, t1) -> Double.compare(t1.date, endDayModel.date));
                viewModel.setEndDayList(endDayModelList);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                int x = 0;
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void calculateTotalSales(List<EndDayModel> endDayModelList) {
        double amount = 0.00;
        for (EndDayModel endDayModel :endDayModelList){
            amount+=Double.parseDouble(endDayModel.totalSales);
        }
        totalSales = amount;
    }

    private void calculateTotalExpenses(List<Expense> expenseList) {
        double total = 0;
        for (Expense expense:expenseList){
            total+=expense.amount;
        }
        totalExpenses = total;
    }
}