package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.utils.LogUtil;
import com.example.mvvmappapplication.utils.ResourceUtil;
import com.example.mvvmappapplication.utils.ThemeUtil;

/**
 * XIcon 표시
 */
public class HSEditTextXIcon extends AppCompatEditText implements ThemeUtil.ITheme, TextWatcher, View.OnTouchListener, View.OnFocusChangeListener {
    private Drawable clearDrawable;
    private OnFocusChangeListener onFocusChangeListener;
    private OnTouchListener onTouchListener;

    @StyleRes
    int mStyle;
    @AttrRes
    int mTextColor;
    @AttrRes
    int mBackground;

    public HSEditTextXIcon(Context context) {
        super(context);
        if(!isInEditMode())
            init();
    }

    public HSEditTextXIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        if(!isInEditMode())
            init();
    }

    public HSEditTextXIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        if(!isInEditMode())
            init();
    }

    private void initAttrs(AttributeSet attrs) {
        mStyle = attrs.getStyleAttribute();
        mTextColor = ThemeUtil.getAttributeValue(attrs, ATTR_TEXT_COLOR);
        mBackground = ThemeUtil.getAttributeValue(attrs, ATTR_BACKGROUND);
    }

    public void setTextColorAttr(@AttrRes int resAttrId) {
        try {
            mTextColor = resAttrId;
            ColorStateList resId = ResourceUtil.getThemeColorStateList(mTextColor);
            if (resId != null) {
                super.setTextColor(resId);
            }
        } catch (Exception e) {
            LogUtil.e("[THEME] " + e.toString());
        }
    }

    public void setBackgroundResourceAttr(@AttrRes int resAttrId) {
        mBackground = resAttrId;
        ThemeUtil.updateThemeToBackground(this, mBackground);
    }

    @Override
    public void onChangeTheme() {
        if (mTextColor == 0) {
            mTextColor = android.R.attr.textColor;
        }
        setTextColorAttr(mTextColor);
        setHintTextColor(ResourceUtil.getThemeColorResId(android.R.attr.textColorHint));
        setBackgroundResourceAttr(mBackground);
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }

    @Override
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }
    private void init() {

        Drawable tempDrawable = ContextCompat.getDrawable(getContext(), ResourceUtil.getThemeDrawableResId(R.attr.btn_text_del));
        clearDrawable = DrawableCompat.wrap(tempDrawable);

//        DrawableCompat.setTintList(clearDrawable, getHintTextColors());
        clearDrawable.setBounds(0, 0, clearDrawable.getIntrinsicWidth(), clearDrawable.getIntrinsicHeight());
        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }

        if (onFocusChangeListener != null) {
            onFocusChangeListener.onFocusChange(v, hasFocus);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        final int x = (int) motionEvent.getX();
        if (clearDrawable.isVisible() && x > getWidth() - getPaddingRight() - clearDrawable.getIntrinsicWidth()) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                setError(null);
                setText(null);
            }
            return true;
        }

        if (onTouchListener != null) {
            return onTouchListener.onTouch(v, motionEvent);
        } else {
            return false;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isFocused()) {
            setClearIconVisible(s.length() > 0);
        }
    }

    private void setClearIconVisible(boolean visible) {
        clearDrawable.setVisible(visible, false);
        setCompoundDrawables(null, null, visible ? clearDrawable : null, null);
    }


}
