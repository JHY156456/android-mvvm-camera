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
 * [공통] BaseDataBindingFragment
 *
 * @author ehjung
 */
public abstract class BaseDataBindingFragment<T extends ViewDataBinding, VM extends BaseViewModel> extends BaseFragment implements BaseNavigator {

    private T mViewBinding;
    private VM mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = getViewModel();
        if (mViewModel != null && mViewModel.getNavigator() == null) {
            mViewModel.setNavigator(this);
        }
        init(savedInstanceState);
    }

    protected abstract
    @LayoutRes
    int getLayoutId();

    public abstract VM newViewModel();

    protected abstract void init(Bundle savedInstanceState);

    public T getBinding() {
        return mViewBinding;
    }

    public VM getViewModel() {
        return (mViewModel == null) ? newViewModel() : mViewModel;
    }

    public View.OnClickListener getOnClickListener() {
        return (View.OnClickListener) this;
    }
}
