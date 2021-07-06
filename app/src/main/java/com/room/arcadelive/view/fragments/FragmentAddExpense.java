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
import com.room.arcadelive.databinding.FragmentAddExpenseBinding;
import com.room.arcadelive.models.Expense;
import com.room.arcadelive.utils.Constants;
import com.room.arcadelive.utils.FirebaseLogs;
import com.room.arcadelive.utils.Utils;
import com.room.arcadelive.utils.ViewModelFactory;
import com.room.arcadelive.viewmodels.ExpenseViewModel;

import java.util.Date;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class FragmentAddExpense extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;

    public static FragmentAddExpense newInstance() {
        return new FragmentAddExpense();
    }

    FragmentAddExpenseBinding fragmentAddExpenseBinding;
    ExpenseViewModel expenseViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        expenseViewModel = new ViewModelProvider(this, viewModelFactory).get(ExpenseViewModel.class);
        fragmentAddExpenseBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_expense, container, false);
        fragmentAddExpenseBinding.executePendingBindings();

      /*  fragmentAddExpenseBinding.button.setOnClickListener(view -> {
          //  ((HomeActivity)getActivity()).changeFragment();
        });*/

        fragmentAddExpenseBinding.btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = fragmentAddExpenseBinding.edExpenseAmount.getText().toString().trim();
                String desc = fragmentAddExpenseBinding.editExpDesc.getText().toString().trim();

                if (amount.isEmpty()){
                    fragmentAddExpenseBinding.edExpenseAmount.setError("Required");
                }else  if (desc.isEmpty()){
                    fragmentAddExpenseBinding.editExpDesc.setError("Required");
                }else {
                    Expense expense = new Expense();
                    expense.amount = Double.parseDouble(amount);
                    expense.description = desc;
                    expense.user = "Peter";
                    expense.date = new Date();

                    new FirebaseLogs().setExpense(Utils.getTodayDate(Constants.DATE_FORMAT),"expenses",expense);
                }
            }
        });

        return fragmentAddExpenseBinding.getRoot();
    }
}