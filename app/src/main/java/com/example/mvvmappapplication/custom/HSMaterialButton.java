package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.AttrRes;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.utils.ResourceUtil;
import com.example.mvvmappapplication.utils.ThemeUtil;
import com.example.mvvmappapplication.utils.UIUtil;
import com.google.android.material.button.MaterialButton;


public class HSMaterialButton extends MaterialButton implements ThemeUtil.ITheme {

    /* 순서 바꾸면 안됨 */
    private enum eButtonStyle {
        ROUND, ELLIPSE, NONE
    }

    @AttrRes
    int mTextColor;
    @AttrRes
    int mStrokeColor;
    @AttrRes
    int mRippleColor;

    eButtonStyle mButtonStyle = eButtonStyle.NONE;

    public HSMaterialButton(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public HSMaterialButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs, defStyleAttr);
    }

    private void initAttrs(AttributeSet attrs, int defStyleAttr) {
        /* button type */
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MaterialButtonStyle, defStyleAttr, 0);
        int buttonType = a.getInteger(R.styleable.MaterialButtonStyle_materialButtonType, eButtonStyle.NONE.ordinal());
        for (eButtonStyle type : eButtonStyle.values()) {
            if (type.ordinal() == buttonType) {
                mButtonStyle = type;
                break;
            }
        }
        a.recycle();

        /* corner Radius */
        switch (mButtonStyle) {
            case ELLIPSE:
                /* get height */
                int height = UIUtil.getPxFromDp(R.dimen.button_height);
                String layoutHeight = attrs.getAttributeValue(ThemeUtil.ITheme.ANDROID_NS, "layout_height");

                if (!layoutHeight.equals(ViewGroup.LayoutParams.MATCH_PARENT + "") && !layoutHeight.equals(ViewGroup.LayoutParams.WRAP_CONTENT + "")) {
                    int[] systemAttrs = {android.R.attr.layout_height};
                    a = getContext().obtainStyledAttributes(attrs, systemAttrs);
                    height = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    a.recycle();
                }
                setCornerRadius(height / 2);
                requestLayout();
                invalidate();
                break;

            case ROUND:
                int cornerRadius = ThemeUtil.getAttributeValueCustom(attrs, "cornerRadius");
                if (cornerRadius == 0) {
                    setCornerRadius(UIUtil.getPxFromDp(R.dimen.dimen_12));
                } else {
                    setCornerRadius(UIUtil.getPxFromDp(cornerRadius));
                }
                break;

            default:
                setCornerRadius(0);
                break;
        }

        mTextColor = ThemeUtil.getAttributeValue(attrs, ATTR_TEXT_COLOR);
        if (mTextColor != 0) {
            setTextColorAttr(mTextColor);
        }

        setRippleColorResource(R.color.transparent);

        mRippleColor = ThemeUtil.getAttributeValueCustom(attrs, "rippleColor");

        mStrokeColor = ThemeUtil.getAttributeValueCustom(attrs, "strokeColor");
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

    public void setStrokeColorAttr(@AttrRes int resAttrId) {
        mStrokeColor = resAttrId;
        try {
            ColorStateList resId = ResourceUtil.getThemeColorStateList(mStrokeColor);
            if (resId != null) {
                super.setStrokeColor(resId);
            }
        } catch (Exception e) {
        }
    }

    public void setRippleColorAttr(@AttrRes int resAttrId) {
        mRippleColor = resAttrId;
        try {
            ColorStateList resId = ResourceUtil.getThemeColorStateList(mRippleColor);
            if (resId != null) {
                super.setRippleColor(resId);
            }
        } catch (Exception e) {
        }
    }

    int[] attributesArray = new int[]{android.R.attr.textColor, android.R.attr.backgroundTint, R.attr.rippleColor};

    @Override
    public void onChangeTheme() {
        if (mTextColor != 0) {
            setTextColorAttr(mTextColor);
        }
        if (mStrokeColor != 0) {
            setStrokeColorAttr(mStrokeColor);
        }

//        try {
//            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(android.R.style.Widget_Material_Button, attributesArray);
//            if (typedArray.length() > 0) {
//                if (mTextColor == 0) {
//                    ColorStateList textColorId = typedArray.getColorStateList(0);
//                    if (textColorId != null) {
//                        super.setTextColor(textColorId);
//                    }
//                }
//                if (mBackgroundTint == 0) {
//                    ColorStateList backgroundTint = typedArray.getColorStateList(1);
//                    if (backgroundTint != null) {
//                        super.setBackgroundTintList(backgroundTint);
//                    }
//                }
//                if (mRippleColor == 0) {
//                    ColorStateList rippleColor = typedArray.getColorStateList(2);
//                    if (rippleColor != null) {
//                        super.setRippleColor(rippleColor);
//                    }
//                }
//            }
//            typedArray.recycle();
//        } catch (Resources.NotFoundException e) {
//            e.printStackTrace();
//        }

//        if (mTextColor == 0) {
//            mTextColor = R.attr.buttonTextColor;
//        }
//        setTextColorAttr(mTextColor);
//
//        switch (mButtonStyle) {
//            case ROUND_OUTLINE:
//            case ELLIPSE_OUTLINE:
//            case RECT_OUTLINE:
//                setStrokeColorAttr(mRippleColor);
//                break;
//        }
//
//        if (mRippleColor == 0) {
//            mRippleColor = R.attr.colorPrimaryDark;
//        }
//        setRippleColorAttr(mRippleColor);

//
//        try {
//            getContext().setTheme((GlobalApp.getThemeType() == Const.eThemeType.DARK) ? R.style.DarkTheme : R.style.WhiteTheme);
//            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(mStyleAttr, attributesArray);
//            if (typedArray.length() > 0) {
//                if (mTextColor == NONE) {
//                    ColorStateList textColorId = typedArray.getColorStateList(0);
//                    if (textColorId != null) {
//                        super.setTextColor(textColorId);
//                    }
//                }
//                if (mBackgroundTint == NONE) {
//                    ColorStateList backgroundTint = typedArray.getColorStateList(1);
//                    if (backgroundTint != null) {
//                        super.setBackgroundTintList(backgroundTint);
//                    }
//                }
//            }
//            typedArray.recycle();
//        } catch (Resources.NotFoundException e) {
//            e.printStackTrace();
//        }
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        if (mButtonStyle == eButtonStyle.ELLIPSE) {
//            try {
//                int radius = (int) (getHeight() / 2f);
//                if (getCornerRadius() != radius) {
//                    setCornerRadius(radius);
//                }
//            } catch (Exception ignored) {
//            }
//        }
//        super.onDraw(canvas);
//    }

    private int getMeasurement(int measureSpec, int preferred) {
        int specSize = MeasureSpec.getSize(measureSpec);
        int measurement;
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.EXACTLY:
                measurement = specSize;
                break;
            case MeasureSpec.AT_MOST:
                measurement = Math.min(preferred, specSize);
                break;
            default:
                measurement = measureSpec;
                break;
        }
        return measurement;
    }
}