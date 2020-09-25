package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.appcompat.widget.AppCompatButton;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.utils.ResourceUtil;
import com.example.mvvmappapplication.utils.ThemeUtil;


public class HSUntactButton extends AppCompatButton implements ThemeUtil.ITheme {

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

    public HSUntactButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public HSUntactButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
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
            mTextColor = R.attr.buttonTextColor;
        }
        setTextColorAttr(mTextColor);
        if (mBackground == 0) {
            mBackground = R.attr.buttonBackground;
        }
        setBackgroundResourceAttr(mBackground);
        setCompoundDrawablesWithIntrinsicBoundsAttr(mDrawableLeft, mDrawableTop, mDrawableRight, mDrawableBottom);
    }
}