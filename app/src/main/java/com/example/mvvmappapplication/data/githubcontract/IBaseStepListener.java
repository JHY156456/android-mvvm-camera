package com.example.mvvmappapplication.data.githubcontract;

import android.os.Bundle;

import androidx.annotation.StringRes;

/**
 * BaseStepActivity, BaseStepFragment callback
 *
 * @author ehjung
 * @since 2018. 9. 28.
 */
public interface IBaseStepListener {

    enum eTitleButtonType {
        BACK, CLOSE, BACK_INFO
    }

    void setTitle(@StringRes int titleId, eTitleButtonType backButtonType);

    void setTitle(String titleName, eTitleButtonType backButtonType);

    void setButton(String preName, String nextName);

    void onNextStep();

    void onPreStep();

    void onFinish(boolean isResultOk);

    void setEnabledNextBtn(boolean isEnabled);

    Bundle getBundle();

    int getTotalStepCount();
}
