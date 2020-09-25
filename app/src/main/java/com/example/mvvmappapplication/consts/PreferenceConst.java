package com.example.mvvmappapplication.consts;

/**
 * PreferenceConst
 *
 * @author josung
 * @version 1.0
 * @since 2016-10-11
 */
public class PreferenceConst {

    // 서버
    public static final String HS_SERVER_INFO = "hs_server_info";

    // 설정
    public static final String HS_SETTING_INFO = "hs_setting_info";

    // 로그인
    public static final String HS_LOGIN_INFO = "hs_login_info";

    // 주문
    public static final String HS_ORDER_INFO = "hs_order_info";

    // 자산
    public static final String HS_ASSET_INFO = "hs_asset_info";

    // 금결원 바이오
    public static final String HS_KFTCBIO_INFO = "hs_kftcbio_info";

    // System Notice
    public static final String HS_EVENT_NOTICE = "EVENT_NOTICE";
    // Url Scheme
    public static final String HS_URL_SCHEME_INFO = "urs_scheme_info";

    // 간편송금
    public static final String HS_TRANSFER_INFO = "hs_transfer_info";

    //파일 업로드
    public static final String HS_FILE_UPLOAD = "hs_file_upload";

    // 준회원 계좌개설 임시 사용자 정보 저장 코드
    public static final String HS_CREATE_ACCOUNT_INFO = "TEMP_USER_INFO";

    // For OCR, FacePhi
    public static final String HS_UNTACT_CREATE_ACCOUNT_OCR_FACEPHI_INFO = "TEMP_HS_UNTACT_CREATE_ACCOUNT_OCR_FACEPHI_INFO";

    // 사용자 설정 파일
    public static final String USER_TAG = "_setting";

    /**********************************************************************************/
    /**********************************************************************************/
    /**
     * 서버설정 key value
     */
    public class ServerInfoPreferences {
        // 이전 접속시 운영 서버 타입 (목동/죽전 서버 번갈아가면서 접속을 위함) 0 : 목동, 1 : 죽전
        public static final String REAL_SERVER_IP = "real_server_ip";
        // 이전 접속시 개발 서버 타입 (목동/죽전 서버 번갈아가면서 접속을 위함) 0 : 목동, 1 : 죽전
        public static final String TEST_SERVER_IP = "test_server_ip";

        // MCI 핸들
        public static final String MCIHLD = "mcihld";
        // MCI 공인 IP
        public static final String MCIOFCIP = "mciofcip";

        // MCI 현재 날짜 (yymmdd)
        public static final String MCI_TODAY = "mci_today";
        // MCI 현재 시간 (hhmmss)
        public static final String MCI_TIME = "mci_time";
        // MCI 현재 날짜와 시간 저장 시점의 SystemClock.elapsedRealtime();
        public static final String DEVICE_TDDAY = "device_tdday";

        // 시스템 공지 skip noticeid
        public static final String SYSTEM_NOTICE_SKIP_ID = "system_notice_skip_id";
        // 시스템 공지 skip notice 노출 시간
        public static final String SYSTEM_NOTICE_LAST_SHOW_TIME = "system_notice_last_show_time";

        public static final String LOGIN005_DEVICE_ID  = "login005_device_id";

        public static final String ENCRYPTION_KEY  = "encryption_key";

        // 단말기 지정 DeviceId 마이그레이션 여부
        public static final String MIGRATION_TERMINALID  = "migration_terminalid";
    }

    /**
     * 환경설정 key value
     */
    public class SettingInfoPreferences {
        // 마이그레이션 버전 체크용
        public static final String STEPS_MIGRATION_VERSION = "steps_migration_version";

        // 상품 마스터 업데이트 날짜
        public static final String MASTER_UPDATE_DAY = "master_update_day";

        // 계좌 비밀번호
        public static final String PREF_IACN_PWD_SAVE = "pref_iacn_pwd_save";
        // 계좌 비밀번호 저장 여부
        public static final String PREF_IACN_PWD_YN = "pref_iacn_pwd_yn";
        // 지문인증 로그인 여부
        public static final String PREF_FINGER_PRINT_CHECK = "pref_finger_print_check";
        // 소개페이지 노출 여부
        public static final String PREF_INTRO_PREVIEW_CHECK = "pref_intro_preview_check";
        // 6.0 미만 사용자 권한팝업 확인 체크 여부
        public static final String PREF_INTRO_USER_PERMISSION_CHECK = "user_permission_check";
        // 푸쉬 설정 여부 getNotiFlog
        public static final String PREF_PUSH_NOTI_FLAG = "pref_push_noti_flag";
        // 테마 설정
        public static final String PREF_THEME_TYPE = "pref_theme_type";
    }

    /**
     * 로그인 key value
     */
    public class LoginInfoPreferences {
        // 계좌 연결 시 정회원 전 준회원 사용자 아이디
        public static final String AC_USER_ID = "ac_user_id";

        // sns 로그인 종류
        public static final String SNS_TYPE = "sns_type";
        // sns 로그인 ID
        public static final String SNS_ID = "sns_id";
        // sns 네이버 Email
        public static final String SNS_NAVER_EMAIL = "sns_naver_email";

        // sns 로그인 마지막 date
        public static final String SNS_LAST_LOGIN_DATE = "pref_sns_last_login_date";
    }

