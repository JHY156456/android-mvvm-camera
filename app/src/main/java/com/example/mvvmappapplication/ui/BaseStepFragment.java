package com.example.mvvmappapplication.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.example.mvvmappapplication.data.githubcontract.IBaseStepListener;


/**
 * BaseStepFragment
 *
 * @author ehjung
 * @version 1.0
 * @since 2018-03-26
 */
public abstract class BaseStepFragment extends BaseFragment implements View.OnClickListener {

    private IBaseStepListener mIActivityListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mIActivityListener = (IBaseStepListener) getActivity();
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {

    }

    public void onFinish(boolean isResultOk) {
        if (mIActivityListener != null) {
            mIActivityListener.onFinish(isResultOk);
        }
    }


    public void onPreStep() {
        if (mIActivityListener != null) {
            mIActivityListener.onPreStep();
        }
    }

    public void onNextStep() {
        if (mIActivityListener != null) {
            mIActivityListener.onNextStep();
        }
    }

    public void setTitle(@StringRes int titleId, IBaseStepListener.eTitleButtonType backButtonType) {
        if (mIActivityListener != null) {
            mIActivityListener.setTitle(titleId, backButtonType);
        }
    }

    public void setTitle(String titleName, IBaseStepListener.eTitleButtonType backButtonType) {
        if (mIActivityListener != null) {
            mIActivityListener.setTitle(titleName, backButtonType);
        }
    }

    public void setButton(String preName, String nextName) {
        if (mIActivityListener != null) {
            mIActivityListener.setButton(preName, nextName);
        }
    }

    public void setEnabledNextBtn(boolean isEnabled) {
        if (mIActivityListener != null) {
            mIActivityListener.setEnabledNextBtn(isEnabled);
        }
    }

    public Bundle getBundle() {
        if (mIActivityListener != null) {
            return mIActivityListener.getBundle();
        }
        return new Bundle();
    }

    public int getTotalStepCount() {
        if (mIActivityListener != null) {
            mIActivityListener.getTotalStepCount();
        }
        return 0;
    }

    public boolean onClickToBackBtn() {
        return true;
    }

    public boolean onClickToPreStep() {
        return true;
    }

    public boolean onClickToNextStep() {
        return true;
    }

}
