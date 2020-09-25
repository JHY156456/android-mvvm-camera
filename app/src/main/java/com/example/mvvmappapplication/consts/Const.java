package com.example.mvvmappapplication.consts;

import android.os.Environment;
import android.text.TextUtils;

import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.StringDef;
import androidx.annotation.StringRes;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.utils.ResourceUtil;

/**
 * Created by kck on 2016-06-29.
 */
public class Const {

    public enum eBuildMode {
        // 순서 변경하지 말 것!
        DEPLOY, DEPLOY_DEV, DEV
    }

    public interface SERVER_TYPE {
        int real = 1;
        int dev = 2;
        int beta = 3;
    }

    public enum eThemeType {
        WHITE, DARK
    }

    /**
     * 주문 타입
     */
    public enum eOrderType {
        NONE("0", "", "", "", "",  0, 0),
        SELL("1", "2", "판매", "매도", "SELL", R.attr.colorSecondaryBlue, R.attr.sellButtonColor),
        BUY("2", "1", "구매", "매수", "BUY", R.attr.colorSecondaryRed, R.attr.colorSecondaryRed);

        public String code;
        public String inqScd;
        public String label;
        public String label2;
        public String label3;
        @AttrRes
        public int textColor;
        @AttrRes
        public int buttonTextColor;

        eOrderType(String code, String inqScd, String label, String label2, String label3, int textColor, int buttonTextColor) {
            this.code = code;
            this.inqScd = inqScd;
            this.label = label;
            this.label2 = label2;
            this.label3 = label3;
            this.textColor = textColor;
            this.buttonTextColor = buttonTextColor;
        }

        public static eOrderType getType(String code) {
            eOrderType resultType = NONE;
            for (eOrderType type : values()) {
                if (type.code.equalsIgnoreCase(code)) {
                    resultType = type;
                    break;
                }
            }
            return resultType;
        }
    }



    /**
     * 주문 형식
     */
    public final static int GROUP_ID_IMMEDIATELY = 1;
    public final static int GROUP_ID_WISH = 2;
    public enum eOrderMethod {
        NONE(-1, "","", -1),
        WISH_PRICE(0, "01","희망가",GROUP_ID_WISH),//희망가    (value : 지정가)
        IMMEDIATELY(1, "05","즉시", GROUP_ID_IMMEDIATELY),//즉시   (value : 시장가)
        MARKET_OPEN_BEFORE(2, "61","개시전종가", GROUP_ID_IMMEDIATELY),//즉시  (value : 현재가) : 정규외.
        MARKET_OPEN_AFTER(3, "71","시간외종가", GROUP_ID_IMMEDIATELY),//즉시   (value : 현재가) : 정규외.
        OVER_TIME(4, "81","시간외단일가", GROUP_ID_WISH),//희망가   (value : 지정가), 정규외.
        RESERVATION(5, "","해외주식 예약", GROUP_ID_WISH);//예약

        public int code;
        public String apiCode;//주문매매구분코드
        public String name;
        public int group;

        eOrderMethod(int code, String apiCode, String name, int group) {
            this.code = code;
            this.apiCode = apiCode;
            this.name = name;
            this.group = group;
        }

        public static eOrderMethod getType(int code) {
            eOrderMethod resultType = NONE;
            for (eOrderMethod type : values()) {
                if (type.code == code) {
                    resultType = type;
                    break;
                }
            }
            return resultType;
        }

        public static eOrderMethod getType(String apiCode) {
            eOrderMethod resultType = NONE;
            for (eOrderMethod type : values()) {
                if (type.apiCode.equals(apiCode)) {
                    resultType = type;
                    break;
                }
            }
            return resultType;
        }
    }
    /**
     * SNS 공유하기 타입
     */
    public enum eSnsShareType {
        FACEBOOK("페이스북", "F"),
        KAKAO("카카오", "K"),
        KAKAO_STORY("카카오스토리", "S"),
        TWITTER("트위터", "T");

        public String label;
        public String code;

        eSnsShareType(String label, String code) {
            this.label = label;
            this.code = code;
        }

        public static eSnsShareType getSnsShareTypeFromCode(String code) {
            eSnsShareType snsType = null;
            if (TextUtils.isEmpty(code)) {
                return snsType;
            }
            for (eSnsShareType type : values()) {
                if (type.code.equalsIgnoreCase(code)) {
                    snsType = type;
                    break;
                }
            }
            return snsType;
        }
    }

