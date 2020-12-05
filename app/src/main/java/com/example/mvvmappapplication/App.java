package com.example.mvvmappapplication;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.mvvmappapplication.consts.Const;
import com.example.mvvmappapplication.data.githubmodel.GitHubService;
import com.example.mvvmappapplication.di.DaggerAppComponent;
import com.example.mvvmappapplication.dto.UserInfo;
import com.example.mvvmappapplication.dto.UserInfoDto;
import com.example.mvvmappapplication.utils.LogUtil;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import kotlin.jvm.JvmField;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class App extends DaggerApplication {
    // 테마 타입
    private static Const.eThemeType mThemeType = Const.eThemeType.WHITE;
    private static Context context;
    public static Const.eBuildMode BUILD_MODE = Const.eBuildMode.DEPLOY_DEV; // 배포 모드
    public static boolean PROTECT_SCREEN_CAPTURE = (BUILD_MODE == Const.eBuildMode.DEPLOY); // 캡쳐 방지 여부
    private static UserInfoDto userInfo = UserInfoDto.getInstance();
    private static UserInfo user;
    public static String carNumber = "";
    public static String phoneNumber = "";

    @JvmField
    public static Context instance;
    //public static String API_HOST = "http://192.168.35.192";
    public static String API_HOST = "http://10.0.2.2";
    public static int API_PORT = 8080;
    //public static String WEBSOCKET_ENDPOINT = "ws://192.168.35.192:8080/ws/randomchat";
    public static String WEBSOCKET_ENDPOINT = "ws://10.0.2.2:8080/ws/randomchat";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        instance = this;
        //로깅용 Timber 설정
        Timber.plant(new Timber.DebugTree());
        setupAPIClient();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        //AppComponent 설정이 끝난 뒤,
        //컴파일 타임에 DaggerAppComponent가 생성된다.
        return DaggerAppComponent.factory().create(this);
    }

    /**
     * 현재 테마 변경
     *
     * @param type
     */
    public static void setThemeType(Const.eThemeType type) {
        mThemeType = type;
    }

    /**
     * 현재 테마 반환
     *
     * @return
     */
    public static Const.eThemeType getThemeType() {
        return mThemeType;
    }
    public static Context getContext() {
        return context;
    }
    static Activity currentActivity;
    public static void setCurrentActivity(Activity currentActivity) {
        if (App.getCurrentActivity() != null && currentActivity.hashCode() == App.getCurrentActivity().hashCode()) {
            return;
        }
        App.currentActivity = currentActivity;
        LogUtil.i("[#LIFECYCLE#] " + currentActivity.getLocalClassName());
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static UserInfoDto userInfoData() {
        return App.userInfo;
    }
    public static void setUserInfoData(UserInfoDto data) {
        App.userInfo = data;
    }

    public static void setUserInfo(UserInfo data) {
        user = data;
    }
    public static UserInfo getUserInfo() {
        if(user == null){
            return new UserInfo("id null","","carNumber","phone");
        } else{
            return user;
        }
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

    private Retrofit retrofit;
    private GitHubService gitHubService;
}
