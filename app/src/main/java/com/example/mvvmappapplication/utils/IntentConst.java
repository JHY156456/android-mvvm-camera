package com.example.mvvmappapplication.utils;

/**
 * Created by josung on 2016-08-08..
 */
public class IntentConst {

    public interface INTRO {
        String KEY_EVENT_NOTICE_DETAIL = "key_event_notice_detial";
        int INTENT_EVENT_NOTICE_DETIAL = 100;
        int MESSAGE_V3_COMPLETE = 777;
    }

    public interface IDENTIT_AUTH_ACTIVITY {
        String KEY_TERMS_TYEP = "key_terms_tyep";

        int REQ_CODE_AUTH_CONFIRM = 0x00;
        String KEY_ALL_LOGIN = "key_all_login";
    }

    public interface ACCOUNT_CONNECT_ACTIVITY {
        int REQ_FIND_ADDRESS = 0x00;
        int REQ_CODE_AGREEMENT_CONFIRM_01 = 0x01;
        //계좌 연결
    }

    public interface CREATE_ACCOUNT_ACTIVITY {
        int REQ_FIND_ADDRESS_ZIPCODE = 0x90;
        int REQ_CODE_INVEST_STYLE_REG_ACTIVITY = 0x80;
        int REQ_CODE_INVEST_STYLE_LOOKUP_ACTIVITY = 0x81;
        int REQ_CODE_BANK_SELECT = 0x70;
        int REQ_CODE_ID_CAPTURE = 0xa0;
        int REQ_CODE_LOCATION_SERVICE = 0xb0;
        int REQ_CODE_AGREEMENT_CONFIRM_01 = 0xc0;
        int REQ_CODE_AGREEMENT_CONFIRM_02 = 0xd0;
        int REQ_FIND_ADDRESS_WITH_MAP = 0xe0;
        int REQ_CODE_TRADE_OBJECT_CONFIRM = 0xf0;

        String KEY_TERMS_TITLE = "agreement_title";

        //이전 스텝
        String KEY_STEP_BEFORE = "key_step_before";
        //종합계좌 개설 안내
        String KEY_STEP_INFO_INDEX = "key_step_info_index";
        // 이동 화면 index
        String KEY_JUMP_SCREEN = "key_jump_screen";
        // 고객번호
        String KEY_CUNO = "key_cuno";
        //비밀번호.
        String KEY_STEP_PASSWD_INDEX = "key_step_passwd_index";
        String KEY_STEP_ID_QUESTION_INDEX = "key_step_id_question_index";
        String KEY_STEP_ACCOUNT_NUMBER = "key_step_account_number";

        String ACTIVITY_RESULT_KEY_ADDR_BASE = "AddrBase";
        String ACTIVITY_RESULT_KEY_ADDR_DETAIL = "AddrDetail";
        String ACTIVITY_RESULT_KEY_ZIP_CODE = "ZipCode";
        String ACTIVITY_RESULT_KEY_ADDR_TYPE = "AddrType";
        String ACTIVITY_RESULT_KEY_ADDR_TYPE_CODE = "mSelectAddressCode";
        int STD_ADDRESS = 0;            // 구주소 (지번주소) 화면
        int ROAD_ADDRESS = 1;            // 새주소 (도로명주소) 화면

    }

    public interface CERTI {
        int CERT_INTENT_EXPORT1_ACTIVITY = 1000;
        int CERT_INTENT_REFRESH1_ACTIVITY = 1001;
        int CERT_INTENT_DELETE_ACTIVITY = 1002;
        int CERT_AUTH_ACTIVITY = 1003;
        int CERT_ADD_ACTIVITY = 1004;
        int CERT_INTENT_LOGIN_ACTIVITY = 1006;
        int CERT_INTENT_ORDER_ACTIVITY = 1008;
        int CERT_INTENT_AGREE_ACTIVITY = 1012;
        int CERT_AUTH_TRANSFER = 1013;
        int CERT_INTENT_BIO_MENU_ACTIVITY = 1014;
        int CERT_INTENT_PIN_MENU_ACTIVITY = 1015;
        int RESULT_CERT_BIO_GOTO_CERT_CENTER = 1016;