    /**
     * 현재가 뉴스 타입
     */
    public enum eNewsType {
        ALL("전체", "0"),
        DISCLOSURE("공시", "1"),
        NORMAL("뉴스", "2");

        public String label;
        public String code;

        eNewsType(String label, String code) {
            this.label = label;
            this.code = code;
        }

        public static eNewsType getType(String label) {
            eNewsType resultType = ALL;
            for (eNewsType type : values()) {
                if (type.label.equalsIgnoreCase(label)) {
                    resultType = type;
                    break;
                }
            }
            return resultType;
        }
    }

    /**
     * 섹터 상세 구성종목 정렬 타입
     */
    public enum eSectorProductSortType {
        S3("시가총액 상위는?", "3"),
        S4("전일 주가 상승률 상위는?", "4"),
        S5("전일 거래량 상위는?", "5"),
        S6("최근 5일 주가 상승률 상위는?", "6"),
        S7("최근 5일 평균 거래량 상위는?", "7"),
        S8("최근 10일 주가 상승률 상위는?", "8"),
        S9("최근 10일 평균 거래량 상위는?", "9");

        public String label;
        public String code;

        eSectorProductSortType(String label, String code) {
            this.label = label;
            this.code = code;
        }

    }


    // 수수료, 세금 필요 없다고 할 경우(준회원 모드일 경우만 해당)
//        public static float SIMULATE_ORDER_TAX = 0f;      //판매 세금
//        public static float SIMULATE_ORDER_FEE = 0f;      //수수료
    // 수수료, 세금 필요하다고 할 경우(준회원 모드일 경우만 해당)
    public static float SIMULATE_ORDER_TAX = 0.0025f;           //판매 세금
    public static float SIMULATE_ORDER_FEE = 0.000046499f;      //수수료
    public static long MAX_CASH_SIMULATE;   //모의투자 가상금액

    public interface SECTOR_SORT {
        String[] codes = {"12","150", "11"};
        String[] values = {"등락률순","가나다순", "전일대비하락률"};
    }

    public interface NEWS_MEDIA {
        String[] codes = {"00", "01", "02", "03", "04", "05", "10", "11", "12", "07", "08", "06", "13", "14", "15", "17", "E9018", "E9013"};
        String[] values = {"전체", "거래소공시", "코스닥공시", "이데일리", "머니투데이", "연합뉴스", "한경뉴스", "매경뉴스", "아시아경제", "해외증시", "프리보드공시", "인포스탁", "서울경제", "조선비즈", "뉴스핌", "마이빅(IR)", "코넥스공시", "K-OTC공시"};

        String[] codes2 = {"00", "E9011", "E9012", "EDNS", "MTNS", "YHNS", "HKNS", "MKNS", "AKNS", "DRWI", "E9018", "ISNS", "SKNS", "CKNS", "NPNS", "IRIF", "E9018", "E9013"};
    }

    public interface AUTH_TERMS {
        String selectionAgreement = "028";  //선택동의 약관
        String createAccount = "034";       //계좌 개설.
        String accountConnect = "035";      //계좌연결(정회원전환.)
        String externalAccount = "036";     //연결계좌등록.
        String plAlarm = "041";
        String createAccountselectionAgreement = "028";  //선택동의 약관.
        String snsPushAgreement = "050";       // 준회원 푸쉬약관
        String overseaTradeApply = "10";       // 해외주식 거래 신청 약관
        String overseaRealtimeApply = "12";   // 해외주식 실시간 신청 약관
    }

