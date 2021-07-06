package com.room.arcadelive.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class AddExpenseViewModel extends ViewModel {
    private MutableLiveData<String> gameItemClickLiveData = new MutableLiveData();

    @Inject
    public AddExpenseViewModel() {

    }
}
