package com.example.mvvmappapplication.data;

import com.example.mvvmappapplication.data.entity.Post;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

/**
 * 원래는 Call<T> 형태의 비동기 결과객체를 반환받는데
 * adapter-rxjava2를 추가하였기 때문에 ReactiveX에서 제공하는
 * Observable의 한 종류변환하여 반환할 수 있습니다. 여기서는 Single로 받도록 함
 */
public interface PostService {
    @GET("/posts")
    Single<List<Post>> getPosts();
}
