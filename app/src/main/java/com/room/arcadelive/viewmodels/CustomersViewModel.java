package com.room.arcadelive.viewmodels;

import static com.room.arcadelive.utils.Constants.DATE_FORMAT;

import android.text.format.DateFormat;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.room.arcadelive.database.entity.Customer;
import com.room.arcadelive.database.entity.CustomerVisit;
import com.room.arcadelive.database.views.CustomerView;
import com.room.arcadelive.models.CustomerMetric;
import com.room.arcadelive.utils.Constants;
import com.room.arcadelive.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

public class CustomersViewModel extends ViewModel {
    private ObservableField<CustomerMetric> customerMetricObservableField = new ObservableField<>();

    @Inject
    public CustomersViewModel() {
        setCustomersData();
    }

    public ObservableField<CustomerMetric> getCustomerMetricObservableField() {
        return customerMetricObservableField;
    }

    public void setCustomersData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.DEFAULT_USER).child("gamelogs").child("all-game-customers");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<CustomerView> customerViewList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    customerViewList.add(snapshot.getValue(CustomerView.class));
                }
                setCustomerMetric(customerViewList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void setCustomerMetric(List<CustomerView> customerViewList) {
        Date todayDate = Utils.convertToDate(Utils.getTodayDate(DATE_FORMAT), DATE_FORMAT);
        String monthString = (String) DateFormat.format("MMM", todayDate); // Jun
        String year = (String) DateFormat.format("yyyy", todayDate); // 2013
        int currentWeek = Utils.getCurrentWeekCount(Utils.getTodayDate(DATE_FORMAT));
        if (customerViewList!=null){
            int loyalCustomers = 0;
            int newCustomers = 0;
            double totalAmountZeroCustomersThisMonth= 0;
            Set<String> customerThisMonth = new HashSet<>();
            Set<String> customersThisWeek = new HashSet<>();
            Set<String> zeroCustomersThisMonth = new HashSet<>();
            for (CustomerView customerView: customerViewList){
                String gamerType = customerView.gamer.getCustomerType()!=null?customerView.gamer.getCustomerType():"";
                if (gamerType.equals("LOYAL_CUSTOMER")){
                    loyalCustomers+=1;
                }else {
                    newCustomers+=1;
                }

                if (customerView.customerVisitList!=null){
                    for (CustomerVisit customerVisit : customerView.customerVisitList){
                        if (customerVisit.getMonth().equals(monthString+"_"+year)){
                            customerThisMonth.add(customerVisit.getCustomer_phone());
                        }else {
                            zeroCustomersThisMonth.add(customerVisit.getCustomer_phone());
                            totalAmountZeroCustomersThisMonth+=customerVisit.getAmountPaidToShop();
                        }

                        if (customerVisit.getWeek()==currentWeek){
                            customersThisWeek.add(customerVisit.getCustomer_phone());
                        }
                    }
                }

            }

            CustomerMetric customerMetric = new CustomerMetric();
            customerMetric.allGamersCount = customerViewList.size()+" Gamers";
            customerMetric.gamersThisMonthCount = customerThisMonth.size()+" Gamers";
            customerMetric.loyalGamesCount = loyalCustomers+ "";
            customerMetric.newGamersCount = newCustomers+ "";
            customerMetric.zeroMonthVisitGamersCount = zeroCustomersThisMonth.size()+ " Gamers";
            customerMetric.WeekGamesCount = customersThisWeek.size()+ "";
            customerMetric.zeroMonthVisitAverageRevenue = "Ksh. "+totalAmountZeroCustomersThisMonth+"0";
            customerMetricObservableField.set(customerMetric);
        }
    }

}
