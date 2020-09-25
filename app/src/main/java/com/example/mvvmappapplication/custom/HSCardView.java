package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.utils.ResourceUtil;
import com.example.mvvmappapplication.utils.ThemeUtil;
import com.example.mvvmappapplication.utils.UIUtil;
import com.google.android.material.card.MaterialCardView;


/**
 * MaterialCardView (공통)
 *
 * @author ehjung
 */
public class HSCardView extends MaterialCardView implements ThemeUtil.ITheme {

    @AttrRes
    int mStrokeColor = R.attr.cardStrockColor;
    @AttrRes
    int mCardBackground = R.attr.cardBackground;

    public HSCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public HSCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        int cornerRadius = ThemeUtil.getAttributeValueCustom(attrs, "cardCornerRadius");
        if (cornerRadius == 0) {
            setRadius(UIUtil.getPxFromDp(R.dimen.dimen_10)); // default
        }

        int strokeColor = ThemeUtil.getAttributeValueCustom(attrs, "strokeColor");
        int strokeWidth = ThemeUtil.getAttributeValueCustom(attrs, "strokeWidth");
        if (strokeColor == 0 && strokeWidth != 0) {
            setStrokeColorAttr(mStrokeColor);
        }

        int cardBackgroundColor = ThemeUtil.getAttributeValueCustom(attrs, "cardBackgroundColor");
        if (cardBackgroundColor == 0) {
            setCardBackgroundColorAttr(mCardBackground);
        }

        int cardElevation = ThemeUtil.getAttributeValueCustom(attrs, "cardElevation");
        if (cardElevation == 0) {
            setCardElevation(0);
        } else {
            setCardElevation(UIUtil.getPxFromDp(cardElevation));
        }
    }

    public void setStrokeColorAttr(@AttrRes int resAttrId) {
        mStrokeColor = resAttrId;
        try {
            int resId = ResourceUtil.getThemeColorResId(mStrokeColor);
            if (resId != 0) {
                super.setStrokeColor(resId);
            }
        } catch (Exception e) {
        }
    }

    public void setCardBackgroundColorAttr(@AttrRes int resAttrId) {
        mCardBackground = resAttrId;
        try {
            ColorStateList resId = ResourceUtil.getThemeColorStateList(mCardBackground);
            if (resId != null) {
                super.setCardBackgroundColor(resId);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onChangeTheme() {
        setStrokeColorAttr(mStrokeColor);
        setCardBackgroundColorAttr(mCardBackground);
    }
}