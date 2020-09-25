package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.mvvmappapplication.utils.ResourceUtil;
import com.example.mvvmappapplication.utils.ThemeUtil;

public class HSUntactImageView extends AppCompatImageView implements ThemeUtil.ITheme {

    @StyleRes
    int mStyle;
    @AttrRes
    int mSrc;
    @AttrRes
    int mBackground;

    public HSUntactImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public HSUntactImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        mStyle = attrs.getStyleAttribute();
        mSrc = ThemeUtil.getAttributeValue(attrs, ATTR_IMAGE_SCR);
        mBackground = ThemeUtil.getAttributeValue(attrs, ATTR_BACKGROUND);
    }

    public void setImageResourceAttr(@AttrRes int resAttrId) {
        mSrc = resAttrId;
        int resId = ResourceUtil.getThemeBackgroundResId(mSrc);
        if (resId != 0) {
            super.setImageResource(resId);
        }
    }

    public void setBackgroundResourceAttr(@AttrRes int resAttrId) {
        mBackground = resAttrId;
        ThemeUtil.updateThemeToBackground(this, mBackground);
    }

    @Override
    public void onChangeTheme() {
        setImageResourceAttr(mSrc);
        setBackgroundResourceAttr(mBackground);
    }
}