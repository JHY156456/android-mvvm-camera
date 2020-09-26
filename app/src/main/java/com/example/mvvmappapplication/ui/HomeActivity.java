package com.example.mvvmappapplication.ui;

import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.mvvmappapplication.databinding.ActivityHomeBinding;
import com.example.mvvmappapplication.di.AppViewModelFactory;
import com.example.mvvmappapplication.ui.user.UserViewModel;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

public class HomeActivity extends DaggerAppCompatActivity {
    @Inject
    Lazy<ActivityHomeBinding> binding;
    UserViewModel viewModel;
    @Inject
    AppViewModelFactory viewModelFactory;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        binding.get().setLifecycleOwner(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(UserViewModel.class);
    }
}
