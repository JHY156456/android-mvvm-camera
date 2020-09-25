package com.example.mvvmappapplication.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

import com.example.mvvmappapplication.App;
import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.utils.DisplayUtil;
import com.example.mvvmappapplication.utils.UIUtil;


/**
 * 공통 팝업
 *
 * Created by kck on 2016-06-30.
 * Update by ehjung on 2017-09-29.
 */
public class CustomDialog extends Dialog {

    private TextView mTitleText;
    private ScrollView mContentLayout;
    private TextView mContentView;
    private TextView mBottomFixedText;
    private Button mPositiveBtn;
    private Button mNegativeBtn;
    private Button mCloseBtn;
    private LinearLayout mBottomBtnLayout;
    private View mBtnLine;
    private LayoutInflater mInflater;

    private int mBtnCnt;

    private boolean isClose = false;

    /**
     * 생성자
     *
     * @param context
     */
    public CustomDialog(Context context) {
        super(context);

        Activity activity = scanForActivity(context);
        if(activity != null) setOwnerActivity(activity);
        setContentView(R.layout.dialog_default);
        initView();
    }

    public CustomDialog(Context context, @LayoutRes int layoutResId) {
        super(context);

        Activity activity = scanForActivity(context);
        if(activity != null) setOwnerActivity(activity);
        setContentView(layoutResId);
    }

    private Activity scanForActivity(Context context) {
        if (context == null)
            return null;
        else if (context instanceof Activity)
            return (Activity)context;
        else if (context instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper)context).getBaseContext());
        return null;
    }

    /**
     * View 초기화
     */
    private void initView() {
        mInflater = LayoutInflater.from(getContext());

        changeWindowSize(false);

        mTitleText = findViewById(R.id.title);
        mContentLayout = findViewById(R.id.contentarea);
        mContentView = findViewById(R.id.contenttext);
        mBottomFixedText = findViewById(R.id.bottomFixedtext);
        mPositiveBtn = findViewById(R.id.btn_positive);
        mNegativeBtn = findViewById(R.id.btn_negative);
        mCloseBtn = findViewById(R.id.closeBtn);
        mBottomBtnLayout = findViewById(R.id.bottomBtnLayout);
        mBtnLine = findViewById(R.id.btnLine);
    }

    /**
     * 다이얼로그 사이즈 변경
     *
     * @param isFull 전체화면 여부
     */
    public void changeWindowSize(boolean isFull) {
        if (isFull) {
            getWindow().setBackgroundDrawable(null);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            if (App.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                getWindow().setLayout((int) (DisplayUtil.getWidthPixels() * 0.88), ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                getWindow().setLayout((int) (DisplayUtil.getWidthPixels() * 0.50), ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (TextUtils.isEmpty(title)) {
            return;
        }
        mTitleText.setText(title);
        mTitleText.setVisibility(View.VISIBLE);
    }

    @Override
    public void setTitle(@StringRes int titleId) {
        mTitleText.setText(titleId);
        mTitleText.setVisibility(View.VISIBLE);
    }

    /**
     * 메세지 입력
     *
     * @param message
     */
    public void setMessage(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        mContentView.setText(message);
    }

    /**
     * 메세지 입력
     *
     * @param messageId
     */
    public void setMessage(@StringRes int messageId) {
        mContentView.setText(messageId);
    }

    /**
     * 하단 고정 메세지 입력
     *
     * @param message
     */
    public void setBottomFixedMessage(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        mBottomFixedText.setText(message);
        mBottomFixedText.setVisibility(View.VISIBLE);
    }

    /**
     * 하단 고정 메세지 입력
     *
     * @param messageId
     */
    public void setBottomFixedMessage(@StringRes int messageId) {
        mBottomFixedText.setText(messageId);
        mBottomFixedText.setVisibility(View.VISIBLE);
    }

    /**
     * 커스텀 뷰 설정
     *
     * @param view
     */
    public void setCustomView(View view) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view);
    }

    public void setCustomView(@LayoutRes int layoutId) {
        View view = mInflater.inflate(layoutId, mContentLayout, false);
        mContentLayout.removeAllViews();
        mContentLayout.addView(view);
    }

    /**
     * PositiveButton 세팅
     *
     * @param text
     * @param listener
     */
    public void setPositiveButton(String text, OnClickListener listener) {
        mPositiveBtn.setText(text);
        setButton(mPositiveBtn, listener);
    }

    public void setPositiveButton(@StringRes int textId, OnClickListener listener) {
        mPositiveBtn.setText(textId);
        setButton(mPositiveBtn, listener);
    }

    /**
     * NegativeButton 세팅
     *
     * @param text
     * @param listener
     */
    public void setNegativeButton(String text, OnClickListener listener) {
        mNegativeBtn.setText(text);
        setButton(mNegativeBtn, listener);
    }

    public void setNegativeButton(@StringRes int textId, OnClickListener listener) {
        mNegativeBtn.setText(textId);
        setButton(mNegativeBtn, listener);
    }


    private void setButton(final View btn, final OnClickListener listener) {
        if (btn == null) {
            return;
        }
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (UIUtil.isDoubleClick()) {
                    return;
                }
                dismiss();
                if (listener != null) {
                    if (v.getId() == R.id.btn_positive) {
                        listener.onClick(CustomDialog.this, DialogInterface.BUTTON_POSITIVE);
                    } else if (v.getId() == R.id.btn_negative) {
                        listener.onClick(CustomDialog.this, DialogInterface.BUTTON_NEGATIVE);
                    }
                }
            }
        });

        mBtnCnt++;
    }

    public void setCancelable(boolean flag, final OnClickListener listener) {
        super.setCancelable(flag);
        if (flag) {
            setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (listener != null) {
                        listener.onClick(CustomDialog.this, DialogInterface.BUTTON_NEUTRAL);
                    }
                }
            });
        }
    }

    public void setCloseBtn(boolean flag) {
        isClose = flag;
    }

    public void setVisibilityToCloseBtn(int visible) {
        mCloseBtn.setVisibility(visible);
        if (mCloseBtn.getVisibility() == View.VISIBLE) {
            mCloseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    @Override
    public void show() {
        if (isClose) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mTitleText.getLayoutParams();
            layoutParams.setMargins(
                    UIUtil.getPxFromDp(R.dimen.content_margin), layoutParams.topMargin,
                    UIUtil.getPxFromDp(R.dimen.content_margin), layoutParams.bottomMargin);
            mTitleText.setGravity(Gravity.START | Gravity.BOTTOM);
            mContentView.setGravity(Gravity.START);
            mCloseBtn.setVisibility(View.VISIBLE);
            mCloseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        if (mBtnCnt == 0 && mBottomBtnLayout != null){
            mBottomBtnLayout.setVisibility(View.GONE);
        }
        if (mBtnCnt > 1) {
            mBtnLine.setVisibility(View.VISIBLE);
        }

        if (UIUtil.checkActivityRunningState(getOwnerActivity())) {
            try {
                super.show();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void setOnCancelListener(OnCancelListener listener) {
        super.setOnCancelListener(listener);
    }
}