package com.example.mvvmappapplication.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mvvmappapplication.HomeViewModel;
import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.custom.HSRadioButton;
import com.example.mvvmappapplication.databinding.ActivityHomeBinding;
import com.example.mvvmappapplication.di.AppViewModelFactory;
import com.example.mvvmappapplication.ui.menu.CameraFragmentDirections;
import com.example.mvvmappapplication.ui.post.SlidingAdapter;
import com.example.mvvmappapplication.utils.BackPressCloseHandler;
import com.example.mvvmappapplication.utils.EventObserver;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Lazy;

public class HomeActivity extends BaseActivity {
    @Inject
    Lazy<ActivityHomeBinding> binding;
    HomeViewModel viewModel;
    @Inject
    AppViewModelFactory viewModelFactory;
    @Inject
    @Named("HomeActivity")
    Lazy<NavController> navController;
    @Inject
    @Named("asdf")
    LinearLayoutManager layoutManager;
    @Inject
    SlidingAdapter adapter;
    private AppBarConfiguration mAppBarConfiguration;
    @Inject
    BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.get().setLifecycleOwner(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(HomeViewModel.class);
        binding.get().setViewModel(viewModel);
        super.setDrawerLayoutAndToolbar();
        super.setToolbarTitle("촬영");

        viewModel.getBottomMenuClickEvent().observe(this, view -> {
            setCheckedFalseAllRadioButton();
            HSRadioButton radioButton = (HSRadioButton) view;
            radioButton.setChecked(true);
            binding.get().include.appBarLayoutMain.appBar.setExpanded(false);
            switch (view.getId()) {
                case R.id.homeBtn:
                    navController.get().navigate(CameraFragmentDirections.actionHomeActivityToHomeMenuFragment());
                    binding.get().include.appBarLayoutMain.toolbarLayout.setTitle("홈");
                    binding.get().include.appBarLayoutMain.appBar.setExpanded(true);
                    break;
                case R.id.cameraBtn:
                    navController.get().navigate(CameraFragmentDirections.actionHomeActivityToCameraFragment());
                    binding.get().include.appBarLayoutMain.toolbarLayout.setTitle("촬영");
                    break;
                case R.id.qrBtn:
                    navController.get().navigate(CameraFragmentDirections.actionHomeActivityToQrFragment());
                    binding.get().include.appBarLayoutMain.toolbarLayout.setTitle("QR");
                    break;
                case R.id.myMenuBtn:
                    navController.get().navigate(CameraFragmentDirections.actionHomeActivityToUserFragment(1));
                    binding.get().include.appBarLayoutMain.toolbarLayout.setTitle("프로필");
                    break;
            }
        });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.camera_fragment, R.id.home_menu_fragment, R.id.qr_fragment, R.id.user_frmagnet)
                .setOpenableLayout(binding.get().drawerLayout)
                .build();
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        /**
         * 왼쪽 위 서랍 아이콘 셋
         */
        NavigationUI.setupActionBarWithNavController(this, navController.get(), mAppBarConfiguration);
        //NavigationUI.setupWithNavController(binding.get().navView, navController.get());
        // collapsed : 살짝 올라와있는 상태
        binding.get().slidingLayout.setFadeOnClickListener(view -> {
            binding.get().slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        });
        binding.get().listView.setLayoutManager(layoutManager);
        binding.get().listView.setAdapter(adapter);
        viewModel.getSlidingUpData().observe(this, list -> {
            adapter.setItems(list);
        });

        viewModel.getOpenSlidingUpPopup().observe(this, new EventObserver<>(data -> {
            boolean isOpen = (Boolean) data;
            if (isOpen) {
                binding.get().slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        }));
    }


    @Override
    public void onBackPressed() {
        if (binding.get().slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            if (mAppBarConfiguration.getTopLevelDestinations().contains(navController.get().getCurrentDestination().getId())) {
                backPressCloseHandler.onBackPressed();
            } else {
                super.onBackPressed();
            }
        } else if (binding.get().slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            //binding.get().slidingUpBackLayout.setClickable(false);
            binding.get().slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }

    public void setCheckedFalseAllRadioButton() {
        binding.get().cameraBtn.setChecked(false);
        binding.get().qrBtn.setChecked(false);
        binding.get().myMenuBtn.setChecked(false);
        binding.get().homeBtn.setChecked(false);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_panel:
                //binding.get().slidingUpBackLayout.setClickable(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController.get(), mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}
