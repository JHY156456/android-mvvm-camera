package com.example.mvvmappapplication.ui;

import android.os.Bundle;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.databinding.ActivityMainBinding;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {
    @Inject
    Lazy<ActivityMainBinding> binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding.get().setLifecycleOwner(this);
    }
}