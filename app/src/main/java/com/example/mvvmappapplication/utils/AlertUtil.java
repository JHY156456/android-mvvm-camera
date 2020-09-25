package com.example.mvvmappapplication.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.consts.Const;
import com.example.mvvmappapplication.custom.HSCardView;
import com.example.mvvmappapplication.custom.HSTextView;
import com.example.mvvmappapplication.custom.HSTitleBar;
import com.example.mvvmappapplication.custom.RecyclerAdapterT;
import com.example.mvvmappapplication.custom.RecyclerViewDividerItemDecoration;
import com.example.mvvmappapplication.custom.RecyclerViewHolderT;
import com.example.mvvmappapplication.custom.Typekit;
import com.example.mvvmappapplication.dialog.CustomDialog;
import com.example.mvvmappapplication.dialog.HSBottomSheetListDialog;
import com.example.mvvmappapplication.dialog.SelectListItemDialog;
import com.example.mvvmappapplication.dto.SelectDialogDto;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * 공통 팝업 유틸리티
 * <p>
 * Created by kck on 2016-06-30.
 * Update by ehjung on 2017-09-29.
 */
public class AlertUtil {

    /********************************************************************************************************
     * Toast
     *********************************************************************************************************/

    private static Toast mToast;

    public static void toast(Context context, String message) {
        toast(context, message, null, true);
    }

    public static void toast(Context context, String message, boolean isShortDuration) {
        toast(context, message, null, isShortDuration);
    }

    public static void toast(Context context, @StringRes int messageId) {
        toast(context, ResourceUtil.getString(messageId), null, true);
    }

    public static void toast(Context context, @StringRes int messageId1, @StringRes int messageId2) {
        toast(context, ResourceUtil.getString(messageId1), ResourceUtil.getString(messageId2), true);
    }

    public static void toast(Context context, String message1, String message2, boolean isShortDuration) {
        View view = null;
        int durationTime = isShortDuration ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;
        if (mToast == null) {
            try {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.layout_toast, null);
                mToast = new Toast(context);
                mToast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                mToast.setDuration(durationTime);
                mToast.setView(view);
            } catch (Exception ignored) {
                return;
            }
        } else {
            view = mToast.getView();
        }

        HSCardView layoutLL = view.findViewById(R.id.toastLayout);
        TextView messageTV1 = view.findViewById(R.id.toastMessage1);
        TextView messageTV2 = view.findViewById(R.id.toastMessage2);

        messageTV1.setText(message1);
        if (message2 == null) {
            layoutLL.setPadding(UIUtil.getPxFromDp(R.dimen.dimen_20), UIUtil.getPxFromDp(R.dimen.dimen_13), UIUtil.getPxFromDp(R.dimen.dimen_20), UIUtil.getPxFromDp(R.dimen.dimen_13));
            messageTV1.setMinHeight(UIUtil.getPxFromDp(R.dimen.dimen_40));
            messageTV2.setVisibility(View.GONE);
        } else {
            layoutLL.setPadding(UIUtil.getPxFromDp(R.dimen.dimen_20), UIUtil.getPxFromDp(R.dimen.dimen_10), UIUtil.getPxFromDp(R.dimen.dimen_20), UIUtil.getPxFromDp(R.dimen.dimen_13));
            messageTV1.setMinHeight(UIUtil.getPxFromDp(R.dimen.dimen_20));
            messageTV2.setText(message2);
            messageTV2.setVisibility(View.VISIBLE);
        }

