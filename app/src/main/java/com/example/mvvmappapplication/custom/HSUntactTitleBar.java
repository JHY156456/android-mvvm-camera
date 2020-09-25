package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import com.example.mvvmappapplication.App;
import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.consts.Const;
import com.example.mvvmappapplication.databinding.LayoutUntactCommonTitlebarBinding;
import com.example.mvvmappapplication.utils.ThemeUtil;
import com.example.mvvmappapplication.utils.UIUtil;


public class HSUntactTitleBar extends RelativeLayout implements ThemeUtil.ITheme {
    private static final String TAG = HSUntactTitleBar.class.getSimpleName();

    public enum TitleBarType {
        CUSTOM,
        BASE,
        BACK1,
        BACK2,
        CLOSE,
        NONE
    }

    private TitleBarType mTitleBarType;
    private String mTitleName;

    @AttrRes
    private int mTitleBg;
    private boolean mTitleClickable;
    private boolean mUnderline;
    private boolean mMenuBack;
    private boolean mMenuClose;
    private boolean mMenuAdmin;
    private boolean mAreaDescription;

    Const.eThemeType mThemeType;

    LayoutUntactCommonTitlebarBinding mBinding;

    @Override
    public void onChangeTheme() {
        if (mThemeType != App.getThemeType()) {
            ThemeUtil.updateTheme(mBinding.clUntactTitle);
            mThemeType = App.getThemeType();
        }
    }

