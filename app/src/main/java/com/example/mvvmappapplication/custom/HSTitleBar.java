package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;

import com.example.mvvmappapplication.App;
import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.consts.Const;
import com.example.mvvmappapplication.consts.MainEvent;
import com.example.mvvmappapplication.databinding.LayoutCommonTitlebarBinding;
import com.example.mvvmappapplication.utils.LogUtil;
import com.example.mvvmappapplication.utils.ResourceUtil;
import com.example.mvvmappapplication.utils.ThemeUtil;
import com.example.mvvmappapplication.utils.UIUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.grantland.widget.AutofitHelper;


/**
 * Created by ehjung on 2017. 9. 5..
 * Updated by tyKim on 2019. 7. 11..
 */
public class HSTitleBar extends RelativeLayout implements ThemeUtil.ITheme {

    public enum eBackIconType {
        NONE, BACK, CLOSE
    }

    private eBackIconType mBackIconType;
    private boolean mIsSearchMenu;
    private boolean mIsCartMenu;
    private String mTitleName;
    private boolean mIsTitleClickable;
    @AttrRes
    private int mTitleBackground;
    private boolean mIsSimulTag;
    private boolean mIsUnderline;

    Const.eThemeType mThemeType;

    LayoutCommonTitlebarBinding mBinding;

    public HSTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HSTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HSTitleBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttributeSet(attrs);
        initViews();
    }

    private void initAttributeSet(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.HSTitleBar, 0, 0);
        try {
            int backIconType = a.getInteger(R.styleable.HSTitleBar_backIconType, eBackIconType.NONE.ordinal());
            for (eBackIconType type : eBackIconType.values()) {
                if (type.ordinal() == backIconType) {
                    mBackIconType = type;
                    break;
                }
            }
            mTitleName = a.getString(R.styleable.HSTitleBar_title);
            mIsUnderline = a.getBoolean(R.styleable.HSTitleBar_underline, false);
            mTitleBackground = ThemeUtil.getAttributeValueCustom(attrs, "titleBackground");

            mIsSearchMenu = a.getBoolean(R.styleable.HSTitleBar_searchMenu, false);
            mIsCartMenu = a.getBoolean(R.styleable.HSTitleBar_cartMenu, false);
            mIsTitleClickable = a.getBoolean(R.styleable.HSTitleBar_titleClickable, false);
            mIsSimulTag = a.getBoolean(R.styleable.HSTitleBar_experienceTag, false);

        } finally {
            a.recycle();
        }
    }

    private void initViews() {
        mThemeType = App.getThemeType();
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_common_titlebar, this, true);

        /* Back 버튼 타입 */
        setBackIconType(mBackIconType);
        mBinding.titleBackBtn.setOnClickListener(mOnClickListener);
        mBinding.titleCloseBtn.setOnClickListener(mOnClickListener);
        setTitleBackgroundAttr(mTitleBackground);

        /* 타이틀 */
        setTitle(mTitleName);
        if (mIsTitleClickable) {
            try {
                setTitleClickListener((OnClickListener) App.getCurrentActivity());
            } catch (Exception e) {
                LogUtil.e(e);
            }
        }

//        if (mIsSimulTag) {
//            mBinding.titleExperienceTag.setVisibility(HSLoginUtil.getInstance().isSNSMember() ? View.VISIBLE : View.GONE);
//        }

        /* underline */
        setUnderLineVisibility(mIsUnderline);

        /* 검색 */
        mBinding.titleSearchBtn.setVisibility(mIsSearchMenu ? View.VISIBLE : View.GONE);
        if (mBinding.titleSearchBtn.getVisibility() == View.VISIBLE) {
            mBinding.titleSearchBtn.setOnClickListener(mOnClickListener);
        }

