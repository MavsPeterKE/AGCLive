package com.room.arcadelive.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.firebase.database.ChildEventListener;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

import static com.room.arcadelive.utils.Utils.getCurrentMonthYear;


public class FragmentRevenue extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;

    public static FragmentRevenue newInstance() {
        return new FragmentRevenue();
    }

    FragmentRevenueBinding fragmentRevenueBinding;
    RevenueViewModel viewModel;
    private double totalSales = 0.0;
    private double totalExpenses = 0.0;
    private MonthYearPickerDialogFragment dialogFragment;
    private String searchPeriod = "";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(RevenueViewModel.class);
        fragmentRevenueBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_revenue, container, false);
        fragmentRevenueBinding.setViewmodel(viewModel);
        fragmentRevenueBinding.executePendingBindings();

        fragmentRevenueBinding.calender.setOnClickListener(view -> {
            dialogFragment = MonthYearPickerDialogFragment
                    .getInstance(1, 2021, "Sales Period");
            dialogFragment.show(getActivity().getSupportFragmentManager(), null);

            dialogFragment.setOnDateSetListener((year, monthOfYear) -> {
                searchPeriod = Utils.getMonthAbbr(monthOfYear) + "_" + year;
                observeRevenueEndDayByMonth(Utils.getMonthAbbr(monthOfYear),year);
            });
        });


        observeRevenueData();

        return fragmentRevenueBinding.getRoot();
    }

    private void observeRevenueData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String salesPeriod = getCurrentMonthYear();
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
                }
                calculateTotalSales(endDayModelList);

                List<Expense> expenseList = new ArrayList<>();
                for (DataSnapshot snapshot : expenseDataSnapshot.getChildren()) {
                    expenseList.add(snapshot.getValue(Expense.class));
                }
                calculateTotalExpenses(expenseList);
                setRevenueValues();
                Collections.sort(endDayModelList, (endDayModel, t1) -> Double.compare(t1.date, endDayModel.date));
                viewModel.setEndDayList(endDayModelList);

            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void observeRevenueEndDayByMonth(String month, int year) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference(Constants.DEFAULT_USER)
                .child("gamelogs")
                .child("-all-end-days")
                .orderByKey().equalTo(searchPeriod).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                List<EndDayModel> endDayModelList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    endDayModelList.add(dataSnapshot.getValue(EndDayModel.class));
                }
                calculateTotalSales(endDayModelList);
                fragmentRevenueBinding.tvRevenueTitle.setText(
                        searchPeriod.equals(getCurrentMonthYear())?"Total Revenue":
                                "Total Revenue for "+month+" "+year);

                Collections.sort(endDayModelList, (endDayModel, t1) -> Double.compare(t1.date, endDayModel.date));
                viewModel.setEndDayList(endDayModelList);
                observeRevenueExpenseByMonth();
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void observeRevenueExpenseByMonth() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference(Constants.DEFAULT_USER)
                .child("gamelogs")
                .child("expenses")
                .orderByKey().equalTo(searchPeriod).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot expenseDataSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                List<Expense> expenseList = new ArrayList<>();
                for (DataSnapshot snapshot : expenseDataSnapshot.getChildren()) {
                    expenseList.add(snapshot.getValue(Expense.class));
                }
                calculateTotalExpenses(expenseList);
                setRevenueValues();
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                int x = 0;
            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                int x = 0;
            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                int x = 0;
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
             int x = 0;
            }
        });
    }

    private void setRevenueValues() {
        viewModel.totalExpensesField.set(totalExpenses);
        viewModel.totalEndDayField.set(totalSales);
        viewModel.revenueField.set(totalSales - totalExpenses);
    }

    private void calculateTotalSales(List<EndDayModel> endDayModelList) {
        double amount = 0.00;
        for (EndDayModel endDayModel : endDayModelList) {
            amount += Double.parseDouble(endDayModel.totalSales);
        }
        totalSales = amount;
    }

    private void calculateTotalExpenses(List<Expense> expenseList) {
        double total = 0;
        for (Expense expense : expenseList) {
            total += expense.amount;
        }
        totalExpenses = total;
    }

    private void showMonthSelect() {

    }


}