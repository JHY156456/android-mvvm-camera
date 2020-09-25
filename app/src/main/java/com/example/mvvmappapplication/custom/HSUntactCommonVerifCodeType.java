package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.databinding.LayoutUntactCommonVerifcodeTypeBodyBinding;
import com.example.mvvmappapplication.utils.AlertUtil;
import com.example.mvvmappapplication.utils.UIUtil;


public class HSUntactCommonVerifCodeType extends LinearLayout {
	private static final String TAG = HSUntactCommonVerifCodeType.class.getSimpleName();

	public enum VerificationCodeStep {
		STEP1,	// 버튼 인증번호 요청 창
		STEP2,	// 인증번호 입력 창
	}

	private CallbackItemClickListener mCallbackItemClickListener;
	public interface CallbackItemClickListener{
		void clickView(View view);
	}
	public void setOnCallbackItemClickListener(CallbackItemClickListener _callbackItemClickListener){
		mCallbackItemClickListener = _callbackItemClickListener;
	}

	LayoutUntactCommonVerifcodeTypeBodyBinding mBinding;

	public HSUntactCommonVerifCodeType(Context context) {
		super(context);
		initView();
	}

	public HSUntactCommonVerifCodeType(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public HSUntactCommonVerifCodeType(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		initView();
	}


	/***********************************************************************************************************************************/
	// Init View
	/***********************************************************************************************************************************/
	private void initView() {
		mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_untact_common_verifcode_type_body, this, true);
		mBinding.hsnetvLayoutUntactCommonVerifcoeTypeBodyLayer2InputBodyInput.setInputType(InputType.TYPE_CLASS_NUMBER);
		mBinding.hsnetvLayoutUntactCommonVerifcoeTypeBodyLayer2InputBodyInput.setMaxLines(1);
		mBinding.hsnetvLayoutUntactCommonVerifcoeTypeBodyLayer2InputBodyInput.setImeOptions(EditorInfo.IME_ACTION_DONE);
		mBinding.hsnetvLayoutUntactCommonVerifcoeTypeBodyLayer2InputBodyInput.addTextChangedListener(mTextWatcherName);

		mBinding.hsnetvLayoutUntactCommonVerifcoeTypeBodyLayer2InputBodyInput.setOnClickListener(mOnClickListener);
		mBinding.btnLayoutUntactCommonVerifcoeTypeBodyLayer1Request.setOnClickListener(mOnClickListener);
		mBinding.flLayoutUntactCommonVerifcoeTypeBodyLayer2InputBody.setOnClickListener(mOnClickListener);
		mBinding.btnLayoutUntactCommonVerifcoeTypeBodyLayer2InputBodyConfirm.setOnClickListener(mOnClickListener);

		refreshStep(VerificationCodeStep.STEP1);
	}



	/***********************************************************************************************************************************/
	// private
	/***********************************************************************************************************************************/
	public void refreshStep(VerificationCodeStep _verificationCodeStep){
		if (_verificationCodeStep.equals(VerificationCodeStep.STEP1)){
			mBinding.btnLayoutUntactCommonVerifcoeTypeBodyLayer1Request.setVisibility(VISIBLE);
			mBinding.llLayoutUntactCommonVerifcoeTypeBodyLayer2.setVisibility(GONE);
			mBinding.flLayoutUntactCommonVerifcoeTypeBodyLayer2InputBody.setSelected(false);
			mBinding.hsnetvLayoutUntactCommonVerifcoeTypeBodyLayer2InputBodyInput.clearFocus();
			mBinding.hsntvLayoutUntactCommonVerifcoeTypeBodyLayer2InputBodyTimer.setText("00:00");
		}
		else if (_verificationCodeStep.equals(VerificationCodeStep.STEP2)){
			mBinding.btnLayoutUntactCommonVerifcoeTypeBodyLayer1Request.setVisibility(GONE);
			mBinding.llLayoutUntactCommonVerifcoeTypeBodyLayer2.setVisibility(VISIBLE);
			mBinding.flLayoutUntactCommonVerifcoeTypeBodyLayer2InputBody.setSelected(true);
			mBinding.hsnetvLayoutUntactCommonVerifcoeTypeBodyLayer2InputBodyInput.requestFocus();
			mBinding.hsntvLayoutUntactCommonVerifcoeTypeBodyLayer2InputBodyTimer.setText("00:00");
		}
	}


	/***********************************************************************************************************************************/
	// Getter
	/***********************************************************************************************************************************/
	public VerificationCodeStep getCurrentStep(){
		return (mBinding.btnLayoutUntactCommonVerifcoeTypeBodyLayer1Request.getVisibility() == VISIBLE)? VerificationCodeStep.STEP1: VerificationCodeStep.STEP2;
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
			switch (v.getId()){
				case R.id.btn_layout_untact_common_verifcoe_type_body_layer_1_request:
					break;

				case R.id.fl_layout_untact_common_verifcoe_type_body_layer_2_input_body:
					mBinding.flLayoutUntactCommonVerifcoeTypeBodyLayer2InputBody.setSelected(true);
					UIUtil.showSoftKeyboard(mBinding.hsnetvLayoutUntactCommonVerifcoeTypeBodyLayer2InputBodyInput);
					mBinding.hsnetvLayoutUntactCommonVerifcoeTypeBodyLayer2InputBodyInput.requestFocus();
					break;

				case R.id.btn_layout_untact_common_verifcoe_type_body_layer_2_input_body_confirm:
					break;
			}
			if (mCallbackItemClickListener != null) mCallbackItemClickListener.clickView(v);
		}
	};


	private TextWatcher mTextWatcherName = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			String str = s.toString().trim();
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};



	/***********************************************************************************************************************************/
	// Timer
	/***********************************************************************************************************************************/
	public CountDownTimer mCountDown = null;
	/**
	 * 카운트다운 타이머 종료
	 */
	public void endCountDownAction() {
		mBinding.hsntvLayoutUntactCommonVerifcoeTypeBodyLayer2InputBodyTimer.setText("03:00");
		if (mCountDown != null) {
			mCountDown.cancel();
			mCountDown = null;
		}
	}

	/**
	 * 카운트다운 타이머 실행
	 */
	public void startCountDownAction() {
		endCountDownAction();

		mCountDown = new CountDownTimer(1000 *60 * 3, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				String minute = String.valueOf(millisUntilFinished / (1000 * 60));
				String second = String.valueOf(millisUntilFinished % (1000 * 60) / 1000);
				if (minute.length() == 1) {
					minute = "0" + minute;
				}
				if (second.length() == 1) {
					second = "0" + second;
				}
				mBinding.hsntvLayoutUntactCommonVerifcoeTypeBodyLayer2InputBodyTimer.setText(minute + ":" + second);
			}

			@Override
			public void onFinish() {
				mBinding.hsntvLayoutUntactCommonVerifcoeTypeBodyLayer2InputBodyTimer.setText("00:00");
				mBinding.hsnetvLayoutUntactCommonVerifcoeTypeBodyLayer2InputBodyInput.setText("");
				AlertUtil.alert(getContext(), "인증번호 입력시간 초과", "인증번호 입력시간이 초과되었습니다.\n다시 시도해주세요.");
			}
		}.start();
	}
}