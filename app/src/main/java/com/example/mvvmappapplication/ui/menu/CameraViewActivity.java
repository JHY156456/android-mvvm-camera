package com.example.mvvmappapplication.ui.menu;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.example.mvvmappapplication.custom.AutoFitTextureView;
import com.example.mvvmappapplication.databinding.ActivityCameraViewBinding;
import com.example.mvvmappapplication.di.AppViewModelFactory;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

public class CameraViewActivity extends DaggerAppCompatActivity {
    private AutoFitTextureView mCameraTextureView;
    private CameraPreview mPreview;

    private static final String TAG = "MAINACTIVITY";

    static final int REQUEST_CAMERA = 1;
    @Inject
    Lazy<ActivityCameraViewBinding> binding;
    CameraViewModel viewModel;
    @Inject
    AppViewModelFactory viewModelFactory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.get().setLifecycleOwner(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(CameraViewModel.class);
        binding.get().setViewModel(viewModel);

        mPreview = new CameraPreview(this,
                binding.get().cameraTextureView,
                binding.get().normal,
                binding.get().wide,
                binding.get().capture,
                binding.get().change);
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Log.d("jhy","CameraViewActivity onRequestPermissionsResult");
//        switch (requestCode) {
//            case REQUEST_CAMERA:
//                for (int i = 0; i < permissions.length; i++) {
//                    String permission = permissions[i];
//                    int grantResult = grantResults[i];
//                    if (permission.equals(Manifest.permission.CAMERA)) {
//                        if(grantResult == PackageManager.PERMISSION_GRANTED) {
//                            mCameraTextureView = (TextureView) findViewById(R.id.cameraTextureView);
//                            mPreview = new Preview(this, mCameraTextureView, mNormalAngleButton, mWideAngleButton, mCameraCaptureButton, mCameraDirectionButton);
//                            mPreview.openCamera();
//                        } else {
//                            Toast.makeText(this,"Should have camera permission to run", Toast.LENGTH_LONG).show();
//                            finish();
//                        }
//                    }
//                }
//                break;
//        }
//    }


    @Override
    protected void onResume() {
        super.onResume();
        mPreview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreview.onPause();
    }
}
