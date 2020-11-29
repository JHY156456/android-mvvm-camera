package com.example.mvvmappapplication.ui.menu;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvmappapplication.data.CameraService;
import com.example.mvvmappapplication.dto.CarNumberDto;
import com.example.mvvmappapplication.ui.BaseNavigator;
import com.example.mvvmappapplication.ui.BaseViewModel;
import com.example.mvvmappapplication.utils.Event;
import com.example.mvvmappapplication.utils.SingleLiveEvent;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;

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

    // ********** 서버 차량번호 조회 이벤트**********
    private final SingleLiveEvent<Response<ResponseBody>> responseSingleLiveEvent = new SingleLiveEvent<>();

    public SingleLiveEvent<Response<ResponseBody>> getResponseSingleLiveEvent() {
        return responseSingleLiveEvent;
    }
    // ********** 서버 차량번호 조회 이벤트**********

    // ********** 테서렉트 인식 번호**********
    private final MutableLiveData<String> tesseractCarNumber = new MutableLiveData<>();

    public MutableLiveData<String> getTesseractCarNumber() {
        return tesseractCarNumber;
    }
    // ********** 테서렉트 인식 번호**********

    // ********** OnClick Event**********
    private MutableLiveData<Event<String>> clickEvent = new MutableLiveData<>();

    public MutableLiveData<Event<String>> getClickEvent() {
        return clickEvent;
    }
    // ********** OnClick Event**********



    private final CompositeDisposable
            compositeDisposable = new CompositeDisposable();
    private final SingleLiveEvent<View> buttonCameraClickEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> buttonClickImmediatelyCallEvent = new SingleLiveEvent<>();
    private final MutableLiveData<Bitmap> cameraItem = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    TessBaseAPI tessBaseAPI;

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
        AssetManager assetMgr = getApplication().getAssets();
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
    @NonNull
    public SingleLiveEvent<Response<ResponseBody>> getResponseBodySingleLiveEvent() {
        return responseBodySingleLiveEvent;
    }

    public SingleLiveEvent<View> getButtonCameraClickEvent() {
        return buttonCameraClickEvent;
    }

    public SingleLiveEvent<Boolean> getButtonClickImmediatelyCallEvent() {
        return buttonClickImmediatelyCallEvent;
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


    public void onClickImmediatelyCall(View view) {
        tessBaseAPI = new TessBaseAPI();
        String dir = getApplication().getFilesDir() + "/tesseract";
        if (checkLanguageFile(dir + "/tessdata"))
            tessBaseAPI.init(dir, "kor");

        String result = "";

        try {
            result = new AsyncTess().execute(getCameraItem().getValue()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
        result = result.replaceAll(match, " ");
        result = result.replaceAll(" ", "");
        Timber.e("matchResult : " + result);
        getPhoneNumberByCarNumber(result);
    }

    public void onClickChat(View view){
        getClickEvent().setValue(new Event<>("chat"));
    }

    public void getPhoneNumberByCarNumber(String carNumber){
        loading.postValue(true);
        compositeDisposable.add(
                cameraService.getPhoneNumberByCarNumber(new CarNumberDto(carNumber))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                responseBody -> {
                                    getResponseSingleLiveEvent().setValue(responseBody);
                                    loading.postValue(false);
                                },
                                throwable -> {
                                    getErrorEvent().setValue(throwable);
                                    loading.postValue(false);
                                }));
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
                getTesseractCarNumber().setValue(result);
                getButtonClickImmediatelyCallEvent().setValue(true);
            } else {
                getButtonClickImmediatelyCallEvent().setValue(false);
            }
        }
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