    public HSUntactTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HSUntactTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HSUntactTitleBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttributeSet(attrs);
        initViews();
    }

    private void initAttributeSet(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.HSUntactTitleBar, 0, 0);
        try {
            int backIconType = a.getInteger(R.styleable.HSUntactTitleBar_titleBarType, TitleBarType.CUSTOM.ordinal());
            for (TitleBarType type : TitleBarType.values()) {
                if (type.ordinal() == backIconType) {
                    mTitleBarType = type;
                    break;
                }
            }

            mTitleBg = ThemeUtil.getAttributeValueCustom(attrs, "titleBg");
            mTitleName = a.getString(R.styleable.HSUntactTitleBar_title);
            mTitleClickable = a.getBoolean(R.styleable.HSUntactTitleBar_titleClickable, false);
            mUnderline = a.getBoolean(R.styleable.HSUntactTitleBar_underline, false);
            mMenuBack = a.getBoolean(R.styleable.HSUntactTitleBar_menuBack, false);
            mMenuClose = a.getBoolean(R.styleable.HSUntactTitleBar_menuClose, false);
            mMenuAdmin = a.getBoolean(R.styleable.HSUntactTitleBar_menuAdmin, false);
            mAreaDescription = a.getBoolean(R.styleable.HSUntactTitleBar_areaDescription, false);
        } finally {
            a.recycle();
        }
    }

    private void initViews() {
        mThemeType = App.getThemeType();
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_untact_common_titlebar, this, true);

        refreshTitleBarType(mTitleBarType, mTitleName, "test", mMenuBack, mMenuClose, mMenuAdmin, mUnderline, mAreaDescription);
        mBinding.hsntvUntactTitleName.setOnClickListener(mOnClickListener);
        mBinding.hsnbtnUntactTitleBack.setOnClickListener(mOnClickListener);
        mBinding.hsnbtnUntactTitleClose.setOnClickListener(mOnClickListener);
        mBinding.hsnbtnUntactTitleAdmin.setOnClickListener(mOnClickListener);
    }


    /* TitleBar Type */
    public void setTitleBarType(TitleBarType _type) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        switch (_type) {
            case BASE:
                params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                mBinding.hsntvUntactTitleName.setLayoutParams(params);
                mBinding.hsnbtnUntactTitleAdmin.setVisibility(GONE);
                mBinding.hsnbtnUntactTitleBack.setVisibility(GONE);
                mBinding.hsnbtnUntactTitleClose.setVisibility(GONE);
                break;

            case BACK1:
                params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                mBinding.hsntvUntactTitleName.setLayoutParams(params);
                mBinding.hsnbtnUntactTitleAdmin.setVisibility(GONE);
                mBinding.hsnbtnUntactTitleBack.setVisibility(VISIBLE);
                mBinding.hsnbtnUntactTitleClose.setVisibility(GONE);
                break;

            case BACK2:
                params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                mBinding.hsntvUntactTitleName.setLayoutParams(params);
                mBinding.hsnbtnUntactTitleAdmin.setVisibility(VISIBLE);
                mBinding.hsnbtnUntactTitleBack.setVisibility(VISIBLE);
                mBinding.hsnbtnUntactTitleClose.setVisibility(GONE);
                break;

            case CLOSE:
                params.leftMargin = UIUtil.getPxFromDp(R.dimen.dimen_18);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                mBinding.hsntvUntactTitleName.setLayoutParams(params);
                mBinding.hsnbtnUntactTitleAdmin.setVisibility(GONE);
                mBinding.hsnbtnUntactTitleBack.setVisibility(GONE);
                mBinding.hsnbtnUntactTitleClose.setVisibility(VISIBLE);
                break;

            default:
                params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                mBinding.hsntvUntactTitleName.setLayoutParams(params);
                mBinding.hsnbtnUntactTitleAdmin.setVisibility(VISIBLE);
                mBinding.hsnbtnUntactTitleBack.setVisibility(VISIBLE);
                mBinding.hsnbtnUntactTitleClose.setVisibility(VISIBLE);
                break;
        }
    }

    /* Title Name */
    public void setTitleName(String _title) {
        mBinding.hsntvUntactTitleName.setText(_title);
    }

    /* Title Desc */
    public void setTitleDesc(String _desc) {
        mBinding.hsntvUntactTitleDesc.setText(_desc);
    }

    /* Refresh */
    public void refreshTitleBarType(TitleBarType _type, String _title, String _desc
            , boolean _menuBack, boolean _menuClose, boolean _menuAdmin
            , boolean _underLine, boolean _areaDesc) {

        if (_type.equals(TitleBarType.NONE)){
            mBinding.getRoot().setVisibility(GONE);
            return;
        }
        else{
            mBinding.getRoot().setVisibility(VISIBLE);
        }

        setTitleBarType(_type);
        setTitleName(_title);
        setTitleDesc(_desc);

        if (_type.equals(TitleBarType.CUSTOM)){
            mBinding.hsnbtnUntactTitleBack.setVisibility(_menuBack?VISIBLE:GONE);
            mBinding.hsnbtnUntactTitleClose.setVisibility(_menuClose?VISIBLE:GONE);
            mBinding.hsnbtnUntactTitleAdmin.setVisibility(_menuAdmin?VISIBLE:GONE);
        }
        mBinding.vUntactTitleUnderline.setVisibility(_underLine?VISIBLE:GONE);
        mBinding.rlUntactTitleContainerAreaDown.setVisibility(_areaDesc?VISIBLE:GONE);
        mBinding.hsuvUntactTitleBottomLine.setVisibility(_areaDesc?VISIBLE:GONE);
    }

    public void refreshAdminBtn(boolean _isSelect) {
        mBinding.hsnbtnUntactTitleAdmin.setSelected(_isSelect);
        mBinding.hsnbtnUntactTitleAdmin.setCompoundDrawablesWithIntrinsicBounds(0, 0, _isSelect?R.drawable.untact_ic_text_draw_check:0, 0);
    }

    public void refreshDescription(int _offset) {
        mBinding.rlUntactTitleContainerAreaDown.setY(mBinding.rlUntactTitleContainerAreaDown.getTop()-_offset);
    }


    /***********************************************************************************************************************************/
    // Listener
    /***********************************************************************************************************************************/
    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (UIUtil.isDoubleClick()) {
                return;
            }
            switch (v.getId()) {
                case R.id.hsntv_untact_title_name:
                    if (!mTitleClickable) return;
                    break;

                case R.id.hsnbtn_untact_title_back:
                case R.id.hsnbtn_untact_title_close:
                    App.getCurrentActivity().onBackPressed();
                    break;

            }
        }
    };
}