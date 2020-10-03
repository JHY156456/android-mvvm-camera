package com.example.mvvmappapplication;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.mvvmappapplication.utils.SingleLiveEvent;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import timber.log.Timber;

public class HomeViewModel extends AndroidViewModel {
    @NonNull
    private final SingleLiveEvent<Throwable> errorEvent;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final SingleLiveEvent<View> menuClickEvent = new SingleLiveEvent<>();

    @Inject
    public HomeViewModel(@NonNull Application application,
                               @Named("errorEvent") SingleLiveEvent<Throwable> errorEvent) {
        super(application);
        this.errorEvent = errorEvent;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Timber.d("onCleared");
        this.compositeDisposable.dispose();
    }
    public SingleLiveEvent<View> getBottomMenuClickEvent() {
        return menuClickEvent;
    }

    public void onMenuClick(View view){
        menuClickEvent.setValue(view);
    }


}
