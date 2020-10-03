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

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import timber.log.Timber;

public class CameraViewModel extends BaseViewModel<BaseNavigator> {

    @NonNull
    private final CameraService cameraService;
    @NonNull
    private final SingleLiveEvent<Throwable> errorEvent;
    private final CompositeDisposable
            compositeDisposable = new CompositeDisposable();
    private final SingleLiveEvent<View> buttonCameraClickEvent = new SingleLiveEvent<>();
    private final MutableLiveData<Bitmap> cameraItem = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    @Inject
    public CameraViewModel(@NonNull Application application,
                         CameraService cameraService,
                         @Named("errorEvent") SingleLiveEvent<Throwable> errorEvent) {
        super(application);
        Timber.d("CameraViewModule created");
        //오브젝트 그래프로 부터 생성자 주입
        this.cameraService = cameraService;
        this.errorEvent = errorEvent;
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
