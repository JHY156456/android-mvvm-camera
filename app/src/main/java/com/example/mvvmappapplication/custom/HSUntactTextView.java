package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.mvvmappapplication.utils.ResourceUtil;
import com.example.mvvmappapplication.utils.ThemeUtil;


public class HSUntactTextView extends AppCompatTextView implements ThemeUtil.ITheme {

    @StyleRes
    int mStyle;
    @AttrRes
    int mTextColor;
    @AttrRes
    int mBackground;

    public HSUntactTextView(Context context) {
        super(context);
    }

    public HSUntactTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public HSUntactTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
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
        setBackgroundResourceAttr(mBackground);
    }
}