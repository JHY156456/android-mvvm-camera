package com.example.mvvmappapplication.ui.addr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.custom.HSUntactFloatingButtonType;
import com.example.mvvmappapplication.custom.HSUntactTitleBar;
import com.example.mvvmappapplication.databinding.FragmentUntactSelectAddressBinding;
import com.example.mvvmappapplication.dto.BC1096Q7Dto;
import com.example.mvvmappapplication.utils.AlertUtil;
import com.example.mvvmappapplication.utils.IntentConst;

public class HSUntactSelectAddressFragment extends HSUntactSearchAddressBaseFragment<FragmentUntactSelectAddressBinding, UntactSearchAddressViewModel> {
    private static final String TAG = HSUntactSelectAddressFragment.class.getSimpleName();

    /********************************************************************************************************
     * Intent Factory
     *********************************************************************************************************/
    public static HSUntactSelectAddressFragment newInstance() {
        HSUntactSelectAddressFragment fragment = new HSUntactSelectAddressFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    /***********************************************************************************************************************************/
    // Override Extends
    /***********************************************************************************************************************************/
    @Override
    public int getLayoutId() {
        return R.layout.fragment_untact_select_address;
    }

    @Override
    public UntactSearchAddressViewModel newViewModel() {
        return ViewModelProviders.of(getActivity()).get(UntactSearchAddressViewModel.class);
    }


    /***********************************************************************************************************************************/
    // Override Android
    /***********************************************************************************************************************************/
    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        initData();
    }

    @Override
    public void onDestroyView() {
        getViewModel().mRefineAddrLiveData.removeObserver(mRefineRoadAddrObserver);
        super.onDestroyView();
    }

    /***********************************************************************************************************************************/
    // View
    /***********************************************************************************************************************************/
    private void initView(){
        mActivity.refreshTitle(View.VISIBLE, HSUntactTitleBar.TitleBarType.CLOSE, "주소검색", "",
                false, false, false, false, false);

        mActivity.refreshFloatingBtn("", "확인");
        mActivity.refreshFloatingBtn(View.GONE, View.GONE, View.VISIBLE);

        mActivity.getBinding().hsnufbtUntactFloatingBtn.setOnCallbackItemClickListener(new HSUntactFloatingButtonType.CallbackItemClickListener() {
            @Override
            public void clickView(View view) {
                switch (view.getId()){
                    case R.id.btn_layout_untact_floating_button_prev:
                        break;

                    case R.id.btn_layout_untact_floating_button_next:
                        onNext();
                        break;
                }
            }
        });

        getBinding().llFragmentUntactSelectAddressRefine.setOnClickListener(mOnClickListener);
        getBinding().llFragmentUntactSelectAddressInput.setOnClickListener(mOnClickListener);
    }

    private void initData() {
        getViewModel().mRefineAddrLiveData.observe(this, mRefineRoadAddrObserver);
    }


    /***********************************************************************************************************************************/
    // 유효성 검사
    /***********************************************************************************************************************************/
    private boolean isValidValueSelectAddr(boolean _isAlert){
        if (!getBinding().llFragmentUntactSelectAddressRefine.isSelected() && !getBinding().llFragmentUntactSelectAddressInput.isSelected()){
            if (_isAlert) AlertUtil.toast(getContext(), "정제된 주소 또는 입력된 주소를 선택해주세요.");
            return false;
        }
        return true;
    }

    /***********************************************************************************************************************************/
    // Data Set
    /***********************************************************************************************************************************/
    private void onDataSet(){
    }

