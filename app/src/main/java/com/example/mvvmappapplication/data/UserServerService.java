package com.example.mvvmappapplication.data;


import com.example.mvvmappapplication.dto.UserInfo;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserServerService {
    @POST("api/auth/register")
    Observable<ResponseBody> register(@Body UserInfo user);

    //여러건 통지는 Observable 객체 사용을 고려하자
    @POST("api/auth/login")
    Observable<ResponseBody> login(@Body UserInfo user);
}
