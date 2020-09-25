package com.example.mvvmappapplication;


import android.app.Activity;
import android.content.Context;

import com.example.mvvmappapplication.consts.Const;
import com.example.mvvmappapplication.di.DaggerAppComponent;
import com.example.mvvmappapplication.dto.UserInfoDto;
import com.example.mvvmappapplication.utils.LogUtil;
import com.example.mvvmappapplication.utils.StringUtil;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import timber.log.Timber;

public class App extends DaggerApplication {
    // 테마 타입
    private static Const.eThemeType mThemeType = Const.eThemeType.WHITE;
    private static Context context;
    public static Const.eBuildMode BUILD_MODE = Const.eBuildMode.DEPLOY_DEV; // 배포 모드
    public static boolean PROTECT_SCREEN_CAPTURE = (BUILD_MODE == Const.eBuildMode.DEPLOY); // 캡쳐 방지 여부
    private static UserInfoDto userInfo = UserInfoDto.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //로깅용 Timber 설정
        Timber.plant(new Timber.DebugTree());
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


}
