package com.example.mvvmappapplication.ui.slideshow;

import android.content.Context;

import androidx.databinding.DataBindingUtil;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.databinding.FragmentSlideshowBinding;
import com.example.mvvmappapplication.di.ActivityContext;
import com.example.mvvmappapplication.di.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class SlideshowModule {

    @Provides
    @ActivityScope
    static FragmentSlideshowBinding provideCameraViewBinding(SlideshowActivity activity) {
        return DataBindingUtil.setContentView(activity, R.layout.fragment_slideshow);
    }

    @Provides
    @ActivityContext
    static Context provideContext(SlideshowActivity activity) {
        return activity;
    }
}
