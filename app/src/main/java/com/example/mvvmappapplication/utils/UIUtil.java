package com.example.mvvmappapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.DimenRes;


import com.example.mvvmappapplication.App;
import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.custom.Typekit;

import java.math.BigDecimal;

/**
 * Some descriptions here
 *
 * @author kck
 * @version 1.0
 * @since 2016-11-02
 */
public class UIUtil {

    public static void setListViewHeightBasedOnChildren(final ListView listView, final ScrollView scrollView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();

        if (scrollView != null) {
            scrollView.post(new Runnable() {
                public void run() {
                    scrollView.scrollTo(0, 0);
                }
            });
        }
    }

    /**
     * advanced-recyclerview drag 유틸리티
     * @param v
     * @param x
     * @param y
     * @return
     */
    public static boolean hitTest(View v, int x, int y) {
        if (v == null) return false;
        final int tx = (int) (v.getTranslationX() + 0.5f);
        final int ty = (int) (v.getTranslationY() + 0.5f);
        final int left = v.getLeft() + tx;
        final int right = v.getRight() + tx;
        final int top = v.getTop() + ty;
        final int bottom = v.getBottom() + ty;
        return (x >= left) && (x <= right) && (y >= top) && (y <= bottom);
    }

    public static void setPLColor(View view, long pl) {
        setPLColor(view, (float) pl);
    }

    public static void setPLColor(View view, float pl) {
        if (pl > 0) {    //빨강
            view.setEnabled(false);
            view.setActivated(false);

            view.setSelected(true);
        } else if (pl < 0) {    //파랑
            view.setSelected(false);
            view.setActivated(false);

            view.setEnabled(true);
        } else { //검정
            view.setSelected(false);
            view.setEnabled(false);

            view.setActivated(true);
        }
    }

    public static void setPLColor(Object data, View... views) {
        BigDecimal pl = null;
        if (data == null || TextUtils.isEmpty(data.toString())) {
            pl = new BigDecimal("0");
        }
        try {
            if (data instanceof BigDecimal) {
                pl = ((BigDecimal) data);
            } else if (data instanceof String) {
                pl = new BigDecimal((String) data);
            } else {
                pl = new BigDecimal(String.valueOf(data));
            }
        } catch (Exception ignored) {
        }
        for (View view : views) {
            if (pl.compareTo(BigDecimal.ZERO) > 0) {    //빨강
                view.setEnabled(false);
                view.setActivated(false);

                view.setSelected(true);
            } else if (pl.compareTo(BigDecimal.ZERO) < 0) {    //파랑
                view.setSelected(false);
                view.setActivated(false);

                view.setEnabled(true);
            } else { //검정
                view.setSelected(false);
                view.setEnabled(false);

                view.setActivated(true);
            }
        }
    }

    public static int isColorFluctuation(int fluctuation) {

        switch (fluctuation) {
            case 1:
            case 2: // 상한 상승
                return 1;
            case 4:
            case 5: // 하한 하락
                return -1;
            default:
                return 0;
        }
    }

    /**
     * FID 색상 표시
     */
    public static int isColorFluctuation(String sign) {
        if ("1".equals(sign) || "2".equals(sign) || "C".equalsIgnoreCase(sign)) {  //상한 , 상승
            return 1;
        } else if ("3".equals(sign) || "B".equals(sign)) {  //보합
            return 0;
        } else if ("4".equals(sign) || "5".equals(sign) || "D".equalsIgnoreCase(sign)) {  //하한 , 하락
            return -1;
        }
        return 0;
    }

    public static int isColorQntFromClsCode(String clsCode) {
        if ("2".equals(clsCode)) {
            return 1;
        } else if ("1".equals(clsCode)) {
            return -1;
        }
        return 0;
    }

