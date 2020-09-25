package com.example.mvvmappapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.example.mvvmappapplication.App;


/**
 * Display 관련 유틸리티
 */
public class DisplayUtil {

    public static int getWidthPixels() {
        DisplayMetrics dm = App.getContext().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getHeightPixels() {
        DisplayMetrics dm = App.getContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static int getStatusBarHeight(Activity activity) {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getNavigationBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
