package com.example.mvvmappapplication.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mvvmappapplication.HomeViewModel;
import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.custom.HSRadioButton;
import com.example.mvvmappapplication.databinding.ActivityHomeBinding;
import com.example.mvvmappapplication.di.AppViewModelFactory;
import com.example.mvvmappapplication.ui.menu.CameraFragmentDirections;
import com.example.mvvmappapplication.ui.menu.HomeMenuFragmentDirections;
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

    private AppBarConfiguration mAppBarConfiguration;

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
                    navController.get().navigate(HomeMenuFragmentDirections.actionHomeActivityToHomeMenuFragment());
                    break;
                case R.id.myMenuBtn:
                    navController.get().navigate(UserFragmentDirections.actionHomeActivityToUserFragment(1));
                    break;
                case R.id.qrBtn:
                    navController.get().navigate(UserFragmentDirections.actionHomeActivityToQrFragment());
                    break;
                case R.id.cameraBtn:
                    navController.get().navigate(CameraFragmentDirections.actionHomeActivityToCameraFragment());
                    break;
            }
        });
        setSupportActionBar(binding.get().include.toolbar);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(binding.get().drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.get().navView, navController);
    }

    public void setCheckedFalseAllRadioButton(){
        binding.get().cameraBtn.setChecked(false);
        binding.get().qrBtn.setChecked(false);
        binding.get().myMenuBtn.setChecked(false);
        binding.get().homeBtn.setChecked(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
