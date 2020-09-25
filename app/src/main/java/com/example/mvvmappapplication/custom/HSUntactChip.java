package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.AttrRes;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.utils.LogUtil;
import com.example.mvvmappapplication.utils.ResourceUtil;
import com.example.mvvmappapplication.utils.ThemeUtil;
import com.example.mvvmappapplication.utils.UIUtil;
import com.google.android.material.chip.Chip;



public class HSUntactChip extends Chip implements ThemeUtil.ITheme {

    @AttrRes
    int mTextColor;
    @AttrRes
    int mChipStrokeColor;
    @AttrRes
    int mChipBackground;

    public HSUntactChip(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public HSUntactChip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        mTextColor = ThemeUtil.getAttributeValue(attrs, ATTR_TEXT_COLOR);
        mChipStrokeColor = ThemeUtil.getAttributeValueCustom(attrs, "chipStrokeColor");
        mChipBackground = ThemeUtil.getAttributeValueCustom(attrs, "chipBackgroundColor");

        int[] systemAttrs = {android.R.attr.textSize};
        TypedArray a = getContext().obtainStyledAttributes(attrs, systemAttrs);
        int textSize = a.getDimensionPixelSize(0, UIUtil.getPxFromDp(R.dimen.dimen_12));
        a.recycle();
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    public void setTextColorAttr(@AttrRes int resAttrId) {
        mTextColor = resAttrId;
        try {
            ColorStateList resId = ResourceUtil.getThemeColorStateList(mTextColor);
            if (resId != null) {
                super.setTextColor(resId);
            }
        } catch (Exception e) {
            LogUtil.e("[THEME] " + e.toString());
        }
    }

    public void setChipStrokeColorAttr(@AttrRes int resAttrId) {
        mChipStrokeColor = resAttrId;
        try {
            ColorStateList resId = ResourceUtil.getThemeColorStateList(mChipStrokeColor);
            if (resId != null) {
                super.setChipStrokeColor(resId);
            }
        } catch (Exception e) {
            LogUtil.e("[THEME] " + e.toString());
        }
    }


    public void setChipBackgroundColorAttr(@AttrRes int resAttrId) {
        mChipBackground = resAttrId;
        try {
            ColorStateList resId = ResourceUtil.getThemeColorStateList(mChipBackground);
            if (resId != null) {
                super.setChipBackgroundColor(resId);
            }
        } catch (Exception e) {
            LogUtil.e("[THEME] " + e.toString());
        }
    }

    @Override
    public void onChangeTheme() {
        if (mTextColor == 0) {
            mTextColor = android.R.attr.textColor;
        }
        setTextColorAttr(mTextColor);
        if (mChipStrokeColor != 0) {
            setChipStrokeColorAttr(mChipBackground);
        }
        if (mChipBackground != 0) {
            setChipBackgroundColorAttr(mChipBackground);
        }
    }
}