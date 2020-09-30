package com.example.mvvmappapplication.ui.user;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.databinding.ActivityLoginBinding;
import com.example.mvvmappapplication.di.AppViewModelFactory;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

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
        initLiveItems();
    }

    public void initLiveItems() {
        viewModel.getResponse().observe((LifecycleOwner) getLifecycle(), response -> {
            if (response.isSuccessful()) {
                //홈액티비티를 띄운다
            } else {

            }
        });
    }

    public void buttonClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignIn: {
                viewModel.login(binding.get().atvEmailLog.getText().toString()
                        , binding.get().atvPasswordLog.getText().toString());
            }
            case R.id.tvSignIn: {

            }
        }

    }
}
