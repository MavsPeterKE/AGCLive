package com.room.arcadelive.view.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.room.arcadelive.R;
import com.room.arcadelive.databinding.FragmentHomeBinding;
import com.room.arcadelive.models.CompletedGame;
import com.room.arcadelive.utils.Constants;
import com.room.arcadelive.utils.Utils;
import com.room.arcadelive.utils.ViewModelFactory;
import com.room.arcadelive.view.adapters.TabsFragmentAdapter;
import com.room.arcadelive.viewmodels.HomeViewModel;

import java.util.Date;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

import static com.room.arcadelive.utils.Constants.DEFAULT_USER;


public class FragmentHome extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;

    public static FragmentHome newInstance() {
        return new FragmentHome();
    }

    FragmentHomeBinding fragmentEndDaysBinding;
    HomeViewModel homeViewModel;

    private TabLayout tabLayout;
    private View mIndicator;
    private ViewPager mViewPager;
    private TextView tvTotals;

    private int indicatorWidth;
    FirebaseDatabase firebaseDatabase;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this, viewModelFactory).get(HomeViewModel.class);
        fragmentEndDaysBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        fragmentEndDaysBinding.executePendingBindings();

        tabLayout = fragmentEndDaysBinding.tab;
        mIndicator = fragmentEndDaysBinding.indicator;
        mViewPager = fragmentEndDaysBinding.viewPager;
        tvTotals = fragmentEndDaysBinding.tvAmount;

        firebaseDatabase = FirebaseDatabase.getInstance();
        observeCompletedGames();
        TabsFragmentAdapter tabsFragmentAdapter = new TabsFragmentAdapter(getActivity().getSupportFragmentManager());
        tabsFragmentAdapter.addFragment(FragmentLiveGames.newInstance(), "Screens");
        tabsFragmentAdapter.addFragment(FragmentCompletedGames.newInstance(), "Games");
        tabsFragmentAdapter.addFragment(FragmentEndDays.newInstance(), "End Days");

        mViewPager.setAdapter(tabsFragmentAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"));

        //Determine indicator width at runtime
        tabLayout.post(() -> {
            indicatorWidth = tabLayout.getWidth() / tabLayout.getTabCount();

            //Assign new width
            FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();
            indicatorParams.width = indicatorWidth;
            mIndicator.setLayoutParams(indicatorParams);
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //To move the indicator as the user scroll, we will need the scroll offset values
            //positionOffset is a value from [0..1] which represents how far the page has been scrolled
            //see https://developer.android.com/reference/android/support/v4/view/ViewPager.OnPageChangeListener
            @Override
            public void onPageScrolled(int i, float positionOffset, int positionOffsetPx) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();

                //Multiply positionOffset with indicatorWidth to get translation
                float translationOffset = (positionOffset + i) * indicatorWidth;
                params.leftMargin = (int) translationOffset;
                mIndicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        return fragmentEndDaysBinding.getRoot();
    }

    private void observeCompletedGames() {
        Date todayDate = Utils.convertToDate(Utils.getTodayDate(Constants.DATE_FORMAT),Constants.DATE_FORMAT);
        String monthString  = (String) DateFormat.format("MMM",  todayDate); // Jun
        String year         = (String) DateFormat.format("yyyy", todayDate); // 2013
        DatabaseReference myRef = firebaseDatabase.getReference(DEFAULT_USER)
                .child("gamelogs")
                .child("all-completed-Games")
                .child(monthString+"_"+year)
                .child(Utils.getTodayDate(Constants.DATE_FORMAT));

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalRevenue = 0;
                for(DataSnapshot gameModel : dataSnapshot.getChildren()) {
                    CompletedGame completedGame = gameModel.getValue(CompletedGame.class);
                    totalRevenue+=completedGame.payableAmount;
                }

                setTotalRevenue(totalRevenue);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                int x = 0;
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void setTotalRevenue(double totalRevenue) {
        tvTotals.setText("KES "+ totalRevenue +"0");
    }
}