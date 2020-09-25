package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.example.mvvmappapplication.utils.LogUtil;
import com.example.mvvmappapplication.utils.ResourceUtil;
import com.example.mvvmappapplication.utils.ThemeUtil;


public class HSUntactRadioButton extends AppCompatRadioButton implements ThemeUtil.ITheme {

    @AttrRes
    int mTextColor;
    @AttrRes
    int mBackground;

    public HSUntactRadioButton(Context context) {
        super(context);
    }

    public HSUntactRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public HSUntactRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
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
        setTextColorAttr(mTextColor);
        setBackgroundResourceAttr(mBackground);
    }
}