//        /* 장바구니 */
//        mBinding.titleCartBtn.setVisibility(mIsCartMenu ? View.VISIBLE : View.GONE);
//        if (mBinding.titleCartBtn.getVisibility() == View.VISIBLE) {
//            if (!EventBus.getDefault().isRegistered(this)) {
//                EventBus.getDefault().register(this);
//            }
//            setCartCountBadge(HSCartManager.getInstance().getProductCartListSize());
//
//            mBinding.titleCartBtn.setOnClickListener(mOnClickListener);
//            mBinding.titleCloseBtn.setVisibility(GONE);
//        }

        /* 타이틀 Align - Back버튼 사용할경우 타이틀 가운데 맞춤 */
        if (mBackIconType == eBackIconType.BACK) {
            mBinding.titleNameLayout.setGravity(Gravity.CENTER);
        }
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (UIUtil.isDoubleClick()) {
                return;
            }
            switch (v.getId()) {
                case R.id.titleBackBtn:
                case R.id.titleCloseBtn:
                    App.getCurrentActivity().onBackPressed();
                    break;

            }
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDetachedFromWindow();
    }

    /**
     * 타이틀 변경
     *
     * @param title
     */
    public void setTitle(String title) {
        mBinding.titleName.setText(title);
//        if (!mIsSimulTag && !mIsTitleClickable) {
//            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mBinding.titleName.getLayoutParams();
//            layoutParams.width = LayoutParams.MATCH_PARENT;
//            mBinding.titleName.setLayoutParams(layoutParams);
//            AutofitHelper.create(mBinding.titleName);
//        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mBinding.titleName.getLayoutParams();
        layoutParams.width = LayoutParams.MATCH_PARENT;
        mBinding.titleName.setLayoutParams(layoutParams);
        AutofitHelper.create(mBinding.titleName);
        if (mIsSimulTag || mIsTitleClickable) {
            layoutParams.width = LayoutParams.WRAP_CONTENT;
            mBinding.titleName.setLayoutParams(layoutParams);
        }
        if (!TextUtils.isEmpty(title)) {
            mBinding.titleName.setVisibility(VISIBLE);
        }
    }

    public void setTitle(@StringRes int titleResId) {
        setTitle(ResourceUtil.getString(titleResId));
    }

    public void setTitleClickListener(OnClickListener listener) {
        mIsTitleClickable = listener != null;
        initTitleClickable();
        mBinding.titleName.setOnClickListener(listener);
    }

    public void setBackButtonClickListener(OnClickListener listener) {
        mBinding.titleBackBtn.setOnClickListener(listener);
    }


    /**
     * Back 버튼 타입 변경
     *
     * @param type
     */
    public void setBackIconType(eBackIconType type) {
        mBackIconType = type;
        switch (type) {
            case BACK:
                mBinding.titleNameLayout.setGravity(Gravity.CENTER);
                mBinding.titleName.setGravity(Gravity.CENTER);
                mBinding.titleBackBtn.setVisibility(View.VISIBLE);
                if (!mIsCartMenu && !mIsSearchMenu) {
                    mBinding.titleCloseBtn.setVisibility(INVISIBLE);
                }
                break;

            case CLOSE:
                mBinding.titleNameLayout.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                mBinding.titleName.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                mBinding.titleBackBtn.setVisibility(View.GONE);
                mBinding.titleCloseBtn.setVisibility(VISIBLE);
                break;

            default:
                mBinding.titleCloseBtn.setVisibility(GONE);
                mBinding.titleNameLayout.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                mBinding.titleName.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                mBinding.titleBackBtn.setVisibility(View.GONE);
                break;
        }
    }

    public void setUnderLineVisibility(boolean isVisible) {
        if (isVisible) {
            mBinding.underlineV.setVisibility(View.VISIBLE);
        } else {
            mBinding.underlineV.setVisibility(View.GONE);
        }
    }

    private void initTitleClickable() {
        int arrowDrawable = 0;
        if (mIsTitleClickable) {
            arrowDrawable = R.attr.btn_drop_more_b;
            try {
                mBinding.titleName.setOnClickListener((OnClickListener) App.getCurrentActivity());
            } catch (Exception e) {
            }
        }
        mBinding.titleName.setCompoundDrawablesWithIntrinsicBoundsAttr(0, 0, arrowDrawable, 0);
    }

    public void setTitleBackgroundAttr(int titleBackground) {
        mTitleBackground = titleBackground;
        if (mTitleBackground != 0) {
            mBinding.backgroundV.setBackgroundResourceAttr(mTitleBackground);
        }
    }

    public void setCartCountBadge(int count) {
        if (count <= 0) {
            mBinding.titleCartCountBadge.setVisibility(View.GONE);
        } else {
            mBinding.titleCartCountBadge.setText(String.valueOf(count));
            mBinding.titleCartCountBadge.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 테마 변경
     */
    @Override
    public void onChangeTheme() {
        if (mThemeType != App.getThemeType()) {
            ThemeUtil.updateTheme(mBinding.titleLayout);
            initTitleClickable();
            mThemeType = App.getThemeType();
        }
    }

    /********************************************************************************************************
     * Event Bus
     *********************************************************************************************************/
    @Subscribe
    public void onEvent(final MainEvent event) {
        switch (event.mEvent) {
            case MainEvent.EVENT_CART_COUNT_ACTION: /* 카트 카운트 */
                setCartCountBadge((Integer) event.mParam);
                break;
        }
    }

}