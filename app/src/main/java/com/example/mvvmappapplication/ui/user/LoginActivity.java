package com.example.mvvmappapplication.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.databinding.ActivityLoginBinding;
import com.example.mvvmappapplication.di.AppViewModelFactory;
import com.example.mvvmappapplication.ui.HomeActivity;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;
import timber.log.Timber;

public class LoginActivity extends DaggerAppCompatActivity {
    @Inject
    Lazy<ActivityLoginBinding> binding;
    UserViewModel viewModel;
    @Inject
    AppViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        binding.get().setLifecycleOwner(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(UserViewModel.class);
        binding.get().setViewModel(viewModel);
        initLiveItems();
    }

    public void initLiveItems() {
        viewModel.getResponseBodySingleEvent().observe(this,responseBody -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });

        viewModel.getErrorEvent().observe(this,throwable -> {
            Timber.e(throwable.getMessage());
        });
    }

    public void buttonClick(View view) {
        switch (view.getId()) {
            case R.id.tvSignIn: {
                startActivity(new Intent(this,RegistrationActivity.class));
            }
        }

    }
}
