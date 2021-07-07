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
import com.room.arcadelive.databinding.FragmentExpenseBinding;
import com.room.arcadelive.models.Expense;
import com.room.arcadelive.utils.Constants;
import com.room.arcadelive.utils.Utils;
import com.room.arcadelive.utils.ViewModelFactory;
import com.room.arcadelive.view.activity.HomeActivity;
import com.room.arcadelive.viewmodels.ExpenseViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class FragmentExpense extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;

    public static FragmentExpense newInstance() {
        return new FragmentExpense();
    }

    FragmentExpenseBinding fragmentExpenseBinding;
    ExpenseViewModel expenseViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        expenseViewModel = new ViewModelProvider(this, viewModelFactory).get(ExpenseViewModel.class);
        fragmentExpenseBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_expense, container, false);
        fragmentExpenseBinding.setViewmodel(expenseViewModel);
        fragmentExpenseBinding.executePendingBindings();

        fragmentExpenseBinding.btAdd.setOnClickListener(view -> {
            ((HomeActivity) getActivity()).createFragments(new FragmentAddExpense());
        });
        observeExpenses();
        return fragmentExpenseBinding.getRoot();
    }

    private void observeExpenses() {
        Date todayDate = Utils.convertToDate(Utils.getTodayDate(Constants.DATE_FORMAT), Constants.DATE_FORMAT);
        String monthString = (String) DateFormat.format("MMM", todayDate); // Jun
        String year = (String) DateFormat.format("yyyy", todayDate); // 2013
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.DEFAULT_USER)
                .child("gamelogs")
                .child("expenses")
                .child(monthString + "_" + year);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Expense> expenseList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Expense expense = snapshot.getValue(Expense.class);
                    expenseList.add(expense);
                }
                expenseViewModel.setExpenseList(expenseList);
                setTotalExpenseAmount(expenseList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                int x = 0;
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void setTotalExpenseAmount(List<Expense> expenseList) {
        double amount = 0.00;
        if (expenseList.isEmpty()) {
            fragmentExpenseBinding.tvExpenseAmount.setText("KES 0.00");
        } else {
            for (Expense expense : expenseList) {
                amount += expense.amount;
            }

            fragmentExpenseBinding.tvExpenseAmount.setText("KES - " + amount + "0");
        }
    }
}