    /***********************************************************************************************************************************/
    // Next
    /***********************************************************************************************************************************/
    private void onNext(){
        if (!isValidValueSelectAddr(true)) return;

        onDataSet();
        /* 도로명 주소 */
        BC1096Q7Dto data = getViewModel().mRefineAddrLiveData.getValue();
        if (data == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(IntentConst.CREATE_ACCOUNT_ACTIVITY.ACTIVITY_RESULT_KEY_ADDR_TYPE, IntentConst.CREATE_ACCOUNT_ACTIVITY.ROAD_ADDRESS);
        if (getBinding().llFragmentUntactSelectAddressRefine.isSelected()){ /* 정제된 주소 */
            intent.putExtra(IntentConst.CREATE_ACCOUNT_ACTIVITY.ACTIVITY_RESULT_KEY_ZIP_CODE, data.ROAD_NM_ZPCD);
            intent.putExtra(IntentConst.CREATE_ACCOUNT_ACTIVITY.ACTIVITY_RESULT_KEY_ADDR_BASE, data.ROAD_NM_BSIC_ADDR);
            intent.putExtra(IntentConst.CREATE_ACCOUNT_ACTIVITY.ACTIVITY_RESULT_KEY_ADDR_DETAIL, data.ROAD_NM_DETL_ADDR);
        }
        else{   /* 입력한 주소 */
            intent.putExtra(IntentConst.CREATE_ACCOUNT_ACTIVITY.ACTIVITY_RESULT_KEY_ZIP_CODE, data.INP_ZPCD);
            intent.putExtra(IntentConst.CREATE_ACCOUNT_ACTIVITY.ACTIVITY_RESULT_KEY_ADDR_BASE, data.INP_BSIC_ADDR);
            intent.putExtra(IntentConst.CREATE_ACCOUNT_ACTIVITY.ACTIVITY_RESULT_KEY_ADDR_DETAIL, data.INP_DETL_ADDR);
        }
        getViewModel().getActivity().setResult(Activity.RESULT_OK, intent);
        getViewModel().getActivity().finish();
    }

    /***********************************************************************************************************************************/
    // Listener
    /***********************************************************************************************************************************/
    private View.OnClickListener mOnClickListener = v -> {
        switch (v.getId()) {
            case R.id.ll_fragment_untact_select_address_refine: /* 도로명 확인 주소 */
                getBinding().llFragmentUntactSelectAddressRefine.setSelected(true);
                getBinding().llFragmentUntactSelectAddressRefine.findViewById(R.id.iv_layout_untact_search_address_result_icon).setVisibility(View.VISIBLE);
                getBinding().llFragmentUntactSelectAddressInput.setSelected(false);
                getBinding().llFragmentUntactSelectAddressInput.findViewById(R.id.iv_layout_untact_search_address_result_icon).setVisibility(View.GONE);
                break;

            case R.id.ll_fragment_untact_select_address_input: /* 입력한 주소 */
                getBinding().llFragmentUntactSelectAddressRefine.setSelected(false);
                getBinding().llFragmentUntactSelectAddressRefine.findViewById(R.id.iv_layout_untact_search_address_result_icon).setVisibility(View.GONE);
                getBinding().llFragmentUntactSelectAddressInput.setSelected(true);
                getBinding().llFragmentUntactSelectAddressInput.findViewById(R.id.iv_layout_untact_search_address_result_icon).setVisibility(View.VISIBLE);
                break;
        }
    };

    /***********************************************************************************************************************************/
    // Observer
    /***********************************************************************************************************************************/
    /**
     * 도로명 정제 주소 조회 결과
     */
    Observer<BC1096Q7Dto> mRefineRoadAddrObserver = new Observer<BC1096Q7Dto>() {
        @Override
        public void onChanged(@Nullable BC1096Q7Dto data) {
            if (data == null) {
                mActivity.onParentButtonClickPrev();
                return;
            }
            getBinding().incFragmentUntactSelectAddressRefine.hsntvLayoutUntactSearchAddressResultPostNum.setText(data.ROAD_NM_ZPCD);
            getBinding().incFragmentUntactSelectAddressRefine.hsntvLayoutUntactSearchAddressResultPostRoadAddr.setText(String.format("%s\n%s", data.ROAD_NM_BSIC_ADDR, data.ROAD_NM_DETL_ADDR));
            getBinding().incFragmentUntactSelectAddressRefine.hsntvLayoutUntactSearchAddressResultPostJibunAddr.setText(getViewModel().mPostJibunAddr);
            getBinding().incFragmentUntactSelectAddressInput.hsntvLayoutUntactSearchAddressResultPostNum.setText(data.INP_ZPCD);
            getBinding().incFragmentUntactSelectAddressInput.hsntvLayoutUntactSearchAddressResultPostRoadAddr.setText(String.format("%s\n%s", data.INP_BSIC_ADDR, data.INP_DETL_ADDR));
            getBinding().incFragmentUntactSelectAddressInput.hsntvLayoutUntactSearchAddressResultPostJibunAddr.setText(getViewModel().mPostJibunAddr);
            getBinding().llFragmentUntactSelectAddressRefine.performClick();
        }
    };

}