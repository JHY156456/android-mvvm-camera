package com.example.mvvmappapplication.ui.addr;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.mvvmappapplication.App;
import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.custom.HSUntactFloatingButtonType;
import com.example.mvvmappapplication.custom.HSUntactTitleBar;
import com.example.mvvmappapplication.databinding.ActivityUntactSearchAddressBinding;
import com.example.mvvmappapplication.dto.BC1099Q1Dto;
import com.example.mvvmappapplication.ui.BaseDataBindingActivity;
import com.example.mvvmappapplication.utils.AlertUtil;
import com.example.mvvmappapplication.utils.IntentConst;

public class HSUntactSearchAddressActivity extends BaseDataBindingActivity<ActivityUntactSearchAddressBinding, UntactSearchAddressViewModel> {
    private static final String TAG = HSUntactSearchAddressActivity.class.getSimpleName();

    public static final int SEARCH_ADDRESS = 0;
    public static final int DETAIL_ADDRESS = 1;
    public static final int SELECT_ADDRESS = 2;

    //현재 적용된 플레그먼트 객체.
    private HSUntactSearchAddressBaseFragment mCurrentFragment;
    //현재의 스텝 index.
    public int mCurrentFragmentStepIndex = 0;


    /***********************************************************************************************************************************/
    // Override Extends
    /***********************************************************************************************************************************/
    @Override
    public int getLayoutId() {
        return R.layout.activity_untact_search_address;
    }

    @Override
    public UntactSearchAddressViewModel newViewModel() {
        return ViewModelProviders.of(this).get(UntactSearchAddressViewModel.class);
    }

    @Override
    public void onRefreshScreen() {
        if (this.mCurrentFragment != null) {
            mCurrentFragment.refreshScreen();
        }
    }