        String account_no = "account_no";                           // 계좌번호
        String account_pwd = "account_pwd";                         // 계좌번호
        String jumin_no = "jumin_no";                               // 주민번호
        String user_name = "user_name";                             // 성명
        String cust_no = "cust_no";                                 // 고객번호
        String sid = "sid";                                         // 아이디
        String entry_type = "entry_type";                           // 진입타입
        String dev_id = "dev_id";                                   // 단말기 아이디
        String dev_nm = "dev_nm";                                   // 단말기 이름
        String certi_type = "certi_type";                           // sms or ars 선택값
        String tel_value = "tel_value";                             // 통신사 코드
        String tel_name = "tel_name";                               // 통신사명
        String tel_number = "tel_number";                           // 전화번호
        String possess = "possess";                                 // 본인 / 소유, Y / N
        String SCRD_PWD_SQN_1 = "SCRD_PWD_SQN_1";                   // 보안코드1
        String SCRD_PWD_SQN_2 = "SCRD_PWD_SQN_2";                   // 보안코드2
        String FST_SELF_CNFM_QST_SCD = "FST_SELF_CNFM_QST_SCD";     // 질문코드1
        String SCND_SELF_CNFM_QST_SCD = "SCND_SELF_CNFM_QST_SCD";   // 질문내용1
        String FST_SELF_CNFM_CTNS = "FST_SELF_CNFM_CTNS";           // 질문코드2
        String SCND_SELF_CNFM_CTNS = "SCND_SELF_CNFM_CTNS";         // 질문내용2
        String backType = "backType";                               // Y / N
        String online_agree_state = "online_agree_state";           // 온라인 동의 여부 AND 전문 실행 여부
        String jointype="joinType";
        String gaScreen = "gaScreen";

        int KEY_ENTRY_CERTI_REG = 0;                 // 진입타입 공인인증서
        int KEY_ENTRY_CERTI_OTHER_REG = 1;           // 진입타입 공인인증서 (타기관)
        int KEY_ENTRY_DEVICE_REG = 2;                // 진입타입 단말기등록
        int KEY_ENTRY_DEVICE_UNREG = 3;              // 진입타입 단말기 해지 후 등록
        int KEY_ENTRY_TRANSFER_REG = 4;              // 진입타입 이체 단말기등록
        int KEY_ENTRY_TRANSFER_UNREG = 5;            // 진입타입 이체 해지 후 등록
        int KEY_ENTRY_ACCOUNT = 6;                   // 진입타입 연결계좌
        int KEY_ENTRY_TRANSFER_SIGN = 7;             // 진입타입 이체 본인인증

        String KEY_ENTRY_POSSESS_Y = "Y";            // 소지인증 타입
        String KEY_ENTRY_POSSESS_N = "N";            // 본인인증 타입
    }

    public interface TRANSFER {
        int INTENT_ACCOUNT_SELECT_ACTION = 2001;    // 내 계좌 선택
        int INTENT_RECIPIENT_SELECT_ACTION = 2002;  // 보내는 사람 선택
        int INTENT_MTRANSKEY_PASSWD = 2003;            //계좌 비밀번호 요청
        int INTENT_CONFIRM_ACTION = 2004;           // 이체 50만원 이상 안내 화면
        int INTENT_DEVICE_REG_ACTION = 2005;        // 디바이스 등록
        int INTENT_PHONE_CONFIRM_ACTION = 2006;        // 휴대폰 송금 확인
        int INTENT_FDS_ACTION = 2007;               // FDS
        int INTENT_DISPLAY_SENDER_NAME_ACTION = 2008;  // 보내는 사람 입력
        int INTENT_DEVICE_REG_LOGIN_ACTION = 2009;     // 단말기 등록 인증 로그인
        int INTENT_SISE_ACCOUNT_LOGIN_ACTION = 2010;   // 시세모드 계좌등록 인증 로그인
        int INTENT_SISE_TRANSFER_OVER_LOGIN_ACTION = 2011;   // 시세모드 간편송금 시 한도 초과 시 인증 로그인

        String transfer_sender_name = "transfer_data";  // 보내사람 이름
        String title = "title";                         // 타이틀명
        String join_type = "join_type";                 // 진입타입
        String transfer_account = "transfer_account";   // 계좌번호
        String transfer_data = "transfer_data";         // 이체 데이터
        String bankName = "bankName";
        String bankId = "bankId";
        String accountNum = "accountNum";
        String userName = "userName";
        String type = "type";
        String account = "account";
        String phone = "phone";
        String accountNo = "accountNo";
        String accountCreateDate = "accountCreateDate";
        String drwgAmt = "drwgAmt";
        String accountType = "accountType";
        String phoneNumber = "phoneNumber";
        String amount = "amount";
        String autoAccountCheck = "autoAccountCheck";
        String gaScreen = "gaScreen";

        String XTNL_ORG_ID = "XTNL_ORG_ID";
        String XTNL_ORG_KRL_NM = "XTNL_ORG_KRL_NM";
        String XTNL_ORG_NO = "XTNL_ORG_NO";
    }
}
