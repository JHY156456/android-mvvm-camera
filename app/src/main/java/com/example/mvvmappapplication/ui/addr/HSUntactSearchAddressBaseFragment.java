package com.example.mvvmappapplication.ui.addr;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.ViewDataBinding;

import com.example.mvvmappapplication.ui.BaseDataBindingFragment;
import com.example.mvvmappapplication.ui.BaseViewModel;
import com.example.mvvmappapplication.utils.UIUtil;


public abstract class HSUntactSearchAddressBaseFragment<T extends ViewDataBinding, VM extends BaseViewModel> extends BaseDataBindingFragment<T, VM> {

    public HSUntactSearchAddressActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (HSUntactSearchAddressActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    public void onBackPressed() {
        if (UIUtil.checkActivityRunningState(getActivity())) {
            //플래그먼트에서 백버튼 클릭시 타이틀에 있는 back버튼과 동일한 동작을 하도록 처리
            mActivity.onParentButtonClickTitleBack();
        }
    }

    public boolean onClickNextStep() {
        return true;
    }

    public boolean onClickTitleBackBtn() {
        return true;
    }

    public boolean onClickPrevStep() {
        return true;
    }
}