    @StringDef({MobileEventUrlScheme.Scheme,
            MobileEventUrlScheme.Events,
            MobileEventUrlScheme.Events2,
            MobileEventUrlScheme.RepeatEvents,
            MobileEventUrlScheme.Survey,
            MobileEventUrlScheme.Alert,
            MobileEventUrlScheme.AlertYn,
            MobileEventUrlScheme.Share,
            MobileEventUrlScheme.ShareSNS,
            MobileEventUrlScheme.SendShareImg,
            MobileEventUrlScheme.Move_Screen,
            MobileEventUrlScheme.Open_Full,
            MobileEventUrlScheme.Close_Full,
            MobileEventUrlScheme.Close_Event,
            MobileEventUrlScheme.Mobile_getValue,
            MobileEventUrlScheme.Mobile_setValue,
            MobileEventUrlScheme.Get_events,
            MobileEventUrlScheme.Get_events2,
            MobileEventUrlScheme.Get_events3,
            MobileEventUrlScheme.Send_Adbrix,
            MobileEventUrlScheme.MobileOutsideBrowser})
    public @interface MobileEventUrlScheme {
        String Scheme = "hwsimple";
        String Events = "events";
        String Events2 = "events2";
        String RepeatEvents = "repeat_events";
        String Survey = "survey";
        String Alert = "event_alert";
        String AlertYn = "event_alertyn";
        String Share = "mobile_share";
        String ShareSNS = "mobile_sns_share";
        String SendShareImg = "mobile_send_image";
        String Move_Screen = "mobile_move_screen";//deep link.
        String Open_Full = "open_full";
        String Close_Full = "close_full";
        String Close_Event = "closed_event";
        String Mobile_getValue = "mobile_getValue";
        String Mobile_setValue = "mobile_setValue";
        String Get_events = "get_events";
        String Get_events2 = "get_events2";
        String Get_events3 = "get_events3";//5월 팡팡 - 추첨권조회용.
        String Send_Adbrix = "send_adbrix";
        String MobileOutsideBrowser = "mobile_outside_browser";
    }

    @StringDef({StepsTellInfo.Customers, StepsTellInfo.ARS})
    public @interface StepsTellInfo {
        String Customers = "080-851-8282";
        String ARS = "1544-8282";
    }

    @IntDef({VersionCheck.Application_Exit, VersionCheck.MoveMarket_Exit, VersionCheck.MoveMarket_Confirm})
    public @interface VersionCheck {
        int Application_Exit = 0; // 앱 강제 종료
        int MoveMarket_Exit = 1; // 앱 강제 업데이트
        int MoveMarket_Confirm = 2; // 마켓 이동/앱 사용 여부 컨펌
    }

    /**
     *  현재가 주가,거래량의 따른 관심도
     *
     *   거래량/주가      상승     평균     하락
     *     감소         LH      LA      LL
     *     평균         AH      AA      AL
     *     증가         HH      HA      HL
     */

    public enum eInvestType {
        LH("LH", "아직 크지 않네요", "거래 주체등의 정보의 격차로 발생하는 상승일 수도 있답니다. 관심을 가져볼까요?"),
        LA("LA", "크지 않네요", "하지만 아직은 조금 더 관심을 가져볼까요?"),
        LL("LL", "많이 떨어졌네요", "좀 더 지켜보는게 어떨까요?"),
        AH("AH", "조금 높네요", "시장이 상승하고 있거나, 거래 주체등의 정보의 격차로 발생하는 상승일 수도 있답니다. 관심을 가져볼까요?"),
        AA("AA", "크지 않네요", "좀 더 지켜보시는게 어떨까요?"),
        AL("AL", "크지 않네요", "좀 더 지켜보시는게 어떨까요?"),
        HH("HH", "높네요", "뉴스, 차트, 기관, 외국인 수급 등 투자를 위한 정보를 살펴보면 좋겠어요. 관심을 가져 볼까요?"),
        HA("HA", "높네요", "거래 주체등의 매수/매도가 균형을 이루고 있어요. 차트를 보고 그 틀안의 주가 흐름도 살펴보면 좋겠어요."),
        HL("HL", "높네요", "하지만 주가가 하락하고 있다면 조금 더 지켜 본 후 관심을 갖는 건 어떨까요?");

        public String code;
        public String subTitle;
        public String popupMessage;

        eInvestType(String code, String title, String popupMessage){
            this.code = code;
            this.subTitle = title;
            this.popupMessage = popupMessage;
        }

        public static String getTitle(eInvestType investType) {
            if (investType == null)
                return "";

            return getFirstTitle(investType) + investType.subTitle;
        }

        public static String getFirstTitle(eInvestType investType) {
            if (investType == null)
                return "";
            if (investType.code.substring(0, 1).equals("H")){
                return  "시장의 관심도가 ";
            }else {
                return "시장의 관심이 ";
            }
        }