    /**
     * 주문 key value
     */
    public class OrderInfoPreferences {
        // 가장 마지막 구매 방식 (즉시/희망가)
        public static final String BUY_ORDER_METHOD = "buy_order_method";
        // 가장 마지막 판매 방식 (즉시/희망가)
        public static final String SELL_ORDER_METHOD = "sell_order_method";
    }

    public class AssetPreferences {
        public static final String ASSET_IACN = "asset_iacn";
    }

    /**
     * 금결원 바이오 정보
     */
    public class KFTCBioInfoPreferences {
        // 금결원 바이오인증 축약서명 공개키
        public static final String PREF_LINK_PUBKEY = "pref_link_pubkey";

        // 금결원 바이오인증 축약서명 개인키
        public static final String PREF_LINK_PRVKEY = "pref_link_prvkey";

        // 사용중인 바이오인증 기술 코드
        public static final String PREF_USED_CODE = "pref_used_code";
    }

    public class EventNotice {
        // 공지사항 Snotice_msg
        public static final String NOTICE_MSG = "key_notice_msg";

        public static final String EVENT_SHOW_NOMORE_DAY = "key_event_notice_check_day";
        public static final String EVENT_SHOW_NOMORE_DAY_1 = "key_event_notice_check_day_1";

        public static final String EVENT_MYMENU_BADGE = "key_event_mymenu_badge";
        public static final String EVENT_MYMENU_CHECK_IN_BADGE = "key_event_mymenu_checkin_badge";
        // Adbrix key 관리
        public static final String EVENT_ADBRIX_KEYS = "key_event_adbrix";
    }

    public class UrlSchemeInfo {
        public static final String SCHEME_CREATE_ACCOUNT_RESULT = "create_account_result";
    }

    public class TransferInfoPreferences {
        // 마지막 이체 이용 계좌번호
        public static final String TRANSFER_LAST_USING_ACCOUNT = "transfer_last_using_account";
    }

    public class FileUploadPreferences {
        public static final String LAST_UPDATE_MOCK_STOCK_DATE = "pref_LAST_UPDATE_MOCK_STOCK_DATE";
    }

    public class CreateAccountPreferences {
        public static final String USER_NAME = "KEY_USER_NAME"; // 이름
        public static final String USER_JUMIN = "KEY_USER_ENC_JUMIN"; // 주민등록번호
        public static final String USER_JUMIN_OLD = "KEY_USER_JUMIN"; // 주민등록번호 (암복호화 전)
    }

    public class OcrFacePhiPreferences {
        public static final String USER_IDCARD_TYPE = "KEY_USER_IDCARD_TYPE";
        public static final String USER_IDCARD_IMAGEID = "KEY_USER_IDCARD_IMAGEID";
        public static final String USER_IDCARD_ISSUESDATA = "KEY_USER_IDCARD_ISSUESDATA";
        public static final String USER_IDCARD_ENCLICENSENUM = "KEY_USER_IDCARD_ENCLICENSENUM";
        public static final String USER_IDCARD_SCPSCRTFATTCCNFMSCD = "KEY_USER_IDCARD_SCPSCRTFATTCCNFMSCD";
        public static final String USER_IDCARD_SRCVSN = "KEY_USER_IDCARD_SRCVSN";
        public static final String USER_IDCARD_ATTCCNFMCNTERSPNCOD = "KEY_USER_IDCARD_ATTCCNFMCNTERSPNCOD";
        public static final String USER_IDCARD_MASKRELMCTNS = "KEY_USER_IDCARD_MASKRELMCTNS";
        public static final String USER_IDCARD_CROPPEDIMAGEBASE64 = "KEY_USER_IDCARD_CROPPEDIMAGEBASE64";
        public static final String USER_FACEPHI_FACIAL_AWARRSLTSCD = "KEY_USER_FACEPHI_FACIAL_AWARRSLTSCD";
        public static final String USER_FACEPHI_FACIAL_FILENAME = "KEY_USER_FACEPHI_FACIAL_FILENAME";
        public static final String USER_FACEPHI_FACIAL_IMAGDSCMNO = "KEY_USER_FACEPHI_FACIAL_IMAGDSCMNO";
    }

    public class UserPreferences {
        // [Watchlist] 활성화 중인 Watchlist GroupNo 저장
        public static final String SELECTED_WATCHLIST_GROUPNO = "pref_select_watchlist_groupno";
        // [Watchlist] News Badge 갱신 시간.
        public static final String SELECTED_WATCHLIST_NEWS_DATE = "pref_select_watchlist_news_date";
        // [현재가] 마지막에 선택한 탭 정보
        public static final String PREF_LAST_SELECT_PRODUCT_TAB = "pref_last_select_product_tab";
        // [검색] 즐겨찾기 카테고리 저장
        public static final String SEARCH_FAVORITES_FILTER = "pref_search_favorites_filter";

        //정렬.
        public static final String WATCHLIST_GROUP_SORT = "pref_watchlist_group_sort_code";
        public static final String WATCHLIST_PRODUCT_SORT = "pref_watchlist_product_sort_code";
        public static final String PORTFOLIO_PRODUCT_CODE = "pref_portfolio_product_code";
    }
}
