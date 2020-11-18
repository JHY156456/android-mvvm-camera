package com.example.mvvmappapplication.ui.gallery;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.mvvmappapplication.databinding.FragmentGalleryBinding;
import com.example.mvvmappapplication.di.AppViewModelFactory;
import com.example.mvvmappapplication.ui.BaseActivity;

import javax.inject.Inject;

import dagger.Lazy;


public class GalleryActivity extends BaseActivity {

    @Inject
    Lazy<FragmentGalleryBinding> binding;
    GalleryViewModel viewModel;
    @Inject
    AppViewModelFactory viewModelFactory;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.get().setLifecycleOwner(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(GalleryViewModel.class);
        binding.get().setViewModel(viewModel);
        super.setDrawerLayoutAndToolbar();
        super.setToolbarTitle("갤러리");
        super.setAppBarConfigurationForLeftMenuIcon();
        viewModel.getText().observe(this, s -> binding.get().textGallery.setText(s));

    }
}