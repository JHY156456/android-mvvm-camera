package com.example.mvvmappapplication.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.mvvmappapplication.App;
import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.consts.Const;


/**
 * Created by kck on 2016-07-04.
 */
public class ProgressDialogBase extends ProgressDialog {


    public static ProgressDialogBase show(Context context, CharSequence title,
                                          CharSequence message) {
        return show(context, title, message, false);
    }

    public static ProgressDialogBase show(Context context, CharSequence title,
                                          CharSequence message, boolean indeterminate) {
        return show(context, title, message, indeterminate, false);
    }

    public static ProgressDialogBase show(Context context, CharSequence title,
                                          CharSequence message, boolean indeterminate,
                                          boolean cancelable) {
        ProgressDialogBase dialog = new ProgressDialogBase(context);
        // 에니메이션 추가 start
        View v = LayoutInflater.from(context).inflate(R.layout.layout_progress_ani, null, false);
        ImageView loaderIcon = v.findViewById(R.id.loader_icon);
        if (App.getThemeType() == Const.eThemeType.DARK) {
            loaderIcon.setBackgroundResource(R.drawable.steps_loader_black);
        } else {
            loaderIcon.setBackgroundResource(R.drawable.steps_loader);
        }
        final AnimationDrawable frameAnimation = (AnimationDrawable) loaderIcon.getBackground();
        frameAnimation.start();
        dialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                frameAnimation.stop();
            }
        });
        // 에니메이션 추가 end

        if (title != null) dialog.setTitle(title);
        dialog.setCancelable(cancelable);

        dialog.show();
        dialog.setContentView(v);

        return dialog;
    }

    public ProgressDialogBase(Context context) {
        super(context, R.style.DialogBase);
    }
}
