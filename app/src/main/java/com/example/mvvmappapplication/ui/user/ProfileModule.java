package com.example.mvvmappapplication.ui.user;

import android.content.Context;
import android.view.LayoutInflater;

import com.example.mvvmappapplication.databinding.FragmentProfileBinding;
import com.example.mvvmappapplication.di.ActivityContext;
import com.example.mvvmappapplication.di.FragmentScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ProfileModule {
    @Provides
    @FragmentScope
    FragmentProfileBinding provideBinding(@ActivityContext Context context){
        return FragmentProfileBinding.inflate(LayoutInflater.from(context));
    }
}
