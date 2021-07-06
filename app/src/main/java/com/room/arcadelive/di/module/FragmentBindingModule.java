package com.room.arcadelive.di.module;


import com.room.arcadelive.view.fragments.FragmentAddExpense;
import com.room.arcadelive.view.fragments.FragmentCompletedGames;
import com.room.arcadelive.view.fragments.FragmentExpense;
import com.room.arcadelive.view.fragments.FragmentHome;
import com.room.arcadelive.view.fragments.FragmentLiveGames;
import com.room.arcadelive.view.fragments.FragmentEndDays;
import com.room.arcadelive.view.fragments.FragmentRevenue;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    abstract FragmentLiveGames bindFragmentLiveGames();

    @ContributesAndroidInjector
    abstract FragmentCompletedGames bindFragmentCompletedGames();

    @ContributesAndroidInjector
    abstract FragmentEndDays bindFragmentEndDays();

    @ContributesAndroidInjector
    abstract FragmentHome bindFragmentHome();

    @ContributesAndroidInjector
    abstract FragmentExpense bindFragmentExpense();

    @ContributesAndroidInjector
    abstract FragmentAddExpense bindFragmentAddExpense();

    @ContributesAndroidInjector
    abstract FragmentRevenue bindFFragmentRevenue();

}
