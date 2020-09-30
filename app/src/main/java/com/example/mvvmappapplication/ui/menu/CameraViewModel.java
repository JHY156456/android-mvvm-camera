package com.example.mvvmappapplication.ui.menu;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvmappapplication.data.CameraService;
import com.example.mvvmappapplication.ui.BaseNavigator;
import com.example.mvvmappapplication.ui.BaseViewModel;
import com.example.mvvmappapplication.ui.post.PostItem;
import com.example.mvvmappapplication.utils.SingleLiveEvent;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import timber.log.Timber;

public class CameraViewModel extends BaseViewModel<BaseNavigator> {

    @NonNull
    private final CameraService cameraService;
    @NonNull
    private final SingleLiveEvent<Throwable> errorEvent;

    //RecyclerView에 표현할 아이템들을 LiveData로 관리
    private final MutableLiveData<List<PostItem>> livePosts = new MutableLiveData<>();
    private final CompositeDisposable
            compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(true);
    //게시글 아이템 클릭 이벤트를 관리
    private final SingleLiveEvent<PostItem> postClickEvent = new SingleLiveEvent<>();

    @Inject
    public CameraViewModel(@NonNull Application application,
                         CameraService cameraService,
                         @Named("errorEvent") SingleLiveEvent<Throwable> errorEvent) {
        super(application);
        Timber.d("CameraViewModule created");
        //오브젝트 그래프로 부터 생성자 주입
        this.cameraService = cameraService;
        this.errorEvent = errorEvent;
    }



    @NonNull
    public MutableLiveData<List<PostItem>> getLivePosts() {
        return livePosts;
    }

    /**
     * ViewModel은 생명주기를 알고 동작한다.
     * 뷰모델이 파괴될 때, RxJava의 Disposable과 같은
     * 리소스 등을 해제할 수 있다록 한다.
     */

    @Override
    protected void onCleared() {
        super.onCleared();
        Timber.d("onCleared");
        compositeDisposable.dispose();
    }


    //PostFragment로 postClickEvent 변수를 노출
    public SingleLiveEvent<PostItem> getPostClickEvent() {
        return postClickEvent;
    }
    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }


}
