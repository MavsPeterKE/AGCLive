package com.room.arcadelive.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

import static com.room.arcadelive.utils.Constants.DEFAULT_FIREBASE_TABLE;

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
    FirebaseDatabase firebaseDatabase;

    public ObservableField<String> observableField = new ObservableField<>("2021-06-24");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        observeCompletedGames();
        TabsFragmentAdapter tabsFragmentAdapter = new TabsFragmentAdapter(getSupportFragmentManager());
        tabsFragmentAdapter.addFragment(FragmentLiveGames.newInstance(), "Screens");
        tabsFragmentAdapter.addFragment(FragmentCompletedGames.newInstance(), "Games");
        tabsFragmentAdapter.addFragment(FragmentEndDays.newInstance(), "End Day");

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

     // search();

    }

    public void search(){
        Date todayDate = Utils.convertToDate(Utils.getTodayDate(Constants.DATE_FORMAT),Constants.DATE_FORMAT);
        String monthString  = (String) DateFormat.format("MMM",  todayDate); // Jun
        String year         = (String) DateFormat.format("yyyy", todayDate); // 2013
         firebaseDatabase.getReference(DEFAULT_FIREBASE_TABLE)
                .child("gamelogs")
                .child("all-completed-Games")
                .child(monthString+"_"+year)
                .orderByKey().equalTo("2021-06-25").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                double totalRevenue = 0;
               for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                   CompletedGame completedGame = snapshot.getValue(CompletedGame.class);
                   totalRevenue+=completedGame.payableAmount;
               }

               setTotalRevenue(totalRevenue);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                int x = 0;
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int x = 0;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                int x = 0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                int x = 0;
            }
        });

    }

    private void observeCompletedGames() {
        Date todayDate = Utils.convertToDate(Utils.getTodayDate(Constants.DATE_FORMAT),Constants.DATE_FORMAT);
        String monthString  = (String) DateFormat.format("MMM",  todayDate); // Jun
        String year         = (String) DateFormat.format("yyyy", todayDate); // 2013
        DatabaseReference myRef = firebaseDatabase.getReference(DEFAULT_FIREBASE_TABLE)
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

