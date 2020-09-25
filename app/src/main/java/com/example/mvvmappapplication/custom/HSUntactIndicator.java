package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.databinding.DataBindingUtil;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.databinding.LayoutUntactIndicatorBinding;
import com.example.mvvmappapplication.utils.UIUtil;


public class HSUntactIndicator extends FrameLayout {
	private static final String TAG = HSUntactIndicator.class.getSimpleName();

	private CallbackItemClickListener mCallbackItemClickListener;
	public interface CallbackItemClickListener{
		void clickView(View view);
	}
	public void setOnCallbackItemClickListener(CallbackItemClickListener _callbackItemClickListener){
		mCallbackItemClickListener = _callbackItemClickListener;
	}

	LayoutUntactIndicatorBinding mBinding;

	public HSUntactIndicator(Context context) {
		super(context);
		initView();
	}

	public HSUntactIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public HSUntactIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		initView();
	}


	/***********************************************************************************************************************************/
	// Init View
	/***********************************************************************************************************************************/
	private void initView() {
		mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_untact_indicator, this, true);
	}

	public void refreshView(int _maxNum){
		mBinding.rgUntactIndicatorGroup.removeAllViews();
		int indicatorSize = UIUtil.getPxFromDp(R.dimen.dimen_8);
		int padSize = UIUtil.getPxFromDp(R.dimen.dimen_3);
		for (int i = 0; i < _maxNum; i++) {
			RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(indicatorSize, indicatorSize);
			if (i != 0) {
				layoutParams.leftMargin = padSize;
			}
			RadioButton oButton = new RadioButton(getContext());
			oButton.setLayoutParams(layoutParams);
			oButton.setId(i);
			oButton.setButtonDrawable(R.drawable.untact_selector_indicator);
			mBinding.rgUntactIndicatorGroup.addView(oButton);
		}
		invalidate();
	}

	public void setCheckIndicator(int _index){
		mBinding.rgUntactIndicatorGroup.check(_index);
		invalidate();
	}

	public int getCheckIndicator(){
		return mBinding.rgUntactIndicatorGroup.getCheckedRadioButtonId();
	}



	/***********************************************************************************************************************************/
	// getViewTreeObserver
	/***********************************************************************************************************************************/
	private ViewTreeObserver.OnGlobalLayoutListener mViewTreeObserver = new ViewTreeObserver.OnGlobalLayoutListener() {
		@Override
		public void onGlobalLayout() {

		}
	};


	/***********************************************************************************************************************************/
	// Listener
	/***********************************************************************************************************************************/
	private OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mCallbackItemClickListener != null) mCallbackItemClickListener.clickView(v);
		}
	};

}