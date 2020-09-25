package com.example.mvvmappapplication.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import androidx.annotation.ArrayRes;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.example.mvvmappapplication.App;
import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.consts.Const;


/**
 * Created by kck on 2016-06-30.
 */
public class ResourceUtil {

    public static String getString(@StringRes int id) {
        return App.getContext().getString(id);
    }

    public static String getString(@StringRes int resId, Object... formatArgs) {
        return App.getContext().getString(resId, formatArgs);
    }

    public static String[] getStringArray(@ArrayRes int resId) {
        return App.getContext().getResources().getStringArray(resId);
    }

    public static int getInteger(@IntegerRes int resId) {
        return App.getContext().getResources().getInteger(resId);
    }

    public static int getColor(@ColorRes int id) {
        return ContextCompat.getColor(App.getContext(), id);
    }

    public static ColorStateList getColorStateList(@ColorRes int id) {
        return ContextCompat.getColorStateList(App.getContext(), id);
    }

    public static Drawable getDrawable(@DrawableRes int id) {
        return ContextCompat.getDrawable(App.getContext(), id);
    }

    public static int getLayoutId(Context context, String idName) {
        return App.getContext().getResources().getIdentifier(idName, "id", context.getPackageName());
    }

    public static int getDrawableResourceId(Context context, String drawableName) {
        return App.getContext().getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
    }

    public static int getThemeColorResId(@AttrRes int attrResId) {
        int themeId = R.style.WhiteTheme;
        if (App.getThemeType() == Const.eThemeType.DARK) {
            themeId = R.style.DarkTheme;
        }
        TypedArray a = App.getContext().getTheme().obtainStyledAttributes(themeId, new int[]{attrResId});
        int resourceId = a.getColor(0, 0);
        a.recycle();
        return resourceId;
    }

    public static int getThemeColor(@AttrRes int attrResId) {
        return getColor(getThemeColorResId(attrResId));
    }

    public static ColorStateList getThemeColorStateList(@AttrRes int attrResId) {
        int themeId = R.style.WhiteTheme;
        if (App.getThemeType() == Const.eThemeType.DARK) {
            themeId = R.style.DarkTheme;
        }
        TypedArray a = App.getContext().getTheme().obtainStyledAttributes(themeId, new int[]{attrResId});
        ColorStateList resourceId = a.getColorStateList(0);
        a.recycle();
        return resourceId;
    }

    public static int getThemeDrawableResId(@AttrRes int attrResId) {
        int themeId = R.style.WhiteTheme;
        if (App.getThemeType() == Const.eThemeType.DARK) {
            themeId = R.style.DarkTheme;
        }
        TypedArray a = App.getContext().getTheme().obtainStyledAttributes(themeId, new int[]{attrResId});
        int resourceId = a.getResourceId(0, 0);
        a.recycle();
        return resourceId;
    }

    public static Drawable getThemeDrawable(@AttrRes int attrResId) {
        return getDrawable(getThemeDrawableResId(attrResId));
    }

    public static int getThemeBackgroundResId(@AttrRes int attrResId) {
        int resourceId = ResourceUtil.getThemeDrawableResId(attrResId);
        if (resourceId == 0) {
            resourceId = ResourceUtil.getThemeColorResId(attrResId);
        }
        return resourceId;
    }
}
