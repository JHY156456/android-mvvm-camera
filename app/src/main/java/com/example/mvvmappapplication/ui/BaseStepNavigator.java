package com.example.mvvmappapplication.ui;

/**
 * [공통] BaseStepNavigator
 *
 * @author ehjung
 */
public interface BaseStepNavigator extends BaseNavigator {

    void changeFragment(int screen);

    void onNextStep();

    void onPreStep();
}

