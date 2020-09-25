package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import androidx.annotation.AttrRes;

import com.example.mvvmappapplication.utils.ThemeUtil;


public class HSUntactRadioGroup extends RadioGroup implements ThemeUtil.ITheme {

    @AttrRes
    int mBackground;

    public HSUntactRadioGroup(Context context) {
        super(context);
    }

    public HSUntactRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        mBackground = ThemeUtil.getAttributeValue(attrs, ATTR_BACKGROUND);
    }

    public void setBackgroundResourceAttr(@AttrRes int resAttrId) {
        mBackground = resAttrId;
        ThemeUtil.updateThemeToBackground(this, mBackground);
    }

    @Override
    public void onChangeTheme() {
        setBackgroundResourceAttr(mBackground);
    }
}