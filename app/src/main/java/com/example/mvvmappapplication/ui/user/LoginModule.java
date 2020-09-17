package com.example.mvvmappapplication.ui.user;

import android.content.Context;

import androidx.databinding.DataBindingUtil;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.databinding.ActivityLoginBinding;
import com.example.mvvmappapplication.di.ActivityContext;
import com.example.mvvmappapplication.di.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class LoginModule {
    @Provides
    @ActivityScope
    static ActivityLoginBinding provideBinding(LoginActivity activity) {
        return DataBindingUtil.setContentView(activity, R.layout.activity_login);
    }

    @Provides
    @ActivityContext
    static Context provideContext(LoginActivity activity) {
        return activity;
    }
}
