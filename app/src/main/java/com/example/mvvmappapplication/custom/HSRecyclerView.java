package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewConfiguration;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

public class HSRecyclerView extends RecyclerView {

    /*******************************************************************************
     * interface
     *******************************************************************************/
    /**
     * 추가 데이터 로딩 리스너.
     */
    public interface IEndlessScrollListener {
        void onLoadMore(String nextPageKey);
    }

    /**
     * 스크롤 방향 리스너.
     */
    public interface IUpAndDownScrollListener {
        void onUpAndDown(boolean isUp, int lastVisibleposition);
    }

    /*******************************************************************************
     * Constant.
     *******************************************************************************/
    /**
     * The minimum amount of items to have below your current scroll position before.
     */
    private static final int VISIBLE_THRESHOLD = 5;

    public static final String LOAD_ERROR = "ERROR";

    /*******************************************************************************
     * Variable.
     *******************************************************************************/

    private LinearLayoutManager mLayoutManager;

    private IEndlessScrollListener mEndlessScrollListener;
    private IUpAndDownScrollListener mUpAndDownScrollListener;

    private int mScaledTouchSlop = 0;
    /**
     * True if we are still waiting for the last set of data to load..
     */
    private boolean loading = true;

    private boolean mIsUpState = false;
    private int mDistance = 0;

    /**
     * 전체 페이지 개수.
     */
    private String mNextPageKey;

    public HSRecyclerView(Context context) {
        super(context);
        init();
    }

    public HSRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HSRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        mLayoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(mLayoutManager);
        ((SimpleItemAnimator) getItemAnimator()).setSupportsChangeAnimations(false);
    }

    public void setEndlessScrollListener(IEndlessScrollListener listener) {
        mEndlessScrollListener = listener;
        super.addOnScrollListener(mOnScrollListener);
    }

    public void setUpAndDownScrollListener(IUpAndDownScrollListener listener) {
        mUpAndDownScrollListener = listener;
    }

    private int getItemCount() {
        return getLayoutManager() == null ? 0 : getLayoutManager().getItemCount();
    }

    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = recyclerView.getChildCount();
            int totalItemCount = getItemCount();
            int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

            // Log.d("JEH", "========================================");
            // Log.d("JEH", "visibleItemCount=" + visibleItemCount);
            // Log.d("JEH", "firstVisibleItem=" + firstVisibleItem);
            // Log.d("JEH", "totalItemCount=" + totalItemCount);

            if (mEndlessScrollListener != null) {
                if ((!loading && !(TextUtils.isEmpty(mNextPageKey) || LOAD_ERROR.equals(mNextPageKey)))
                        && ((totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD))) {
                    // End has been reached
                    // Do something
                    loading = true;
                    mEndlessScrollListener.onLoadMore(mNextPageKey);
                }
            }

            if (mUpAndDownScrollListener != null) {
                if ((dy < 0) && !mIsUpState || (dy > 0) && mIsUpState) {
                    mDistance = 0;
                }
                mDistance += dy;

                int lastVisibleItem = firstVisibleItem + visibleItemCount - 1;
                if (dy < 0) {
                    // UP
                    if (mScaledTouchSlop < Math.abs(mDistance)) {
                        mIsUpState = true;
                    }
                    mUpAndDownScrollListener.onUpAndDown(true, lastVisibleItem);
                } else if (0 < dy) {
                    // DOWN
                    if (mScaledTouchSlop < mDistance) {
                        mIsUpState = false;
                    }
                    mUpAndDownScrollListener.onUpAndDown(false, lastVisibleItem);
                }
            }
        }
    };

    /**
     * 상태 정보 clear.
     */
    public void initState() {
        mNextPageKey = "";
        loading = true;
        mIsUpState = false;
        mDistance = 0;
    }

    public void setNextPageKey(String nextPageKey) {
        mNextPageKey = nextPageKey;
        loading = false;
    }
}