package com.example.mvvmappapplication.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.example.mvvmappapplication.databinding.FragmentCameraBinding;
import com.example.mvvmappapplication.di.AppViewModelFactory;
import com.example.mvvmappapplication.ui.post.PostAdapter;
import com.example.mvvmappapplication.utils.PermissionUtil;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerFragment;

public class CameraFragment extends DaggerFragment {

    /**
     * 오브젝트 그래프로부터 멤버 인젝션
     */
    @Inject
    FragmentCameraBinding binding;
    @Inject
    AppViewModelFactory viewModelFactory;
    @Inject
    PostAdapter adapter;

    @Inject
    Lazy<NavController> navController;

    CameraViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ViewModel 객체를 요청
        viewModel = new ViewModelProvider(this, viewModelFactory).get(CameraViewModel.class);
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
        viewModel.getButtonCameraClickEvent().observe(getViewLifecycleOwner(), view1 -> {
            if (!PermissionUtil.isGranted(getActivity(), PermissionUtil.REQUEST_CAMERA_PERMISSION)) {
                PermissionUtil.requestPermissionCamera(getActivity());
            } else {
                startActivityForResult(new Intent(getContext(), CameraViewActivity.class), 100);
            }
        });
        viewModel.getCameraItem().observe(getViewLifecycleOwner(), bitmap -> {
            if (bitmap == null) {
                Toast.makeText(getActivity(), "번호판 인식에 실패했습니다. 다시 촬영해주세요", Toast.LENGTH_SHORT).show();
            } else {
                binding.ivCompletedImage.setImageBitmap(bitmap);
            }
        });
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