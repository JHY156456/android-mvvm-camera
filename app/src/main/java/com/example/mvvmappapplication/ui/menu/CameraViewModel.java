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

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import timber.log.Timber;

public class CameraViewModel extends BaseViewModel<BaseNavigator> {

    @NonNull
    private final CameraService cameraService;
    @NonNull
    private final SingleLiveEvent<Response<ResponseBody>> responseBodySingleLiveEvent;
    private final CompositeDisposable
            compositeDisposable = new CompositeDisposable();
    private final SingleLiveEvent<View> buttonCameraClickEvent = new SingleLiveEvent<>();
    private final MutableLiveData<Bitmap> cameraItem = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    @Inject
    public CameraViewModel(@NonNull Application application,
                           CameraService cameraService,
                           @Named("errorEvent") SingleLiveEvent<Throwable> errorEvent,
                           @Named("responseBodySingleLiveEvent") SingleLiveEvent<Response<ResponseBody>> responseBodySingleLiveEvent) {
        super(application, errorEvent);
        Timber.d("CameraViewModule created");
        //오브젝트 그래프로 부터 생성자 주입
        this.cameraService = cameraService;
        this.responseBodySingleLiveEvent = responseBodySingleLiveEvent;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Timber.d("onCleared");
        compositeDisposable.dispose();
    }

    @NonNull
    public SingleLiveEvent<Response<ResponseBody>> getResponseBodySingleLiveEvent() {
        return responseBodySingleLiveEvent;
    }

    public SingleLiveEvent<View> getButtonCameraClickEvent() {
        return buttonCameraClickEvent;
    }

    public MutableLiveData<Bitmap> getCameraItem() {
        return cameraItem;
    }

    public void onClickButtonCamera(View view) {
        buttonCameraClickEvent.setValue(view);
    }

    File f;

    public void onClickCall(View view) {
        loading.setValue(true);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
        MultipartBody.Part body = MultipartBody.Part.createFormData("carImage", f.getName(), reqFile);
        compositeDisposable.add(cameraService.uploadFoodImage(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((responseBodyResponse) -> {
                    getResponseBodySingleLiveEvent().setValue(responseBodyResponse);
                    getLoading().setValue(false);
                }, throwable -> {
                    getErrorEvent().setValue(throwable);
                    loading.postValue(false);
                }));
    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                byte[] byteArray = data.getByteArrayExtra("image");
                f = (File) data.getSerializableExtra("file");
                Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                cameraItem.setValue(image);
            } else {
                cameraItem.setValue(null);
            }
        }
    }


    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }
}