        public static String getDescription(eInvestType investType){
            if (investType == null)
                return "";

            String amountAvg = "";
            String stockAvg = "";
            if (investType.code.substring(0,1).equals("L")){
                amountAvg = "감소 중";
            }else if (investType.code.substring(0,1).equals("A")) {
                amountAvg = "평균수준";
            }else if (investType.code.substring(0,1).equals("H")) {
                amountAvg = "증가 중";
            }

            if (investType.code.substring(1,2).equals("L")){
                stockAvg = "감소 중";
            }else if (investType.code.substring(1,2).equals("A")) {
                stockAvg = "평균수준";
            }else if (investType.code.substring(1,2).equals("H")) {
                stockAvg = "상승 중";
            }

            return String.format(ResourceUtil.getString(R.string.invest_amount_stock_description), amountAvg, stockAvg);
        }

        public static eInvestType getInvestTypeFromCode(String code) {
            eInvestType investType = null;
            if (TextUtils.isEmpty(code)) {
                return investType;
            }
            for (eInvestType type : values()) {
                if (type.code.equalsIgnoreCase(code)) {
                    investType = type;
                    break;
                }
            }
            return investType;
        }
    }

    /**
     * 현재가 매력포인트 표기
     */
    public enum eInvestCharmType{

        UP("투자하기에", "매력적 입니다", R.attr.img_weather_up),
        NORMAL("투자 전 점검이", "필요해요", R.attr.img_weather_normal),
        DOWN("투자 결정에 신중해야","할 시기입니다", R.attr.img_weather_down);

        public String title01;
        public String title02;
        public int resource;

        eInvestCharmType(String title01, String title02, int resource){
            this.title01 = title01;
            this.title02 = title02;
            this.resource = resource;
        }

        public static eInvestCharmType getInvestCharmType(double charmValue, double lastYearCharmValue){
            eInvestCharmType type = null;

            if (charmValue >= 55){
                if (lastYearCharmValue > charmValue){
                    type = NORMAL;
                }else {
                    type = UP;
                }
            }else if (charmValue > 45){
                if (lastYearCharmValue < charmValue){
                    type = UP;
                }else if (lastYearCharmValue > charmValue){
                    type = DOWN;
                }else {
                    type = NORMAL;
                }
            }else {
                if (lastYearCharmValue < charmValue){
                    type = NORMAL;
                }else {
                    type = DOWN;
                }
            }
            return type;
        }
    }

    /**
     * 현재가 차트 타입
     */
    public enum eChartType {
        CANDLE,
        LINE
    }

    public enum eChartPeriod {
        NONE("", "0", ""),
        ONE_TICK("1틱", "1", "0"),
        THREE_TICK("3틱", "3", "0"),
        FIVE_TICK("5틱", "5", "0"),
        TEN_TICK("10틱", "10", "0"),
        FIFTEEN_TICK("15틱", "15", "0"),
        THIRTY_TICK("30틱", "30", "0"),
        SIXTY_TICK("60틱", "60", "0"),
        ONE_TWENTY_TICK("120틱", "120", "0"),

        ONE_MIN("1분", "1", "1"),
        THREE_MIN("3분", "3", "1"),
        FIVE_MIN("5분", "5", "1"),
        TEN_MIN("10분", "10", "1"),
        FIFTEEN_MIN("15분", "15", "1"),
        THIRTY_MIN("30분", "30", "1"),
        SIXTY_MIN("60분", "60", "1"),
        ONE_TWENTY_MIN("120분", "120", "1"),

        ONE_DAY("1일", "1", "2"),

        ONE_WEEK("1주", "1", "3"),

        ONE_MONTH("1달", "1", "4"),
        THREE_MONTH("3달", "3", "4"),
        SIX_MONTH("6달", "6", "4"),

        ONE_YEAR("1년", "1", ""),
        THREE_YEAR("3년", "3", ""),
        FIVE_YEAR("5년", "5", ""),
        TEN_YEAR("10년", "10", "");

        public String label;
        public String unitCode;
        public String chartPeriodCode;

        eChartPeriod(String label, String unitCode, String chartPeriodCode) {
            this.label = label;
            this.unitCode = unitCode;
            this.chartPeriodCode = chartPeriodCode;
        }

        public static boolean isMinType(eChartPeriod chartPeriod) {
            return "1".equals(chartPeriod.chartPeriodCode);
        }

