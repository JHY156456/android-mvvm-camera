package com.example.mvvmappapplication.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.StringRes;

import com.example.mvvmappapplication.R;

import com.example.mvvmappapplication.custom.HSMaterialButton;
import com.example.mvvmappapplication.custom.HSTitleBar;
import com.example.mvvmappapplication.custom.HSTitleBar;
import com.example.mvvmappapplication.data.githubcontract.IBaseStepListener;
import com.example.mvvmappapplication.utils.ResourceUtil;


/**
 * BaseStepActivity
 *
 * @author ehjung
 * @since 2018-03-26
 */
public abstract class BaseStepActivity extends BaseActivity {

    private HSTitleBar titleBar;

    private View layout_floating_button;
    private HSMaterialButton btn_step_next;
    private HSMaterialButton btn_step_prev;
    private View btnDivider;

    protected BaseStepFragment mCurrentFragment;
    protected int mCurrentStepIndex;
    protected int mStartStepIndex;
    protected int mTotalStep;
    private IBaseStepListener.eTitleButtonType mBackButtonType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_step);
        initView();
    }

    private void initView() {
        titleBar = findViewById(R.id.titleBar);

        //하단 버튼 레이아웃
        layout_floating_button = findViewById(R.id.layout_floating_button);

        //이전(취소) 버튼.
        btn_step_prev = findViewById(R.id.btn_step_prev);
        btn_step_prev.setOnClickListener(this);

        btnDivider = findViewById(R.id.btnDivider);

        //다음 스텝 버튼.
        btn_step_next = findViewById(R.id.btn_step_next);
        btn_step_next.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (mCurrentFragment.onClickToBackBtn()) {
            onPreStep();
        }
    }

    public void setTitle(@StringRes int titleId, IBaseStepListener.eTitleButtonType backButtonType) {
        setTitle(ResourceUtil.getString(titleId), backButtonType);
    }

    public void setTitle(String titleName, IBaseStepListener.eTitleButtonType backButtonType) {
        mBackButtonType = backButtonType;
        /* title name */
        titleBar.setVisibility(TextUtils.isEmpty(titleName) ? View.GONE : View.VISIBLE);
        if (titleBar.getVisibility() == View.GONE) {
            return;
        }
        titleBar.setTitle(titleName);

        /* title back button */
        switch (mBackButtonType) {
            case BACK:
                titleBar.setBackIconType(HSTitleBar.eBackIconType.BACK);
                break;

            case CLOSE:
                titleBar.setBackIconType(HSTitleBar.eBackIconType.CLOSE);
                break;
        }
    }

    public void setButton(String preName, String nextName) {
        /* pre btn */
        btn_step_prev.setVisibility(TextUtils.isEmpty(preName) ? View.GONE : View.VISIBLE);
        if (btn_step_prev.getVisibility() == View.VISIBLE) {
            btn_step_prev.setText(preName);
            btn_step_prev.setEnabled(true);
        }
        btnDivider.setVisibility(btn_step_prev.getVisibility());
        /* next btn */
        btn_step_next.setVisibility(TextUtils.isEmpty(nextName) ? View.GONE : View.VISIBLE);
        if (btn_step_next.getVisibility() == View.VISIBLE) {
            btn_step_next.setText(nextName);
            btn_step_next.setEnabled(true);
        }
        if (btn_step_prev.getVisibility() == View.GONE && btn_step_next.getVisibility() == View.GONE) {
            layout_floating_button.setVisibility(View.GONE);
        } else {
            layout_floating_button.setVisibility(View.VISIBLE);
        }
    }

    public void setEnabledNextBtn(boolean isEnabled) {
        btn_step_next.setEnabled(isEnabled);
    }

    public int getTotalStepCount() {
        return mTotalStep;
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

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_step_prev:
                if (mCurrentFragment.onClickToPreStep()) {
                    onPreStep();
                }
                break;

            case R.id.btn_step_next:
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

    protected abstract void changeFragment(int nextStep);

}
