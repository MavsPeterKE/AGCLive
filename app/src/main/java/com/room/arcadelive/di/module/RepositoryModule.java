package com.room.arcadelive.di.module;


import com.room.arcadelive.repository.UserRepository;
import com.room.arcadelive.retrofit.RetrofitService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {RetrofitModule.class, RoomModule.class})
public class RepositoryModule {

    @Singleton
    @Provides
    ExecutorService provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }


    @Provides
    UserRepository provideAuthRepository(RetrofitService retrofitService) {
        return new UserRepository(retrofitService);
    }


}
