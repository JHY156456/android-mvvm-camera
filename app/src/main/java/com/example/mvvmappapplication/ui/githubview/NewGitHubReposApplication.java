package com.example.mvvmappapplication.ui.githubview;

import android.app.Application;
import android.util.Log;


import com.example.mvvmappapplication.data.githubmodel.GitHubService;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewGitHubReposApplication extends Application {
  private Retrofit retrofit;
  private GitHubService gitHubService;

  @Override
  public void onCreate() {
    super.onCreate();
    // 어느 Activity에서라도 API를 이용할 수 있게 이 클래스에서 API를 이용한다
    setupAPIClient();
  }

  private void setupAPIClient() {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
      @Override
      public void log(String message) {
        Log.d("API LOG", message);
      }
    });

    logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();

    retrofit = new Retrofit.Builder()
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .baseUrl("https://api.github.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build();

    gitHubService = retrofit.create(GitHubService.class);
  }

  public GitHubService getGitHubService() {
    return gitHubService;
  }
}
