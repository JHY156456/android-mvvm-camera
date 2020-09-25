package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.databinding.LayoutUntactCommonTermTypeBinding;
import com.example.mvvmappapplication.utils.CommonUtil;

public class HSUntactCommonTermType extends LinearLayout {
	private static final String TAG = HSUntactCommonTermType.class.getSimpleName();
	public enum TermType {
		NONE,
		IDENTITY_ALL,
		ACCOUNT_ALL,
		ACCOUNT_CHOICE,
		ACCOUNT_CHOICE_ALL
	}
	private TermType mTermType;

	public void setOnCallbackClickListener(CallbackItemClickListener _callbackItemClickListener, CallbackItemSubRowClickListener _callbackItemSubRowClickListener){
		mCallbackItemClickListener = _callbackItemClickListener;
		mCallbackItemSubRowClickListener = _callbackItemSubRowClickListener;
	}

	private CallbackItemClickListener mCallbackItemClickListener;
	public interface CallbackItemClickListener{
		void clickView(View view);
	}

	private CallbackItemSubRowClickListener mCallbackItemSubRowClickListener;
	public interface CallbackItemSubRowClickListener {
		void clickRowView(int _viewId, boolean _isChecked);
		void clickDrawView(int _viewId, String _text);
	}

	LayoutUntactCommonTermTypeBinding mBinding;

	public HSUntactCommonTermType(Context context) {
		super(context);
		initView();
	}

	public HSUntactCommonTermType(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttributeSet(attrs);
		initView();
	}

	public HSUntactCommonTermType(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		initAttributeSet(attrs);
		initView();
	}

