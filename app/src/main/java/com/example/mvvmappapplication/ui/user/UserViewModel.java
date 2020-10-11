package com.example.mvvmappapplication.ui.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.mvvmappapplication.data.UserService;
import com.example.mvvmappapplication.data.entity.User;
import com.example.mvvmappapplication.dto.UserInfo;
import com.example.mvvmappapplication.ui.BaseNavigator;
import com.example.mvvmappapplication.ui.BaseViewModel;
import com.example.mvvmappapplication.utils.SingleLiveEvent;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import timber.log.Timber;

public class UserViewModel extends BaseViewModel<BaseNavigator> {
    @NonNull
    private final UserService userService;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<User> liveItem = new MutableLiveData<>();
    private final LiveData<String> name = Transformations.map(liveItem, input -> input.getName());
    private final LiveData<String> email = Transformations.map(liveItem, input -> input.getEmail());
    private final LiveData<String> homepage = Transformations.map(liveItem, input -> input.getWebsite());
    private final LiveData<String> phone = Transformations.map(liveItem, input -> input.getPhone());
    private final LiveData<String> location = Transformations.map(liveItem, input -> input.getAddress().toString());

    private ObservableField<String> carNumber = new ObservableField<>();
    private ObservableField<String> id = new ObservableField<>();
    private ObservableField<String> password= new ObservableField<>();
    private ObservableField<String> loginId = new ObservableField<>();
    private ObservableField<String> loginPassword= new ObservableField<>();

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    @Inject
    public UserViewModel(@NonNull Application application,
                         @NonNull UserService userService,
                         @Named("errorEvent") SingleLiveEvent<Throwable> errorEvent,
                         @Named("responseBodySingleLiveEvent") SingleLiveEvent<ResponseBody> responseBodySingleLiveEvent) {
        super(application,errorEvent,responseBodySingleLiveEvent);
        Timber.d("UserViewModel created");
        this.userService = userService;
    }


    /**
     * 메소드 참조 방식
     * @param userId
     */
    public void loadUser(long userId) {
        compositeDisposable.add(userService.getUser(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess((item) -> loading.postValue(false))
                .subscribe(liveItem::setValue, getErrorEvent()::setValue));
    }

    /**
     * 기본적인 람다방식
     */
    public void onRegisterCompletedClick() {
        performUserService(compositeDisposable,
                userService.register(
                        new UserInfo(
                                getId().get(),
                                getPassword().get())),loading);
    }



    public void login(String id, String pw) {
        performUserService(compositeDisposable,
                userService.login(new UserInfo(id,pw)),loading);
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



    public ObservableField<String> getCarNumber() {
        return carNumber;
    }

    public ObservableField<String> getId() {
        return id;
    }

    public ObservableField<String> getPassword() {
        return password;
    }
}
