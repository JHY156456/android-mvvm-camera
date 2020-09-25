package com.example.mvvmappapplication.utils;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

public class CommonUtil {
    public static void animationSpead(final View _view, boolean _hide, long _duration){
        if (!_hide){
            _view.setVisibility(View.VISIBLE);
            int widthSpec = View.MeasureSpec.makeMeasureSpec(_view.getWidth(), View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            _view.measure(widthSpec, heightSpec);
        }
        ValueAnimator anim = ValueAnimator.ofInt(_hide?_view.getMeasuredHeight():0, _hide?0:_view.getMeasuredHeight());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = _view.getLayoutParams();
                layoutParams.height = val;
                _view.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(_duration);
        anim.start();
    }
}