        mToast.show();
    }

    /**
     * DAM 커스텀 토스트. 장바구니에서 사용
     *
     * @param context         컨텍스트
     * @param message         에러메시지
     * @param isShortDuration 토스출력시간.
     * @param isError         true DAM 커스텀 토스트, false 기존 토스트
     */
    public static void toast(Context context, String message, boolean isShortDuration, boolean isError) {
        Toast toast = null;
        if (!isError) {
            toast(context, message, null, isShortDuration);
        } else {
            View view = null;
            int durationTime = isShortDuration ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;
            if (toast == null) {
                try {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.layout_toast_red_theme, null);
                    toast = new Toast(context);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 238 * 3);
                    toast.setDuration(durationTime);
                    toast.setView(view);
                } catch (Exception ignored) {
                    return;
                }
            } else {
                view = toast.getView();
            }

            TextView messageTV = view.findViewById(R.id.toastMessage);

            messageTV.setText(message);

            toast.show();
        }
    }

    /********************************************************************************************************
     * WindowPopup
     *********************************************************************************************************/

    public static void tooltip(Context context, View view, @StringRes int msgResId) {
        Rect location = new Rect();
        view.getGlobalVisibleRect(location);

        View convertView = LayoutInflater.from(context).inflate(R.layout.layout_pop_tooltip, null);
        final PopupWindow popupWindow = new PopupWindow(convertView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);

        ImageView arrowIV = convertView.findViewById(R.id.arrowIV);
        TextView msgTV = convertView.findViewById(R.id.msgTV);
        msgTV.setText(msgResId);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) arrowIV.getLayoutParams();
        layoutParams.leftMargin = location.left + (location.right - location.left - UIUtil.getPxFromDp(R.dimen.dimen_14)) / 2;
        arrowIV.setLayoutParams(layoutParams);

        msgTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });

        popupWindow.showAtLocation(convertView, Gravity.TOP | Gravity.START, location.left, location.bottom);
    }



    public static void dropdown(Context context, View view, final List<SelectDialogDto> items, int selectedPosition, final SelectListItemDialog.onDialogSelectListener listener) {
        Rect location = new Rect();
        view.getGlobalVisibleRect(location);

        View convertView = LayoutInflater.from(context).inflate(R.layout.layout_popup_dropdown, null);
        final PopupWindow popupWindow = new PopupWindow(convertView, location.right - location.left, RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);

        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                listener.onSelectedItem(position, items.get(position));
                popupWindow.dismiss();
            }
        };

        LinearLayout contentContainer = convertView.findViewById(R.id.contentContainer);

        for (int i = 0; i < items.size(); i++) {
            SelectDialogDto item = items.get(i);

            HSTextView textView = new HSTextView(context);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtil.getPxFromDp(R.dimen.dimen_30)));
            textView.setText(item.getValue());
            textView.setPadding(UIUtil.getPxFromDp(R.dimen.dimen_10), 0, UIUtil.getPxFromDp(R.dimen.dimen_10), 0);
            textView.setTag(i);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setTypeface(Typekit.getInstance().get(Typekit.Style.Italic));
            textView.setOnClickListener(onClickListener);
            contentContainer.addView(textView);

            if (i == selectedPosition) {
                textView.setBackgroundResourceAttr(R.attr.searchSectorBackgroundColor);
            }
        }

        popupWindow.showAtLocation(convertView, Gravity.TOP | Gravity.END, DisplayUtil.getWidthPixels() - location.right, location.top);
    }

    /********************************************************************************************************
     * Alert
     *********************************************************************************************************/

    public static Dialog alert(Context context, int messageId) {
        return alert(context, messageId, null);
    }

    public static Dialog alert(Context context, String message) {
        String title = ResourceUtil.getString(R.string.alert_title);
        return alert(context, title, message, null);
    }

    public static Dialog alert(Context context, int messageId, OnClickListener listener) {
        return alert(context, R.string.alert_title, messageId, listener);
    }

    public static Dialog alert(Context context, String message, OnClickListener listener) {
        String title = ResourceUtil.getString(R.string.alert_title);
        return alert(context, title, message, listener);
    }

    public static Dialog alert(Context context, int titleId, int messageId) {
        String title = ResourceUtil.getString(titleId);
        String message = ResourceUtil.getString(messageId);
        return alert(context, title, message);
    }

    public static Dialog alert(Context context, String title, String message) {
        return alert(context, title, message, null);
    }

    public static Dialog alert(Context context, int titleId, int messageId, OnClickListener listener) {
        return alert(context, titleId, messageId, listener, R.string.alert_confirm);
    }

    public static Dialog alert(Context context, String title, String message, OnClickListener listener) {
        String ok = ResourceUtil.getString(R.string.alert_confirm);
        return alert(context, title, message, listener, ok);
    }

    public static Dialog alert(Context context, int messageId, OnClickListener listener, int okId) {
        return alert(context, R.string.alert_title, messageId, listener, okId);
    }

    public static Dialog alert(Context context, String message, OnClickListener listener, String ok) {
        String title = ResourceUtil.getString(R.string.alert_title);
        return alert(context, title, message, listener, ok);
    }

    public static Dialog alert(Context context, int titleId, int messageId, OnClickListener listener, int okId) {
        String title = ResourceUtil.getString(titleId);
        String message = ResourceUtil.getString(messageId);
        String ok = ResourceUtil.getString(okId);
        return alert(context, title, message, listener, ok);
    }

    public static Dialog alert(Context context, String title, String message, OnClickListener listener, String ok) {
        CustomDialog dialog = new CustomDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton(ok, listener);
        dialog.setCancelable(listener == null);
        dialog.show();
        return dialog;
    }



    /********************************************************************************************************
     * Confirm
     *********************************************************************************************************/

    public static Dialog confirm(Context context, int messageId, OnClickListener listener) {
        String message = ResourceUtil.getString(messageId);
        return confirm(context, message, listener);
    }

    public static Dialog confirm(Context context, int messageId, OnClickListener listener, boolean isCancelable) {
        String message = ResourceUtil.getString(messageId);
        return confirm(context, message, listener, isCancelable);
    }

    public static Dialog confirm(Context context, String message, OnClickListener listener) {
        String title = ResourceUtil.getString(R.string.alert_title);
        return confirm(context, title, message, listener);
    }

    public static Dialog confirm(Context context, String message, OnClickListener listener, boolean isCancelable) {
        String title = ResourceUtil.getString(R.string.alert_title);
        return confirm(context, title, message, listener, isCancelable);
    }

    public static Dialog confirm(Context context, int titleId, int messageId) {
        return confirm(context, titleId, messageId, null);
    }

    public static Dialog confirm(Context context, int titleId, int messageId, boolean isCancelable) {
        return confirm(context, titleId, messageId, null, isCancelable);
    }

    public static Dialog confirm(Context context, String title, String message) {
        return confirm(context, title, message, null);
    }

    public static Dialog confirm(Context context, int titleId, int messageId, OnClickListener listener) {
        return confirm(context, titleId, messageId, listener, R.string.alert_confirm, R.string.alert_cancel);
    }

    public static Dialog confirm(Context context, int titleId, int messageId, OnClickListener listener, boolean isCancelable) {
        return confirm(context, titleId, messageId, listener, R.string.alert_confirm, R.string.alert_cancel, isCancelable);
    }

    public static Dialog confirm(Context context, String title, String message, OnClickListener listener) {
        String ok = ResourceUtil.getString(R.string.alert_confirm);
        String cancel = ResourceUtil.getString(R.string.alert_cancel);
        return confirm(context, title, message, listener, ok, cancel);
    }

    public static Dialog confirm(Context context, String title, String message, OnClickListener listener, boolean isCancelable) {
        String ok = ResourceUtil.getString(R.string.alert_confirm);
        String cancel = ResourceUtil.getString(R.string.alert_cancel);
        return confirm(context, title, message, listener, ok, cancel, isCancelable);
    }

    public static Dialog confirm(Context context, int messageId, OnClickListener listener, int okId, int cancelId) {
        return confirm(context, R.string.alert_title, messageId, listener, okId, cancelId);
    }

    public static Dialog confirm(Context context, int messageId, OnClickListener listener, int okId, int cancelId, boolean isCancelable) {
        return confirm(context, R.string.alert_title, messageId, listener, okId, cancelId, isCancelable);
    }

    public static Dialog confirm(Context context, String message, OnClickListener listener, String ok, String cancel) {
        String title = ResourceUtil.getString(R.string.alert_title);
        return confirm(context, title, message, listener, ok, cancel);
    }

    public static Dialog confirm(Context context, int titleId, int messageId, OnClickListener listener, int okId, int cancelId) {
        String title = ResourceUtil.getString(titleId);
        String message = ResourceUtil.getString(messageId);
        String ok = ResourceUtil.getString(okId);
        String cancel = ResourceUtil.getString(cancelId);
        return confirm(context, title, message, listener, ok, cancel);
    }

    public static Dialog confirm(Context context, int titleId, int messageId, OnClickListener listener, int okId, int cancelId, boolean isCancelable) {
        String title = ResourceUtil.getString(titleId);
        String message = ResourceUtil.getString(messageId);
        String ok = ResourceUtil.getString(okId);
        String cancel = ResourceUtil.getString(cancelId);
        return confirm(context, title, message, listener, ok, cancel, isCancelable);
    }

    public static Dialog confirm(Context context, String title, String message, OnClickListener listener, String ok, String cancel) {
        CustomDialog dialog = new CustomDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton(ok, listener);
        dialog.setNegativeButton(cancel, listener);
        dialog.setCancelable(false, listener);
        dialog.show();
        return dialog;
    }

    public static Dialog confirm(Context context, String title, String message, OnClickListener listener, String ok, String cancel, boolean isCancelable) {
        CustomDialog dialog = new CustomDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton(ok, listener);
        dialog.setNegativeButton(cancel, listener);
        dialog.setCancelable(isCancelable, listener);
        dialog.show();
        return dialog;
    }

    /********************************************************************************************************
     * Close
     *********************************************************************************************************/

    public static Dialog alertClose(Context context, int titleId, int messageId) {
        String title = ResourceUtil.getString(titleId);
        String message = ResourceUtil.getString(messageId);
        return alertClose(context, title, message);
    }

    public static Dialog alertClose(Context context, String title, String message) {
        CustomDialog dialog = new CustomDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(true);
        dialog.setCloseBtn(true);
        dialog.show();
        return dialog;
    }

    /********************************************************************************************************
     * List - Dialog
     *********************************************************************************************************/

    /**
     * 단일 선택 리스트 팝업
     *
     * @param context
     * @param title
     * @param items
     * @param selectPosition
     * @param listener
     * @return
     */
    public static Dialog list(Context context, String title, String[] items, int selectPosition, SelectListItemDialog.onDialogSelectListener listener) {
        SelectListItemDialog dialog = new SelectListItemDialog(context, title, items, selectPosition);
        dialog.setDialogItemSelectListener(listener);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;
    }

    public static Dialog list(Context context, String title, String[] items, int selectPosition, HSBottomSheetListDialog.ISelectItemListener listener) {
        SelectListItemDialog dialog = new SelectListItemDialog(context, title, items, selectPosition);
        dialog.setDialogItemSelectListener((SelectListItemDialog.onDialogSelectListener) listener);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;
    }

    public static Dialog list(Context context, String title, List<SelectDialogDto> items, int selectPosition, boolean isCancelable, SelectListItemDialog.onDialogSelectListener listener) {
        SelectListItemDialog dialog = new SelectListItemDialog(context, title, items, selectPosition);
        dialog.setDialogItemSelectListener(listener);
        dialog.setCancelable(isCancelable);
        dialog.show();
        return dialog;
    }

    /**
     * 다중 선택 리스트 팝업
     *
     * @param context
     * @param title
     * @param items
     * @param listener
     * @return
     */
    public static Dialog list(Context context, String title, List<SelectDialogDto> items, SelectListItemDialog.onDialogSelectListener listener) {
        SelectListItemDialog dialog = new SelectListItemDialog(context, title, items, true);
        dialog.setDialogItemSelectListener(listener);
        dialog.show();
        return dialog;
    }



    /********************************************************************************************************
     * 이미지 팝업
     *********************************************************************************************************/

    public static Dialog showImagePopup(final Context context, String imgUrl, final OnClickListener listener, @NonNull String ok) {
        return showImagePopup(context, null, imgUrl, listener, ok, null);
    }

    public static Dialog showImagePopup(final Context context, String title, String imgUrl, final OnClickListener listener, @NonNull String ok) {
        return showImagePopup(context, title, imgUrl, listener, ok, null);
    }

    public static Dialog showImagePopup(final Context context, String title, String imgUrl, final OnClickListener listener, @NonNull String ok, String cancel) {
        CustomDialog dialog = new CustomDialog(context);
        dialog.setTitle(title);
        dialog.setCustomView(R.layout.dialog_image);

        final ImageView imageView = dialog.findViewById(R.id.imageView);
        Glide.with(context).load(imgUrl).into(imageView);

        dialog.setPositiveButton(ok, listener);
        if (!TextUtils.isEmpty(cancel)) {
            dialog.setNegativeButton(cancel, listener);
        }
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }



    /********************************************************************************************************
     * Custom
     *********************************************************************************************************/

    /**
     * 정렬 변경
     *
     * @param dialog
     * @param gravity
     */
    public static void setGravityToContent(Dialog dialog, int gravity) {
        if (dialog == null) {
            return;
        }
        try {
            ((TextView) dialog.findViewById(R.id.contenttext)).setGravity(gravity);
        } catch (Exception e) {
        }
    }

    /**
     * 퍼미션 안내 팝업
     *
     * @param context
     * @param message
     * @param listener
     * @return
     */
    public static Dialog alertPermissionPopup(Context context, String message, DialogInterface.OnClickListener listener) {
        Dialog dialog = alert(context, "안내", message, listener);
        AlertUtil.setGravityToContent(dialog, Gravity.START);
        return dialog;
    }

    /**
     * 퍼미션 안내 팝업
     *
     * @param context
     * @param message
     * @param listener
     * @param ok
     * @param cancel
     * @return
     */
    public static Dialog confirmPermissionPopup(Context context, String message, DialogInterface.OnClickListener listener, String ok, String cancel) {
        Dialog dialog = confirm(context, "안내", message, listener, ok, cancel);
        AlertUtil.setGravityToContent(dialog, Gravity.START);
        return dialog;
    }

    /**
     * 현재가 > 채권 > 체크포인트 > 신용등급 (i) 팝업
     */
    public static void showCreditRatingInfoPopup(Context context) {
        CustomDialog dialog = new CustomDialog(context);
        dialog.setCustomView(R.layout.layout_popup_credit_rating_info);
        dialog.setPositiveButton("확인", null);
        dialog.setCancelable(true);
        dialog.show();
    }


    /**
     * OTP 안내 팝업
     */
    public static void showOtpGuidePopup(Context context) {
        final CustomDialog dialog = new CustomDialog(context);
        dialog.setCustomView(R.layout.dialog_otp_guide);
        dialog.setCloseBtn(true);
        dialog.setCancelable(true);
        dialog.show();
    }

    /**
     * 보안카드 안내 팝업
     */
    public static void showSecurityCardGuidePopup(Context context) {
        final CustomDialog dialog = new CustomDialog(context);
        dialog.setCustomView(R.layout.dialog_security_card_guide);
        dialog.setCloseBtn(true);
        dialog.setCancelable(true);
        dialog.show();
    }

    /**
     * 자산 상세보기 판매/추가구매 버튼 팝업
     *
     * @param activity
     * @param title
     * @param listener
     * @return
     */
    public static BottomSheetDialog showOrderDialog(Activity activity, String title, boolean isBuy, boolean isSell, final View.OnClickListener listener) {
        if (!UIUtil.checkActivityRunningState(activity)) return null;
        final BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(R.layout.dialog_assets_order_bottom_sheet);

        TextView titleTV = dialog.findViewById(R.id.dialogAssetsBsTitle);
        Button addBuyBT = dialog.findViewById(R.id.addBuyBT);
        Button sellBT = dialog.findViewById(R.id.sellBT);

        titleTV.setText(title);

        View.OnClickListener mOnClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (UIUtil.isDoubleClick()) {
                    return;
                }
                dialog.dismiss();
                listener.onClick(v);
            }
        };

        if (isBuy) {
            addBuyBT.setOnClickListener(mOnClickListener);
        } else {
            addBuyBT.setVisibility(View.GONE);
        }
        if (isSell) {
            sellBT.setOnClickListener(mOnClickListener);
        } else {
            sellBT.setVisibility(View.GONE);
        }
        dialog.show();
        return dialog;
    }


    /**********************************************************************************************
    * 홈
    ***********************************************************************************************/
    public static void showInfoDialog(Context context, String title) {
        final CustomDialog dialog = new CustomDialog(context);
        dialog.setCustomView(R.layout.dialog_info_fond);
        dialog.setTitle(title);
        dialog.setPositiveButton("확인", null);
        dialog.setCancelable(false);
        dialog.show();
    }


    /********************************************************************************************************
     * Theme
     *********************************************************************************************************/

    public static void onChangeTheme() {
        mToast = null;
    }
}

