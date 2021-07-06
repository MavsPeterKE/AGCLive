package com.room.arcadelive.utils;

import android.text.format.DateFormat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.room.arcadelive.models.Expense;

import java.util.Date;

import javax.inject.Inject;

public class FirebaseLogs {
    private DatabaseReference firebaseDatabaseReference;

    @Inject
    public FirebaseLogs() {
        firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

    }


    public void setExpense(String date, String tableName, Expense expense) {
        Date dataTest = Utils.convertToDate(date,Constants.DATE_FORMAT);
        String monthString  = (String) DateFormat.format("MMM",  dataTest); // Jun
        String year         = (String) DateFormat.format("yyyy", dataTest); // 2013
        String monthNumber  = (String) DateFormat.format("MM",   dataTest); // 06
        String dayOfTheWeek = (String) DateFormat.format("EEEE", dataTest); // Thursday
        String day          = (String) DateFormat.format("dd",   dataTest); // 20
        firebaseDatabaseReference
                .child(Constants.DEFAULT_USER)
                .child("gamelogs")
                .child(tableName)
                .child(monthString+"_"+year)
                .child("Ex"+Utils.getRandomId())
                .setValue(expense);
    }

}
