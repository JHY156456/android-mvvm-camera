package com.example.mvvmappapplication.consts;

/**
 * Created by josung on 2016-08-02.
 */
public class MainEvent {

    /* 프로필 변경 */
    public final static int EVENT_CHANGE_PROFILE_NAME_ACTION = 10;
    public final static int EVENT_CHANGE_PROFILE_IMAGE_ACTION = 11;

    /* 실시간 데이터 */
    public final static int EVENT_REAL_DATA_ACTION = 23;

    /* 이벤트 - 갤러리 사진 업로드 */
    public final static int EVENT_EVENT_PHOTO_ACTION = 27;

    /* 테마 변경 */
    public final static int EVENT_CHANGE_THEME_ACTION = 32;

    /* Push Alarm Badge 갱신 */
    public final static int EVENT_CHANGE_ALARM_ACTION = 40;

    /* 현재가 차트 Long Press */
    public final static int EVENT_PRODUCT_CHART_TOUCH_ACTION = 50;

    /* 카트 개수 */
    public final static int EVENT_CART_COUNT_ACTION = 60;

    public int mEvent;
    public Object mParam;
    public Object mParam2;

    public MainEvent() {
    }

    public MainEvent(int event) {
        mEvent = event;
    }

    public MainEvent(int event, Object param) {
        mEvent = event;
        mParam = param;
    }

    public MainEvent(int event, Object param, Object param2) {
        mEvent = event;
        mParam = param;
        mParam2 = param2;
    }
}
