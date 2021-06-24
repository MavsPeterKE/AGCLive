package com.room.arcadelive.di.component;

import android.app.Application;

import com.room.arcadelive.di.module.ActivityBindingModule;
import com.room.arcadelive.di.module.ApplicationModule;
import com.room.arcadelive.di.module.ContextModule;
import com.room.arcadelive.di.module.FragmentBindingModule;
import com.room.arcadelive.di.module.RetrofitModule;
import com.room.arcadelive.di.module.RoomModule;
import com.room.arcadelive.di.module.ViewModelModule;
import com.room.arcadelive.utils.BaseApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.android.support.DaggerApplication;

@Singleton
@Component(modules = {ContextModule.class, ApplicationModule.class, FragmentBindingModule.class,
        AndroidSupportInjectionModule.class, ActivityBindingModule.class, ViewModelModule.class,
        RetrofitModule.class, RoomModule.class})
public interface ApplicationComponent extends AndroidInjector<DaggerApplication> {

    void inject(BaseApplication application);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        ApplicationComponent build();
    }
}