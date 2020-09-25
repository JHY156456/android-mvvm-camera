package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.graphics.Color;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.databinding.DataBindingUtil;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.custom.dto.TableTypeDto;
import com.example.mvvmappapplication.databinding.LayoutUntactCommonTableTypeBinding;
import com.example.mvvmappapplication.ui.BaseActivity;
import com.example.mvvmappapplication.utils.StringUtil;
import com.example.mvvmappapplication.utils.UIUtil;

import java.util.List;


public class HSUntactCommonTableType extends LinearLayout {
	private static final String TAG = HSUntactCommonTableType.class.getSimpleName();

	private CallbackItemClickListener mCallbackItemClickListener;
	public interface CallbackItemClickListener{
		void clickView(View view, int itemPos);
	}

	private CallbackElseClickListener mCallbackElseClickListener;
	public interface CallbackElseClickListener {
		void clickCompanyView(int _companyIndex);
		void clickAdminView(int _adminIndex);
		void clickView(View view, int itemPos);
		void checkedChanged(View view, int itemPos, boolean isChecked);
	}

	private CallbackEditTextValueCheckListner mCallbackEditTextValueCheckListner;
	public interface CallbackEditTextValueCheckListner{
		void valueCheck(TableTypeDto.ValidCheckType _tag, boolean _isPass, String _str);
	}

	LayoutUntactCommonTableTypeBinding mBinding;

	public HSUntactCommonTableType(Context context) {
		super(context);
		initView();
	}

	public HSUntactCommonTableType(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public HSUntactCommonTableType(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_untact_common_table_type, this, true);
	}


	/***********************************************************************************************************************************/
	// Init View
	/***********************************************************************************************************************************/
	public void setTable(List<TableTypeDto> items) {
		setTable(items, null);
	}
	public void setTable(List<TableTypeDto> items, CallbackItemClickListener _callbackItemClickListener) {
		setTable(items, _callbackItemClickListener, null);
	}
	public void setTable(List<TableTypeDto> items, CallbackItemClickListener _callbackItemClickListener, CallbackElseClickListener _callbackElseClickListener) {
		setTable(items, _callbackItemClickListener, _callbackElseClickListener, null);
	}

	public void setTable(List<TableTypeDto> items,
                         CallbackItemClickListener _callbackItemClickListener,
                         CallbackElseClickListener _callbackElseClickListener,
                         CallbackEditTextValueCheckListner _callbackEditTextValueCheckListner) {
		if (items == null) return;
		mCallbackItemClickListener = _callbackItemClickListener;
		mCallbackElseClickListener = _callbackElseClickListener;
		mCallbackEditTextValueCheckListner = _callbackEditTextValueCheckListner;
		View mView;
		for (int i = 0; i < items.size(); i++) {
			mView = getChildViewFromType(items.get(i).typeLayout);
			if (mView != null){
				setTableItemCaseType(mView, items.get(i), i);
				mView.setTag(items.get(i));
				mView.setOnClickListener(mOnClickListener);
				if (items.get(i).isBgChangeDisable) mView.setBackground(null);

				mBinding.llLayoutUntactCommonTableTypeBody.addView(mView);
				if (i < items.size()-1) {
					final View dividerView = getChildViewFromType(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_DIVIDER.getItemViewType());
					dividerView.setTag(new TableTypeDto(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_DIVIDER.getItemViewType()));
					mBinding.llLayoutUntactCommonTableTypeBody.addView(dividerView);
				}
			}
		}
		invalidate();
	}

