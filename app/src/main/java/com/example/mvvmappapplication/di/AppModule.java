package com.example.mvvmappapplication.di;

import android.app.Application;
import android.content.Context;

import com.example.mvvmappapplication.App;
import com.example.mvvmappapplication.data.githubcontract.RepositoryListViewContract;
import com.example.mvvmappapplication.data.githubmodel.GitHubService;
import com.example.mvvmappapplication.utils.SingleLiveEvent;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.ResponseBody;

/**
 * incldues : ViewModel을 애플리케이션 범위로 관리하도록 AppModule에 포함시킨다.
 * @Module(
 *         includes = MotorModule.class
 * )
 * public class BikeModule {
 *
 *     @Provides
 *     Bike provideBike(Motor motor) {
 *         return new Bike(motor);
 *     }
 * }
 * 여기서 보면 Bike객체를 제공해주는 @Provides을 보면 Motor객체가 파라미터로 존재합니다.
 * 과연 이 파라미터는 어디서 제공받고 있을까 라는 의문이 듭니다.
 * 이건 @Module의 includes부분에 보면 MotorModule.class를 포함하고 있고, MotorModule에서 Motor객체를 provides하고 있기에 가능합니다.
 */
@Module(includes = {
        ViewModelModule.class,
        RetrofitModule.class
})
public class AppModule {

    @Provides
    @Singleton
    Application provideApp(App app) {
        return app;
    }

    @Provides
    @Singleton
    @ApplicationContext
    Context provideContext(App app) {
        return app;
    }

    //앱의 오류 이벤트를 처리하는 SingleLiveEvent

    /**
     * ViewModelModule을 애플리케이션 범위로 관리하도록 포함되어있기때문에
     * 액티비티에서 직접 주입받아도되고 뷰모델어서 주입받아 사용해도 된다.
     * @return
     */
    @Singleton
    @Provides
    @Named("errorEvent")
    SingleLiveEvent<Throwable> provideErrorEvent(){
        return new SingleLiveEvent<>();
    }

    //네트워크 연결 성공 이벤트를 처리하는 SingleLiveEvent
    @Singleton
    @Provides
    @Named("responseBodySingleLiveEvent")
    SingleLiveEvent<retrofit2.Response<ResponseBody>> provideResponseBodySingleLiveEvent(){
        return new SingleLiveEvent<>();
    }

    @Singleton
    @Provides
    @Named("githubService")
    GitHubService provideGithubService(App app){
        return app.getGitHubService();
    }
}