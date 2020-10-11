package com.example.mvvmappapplication.ui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.SparseIntArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.mvvmappapplication.utils.SingleLiveEvent;

import okhttp3.ResponseBody;

/**
 * [공통] BaseViewModel
 *
 * @param <V>
 * @author ehjung
 */
public class BaseViewModel<V extends BaseNavigator> extends AndroidViewModel {

    public SparseIntArray mDataLoadState = new SparseIntArray();

    private V mNavigator;
    @NonNull
    private final SingleLiveEvent<Throwable> errorEvent;
    @NonNull
    private final SingleLiveEvent<ResponseBody> responseBodySingleLiveEvent;

    public BaseViewModel(Application context, SingleLiveEvent<Throwable> errorEvent,
                          SingleLiveEvent<ResponseBody> responseBodySingleLiveEvent) {
        super(context);
        this.errorEvent = errorEvent;
        this.responseBodySingleLiveEvent = responseBodySingleLiveEvent;
    }

    public BaseViewModel(Application context) {
        super(context);
        errorEvent= new SingleLiveEvent<>();
        responseBodySingleLiveEvent = new SingleLiveEvent<>();
    }

    @NonNull
    public SingleLiveEvent<Throwable> getErrorEvent() {
        return errorEvent;
    }
    @NonNull
    public SingleLiveEvent<ResponseBody> getResponseBodySingleEvent() {
        return responseBodySingleLiveEvent;
    }


    public Context getContext() {
        return mNavigator.getContext();
    }

    public Activity getActivity() {
        return mNavigator.getActivity();
    }

    public V getNavigator() {
        return mNavigator;
    }

    public void setNavigator(V navigator) {
        mNavigator = navigator;
    }

    public View.OnClickListener getOnClickListener() {
        return mNavigator.getOnClickListener();
    }

    /*******************************************************************************
     * {@link BaseStepDataBindingActivity} 상속시 사용
     *******************************************************************************/

    /**
     * 화면 전환
     *
     * @param sereen 화면 index
     */
    public void changeFragment(int sereen) {
        if (mNavigator instanceof BaseStepNavigator) {
            ((BaseStepNavigator) mNavigator).changeFragment(sereen);
        }
    }

    /**
     * 다음화면 이동
     */
    public void onNextStep() {
        if (mNavigator instanceof BaseStepNavigator) {
            ((BaseStepNavigator) mNavigator).onNextStep();
        }
    }

    /**
     * 이전화면 이동
     */
    public void onPreStep() {
        if (mNavigator instanceof BaseStepNavigator) {
            ((BaseStepNavigator) mNavigator).onPreStep();
        }
    }
}
