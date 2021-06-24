package com.room.arcadelive.di.module;


import com.room.arcadelive.view.fragments.FragmentCompletedGames;
import com.room.arcadelive.view.fragments.FragmentLiveGames;
import com.room.arcadelive.view.fragments.FragmentEndDays;

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

}
