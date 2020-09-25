package com.example.mvvmappapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.StringRes;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.custom.HSTitleBar;
import com.example.mvvmappapplication.data.githubcontract.IBaseStepListener;
import com.example.mvvmappapplication.databinding.ActivityBaseStepDatabindingBinding;
import com.example.mvvmappapplication.utils.ResourceUtil;

/**
 * Step UI Base Activity
 *
 * @author ehjung
 * @since 2018-09-05
 */
public abstract class BaseStepDataBindingActivity<VM extends BaseViewModel>
        extends BaseDataBindingActivity<ActivityBaseStepDatabindingBinding, VM>
        implements IBaseStepListener, BaseStepNavigator {

    protected BaseStepFragment mCurrentFragment;
    protected int mCurrentStepIndex;
    protected int mStartStepIndex;
    protected int mTotalStepCount;
    private eTitleButtonType mBackButtonType = eTitleButtonType.BACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getViewModel() != null) {
            getViewModel().setNavigator(this);
        }
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_base_step_databinding;
    }

    private void initView() {
        getBinding().infoBtn.setOnClickListener(this);
        getBinding().stepPreBtn.setOnClickListener(this);
        getBinding().stepNextBtn.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (mCurrentFragment.onClickToBackBtn()) {
            onPreStep();
        }
    }

    public void setVisibilityToRightBtn(int visibility) {
        getBinding().infoBtn.setVisibility(visibility);
    }

    public void setTitle(@StringRes int titleNameResId, eTitleButtonType backButtonType) {
        setTitle(ResourceUtil.getString(titleNameResId), backButtonType);
    }

    public void setTitle(String titleName, eTitleButtonType backButtonType) {
        mBackButtonType = backButtonType;
        /* title name */
        getBinding().titleBar.setVisibility(TextUtils.isEmpty(titleName) ? View.GONE : View.VISIBLE);
        if (getBinding().titleBar.getVisibility() == View.GONE) {
            return;
        }
        getBinding().titleBar.setTitle(titleName);

        /* title back button */
        switch (mBackButtonType) {
            case BACK:
                getBinding().infoBtn.setVisibility(View.GONE);
                getBinding().titleBar.setBackIconType(HSTitleBar.eBackIconType.BACK);
                break;

            case CLOSE:
                getBinding().infoBtn.setVisibility(View.GONE);
                getBinding().titleBar.setBackIconType(HSTitleBar.eBackIconType.CLOSE);
                break;

            case BACK_INFO:
                getBinding().infoBtn.setVisibility(View.VISIBLE);
                getBinding().titleBar.setBackIconType(HSTitleBar.eBackIconType.BACK);
                break;
        }
    }

    public void setButton(String preName, String nextName) {
        /* pre btn */
        getBinding().stepPreBtn.setVisibility(TextUtils.isEmpty(preName) ? View.GONE : View.VISIBLE);
        if (getBinding().stepPreBtn.getVisibility() == View.VISIBLE) {
            getBinding().stepPreBtn.setText(preName);
            getBinding().stepPreBtn.setEnabled(true);
        }
        //todo Pine에서 이전버튼 전부 삭제. 오픈후 UI부터 단계적 삭제 예정.
        getBinding().stepPreBtn.setVisibility(View.GONE);

        /* next btn */
        getBinding().stepNextBtn.setVisibility(TextUtils.isEmpty(nextName) ? View.GONE : View.VISIBLE);
        if (getBinding().stepNextBtn.getVisibility() == View.VISIBLE) {
            getBinding().stepNextBtn.setText(nextName);
            getBinding().stepNextBtn.setEnabled(true);
        }
        if (getBinding().stepPreBtn.getVisibility() == View.GONE && getBinding().stepNextBtn.getVisibility() == View.GONE) {
            getBinding().preNextLayout.setVisibility(View.GONE);
        } else {
            getBinding().preNextLayout.setVisibility(View.VISIBLE);
        }
    }

    public void setEnabledNextBtn(boolean isEnabled) {
        getBinding().stepNextBtn.setEnabled(isEnabled);
    }

    public int getTotalStepCount() {
        return mTotalStepCount;
    }

    public void onPreStep() {
        int preStep = mCurrentStepIndex - 1;
        if (preStep < mStartStepIndex) {
            onFinish(false);
        } else {
            changeFragment(preStep);
        }
    }

    public void onNextStep() {
        int nextStep = mCurrentStepIndex + 1;
        changeFragment(nextStep);
    }

    protected void onForceNextStep() {
        int nextStep = mCurrentStepIndex + 1;
        changeFragment(nextStep);
    }

    public void onFinish(boolean isResultOk) {
        if (isResultOk) {
            setResult(RESULT_OK);
        }
        finish();
    }

    public void onFinish(boolean isResultOk, Intent intent) {
        if (isResultOk) {
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
//            case R.id.backBtn:
//                onBackPressed();
//                break;

            case R.id.closeBtn:
                onFinish(false);
                break;

            case R.id.stepPreBtn:
                if (mCurrentFragment.onClickToPreStep()) {
                    onPreStep();
                }
                break;

            case R.id.stepNextBtn:
                if (mCurrentFragment.onClickToNextStep()) {
                    onNextStep();
                }
                break;
        }
    }

    private Bundle mBundle;

    public Bundle getBundle() {
        if (mBundle == null) {
            mBundle = new Bundle();
        }
        return mBundle;
    }

    public abstract void changeFragment(int fragmentIndex);

}
