package com.example.mvvmappapplication.ui.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.mvvmappapplication.App;
import com.example.mvvmappapplication.data.UserServerService;
import com.example.mvvmappapplication.data.UserService;
import com.example.mvvmappapplication.data.entity.User;
import com.example.mvvmappapplication.dto.UserInfo;
import com.example.mvvmappapplication.dto.UserInfoBuilder;
import com.example.mvvmappapplication.ui.BaseNavigator;
import com.example.mvvmappapplication.ui.BaseViewModel;
import com.example.mvvmappapplication.utils.SingleLiveEvent;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import timber.log.Timber;

public class UserViewModel extends BaseViewModel<BaseNavigator> {
    @NonNull
    private final UserService userService;
    @NonNull
    private final UserServerService userServerService;
    @NonNull
    private final SingleLiveEvent<Response<ResponseBody>> responseBodySingleLiveEvent;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<User> liveItem = new MutableLiveData<>();

    //******** UserProfile *********
    private final LiveData<String> name = Transformations.map(liveItem, input -> input.getName());
    private final LiveData<String> email = Transformations.map(liveItem, input -> input.getEmail());
    private final LiveData<String> homepage = Transformations.map(liveItem, input -> input.getWebsite());
    private final LiveData<String> phone = Transformations.map(liveItem, input -> input.getPhone());
    private final LiveData<String> location = Transformations.map(liveItem, input -> input.getAddress().toString());
    //******** UserProfile *********


    //******** MyProfile *********
    private final MutableLiveData<String> profileId = new MutableLiveData<>();
    private final MutableLiveData<String> profileCarNumber = new MutableLiveData<>();
    private final MutableLiveData<String> profilePhone = new MutableLiveData<>();

    public MutableLiveData<String> getProfileId() {
        return profileId;
    }

    public MutableLiveData<String> getProfileCarNumber() {
        return profileCarNumber;
    }

    public MutableLiveData<String> getProfilePhone() {
        return profilePhone;
    }
    //******** MyProfile *********


    private ObservableField<String> carNumber = new ObservableField<>();
    private ObservableField<String> id = new ObservableField<>();
    private ObservableField<String> password = new ObservableField<>();
    private ObservableField<String> loginId = new ObservableField<>();
    private ObservableField<String> loginPassword = new ObservableField<>();

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    @Inject
    public UserViewModel(@NonNull Application application,
                         @NonNull UserService userService,
                         @NonNull UserServerService userServerService,
                         @Named("errorEvent") SingleLiveEvent<Throwable> errorEvent,
                         @Named("responseBodySingleLiveEvent") SingleLiveEvent<Response<ResponseBody>> responseBodySingleLiveEvent) {
        super(application, errorEvent);
        Timber.d("UserViewModel created");
        this.userService = userService;
        this.userServerService = userServerService;
        this.responseBodySingleLiveEvent = responseBodySingleLiveEvent;
        profileCarNumber.setValue("");
        profilePhone.setValue("");
    }

    @NonNull
    public SingleLiveEvent<Response<ResponseBody>> getResponseBodySingleLiveEvent() {
        return responseBodySingleLiveEvent;
    }

    /**
     * 메소드 참조 방식
     *
     * @param userId
     */
    public void loadUser(long userId) {
        loading.setValue(true);
        compositeDisposable.add(userService.getUser(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess((item) -> loading.postValue(false))
                .subscribe(user -> {
                    liveItem.setValue(user);
                    loading.setValue(false);
                }, getErrorEvent()::setValue));
    }

    public void loadMyProfile() {
        UserInfo myProfile = App.getUserInfo();
        profileId.setValue(myProfile.getId());
        profileCarNumber.setValue(myProfile.getCarNumber());
        profilePhone.setValue(myProfile.getPhone());
    }

    public void onRegisterCompletedClick() {
        loading.setValue(true);
        App.carNumber = profileCarNumber.getValue().trim();
        App.phoneNumber = profilePhone.getValue().trim();
        compositeDisposable.add(
                userServerService.register(
                        new UserInfoBuilder().setId(getId().get())
                                .setPassword(getPassword().get())
                                .build())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    getResponseBodySingleLiveEvent().setValue(response);
                                },
                                throwable -> {
                                    getErrorEvent().setValue(throwable);
                                    loading.postValue(false);
                                },
                                () -> loading.postValue(false)));
    }


    //Todo : onComplete에 ()->loading.setValue(false) 하니까 바로 실행되면서 로그인 로딩화면이 꺼지는것같은데 이유를 찾아보자
    public void login(String id, String pw) {
        loading.setValue(true);
        UserInfo userInfo = new UserInfoBuilder().setId(id)
                .setPassword(pw)
                .setCarNumber(App.carNumber)
                .setPhone(App.phoneNumber)
                .build();
        App.setUserInfo(userInfo);
        compositeDisposable.add(
                userServerService.login(userInfo)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                responseBody -> {
                                    getResponseBodySingleLiveEvent().setValue(responseBody);
                                },
                                throwable -> {
                                    getErrorEvent().setValue(throwable);
                                    loading.postValue(false);
                                }));
    }

    public ObservableField<String> getLoginId() {
        return loginId;
    }

    public ObservableField<String> getLoginPassword() {
        return loginPassword;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getName() {
        return name;
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public LiveData<String> getHomepage() {
        return homepage;
    }

    public LiveData<String> getPhone() {
        return phone;
    }

    public LiveData<String> getLocation() {
        return location;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    public ObservableField<String> getId() {
        return id;
    }

    public ObservableField<String> getPassword() {
        return password;
    }
}