	private void setTableItemCaseType(final View _view, final TableTypeDto _item, final int _pos){

		/* 공통 Layout */
		final HSUntactCardView mContents = _view.findViewById(R.id.hsnucv_common_table_type_cardview_body);

		/* Touch Area */
		final RelativeLayout unTouchableArea = mContents.findViewById(R.id.rl_common_table_type_untouchable);
		if (unTouchableArea != null){
			if (_item.isSomeAreaSelect) unTouchableArea.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return true;
				}
			});
		}

		switch (_item.typeLayout){
			case R.layout.layout_untact_table_type_content_edittext_line_1_verifcode:
			case R.layout.layout_untact_table_type_content_edittext_line_1:
			case R.layout.layout_untact_table_type_content_edittext_line_2:
				final HSUntactTextView mTitle1 = mContents.findViewById(R.id.hsntv_common_title_type_title);
				final HSUntactEditText mInputField1 = mContents.findViewById(R.id.hsnetv_common_table_type_input);
				final HSUntactImageView mIcon1 = mContents.findViewById(R.id.hsniv_common_table_type_icon);
				/* Title */
				if (mTitle1 != null){
					mTitle1.setText(_item.text1);
					if (_item.isBgChangeDisable) mTitle1.setTextColor(Color.parseColor("#929292"));
				}

				/* Content */
				if (mInputField1 != null){
					mInputField1.setText(_item.text2);
					mInputField1.clearFocus();

					/* Hint */
					mInputField1.setHint(_item.text2Hint);

					/* 기타 및 Formatter 설정 */
					if (_item.validCheckType.equals(TableTypeDto.ValidCheckType.VALID_CHECK_TYPE_NAME)){
						mInputField1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
						mInputField1.setImeOptions(EditorInfo.IME_ACTION_DONE);
						mInputField1.setMaxLines(1);
						mInputField1.addTextChangedListener(mTextWatcherName);
					}
					else if (_item.validCheckType.equals(TableTypeDto.ValidCheckType.VALID_CHECK_TYPE_PHONE)){
						mInputField1.setInputType(InputType.TYPE_CLASS_PHONE);
						mInputField1.addTextChangedListener(mTextWatcherPhone);
					}
					else if (_item.validCheckType.equals(TableTypeDto.ValidCheckType.VALID_CHECK_TYPE_EMAIL)){
						mInputField1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
						mInputField1.setImeOptions(EditorInfo.IME_ACTION_DONE);
						mInputField1.setMaxLines(1);
						mInputField1.setFilters(new InputFilter[] {new InputFilter.LengthFilter(32)});
						mInputField1.addTextChangedListener(mTextWatcherEmail);
					}
					else if (_item.validCheckType.equals(TableTypeDto.ValidCheckType.VALID_CHECK_TYPE_ADDRESS)){
						mInputField1.setInputType(InputType.TYPE_CLASS_TEXT);
						mInputField1.setMaxLines(1);
					}
					else if (_item.validCheckType.equals(TableTypeDto.ValidCheckType.VALID_CHECK_TYPE_ACCOUNT)){
						mInputField1.setInputType(InputType.TYPE_CLASS_NUMBER);
						mInputField1.setMaxLines(1);
						mInputField1.addTextChangedListener(mTextWatcherAccount);
					}
					else if (_item.validCheckType.equals(TableTypeDto.ValidCheckType.VALID_CHECK_TYPE_VERIFCODE)){
						mInputField1.setInputType(InputType.TYPE_CLASS_NUMBER);
						mInputField1.setMaxLines(1);
						mInputField1.addTextChangedListener(mTextWatcherVerifCode);
					}
					else{}
				}

				/* Icon */
				if (mIcon1 != null){
					mIcon1.setVisibility(_item.icon != null?VISIBLE:GONE);
					mIcon1.setBackground(_item.icon);
					mIcon1.setOnClickListener(mOnElseClickListener);
				}
				break;

			case R.layout.layout_untact_table_type_content_secret_keypad:
				break;

			case R.layout.layout_untact_table_type_content_horizontal_scroll_1:
				final HSUntactRadioGroup mMobileCompany = mContents.findViewById(R.id.hsnrg_common_table_type_horizontal_scroll_body);
				mMobileCompany.setOnCheckedChangeListener(mOnCheckedChangeListener);
				((HSUntactHorizontalScrollView)_view.findViewById(R.id.hsv_layout_new_untact_table_type_content_scroll_view)).setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_1) != null) getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_1).performClick();	/* 레이아웃 선택 후 스크롤 가능하게 요청 -> 한번에 터치 가능하게 요청으로 변경되어 수정(20200715) */
						return false;
					}
				});
				break;

			case R.layout.layout_untact_table_type_content_horizontal_scroll_2:
				final HSUntactRadioGroup mAdmin = mContents.findViewById(R.id.hsnrg_common_table_type_horizontal_scroll_body);
				mAdmin.setOnCheckedChangeListener(mOnCheckedChangeListener);
				((HSUntactHorizontalScrollView)_view.findViewById(R.id.hsv_layout_new_untact_table_type_content_scroll_view)).setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_2) != null) getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_2).performClick();	/* 레이아웃 선택 후 스크롤 가능하게 요청 -> 한번에 터치 가능하게 요청으로 변경되어 수정(20200715) */
						return false;
					}
				});
				break;



			case R.layout.layout_untact_table_type_content_switch_with_img:
				final HSUntactImageView mInfo8 = mContents.findViewById(R.id.hsniv_common_title_type_switch_info);
				if (mInfo8 != null){
					mInfo8.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Toast.makeText(getContext(), "??????????????", Toast.LENGTH_SHORT).show();
						}
					});
				}
			case R.layout.layout_untact_table_type_content_switch_base:
				final HSUntactTextView mInputField5 = mContents.findViewById(R.id.hsntv_common_title_type_button_input);
				final LinearLayout mSwitchBody5 = mContents.findViewById(R.id.ll_common_title_type_switch);
				final HSUntactTextView mSwitchText5 = mContents.findViewById(R.id.hsntv_common_title_type_switch_text);
				final ToggleButton mSwitchBtn5 = mContents.findViewById(R.id.tb_common_title_type_swich_btn);
				/* Content */
				if (mInputField5 != null){
					mInputField5.setText(_item.text2);
				}

				/* Toggle Btn */
				if (mSwitchBody5 != null){
					mSwitchBody5.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							mSwitchBtn5.setChecked(!mSwitchBtn5.isChecked());
						}
					});
				}

				/* Toggle Btn */
				if (mSwitchBtn5 != null){
					mSwitchBtn5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							mSwitchText5.setText(isChecked?"예":"아니오");
							if (mCallbackElseClickListener != null) mCallbackElseClickListener.checkedChanged(_view, _pos, isChecked);
						}
					});
				}
				break;

			case R.layout.layout_untact_table_type_content_button_line_2_job:
				final FrameLayout mElse = mContents.findViewById(R.id.fl_layout_untact_common_type_detail_body);
				mElse.addView(getChildViewFromType(R.layout.layout_untact_table_type_content_button_line_2_job_else));
			case R.layout.layout_untact_table_type_content_button_line_2_address:
			case R.layout.layout_untact_table_type_content_button_line_1:
			case R.layout.layout_untact_table_type_content_button_line_2:
				final HSUntactTextView mTitle6 = mContents.findViewById(R.id.hsntv_common_title_type_title);
				final HSUntactTextView mInputField6 = mContents.findViewById(R.id.hsntv_common_title_type_button_input);
				final HSUntactImageView mIcon6 = mContents.findViewById(R.id.hsniv_common_table_type_icon);

				/* Title */
				if (mTitle6 != null){
					mTitle6.setText(_item.text1);
					mTitle6.setVisibility((_item.text1.length() > 0)?VISIBLE:GONE);
					if (_item.isBgChangeDisable) mTitle6.setTextColor(Color.parseColor("#929292"));
				}

				/* Content */
				if (mInputField6 != null){
					mInputField6.setText(_item.text2);
					mInputField6.setHint(_item.text2Hint);
					if (_item.isBgChangeDisable) mInputField6.setTextColor(Color.parseColor("#929292"));
				}

				/* Icon */
				if (mIcon6 != null){
					mIcon6.setVisibility(_item.icon != null?VISIBLE:GONE);
					mIcon6.setBackground(_item.icon);
				}
				break;

			case R.layout.layout_untact_table_type_content_text_line_1_type_1:
			case R.layout.layout_untact_table_type_content_text_line_1_display:
				final HSUntactTextView mTitle12 = mContents.findViewById(R.id.hsntv_common_title_type_title);
				final HSUntactTextView mInputField12 = mContents.findViewById(R.id.hsntv_common_table_type_input);

				/* Title */
				if (mTitle12 != null){
					mTitle12.setText(_item.text1);
					mTitle12.setVisibility((_item.text1.length() > 0)?VISIBLE:GONE);
					if (_item.isBgChangeDisable) mTitle12.setTextColor(Color.parseColor("#929292"));
				}

				/* Content */
				if (mInputField12 != null){
                    mInputField12.setText(_item.text2);
					if (_item.isBgChangeDisable) mInputField12.setTextColor(Color.parseColor("#929292"));
				}
				break;
		}
	}

	private View getChildViewFromType(int _layout) {
		return LayoutInflater.from(getContext()).inflate(_layout, null);
	}

	private boolean isDividerView(View _childView){
		TableTypeDto mTempTableTypeDto = (TableTypeDto)_childView.getTag();
		return (mTempTableTypeDto.typeLayout == TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_DIVIDER.getItemViewType());
	}


	/***********************************************************************************************************************************/
	// View 갱신
	/***********************************************************************************************************************************/
	private void refreshChildView(View _selectedView){
		TableTypeDto mSelectedTableTypeDto = (TableTypeDto)_selectedView.getTag();
		if (_selectedView.isSelected()){
			if (mSelectedTableTypeDto.isOnOff){
				unSelectView(_selectedView);
				invalidate();
			}
			return;
		}

		for (int i = 0; i < mBinding.llLayoutUntactCommonTableTypeBody.getChildCount(); i++) {
			if (isDividerView(mBinding.llLayoutUntactCommonTableTypeBody.getChildAt(i))) continue;

			if (_selectedView == mBinding.llLayoutUntactCommonTableTypeBody.getChildAt(i)){
				selectView(mBinding.llLayoutUntactCommonTableTypeBody.getChildAt(i));
			}
			else{
				unSelectView(mBinding.llLayoutUntactCommonTableTypeBody.getChildAt(i));
			}
		}
		invalidate();
	}

	private void selectView(View _contentView){
		_contentView.setSelected(true);
		TableTypeDto mTempTableTypeDto = (TableTypeDto)_contentView.getTag();

		/* Card Background */
		final HSUntactCardView mContents = _contentView.findViewById(R.id.hsnucv_common_table_type_cardview_body);
		if (mTempTableTypeDto.isBgSizeUp) {
			mContents.setTranslationZ(10);
			mContents.setScaleY(1.02f);
			mContents.setRadius(20);
			mContents.setStrokeWidth(10);
			mContents.setStrokeColor(getResources().getColor(android.R.color.black));
			mContents.setCardElevation(10);
			mContents.setCardBackgroundColor(getResources().getColor(android.R.color.white));
		}

		final HSUntactEditText mInputField = mContents.findViewById(R.id.hsnetv_common_table_type_input);
		if (mInputField != null) {
			UIUtil.showSoftKeyboard(mInputField);
			mInputField.requestFocus();
		}
		else{
			((BaseActivity)getContext()).hideSoftKeyboard();
			clearFocus();
		}
	}

	private void unSelectView(View _contentView){
		_contentView.setSelected(false);
		TableTypeDto mTempTableTypeDto = (TableTypeDto)_contentView.getTag();

		/* Card Background */
		final HSUntactCardView mContents = _contentView.findViewById(R.id.hsnucv_common_table_type_cardview_body);
		mContents.setTranslationZ(0);
		mContents.setScaleY(1f);
		mContents.setRadius(0);
		mContents.setStrokeWidth(0);
		mContents.setStrokeColor(getResources().getColor(android.R.color.transparent));
		mContents.setCardElevation(0);
		mContents.setCardBackgroundColor(getResources().getColor(android.R.color.transparent));

		final HSUntactEditText mInputField = mContents.findViewById(R.id.hsnetv_common_table_type_input);
		if (mInputField != null) mInputField.clearFocus();
	}

	/* 선택되어 있는 있는 상태에서 전후 layout 선택시 */
	public void setSelectionIdx(int moveCount) {
		for (int i = 0; i < mBinding.llLayoutUntactCommonTableTypeBody.getChildCount(); i++) {
			View childview = mBinding.llLayoutUntactCommonTableTypeBody.getChildAt(i);
			if (childview.isSelected()){
				try {
					if (i==0 && moveCount<0) {
						return;
					}
					else if (i==mBinding.llLayoutUntactCommonTableTypeBody.getChildCount()-1 && moveCount>0) {
						return;
					}
					else {
						mBinding.llLayoutUntactCommonTableTypeBody.getChildAt(i+moveCount*2).performClick();
					}
				}catch (Exception e){}
				break;
			}
		}
	}

	/* layout 직접 선택하기 */
	public void setSelectionContentView(View _view) {
		for (int i = 0; i < mBinding.llLayoutUntactCommonTableTypeBody.getChildCount(); i++) {
			if (_view == mBinding.llLayoutUntactCommonTableTypeBody.getChildAt(i)){
				_view.performClick();
				return;
			}
		}
	}

	public void clearSelection() {
		for (int i = 0; i < mBinding.llLayoutUntactCommonTableTypeBody.getChildCount(); i++) {
			if (isDividerView(mBinding.llLayoutUntactCommonTableTypeBody.getChildAt(i))) continue;
			if (mBinding.llLayoutUntactCommonTableTypeBody.getChildAt(i).isSelected()){
				unSelectView(mBinding.llLayoutUntactCommonTableTypeBody.getChildAt(i));
			}
		}
	}

	/***********************************************************************************************************************************/
	// Get
	/***********************************************************************************************************************************/
	public int getSelectionItemIdx(View _view) {
		int selectedIdx = -1;
		for (int i = 0; i < mBinding.llLayoutUntactCommonTableTypeBody.getChildCount(); i++) {
			if (mBinding.llLayoutUntactCommonTableTypeBody.getChildAt(i) == _view){
				selectedIdx = i/2;
				break;
			}
		}
		return selectedIdx;
	}

	public int getSelectionItemIdx() {
		int selectedIdx = -1;
		for (int i = 0; i < mBinding.llLayoutUntactCommonTableTypeBody.getChildCount(); i++) {
			if (isDividerView(mBinding.llLayoutUntactCommonTableTypeBody.getChildAt(i)))  continue;
			if (mBinding.llLayoutUntactCommonTableTypeBody.getChildAt(i).isSelected()){
				selectedIdx = i/2;
				break;
			}
		}
		return selectedIdx;
	}

	public TableTypeDto getSelectionTableTypeDto() {
		for (int i = 0; i < mBinding.llLayoutUntactCommonTableTypeBody.getChildCount(); i++) {
			if (mBinding.llLayoutUntactCommonTableTypeBody.getChildAt(i).isSelected()){
				return (TableTypeDto)mBinding.llLayoutUntactCommonTableTypeBody.getChildAt(i).getTag();
			}
		}
		return null;
	}

	public HSUntactCardView getContentView(int i) {
		if (i >= mBinding.llLayoutUntactCommonTableTypeBody.getChildCount()) return null;
		int childIdx = i*2;
		return (HSUntactCardView) mBinding.llLayoutUntactCommonTableTypeBody.getChildAt(childIdx).findViewById(R.id.hsnucv_common_table_type_cardview_body);
	}

	public View getDividerView(int i) {
		if (i >= mBinding.llLayoutUntactCommonTableTypeBody.getChildCount()) return null;
		int childIdx = i*2+1;
		return mBinding.llLayoutUntactCommonTableTypeBody.getChildAt(childIdx);
	}

	public HSUntactCardView getContentView(TableTypeDto.ItemViewType itemViewType) {
		for (int i = 0; i < mBinding.llLayoutUntactCommonTableTypeBody.getChildCount(); i++) {
			TableTypeDto mTempTableTypeDto = (TableTypeDto)mBinding.llLayoutUntactCommonTableTypeBody.getChildAt(i).getTag();
			if (mTempTableTypeDto.typeLayout  == itemViewType.getItemViewType()){
				return mBinding.llLayoutUntactCommonTableTypeBody.getChildAt(i).findViewById(R.id.hsnucv_common_table_type_cardview_body);
			}
		}
		return null;
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
			refreshChildView(v);
			if (mCallbackItemClickListener != null) {
				mCallbackItemClickListener.clickView(v, getSelectionItemIdx(v));
			}
		}
	};

	private OnClickListener mOnElseClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mCallbackElseClickListener != null) {
				mCallbackElseClickListener.clickView(v, getSelectionItemIdx(v.getRootView().findViewById(R.id.hsnucv_common_table_type_cardview_body)));
			}
		}
	};

	private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId){

				/* Mobile Company */
				case R.id.hsnrb_common_table_horizontal_scrollview_item_skt:
					if (getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_1) != null) getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_1).performClick();	/* 레이아웃 선택 후 스크롤 가능하게 요청 -> 한번에 터치 가능하게 요청으로 변경되어 수정(20200715) */
					if (mCallbackElseClickListener != null) mCallbackElseClickListener.clickCompanyView(0);
					break;

				case R.id.hsnrb_common_table_horizontal_scrollview_item_kt:
					if (getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_1) != null) getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_1).performClick();	/* 레이아웃 선택 후 스크롤 가능하게 요청 -> 한번에 터치 가능하게 요청으로 변경되어 수정(20200715) */
					if (mCallbackElseClickListener != null) mCallbackElseClickListener.clickCompanyView(1);
					break;

				case R.id.hsnrb_common_table_horizontal_scrollview_item_lg:
					if (getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_1) != null) getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_1).performClick();	/* 레이아웃 선택 후 스크롤 가능하게 요청 -> 한번에 터치 가능하게 요청으로 변경되어 수정(20200715) */
					if (mCallbackElseClickListener != null) mCallbackElseClickListener.clickCompanyView(2);
					break;

				case R.id.hsnrb_common_table_horizontal_scrollview_item_skt_s:
					if (getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_1) != null) getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_1).performClick();	/* 레이아웃 선택 후 스크롤 가능하게 요청 -> 한번에 터치 가능하게 요청으로 변경되어 수정(20200715) */
					if (mCallbackElseClickListener != null) mCallbackElseClickListener.clickCompanyView(3);
					break;

				case R.id.hsnrb_common_table_horizontal_scrollview_item_kt_s:
					if (getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_1) != null) getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_1).performClick();	/* 레이아웃 선택 후 스크롤 가능하게 요청 -> 한번에 터치 가능하게 요청으로 변경되어 수정(20200715) */
					if (mCallbackElseClickListener != null) mCallbackElseClickListener.clickCompanyView(4);
					break;

				case R.id.hsnrb_common_table_horizontal_scrollview_item_lg_s:
					if (getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_1) != null) getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_1).performClick();	/* 레이아웃 선택 후 스크롤 가능하게 요청 -> 한번에 터치 가능하게 요청으로 변경되어 수정(20200715) */
					if (mCallbackElseClickListener != null) mCallbackElseClickListener.clickCompanyView(5);
					break;


					/* Admin */
				case R.id.hsnrb_common_table_horizontal_scrollview_item_admin_1:
					if (getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_2) != null) getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_2).performClick();	/* 레이아웃 선택 후 스크롤 가능하게 요청 -> 한번에 터치 가능하게 요청으로 변경되어 수정(20200715) */
					if (mCallbackElseClickListener != null) mCallbackElseClickListener.clickAdminView(0);
					break;

				case R.id.hsnrb_common_table_horizontal_scrollview_item_admin_2:
					if (getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_2) != null) getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_2).performClick();	/* 레이아웃 선택 후 스크롤 가능하게 요청 -> 한번에 터치 가능하게 요청으로 변경되어 수정(20200715) */
					if (mCallbackElseClickListener != null) mCallbackElseClickListener.clickAdminView(1);
					break;

				case R.id.hsnrb_common_table_horizontal_scrollview_item_admin_3:
					if (getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_2) != null) getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_2).performClick();	/* 레이아웃 선택 후 스크롤 가능하게 요청 -> 한번에 터치 가능하게 요청으로 변경되어 수정(20200715) */
					if (mCallbackElseClickListener != null) mCallbackElseClickListener.clickAdminView(2);
					break;

				case R.id.hsnrb_common_table_horizontal_scrollview_item_admin_4:
					if (getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_2) != null) getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_2).performClick();	/* 레이아웃 선택 후 스크롤 가능하게 요청 -> 한번에 터치 가능하게 요청으로 변경되어 수정(20200715) */
					if (mCallbackElseClickListener != null) mCallbackElseClickListener.clickAdminView(3);
					break;

				case R.id.hsnrb_common_table_horizontal_scrollview_item_admin_5:
					if (getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_2) != null) getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_2).performClick();	/* 레이아웃 선택 후 스크롤 가능하게 요청 -> 한번에 터치 가능하게 요청으로 변경되어 수정(20200715) */
					if (mCallbackElseClickListener != null) mCallbackElseClickListener.clickAdminView(4);
					break;

				case R.id.hsnrb_common_table_horizontal_scrollview_item_admin_6:
					if (getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_2) != null) getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_2).performClick();	/* 레이아웃 선택 후 스크롤 가능하게 요청 -> 한번에 터치 가능하게 요청으로 변경되어 수정(20200715) */
					if (mCallbackElseClickListener != null) mCallbackElseClickListener.clickAdminView(5);
					break;

				case R.id.hsnrb_common_table_horizontal_scrollview_item_admin_7:
					if (getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_2) != null) getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_2).performClick();	/* 레이아웃 선택 후 스크롤 가능하게 요청 -> 한번에 터치 가능하게 요청으로 변경되어 수정(20200715) */
					if (mCallbackElseClickListener != null) mCallbackElseClickListener.clickAdminView(6);
					break;

				case R.id.hsnrb_common_table_horizontal_scrollview_item_admin_8:
					if (getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_2) != null) getContentView(TableTypeDto.ItemViewType.ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_2).performClick();	/* 레이아웃 선택 후 스크롤 가능하게 요청 -> 한번에 터치 가능하게 요청으로 변경되어 수정(20200715) */
					if (mCallbackElseClickListener != null) mCallbackElseClickListener.clickAdminView(7);
					break;

			}
		}
	};

	TextWatcher mTextWatcherName = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			String str = s.toString().trim();
			if (str.length() >= 2){
				if (mCallbackEditTextValueCheckListner != null) mCallbackEditTextValueCheckListner.valueCheck(TableTypeDto.ValidCheckType.VALID_CHECK_TYPE_NAME, true, str);
			}
			else{
				if (mCallbackEditTextValueCheckListner != null) mCallbackEditTextValueCheckListner.valueCheck(TableTypeDto.ValidCheckType.VALID_CHECK_TYPE_NAME, true, str);
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

	PhoneNumberFormattingTextWatcher mTextWatcherPhone = new PhoneNumberFormattingTextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			super.beforeTextChanged(s, start, count, after);
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			super.onTextChanged(s, start, before, count);
		}

		@Override
		public void afterTextChanged(Editable s) {
			super.afterTextChanged(s);
			if (StringUtil.isCellPhone(s.toString())) {
				if (mCallbackEditTextValueCheckListner != null) mCallbackEditTextValueCheckListner.valueCheck(TableTypeDto.ValidCheckType.VALID_CHECK_TYPE_PHONE, true, s.toString());
			} else {
				if (mCallbackEditTextValueCheckListner != null) mCallbackEditTextValueCheckListner.valueCheck(TableTypeDto.ValidCheckType.VALID_CHECK_TYPE_PHONE, false, s.toString());
			}
		}
	};

	TextWatcher mTextWatcherEmail = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			String str = s.toString().trim();
			if (StringUtil.isEmail(str)) {
				if (mCallbackEditTextValueCheckListner != null) mCallbackEditTextValueCheckListner.valueCheck(TableTypeDto.ValidCheckType.VALID_CHECK_TYPE_EMAIL, true, s.toString());
			}
			else{
				if (mCallbackEditTextValueCheckListner != null) mCallbackEditTextValueCheckListner.valueCheck(TableTypeDto.ValidCheckType.VALID_CHECK_TYPE_EMAIL, false, s.toString());
			}
		}
	};

	TextWatcher mTextWatcherAccount = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			String str = s.toString().trim();
			if (str.length() > 0) {
				if (mCallbackEditTextValueCheckListner != null) mCallbackEditTextValueCheckListner.valueCheck(TableTypeDto.ValidCheckType.VALID_CHECK_TYPE_ACCOUNT, true, s.toString());
			}
			else{
				if (mCallbackEditTextValueCheckListner != null) mCallbackEditTextValueCheckListner.valueCheck(TableTypeDto.ValidCheckType.VALID_CHECK_TYPE_ACCOUNT, false, s.toString());
			}
		}
	};

	TextWatcher mTextWatcherVerifCode= new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			String str = s.toString().trim();
			if (str.length() == 3) {
				if (mCallbackEditTextValueCheckListner != null) mCallbackEditTextValueCheckListner.valueCheck(TableTypeDto.ValidCheckType.VALID_CHECK_TYPE_VERIFCODE, true, s.toString());
			}
			else{
				if (mCallbackEditTextValueCheckListner != null) mCallbackEditTextValueCheckListner.valueCheck(TableTypeDto.ValidCheckType.VALID_CHECK_TYPE_VERIFCODE, false, s.toString());
			}
		}
	};
}