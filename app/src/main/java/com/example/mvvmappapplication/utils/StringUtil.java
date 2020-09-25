package com.example.mvvmappapplication.utils;

import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import com.example.mvvmappapplication.data.ProfileApiService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by kck on 2016-06-30.
 */
public class StringUtil {

    //휴대 전화번호 유효성 검사 체크.
    public static boolean isCellPhone(String str) {

        //기호가 있다면 빼고 체크.
        str = str.trim().replace("-", "").replace(".", "").replace(" ", "");

        return (str.length() == 10 || str.length() == 11) != false && str.startsWith("01") != false;
//        return str.matches("(01[016789])[-](\\d{3,4})[-](\\d{4})");
    }

    //이메일 유효성 검사 체크.
    public static boolean isEmail(String email) {
        if (email == null) return false;
        boolean b = Pattern.matches("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}", email.trim());
        return b;
    }

    //이메일 유효성 검사 체크.
    public static boolean isNumber(String number) {
        boolean result = false;
        if (!TextUtils.isEmpty(number)) {
            result = Pattern.matches("^[0-9]+$", number);
        }
        return result;
    }

    /**
     * 일반전화 유효성 검사 체크
     *
     * @param tellNumber
     * @return
     */
    public static boolean isTellNumber(String tellNumber) {
        boolean returnValue = false;
        String regex = "^\\s*(02|031|032|033|041|042|043|051|052|053|054|055|061|062|063|064|070)?(-|\\)|\\s)*(\\d{3,4})(-|\\s)*(\\d{4})\\s*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(tellNumber);
        if (m.matches()) {
            returnValue = true;
        }
        return returnValue;
    }

    /**
     * 한글 포함 여부 반환
     *
     * @param text
     * @return
     */
    public static boolean isIncludeHangul(String text) {
        if (TextUtils.isEmpty(text)) {
            return false;
        }
        return text.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*");
    }

    /**
     * Base64 encoding 여부 반환
     *
     * @param text
     * @return
     */
    public static boolean isValidBase64(String text) {
        if (TextUtils.isEmpty(text)) {
            return false;
        }
        String regex = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return text.matches(regex);
    }

    public static int parseShort(String string) {
        try {
            return Short.parseShort(string);
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean isValidEUCKR(String text) {
        if (TextUtils.isEmpty(text)) {
            return true;
        }

        CharsetEncoder en = Charset.forName("ISO-2022-KR").newEncoder();
        return en.canEncode(text);
    }


    public static int parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {
            return 0;
        }
    }

    public static byte parseByte(String string) {
        try {
            return Byte.parseByte(string);
        } catch (Exception e) {
            return 0;
        }
    }

    public static long parseLong(String string) {
        try {
            return Long.parseLong(string);
        } catch (Exception e) {
            return 0;
        }
    }

    public static float parseFloat(String string) {
        try {
            return Float.parseFloat(string);
        } catch (Exception e) {
            return 0;
        }
    }

    public static double parseDouble(String string) {
        try {
            return Double.parseDouble(string);
        } catch (Exception e) {
            return 0;
        }
    }

    public static BigDecimal parseBigDecimal(String string) {
        try {
            return new BigDecimal(string);
        } catch (Exception e) {
            return new BigDecimal(0);
        }
    }

    public static DecimalFormatSymbols getDecimalFormatSymbols() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
        return symbols;
    }

