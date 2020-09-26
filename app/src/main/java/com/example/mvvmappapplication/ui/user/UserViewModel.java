package com.example.mvvmappapplication.ui.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.mvvmappapplication.data.UserService;
import com.example.mvvmappapplication.data.entity.User;
import com.example.mvvmappapplication.ui.BaseNavigator;
import com.example.mvvmappapplication.ui.BaseViewModel;
import com.example.mvvmappapplication.util.SingleLiveEvent;
import com.example.mvvmappapplication.utils.AlertUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Response;
import timber.log.Timber;

public class UserViewModel extends BaseViewModel<BaseNavigator> {
    @NonNull
    private final UserService userService;
    @NonNull
    private final SingleLiveEvent<Throwable> errorEvent;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<User> liveItem = new MutableLiveData<>();
    private final LiveData<String> name = Transformations.map(liveItem, input -> input.getName());
    private final LiveData<String> email = Transformations.map(liveItem, input -> input.getEmail());
    private final LiveData<String> homepage = Transformations.map(liveItem, input -> input.getWebsite());
    private final LiveData<String> phone = Transformations.map(liveItem, input -> input.getPhone());
    private final LiveData<String> location = Transformations.map(liveItem, input -> input.getAddress().toString());


    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(true);
    private final MutableLiveData<Response> response = new MutableLiveData<>();
    @Inject
    public UserViewModel(@NonNull Application application,
                         @NonNull UserService userService,
                         @Named("errorEvent") SingleLiveEvent<Throwable> errorEvent) {
        super(application);
        Timber.d("UserViewModel created");
        this.userService = userService;
        this.errorEvent = errorEvent;

    }

    public void loadUser(long userId) {
        compositeDisposable.add(userService.getUser(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess((item) -> loading.postValue(false))
                .subscribe(liveItem::setValue, errorEvent::setValue));
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }
    public MutableLiveData<Response> getResponse() {
        return response;
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

    public void login(String email, String password){
        compositeDisposable.add(userService.login(email,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(Response response) {
        setResponse(response);
    }

    private void handleError(Throwable error) {
        if (error instanceof HttpException) {
            Gson gson = new GsonBuilder().create();
            try {
                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody, Response.class);
                setResponse(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            AlertUtil.toast(getActivity(), error.getMessage());
        }
    }

    private void setResponse(Response response) {
        this.response.setValue(response);
    }
}
