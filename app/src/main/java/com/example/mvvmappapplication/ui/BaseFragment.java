package com.example.mvvmappapplication.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mvvmappapplication.App;
import com.example.mvvmappapplication.consts.MainEvent;
import com.example.mvvmappapplication.utils.LogUtil;
import com.example.mvvmappapplication.utils.ThemeUtil;
import com.example.mvvmappapplication.utils.UIUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


/**
 * 뷰에서 기본적으로 상속받아야 하는 View class로 기본 UI, 버튼 및 Progressbar등을 관리함
 *
 * @author josung
 * @version 1.0
 * @since 2016-09-05
 */
public class BaseFragment extends Fragment {

    private BaseActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * Activity 활성화 여부 반환 (최상위 Activity 여부)
     *
     * @return
     */
    public boolean isActivityActived() {
        if (mActivity == null) {
            return false;
        }
        return mActivity.isActived();
    }

    /**
     * 화면 UI/데이터 갱신 처리를 하는 공통 Method
     */
    @Deprecated
    public void refreshScreen() {
        //App.isBeforeCalledRefresh = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /********************************************************************************************************
     * ProgressDialog
     *********************************************************************************************************/

    public void showProgress() {
        showProgress(null, 0, true);
    }

    /**
     * 프로그래스바 출력.
     *
     * @param msg
     * @param duration
     * @param cancelable
     */
    public void showProgress(final String msg, final int duration, final boolean cancelable) {
        if (UIUtil.checkActivityRunningState(mActivity)) {
            mActivity.showProgress(msg, duration, cancelable);
        }
    }

    /**
     * 프로그래스바 감추기.
     */
    public void hideProgress() {
        if (UIUtil.checkActivityRunningState(mActivity)) {
            mActivity.hideProgress();
        }
    }

    /********************************************************************************************************
     * softKeyboard
     *********************************************************************************************************/

    /**
     * 키패드 보이기.
     */
    public void showSoftKeyboard() {
        if (UIUtil.checkActivityRunningState(mActivity)) {
            mActivity.showSoftKeyboard();
        }
    }

    /**
     * 키패드 보이기.
     */
    public void showSoftKeyboardDelayed() {
        if (UIUtil.checkActivityRunningState(mActivity)) {
            mActivity.showSoftKeyboardDelayed();
        }
    }

    /**
     * 키패드 감추기
     */
    public void hideSoftKeyboard() {
        if (UIUtil.checkActivityRunningState(mActivity)) {
            mActivity.hideSoftKeyboard();
        }
    }

    /**
     * 키패드 감추기
     */
    public void hideSoftKeyboardDelayed() {
        if (UIUtil.checkActivityRunningState(mActivity)) {
            mActivity.hideSoftKeyboardDelayed();
        }
    }

    /********************************************************************************************************
     * Event Bus
     *********************************************************************************************************/
    @Subscribe
    public void onEvent(final MainEvent event) {
        switch (event.mEvent) {
            case MainEvent.EVENT_CHANGE_THEME_ACTION: /* 테마변경 */
                onChangeTheme();
                break;
        }
    }

    /********************************************************************************************************
     * Theme
     *********************************************************************************************************/

    protected void onChangeTheme() {
        View rootView = getView();
//        ThemeUtil.updateTheme(rootView, R.attr.windowBackground);
        ThemeUtil.updateTheme((ViewGroup) rootView);
    }

    /********************************************************************************************************
     * Child Fragment Override
     *********************************************************************************************************/

    public void onBackPressed() {

    }

    public boolean onClickNextStep() {
        return true;
    }

    public boolean onClickTitleBackBtn() {
        return true;
    }

    public void selectFragment(){}

}
