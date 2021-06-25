package com.room.arcadelive.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.room.arcadelive.R;
import com.room.arcadelive.models.CompletedGame;
import com.room.arcadelive.utils.Constants;
import com.room.arcadelive.utils.Utils;
import com.room.arcadelive.view.adapters.TabsFragmentAdapter;
import com.room.arcadelive.view.fragments.FragmentLiveGames;
import com.room.arcadelive.view.fragments.FragmentCompletedGames;
import com.room.arcadelive.view.fragments.FragmentEndDays;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {
    @BindView(R.id.tab)
    TabLayout tabLayout;

    @BindView(R.id.indicator)
    View mIndicator;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    @BindView(R.id.tvAmount)
    TextView tvTotals;

    private int indicatorWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        observeCompletedGames();
        TabsFragmentAdapter tabsFragmentAdapter = new TabsFragmentAdapter(getSupportFragmentManager());
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

    }

    private void observeCompletedGames() {
        Date todayDate = Utils.convertToDate(Utils.getTodayDate(Constants.DATE_FORMAT),Constants.DATE_FORMAT);
        String monthString  = (String) DateFormat.format("MMM",  todayDate); // Jun
        String year         = (String) DateFormat.format("yyyy", todayDate); // 2013
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("juja_cross_roads")
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

                tvTotals.setText("KES "+totalRevenue+"0");

            }

            @Override
            public void onCancelled(DatabaseError error) {
                int x = 0;
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

}

