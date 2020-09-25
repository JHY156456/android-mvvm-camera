package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.AttrRes;

import com.example.mvvmappapplication.utils.ThemeUtil;


public class HSRelativeLayout extends RelativeLayout implements ThemeUtil.ITheme {

    @AttrRes
    int mBackground;

    public HSRelativeLayout(Context context) {
        super(context);
    }

    public HSRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public HSRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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