package com.example.mvvmappapplication.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.mvvmappapplication.App;
import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.consts.Const;
import com.example.mvvmappapplication.consts.Const.eThemeType;
import com.example.mvvmappapplication.consts.MainEvent;
import com.example.mvvmappapplication.custom.HSTitleBar;
import com.example.mvvmappapplication.dialog.ProgressDialogBase;
import com.example.mvvmappapplication.ui.gallery.GalleryActivity;
import com.example.mvvmappapplication.ui.slideshow.SlideshowActivity;
import com.example.mvvmappapplication.utils.AlertUtil;
import com.example.mvvmappapplication.utils.IntentUtil;
import com.example.mvvmappapplication.utils.LogUtil;
import com.example.mvvmappapplication.utils.PermissionUtil;
import com.example.mvvmappapplication.utils.ThemeUtil;
import com.example.mvvmappapplication.utils.UIUtil;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import dagger.android.support.DaggerAppCompatActivity;


/**
 * 뷰에서 기본적으로 상속받아야 하는 View class로 기본 UI, 버튼 및 Progressbar등을 관리함.
 *
 * @author kck
 * @version 1.0
 * @since 2016-07-26
 */
public abstract class BaseActivity extends DaggerAppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private ProgressDialogBase mProgressDialog;

    Const.eThemeType mThemeType = Const.eThemeType.WHITE;

    private boolean isActived = false;

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    NavigationView navigationView;
    AppBarLayout appBarLayout;
    AppBarConfiguration appBarConfiguration;
    ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (App.getThemeType() == Const.eThemeType.DARK) {
//            mThemeType = eThemeType.DARK;
//            setTheme(mThemeType);
//        }
        //ThemeUtil.setStatusBarColor(getActivity());
        super.onCreate(savedInstanceState);
//        if(savedInstanceState!=null)
//            onRestoreInstance(savedInstanceState);
//        App.setCurrentActivity(this);
    }

    public void setToolbarTitle(String title){
        collapsingToolbarLayout.setTitle(title);
    }
    /*
    TODO : collapsingToolbarLayout의 app:titleEnabled="false" 속성일때 collapsing.setTitle이 안되는것같다.
            아래 setDisplayShowTitleEnabled(false)를 주어 내부적으로 툴바 타이틀이 적용되는것을 막고,
            그다음 title을 set을 해준다. 정확한 원인 파악하기!
     */
    public void setToolbarTitleNoCollapsing(String title){
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(title);
    }
    public void setDrawerLayoutAndToolbar() {
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        //appBarLayout = findViewById(R.id.app_bar);

        if (drawerLayout != null && toolbar != null && navigationView != null && collapsingToolbarLayout != null) {
            setSupportActionBar(toolbar);
            navigationView.bringToFront();
            navigationView.setNavigationItemSelectedListener(this);
            //appBarLayout.setExpanded(false);
        }
    }
    public void setAppBarLayout(boolean isOpen){
        appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.setExpanded(isOpen);
    }
    public void setAppBarConfigurationForLeftMenuIcon() {
        drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);
    }

    /**
     * 테마 설정
     */
    public void setTheme(eThemeType themeType) {

        setTheme((themeType == eThemeType.DARK) ? R.style.DarkTheme : R.style.WhiteTheme);
    }

    private Dialog mNetworkPopup;

    public void showNetworkPopup(int nType, String message) {
//        if(this instanceof HSIntroMainActivity){
//            nType = 0;
//            message = getString(R.string.network_error_connect_base);
//        }
        if (mNetworkPopup != null) {
            if (mNetworkPopup.isShowing()) {
                return;
            }
        }

        switch (nType) {
            case 0:
                mNetworkPopup = AlertUtil.alert(getActivity(), message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                IntentUtil.finishApp(getActivity());
                            }
                        });
                break;
            case 1:
                mNetworkPopup = AlertUtil.confirm(getActivity(), message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            //restartNetwork();
                        } else {
                            IntentUtil.finishApp(getActivity());
                        }
                    }
                }, "재접속", "앱종료");
                break;
        }

    }

    public void dismissNetworkPopup() {
        if (mNetworkPopup != null) {
            if (mNetworkPopup.isShowing()) {
                mNetworkPopup.dismiss();
            }
        }
    }


    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.i("onNewIntent()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActived = true;
        App.setCurrentActivity(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 화면 활성화 여부 반환 (최상위 Activity 여부)
     *
     * @return
     */
    public boolean isActived() {
        return isActived;
    }

    /**
     * 화면 UI/데이터 갱신 처리를 하는 공통 Method
     */
    public void onRefreshScreen() {

    }

    ;

    /**
     * 테마 변경 처리를 하는 공통 Method
     */
    protected void onChangeTheme() {
        ThemeUtil.setStatusBarColor(getActivity());
        View rootView = findViewById(android.R.id.content);
        ThemeUtil.updateThemeToBackground(rootView, R.attr.windowBackground);
        ThemeUtil.updateTheme((ViewGroup) rootView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
//            case HSEventNoticeDialog.EVENT_MOVE_SCREEN:
//                if (resultCode == Activity.RESULT_OK) {
//                    IntentUtil.startActivity(getContext(), HSIdentitAuthBaseActivity.class);
//                }
//                break;
//
//            case HSEventNoticeDialog.EVENT_SEND_SHARE_IMG:
//                if (resultCode == Activity.RESULT_OK) {
//                    Uri uri = data.getData();
//                    EventBus.getDefault().post(new MainEvent(MainEvent.EVENT_EVENT_PHOTO_ACTION, uri));
//                }
//                break;
//
//            default:
//                // 장바구니 로그인 후 이동
//                HSCartManager.getInstance().onActivityResult(this, requestCode, resultCode, data);
//                // 간편송금 로그인 후 이동
//                HSTransferHelper.onActivityResult(this, requestCode, resultCode, data);
//
//                //이벤트 해외송금 로그인 후
//                HSEventNoticeManager.getInstance().onActivityResult(this, requestCode, resultCode, data);
//                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        App.onSaveInstanceState(outState);
    }

    private boolean mPermissionGranted = true;

    private void onRestoreInstance(Bundle savedInstanceState) {


        if (PermissionUtil.isDeniedSize(this, PermissionUtil.REQUEST_ESSENTIAL_PERMISSION) > 0) {
            hideProgress();
            mPermissionGranted = false;
            String message = getString(R.string.app_finish);
            AlertUtil.alert(getActivity(), "안내", message, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //IntentUtil.finishApp(getActivity());
                }
            });
            return;
        }

//        if (savedInstanceState != null) {
//            LogUtil.w("onRestoreInstanceState != null");
//            App.onRestoreInstanceState(savedInstanceState);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActived = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

    }

    public BaseActivity getActivity() {
        return this;
    }

    public Context getContext() {
        return getActivity();
    }

    /********************************************************************************************************
     * ProgressDialog
     *********************************************************************************************************/

    public void showProgress() {
        if (mPermissionGranted) {
            showProgress(null, 0, true);
        }
    }

    public void showProgress(final String msg, final int duration, final boolean cancelable) {
        if (!mPermissionGranted) {
            return;
        }
        if (mProgressDialog != null)
            if (mProgressDialog.isShowing()) return;

        //프로그래스바 출력.
        if (!UIUtil.checkActivityRunningState(getActivity())) return;

        mProgressDialog = ProgressDialogBase.show(getActivity(), "", msg, true, cancelable);

        //프로그래스바 타이머.
        if (duration > 0) {
            Runnable run = new Runnable() {
                public void run() {
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    }
                }
            };
            Handler handler = new Handler();
            handler.postDelayed(run, duration);
        }
    }

    public void hideProgress() {
        try {
            //프로그래스바 감추기.
            if (isFinishing()) return;
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /********************************************************************************************************
     * Event Bus
     *********************************************************************************************************/
    @Subscribe
    public void onEvent(final MainEvent event) {

        switch (event.mEvent) {
            case MainEvent.EVENT_CHANGE_THEME_ACTION: /* 테마변경 */

                LogUtil.i("ThemeUtil", "onEvent(" + this.getClass().getSimpleName() + ")");
                if (mThemeType != App.getThemeType()) {
                    mThemeType = App.getThemeType();
                    setTheme(mThemeType);
                    onChangeTheme();
                }
                break;
        }
    }

    /********************************************************************************************************
     * Click Event
     *********************************************************************************************************/

    @Override
    public void onClick(View view) {
        if (UIUtil.isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.titleBackBtn:
                onBackPressed();
                break;

//            case R.id.back_btn:
//                finish();
//                break;

            default:
                break;
        }
    }

    /********************************************************************************************************
     * softKeyboard
     *********************************************************************************************************/

    /**
     * 키패드 보이기.
     */
    public void showSoftKeyboard() {
        View v = getCurrentFocus();
        if (v != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(v, 0);
        }
    }

    /**
     * 키패드 보이기.
     */
    public void showSoftKeyboardDelayed() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                View v = getCurrentFocus();
                if (v != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(v, 0);
                }
            }
        }, 300);
    }

    /**
     * 키패드 감추기
     */
    public void hideSoftKeyboard() {
        View v = getCurrentFocus();
        if (v != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            v.clearFocus();
        }
    }

    /**
     * 키패드 감추기
     */
    public void hideSoftKeyboardDelayed() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                View v = getCurrentFocus();
                if (v != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    v.clearFocus();
                }
            }
        }, 200);
    }

    /********************************************************************************************************
     * HSTitleBar
     *********************************************************************************************************/

    protected void setTitle(String title) {
        HSTitleBar titleBar = findViewById(R.id.titleBar);
        if (titleBar != null) {
            titleBar.setTitle(title);
        }
    }

    protected void setBackIconType(HSTitleBar.eBackIconType type) {
        HSTitleBar titleBar = findViewById(R.id.titleBar);
        if (titleBar != null) {
            titleBar.setBackIconType(type);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home: {
                Intent intent = new Intent(this, HomeActivity.class);
                //intent.addCategory(Intent.CATEGORY_HOME);
                //나중에 위 방식도 사용해보자
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            }
            case R.id.nav_gallery: {
                startActivity(new Intent(this, GalleryActivity.class));
                break;
            }
            case R.id.nav_slideshow: {
                startActivity(new Intent(this, SlideshowActivity.class));
                break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
