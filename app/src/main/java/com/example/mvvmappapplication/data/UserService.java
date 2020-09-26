package com.example.mvvmappapplication.data;


import com.example.mvvmappapplication.data.entity.User;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserService {
    @GET("/users/{userId}")
    Single<User> getUser(@Path("userId") long userId);

    @GET("/users/login/{email}/{password}")
    Observable<Response> login(@Path("email") String email, @Path("password")  String password);
}
