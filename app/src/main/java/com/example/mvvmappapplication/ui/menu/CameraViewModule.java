package com.example.mvvmappapplication.ui.menu;

import android.content.Context;

import androidx.databinding.DataBindingUtil;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.databinding.ActivityCameraViewBinding;
import com.example.mvvmappapplication.di.ActivityContext;
import com.example.mvvmappapplication.di.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class CameraViewModule {
    @Provides
    @ActivityScope
    static ActivityCameraViewBinding provideCameraViewBinding(CameraViewActivity activity) {
        return DataBindingUtil.setContentView(activity, R.layout.activity_camera_view);
    }

    @Provides
    @ActivityContext
    static Context provideContext(CameraViewActivity activity) {
        return activity;
    }

}
