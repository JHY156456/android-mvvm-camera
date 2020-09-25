package com.example.mvvmappapplication.utils;

import android.os.SystemClock;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kck on 2016-07-04.
 */
public class DateUtil {

    /**
     * 현재 Calendar 반환.
     *
     * @return
     */
    public static Calendar getCurrentCalendar() {
        Calendar calendar = Calendar.getInstance();
        // 리턴은 서버시간 + (현재 부팅시간 - 처음 부팅 시간);
        String today = PreferenceFileUtil.getMCIToday();
        String time = PreferenceFileUtil.getMCITime();
        if (!TextUtils.isEmpty(today) && !TextUtils.isEmpty(time)) {
            Calendar currentTime = DateUtil.getConvertCalendar(today + time, "yyyyMMddHHmmssSSSSSS");
            long serverTime = currentTime.getTimeInMillis();
            long deviceTime = PreferenceFileUtil.getDeviceToday();
            long todayTime = serverTime + (SystemClock.elapsedRealtime() - deviceTime);
            calendar.setTimeInMillis(todayTime);
        }
        return calendar;
    }

    public static String getCurrentDate() {
        return getCurrentDateTime("yyyyMMdd");
    }

    public static String getCurrentTime() {
        return getCurrentDateTime("HHmmss");
    }

    /**
     * 현재 dateTime을 format 형식으로 반환
     *
     * @param format 반환  format
     * @return
     */
    public static String getCurrentDateTime(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(DateUtil.getCurrentCalendar().getTime());
    }

    /**
     * 날짜 format 변경 (fromFormat -> toFormat)
     *
     * <code>
     * getConvertDateFormat("20190611", "yyyyMMdd", "yyyy-MM-dd") -> 2019-06-11
     * getConvertDateFormat("20190611150833", "yyyyMMddHHmmss", "yyyy.MM.dd HH:mm:ss") -> 2019.06.11 15:08:33
     * </code>
     *
     * @param dateTime   dateTime
     * @param fromFormat dateTime format
     * @param toFormat   변경 format
     * @return
     */
    public static String getConvertDateFormat(String dateTime, String fromFormat, String toFormat) {
        if (TextUtils.isEmpty(dateTime)) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(fromFormat, Locale.getDefault());
        Date date = null;
        try {
            date = dateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
            LogUtil.e(e.getMessage());
            return "";
        }
        dateFormat.applyPattern(toFormat);
        return dateFormat.format(date);
    }

    /**
     * 날짜 format 변경 (fromFormat -> toFormat)
     *
     * <code>
     * getConvertDateFormat("20190611", "yyyyMMdd", "yyyy-MM-dd") -> 2019-06-11
     * getConvertDateFormat("20190611150833", "yyyyMMddHHmmss", "yyyy.MM.dd HH:mm:ss") -> 2019.06.11 15:08:33
     * </code>
     *
     * @param dateTime   dateTime
     * @param fromFormat dateTime format
     * @param toFormat   변경 format
     * @return
     */
    public static String getConvertDateFormatWithLocale(String dateTime, String fromFormat, String toFormat) {
        if (TextUtils.isEmpty(dateTime)) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(fromFormat, Locale.KOREA);
        Date date = null;
        try {
            date = dateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
            LogUtil.e(e.getMessage());
            return "";
        }
        dateFormat.applyPattern(toFormat);
        return dateFormat.format(date);
    }

