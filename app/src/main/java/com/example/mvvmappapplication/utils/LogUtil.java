package com.example.mvvmappapplication.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.mvvmappapplication.R;

public class LogUtil {
    public final static String TAG_STEPS = ResourceUtil.getString(R.string.app_name);

    private static final int LEVEL_VERBOSE = 0;
    private static final int LEVEL_DEBUG = 1;
    private static final int LEVEL_INFO = 2;
    private static final int LEVEL_WARNING = 3;
    private static final int LEVEL_ERROR = 4;
    public static boolean mIsDebugMode = false;
    public static void setDebugMode(boolean isDebugMode) {
        mIsDebugMode = isDebugMode;
    }

    public static void e(Object... args) {
        LogUtil.log(LEVEL_ERROR, args);
    }
    public static void i(Object... args) {
        LogUtil.log(LEVEL_INFO, args);
    }


    /**
     * .
     *
     * @param level nLevel
     * @param args  args
     */
    @SuppressLint("DefaultLocale")
    private static synchronized void log(int level, Object[] args) {
        if (mIsDebugMode) {
            if (args == null || args.length == 0) {
                return;
            }
        } else {
            return;
        }

        String className = Thread.currentThread().getStackTrace()[4].getClassName();
        String methodName = Thread.currentThread().getStackTrace()[4].getMethodName();
        int nLineNumber = Thread.currentThread().getStackTrace()[4].getLineNumber();

        if (className != null) {
            int lastDotPos = className.lastIndexOf(".");
            className = className.substring(lastDotPos + 1);
        }

        // format
        String format = "" + args[0];
        format = format.replaceAll("%d", "%s");
        format = format.replaceAll("%f", "%s");
        format = format.replaceAll("%c", "%s");
        format = format.replaceAll("%b", "%s");
        format = format.replaceAll("%x", "%s");
        format = format.replaceAll("%l", "%s");

        // argument
        String argument = "";

        switch (args.length - 1) {
            case 0:
                argument = format;
                break;
            case 1:
                argument = String.format(format, "" + args[1]);
                break;
            case 2:
                argument = String.format(format, "" + args[1], "" + args[2]);
                break;
            case 3:
                argument = String.format(format, "" + args[1], "" + args[2], "" + args[3]);
                break;
            case 4:
                argument = String.format(format, "" + args[1], "" + args[2], "" + args[3], "" + args[4]);
                break;
            case 5:
                argument = String.format(format, "" + args[1], "" + args[2], "" + args[3], "" + args[4], "" + args[5]);
                break;
            default:
                break;
        }

        String printLog = String.format("[%s] %s() [%5d] >> %s\n", className, methodName, nLineNumber, argument);
        appendLog(level,printLog,3);
    }
    private static void printLogger(int level, String printLog) {
        // Level
        switch (level) {
            case LEVEL_ERROR:
                Log.e(TAG_STEPS, printLog);
                break;
            case LEVEL_WARNING:
                Log.w(TAG_STEPS, printLog);
                break;
            case LEVEL_INFO:
                Log.i(TAG_STEPS, printLog);
                break;
            case LEVEL_VERBOSE:
                Log.v(TAG_STEPS, printLog);
                break;
            case LEVEL_DEBUG:
            default:
                Log.d(TAG_STEPS, printLog);
                break;
        }
    }
    private static void appendLog(int level, String printLog, int discount) {
        if(printLog.length() > 3000 && discount >= 0) {
            String log = printLog.substring(0,3000);
            printLogger(level,log);
            appendLog(level, printLog.substring(3000), --discount);
        } else {
            printLogger(level,printLog);
        }
    }
}
