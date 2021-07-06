package com.room.arcadelive.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.eightbitlab.bottomnavigationbar.BottomBarItem;
import com.eightbitlab.bottomnavigationbar.BottomNavigationBar;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.room.arcadelive.R;
import com.room.arcadelive.view.fragments.FragmentExpense;
import com.room.arcadelive.view.fragments.FragmentHome;
import com.room.arcadelive.view.fragments.FragmentRevenue;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.bottom_bar)
    BottomNavigationBar bottomNavigationBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setBottomNavigation();

    }

    private void setBottomNavigation() {
        BottomBarItem gameCountItem = new BottomBarItem(R.drawable.sales_icon, R.string.title_home);
        bottomNavigationBar.addTab(gameCountItem);
        BottomBarItem screenItem = new BottomBarItem(R.drawable.reduction, R.string.title_screen);
        bottomNavigationBar.addTab(screenItem);
        /*BottomBarItem gamersData = new BottomBarItem(R.drawable.games, R.string.title_gamers);
        bottomNavigationBar.addTab(gamersData);*/
        BottomBarItem eodItem = new BottomBarItem(R.drawable.cash_hand, R.string.title_eod);
        bottomNavigationBar.addTab(eodItem);

        //Create Default Home View
        startHomeView();

        //Bottom Navigation Action
        bottomNavigationBar.setOnSelectListener(position -> {
            switch (position) {
                case 0:
                    startHomeView();
                    break;
                case 1:
                    startExpenseFragment();
                    break;
                case 2:
                      startRevenueFragment();
                    break;
            /*    case 3:
                    startEODFragment();
                    break;*/
            }
        });
    }

    private void startExpenseFragment() {
        changeFragment(new FragmentExpense(),FragmentExpense.class.getSimpleName());
    }

    private void startRevenueFragment() {
        changeFragment(new FragmentRevenue(),FragmentRevenue.class.getSimpleName());
    }

    private void startGamersScreen() {
    }

    private void startHomeView() {
        changeFragment(new FragmentHome(), FragmentHome.class.getSimpleName());
    }

    public void changeFragment(Fragment fragment, String tagFragmentName) {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }
        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            fragmentTransaction.add(R.id.frame_container, fragmentTemp, tagFragmentName);
        } else {
            fragmentTransaction.show(fragmentTemp);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
    }


}