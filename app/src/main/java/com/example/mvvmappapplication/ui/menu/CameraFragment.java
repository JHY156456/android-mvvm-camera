package com.example.mvvmappapplication.ui.menu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.databinding.FragmentCameraBinding;
import com.example.mvvmappapplication.di.AppViewModelFactory;
import com.example.mvvmappapplication.ui.post.PostAdapter;
import com.example.mvvmappapplication.ui.post.PostViewModel;
import com.example.mvvmappapplication.utils.PermissionUtil;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerFragment;

public class CameraFragment extends DaggerFragment implements View.OnClickListener {

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

    PostViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ViewModel 객체를 요청
        viewModel = new ViewModelProvider(this, viewModelFactory).get(PostViewModel.class);
//        if (savedInstanceState == null) {
//            // 데이터 요청, 프레그먼트가 재생성 되었을 때는 요청하지 않는다.
//            viewModel.loadPosts();
//        }

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
        binding.buttonCamera.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            byte[] byteArray = data.getByteArrayExtra("image");
            Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            binding.ivCompletedImage.setImageBitmap(image);
        }
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.button_camera :{
                if (!PermissionUtil.isGranted(getActivity(), PermissionUtil.REQUEST_CAMERA_PERMISSION)) {
                    PermissionUtil.requestPermissionCamera(getActivity());
                } else {
                    startActivityForResult(new Intent(getContext(), CameraViewActivity.class),100);
                }
            }
        }
    }
}