package com.example.mvvmappapplication.utils.inputfilter;

import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.example.mvvmappapplication.utils.LogUtil;

import java.util.regex.Pattern;



public class InputFilterMinMax implements InputFilter {
    private Pattern mPattern;
    private Pattern mPointNumPattern;

    private double minValue;
    private double maxValue;

    public InputFilterMinMax(double minVal, double maxVal) {
        this.minValue = minVal;
        this.maxValue = maxVal;
        mPattern = Pattern.compile("(^0+)");
        mPointNumPattern = Pattern.compile("^[\\d]{1,3}([.]\\d{0,2})?$");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dStart, int dEnd) {
        try {
            // Remove the string out of destination that is to be replaced
            String newVal = dest.toString().substring(0, dStart) + dest.toString().substring(dEnd, dest.toString().length());
            newVal = newVal.substring(0, dStart) + source.toString() + newVal.substring(dStart, newVal.length());

            double input = Double.parseDouble(newVal);

            if (newVal.length() > 1 && !newVal.contains("0.")) {
                //"0"체크 필터, 00, 000 -> ""
                if(mPattern.matcher(newVal).find()) {
                    return "";
                }
            }

            //소수점 자리수 제한.
            if(!mPointNumPattern.matcher(newVal).find()){
                return "";
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

    private boolean isInRange(double a, double b, double c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
