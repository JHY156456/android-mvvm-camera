package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.StyleRes;

import com.example.mvvmappapplication.utils.ThemeUtil;


public class HSView extends View implements ThemeUtil.ITheme {

    @StyleRes
    int mStyle;
    @AttrRes
    int mBackground;

    public HSView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public HSView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        mStyle = attrs.getStyleAttribute();
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