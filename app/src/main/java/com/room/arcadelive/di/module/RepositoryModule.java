package com.room.arcadelive.di.module;


import com.room.arcadelive.repository.UserRepository;

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
    UserRepository provideAuthRepository() {
        return new UserRepository();
    }


}
