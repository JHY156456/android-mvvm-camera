package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.databinding.DataBindingUtil;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.databinding.LayoutUntactFloatingButtonBinding;


public class HSUntactFloatingButtonType extends FrameLayout {
	private static final String TAG = HSUntactFloatingButtonType.class.getSimpleName();

	private CallbackItemClickListener mCallbackItemClickListener;
	public interface CallbackItemClickListener{
		void clickView(View view);
	}
	public void setOnCallbackItemClickListener(CallbackItemClickListener _callbackItemClickListener){
		mCallbackItemClickListener = _callbackItemClickListener;
	}

	LayoutUntactFloatingButtonBinding mBinding;

	public HSUntactFloatingButtonType(Context context) {
		super(context);
		initView();
	}

	public HSUntactFloatingButtonType(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public HSUntactFloatingButtonType(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		initView();
	}


	/***********************************************************************************************************************************/
	// Init View
	/***********************************************************************************************************************************/
	private void initView() {
		mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_untact_floating_button, this, true);
		mBinding.btnLayoutUntactFloatingButtonPrev.setOnClickListener(mOnClickListener);
		mBinding.btnLayoutUntactFloatingButtonNext.setOnClickListener(mOnClickListener);
	}

	public void refreshViewText(String _prevViewText, String _nextViewText){
		if (_prevViewText.length() != 0) mBinding.btnLayoutUntactFloatingButtonPrev.setText(_prevViewText);
		if (_nextViewText.length() != 0) mBinding.btnLayoutUntactFloatingButtonNext.setText(_nextViewText);
	}

	public void refreshViewEnable(boolean _prevViewEnable, boolean _nextViewEnable){
		mBinding.btnLayoutUntactFloatingButtonPrev.setEnabled(_prevViewEnable);
		mBinding.btnLayoutUntactFloatingButtonNext.setEnabled(_nextViewEnable);
	}

	public void refreshViewVisible(int _prevViewVisible, int _dividerViewVisible, int _nextViewVisible){
		mBinding.btnLayoutUntactFloatingButtonPrev.setVisibility(_prevViewVisible);
		mBinding.btnLayoutUntactFloatingButtonDivider.setVisibility(_dividerViewVisible);
		mBinding.btnLayoutUntactFloatingButtonNext.setVisibility(_nextViewVisible);
	}

	public void refreshViewContainerVisible(int _viewVisible){
		mBinding.btnLayoutUntactFloatingButtonPrev.setVisibility(_viewVisible);
		mBinding.btnLayoutUntactFloatingButtonNext.setVisibility(_viewVisible);
		mBinding.btnLayoutUntactFloatingButtonDivider.setVisibility(_viewVisible);
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