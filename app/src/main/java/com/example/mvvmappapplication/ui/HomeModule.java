package com.example.mvvmappapplication.ui;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.databinding.ActivityHomeBinding;
import com.example.mvvmappapplication.di.ActivityContext;
import com.example.mvvmappapplication.di.ActivityScope;
import com.example.mvvmappapplication.di.FragmentScope;
import com.example.mvvmappapplication.ui.menu.CameraFragment;
import com.example.mvvmappapplication.ui.menu.CameraModule;
import com.example.mvvmappapplication.ui.user.UserFragment;
import com.example.mvvmappapplication.ui.user.UserModule;

import javax.inject.Named;

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

    @Provides
    @ActivityScope
    @Named("HomeActivity")
    static NavController provideNavController(HomeActivity activity){
        return Navigation.findNavController(activity,R.id.content);
    }


    /**
     * 서브컴포넌트 정의
     */
    @FragmentScope
    @ContributesAndroidInjector(modules = CameraModule.class)
    abstract CameraFragment getCameraFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = UserModule.class)
    abstract UserFragment getUserFragment();
}