        public static boolean isTickType(eChartPeriod chartPeriod) {
            return "0".equals(chartPeriod.chartPeriodCode);
        }
    }

    public enum eChartJipyo {
        NONE("선택없음", ""),
        DEAL_AMOUNT("거래량", ""),
        TWENTY_AVERAGE("20일 이동평균", ""),
        FOREIGNER_SUPPLY("외국인수급", ""),
        AGENCY_SUPPLY("기관수급", ""),
        MARKET_INDEX("시장지수", ""),
        KOSPI("KOSPI", eMarketJisuType.KOSPI.code),
        KOSDAQ("KOSDAQ", eMarketJisuType.KOSDAQ.code),
        SNP500("S&P 500", eMarketJisuType.SNP500.code),
        NIKKEI225("니케이225", eMarketJisuType.NIKKEI225.code),
        SHANGHAI("상해종합", eMarketJisuType.SHANGHAI.code),
        FOREIGN_BUYING("외국인수급", eMarketJisuType.FOREIGN.code),
        JPY_USD("JPY/USD", eMarketJisuType.JPY.code),
        KRW_USD("KRW/USD", eMarketJisuType.USD.code);

        public String label;
        public String code;

        eChartJipyo(String label, String code) {
            this.label = label;
            this.code = code;
        }
    }

    public enum eMarketJisuType {
        KOSPI("코스피", "001", "코스피"),
        KOSDAQ("코스닥", "201", "코스닥"),
        SNP500("s&p 500", "SPX", "S&P500"),
        DAWOO("다우산업", ".DJI", "다우지수"),
        NIKKEI225("니케이225", "JP#NI225", "니케이지수"),
        SHANGHAI("상해종합", "SHANG", "상해종합"),
        USD("미국 USD", "FX@KRW", "원화환율"),
        JPY("일본 JPY", "FX@JPY", "엔화환율"),
        EUR("유럽연합 EUR", "FX@EUR", "유로환율"),
        NB3("국고채(3년)", "NB3", "국고채 3년"),
        FOREIGN("외국인", "DHLRNR", "");

        public String label;
        public String code;
        public String newsSearchKeyword;

        eMarketJisuType(String label, String code, String newsSearchKeyword) {
            this.label = label;
            this.code = code;
            this.newsSearchKeyword = newsSearchKeyword;
        }
    }

    public enum eMarketFutureType {
        KOSPI200("코스피200", "F01", "K2I", "101"),
        MINIKOSPI("미니코스피", "F09", "MKI", "105"),
        KOSDAQ("코스닥", "F02", "KQI", "106"),
        KRX("코넥스", "F0I", "XI3", "108");

        public String label;
        public String code;
        public String futureCode;
        public String startWidth;

        eMarketFutureType(String label, String code, String futureCode, String startWidth) {
            this.label = label;
            this.code = code;
            this.futureCode = futureCode;
            this.startWidth = startWidth;
        }

        public static eMarketFutureType getFutureTypeFromCode(String code) {
            eMarketFutureType futureType = null;
            if (TextUtils.isEmpty(code)) {
                return futureType;
            }
            for (eMarketFutureType type : values()) {
                if (type.code.equalsIgnoreCase(code)) {
                    futureType = type;
                    break;
                }
            }
            return futureType;
        }
    }

    public enum eMarketType {
        KOSPI("J", "0001", "001"),
        KOSDAQ("Q", "0004", "201");

        public String code;
        public String tradingCode;
        public String iscdCode;

        eMarketType(String code, String tradingCode, String iscdCode) {
            this.code = code;
            this.tradingCode = tradingCode;
            this.iscdCode = iscdCode;
        }
    }

    /**
     * 계좌개설 상태
     */
    public enum eCreateAccountStatus {
        NONE,

        CREATE_ACCOUNT_FAILE,  // 계좌개설 취소
        CREATE_ACCOUNT_COMPLET, // 계좌개설 완료

        ID_CHECK_COMPLET, // 신분증 확인 완료
        ID_CHECK_ING,     // 신분증 확인중
        ID_CHECK_FAILE,   // 신분증 확인실패

        TRANSFER_SIGN_NO, // 이체인증번호확인 (1원 입금)

