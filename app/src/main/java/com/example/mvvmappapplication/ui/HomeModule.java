package com.example.mvvmappapplication.ui;

import android.content.Context;

import androidx.databinding.DataBindingUtil;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.databinding.ActivityHomeBinding;
import com.example.mvvmappapplication.di.ActivityContext;
import com.example.mvvmappapplication.di.ActivityScope;
import com.example.mvvmappapplication.di.FragmentScope;
import com.example.mvvmappapplication.ui.menu.CameraFragment;
import com.example.mvvmappapplication.ui.menu.CameraModule;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class HomeModule {
    @Provides
    @ActivityScope
    static ActivityHomeBinding provideHomeBinding(HomeActivity activity) {
        return DataBindingUtil.setContentView(activity, R.layout.activity_home);
    }
    @Provides
    @ActivityContext
    static Context provideContext(HomeActivity activity) {
        return activity;
    }

    @FragmentScope
    @ContributesAndroidInjector(modules = CameraModule.class)
    abstract CameraFragment getCameraFragment();

}
