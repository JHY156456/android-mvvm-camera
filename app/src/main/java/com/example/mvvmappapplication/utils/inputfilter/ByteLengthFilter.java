package com.example.mvvmappapplication.utils.inputfilter;

import android.text.InputFilter;
import android.text.Spanned;

import java.io.UnsupportedEncodingException;

public class ByteLengthFilter implements InputFilter {
    private int limit;
    private String mCharset; //인코딩 문자셋

    public ByteLengthFilter(int limit, String charset) {
        this.mCharset = charset;
        this.limit = limit;
    }

    @Override
    public CharSequence filter(CharSequence charSequence, int start, int end, Spanned origin, int dstart, int dend) {
        String expected = new String();
        expected += origin.subSequence(0, dstart);
        expected += charSequence.subSequence(start, end);
        expected += origin.subSequence(dend, origin.length());
        int keep = calculateMaxLength(expected) - (origin.length() - (dend - dstart));
        if (keep < 0) {
            keep = 0;
        }
        int rekeep = plusMaxLength(origin.toString(), charSequence.toString(), start);
        if (keep <= 0 && rekeep <= 0) {
            return ""; // source 입력 불가(원래 문자열 변경 없음)
        } else if (keep >= end - start) {
            return null; // keep original. source 그대로 허용
        } else {
            if (origin.length() == 0 && rekeep <= 0) { //기존의 내용이 없고, 붙여넣기 하는 문자바이트가 71바이트를 넘을경우
                return charSequence.subSequence(start, start + keep);
            } else if (rekeep <= 0) { //엔터가 들어갈 경우 keep이 0이 되어버리는 경우가 있음
                return charSequence.subSequence(start, start + (charSequence.length() - 1));
            } else {
                return charSequence.subSequence(start, start + rekeep); // source중 일부만입력 허용
            }
        }
    }

    protected int plusMaxLength(String expected, String source, int start) {
        int keep = source.length();

        if (keep > 0 ){
            try{
                int maxByte = limit - getByteLength(expected); //입력가능한 byte
                while (getByteLength(source.subSequence(start, start + keep).toString()) > maxByte) {
                    keep--;
                    if ( keep < 0)
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
                return 0;
            }
        }
        return keep;
    }

    protected int calculateMaxLength(String expected) {
        int expectedByte = getByteLength(expected);
        if (expectedByte == 0) {
            return 0;
        }
        return limit - (getByteLength(expected) - expected.length());
    }

    private int getByteLength(String str) {
        try {
            return str.getBytes(this.mCharset).length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return 0;
    }
}