        SIGN_NO_WAITING, // 이체인증번호 입력대기
        SIGN_NO_COMPLET, // 이체인증번호 입력완료
        SIGN_NO_ERROR, // 이체인증번호 5회오류
        SIGN_NO_TRANSFER_ERROR // 이체인증번호 이체오류
    }

    /**
     * 계좌개설 단계
     */
    public enum eCreateAccountStep {
        NONE, //
        STEP_CHECK, // 개좌개설 완료 여부 체크
        ID_CHECK, // 신분증 확인 체크
        TRANSFER_TYPE_CHECK, // 기존계좌확인방법구분코드 체크
        SIGN_NO_CHECK, // 이체인증번호확인 체크
    }

    /**
     * 이동통신사 정보
     */
    public enum ePhoneCompany {
        NONE("", ""),
        SKT("SKT", "1"),
        KT("KT", "2"),
        LGU("LG U+", "3"),
        SKT_FLAT("SKT 알뜰폰", "5"),
        KT_FLAT("KT 알뜰폰", "6"),
        LGU_FLAT("LGU+ 알뜰폰", "7");

        public String label;
        public String code;

        ePhoneCompany(String label, String code) {
            this.label = label;
            this.code = code;
        }
    }

    /**
     * 실시간 전문 호출 타입
     */
    public enum eRealTimeType {
        U00, // 업종지수체결
        U03, // 업종지수등락
        F00, // 선물체결
        V00, // 해외지수체결
        S00, // 주식체결
        S02, // 주식예상체결
        S03, // 주식시간외단일가체결
        S04, // 주식시간외단일가예상체결
        S06, // 주식거래원
        S11, // 주식체결
        S15, // 주식호가
        S16, // 주식LP (ETF)
        S17, // 주식시간외단일가호가
        Y00, // 해외주식체결
        Y01, // 해외주식 호가
        Y10, // 해외주식체결 - 지연
        Y11, // 해외주식 호가 - 지연
        S19  // Vi 발동(변동완화)
    }

    /**
     * 휴대폰 송금 홈페이지 URL 경로명
     */
    public static final String PHONE_TRANSFER_LINK_URL = "https://www.hanwhawm.com/sbank/trans.cmd?skey=";
    public static final String PHONE_TRANSFER_LINK_URL_DEV = "https://devm.hanwhawm.com:9090/sbank/trans.cmd?skey=";

    /**
     * 탈잉 이벤트 연결 URL
     */
    public static final String EVENT_TALING_LINK_URL = "https://taling.me/Event/accountEvent.php";

    /**
     * File 내부 저장 폴더
     */
    public static final String EXTERNAL_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory() + "/Pine/";

    /**
     * 주소 타입
     */
    public interface ADDRESS_TYPE {
        String HOME = "01";         // 자택
        String WORK_PLACE = "02";   // 직장
    }

    /**
     * 상품군 타입
     */
    public enum eProductGroup {
        ALL("", "전체", ""),
        STOCK("J", "주식", "01"),
        OVERSEAS("OY", "해외주식", "04"),
        JISU("U_OU_F", "지수", ""), // code 다양하여 사용불가 - U:국냊지수, OU:해외지수, _F:지수선물
        RAW("OM", "원자재", ""),
        EXCHANGE("OX", "환율", ""),
        INTEREST("OI", "금리", ""),
        FUND("FN", "펀드", "03"),
        BOND("B", "채권", "02"),
        BOND_OVERSEAS("OB", "해외채권", "05"),
        ESTATE("OE", "부동산", ""), // 부동산 PINE 미노출

        //eDamCategoryGroup
        PORTFOLIO("P", "포트폴리오", "00");

        public String code;
        public String label;
        public String damCode;

        eProductGroup(String code, String label, String damCode) {
            this.code = code;
            this.label = label;
            this.damCode = damCode;
        }
    }

    /**
     * 기타검색 상품군 타입
     */
    public enum eSearchProductGroup {
        STOCK("주식", R.attr.bg_asset_01, true),
        OVERSEAS("해외주식", R.attr.bg_asset_02, true),
        JISU("지수", R.attr.bg_asset_03, false),
        RAW("원자재", R.attr.bg_asset_04, false),
        JISU_FUTURE("지수선물", R.attr.bg_asset_05, false),
        INTEREST("금리", R.attr.bg_asset_06, false),
        EXCHANGE("환율", R.attr.bg_asset_07, false),
        BOND("채권", R.attr.bg_asset_08, true),
        FUND("펀드", R.attr.bg_asset_09, true),
        ETF("ETF", R.attr.bg_asset_10, false);