    public static int getRankTextColor(String strRank) {
        if (!TextUtils.isEmpty(strRank)) {
            int rank = StringUtil.parseInt(strRank);
            switch (rank) {
                case 0:
                    return R.attr.lowColor;
                case 1:
                    return android.R.attr.textColor;
                case 2:
                    return R.attr.highColor;
            }
        }
        return android.R.attr.textColor;
    }
    public static void setColorFluctuation(View view, int fluctuation) {

        switch (fluctuation) {
            case 1:
            case 2: // 상한 상승
                view.setEnabled(false);
                view.setActivated(false);
                view.setSelected(true);
                break;
            case 4:
            case 5: // 하한 하락
                view.setEnabled(true);
                view.setActivated(false);
                view.setSelected(false);
                break;
            default:
                view.setEnabled(false);
                view.setActivated(true);
                view.setSelected(false);
                break;
        }
    }

//    public static eDaebiColor convertDaebiColor(String strFluctuation) {
//        if ("1".equals(strFluctuation)){
//            return eDaebiColor.HIGH;
//        } else if ("2".equals(strFluctuation) || "C".equalsIgnoreCase(strFluctuation) || "+".equals(strFluctuation)){
//            return eDaebiColor.UPPER;
//        } else if ("4".equals(strFluctuation) || "D".equalsIgnoreCase(strFluctuation) || "-".equals(strFluctuation)) {
//            return eDaebiColor.LOW;
//        } else if ("5".equals(strFluctuation)){
//            return eDaebiColor.LOWER;
//        } else {
//            return eDaebiColor.NONE;
//        }
//    }
//
//    public static eDaebiColor convertDaebiColor(double value) {
//        if (value>0)
//            return eDaebiColor.UPPER;
//        else if (value<0)
//            return eDaebiColor.LOW;
//        else
//            return eDaebiColor.NONE;
//    }
//
//    public static void setColorFluctuation(View view, eDaebiColor daebiColor) {
//        switch (daebiColor) {
//            case UPPER:
//            case HIGH:
//                view.setEnabled(false);
//                view.setActivated(false);
//                view.setSelected(true);
//                break;
//            case LOW:
//            case LOWER:
//                view.setEnabled(true);
//                view.setActivated(false);
//                view.setSelected(false);
//                break;
//            default:
//                view.setEnabled(false);
//                view.setActivated(true);
//                view.setSelected(false);
//                break;
//        }
//    }

