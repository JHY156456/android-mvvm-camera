package com.example.mvvmappapplication.ui.menu;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import androidx.lifecycle.ViewModelProvider;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.custom.AutoFitTextureView;
import com.example.mvvmappapplication.databinding.ActivityCameraViewBinding;
import com.example.mvvmappapplication.di.AppViewModelFactory;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

public class CameraViewActivity extends DaggerAppCompatActivity {
    private AutoFitTextureView mCameraTextureView;
    private CameraPreview mPreview;
    private Button mNormalAngleButton;
    private Button mWideAngleButton;
    private Button mCameraCaptureButton;
    private Button mCameraDirectionButton;

    Activity mainActivity = this;

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


        mNormalAngleButton = (Button) findViewById(R.id.normal);
        mWideAngleButton = (Button) findViewById(R.id.wide);
        mCameraCaptureButton = (Button) findViewById(R.id.capture);
        mCameraDirectionButton = (Button) findViewById(R.id.change);

        mCameraTextureView =  findViewById(R.id.cameraTextureView);
        mPreview = new CameraPreview(this, mCameraTextureView, mNormalAngleButton, mWideAngleButton, mCameraCaptureButton, mCameraDirectionButton);

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
