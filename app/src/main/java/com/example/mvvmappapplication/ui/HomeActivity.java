package com.example.mvvmappapplication.ui;

import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.example.mvvmappapplication.HomeViewModel;
import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.custom.HSRadioButton;
import com.example.mvvmappapplication.databinding.ActivityHomeBinding;
import com.example.mvvmappapplication.di.AppViewModelFactory;
import com.example.mvvmappapplication.ui.menu.CameraFragmentDirections;
import com.example.mvvmappapplication.ui.user.UserFragmentDirections;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

public class HomeActivity extends DaggerAppCompatActivity {
    @Inject
    Lazy<ActivityHomeBinding> binding;
    HomeViewModel viewModel;
    @Inject
    AppViewModelFactory viewModelFactory;
    @Inject
    @Named("HomeActivity")
    Lazy<NavController> navController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        binding.get().setLifecycleOwner(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(HomeViewModel.class);
        binding.get().setViewModel(viewModel);
        viewModel.getBottomMenuClickEvent().observe(this, view -> {
            setCheckedFalseAllRadioButton();
            HSRadioButton radioButton = (HSRadioButton) view;
            radioButton.setChecked(true);
            switch (view.getId()){
                case R.id.homeBtn:
                    break;
                case R.id.myMenuBtn:
                    navController.get().navigate(UserFragmentDirections.actionHomeActivityToUserFragment(1));
                    break;
                case R.id.cameraBtn:
                    navController.get().navigate(CameraFragmentDirections.actionHomeActivityToCameraFragment());
                    break;
            }
        });
    }

    public void setCheckedFalseAllRadioButton(){
        binding.get().cameraBtn.setChecked(false);
        binding.get().myMenuBtn.setChecked(false);
        binding.get().homeBtn.setChecked(false);
    }

}