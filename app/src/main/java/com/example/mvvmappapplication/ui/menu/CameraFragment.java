package com.example.mvvmappapplication.ui.menu;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.googlecode.tesseract.android.TessBaseAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    TessBaseAPI tessBaseAPI;

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
        viewModel.getErrorEvent().observe(getViewLifecycleOwner(), throwable -> {
            Timber.e("throwable : " + throwable.getMessage());
            Toast.makeText(getActivity(), "error : " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
        });


        tessBaseAPI = new TessBaseAPI();
        String dir = getActivity().getFilesDir() + "/tesseract";
        if (checkLanguageFile(dir + "/tessdata"))
            tessBaseAPI.init(dir, "kor");

        viewModel.getButtonClickImmediatelyCallEvent().observe(getViewLifecycleOwner(), aBoolean -> {
            new AsyncTess().execute(viewModel.getCameraItem().getValue());
        });
    }

    boolean checkLanguageFile(String dir) {
        File file = new File(dir);
        if (!file.exists() && file.mkdirs())
            createFiles(dir);
        else if (file.exists()) {
            String filePath = dir + "/kor.traineddata";
            File langDataFile = new File(filePath);
            if (!langDataFile.exists())
                createFiles(dir);
        }
        return true;
    }

    private void createFiles(String dir) {
        AssetManager assetMgr = getResources().getAssets();

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = assetMgr.open("kor.traineddata");

            String destFile = dir + "/kor.traineddata";

            outputStream = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private class AsyncTess extends AsyncTask<Bitmap, Integer, String> {
        @Override
        protected String doInBackground(Bitmap... mRelativeParams) {
            tessBaseAPI.setImage(mRelativeParams[0]);
            return tessBaseAPI.getUTF8Text();
        }

        protected void onPostExecute(String result) {
            //특수문자 제거
            String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
            result = result.replaceAll(match, " ");
            result = result.replaceAll(" ", "");
            if (result.length() >= 7 && result.length() <= 8) {
                Toast.makeText(getActivity(), "인식!! : " + result, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "번호판 문자인식에 실패했습니다", Toast.LENGTH_LONG).show();
            }
        }
    }
}