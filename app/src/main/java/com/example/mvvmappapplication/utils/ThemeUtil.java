package com.example.mvvmappapplication.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.AttrRes;

import com.example.mvvmappapplication.App;
import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.consts.Const;

/**
 * 테마 유틸리티
 * <p>
 * Created by ehjung on 2017-11-09.
 */
public class ThemeUtil {

    /**
     * StatusBar 테마 적용
     *
     * @param activity
     */
    public static void setStatusBarColor(Activity activity) {
        // Splash 화면 배경 색상과 동일 색 적용
//        if(activity instanceof HSIntroMainActivity) {
//            UIUtil.setTaskBarColored(activity, R.color.color_111420);
//            return;
//        }
//        // 현재가, 섹터상세, 신분증촬영 화면은 투명 적용
//        if (activity instanceof HSProductMainActivity || activity instanceof SectorDetailActivity || activity instanceof CameraPreviewActivity || activity instanceof UntactCameraPreviewActivity) {    // TODO : (2020.08.05) - 비대면용 OCR 적용
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
//                activity.getWindow().getDecorView().setSystemUiVisibility(0);
//                if (App.getThemeType() == Const.eThemeType.DARK) {
//                    UIUtil.setTaskBarColored(activity, R.color.color_000000);
//                } else {
//                    UIUtil.setTaskBarColored(activity, R.color.color_f8f8f8);
//                    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                }
//                return;
//            }
//        }
        if (App.getThemeType() == Const.eThemeType.DARK) {
            UIUtil.setTaskBarColored(activity, R.color.color_000000);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility(0);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                UIUtil.setTaskBarColored(activity, R.color.color_f8f8f8);
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                UIUtil.setTaskBarColored(activity, R.color.color_000000);
            }
        }
    }

    public static void updateThemeToBackground(View view, @AttrRes int attrResId) {
        int resId = ResourceUtil.getThemeDrawableResId(attrResId);
        if (resId == 0) {
            resId = ResourceUtil.getThemeColorResId(attrResId);
            view.setBackgroundColor(resId);
        } else {
            view.setBackgroundResource(resId);
        }
    }

    public static void updateTheme(ViewGroup parent) {
        if (parent == null) {
            return;
        }
        Object tag = parent.getTag();
        try {
            for (int i = parent.getChildCount() - 1; i >= 0; i--) {
                final View child = parent.getChildAt(i);
                tag = child.getTag();

                if (child instanceof ITheme) {
                    ((ITheme) child).onChangeTheme();
                }
                if (child instanceof ViewGroup) {
                    updateTheme((ViewGroup) child);
                }
            }
        } catch (Exception e) {
            LogUtil.e("[THEME] tag:" + tag + ", " + e.toString());
        }
    }

    public static int getAttributeValue(AttributeSet attrs, String name) {
        String value = attrs.getAttributeValue(ITheme.ANDROID_NS, name);
        if (value != null && (value.matches("\\?.+") || value.matches("@.+"))) {
            try {
                return Integer.parseInt(value.substring(1));
            } catch (Exception e) {
            }
        }
        return 0;
    }

    public static int getAttributeValueCustom(AttributeSet attrs, String name) {
        String value = attrs.getAttributeValue(ITheme.RES_AUTO_NS, name);
        if (value != null && (value.matches("\\?.+") || value.matches("@.+"))) {
            try {
                return Integer.parseInt(value.substring(1));
            } catch (Exception e) {
            }
        }
        return 0;
    }

    public interface ITheme {

        String ANDROID_NS = "http://schemas.android.com/apk/res/android";
        String RES_AUTO_NS = "http://schemas.android.com/apk/res-auto";

        String ATTR_TEXT_COLOR = "textColor";
        String ATTR_BACKGROUND = "background";
        String ATTR_DRAWABLE_LEFT = "drawableLeft";
        String ATTR_DRAWABLE_TOP = "drawableTop";
        String ATTR_DRAWABLE_RIGHT = "drawableRight";
        String ATTR_DRAWABLE_BOTTOM = "drawableBottom";
        String ATTR_IMAGE_SCR = "src";

        void onChangeTheme();
    }
}
