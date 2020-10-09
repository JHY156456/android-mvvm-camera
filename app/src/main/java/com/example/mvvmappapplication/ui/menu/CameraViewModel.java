package com.example.mvvmappapplication.ui.menu;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvmappapplication.data.CameraService;
import com.example.mvvmappapplication.ui.BaseNavigator;
import com.example.mvvmappapplication.ui.BaseViewModel;
import com.example.mvvmappapplication.utils.SingleLiveEvent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import timber.log.Timber;

public class CameraViewModel extends BaseViewModel<BaseNavigator> {

    @NonNull
    private final CameraService cameraService;
    private final CompositeDisposable
            compositeDisposable = new CompositeDisposable();
    private final SingleLiveEvent<View> buttonCameraClickEvent = new SingleLiveEvent<>();
    private final MutableLiveData<Bitmap> cameraItem = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    @Inject
    public CameraViewModel(@NonNull Application application,
                         CameraService cameraService,
                         @Named("errorEvent") SingleLiveEvent<Throwable> errorEvent,
                           @Named("responseBodySingleLiveEvent") SingleLiveEvent<ResponseBody> responseBodySingleLiveEvent) {
        super(application,errorEvent,responseBodySingleLiveEvent);
        Timber.d("CameraViewModule created");
        //오브젝트 그래프로 부터 생성자 주입
        this.cameraService = cameraService;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Timber.d("onCleared");
        compositeDisposable.dispose();
    }

    public SingleLiveEvent<View> getButtonCameraClickEvent() {
        return buttonCameraClickEvent;
    }
    public MutableLiveData<Bitmap> getCameraItem(){
        return cameraItem;
    }
    public void onClickButtonCamera(View view){
        buttonCameraClickEvent.setValue(view);
    }

    public void onClickCall(View view)  {
        buttonCameraClickEvent.setValue(view);

        File f = new File("asdf");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Convert bitmap to byte array
        Bitmap bitmap = cameraItem.getValue();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", f.getName(), reqFile);
        compositeDisposable.add(cameraService.uploadFoodImage(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getResponseBodySingleEvent()::setValue, getErrorEvent()::setValue));
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==100){
            if(resultCode== Activity.RESULT_OK){
                byte[] byteArray = data.getByteArrayExtra("image");
                Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                cameraItem.setValue(image);
            } else{
                cameraItem.setValue(null);
            }
        }
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }
}
