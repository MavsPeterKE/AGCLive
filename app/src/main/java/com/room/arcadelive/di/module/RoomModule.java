package com.room.arcadelive.di.module;

import android.app.Application;

import androidx.room.Room;

import com.room.arcadelive.database.AppDataBase;
import com.room.arcadelive.database.dao.CompleteGameDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.room.arcadelive.utils.Constants.RoomConfigs.DATABASE_NAME;


@Module
public class RoomModule {

    @Singleton
    @Provides
    AppDataBase provideAppDataBase(Application application) {
        return Room.databaseBuilder(application, AppDataBase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @Singleton
    @Provides
    CompleteGameDao provideCompleteGameDao(AppDataBase appDataBase) {
        return appDataBase.completeGameDao();
    }


}
