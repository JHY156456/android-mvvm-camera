package com.example.mvvmappapplication.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.mvvmappapplication.App;
import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.consts.Const;

import java.util.ArrayList;



/**
 * Created by kck on 2016-07-04.
 */
public class IntentUtil {

    public static void startActivity(Context context, String action) {
        Intent newIntent = new Intent(action);
        context.startActivity(newIntent);
    }

    public static void startActivity(Context context, String action, @Nullable Bundle options) {
        Intent newIntent = new Intent(action);
        if (options != null) {
            newIntent.putExtras(options);
        }
        context.startActivity(newIntent);
    }

    public static void startActivity(Context context, Class<?> cls) {
        startActivity(context, cls, null);
    }

    public static void startActivity(Context context, Class<?> cls, @Nullable Bundle options) {
        Intent intent = new Intent(context, cls);
        if (options != null) {
            intent.putExtras(options);
        }
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity activity, Class<?> cls, int requestCode, @Nullable Bundle options) {
        Intent intent = new Intent(activity, cls);
        if (options != null) {
            intent.putExtras(options);
        }
        activity.startActivityForResult(intent, requestCode, options);
    }

    public static void startActivityForResult(Activity activity, Intent intent, int requestCode, @Nullable Bundle options) {
        if (options != null) {
            intent.putExtras(options);
        }
        activity.startActivityForResult(intent, requestCode, options);
    }

    public static void startActivityForResult(Fragment fragment, Class<?> cls, int requestCode, @Nullable Bundle options) {
        Intent intent = new Intent(fragment.getActivity(), cls);
        if (options != null) {
            intent.putExtras(options);
        }
        fragment.startActivityForResult(intent, requestCode, options);
    }

    public static void startActivityForResult(Fragment fragment, Intent intent, int requestCode, @Nullable Bundle options) {
        if (options != null) {
            intent.putExtras(options);
        }
        fragment.startActivityForResult(intent, requestCode, options);
    }

    /**
     * 앱 종료
     */
    public static void finishApp(Activity activity) {
        /* killProcess 사용하여 앱 종료 시 Adbrix IGAWorks endSession Api 가 정상적으로 호출되지 않을 수 있어
        killProcess 호출 전 protectSessionTracking() 호출 필수! */
        try {
            //((App) App.getContext()).clear();
            App.getCurrentActivity().finishAffinity();
        } catch (Exception ignored) {
        } finally {
            //App.isFinishingIntro = false;
            Process.killProcess(Process.myPid());
        }
    }
    /**
     * App 재시작
     * @param activity
     */
    public static void doRestartApp(Activity activity) {
        try {
            if (activity != null) {
                PackageManager packageManager = activity.getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage(activity.getPackageName());
                ComponentName componentName = intent.getComponent();
                Intent mainIntent = Intent.makeRestartActivityTask(componentName);
                activity.startActivity(mainIntent);
                finishApp(activity);
                System.exit(0);
            }
        } catch (Exception ex) {
        }
    }

    /**
     * PDF Viewer 앱 실행
     *
     * @param context
     * @param url
     */
    public static void toPDFViewer(Context context, String url) {
//        url =   "http://gahp.net/wp-content/uploads/2017/09/sample.pdf";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(url), "text/html");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

}