	private void initAttributeSet(AttributeSet attrs) {
		TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.HSNewUntactTermType, 0, 0);
		try {
			int backIconType = a.getInteger(R.styleable.HSNewUntactTermType_termType, TermType.NONE.ordinal());
			for (TermType type : TermType.values()) {
				if (type.ordinal() == backIconType) {
					mTermType = type;
					break;
				}
			}

		} finally {
			a.recycle();
		}
	}



	/***********************************************************************************************************************************/
	// Init View
	/***********************************************************************************************************************************/
	private void initView() {
		mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_untact_common_term_type, this, true);
		mBinding.hsnucvLayoutUntactCommonTermTypeItemBody.setOnClickListener(mOnClickListener);

		View subView = null;
		if (mTermType.equals(TermType.NONE)){
			return;
		}
		else if (mTermType.equals(TermType.IDENTITY_ALL)){
			mBinding.rlLayoutUntactCommonTermTypeContainer1.setVisibility(VISIBLE);
			mBinding.llLayoutUntactCommonTermTypeContainer2.setVisibility(GONE);
			subView = getChildViewFromType(R.layout.layout_untact_common_term_sub_type_1);
			((HSUntactCheckBox)subView.findViewById(R.id.cb_layout_untact_common_term_sub_type_1_term_1)).setOnCheckedChangeListener(mOnCheckedChangeListener);
			((HSUntactCheckBox)subView.findViewById(R.id.cb_layout_untact_common_term_sub_type_1_term_2)).setOnCheckedChangeListener(mOnCheckedChangeListener);
			((HSUntactCheckBox)subView.findViewById(R.id.cb_layout_untact_common_term_sub_type_1_term_3)).setOnCheckedChangeListener(mOnCheckedChangeListener);
			((HSUntactCheckBox)subView.findViewById(R.id.cb_layout_untact_common_term_sub_type_1_term_1)).setDrawableClickListener(mDrawableClickListener);
			((HSUntactCheckBox)subView.findViewById(R.id.cb_layout_untact_common_term_sub_type_1_term_2)).setDrawableClickListener(mDrawableClickListener);
			((HSUntactCheckBox)subView.findViewById(R.id.cb_layout_untact_common_term_sub_type_1_term_3)).setDrawableClickListener(mDrawableClickListener);
		}
		else if (mTermType.equals(TermType.ACCOUNT_ALL)){
			mBinding.rlLayoutUntactCommonTermTypeContainer1.setVisibility(VISIBLE);
			mBinding.llLayoutUntactCommonTermTypeContainer2.setVisibility(GONE);
			subView = getChildViewFromType(R.layout.layout_untact_common_term_sub_type_2);
			((HSUntactCheckBox)subView.findViewById(R.id.cb_layout_untact_common_term_sub_type_2_term_1)).setOnCheckedChangeListener(mOnCheckedChangeListener);
			((HSUntactCheckBox)subView.findViewById(R.id.cb_layout_untact_common_term_sub_type_2_term_2)).setOnCheckedChangeListener(mOnCheckedChangeListener);
			((HSUntactCheckBox)subView.findViewById(R.id.cb_layout_untact_common_term_sub_type_2_term_3)).setOnCheckedChangeListener(mOnCheckedChangeListener);
			((HSUntactTextView)subView.findViewById(R.id.hsntv_layout_untact_common_term_sub_type_2_term_3)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mBinding.flLayoutUntactCommonTermTypeDetailBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_2_term_3).performClick();
				}
			});
			((HSUntactCheckBox)subView.findViewById(R.id.cb_layout_untact_common_term_sub_type_2_term_4)).setOnCheckedChangeListener(mOnCheckedChangeListener);
			((HSUntactCheckBox)subView.findViewById(R.id.cb_layout_untact_common_term_sub_type_2_term_7)).setOnCheckedChangeListener(mOnCheckedChangeListener);
			((HSUntactCheckBox)subView.findViewById(R.id.cb_layout_untact_common_term_sub_type_2_term_1)).setDrawableClickListener(mDrawableClickListener);
			((HSUntactCheckBox)subView.findViewById(R.id.cb_layout_untact_common_term_sub_type_2_term_2)).setDrawableClickListener(mDrawableClickListener);
			((HSUntactCheckBox)subView.findViewById(R.id.cb_layout_untact_common_term_sub_type_2_term_3)).setDrawableClickListener(mDrawableClickListener);
			((HSUntactCheckBox)subView.findViewById(R.id.cb_layout_untact_common_term_sub_type_2_term_4)).setDrawableClickListener(mDrawableClickListener);
			((HSUntactCheckBox)subView.findViewById(R.id.cb_layout_untact_common_term_sub_type_2_term_7)).setDrawableClickListener(mDrawableClickListener);
		}
		else if (mTermType.equals(TermType.ACCOUNT_CHOICE)){
			mBinding.rlLayoutUntactCommonTermTypeContainer1.setVisibility(GONE);
			mBinding.llLayoutUntactCommonTermTypeContainer2.setVisibility(VISIBLE);
			mBinding.flLayoutUntactCommonTermTypeDetailBody.setVisibility(GONE);
			mBinding.hsnucbLayoutUntactCommonTermTypeContainer2Item.setOnClickListener(mOnItemClickListener);
			mBinding.hsnucbLayoutUntactCommonTermTypeContainer2Item.setDrawableClickListener(mDrawableClickListener);
			return;
		}
		else if (mTermType.equals(TermType.ACCOUNT_CHOICE_ALL)){
			mBinding.rlLayoutUntactCommonTermTypeContainer1.setVisibility(VISIBLE);
			mBinding.llLayoutUntactCommonTermTypeContainer2.setVisibility(GONE);
			subView = getChildViewFromType(R.layout.layout_untact_common_term_sub_type_3);
			((HSUntactCheckBox)subView.findViewById(R.id.cb_layout_untact_common_term_sub_type_3_term_1)).setOnCheckedChangeListener(mOnCheckedChangeListener);
			((HSUntactCheckBox)subView.findViewById(R.id.cb_layout_untact_common_term_sub_type_3_term_2)).setOnCheckedChangeListener(mOnCheckedChangeListener);
			((HSUntactCheckBox)subView.findViewById(R.id.cb_layout_untact_common_term_sub_type_3_term_3)).setOnCheckedChangeListener(mOnCheckedChangeListener);
			((HSUntactCheckBox)subView.findViewById(R.id.cb_layout_untact_common_term_sub_type_3_term_4)).setOnCheckedChangeListener(mOnCheckedChangeListener);
			((HSUntactCheckBox)subView.findViewById(R.id.cb_layout_untact_common_term_sub_type_3_term_5)).setOnCheckedChangeListener(mOnCheckedChangeListener);
			((HSUntactCheckBox)subView.findViewById(R.id.cb_layout_untact_common_term_sub_type_3_term_6)).setOnCheckedChangeListener(mOnCheckedChangeListener);
			((HSUntactCheckBox)subView.findViewById(R.id.cb_layout_untact_common_term_sub_type_3_term_7)).setOnCheckedChangeListener(mOnCheckedChangeListener);
		}

		subView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		mBinding.flLayoutUntactCommonTermTypeDetailBody.addView(subView);
	}


	private View getChildViewFromType(int _layout) {
		return LayoutInflater.from(getContext()).inflate(_layout, null);
	}


	/***********************************************************************************************************************************/
	// Get Value
	/***********************************************************************************************************************************/
	public boolean isItemBodySelection(){
		return mBinding.hsnucvLayoutUntactCommonTermTypeItemBody.isSelected();
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
		public void onClick(final View v) {
			v.setSelected(!v.isSelected());
			if (mBinding.flLayoutUntactCommonTermTypeDetailBody.getChildCount() > 0) CommonUtil.animationSpead(mBinding.flLayoutUntactCommonTermTypeDetailBody, v.isSelected(), 300);
			if (!v.isSelected() && mTermType.equals(TermType.IDENTITY_ALL)){
				((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_1_term_1)).setChecked(false);
				((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_1_term_2)).setChecked(false);
				((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_1_term_3)).setChecked(false);
			}
			else if (!v.isSelected() && mTermType.equals(TermType.ACCOUNT_ALL)){
				((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_2_term_1)).setChecked(false);
				((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_2_term_2)).setChecked(false);
				((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_2_term_3)).setChecked(false);
				((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_2_term_4)).setChecked(false);
				((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_2_term_7)).setChecked(false);
			}
			else if (!v.isSelected() && mTermType.equals(TermType.ACCOUNT_CHOICE)){
				mBinding.flLayoutUntactCommonTermTypeDetailBody.setVisibility(GONE);
			}
			else if (!v.isSelected() && mTermType.equals(TermType.ACCOUNT_CHOICE_ALL)){
				((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_3_term_1)).setChecked(false);
				((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_3_term_2)).setChecked(false);
				((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_3_term_3)).setChecked(false);
				((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_3_term_4)).setChecked(false);
				((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_3_term_5)).setChecked(false);
				((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_3_term_6)).setChecked(false);
				((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_3_term_7)).setChecked(false);
			}

			if (mCallbackItemClickListener != null) mCallbackItemClickListener.clickView(v);
		}
	};

	private OnClickListener mOnItemClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mCallbackItemClickListener != null) mCallbackItemClickListener.clickView(v);
		}
	};

	private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton view, boolean isChecked) {
			if (mCallbackItemSubRowClickListener != null) mCallbackItemSubRowClickListener.clickRowView(view.getId(), isChecked);
			if (mTermType.equals(TermType.IDENTITY_ALL)){
				if (((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_1_term_1)).isChecked()
						&& ((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_1_term_2)).isChecked()
						&& ((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_1_term_3)).isChecked()){
					mBinding.hsnucvLayoutUntactCommonTermTypeItemBody.performClick();
				}
			}
			else if (mTermType.equals(TermType.ACCOUNT_ALL)){
				if (((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_2_term_1)).isChecked()
						&& ((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_2_term_2)).isChecked()
						&& ((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_2_term_3)).isChecked()
						&& ((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_2_term_4)).isChecked()
						&& ((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_2_term_7)).isChecked()){
					mBinding.hsnucvLayoutUntactCommonTermTypeItemBody.performClick();
				}
			}
			else if (mTermType.equals(TermType.ACCOUNT_CHOICE)){

			}
			else if (mTermType.equals(TermType.ACCOUNT_CHOICE_ALL)){
				if (((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_3_term_1)).isChecked()
						&& ((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_3_term_2)).isChecked()
						&& ((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_3_term_3)).isChecked()
						&& ((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_3_term_4)).isChecked()
						&& ((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_3_term_5)).isChecked()
						&& ((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_3_term_6)).isChecked()
						&& ((HSUntactCheckBox)mBinding.llLayoutUntactCommonTermTypeBody.findViewById(R.id.cb_layout_untact_common_term_sub_type_3_term_7)).isChecked()){
					mBinding.hsnucvLayoutUntactCommonTermTypeItemBody.performClick();
				}
			}
		}
	};

	private HSUntactCheckBox.DrawableClickListener mDrawableClickListener = new HSUntactCheckBox.DrawableClickListener(){
		@Override
		public void onClick(DrawablePosition target, int id, String text) {
			if (target == DrawablePosition.RIGHT && mCallbackItemSubRowClickListener != null) mCallbackItemSubRowClickListener.clickDrawView(id, text);
		}
	};

}