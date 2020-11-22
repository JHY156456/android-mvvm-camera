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

import com.example.mvvmappapplication.HomeViewModel;
import com.example.mvvmappapplication.databinding.FragmentCameraBinding;
import com.example.mvvmappapplication.di.AppViewModelFactory;
import com.example.mvvmappapplication.utils.PermissionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerFragment;
import timber.log.Timber;

public class CameraFragment extends DaggerFragment {

    /**
     * 오브젝트 그래프로부터 멤버 인젝션
     */
    @Inject
    FragmentCameraBinding binding;
    @Inject
    AppViewModelFactory viewModelFactory;
    @Inject
    Lazy<NavController> navController;

    CameraViewModel viewModel;
    HomeViewModel homeViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ViewModel 객체를 요청
        viewModel = new ViewModelProvider(this, viewModelFactory).get(CameraViewModel.class);
        homeViewModel = new ViewModelProvider(getActivity(), viewModelFactory).get(HomeViewModel.class);
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
        //setHomeViewModel을 해야 activity의 viewmodel을 사용할 수 있다.
        binding.setHomeViewModel(homeViewModel);
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
                binding.buttonCall.setVisibility(View.GONE);
                binding.buttonImmediatelyCall.setVisibility(View.GONE);
            } else {
                binding.ivCompletedImage.setImageBitmap(bitmap);
                binding.buttonCall.setVisibility(View.VISIBLE);
                binding.buttonImmediatelyCall.setVisibility(View.VISIBLE);
            }
        });
        viewModel.getResponseBodySingleLiveEvent().observe(getViewLifecycleOwner(), response -> {
            JSONObject jsonObj = null;
            String resultStr = "";
            String phoneNum = "";
            try {
                jsonObj = new JSONObject(response.body().string());
                resultStr = jsonObj.getString("resultStr");
                phoneNum = jsonObj.getString("phoneNum");
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            Intent tt;
            if (!phoneNum.equals("null")) {
                tt = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
            } else {
                tt = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "01063797794"));
            }
            startActivity(tt);
            Toast.makeText(getActivity(), "resultStr : " + resultStr + "\nphoneNum : " + phoneNum, Toast.LENGTH_SHORT).show();
        });
        viewModel.getResponseSingleLiveEvent().observe(getViewLifecycleOwner(), response -> {
            JSONObject jsonObj = null;
            String phoneNum = "";
            try {
                jsonObj = new JSONObject(response.body().string());
                phoneNum = jsonObj.getString("phoneNum");
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            Intent tt;
            if (!phoneNum.equals("null")) {
                tt = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
            } else {
                tt = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "01063797794"));
            }
            startActivity(tt);
        });

        viewModel.getErrorEvent().observe(getViewLifecycleOwner(), throwable -> {
            Timber.e("throwable : " + throwable.getMessage());
            //Toast.makeText(getActivity(), "error : " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
        });




        viewModel.getButtonClickImmediatelyCallEvent().observe(getViewLifecycleOwner(), aBoolean -> {
            //new AsyncTess().execute(viewModel.getCameraItem().getValue());
            if(aBoolean){
                Toast.makeText(getActivity(), "인식 번호 : " + viewModel.getTesseractCarNumber().getValue(), Toast.LENGTH_LONG).show();
            } else{
                Toast.makeText(getActivity(), "번호판 문자인식에 실패했습니다", Toast.LENGTH_LONG).show();

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