        public String label;
        @AttrRes
        public int icon;
        public boolean isFilter;  // 검색쪽에서 재정의후 삭제 Const에서도 삭제해서 중복 제거

        eSearchProductGroup(String label, @AttrRes int icon, boolean isFilter) {
            this.label = label;
            this.icon = icon;
            this.isFilter = isFilter;
        }
    }

    /**
     * 자산 구분 (자산상세에서 ordinal() 사용)
     */
    public enum eAssetsType {
        PORTFOLIO("포트폴리오"),
        STOCK("주식"),
        STOCK_OVERSEAS("해외주식"),
        FUND("펀드"),
        BOND("채권"),
        BOND_OVERSEAS("해외채권"),
        CASH("현금"),
        FOREIGN("외화현금"),
        ETC("기타"),
        RCVA("미수금");
        public String label;

        eAssetsType(String label) {
            this.label = label;
        }
    }

    /**
     * DAM 그룹구분코드
     */
    public enum eDamCategoryGroup {
        ALL("", "전체"),
        PORTFOLIO("P", "포트폴리오"),
        DAM("D", "전용상품"),
        INDIVID("I", "개별상품");
        public String code;
        public String label;

        eDamCategoryGroup(String code, String label) {
            this.code = code;
            this.label = label;
        }
    }

    /**
     * DAM 상품구분코드
     */
    public enum eDamPortpolioGroup {
        NONE("0", "", "P"),
        STOCK("1", "주식형", "P"),
        FUND("2", "펀드형", "P"),
        BOND("3", "채권형", "P"),
        MIX("4", "혼합형", "P"),
        STOCK_OVERSEAS("5", "해외주식형", "P"),
        BOND_OVERSEAS("6", "해외채권형", "P"),
        ETC("9", "기타", "P");

        public String code;
        public String label;
        public String marketType;

        eDamPortpolioGroup(String code, String label, String marketType) {
            this.code = code;
            this.label = label;
            this.marketType = marketType;
        }
    }

    /**
     * 포트폴리오 성향 구분코드
     */
    public enum ePortfolioPtflNclType {
        STANDARD("0", "표준"),
        AGGRESSIVE("1", "공격"),
        STABLE("2", "안정");
        public String code;
        public String label;

        ePortfolioPtflNclType(String code, String label) {
            this.code = code;
            this.label = label;
        }
    }

    /**
     * 주문 상태
     */
//    public enum eOrderStatusType {
//        BUY_DONE("1", "구매완료"),
//        SELL_DONE("2", "판매완료"),
//        BUY_PROC("3", "구매신청중"),
//        SELL_PROC("4", "판매신청중"),
//        BUY_CANCEL("5", "구매취소"),
//        SELL_CANCEL("6", "판매취소"),
//        OVERSEA_BUY_DONE("01", "구매완료"),
//        OVERSEA_SELL_DONE("02", "판매완료"),
//        OVERSEA_BUY_PROC("03", "구매신청중"),
//        OVERSEA_SELL_PROC("04", "판매신청중"),
//        OVERSEA_BUY_CANCEL("05", "구매취소"),
//        OVERSEA_SELL_CANCEL("06", "판매취소");
//
//        public String code;
//        public String label;
//
//        eOrderStatusType(String code, String label) {
//            this.code = code;
//            this.label = label;
//        }
//    }

    /**
     * 주문내역 버튼 상태
     */
    public enum eOrderButtonGroup {
        // 01추가구매+취소 02취소 03추가구매 04판매 05추가구매+판매
        ADD_CANCEL("01"),
        CANCEL("02"),
        ADD("03"),
        SELL("04"),
        ADD_SELL("05");

        public String code;

        eOrderButtonGroup(String code) {
            this.code = code;
        }
    }

    /**
     * 색상 타입
     */
    public enum eDaebiColor {
        NONE, UPPER, HIGH, LOW, LOWER
    }

