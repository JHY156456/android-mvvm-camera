package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.DimenRes;
import androidx.annotation.StringRes;
import androidx.core.widget.TextViewCompat;


import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.utils.UIUtil;

import static android.util.TypedValue.COMPLEX_UNIT_PX;

/**
 *
 * @author tykim
 * @since 2018-08-16
 */
public class HSHorizonTalTitleValueTextView extends LinearLayout {
	HSTextView textViewTitle;
	HSTextView textViewValue;
	HSView mUnderlineV;

	public HSHorizonTalTitleValueTextView(Context context) {
		super(context);
		initView();
	}

	public HSHorizonTalTitleValueTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
        getAttrs(attrs, 0);
	}

	public HSHorizonTalTitleValueTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
		getAttrs(attrs, defStyle);
	}

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_title_value_textview, this, true);

        textViewTitle = findViewById(R.id.title);
        textViewValue = findViewById(R.id.value);
        mUnderlineV = findViewById(R.id.underline);
    }

	private void getAttrs(AttributeSet attrs, int defStyle) {
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleValueTextView, defStyle, 0);
		setTypeArray(typedArray);
	}

	private void setTypeArray(TypedArray typedArray) {
		try {
			String title = typedArray.getString(R.styleable.TitleValueTextView_titleText);
			textViewTitle.setText(title != null ? title : "");
			String value = typedArray.getString(R.styleable.TitleValueTextView_valueText);
			textViewValue.setText(value != null ? value : "");

			float titleWidth = typedArray.getDimension(R.styleable.TitleValueTextView_titleWidth, 0);
			textViewTitle.getLayoutParams().width = (int) titleWidth;

			float titleTextSize = typedArray.getDimension(R.styleable.TitleValueTextView_titleSize, UIUtil.getPxFromDp(R.dimen.dimen_14));
			if (titleTextSize > 0) {
				textViewTitle.setTextSize(COMPLEX_UNIT_PX, titleTextSize);
			}
			float valueTextSize = typedArray.getDimension(R.styleable.TitleValueTextView_valueSize, UIUtil.getPxFromDp(R.dimen.dimen_16));
			if (valueTextSize > 0) {
				textViewValue.setTextSize(COMPLEX_UNIT_PX, valueTextSize);
				TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(textViewValue, 4, (int) valueTextSize, 2, TypedValue.COMPLEX_UNIT_PX);
			}

			int titleColor = typedArray.getColor(R.styleable.TitleValueTextView_titleColor, 0);
			if (titleColor != 0) textViewTitle.setTextColor(titleColor);
			int valueColor = typedArray.getColor(R.styleable.TitleValueTextView_valueColor, 0);
			if (valueColor != 0) textViewValue.setTextColor(valueColor);

			boolean titleStyleBold = typedArray.getBoolean(R.styleable.TitleValueTextView_titleStyleBold, false);
			if (titleStyleBold) {
				textViewTitle.setTypeface(Typekit.getInstance().get(Typekit.Style.Bold));
			}
			boolean valueStyleNumber = typedArray.getBoolean(R.styleable.TitleValueTextView_valueStyleNormal, false);
			if (valueStyleNumber) {
				textViewValue.setTypeface(Typekit.getInstance().get(Typekit.Style.Normal));
			}
			boolean valueStyleBold = typedArray.getBoolean(R.styleable.TitleValueTextView_valueStyleBold, false);
			if (valueStyleBold) {
				textViewValue.setTypeface(Typekit.getInstance().get(Typekit.Style.Bold));
			}
			boolean valueStyleItalic = typedArray.getBoolean(R.styleable.TitleValueTextView_valueStyleItalic, false);
			if (valueStyleItalic) {
				textViewValue.setTypeface(Typekit.getInstance().get(Typekit.Style.Italic));
			}

            int valueGravity = typedArray.getInt(R.styleable.TitleValueTextView_valueGravity, Gravity.CENTER_VERTICAL | Gravity.END);
            textViewValue.setGravity(valueGravity);

			int valueMaxLines = typedArray.getInteger(R.styleable.TitleValueTextView_valueMaxLines, 1);
			textViewValue.setMaxLines(valueMaxLines);

			boolean isUnderline = typedArray.getBoolean(R.styleable.TitleValueTextView_underline, true);
			setUnderLineVisibility(isUnderline);

            float horizontalPadding = typedArray.getDimension(R.styleable.TitleValueTextView_horizontalPadding, 0);
            if (horizontalPadding > 0) {
                textViewTitle.setPadding((int) horizontalPadding, 0, 0, 0);
                textViewValue.setPadding(0, 0, (int) horizontalPadding, 0);
            }
		} catch (Exception e) {
		} finally {
			typedArray.recycle();
		}
	}

	public void setTitleText(@StringRes int text) {
		textViewTitle.setText(text);
	}

	public void setTitleText(CharSequence text) {
		textViewTitle.setText(text);
	}

	public void setValueText(CharSequence text) {
		textViewValue.setText(text);
	}

	public CharSequence getTitleText() { return textViewTitle.getText();}

	public CharSequence getValueText() { return textViewValue.getText();}

	public HSTextView getTitleView() {
		return textViewTitle;
	}

	public HSTextView getValueView() {
		return textViewValue;
	}

	public void setTitleSize(@DimenRes int dimenId) {
		textViewTitle.setTextSize(COMPLEX_UNIT_PX, UIUtil.getPxFromDp(dimenId));
	}

	public void setValueSize(@DimenRes int dimenId) {
		int valueTextSize = UIUtil.getPxFromDp(dimenId);
		textViewValue.setTextSize(COMPLEX_UNIT_PX, valueTextSize);
		TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(textViewValue, 4, valueTextSize, 2, TypedValue.COMPLEX_UNIT_PX);
	}

	public void setTitleColor(@AttrRes int color) {
		textViewTitle.setTextColorAttr(color);
	}

	public void setValueColor(@AttrRes int color) {
		textViewValue.setTextColorAttr(color);
	}

	public void setTypeface(Typekit.Style style) {
		textViewValue.setTypeface(Typekit.getInstance().get(style));
	}

	public void setUnderLineVisibility(boolean isVisible) {
		mUnderlineV.setVisibility(isVisible ? View.VISIBLE : View.GONE);
	}

	public void setHorizontalPadding(@DimenRes int dimenId) {
		textViewTitle.setPadding(UIUtil.getPxFromDp(dimenId), 0, 0, 0);
		textViewValue.setPadding(0, 0, UIUtil.getPxFromDp(dimenId), 0);
	}
}
