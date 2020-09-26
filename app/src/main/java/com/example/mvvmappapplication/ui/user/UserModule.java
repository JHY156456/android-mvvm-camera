package com.example.mvvmappapplication.ui.user;

import android.content.Context;
import android.view.LayoutInflater;

import com.example.mvvmappapplication.databinding.FragmentUserBinding;
import com.example.mvvmappapplication.di.ActivityContext;
import com.example.mvvmappapplication.di.FragmentScope;

import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {
    @Provides
    @FragmentScope
    FragmentUserBinding provideBinding(@ActivityContext Context context){
        return FragmentUserBinding.inflate(LayoutInflater.from(context));
    }


}
