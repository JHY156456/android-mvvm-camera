package com.example.mvvmappapplication.utils.inputfilter;

import android.text.InputFilter;
import android.text.Spanned;

public class EmojiInputFilter implements InputFilter {
    private static final String CLASS_NAME = EmojiInputFilter.class.getSimpleName();

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        for (int index = start; index < end; index++) {
            int type = Character.getType(source.charAt(index));
            if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                return "";
            }
        }
        return null;
    }
}