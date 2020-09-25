package com.example.mvvmappapplication.data;

import com.example.mvvmappapplication.App;
import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.consts.Const;
import com.example.mvvmappapplication.utils.ResourceUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author 안승진(seungjin.an)
 * @since 2018. 4. 5.
 */

public class RetrofitApiServiceBuilder {
    private static HashMap<eAPICategory, Object> cacheHashMap = new HashMap<>();
    private eAPICategory apiCategory;

    public static RetrofitApiServiceBuilder create(eAPICategory apiCategory) {
        RetrofitApiServiceBuilder builder = new RetrofitApiServiceBuilder();
        builder.apiCategory = apiCategory;
        return builder;
    }

    public static void clearCache(){
        cacheHashMap.clear();
    }

    public Object build() {
        //cache.
        Object apiService = cacheHashMap.get(apiCategory);

        if(apiService == null){
            apiService = init(apiCategory);
            cacheHashMap.put(apiCategory,apiService);
        }
        return apiService;
    }

    private Object init(eAPICategory apiCategory) {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.connectTimeout(3, TimeUnit.SECONDS); //
        if (App.BUILD_MODE == Const.eBuildMode.DEV) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient.addInterceptor(interceptor);
        }

        switch (apiCategory) {
            case PROFILE:
                return setupProfile(okHttpClient);

        }
        throw new RuntimeException("invalid service api");
    }


    /**
     * 프로필 이미지 업로드
     * @param okHttpClient
     * @return
     */
    public ProfileApiService setupProfile(OkHttpClient.Builder okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ProfileApiService.BASE_URL)
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ProfileApiService.class);
    }


}
