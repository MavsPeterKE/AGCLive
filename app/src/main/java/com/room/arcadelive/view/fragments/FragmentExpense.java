package com.room.arcadelive.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.room.arcadelive.databinding.FragmentExpenseBinding;
import com.room.arcadelive.models.Expense;
import com.room.arcadelive.utils.Constants;
import com.room.arcadelive.utils.Utils;
import com.room.arcadelive.utils.ViewModelFactory;
import com.room.arcadelive.view.activity.HomeActivity;
import com.room.arcadelive.viewmodels.ExpenseViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

import static com.room.arcadelive.utils.Utils.getCurrentMonthYear;


public class FragmentExpense extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;

    public static FragmentExpense newInstance() {
        return new FragmentExpense();
    }

    FragmentExpenseBinding fragmentExpenseBinding;
    ExpenseViewModel expenseViewModel;
    private MonthYearPickerDialogFragment dialogFragment;


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

        fragmentExpenseBinding.calender.setOnClickListener(view -> {
            dialogFragment = MonthYearPickerDialogFragment
                    .getInstance(1, 2021, "Sales Period");
            dialogFragment.show(getActivity().getSupportFragmentManager(), null);

            dialogFragment.setOnDateSetListener((year, monthOfYear) -> {
                observeRevenueExpenseByMonth(monthOfYear, year);
            });
        });

        observeExpenses();
        return fragmentExpenseBinding.getRoot();
    }

    private void observeExpenses() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.DEFAULT_USER)
                .child("gamelogs")
                .child("expenses")
                .child(getCurrentMonthYear());

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Expense> expenseList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Expense expense = snapshot.getValue(Expense.class);
                    expenseList.add(expense);
                }
                Collections.sort(expenseList, (expense, t1) -> Long.compare(t1.date.getTime(), expense.date.getTime()));
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

    private void observeRevenueExpenseByMonth(int monthOfYear, int year) {
        String month = Utils.getMonthAbbr(monthOfYear);
        String searchPeriod = month + "_" + year;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference(Constants.DEFAULT_USER)
                .child("gamelogs")
                .child("expenses")
                .orderByKey().equalTo(searchPeriod).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot expenseDataSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                List<Expense> expenseList = new ArrayList<>();
                for (DataSnapshot snapshot : expenseDataSnapshot.getChildren()) {
                    Expense expense = snapshot.getValue(Expense.class);
                    expenseList.add(expense);
                }
                fragmentExpenseBinding.tvTitle.setText("Expenses For " + month + " " + year);
                fragmentExpenseBinding.btAdd.setVisibility(
                        searchPeriod.equals(getCurrentMonthYear()) ? View.VISIBLE : View.GONE);
                Collections.sort(expenseList, (expense, t1) -> Long.compare(t1.date.getTime(), expense.date.getTime()));
                expenseViewModel.setExpenseList(expenseList);
                setTotalExpenseAmount(expenseList);
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
}