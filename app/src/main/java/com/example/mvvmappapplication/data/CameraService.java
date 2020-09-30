package com.example.mvvmappapplication.data;

import com.example.mvvmappapplication.data.entity.Post;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface CameraService {
    @GET("/posts")
    Single<List<Post>> getPosts();
}