    /********************************************************************************************************
     * Intent Factory
     *********************************************************************************************************/
    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, HSUntactSearchAddressActivity.class);
        return intent;
    }

    private void getIntent(Intent intent) {
        try {
            String gaScreen = intent.getStringExtra("gaScreen");
        } catch (Exception e) {
        }

    }


    /***********************************************************************************************************************************/
    // Override Android
    /***********************************************************************************************************************************/
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        mCurrentFragment.onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCurrentFragment.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState == null) return;

        if (mCurrentFragment != null)
            mCurrentFragment.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("mCurrentFragmentStepIndex", mCurrentFragmentStepIndex);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        if (App.PROTECT_SCREEN_CAPTURE)  getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);   // 화면캡처 기능 추가
        getIntent(getIntent());

        initView();
        initData();
    }


    /***********************************************************************************************************************************/
    // View
    /***********************************************************************************************************************************/
    private void initView() {
        /* Title */
        getBinding().hsnufbtUntactFloatingBtn.setOnCallbackItemClickListener(new HSUntactFloatingButtonType.CallbackItemClickListener() {
            @Override
            public void clickView(View view) {
                switch (view.getId()){
                    case R.id.btn_layout_untact_floating_button_prev:
                        onParentButtonClickPrev();
                        break;

                    case R.id.btn_layout_untact_floating_button_next:
                        onParentButtonClickNext();;
                        break;
                }
            }
        });

    }

    private void initData() {
        mCurrentFragmentStepIndex = HSUntactSearchAddressActivity.SEARCH_ADDRESS;
        changeFragment(mCurrentFragmentStepIndex);
    }

    /* Title Bar 변경 */
    public void refreshTitle(int _visibility, HSUntactTitleBar.TitleBarType _type, String _title, String _desc
            , boolean _menuBack, boolean _menuClose, boolean _menuAdmin, boolean _underLine, boolean _areaDesc){
        if (getBinding().hsnutbUntactTitleBar != null) {
            getBinding().hsnutbUntactTitleBar.setVisibility(_visibility);
            if (_visibility == View.VISIBLE) getBinding().hsnutbUntactTitleBar.refreshTitleBarType(_type, _title, _desc, _menuBack, _menuClose, _menuAdmin, _underLine, _areaDesc);
        }
    }
    public void refreshDiscription(int offset){
        if (getBinding().hsnutbUntactTitleBar != null) getBinding().hsnutbUntactTitleBar.refreshDescription(offset);
    }

    /* Floating Button 변경 */
    public void refreshFloatingBtn(int _prevViewVisible, int _dividerViewVisible, int _nextViewVisible){
        if (getBinding().hsnufbtUntactFloatingBtn != null) getBinding().hsnufbtUntactFloatingBtn.refreshViewVisible(_prevViewVisible, _dividerViewVisible, _nextViewVisible);
    }
    public void refreshFloatingBtn(String _prevViewText, String _nextViewText){
        if (getBinding().hsnufbtUntactFloatingBtn != null) getBinding().hsnufbtUntactFloatingBtn.refreshViewText(_prevViewText,_nextViewText);
    }


    /***********************************************************************************************************************************/
    // Fragment
    /***********************************************************************************************************************************/
    /**
     * 현재 표기할 Fragment set
     */
    public void changeFragment(int fragmentIndex) {
        hideSoftKeyboard();
        Bundle bundle = new Bundle();
        changeFragment(fragmentIndex, bundle);
    }

    /**
     * 현재 표기할 Fragment set
     */
    public void changeFragment(int fragmentIndex, Bundle bundle) {

        // TODO: 2016. 10. 6. 플래그먼트를 변경전에 어떤 프래그먼트 였는지 id값을 함게 넘긴다.
        bundle.putInt(IntentConst.CREATE_ACCOUNT_ACTIVITY.KEY_STEP_BEFORE,
                mCurrentFragmentStepIndex);

        mCurrentFragmentStepIndex = fragmentIndex;

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        if (this.mCurrentFragment != null) {
            fragmentTransaction.remove(this.mCurrentFragment);
        }

        switch (fragmentIndex) {
            case SEARCH_ADDRESS: /* 검색어 입력 */
                mCurrentFragment = HSUntactSearchAddressFragment.newInstance();
                break;

            case DETAIL_ADDRESS: /* 상세주소 입력 */
                mCurrentFragment = HSUntactDetailAddressFragment.newInstance();
                break;

            case SELECT_ADDRESS: /* 주소 정제 및 주소지 선택 */
                mCurrentFragment = HSUntactSelectAddressFragment.newInstance();
                break;

        }

        fragmentTransaction.replace(getBinding().flFragmentContainer.getId(), mCurrentFragment);
        fragmentTransaction.addToBackStack(null);
        mCurrentFragment.setArguments(bundle);
        fragmentTransaction.commit();

    }

    //fragment 변경.
    public void changeFragment(HSUntactSearchAddressBaseFragment fragment, Bundle bundle) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        if (this.mCurrentFragment != null) {
            fragmentTransaction.remove(this.mCurrentFragment);
        }

        this.mCurrentFragment = fragment;

        fragmentTransaction.replace(getBinding().flFragmentContainer.getId(), mCurrentFragment);
        if (bundle != null) {
            mCurrentFragment.setArguments(bundle);
        }
        fragmentTransaction.commit();
    }



    /***********************************************************************************************************************************/
    // Listener
    /***********************************************************************************************************************************/
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.cl_item_untact_search_address_body:
                int position = (int) view.getTag();
                BC1099Q1Dto item = getViewModel().getItem(position);
                getViewModel().mSelectAddrLiveData.setValue(item);

                onParentButtonClickNext();
                break;
        }
    }



    /***********************************************************************************************************************************/
    // Interface Fragment
    /***********************************************************************************************************************************/
    public void onParentButtonClickPrev() {
        if (mCurrentFragment.onClickPrevStep()) {
            if (mCurrentFragmentStepIndex == SEARCH_ADDRESS) {
                return;
            } else {
                changeFragment(--mCurrentFragmentStepIndex);
            }
        }
    }

    public void onParentButtonClickNext() {

        if (mCurrentFragment.onClickNextStep()) {
            if (mCurrentFragmentStepIndex == SELECT_ADDRESS) {
                return;
            } else {
                changeFragment(++mCurrentFragmentStepIndex);
            }
        }

    }

    public void onParentButtonClickTitleBack() {
        //타이틀 back버튼 과 back 버튼 공통 액션 처리.

        if (mCurrentFragment.onClickTitleBackBtn()) {
            AlertUtil.confirm(getContext(), "주소입력 취소", "주소입력을 중지하시겠습니까?",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                finish();
                            }
                        }
                    });
        }
    }

}