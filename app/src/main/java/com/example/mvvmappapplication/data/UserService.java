package com.example.mvvmappapplication.data;


import com.example.mvvmappapplication.data.entity.User;
import com.example.mvvmappapplication.dto.UserInfo;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {
    @GET("/users/{userId}")
    Single<User> getUser(@Path("userId") long userId);

    @POST("api/auth/register")
    Observable<ResponseBody> register(@Body UserInfo user);

    @POST("api/auth/login")
    Observable<ResponseBody> login(@Body UserInfo user);
}
