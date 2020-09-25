package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.AttrRes;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.utils.ResourceUtil;
import com.example.mvvmappapplication.utils.ThemeUtil;


/**
 * 점 View
 *
 * @author ehjung
 */
public class HSDotView extends View implements ThemeUtil.ITheme {

    @AttrRes
    private int mDotColor;

    Paint mPaint;

    public HSDotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public HSDotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HSDotView, 0, 0);
        try {
            mDotColor = ThemeUtil.getAttributeValueCustom(attrs, "dotColor");
        } finally {
            a.recycle();
        }
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        onChangeTheme();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2, mPaint);
        super.onDraw(canvas);
    }

    public void setDotColor(@AttrRes int color) {
        if (mDotColor == color) {
            return;
        }
        mDotColor = color;
        onChangeTheme();
    }

    @Override
    public void onChangeTheme() {
        try {
            int resId = ResourceUtil.getThemeDrawableResId(mDotColor);
            mPaint.setColor(ResourceUtil.getColor(resId));
            postInvalidate();
        } catch (Exception e) {
        }
    }
}