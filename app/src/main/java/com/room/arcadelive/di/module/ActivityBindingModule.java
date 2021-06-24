package com.room.arcadelive.di.module;

import com.room.arcadelive.view.activity.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();

}
