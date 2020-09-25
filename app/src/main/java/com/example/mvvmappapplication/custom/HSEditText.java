package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.widget.AppCompatEditText;

import com.example.mvvmappapplication.utils.LogUtil;
import com.example.mvvmappapplication.utils.ResourceUtil;
import com.example.mvvmappapplication.utils.ThemeUtil;


public class HSEditText extends AppCompatEditText implements ThemeUtil.ITheme {

    @StyleRes
    int mStyle;
    @AttrRes
    int mTextColor;
    @AttrRes
    int mBackground;
    @AttrRes
    int mDrawableLeft;
    @AttrRes
    int mDrawableTop;
    @AttrRes
    int mDrawableRight;
    @AttrRes
    int mDrawableBottom;

    public HSEditText(Context context) {
        super(context);
    }

    public HSEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public HSEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        mStyle = attrs.getStyleAttribute();
        mTextColor = ThemeUtil.getAttributeValue(attrs, ATTR_TEXT_COLOR);
        mBackground = ThemeUtil.getAttributeValue(attrs, ATTR_BACKGROUND);
        mDrawableLeft = ThemeUtil.getAttributeValue(attrs, ATTR_DRAWABLE_LEFT);
        mDrawableTop = ThemeUtil.getAttributeValue(attrs, ATTR_DRAWABLE_TOP);
        mDrawableRight = ThemeUtil.getAttributeValue(attrs, ATTR_DRAWABLE_RIGHT);
        mDrawableBottom = ThemeUtil.getAttributeValue(attrs, ATTR_DRAWABLE_BOTTOM);
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

    public void setCompoundDrawablesWithIntrinsicBoundsAttr(
            @AttrRes int leftResAttrId, @AttrRes int topResAttrId, @AttrRes int rightResAttrId, @AttrRes int bottomResAttrId) {
        mDrawableLeft = leftResAttrId;
        mDrawableTop = topResAttrId;
        mDrawableRight = rightResAttrId;
        mDrawableBottom = bottomResAttrId;
        Drawable drawableLeft = null;
        Drawable drawableTop = null;
        Drawable drawableRight = null;
        Drawable drawableBottom = null;
        if (leftResAttrId != 0) {
            drawableLeft = ResourceUtil.getThemeDrawable(leftResAttrId);
        }
        if (topResAttrId != 0) {
            drawableTop = ResourceUtil.getThemeDrawable(topResAttrId);
        }
        if (rightResAttrId != 0) {
            drawableRight = ResourceUtil.getThemeDrawable(rightResAttrId);
        }
        if (bottomResAttrId != 0) {
            drawableBottom = ResourceUtil.getThemeDrawable(bottomResAttrId);
        }
        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

    @Override
    public void onChangeTheme() {
        if (mTextColor == 0) {
            mTextColor = android.R.attr.textColor;
        }
        setTextColorAttr(mTextColor);
        setHintTextColor(ResourceUtil.getThemeColorResId(android.R.attr.textColorHint));
        setBackgroundResourceAttr(mBackground);
        setCompoundDrawablesWithIntrinsicBoundsAttr(mDrawableLeft, mDrawableTop, mDrawableRight, mDrawableBottom);
    }
}