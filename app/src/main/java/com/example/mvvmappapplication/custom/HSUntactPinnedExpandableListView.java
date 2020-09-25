package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

import androidx.annotation.AttrRes;
import androidx.annotation.StyleRes;

import com.example.mvvmappapplication.utils.ThemeUtil;

public class HSUntactPinnedExpandableListView extends ExpandableListView implements ThemeUtil.ITheme {

    @StyleRes
    int mStyle;
    @AttrRes
    int mBackground;

    public HSUntactPinnedExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public HSUntactPinnedExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        mStyle = attrs.getStyleAttribute();
        mBackground = ThemeUtil.getAttributeValue(attrs, ATTR_BACKGROUND);
        setListeners();
    }

    public void setBackgroundResourceAttr(@AttrRes int resAttrId) {
        mBackground = resAttrId;
        ThemeUtil.updateThemeToBackground(this, mBackground);
    }

    @Override
    public void onChangeTheme() {
        setBackgroundResourceAttr(mBackground);
    }

    private void setListeners(){
        super.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) { }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    if (getChildAt(0) != null) {
                        getChildAt(0).setY(0);
                    }
                }
            }
        });
    }
}