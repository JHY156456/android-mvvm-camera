package com.example.mvvmappapplication.ui.menu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.example.mvvmappapplication.databinding.FragmentQrBinding;
import com.example.mvvmappapplication.di.AppViewModelFactory;
import com.example.mvvmappapplication.utils.EventObserver;
import com.example.mvvmappapplication.utils.PermissionUtil;
import com.google.zxing.integration.android.IntentIntegrator;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerFragment;

public class QRFragment extends DaggerFragment {

    /**
     * 오브젝트 그래프로부터 멤버 인젝션
     */
    @Inject
    FragmentQrBinding binding;
    @Inject
    AppViewModelFactory viewModelFactory;
    @Inject
    Lazy<NavController> navController;

    QRViewModel viewModel;
    private IntentIntegrator qrScan;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ViewModel 객체를 요청
        viewModel = new ViewModelProvider(this, viewModelFactory).get(QRViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Lifecycle Owner 등록
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);
        initEvent();
    }

    private void initEvent() {
        viewModel.getButtonCameraClickEvent().observe(getViewLifecycleOwner(), view1 -> {
            if (!PermissionUtil.isGranted(getActivity(), PermissionUtil.REQUEST_CAMERA_PERMISSION)) {
                PermissionUtil.requestPermissionCamera(getActivity());
            } else {
                qrScan = IntentIntegrator.forSupportFragment(this);
                qrScan.setOrientationLocked(true);
                qrScan.setPrompt("QR코드를 촬영하세요");
                qrScan.initiateScan();
            }
        });

        viewModel.getIsFail().observe(getViewLifecycleOwner(), new EventObserver<>(data -> {
            boolean isFail = (Boolean) data;
            if (isFail) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        }));

        viewModel.getResultContents().observe(getViewLifecycleOwner(), new EventObserver<>(data -> {
            String resultData = (String)data;

            if(resultData != null){
                Toast.makeText(getActivity(), "resultData : " + resultData, Toast.LENGTH_LONG).show();
                Intent tt;
                tt = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "01063797794"));
                startActivity(tt);
            }
        }));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtil.REQUEST_CAMERA_PERMISSION) {
            if (PermissionUtil.isGrantedAll(permissions, grantResults)) {
                startActivityForResult(new Intent(getContext(), CameraViewActivity.class), 100);
            } else {
                PermissionUtil.showDeniedDialog(getActivity(), false);
            }
        }
    }
}