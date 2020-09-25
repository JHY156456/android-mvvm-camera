package com.example.mvvmappapplication.ui.addr;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.custom.HSRecyclerView;
import com.example.mvvmappapplication.custom.HSUntactFloatingButtonType;
import com.example.mvvmappapplication.custom.HSUntactTitleBar;
import com.example.mvvmappapplication.databinding.FragmentUntactSearchAddressBinding;
import com.example.mvvmappapplication.utils.AlertUtil;
import com.example.mvvmappapplication.utils.UIUtil;


public class HSUntactSearchAddressFragment extends HSUntactSearchAddressBaseFragment<FragmentUntactSearchAddressBinding, UntactSearchAddressViewModel> {
    private static final String TAG = HSUntactSearchAddressFragment.class.getSimpleName();

    UntactSearchAddressAdapter mAdapter;

    /********************************************************************************************************
     * Intent Factory
     *********************************************************************************************************/
    public static HSUntactSearchAddressFragment newInstance() {
        HSUntactSearchAddressFragment fragment = new HSUntactSearchAddressFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    /***********************************************************************************************************************************/
    // Override Extends
    /***********************************************************************************************************************************/
    @Override
    public int getLayoutId() {
        return R.layout.fragment_untact_search_address;
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
        getViewModel().mEndlessScrollLiveData.removeObserver(mEndlessScrollObserver);
        getBinding().hsuetFragmentUntactSearchAddressInput.removeTextChangedListener(mTextWatcher);
        getBinding().hsuetFragmentUntactSearchAddressInput.setOnEditorActionListener(null);
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

        mActivity.refreshFloatingBtn("", "검색");
        mActivity.refreshFloatingBtn(View.GONE, View.GONE, View.VISIBLE);

        mActivity.getBinding().hsnufbtUntactFloatingBtn.setOnCallbackItemClickListener(new HSUntactFloatingButtonType.CallbackItemClickListener() {
            @Override
            public void clickView(View view) {
                switch (view.getId()){
                    case R.id.btn_layout_untact_floating_button_prev:
                        break;

                    case R.id.btn_layout_untact_floating_button_next:
                        onSearch();
                        break;
                }
            }
        });

        getBinding().llFragmentUntactSearchAddressInputBody.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectInputLayout();
                return false;
            }

        });

        getBinding().hsuetFragmentUntactSearchAddressInput.addTextChangedListener(mTextWatcher);
        getBinding().hsuetFragmentUntactSearchAddressInput.setOnEditorActionListener(mOnEditorActionListener);
        getBinding().hsuetFragmentUntactSearchAddressInput.setText(getViewModel().mSearchKeyword);
        getBinding().btnFragmentUntactSearchAddressClear.setOnClickListener(mOnClickListener);

        mAdapter = new UntactSearchAddressAdapter(getContext(), getViewModel());

        getBinding().recyclerView.setAdapter(mAdapter);
        getBinding().recyclerView.setEndlessScrollListener(mIEndlessScrollListener);
    }

    private void initData() {
        selectInputLayout();
        mActivity.getBinding().hsnufbtUntactFloatingBtn.findViewById(R.id.btn_layout_untact_floating_button_next).setEnabled(!TextUtils.isEmpty(getBinding().hsuetFragmentUntactSearchAddressInput.getText().toString())?true:false);
        getViewModel().mEndlessScrollLiveData.observe(this, mEndlessScrollObserver);
        getViewModel().mCheckListEmptyLiveData.observe(this, mCheckListEmptyObserver);
    }

    private void selectInputLayout(){
        getBinding().llFragmentUntactSearchAddressBody.setSelected(true);
        getBinding().llFragmentUntactSearchAddressBody.setElevation(20);
        getBinding().hsuetFragmentUntactSearchAddressInput.requestFocus();
        mActivity.showSoftKeyboard();
    }


    /***********************************************************************************************************************************/
    // 유효성 검사
    /***********************************************************************************************************************************/
    private boolean isValidValueKeyword(boolean _isAlert){
        String keyword = getBinding().hsuetFragmentUntactSearchAddressInput.getText().toString().trim();
        if (keyword.length() < 2) {
            if (_isAlert) AlertUtil.alert(getActivity(), "검색어는 2글자 이상 입력하세요.");
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
    private void onSearch(){
        if (!isValidValueKeyword(true)) return;
        UIUtil.hideSoftKeyboard(getBinding().hsuetFragmentUntactSearchAddressInput);
      //  getViewModel().performBC1099Q1(getBinding().hsuetFragmentUntactSearchAddressInput.getText().toString().trim());
    }


    /***********************************************************************************************************************************/
    // Listener
    /***********************************************************************************************************************************/
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_fragment_untact_search_address_clear: /* 텍스트 clear */
                    getBinding().hsuetFragmentUntactSearchAddressInput.setText("");
                    getViewModel().getItems().clear();
                    if (mAdapter != null) mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String str = s.toString().trim();
            getBinding().btnFragmentUntactSearchAddressClear.setVisibility((str.length() > 0) ? View.VISIBLE : View.GONE);
            getBinding().ivFragmentUntactSearchAddressSearchIcon.setVisibility((str.length() > 0) ? View.GONE : View.VISIBLE);
            mActivity.getBinding().hsnufbtUntactFloatingBtn.findViewById(R.id.btn_layout_untact_floating_button_next).setEnabled(str.length() >= 2?true:false);
        }
    };

    TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_SEARCH) {
                onSearch();
            }
            return false;
        }
    };

    HSRecyclerView.IEndlessScrollListener mIEndlessScrollListener = new HSRecyclerView.IEndlessScrollListener() {

        @Override
        public void onLoadMore(String nextPageKey) {
            String keyword = getBinding().hsuetFragmentUntactSearchAddressInput.getText().toString().trim();
            if (keyword.length() > 2) {
                //getViewModel().performBC1099Q1(keyword, nextPageKey);
            }
        }
    };


    /***********************************************************************************************************************************/
    // Observer
    /***********************************************************************************************************************************/
    Observer<String> mEndlessScrollObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String nextPageKey) {
            if (TextUtils.isEmpty(nextPageKey)) {
                getBinding().recyclerView.initState();
            }
            mAdapter.notifyDataSetChanged();
            getBinding().recyclerView.setNextPageKey(nextPageKey);
        }
    };

    Observer<Boolean> mCheckListEmptyObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean isEmpty) {
            getBinding().vFragmentUntactSearchAddressDivider.setVisibility(isEmpty? View.GONE: View.VISIBLE);
        }
    };

}