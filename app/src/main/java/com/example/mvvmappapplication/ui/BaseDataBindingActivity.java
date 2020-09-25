package com.example.mvvmappapplication.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * [공통] BaseDataBindingActivity
 *
 * @param <T>
 * @param <VM>
 * @author ehjung
 */
public abstract class BaseDataBindingActivity<T extends ViewDataBinding, VM extends BaseViewModel> extends BaseActivity implements BaseNavigator {

    private T mViewBinding;
    private VM mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performDataBinding();

    }

    private void performDataBinding() {
        mViewBinding = DataBindingUtil.setContentView(this, getLayoutId());
        mViewModel = getViewModel();
//        mViewDataBinding.setVariable(BR.viewModel, mViewModel);
//        mViewDataBinding.executePendingBindings();
        if (mViewModel != null && mViewModel.getNavigator() == null) {
            mViewModel.setNavigator(this);
        }
        mViewBinding.setLifecycleOwner(this);
    }

    public abstract
    @LayoutRes
    int getLayoutId();

    public abstract VM newViewModel();

    public T getBinding() {
        return mViewBinding;
    }

    public VM getViewModel() {
        return (mViewModel == null) ? newViewModel() : mViewModel;
    }

    public View.OnClickListener getOnClickListener() {
        return this;
    }
}