    public static void setTaskBarColored(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ResourceUtil.getColor(color));
        }
    }

    public static int getScrollY(AbsListView listView) {
        View firstChildView = listView.getChildAt(0);
        if (firstChildView == null) {
            return 0;
        }

        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int top = firstChildView.getTop();

        int headerHeight = 0;
        return -top + firstVisiblePosition * firstChildView.getHeight() + headerHeight;
    }

    public static int getScrollY(AbsListView listView, View layoutHolder) {
        View firstChildView = listView.getChildAt(0);
        if (firstChildView == null) {
            return 0;
        }

        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int top = firstChildView.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = layoutHolder.getHeight();
        }

        return -top + firstVisiblePosition * firstChildView.getHeight() + headerHeight;
    }

    public interface KeyboardVisibilityListener {
        void onKeyboardVisibilityChanged(boolean keyboardVisible);
    }

    @Deprecated
    public static ViewTreeObserver.OnGlobalLayoutListener setKeyboardVisibilityListener(Activity activity, final KeyboardVisibilityListener keyboardVisibilityListener) {
        final View contentView = activity.findViewById(android.R.id.content);
        ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
            private int mPreviousHeight;

            @Override
            public void onGlobalLayout() {
                int newHeight = contentView.getHeight();
                if (mPreviousHeight != 0) {
                    if (mPreviousHeight > newHeight) {
                        // Height decreased: keyboard was shown
                        keyboardVisibilityListener.onKeyboardVisibilityChanged(true);
                    } else if (mPreviousHeight < newHeight) {
                        // Height increased: keyboard was hidden
                        keyboardVisibilityListener.onKeyboardVisibilityChanged(false);
                    } else {
                        // No change
                    }
                }
                mPreviousHeight = newHeight;
            }
        };
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(listener);
        return listener;
    }

    @Deprecated
    public static void removeKeyboardVisibilityListener(Activity activity, ViewTreeObserver.OnGlobalLayoutListener listener){
        final View contentView = activity.findViewById(android.R.id.content);
        contentView.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
    }

    private static long mlLastClickTime = 0;

    /**
     * . 더블 클릭 여부 체크
     *
     * @return boolean
     */
    public static boolean isDoubleClick() {
        if (SystemClock.elapsedRealtime() - mlLastClickTime <= ViewConfiguration.getDoubleTapTimeout()) {
            return true;
        }
        mlLastClickTime = SystemClock.elapsedRealtime();
        return false;
    }

    /**
     * 해당 DP를 pX로 변환하여 반환
     *
     * @param dimensionId dimensionId
     * @return int
     */
    public static int getPxFromDp(int dimensionId) {
        return App.getContext().getResources().getDimensionPixelSize(dimensionId);
    }

    /**
     * Activity 상태 체크
     *
     * @param activity Activity
     * @return boolean
     */
    public static boolean checkActivityRunningState(Activity activity) {
        return !(activity == null || activity.isFinishing() || null == activity.getWindow());
    }

    /**
     * 단말 키보드 show
     */
    public static void showSoftKeyboard(View view) {
        if (view == null) return;
        InputMethodManager inputMethodManager = (InputMethodManager) App.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            view.requestFocus();
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * 단말 키보드 hide
     */
    public static void hideSoftKeyboard(View view) {
        if (view == null) return;
        InputMethodManager inputMethodManager = (InputMethodManager) App.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            view.clearFocus();
            inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    /**
     * 코드에서 생성한 View ID Generator.
     *
     * @param position
     *            position
     * @return view id
     */
    public static int generatorViewID(int position) {
        return ((position + 1) + 2 << 24);
    }

//    /**
//     * 포인트 색상 적용
//     *
//     * @param text       전체 문구
//     * @param token      색상 변경 문자열
//     * @param pointColor 적용 색상
//     * @param isAll      문자열이 여러개 있는 경우 모두 변경 여부
//     * @param isBold     볼드 적용
//     * @return
//     */
//    public static CharSequence convertKeywordColor(CharSequence text, String token, @AttrRes int pointColor, boolean isAll, boolean isBold) {
//        if(TextUtils.isEmpty(text) || TextUtils.isEmpty(token)) {
//            return text;
//        }
//        int tokenLen = token.length();
//        int start = text.toString().toUpperCase().indexOf(token.toUpperCase());
//        int next = -1;
//
//        if (start > -1) {
//            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
//            do {
//                next = start + tokenLen;
//                Typeface typeface = isBold ? Typekit.getInstance().get(Typekit.Style.Bold) : Typekit.getInstance().get(Typekit.Style.Italic);
//                spannableStringBuilder.setSpan(new HSTypefaceSpan("", typeface), start, next, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//                spannableStringBuilder.setSpan(new ForegroundColorSpan(ResourceUtil.getThemeColorResId(pointColor)), start, next, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                start = text.toString().indexOf(token, next);
//            } while (isAll && start > -1);
//            text = spannableStringBuilder;
//        }
//        return text;
//    }

//    public static CharSequence convertKeywordColor(CharSequence text, String token, @AttrRes int pointColor) {
//        return convertKeywordColor(text, token, pointColor, true, false);
//    }

    public static CharSequence convertTextSpannable(CharSequence text, String token, @AttrRes int pointColor, @DimenRes int textSize) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(token)) {
            return text;
        }
        int start = text.toString().toUpperCase().indexOf(token.toUpperCase());
        int end = start + token.length();
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        if (pointColor != -1) {
            builder.setSpan(new ForegroundColorSpan(ResourceUtil.getThemeColorResId(pointColor)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (textSize != -1) {
            builder.setSpan(new AbsoluteSizeSpan(UIUtil.getPxFromDp(textSize)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

//    public static CharSequence convertTextSpannable(CharSequence text, String token, @AttrRes int pointColor, Typekit.Style textStyle) {
//        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(token)) {
//            return text;
//        }
//        int start = text.toString().toUpperCase().indexOf(token.toUpperCase());
//        int end = start + token.length();
//        SpannableStringBuilder builder = new SpannableStringBuilder(text);
//        if (pointColor != -1) {
//            builder.setSpan(new HSTypefaceSpan("", Typekit.getInstance().get(textStyle)), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//            builder.setSpan(new ForegroundColorSpan(ResourceUtil.getThemeColorResId(pointColor)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//        return builder;
//    }
//
//    /**
//     * 텍스트 문자 ellipsize
//     *
//     * @param textView
//     * @param text
//     * @param textViewWidth
//     */
//    public static void ellipsize(TextView textView, String text, int textViewWidth) {
//        int measuredLength = CanvasUtil.getMaxTextLength(textView.getPaint(), text, textViewWidth);
//        if (measuredLength < text.length() && measuredLength > 2) {
//            textView.setText(String.format("%s...", text.substring(0, measuredLength - 2)));
//        } else {
//            textView.setText(text);
//        }
//    }
}