package com.example.mvvmappapplication.utils.inputfilter;

import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.example.mvvmappapplication.utils.LogUtil;

import java.util.regex.Pattern;



public class InputFilterIntMinMax implements InputFilter {
    private Pattern mPattern;

    private int minValue;
    private int maxValue;

    public InputFilterIntMinMax(int minVal, int maxVal) {
        this.minValue = minVal;
        this.maxValue = maxVal;
        mPattern = Pattern.compile("(^0+)");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dStart, int dEnd) {
        try {
            // Remove the string out of destination that is to be replaced
            String newVal = dest.toString().substring(0, dStart) + dest.toString().substring(dEnd, dest.toString().length());
            newVal = newVal.substring(0, dStart) + source.toString() + newVal.substring(dStart, newVal.length());

            int input = Integer.parseInt(newVal);

            if(newVal.length() > 1) {
                //"0"체크 필터, 00, 000 -> ""
                if(mPattern.matcher(newVal).find()) {
                    return "";
                }
            }

            if(maxValue < input){
                try{
                    ((SpannableStringBuilder)dest).clear();
                    ((SpannableStringBuilder)dest).append(maxValue+"");
                } catch (Exception e) {
                    LogUtil.e(e.getMessage());
                }
                return "";
            }

            if (isInRange(minValue, maxValue, input)) {
                return null;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
