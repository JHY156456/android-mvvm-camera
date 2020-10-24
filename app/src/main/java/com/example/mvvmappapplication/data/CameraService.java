package com.example.mvvmappapplication.data;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface CameraService {
    @Multipart
    @POST("/api/camera")
    Observable<ResponseBody> uploadFoodImage(@Part MultipartBody.Part file);
}
