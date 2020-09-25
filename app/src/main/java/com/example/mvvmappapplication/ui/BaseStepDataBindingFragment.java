package com.example.mvvmappapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * BaseStepFragment
 *
 * @author ehjung
 * @version 1.0
 * @since 2018-03-26
 */
public abstract class BaseStepDataBindingFragment<T extends ViewDataBinding> extends BaseStepFragment {

    private T mViewBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init(savedInstanceState);
    }

    protected abstract
    @LayoutRes
    int getLayoutId();

    protected abstract void init(Bundle savedInstanceState);

    public T getBinding() {
        return mViewBinding;
    }

    @Override
    public void onClick(View v) {

    }
}
