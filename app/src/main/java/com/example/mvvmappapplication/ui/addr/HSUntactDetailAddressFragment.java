package com.example.mvvmappapplication.ui.addr;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.custom.HSUntactFloatingButtonType;
import com.example.mvvmappapplication.custom.HSUntactTitleBar;
import com.example.mvvmappapplication.databinding.FragmentUntactDetailAddressBinding;
import com.example.mvvmappapplication.dto.BC1099Q1Dto;
import com.example.mvvmappapplication.utils.AlertUtil;
import com.example.mvvmappapplication.utils.UIUtil;


public class HSUntactDetailAddressFragment extends HSUntactSearchAddressBaseFragment<FragmentUntactDetailAddressBinding, UntactSearchAddressViewModel> {
    private static final String TAG = HSUntactDetailAddressFragment.class.getSimpleName();

    /********************************************************************************************************
     * Intent Factory
     *********************************************************************************************************/
    public static HSUntactDetailAddressFragment newInstance() {
        HSUntactDetailAddressFragment fragment = new HSUntactDetailAddressFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    /***********************************************************************************************************************************/
    // Override Extends
    /***********************************************************************************************************************************/
    @Override
    public int getLayoutId() {
        return R.layout.fragment_untact_detail_address;
    }

    @Override
    public UntactSearchAddressViewModel newViewModel() {
        return ViewModelProviders.of(getActivity()).get(UntactSearchAddressViewModel.class);
    }


    /***********************************************************************************************************************************/
    // Override Android
    /***********************************************************************************************************************************/
    @Override
    public void onDestroyView() {
        getViewModel().mSelectAddrLiveData.removeObserver(mSelectAddrObserver);
        super.onDestroyView();
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        initData();
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

        getBinding().rlFragmentUntactDetailAddressResultBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onParentButtonClickPrev();
            }
        });

        getBinding().rlFragmentUntactDetailAddressDetailBody.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                getBinding().rlFragmentUntactDetailAddressDetailBody.setSelected(true);
                getBinding().rlFragmentUntactDetailAddressDetailBody.setElevation(20);
                getBinding().hsuetFragmentUntactDetailAddressDetail.requestFocus();
                mActivity.showSoftKeyboard();
                return false;
            }

        });
    }

    private void initData() {
        getViewModel().mSelectAddrLiveData.observe(this, mSelectAddrObserver);
    }



    /***********************************************************************************************************************************/
    // 유효성 검사
    /***********************************************************************************************************************************/
    private boolean isValidValueItem(boolean _isAlert){
        BC1099Q1Dto item = getViewModel().mSelectAddrLiveData.getValue();
        if (item == null) {
            if (_isAlert) AlertUtil.toast(getContext(), "값 오류");
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
        if (!isValidValueItem(false)) return;
        onDataSet();
//        getViewModel().performBC1096Q7(
//                getViewModel().mSelectAddrLiveData.getValue().ROAD_NM_ZPCD,
//                getViewModel().mSelectAddrLiveData.getValue().ROAD_NM_BSIC_ADDR,
//                getBinding().hsuetFragmentUntactDetailAddressDetail.getText().toString().trim(),
//                getBinding().incFragmentUntactDetailAddressResult.hsntvLayoutUntactSearchAddressResultPostJibunAddr.getText().toString().trim()
//        );
        UIUtil.hideSoftKeyboard(getBinding().hsuetFragmentUntactDetailAddressDetail);

        mActivity.onParentButtonClickNext();
    }


    /***********************************************************************************************************************************/
    // Observer
    /***********************************************************************************************************************************/
    /**
     * 키워드 검색 결과 선택 주소
     */
    Observer<BC1099Q1Dto> mSelectAddrObserver = new Observer<BC1099Q1Dto>() {
        @Override
        public void onChanged(@Nullable BC1099Q1Dto data) {
            getBinding().incFragmentUntactDetailAddressResult.hsntvLayoutUntactSearchAddressResultPostNum.setText(data.ROAD_NM_ZPCD);
            getBinding().incFragmentUntactDetailAddressResult.hsntvLayoutUntactSearchAddressResultPostRoadAddr.setText(data.ROAD_NM_BSIC_ADDR);
            getBinding().incFragmentUntactDetailAddressResult.hsntvLayoutUntactSearchAddressResultPostJibunAddr.setText(data.STD_BSIC_ADDR);
        }
    };

}