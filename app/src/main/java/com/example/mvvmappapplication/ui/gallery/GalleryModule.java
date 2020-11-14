package com.example.mvvmappapplication.ui.gallery;

import android.content.Context;

import androidx.databinding.DataBindingUtil;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.databinding.FragmentGalleryBinding;
import com.example.mvvmappapplication.di.ActivityContext;
import com.example.mvvmappapplication.di.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class GalleryModule {

    @Provides
    @ActivityScope
    static FragmentGalleryBinding provideCameraViewBinding(GalleryActivity activity) {
        return DataBindingUtil.setContentView(activity, R.layout.fragment_gallery);
    }

    @Provides
    @ActivityContext
    static Context provideContext(GalleryActivity activity) {
        return activity;
    }
}
