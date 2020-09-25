package com.example.mvvmappapplication.custom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.mvvmappapplication.App;
import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.consts.Const;


//todo 테마적용
public class SwipeButton extends RelativeLayout {
    private ImageView slidingButton;
    private float initialX;
    private boolean active;
    private int initialButtonWidth;
    private TextView centerText;

    private Drawable disabledDrawable;
    private Drawable enabledDrawable;

    private int bgDrawableId;
    private int btnDrawableId;

    private OnStateChangeListener onStateChangeListener;
    private OnActiveListener onActiveListener;

    private final int margin = 15;

    private boolean btnEnabled = false;

    private RelativeLayout background;

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public void setOnActiveListener(OnActiveListener onActiveListener) {
        this.onActiveListener = onActiveListener;
    }

    public SwipeButton(Context context) {
        super(context);

        init(context, null, -1, -1);
    }

    public SwipeButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, -1, -1);
    }

    public SwipeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr, -1);
    }

    @TargetApi(21)
    public SwipeButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        background = new RelativeLayout(context);

        LayoutParams layoutParamsView = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                50 * 3);

        layoutParamsView.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

//        //테마구분
//        if(App.getThemeType() == Const.eThemeType.DARK) {
//            //bgDrawableId = drawable.drawable_swipebtn_corner_rectangle_desabled;
//            bgDrawableId = drawable.drawable_swipebtn_corner_rectangle_off;
//            btnDrawableId = drawable.btn_swipe;
//            disabledDrawable = ContextCompat.getDrawable(getContext(), drawable.btn_i_swipe_off);//btn_i_swipe_on
//            enabledDrawable = ContextCompat.getDrawable(getContext(), drawable.check_icon_n);
//
//        } else {
////            bgDrawableId = drawable.drawable_swipebtn_corner_rectangle_desabled;
//            bgDrawableId = drawable.drawable_swipebtn_corner_rectangle_off;
//            btnDrawableId = drawable.btn_swipe;
//            disabledDrawable = ContextCompat.getDrawable(getContext(), drawable.btn_i_swipe_off);
//            enabledDrawable = ContextCompat.getDrawable(getContext(), drawable.check_icon_n);
//        }

        background.setBackground(ContextCompat.getDrawable(context, bgDrawableId));

        addView(background, layoutParamsView);

        final HSTextView centerText = new HSTextView(context);
        this.centerText = centerText;

        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        centerText.setText("밀어서 확인하고 결제를 진행합니다");
        centerText.setTextColor(Color.WHITE);
        centerText.setPadding(35, 35, 55, 35);
        centerText.setTextColor(getResources().getColor(R.color.color_ffffff));
        centerText.setTextSize(12);
        centerText.setTypeface(Typekit.getInstance().get(Typekit.Style.Normal));

        background.addView(centerText, layoutParams);

        final ImageView swipeButton = new ImageView(context);
        this.slidingButton = swipeButton;

        slidingButton.setImageDrawable(disabledDrawable);

        LayoutParams layoutParamsButton = new LayoutParams(
                42*3,
                42*3);

        layoutParamsButton.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParamsButton.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        layoutParamsButton.leftMargin = margin;
        swipeButton.setBackground(ContextCompat.getDrawable(context, btnDrawableId));
        swipeButton.setImageDrawable(disabledDrawable);
        addView(swipeButton, layoutParamsButton);

        setOnTouchListener(getButtonTouchListener());

    }

    public void setEnable(Context context, boolean enable) {
        btnEnabled = enable;

//        if(!btnEnabled) {
//            bgDrawableId = android.R.drawable.drawable_swipebtn_corner_rectangle_off;
//            disabledDrawable = ContextCompat.getDrawable(getContext(), drawable.btn_i_swipe_off);
//            centerText.setTextColor(getResources().getColor(R.color.color_ffffff));
//
//
//        } else {
//            bgDrawableId = drawable.drawable_swipebtn_corner_rectangle_on;
//            disabledDrawable = ContextCompat.getDrawable(getContext(), drawable.btn_i_swipe_on);
//            centerText.setTextColor(getResources().getColor(color.color_ffffff));
//
//        }

        background.setBackground(ContextCompat.getDrawable(context, bgDrawableId));
        slidingButton.setImageDrawable(disabledDrawable);

        invalidate();
    }

    private OnTouchListener getButtonTouchListener() {
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(btnEnabled) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            if (initialX == margin) {
                                initialX = slidingButton.getX();
                            }
                            if (event.getX() > initialX + slidingButton.getWidth() / 2 &&
                                    event.getX() + slidingButton.getWidth() / 2 < getWidth()) {
                                slidingButton.setX(event.getX() - slidingButton.getWidth() / 2);
                                centerText.setAlpha(1 - 1.3f * (slidingButton.getX() + slidingButton.getWidth()) / getWidth());
                            }

                            if (event.getX() + slidingButton.getWidth() / 2 > getWidth() &&
                                    slidingButton.getX() + slidingButton.getWidth() / 2 < getWidth()) {
                                slidingButton.setX(getWidth() - slidingButton.getWidth());
                            }

                            if (event.getX() < slidingButton.getWidth() / 2 &&
                                    slidingButton.getX() > margin) {
                                slidingButton.setX(margin);
                            }
                            return true;
                        case MotionEvent.ACTION_UP:
                            if (active) {
                                collapseButton();
                            } else {
                                initialButtonWidth = slidingButton.getWidth();

                                if (slidingButton.getX() + slidingButton.getWidth() > getWidth() * 0.85) {
                                    expandButton();
                                } else {
                                    moveButtonBack();
                                }
                            }
                            return true;
                    }
                    return false;
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return true;
                }


            }
        };
    }

    private void expandButton() {
        final ValueAnimator positionAnimator =
                ValueAnimator.ofFloat(slidingButton.getX(), margin);
        positionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (Float) positionAnimator.getAnimatedValue();
                slidingButton.setX(x);
            }
        });


        final ValueAnimator widthAnimator = ValueAnimator.ofInt(
                slidingButton.getWidth(),
                getWidth() - (margin * 2 ));

        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams params = slidingButton.getLayoutParams();
                params.width = (Integer) widthAnimator.getAnimatedValue();
                slidingButton.setLayoutParams(params);
            }
        });


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                active = true;
                slidingButton.setImageDrawable(enabledDrawable);

                if (onStateChangeListener != null) {
                    onStateChangeListener.onStateChange(active);
                }

                if (onActiveListener != null) {
                    onActiveListener.onActive();
                }
            }
        });

        animatorSet.playTogether(positionAnimator, widthAnimator);
        animatorSet.start();
    }

    public void collapseButton() {
        final ValueAnimator widthAnimator = ValueAnimator.ofInt(
                slidingButton.getWidth(),
                initialButtonWidth);

        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams params =  slidingButton.getLayoutParams();
                params.width = (Integer) widthAnimator.getAnimatedValue();
                slidingButton.setLayoutParams(params);
            }
        });

        widthAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                active = false;
                slidingButton.setImageDrawable(disabledDrawable);
            }
        });

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(
                centerText, "alpha", 1);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(objectAnimator, widthAnimator);
        animatorSet.start();
    }


    public void moveButtonBack() {
        final ValueAnimator positionAnimator =
                ValueAnimator.ofFloat(slidingButton.getX(), margin);
        positionAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        positionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (Float) positionAnimator.getAnimatedValue();
                slidingButton.setX(x);
            }
        });

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(
                centerText, "alpha", 1);

        positionAnimator.setDuration(200);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator, positionAnimator);
        animatorSet.start();
    }
}