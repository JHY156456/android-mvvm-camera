package com.example.mvvmappapplication.ui.slideshow;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mvvmappapplication.databinding.FragmentSlideshowBinding;
import com.example.mvvmappapplication.di.AppViewModelFactory;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;


public class SlideshowActivity extends DaggerAppCompatActivity {

    @Inject
    Lazy<FragmentSlideshowBinding> binding;
    SlideshowViewModel viewModel;
    @Inject
    AppViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.get().setLifecycleOwner(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(SlideshowViewModel.class);
        binding.get().setViewModel(viewModel);

        viewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.get().textSlideshow.setText(s);
            }
        });
    }
}