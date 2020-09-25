package com.example.mvvmappapplication.ui;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * [공통] BaseNavigator
 *
 * @author ehjung
 */
public interface BaseNavigator {

    Context getContext();

    Activity getActivity();

    View.OnClickListener getOnClickListener();

}
