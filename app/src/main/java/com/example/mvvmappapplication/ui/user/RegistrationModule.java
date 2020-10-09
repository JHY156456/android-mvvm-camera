package com.example.mvvmappapplication.ui.user;

import android.content.Context;

import androidx.databinding.DataBindingUtil;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.databinding.ActivityRegistrationBinding;
import com.example.mvvmappapplication.di.ActivityContext;
import com.example.mvvmappapplication.di.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class RegistrationModule {

    @Provides
    @ActivityScope
    static ActivityRegistrationBinding provideRegistrationBinding(RegistrationActivity activity) {
        return DataBindingUtil.setContentView(activity, R.layout.activity_registration);
    }

    @Provides
    @ActivityContext
    static Context provideContext(RegistrationActivity activity) {
        return activity;
    }
}
