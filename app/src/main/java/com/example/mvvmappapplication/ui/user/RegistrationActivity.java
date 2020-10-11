package com.example.mvvmappapplication.ui.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.mvvmappapplication.databinding.ActivityRegistrationBinding;
import com.example.mvvmappapplication.di.AppViewModelFactory;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;
import timber.log.Timber;

public class RegistrationActivity extends
        DaggerAppCompatActivity {
    @Inject
    Lazy<ActivityRegistrationBinding> binding;
    UserViewModel viewModel;
    @Inject
    AppViewModelFactory viewModelFactory;
    @SuppressLint("TimberArgCount")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        binding.get().setLifecycleOwner(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(UserViewModel.class);
        binding.get().setViewModel(viewModel);
        viewModel.getResponseBodySingleEvent().observe(this,responseBody -> {
            //로그인액티비티를 띄운다.
            Timber.e(responseBody.toString());
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        });
        viewModel.getErrorEvent().observe(this,hi->{
            Timber.e(hi.toString());
        });
    }
}