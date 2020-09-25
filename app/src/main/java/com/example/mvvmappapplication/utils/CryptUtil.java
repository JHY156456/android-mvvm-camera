package com.example.mvvmappapplication.utils;

import android.text.TextUtils;


import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created by kck on 2016-07-04.
 */
public class CryptUtil {

    public static String getEncodeToDes(String data) {
        return getEncodeToDes(data, PreferenceFileUtil.getEncryptionKey());
    }

    public static String getEncodeToDes(String data, String keyData) {
        if (TextUtils.isEmpty(data)) return null;
        if (TextUtils.isEmpty(keyData)) return null;

        try {
            byte[] ciphertext = WebDESUtil.encrypt(true, data.getBytes(), keyData.getBytes(), false);
            return WebDESUtil.hexToString(ciphertext);
        } catch (Exception ex) {
            return null;
        }
    }

    static public String getDecodeToDes(String data) {
        if (TextUtils.isEmpty(data)) return null;
        String keyData = PreferenceFileUtil.getEncryptionKey();
        if (TextUtils.isEmpty(keyData)) return null;

        byte[] decryptData;
        try {
            decryptData = WebDESUtil.decrypt(true, WebDESUtil.stringToHex(data), keyData.getBytes(), false);
            if (decryptData != null) {
                return new String(decryptData, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            LogUtil.e(e);
        }
        return "";
    }

//    static public String getDecryptPassword(byte[] secureKey, int length, String cryptData) {
//        if ( secureKey == null || cryptData == null )
//        {
//            return "";
//        }
//        TransKeyCipher tkc = new TransKeyCipher("SEED");
//        tkc.setSecureKey(secureKey);
//        byte pbPlainData[] = new byte [length];
//        if (tkc.getDecryptCipherData(cryptData, pbPlainData))
//        {
//            return new String(pbPlainData);
//        }
//        return "";
//    }

//    /**
//     * 기본계좌 비밀번호 가져오기
//     */
//    public static String getAcnt_pwd(String encryptAccountPwd, String accountSecureKey, int accountPwdLength) {
//        if (encryptAccountPwd == null || accountSecureKey ==null || accountPwdLength == 0) {
//            return null;
//        }
//
//        return getDecryptPassword(accountSecureKey.getBytes(), accountPwdLength, encryptAccountPwd);
//    }

    /**
     * 서버전송 암호화 형식 반환 (실명번호/면허번호)
     *
     * @param gaStr
     * @return
     */
    public static String encryptToServerGA(String gaStr) {
        int length = gaStr.length();
        if ((length >= 8)) {
            String start = gaStr.substring(length - 1) + gaStr.substring(length - 2, length - 1) + gaStr.substring(length - 3, length - 2);
            String end = gaStr.substring(2, 3) + gaStr.substring(1, 2) + gaStr.substring(0, 1);
            return start + gaStr + end;
        }
        return "";
    }

    /**
     * 숫자 -> 영문 치환
     *
     * @param ch
     * @return
     */
    public static String encryptToGA(char[] ch) {
        StringBuilder sb = new StringBuilder();
        for (char c : ch) {
            sb.append(encryptToGAChar(c));
        }
        Arrays.fill(ch, (char) 0x20);
        return sb.toString();
    }

    public static String encryptToGA(byte[] b) {
        char[] c = new char[b.length];
        for (int i = 0; i < c.length; i++) {
            c[i] = (char) b[i];
        }
        Arrays.fill(b, (byte) 0x20);
        return encryptToGA(c);
    }

    /**
     * 영문 -> 숫자 치환
     *
     * @param data
     * @return
     */
    public static String decryptToGA(String data) {
        StringBuilder sb = new StringBuilder();
        for (char c : data.toCharArray()) {
            sb.append(decryptToGAChar(c));
        }
        return sb.toString();
    }

    public static byte decryptToGA(char c) {
        if (c == 'A') {
            return '0';
        } else if (c == 'B') {
            return '1';
        } else if (c == 'C') {
            return '2';
        } else if (c == 'D') {
            return '3';
        } else if (c == 'E') {
            return '4';
        } else if (c == 'F') {
            return '5';
        } else if (c == 'G') {
            return '6';
        } else if (c == 'H') {
            return '7';
        } else if (c == 'I') {
            return '8';
        } else if (c == 'J') {
            return '9';
        }
        return ' ';
    }


    public static char encryptToGAChar(final char c){
        switch (c) {
            case '0': return 'A';
            case '1': return 'B';
            case '2': return 'C';
            case '3': return 'D';
            case '4': return 'E';
            case '5': return 'F';
            case '6': return 'G';
            case '7': return 'H';
            case '8': return 'I';
            case '9': return 'J';
        }
        return c;
    }

    public static char decryptToGAChar(final char c){
        switch (c) {
            case 'A': return '0';
            case 'B': return '1';
            case 'C': return '2';
            case 'D': return '3';
            case 'E': return '4';
            case 'F': return '5';
            case 'G': return '6';
            case 'H': return '7';
            case 'I': return '8';
            case 'J': return '9';
        }
        return c;
    }

}
