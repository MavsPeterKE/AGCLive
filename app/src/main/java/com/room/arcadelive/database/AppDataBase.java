package com.room.arcadelive.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.room.arcadelive.database.converters.DateConverter;
import com.room.arcadelive.database.dao.CompleteGameDao;
import com.room.arcadelive.database.entity.CompletedGame;
import com.room.arcadelive.database.entity.Customer;
import com.room.arcadelive.database.entity.CustomerVisit;
import com.room.arcadelive.database.views.CustomerView;

import static com.room.arcadelive.utils.Constants.RoomConfigs.NEW_DB_VERSION;

@Database(entities = {CompletedGame.class, Customer.class, CustomerVisit.class},views = {CustomerView.class},
        version = NEW_DB_VERSION)
@TypeConverters(DateConverter.class)
public abstract class AppDataBase extends RoomDatabase {

    public abstract CompleteGameDao completeGameDao();

}
