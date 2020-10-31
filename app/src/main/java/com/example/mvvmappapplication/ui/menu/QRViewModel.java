package com.example.mvvmappapplication.ui.menu;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvmappapplication.data.CameraService;
import com.example.mvvmappapplication.ui.BaseNavigator;
import com.example.mvvmappapplication.ui.BaseViewModel;
import com.example.mvvmappapplication.utils.Event;
import com.example.mvvmappapplication.utils.SingleLiveEvent;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
import timber.log.Timber;

public class QRViewModel extends BaseViewModel<BaseNavigator> {

    @NonNull
    private final CameraService cameraService;
    private final CompositeDisposable
            compositeDisposable = new CompositeDisposable();
    private final SingleLiveEvent<View> buttonCameraClickEvent = new SingleLiveEvent<>();
    private final MutableLiveData<Bitmap> cameraItem = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Event<Boolean>> isSuccess = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> isFail = new MutableLiveData<>();
    private MutableLiveData<String> resultContents = new MutableLiveData<>();


    @Inject
    public QRViewModel(@NonNull Application application,
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

    public MutableLiveData<Event<Boolean>> getIsSuccess() {
        return isSuccess;
    }

    public MutableLiveData<Event<Boolean>> getIsFail() {
        return isFail;
    }

    public MutableLiveData<String> getResultContents() {
        return resultContents;
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

    File f;
    public void onClickCall(View view)  {
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", f.getName(), reqFile);
        compositeDisposable.add(cameraService.uploadFoodImage(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getResponseBodySingleEvent()::setValue, getErrorEvent()::setValue));
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                isFail.setValue(new Event<>(true));
            } else {
                resultContents.setValue(result.getContents());
            }
        }
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }
}
