package com.example.mvvmappapplication.data;

import com.example.mvvmappapplication.data.entity.Post;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface CameraService {
    @GET("/posts")
    Single<List<Post>> getPosts();

    @Multipart
    @POST("/api/camera")
    Observable<ResponseBody> uploadFoodImage(@Part MultipartBody.Part file);
}