    /**
     * 화면 코드
     */
    public enum eScreenNo {
        NONE(""),
        CA00000("CA00000"),     // 계좌개설화면
        CA00001("CA00001"),     // 신분증 재촬영 화면
        CA00002("CA00002"),     // 계좌개설 완료 화면
        CA00003("CA00003"),     // 개좌개설 1원 입금 입력 화면
        CA00004("CA00004"),     // 송금하기 화면으로 이동
        CA00005("CA00005"),     // 간편송금 출금계좌설정 화면
        CA00007("CA00007"),     // 탈링 이벤트 화면
        CA00012("CA00012"),     // 홈화면 이동
        CA00013("CA00013"),     // 해외 송금화면 이동
        ;

        public String code;

        eScreenNo(String code) {
            this.code = code;
        }

        public static eScreenNo getNo(String code) {
            eScreenNo resultType = NONE;
            if (!TextUtils.isEmpty(code))
                code = code.toUpperCase();
            for (eScreenNo type : values()) {
                if (type.code.equalsIgnoreCase(code)) {
                    resultType = type;
                    break;
                }
            }
            return resultType;
        }

        public static boolean isScreenNo(String code) {
            eScreenNo resultType = getNo(code);
            if (resultType != Const.eScreenNo.NONE){
                return true;
            }
            return false;
        }
    }

    /**
     * 전문 요청 상태 체크
     */
    public enum eDataLoadState {
        NONE, ING, SUCCESS, FAILD
    }

    /**
     * 설문 조사
     */
    public static final String RESEARCH_LINK_URL = "https://m.hanwhawm.com:9090/M/event/event/event_common_view.cmd?event_code=20190617&userid=%s&random=%s";
    public static final String RESEARCH_LINK_URL_DEV = "https://devm.hanwhawm.com:9090/M/event/event/event_common_view.cmd?event_code=20190617&userid=%s&random=%s";


    /***********************************************************************************************************************************/
    // 비대면용
    /***********************************************************************************************************************************/
    /**
     * 투자위험등급 (비대면용)
     */
    public enum eUntactRiskGrade {
        NONE("", "정보미제공", "", R.attr.color_risk_grade_0, 0, ""),
        STABILITY("1", "안정형", "투자원금의 손실이 발생하는 것을 원하지 않으며, 안전한 투자를 목표로 합니다.",
                R.attr.InvestResultType1Background, R.drawable.untact_ic_graph_5, "5"),
        PURSUING_STABILITY("2", "안정추구형", "원금 손실 위험은 최소화하고, 이자나 배당소득 수준의 안정적 투자를 목표로 합니다.",
                R.attr.InvestResultType1Background, R.drawable.untact_ic_graph_3, "4"),
        DANGER("3", "위험중립형", "투자에 상응하는 투자위험이 있음을 충분히 인식하고 있습니다.",
                R.attr.InvestResultType1Background, R.drawable.untact_ic_graph_3, "3"),
        ACTIVE("4", "적극투자형", "투자원금의 보전보다는 위험을 감내하더라도 높은 수준의 수익을 추구합니다.",
                R.attr.InvestResultType1Background, R.drawable.untact_ic_graph_2, "2"),
        ATTACK("5", "공격투자형", "높은 수준의 투자수익을 추구하며, 자산의 높은 손실위험을 적극 수용합니다.",
                R.attr.InvestResultType1Background, R.drawable.untact_ic_graph_1, "1");

        public String gradeCode;
        public String gradeName;
        public String gradeInfo;
        @AttrRes
        public int gradeBgAttr;
        @DrawableRes
        public int gradeIcon;
        public String diplayGradeCode;

        eUntactRiskGrade(String gradeCode, String gradeName, String gradeInfo, int gradeBgAttr, int gradeIcon, String diplayGradeCode) {
            this.gradeCode = gradeCode;
            this.gradeName = gradeName;
            this.gradeInfo = gradeInfo;
            this.gradeBgAttr = gradeBgAttr;
            this.gradeIcon = gradeIcon;
            this.diplayGradeCode = diplayGradeCode;
        }

        public static eUntactRiskGrade getRiskGradeFromCode(String gradeCode) {
            eUntactRiskGrade resultType = NONE;
            for (eUntactRiskGrade type : values()) {
                if (type.gradeCode.equals(gradeCode)) {
                    resultType = type;
                    break;
                }
            }
            return resultType;
        }
    }

}
