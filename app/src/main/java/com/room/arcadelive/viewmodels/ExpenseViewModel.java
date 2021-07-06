package com.room.arcadelive.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.room.arcadelive.R;
import com.room.arcadelive.models.EndDayModel;
import com.room.arcadelive.models.Expense;
import com.room.arcadelive.utils.Constants;
import com.room.arcadelive.utils.Utils;
import com.room.arcadelive.view.adapters.ExpenseAdapter;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class ExpenseViewModel extends ViewModel {
    private MutableLiveData<String> gameItemClickLiveData = new MutableLiveData();
    ExpenseAdapter expenseAdapter;

    @Inject
    public ExpenseViewModel() {
        expenseAdapter = new ExpenseAdapter(R.layout.expense_item,this);

    }

    public String getExpenseDate(Date date){return Utils.getDateString(Constants.DATE_FORMAT,date);
    }

    public ExpenseAdapter getExpenseAdapter() {
        return expenseAdapter;
    }

    public void setExpenseList(List<Expense> expenseList){
        expenseAdapter.setExpenseList(expenseList);
    }
}
