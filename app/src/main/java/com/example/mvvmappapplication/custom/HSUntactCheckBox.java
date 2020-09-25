package com.example.mvvmappapplication.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.AttrRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.utils.LogUtil;
import com.example.mvvmappapplication.utils.ResourceUtil;
import com.example.mvvmappapplication.utils.ThemeUtil;


public class HSUntactCheckBox extends AppCompatCheckBox implements ThemeUtil.ITheme {

    @StyleRes
    int mStyle;
    @AttrRes
    int mTextColor;
    @AttrRes
    int mBackground;
    @AttrRes
    int mDrawableLeft;
    @AttrRes
    int mDrawableTop;
    @AttrRes
    int mDrawableRight;
    @AttrRes
    int mDrawableBottom;

    public HSUntactCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public HSUntactCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        mStyle = attrs.getStyleAttribute();
        mTextColor = ThemeUtil.getAttributeValue(attrs, ATTR_TEXT_COLOR);
        mBackground = ThemeUtil.getAttributeValue(attrs, ATTR_BACKGROUND);
        mDrawableLeft = ThemeUtil.getAttributeValue(attrs, ATTR_DRAWABLE_LEFT);
        mDrawableTop = ThemeUtil.getAttributeValue(attrs, ATTR_DRAWABLE_TOP);
        mDrawableRight = ThemeUtil.getAttributeValue(attrs, ATTR_DRAWABLE_RIGHT);
        mDrawableBottom = ThemeUtil.getAttributeValue(attrs, ATTR_DRAWABLE_BOTTOM);
    }

    public void setTextColorAttr(@AttrRes int resAttrId) {
        try {
            mTextColor = resAttrId;
            ColorStateList resId = ResourceUtil.getThemeColorStateList(mTextColor);
            if (resId != null) {
                super.setTextColor(resId);
            }
        } catch (Exception e) {
            LogUtil.e("[THEME] " + e.toString());
        }
    }

    public void setBackgroundResourceAttr(@AttrRes int resAttrId) {
        try {
            mBackground = resAttrId;
            ThemeUtil.updateThemeToBackground(this, mBackground);
        } catch (Exception e) {
        }
    }

    public void setCompoundDrawablesWithIntrinsicBoundsAttr(
            @AttrRes int leftResAttrId, @AttrRes int topResAttrId, @AttrRes int rightResAttrId, @AttrRes int bottomResAttrId) {
        mDrawableLeft = leftResAttrId;
        mDrawableTop = topResAttrId;
        mDrawableRight = rightResAttrId;
        mDrawableBottom = bottomResAttrId;
        Drawable drawableLeft = null;
        Drawable drawableTop = null;
        Drawable drawableRight = null;
        Drawable drawableBottom = null;
        if (leftResAttrId != 0) {
            drawableLeft = ResourceUtil.getThemeDrawable(leftResAttrId);
        }
        if (topResAttrId != 0) {
            drawableTop = ResourceUtil.getThemeDrawable(topResAttrId);
        }
        if (rightResAttrId != 0) {
            drawableRight = ResourceUtil.getThemeDrawable(rightResAttrId);
        }
        if (bottomResAttrId != 0) {
            drawableBottom = ResourceUtil.getThemeDrawable(bottomResAttrId);
        }
        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

    @Override
    public void onChangeTheme() {
        if (mTextColor == 0) {
            mTextColor = R.attr.checkbox_textcolor;
        }
        setTextColorAttr(mTextColor);
        if (mBackground == 0) {
            setButtonDrawable(ResourceUtil.getThemeDrawable(R.attr.checkbox_selector));
        } else {
            setBackgroundResourceAttr(mBackground);
        }
        setCompoundDrawablesWithIntrinsicBoundsAttr(mDrawableLeft, mDrawableTop, mDrawableRight, mDrawableBottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private Drawable drawableRight;
    private Drawable drawableLeft;
    private Drawable drawableTop;
    private Drawable drawableBottom;

    int actionX, actionY;

    private DrawableClickListener clickListener;
    public interface DrawableClickListener {

        public static enum DrawablePosition { TOP, BOTTOM, LEFT, RIGHT };
        public void onClick(DrawablePosition target, int id, String text);
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top,
                                     Drawable right, Drawable bottom) {
        if (left != null) {
            drawableLeft = left;
        }
        if (right != null) {
            drawableRight = right;
        }
        if (top != null) {
            drawableTop = top;
        }
        if (bottom != null) {
            drawableBottom = bottom;
        }
        super.setCompoundDrawables(left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Rect bounds;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            actionX = (int) event.getX();
            actionY = (int) event.getY();
            if (drawableBottom != null
                    && drawableBottom.getBounds().contains(actionX, actionY)) {
                clickListener.onClick(DrawableClickListener.DrawablePosition.BOTTOM, this.getId(), this.getText().toString());
                return super.onTouchEvent(event);
            }

            if (drawableTop != null
                    && drawableTop.getBounds().contains(actionX, actionY)) {
                clickListener.onClick(DrawableClickListener.DrawablePosition.TOP, this.getId(), this.getText().toString());
                return super.onTouchEvent(event);
            }

            // this works for left since container shares 0,0 origin with bounds
            if (drawableLeft != null) {
                bounds = null;
                bounds = drawableLeft.getBounds();

                int x, y;
                int extraTapArea = (int) (13 * getResources().getDisplayMetrics().density  + 0.5);

                x = actionX;
                y = actionY;

                if (!bounds.contains(actionX, actionY)) {
                    /** Gives the +20 area for tapping. */
                    x = (int) (actionX - extraTapArea);
                    y = (int) (actionY - extraTapArea);

                    if (x <= 0)
                        x = actionX;
                    if (y <= 0)
                        y = actionY;

                    /** Creates square from the smallest value */
                    if (x < y) {
                        y = x;
                    }
                }

                if (bounds.contains(x, y) && clickListener != null) {
                    clickListener
                            .onClick(DrawableClickListener.DrawablePosition.LEFT, this.getId(), this.getText().toString());
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    return false;

                }
            }

            if (drawableRight != null) {

                bounds = null;
                bounds = drawableRight.getBounds();

                int x, y;
//                int extraTapArea = 13;
                int extraTapArea = 33;

                /**
                 * IF USER CLICKS JUST OUT SIDE THE RECTANGLE OF THE DRAWABLE
                 * THAN ADD X AND SUBTRACT THE Y WITH SOME VALUE SO THAT AFTER
                 * CALCULATING X AND Y CO-ORDINATE LIES INTO THE DRAWBABLE
                 * BOUND. - this process help to increase the tappable area of
                 * the rectangle.
                 */
                x = (int) (actionX + extraTapArea);
                y = (int) (actionY - extraTapArea);

                /**Since this is right drawable subtract the value of x from the width
                 * of view. so that width - tappedarea will result in x co-ordinate in drawable bound.
                 */
                x = getWidth() - x;

                /*x can be negative if user taps at x co-ordinate just near the width.
                 * e.g views width = 300 and user taps 290. Then as per previous calculation
                 * 290 + 13 = 303. So subtract X from getWidth() will result in negative value.
                 * So to avoid this add the value previous added when x goes negative.
                 */

                if(x <= 0){
                    x += extraTapArea;
                }

                /* If result after calculating for extra tappable area is negative.
                 * assign the original value so that after subtracting
                 * extratapping area value doesn't go into negative value.
                 */

                if (y <= 0)
                    y = actionY;

                /**If drawble bounds contains the x and y points then move ahead.*/
                if (bounds.contains(x, y) && clickListener != null) {
                    clickListener
                            .onClick(DrawableClickListener.DrawablePosition.RIGHT, this.getId(), this.getText().toString());
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    return false;
                }
                return super.onTouchEvent(event);
            }

        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        drawableRight = null;
        drawableBottom = null;
        drawableLeft = null;
        drawableTop = null;
        super.finalize();
    }

    public void setDrawableClickListener(DrawableClickListener listener) {
        this.clickListener = listener;
    }
}