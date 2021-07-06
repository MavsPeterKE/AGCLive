package com.room.arcadelive.di.module;

import androidx.lifecycle.ViewModel;

import com.room.arcadelive.di.util.ViewModelKey;
import com.room.arcadelive.viewmodels.AddExpenseViewModel;
import com.room.arcadelive.viewmodels.CompletedGamesViewModel;
import com.room.arcadelive.viewmodels.EndDayViewModel;
import com.room.arcadelive.viewmodels.ExpenseViewModel;
import com.room.arcadelive.viewmodels.HomeViewModel;
import com.room.arcadelive.viewmodels.LiveGamesViewModel;
import com.room.arcadelive.viewmodels.RevenueViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LiveGamesViewModel.class)
    abstract ViewModel bindLiveGamesViewModel(LiveGamesViewModel liveGamesViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CompletedGamesViewModel.class)
    abstract ViewModel bindCompletedGamesViewModel(CompletedGamesViewModel completedGamesViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EndDayViewModel.class)
    abstract ViewModel bindEndDayViewModel(EndDayViewModel endDayViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    abstract ViewModel bindHomeViewModel(HomeViewModel homeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ExpenseViewModel.class)
    abstract ViewModel bindExpenseViewModel(ExpenseViewModel homeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AddExpenseViewModel.class)
    abstract ViewModel bindAddExpenseViewModel(AddExpenseViewModel homeViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(RevenueViewModel.class)
    abstract ViewModel bindRevenueViewModel(RevenueViewModel revenueViewModel);

}