    /**
     * string date를 Calendar로 반환
     *
     * <code>
     * getConvertCalendar("2019-06-11", "yyyy-MM-dd")
     * getConvertCalendar("20190611150833", "yyyyMMddHHmmss")
     * </code>
     *
     * @param dateTime dateTime
     * @param format   dateTime format
     * @return
     */
    public static Calendar getConvertCalendar(String dateTime, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = dateFormat.parse(dateTime);
            calendar.setTime(date);
        } catch (Exception e) {
        }
        return calendar;
    }

    /**
     * 기준날짜에 field amount 더한 후 반환
     *
     * <code>
     * getCalcDate(endDate, Calendar.MONTH, -12, "yyyyMMdd")
     * getCalcDate(endDate, Calendar.DATE, 30, "yyyyMMdd")
     * </code>
     *
     * @param dateTime 기준 날짜
     * @param field    calendar field
     * @param amount   the amount of date or time to be added to the field.
     * @param format   기준 날짜 format
     * @return
     */
    public static String getCalcDate(String dateTime, int field, int amount, String format) {
        Calendar calendar = getConvertCalendar(dateTime, format);
        calendar.add(field, amount);
        return new SimpleDateFormat(format, Locale.getDefault()).format(calendar.getTime());
    }

    /**
     * 오늘 날짜에 field amount 더한 후 반환
     *
     * @param field  calendar field
     * @param amount the amount of date or time to be added to the field.
     * @param format 반환 날짜 format
     * @return
     */
    public static String getCalcDateToday(int field, int amount, String format) {
        Calendar oCalendar = getCurrentCalendar();
        oCalendar.add(field, amount);
        return new SimpleDateFormat(format, Locale.getDefault()).format(oCalendar.getTime());
    }

    /**
     * 날짜간 차이 반환 (기준날짜 - 비교날짜)
     *
     * <code>
     * getDiffDays("20190610", "20190611") -> -1
     * </code>
     *
     * @param baseDate   기준 날짜 (yyyyMMdd)
     * @param targetDate 비교 날짜 (yyyyMMdd)
     * @return
     */
    public static long getDiffDays(String baseDate, String targetDate) {
        Calendar baseCalendar = getConvertCalendar(baseDate, "yyyyMMdd");
        Calendar targetCalendar = getConvertCalendar(targetDate, "yyyyMMdd");
        return (baseCalendar.getTimeInMillis() - targetCalendar.getTimeInMillis()) / (24 * 60 * 60 * 1000);
    }

    public static long getDiffSec(String baseDate, String targetDate) {
        Calendar baseCalendar = getConvertCalendar(baseDate, "yyyyMMddHHmmss");
        Calendar targetCalendar = getConvertCalendar(targetDate, "yyyyMMddHHmmss");
        return (baseCalendar.getTimeInMillis() - targetCalendar.getTimeInMillis()) / 1000;
    }

    /**
     * 오늘의 요일 가져오기
     *
     * @return
     */
    public static String getDayOfWeek() {
        Calendar calendar = getCurrentCalendar();
        int nWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (nWeek) {
            case 1:
                return "SUN";
            case 2:
                return "MON";
            case 3:
                return "TUES";
            case 4:
                return "WED";
            case 5:
                return "THU";
            case 6:
                return "FRI";
            case 7:
                return "SAT";
        }
        return "";
    }

    /**
     * 오늘의 요일 가져오기
     *
     * @return
     */
    public static boolean isWeekend() {
        Calendar calendar = getCurrentCalendar();
        int nWeek = calendar.get(Calendar.DAY_OF_WEEK);
        boolean isWeekend = false;
        switch (nWeek) {
            case 1:     // 일요일
            case 7:     // 토요일
                isWeekend = true;
                break;
        }
        return isWeekend;
    }

    /********************************************************************************************************
     *
     *********************************************************************************************************/


    /**
     * SNS 형태의 시간으로 변환
     *
     * 오늘 1분 이내 : 방금전
     * 오늘 1시간 이내 : mm분 전
     * 오늘 그외 : 오전오후 hh:mm
     *
     * @param dateTime yyyyMMddHHmmss
     */
    public static String getSnsDateFormat(String dateTime) {
        try {
            String todayDate = DateUtil.getCurrentDate();
            String targetDate = dateTime.substring(0, 8);
            if (todayDate.equals(targetDate)) {
                long diffTime = getDiffSec(todayDate, dateTime);
                if (diffTime < TIME_MAXIMUM.SEC) {
                    // 방금 전
                    return "방금 전";
                } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
                    // mm분 전
                    return diffTime + "분 전";
                } else {
                    //오전(오후) hh:mm
                    return getConvertDateFormat(dateTime, "yyyyMMddHHmmss", "a hh:mm");
                }
            } else {
                //YY.MM.DD
                return getConvertDateFormat(dateTime, "yyyyMMddHHmmss", "yyyy년\nMM월dd일");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    private static class TIME_MAXIMUM {
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }

    /**
     * 알림 시간 포맷
     *
     * @param date yyyyMMddHHmmss
     * @return
     */
    public static String getAlarmDateFormat(final Date date) {
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(date);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        if (DateUtil.getCurrentDate().equals(dateFormat.format(date.getTime()))) {
            // 오늘
            dateFormat.applyPattern("a h시 m분");
        } else {
            //YYYY.MM.DD
            dateFormat.applyPattern("yyyy.MM.dd");
        }
        return dateFormat.format(date.getTime());
    }

    /**
     * 주문내역, 거래내역, 환전현황 등.. 내역 날짜 노출 포맷 반환
     *
     * @param date YYYYMMDD
     * @return M월 d일
     */
    public static String getHistoryDateFormat(String date) {
        String today = getCurrentDate();
        if (today.equals(date)) {
            return "오늘";
        }
        return getConvertDateFormat(date, "yyyyMMdd", "M월 d일");
    }


    /**
     * 펀드/채권 매매불가 시간
     * 정규거래시간 -> 예약거래시간 전환 시 : 17:00 ~17:10
     *
     * @return  true 매매불가, false 매매가능
     */
    public static boolean isTransAfterTime() {
        boolean check = false;

        final int TRANS_TIME_START = 1700;
        final int TRANS_TIME_END = 1709;

        try {
            String currentTime = DateUtil.getCurrentTime();
            int mci = Integer.valueOf(currentTime.substring(0, 4));

            if (TRANS_TIME_START <= mci && mci <= TRANS_TIME_END) {
                check = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isWeekend()){
            check = false;
        }

        return check;
    }

    /**
     * 펀드/채권 매매불가 시간
     * 예약거래시간 -> 정규거래시간 전환 시 : 08:40 ~ 09:00
     *
     * @return  true 매매불가, false 매매가능
     */
    public static boolean isTransBeforeTime() {
        boolean check = false;

        final int TRANS_TIME_START = 830;
        final int TRANS_TIME_END = 859;

        try {
            String currentTime = DateUtil.getCurrentTime();
            int mci = Integer.valueOf(currentTime.substring(0, 4));

            if (TRANS_TIME_START <= mci && mci <= TRANS_TIME_END) {
                check = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isWeekend()){
            check = false;
        }

        return check;
    }

    /**
     * 예약구매 진행여부
     * 17시 ~ 익일영업 9시 : 예약구매
     * @return true 예약구매, false 장구매
     */
    public static boolean isReservationTime() {
        boolean check = true;

        final int TRANS_TIME_START = 1700;
        final int TRANS_TIME_END = 900;

        try {
            String currentTime = DateUtil.getCurrentTime();
            int mci = Integer.valueOf(currentTime.substring(0, 4));

            if (TRANS_TIME_END <= mci && mci <= TRANS_TIME_START) {
                check = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isWeekend()){
            check = true;
        }

        return check;
    }
    /**
     * 펀드 예약구매 진행여부
     * 17시10분 ~ 익일영업 8시40분 : 예약구매
     * @return true 장구매, false 예약구매
     */
    public static boolean isFundReservationTime() {
        boolean check = false;

        final int TRANS_TIME_START = 1710;
        final int TRANS_TIME_END = 840;

        try {
            String currentTime = DateUtil.getCurrentTime();
            int mci = Integer.valueOf(currentTime.substring(0, 4));

            if (TRANS_TIME_END <= mci && mci <= TRANS_TIME_START) {
                check = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isWeekend()){
            check = true;
        }

        return check;
    }

    /**
     * 외화 환전 가능 진행여부
     * 17시00분 ~ 익일영업 8시59분 : 환전불가시간
     * @return true 환전불가, false 환전가능시간
     */
    public static boolean isExchangeTime() {
        boolean check = false;

        final int TRANS_TIME_START = 1700;
        final int TRANS_TIME_END = 859;

        try {
            String currentTime = DateUtil.getCurrentTime();
            int mci = Integer.valueOf(currentTime.substring(0, 4));

            if (TRANS_TIME_END <= mci && mci <= TRANS_TIME_START) {
                check = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isWeekend()){
            check = false;
        }

        return check;
    }


}