    public static String makeStringWithComma(String string) {
        if (string == null || string.length() == 0) {
            return "";
        }
        try {
            if (string.indexOf(".") >= 0) {
                double value = Double.parseDouble(string);
                DecimalFormat format = new DecimalFormat("###,##0.00", getDecimalFormatSymbols());
                return format.format(value);
            }

            long value = Long.parseLong(string);
            DecimalFormat format = new DecimalFormat("###,##0", getDecimalFormatSymbols());
            return format.format(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return string;
    }

    public static String makeStringWithCommaForAssets(String string) {
        if (string == null || string.length() == 0) {
            return "";
        }
        try {
            if (string.indexOf(".") >= 0) {
                double value = Double.parseDouble(string);
                DecimalFormat format = new DecimalFormat("###,##0.00", getDecimalFormatSymbols());
                return ((value > 0) ? "+" : "") + format.format(value);
            }

            long value = Long.parseLong(string);
            DecimalFormat format = new DecimalFormat("###,##0", getDecimalFormatSymbols());
            return ((value > 0) ? "+" : "") + format.format(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return string;
    }

    public static String makeStringWithCommaCrrd(String string, String crrd) {
        if (string == null || string.length() == 0) {
            return "";
        }
        try {
            if (string.indexOf(".") >= 0) {
                double value = Double.parseDouble(string);
                DecimalFormat format = new DecimalFormat("###,##0.00", getDecimalFormatSymbols());
                return ((value > 0) ? "+" : "") + format.format(value) + " " + crrd;
            }

            long value = Long.parseLong(string);
            DecimalFormat format = new DecimalFormat("###,##0", getDecimalFormatSymbols());
            return ((value > 0) ? "+" : "") + format.format(value) + " " + crrd;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return string;
    }

    public static String getFormattedPhoneNumber(String number) {
        String convertString;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            convertString = PhoneNumberUtils.formatNumber(number, "KR");
        } else {
            // noinspection deprecation
            convertString = PhoneNumberUtils.formatNumber(number);
        }

        // OS 7.0에서 전화번호 10자리 일때 오류 수정.
        if (!TextUtils.isEmpty(convertString)) {
            if (!convertString.contains("-")) {
                convertString = makePhoneNumberLive(convertString, 0);
            }
        }

        return convertString;
    }

    /**
     * edittext 입력된 전화번호 실시간 (-) 추가 변환.
     *
     * @param value
     * @param before
     * @return
     */
    private static String makePhoneNumberLive(String value, int before) {
        String input = value.replaceAll("-", "");
        if (before == 0) {
            int lastLangth = input.length() == 10 ? 6 : 7;
            if (input.length() >= 4 && input.length() < lastLangth) {
                return input.substring(0, 3) + "-" + input.substring(3);
            } else if (input.length() > lastLangth) {
                return input.substring(0, 3) + "-" + input.substring(3, lastLangth) + "-" + input.substring(lastLangth);
            }
            return value;
        } else {
            if (input.length() >= 10) {
                return getFormattedPhoneNumber(input);
            }
            return value.endsWith("-") ? value.substring(0, value.length() - 1) : value;
        }
    }

    /**
     * 주민등록번호 formatter
     *
     * @param encText 암호화 데이터
     * @return 000000-0******
     */
    public static CharSequence reformatIdCard(String encText) {
        if (encText.length() < 7) {
            return CryptUtil.decryptToGA(encText);
        }
        StringBuilder builder = new StringBuilder();
        builder.append(CryptUtil.decryptToGA(encText.substring(0, 6)));
        builder.append("-");
        builder.append(CryptUtil.decryptToGA(encText.substring(6, 7)));
        builder.append("******");
        return builder;
    }

    /**
     * 운전면허번호 formatter
     *
     * @param encText 암호화 데이터
     * @return 00-00-000000-00
     */
    public static CharSequence reformatLicenseNumber(String encText) {
        StringBuilder builder = new StringBuilder();
        if (encText.length() > 2) {
            builder.append(CryptUtil.decryptToGA(encText.substring(0, 2)));
            builder.append("-");
            if (encText.length() > 4) {
                builder.append(CryptUtil.decryptToGA(encText.substring(2, 4)));
                builder.append("-");
                if (encText.length() > 6) {
                    builder.append(CryptUtil.decryptToGA(encText.substring(4, 7)));
                    builder.append("***-**");
                } else {
                    builder.append(CryptUtil.decryptToGA(encText.substring(4)));
                }
            } else {
                builder.append(CryptUtil.decryptToGA(encText.substring(2)));
            }
        }
        return builder;
    }

    /**
     * 실수 -> 정수 문자열로 변환 ( 소수점 이하 절삭 )
     * <p/>
     *
     * @param str
     * @return <p/>
     * <p/>
     * <pre>
     * - 사용 예
     *
     * </pre>
     * @author nyk0806@i-on.net
     * @date 2011. 6. 29.
     * @version 1.0.0
     * @since 1.0.0
     */
    public static String convFloatStrToIntStr(String str) {
        if (str != null) {
            int index = str.indexOf(".");
            if (index > 0) {
                return str.substring(0, index);
            } else {
                return str;
            }
        }
        return "";
    }

    /**
     * 숫자 문자열에 부호가 맨앞에 붙는 경우를 처리.
     * <p/>
     *
     * @param str
     * @return <p/>
     * <p/>
     * <pre>
     * - 사용 예
     *
     * </pre>
     * @author nyk0806@i-on.net
     * @date 2011. 6. 7.
     * @version 1.0.0
     * @since 1.0.0
     */
    public static int convStringToInt(String str) {
        if (str != null) {
            try {
                str = removeSpace(str);
                if (str.contains("-")) {
                    return Integer.parseInt(str.replace("-", "").trim()) * (-1);
                } else if (str.contains(".")) {
                    return Integer.parseInt(convFloatStrToIntStr(str));
                } else {
                    return Integer.parseInt(str);
                }
            } catch (Exception e) {
                LogUtil.e("[CommonUtil::convStringToInt] Exception !!");
            }
        }
        return 0;

    }

    /**
     * 문자열 공백 제거
     *
     * @param text
     * @return
     */
    public static String removeSpace(String text) {
        return text.replaceAll("\\p{Z}", "");
    }

    /**
     * 문자갯수가 0이면 0으로 변경
     *
     * @param str
     * @return
     */
    public static String getNumberDefaultZero(String str) {
        if (TextUtils.isEmpty(str)) {
            return "0";
        }

        return str;
    }

    /**
     * 문자를 숫자형으로
     * <p/>
     *
     * @param input
     * @return <p/>
     * <p/>
     * <pre>
     * - 사용 예
     *
     * </pre>
     * @author ksd@i-on.net
     * @date 2011. 8. 12.
     * @version 1.0.0
     * @since 1.0.0
     */
    public static double getDouble(String input) {
        if (input == null || input.equals("")) {
            input = "0.0";
        } else {
            input = input.replace(",", "").replace(" ", "");
        }
        return StringUtil.parseDouble(input.replaceAll(",", "").replaceAll(" ", ""));
    }

    /**
     * NumberFormat 적용
     *
     * @param data number type data
     * @return 적용예시 1000 -> 1,000, 1000.25 -> 1,000
     */
    public static String numberFormat(Object data) {
        if (data == null || TextUtils.isEmpty(data.toString())) {
            return "0";
        }
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        try {
            if (data instanceof String) {
                return numberFormat.format(new BigDecimal((String) data));
            }
            return numberFormat.format(data);
        } catch (Exception ignored) {
        }
        return String.valueOf(data);
    }

    /**
     * NumberFormat RoundingMode 적용
     * pointNum까지 소수점 이하 0 노출됨
     *
     * @param data         number type data
     * @param pointNum     소수점 이하 노출 자리수
     * @param roundingMode
     * @return pointNum(2) 적용예시 10000.00 -> 10,000.00, 10.20 -> 10.20, 10.25 -> 10.25
     */
    public static String numberFormatSetMode(Object data, int pointNum, RoundingMode roundingMode) {
        if (data == null || TextUtils.isEmpty(data.toString())) {
            return "0";
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setRoundingMode(roundingMode);
        numberFormat.setMinimumFractionDigits(pointNum);
        numberFormat.setMaximumFractionDigits(pointNum);
        try {
            if (data instanceof String) {
                return numberFormat.format(new BigDecimal((String) data));
            }
            return numberFormat.format(data);
        } catch (Exception ignored) {
        }
        return String.valueOf(data);
    }

    /**
     * 숫자 콤마 노출 (Default)
     *
     * @param data
     * @param pointNum 소수점 이하 버림
     * @return
     */
    public static String numberFormat(Object data, int pointNum) {
        return numberFormatSetMode(data, pointNum, RoundingMode.DOWN);
    }

    /**
     * NumberFormat RoundingMode 적용
     * pointNum까지 소수점 이하 0 제거됨
     *
     * @param data     number type data
     * @param pointNum 소수점 이하 노출 자리수
     * @return pointNum(2) 적용예시 10.00 -> 10, 10.20 -> 10.2, 10.25 -> 10.25
     */
    public static String numberFormatSetMode2(Object data, int pointNum, RoundingMode roundingMode) {
        if (data == null || TextUtils.isEmpty(data.toString())) {
            return "0";
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setRoundingMode(roundingMode);
        numberFormat.setMaximumFractionDigits(pointNum);
        try {
            if (data instanceof String) {
                return numberFormat.format(new BigDecimal((String) data));
            }
            return numberFormat.format(data);
        } catch (Exception ignored) {
        }
        return String.valueOf(data);
    }

    public static String numberFormat2(Object data, int pointNum) {
        return numberFormatSetMode2(data, pointNum, RoundingMode.HALF_EVEN);
    }

    public static String numberFormatSetMode2(Object data) {
        return numberFormatSetMode2(data,6, RoundingMode.DOWN);
    }

    /**
     * 숫자 문자열을 금액으로 만들기 (콤마 붙이기)
     */
    public static String makeStringComma(String str) {
        if (TextUtils.isEmpty(str) || str.equals("0")) {
            return "0";
        } else {
            str = str.replaceAll(",", "");
        }

        try {
            long value = Long.parseLong(str);
            DecimalFormat format = new DecimalFormat("###,###", getDecimalFormatSymbols());
            return format.format(value);
        } catch (IllegalArgumentException e) {
        }

        return "";
    }

    /**
     * 숫자 문자열을 금액으로 만들기 (콤마 붙이기) - Float
     */
    public static String makeStringCommaFloat(String str) {
        if (TextUtils.isEmpty(str) || str.equals("0")) {
            return "0.00";
        } else {
            str = str.replaceAll(",", "");
        }

        try {
            float value = Float.parseFloat(str);
            DecimalFormat format = new DecimalFormat("###,##0.00", getDecimalFormatSymbols());
            return format.format(value);
        } catch (IllegalArgumentException e) {
        }

        return "";
    }

    /**
     * 숫자 문자열을 금액으로 만들기 (콤마 붙이기) - Float
     */
    public static String makeStringCommaFloat01(String str) {
        if (TextUtils.isEmpty(str) || str.equals("0")) {
            return "0.0";
        } else {
            str = str.replaceAll(",", "");
        }

        try {
            float value = Float.parseFloat(str);
            DecimalFormat format = new DecimalFormat("###,##0.0", getDecimalFormatSymbols());
            return format.format(value);
        } catch (IllegalArgumentException e) {
        }

        return "";
    }

    /**
     * 숫자 문자열을 금액으로 만들기 (콤마 붙이기) - double 내림
     */
    public static String makeStringComma(String str, String pointNum) {
        String pattern = "###,##0";
//
        int point = TextUtils.isEmpty(pointNum) ? 0 : Integer.valueOf(pointNum);
        for (int i = 0; i < point; i++) {
            if (i == 0)
                pattern += ".";
            pattern += "0";
        }
//
        DecimalFormat format = new DecimalFormat(pattern, getDecimalFormatSymbols());
        format.setRoundingMode(RoundingMode.DOWN);
        if (TextUtils.isEmpty(str) || str.equals("0")) {
            return format.format(0);
        } else {
            str = str.replaceAll(",", "");
        }

        if (TextUtils.isEmpty(str) || str.equals("0")) {
            str = "0";
        } else {
            str = str.replaceAll(",", "");
        }

        BigDecimal value = new BigDecimal(str);

        try {
            return format.format(value);
        } catch (IllegalArgumentException e) {
        }

        return "";
    }

    /**
     * 숫자 문자열을 금액으로 만들기 - double 내림
     */
    public static String makeString(String str, int pointNum) {
        String pattern = "#0";

        for (int i = 0; i < pointNum; i++) {
            if (i == 0)
                pattern += ".";
            pattern += "0";
        }

        DecimalFormat format = new DecimalFormat(pattern, getDecimalFormatSymbols());
        format.setRoundingMode(RoundingMode.DOWN);
        if (TextUtils.isEmpty(str) || str.equals("0")) {
            return format.format(0);
        } else {
            str = str.replaceAll(",", "");
        }

        try {
            double value = Double.parseDouble(str);

            return format.format(value);
        } catch (IllegalArgumentException e) {
        }

        return "";
    }

    /**
     * 숫자 문자열을 금액으로 만들기 - double 올림
     */
    public static String makeStringRoundUp(String str, int pointNum) {
        String pattern = "#0";

        for (int i = 0; i < pointNum; i++) {
            if (i == 0)
                pattern += ".";
            pattern += "0";
        }

        DecimalFormat format = new DecimalFormat(pattern, getDecimalFormatSymbols());
        format.setRoundingMode(RoundingMode.UP);
        if (TextUtils.isEmpty(str) || str.equals("0")) {
            return format.format(0);
        } else {
            str = str.replaceAll(",", "");
        }

        try {
            double value = Double.parseDouble(str);

            return format.format(value);
        } catch (IllegalArgumentException e) {
        }

        return "";
    }

    /**
     * @param value
     * @return
     */
    public static String getOnlyNumberString(String value) {
        try {
            Double data = Double.valueOf(value);

            DecimalFormat df = new DecimalFormat("#.##");

            return df.format(data);
        } catch (Exception e) {
            return value;
        }
    }

    /**
     * edittext 입력된 한화증건 계좌 실시간 (-) 추가 변환.
     *
     * @param number
     * @param before 500 07
     * @return
     */
    public static String hiponNumberLive(String number, int before) {
        if (before == 0) {
            String input;
            int first;
            try {
                input = number.replaceAll("-", "");
                first = Integer.parseInt(input.substring(0, 1));
            } catch (Exception e) {
                return number;
            }
            int mid;    // 9 = 신계좌, 5 = 구계좌
            if (first == 5) {
                mid = 9;
            } else {
                mid = 5;
            }
            if (input.length() >= 4 && input.length() < mid) {
                return input.substring(0, 3) + "-" + input.substring(3);
            } else if (input.length() > mid) {
                return input.substring(0, 3) + "-" + input.substring(3, mid) + "-" + input.substring(mid);
            }
            return number;
        } else {
            return number.endsWith("-") ? number.substring(0, number.length() - 1) : number;
        }
    }

    /**
     * 소수점 처리 시 문제 발생함.
     * @param money
     * @return
     */
    @Deprecated
    public static long getNumberFromMoney(String money) {
        long number = 0L;
        if (!TextUtils.isEmpty(money)) {
            String numberString = money.replaceAll(",", "");
            try {
                number = Long.parseLong(numberString);

            } catch (NumberFormatException e) {
            }
        } else {
        }

        return number;
    }

    public static String getPercentString(String percent) {
        String percentString = percent;

        try {
            float data = Float.parseFloat(percent);
            percentString = getPercentString(data);
        } catch (Exception e) {
        }
        return percentString;
    }

    public static String getPercentString(float percent) {
        return String.format("%.2f", percent) + "%";
    }

    public static String getPercentString(double percent) {
        return String.format("%.2f", percent) + "%";
    }

    /**
     * 자리수가 넘치면 나머지 값은 버린다.
     *
     * @param value
     * @param pointNum 소수점 자리수
     * @return
     */
    public static String getPercentStringForWatchlist(String value, int pointNum) {
        String result = null;
        try {
            double dValue = Double.parseDouble(value);
            result = getPercentStringForWatchlist(dValue, pointNum);
        } catch (Exception e) {
            result = value;
            LogUtil.e(e.getMessage());
        }
        return result;
    }

    public static String getPercentStringForWatchlist(double value, int pointNum) {
        //양수 경우 "+"를 붙여줘야 한다.
        return ((value > 0) ? "+" : "") + numberFormatSetMode(value, pointNum, RoundingMode.DOWN) + "%";
    }

    public static String getDateToNumberString(String date) {
        return date.replaceAll("[.]", "");
    }

    public static float isNaNCheck(float value) {
        if (Float.isNaN(value)) {
            return 0.0f;
        }
        return value;
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }

    public static Spanned fromHtml(int resId) {
        return fromHtml(ResourceUtil.getString(resId));
    }



    public static String getLikeCount(String count) {
        String likeCnt = "";
        if (!TextUtils.isEmpty(count)) {
            switch (count.length()) {
                case 5: //99999 -> 99.9k
                    likeCnt = new DecimalFormat("##.#", getDecimalFormatSymbols()).format(StringUtil.parseDouble(String.format("%.1f", StringUtil.parseFloat(count.substring(0, 2) + "." + count.substring(2, 5))))) + "K";
                    break;
                case 6: //999999 -> 999.9k
                    likeCnt = new DecimalFormat("###.#", getDecimalFormatSymbols()).format(StringUtil.parseDouble(String.format("%.1f", StringUtil.parseFloat(count.substring(0, 3) + "." + count.substring(3, 6))))) + "K";
                    break;
            }

            if (TextUtils.isEmpty(likeCnt)) {
                if (count.length() < 5) {
                    likeCnt = StringUtil.makeStringComma(count);
                } else {
                    likeCnt = "999.9K";
                }
            }
        } else {
            likeCnt = "0";
        }

        return likeCnt;
    }

    /**
     * 1000 -> 1.0K 유틸
     *
     * @param value
     * @return
     */
    public static String formatK(long value) {
        if (value <= 0) return "" + value;
        if (value < 1000) return Long.toString(value);

        Long divideBy = 1_000L;
        String suffix = "K";

        long truncated = value / (divideBy / 10);
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static String formatK(String value) {
        return formatK(parseInt(value));
    }



    /**
     * 환전 원화,JPY 빼고 금액입력시, 주문, 예약주문 단가입력시 사용 금액콤마와 소수점 자리수 붙여주기
     * 1345 -> 1,345.00
     *
     * @param money
     * @param decimalCount
     * @return
     */
    public static String parseCash3(String money, int decimalCount) {

        String data = "";

        if (!TextUtils.isEmpty(money)) {
            if (money.indexOf('.') != -1) {
                int a = money.indexOf('.');

                String first = money.substring(0, a);  //소수점 앞까지 잘라서 넣는다.
                String last = money.substring(a, money.length()); //소수점 부터 나머지

                String temp_first = StringUtil.makeStringWithComma(first);

                if (decimalCount > 0) {
                    if (!last.isEmpty()) {
                        if (last.length() < decimalCount + 1) {
                            for (int i = last.length(); i < decimalCount + 1; i++) {
                                last = last + "0";
                            }
                        }
                        last = last.substring(0, decimalCount + 1);
                    }
                    data = temp_first + last;
                } else {
                    data = temp_first;
                }

            } else {
                if (decimalCount > 0) {
                    data = StringUtil.makeStringWithComma(money) + ".";
                    for (int i = 0; i < decimalCount; i++) {
                        data = data + "0";
                    }

                } else {
                    data = StringUtil.makeStringWithComma(money);
                }
            }
        }

        return data;
    }

    /**
     * 필터 데이터 미선택 체크
     *
     * @param selectStr
     * @return
     */
    public static boolean isSelected(String selectStr) {
        if (selectStr.equals("선택안함")) {
            return true;
        } else {
            return false;
        }
    }

//    /**
//     * 프로필 이미지 url 반환
//     *
//     * @param imageKey 이미지 key
//     * @return
//     */
//    public static String getProfileImageUrl(String imageKey, boolean isThumbnail) {
//        if (!TextUtils.isEmpty(imageKey)) {
//            return String.format("%s%s?SECURE_KEY=%s&user_id=%s&profile_gbn=%s",
//                    ProfileApiService.BASE_URL,
//                    ProfileApiService.IMAGE_DOWNLOAD,
//                    getProfileImageSecureKey(imageKey),
//                    imageKey,
//                    isThumbnail ? "S" : "B");
//        }
//        return "";
//    }
//
//    /**
//     * 프로필 이미지 SecureKey 생성
//     *
//     * @param imageKey
//     * @return
//     */
//    public static String getProfileImageSecureKey(String imageKey) {
//        String keyData = DateUtil.getCurrentDate().replaceAll("2", "!");
//        return CryptUtil.getEncodeToDes(imageKey, keyData);
//    }

    /**
     * 을,를
     * 이,가
     * 은,는
     *
     * @param name
     * @param firstValue
     * @param secondValue
     * @param defaultValue 한글 이외의 값으로 끝났을 경우.
     * @return
     */
    public static final String getComleteWordByJongsung(String name, String firstValue, String secondValue, String defaultValue) {
        char lastName = name.charAt(name.length() - 1);

        if (lastName < 0xAC00 || lastName > 0xD7A3) {
            return name + defaultValue;
        }

        String selectedValue = (lastName - 0xAC00) % 28 > 0 ? firstValue : secondValue;
        return name + selectedValue;
    }

    /**
     * 숫자를 ABC롤 반환
     *
     * @param i 숫자
     * @return A or B or C ...
     */
    public static String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char) (i + 64)) : null;
    }

    public final static String SPACE_STRING = Character.toString((char) 0x20);

    /**
     * 프로필 이미지 url 반환
     *
     * @param imageKey 이미지 key
     * @return
     */
    public static String getProfileImageUrl(String imageKey, boolean isThumbnail) {
        if (!TextUtils.isEmpty(imageKey)) {
            return String.format("%s%s?SECURE_KEY=%s&user_id=%s&profile_gbn=%s",
                    ProfileApiService.BASE_URL,
                    ProfileApiService.IMAGE_DOWNLOAD,
                    getProfileImageSecureKey(imageKey),
                    imageKey,
                    isThumbnail ? "S" : "B");
        }
        return "";
    }
    /**
     * 프로필 이미지 SecureKey 생성
     *
     * @param imageKey
     * @return
     */
    public static String getProfileImageSecureKey(String imageKey) {
        String keyData = DateUtil.getCurrentDate().replaceAll("2", "!");
        return CryptUtil.getEncodeToDes(imageKey, keyData);
    }
}