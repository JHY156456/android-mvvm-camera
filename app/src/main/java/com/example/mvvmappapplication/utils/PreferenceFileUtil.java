package com.example.mvvmappapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.mvvmappapplication.App;
import com.example.mvvmappapplication.consts.Const;
import com.example.mvvmappapplication.consts.PreferenceConst;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;


/**
 * PreferenceFileUtil
 *
 * @author josung
 * @version 1.0
 * @since 2016-10-11
 */
public class PreferenceFileUtil extends PreferenceConst {
    private static Hashtable<String, SharedPreferences> PreferenceFile = new Hashtable<>();

    /**
     * SharedPreference 가져오기
     */
    public static SharedPreferences getPreferences(String filename) {
        if (null == filename) {
            return null;
        }

        SharedPreferences preferences = PreferenceFile.get(filename);
        if(preferences == null) {
            PreferenceFile.put(filename, App.getContext().getSharedPreferences(filename, Context.MODE_PRIVATE));
        }

        return PreferenceFile.get(filename);
    }

    /**
     * SharedPreferences.Editor 가져오기
     */
    public static SharedPreferences.Editor getPreferencesEditor(String filename) {
        SharedPreferences pref = getPreferences(filename);
        if (null == pref) {
            return null;
        }
        return pref.edit();
    }

    /**************************************************************************************************************/
    /**                            프리페어런스 int,string,long,boolean getter&setter                               */
    /**************************************************************************************************************/

    public static String getString(String filename, String key) {
        return getPreferences(filename).getString(key, "");
    }

    public static String getString(String filename, String key, String defValue) {
        return getPreferences(filename).getString(key, defValue);
    }

    public static boolean setString(String filename, String key, String value) {
        SharedPreferences.Editor editor = getPreferencesEditor(filename);
        editor.putString(key, value);
        return editor.commit();
    }

    public static int getInt(String filename, String key, int defValue) {
        return getPreferences(filename).getInt(key, defValue);
    }

    public static boolean setInt(String filename, String key, int value) {
        SharedPreferences.Editor editor = getPreferencesEditor(filename);
        editor.putInt(key, value);
        return editor.commit();
    }

    public static long getLong(String filename, String key) {
        return getPreferences(filename).getLong(key, 0);
    }

    public static long getLong(String filename, String key, long defValue) {
        return getPreferences(filename).getLong(key, defValue);
    }

    public static boolean setLong(String filename, String key, long value) {
        SharedPreferences.Editor editor = getPreferencesEditor(filename);
        editor.putLong(key, value);
        return editor.commit();
    }

    public static boolean getBoolean(String filename, String key) {
        return getPreferences(filename).getBoolean(key, false);
    }

    public static boolean getBoolean(String filename, String key, boolean defValue) {
        return getPreferences(filename).getBoolean(key, defValue);
    }

    public static boolean setBoolean(String filename, String key, boolean value) {
        SharedPreferences.Editor editor = getPreferencesEditor(filename);
        editor.putBoolean(key, value);
        return editor.commit();
    }


    /**************************************************************************************************************/
    /**                                           name별 key별 저장 및 조회                                         */
    /**************************************************************************************************************/

    /**
     * MCI 핸들러 (set)
     */
    public static void setMCIInit_MciHld(String value) {
        setString(HS_SERVER_INFO, ServerInfoPreferences.MCIHLD, value);
    }

    /**
     * MCI 핸들러 (get)
     */
    public static String getMCIInit_MciHld() {
        return getString(HS_SERVER_INFO, ServerInfoPreferences.MCIHLD, "");
    }

    /**
     * MCI 공인 IP (set)
     */
    public static void setMCIInit_MciOfcIp(String value) {
        String enc = CryptUtil.getEncodeToDes(value);
        setString(HS_SERVER_INFO, ServerInfoPreferences.MCIOFCIP, enc);
    }

    /**
     * MCI 공인 IP (get)
     */
    public static String getMCIInit_MciOfcIp() {
        String dec = CryptUtil.getDecodeToDes(getString(HS_SERVER_INFO, ServerInfoPreferences.MCIOFCIP, ""));

        if (dec == null) dec = "";
        return dec;
    }

    /**
     * MCI set Today (set)
     */
    public static void setMCIToday(String mciToday) {
        setString(HS_SERVER_INFO, ServerInfoPreferences.MCI_TODAY, mciToday);
    }

    /**
     * MCI get Today (get)
     */
    public static String getMCIToday() {
        return getString(HS_SERVER_INFO, ServerInfoPreferences.MCI_TODAY, "");
    }

    /**
     * MCI set Time (set)
     */
    public static void setMCITime(String mciTime) {
        setString(HS_SERVER_INFO, ServerInfoPreferences.MCI_TIME, mciTime);
    }

    /**
     * MCI get Time (get)
     */
    public static String getMCITime() {
        return getString(HS_SERVER_INFO, ServerInfoPreferences.MCI_TIME, "");
    }


    /**
     * DeviceToday (set)
     */
    public static void setDeviceToday(long deviceToday) {
        setLong(HS_SERVER_INFO, ServerInfoPreferences.DEVICE_TDDAY, deviceToday);
    }

    /**
     * DeviceToday (get)
     */
    public static long getDeviceToday() {
        return getLong(HS_SERVER_INFO, ServerInfoPreferences.DEVICE_TDDAY, 0);
    }

    /**
     * 시스템 공지 skip id (set)
     */
    public static void setSystemNoticeSkipId(String noticeId) {
        setString(HS_SERVER_INFO, ServerInfoPreferences.SYSTEM_NOTICE_SKIP_ID, noticeId);
    }

    /**
     * 시스템 공지 skip id (get)
     */
    public static String getSystemNoticeSkipId() {
        return getString(HS_SERVER_INFO, ServerInfoPreferences.SYSTEM_NOTICE_SKIP_ID, "");
    }

    /**
     * 시스템 skip notice 노출 시간 (set)
     */
    public static void setSystemNoticeLastShowTime(long lastShowTime) {
        setLong(HS_SERVER_INFO, ServerInfoPreferences.SYSTEM_NOTICE_LAST_SHOW_TIME, lastShowTime);
    }

    /**
     * 시스템 skip notice 노출 시간 (get)
     */
    public static long getSystemNoticeLastShowTime() {
        return getLong(HS_SERVER_INFO, ServerInfoPreferences.SYSTEM_NOTICE_LAST_SHOW_TIME, 0);
    }

    /**
     * 데이터 암호화 키 저장
     */
    public static void setEncryptionKey(String key) {
        setString(HS_SERVER_INFO, ServerInfoPreferences.ENCRYPTION_KEY, key);
    }

    /**
     * 데이터 암호화 키 반환
     */
    public static String getEncryptionKey() {
        return getString(HS_SERVER_INFO, ServerInfoPreferences.ENCRYPTION_KEY, null);
    }

    /**
     * 단말기 지정 DeviceId 마이그레이션 여부 저장 (Android 8.0 부터 Android10 미만만 마이그레이션 진행)
     */
    public static void setMigrationToTerminalID(boolean isMigration) {
        setBoolean(HS_SERVER_INFO, ServerInfoPreferences.MIGRATION_TERMINALID, isMigration);
    }

    /**
     * 단말기 지정 DeviceId 마이그레이션 여부 반환
     */
    public static boolean isMigrationToTerminalID() {
        return getBoolean(HS_SERVER_INFO, ServerInfoPreferences.MIGRATION_TERMINALID, false);
    }

    /**
     * 자동 로그인
     */
    public static String getAutoLoginType() {
        return getString(HS_LOGIN_INFO, LoginInfoPreferences.SNS_TYPE);
    }

    public static void setAutoLoginType(String snsType) {
        setString(HS_LOGIN_INFO, LoginInfoPreferences.SNS_TYPE, snsType);
    }

    public static String getNaverEmail() {
        return getString(HS_LOGIN_INFO, LoginInfoPreferences.SNS_NAVER_EMAIL);
    }

    public static void setNaverEmail(String naverEmail) {
        setString(HS_LOGIN_INFO, LoginInfoPreferences.SNS_NAVER_EMAIL, naverEmail);
    }

    public static String getAutoLoginId() {
        return getString(HS_LOGIN_INFO, LoginInfoPreferences.SNS_ID);
    }

    public static void setAutoLoginId(String snsId) {
        setString(HS_LOGIN_INFO, LoginInfoPreferences.SNS_ID, snsId);
    }

    public static String getSnsLastLoginDate() {
        return getString(HS_LOGIN_INFO, LoginInfoPreferences.SNS_LAST_LOGIN_DATE);
    }

    public static void setSnsLastLoginDate(String lastLoginDate) {
        setString(HS_LOGIN_INFO, LoginInfoPreferences.SNS_LAST_LOGIN_DATE, lastLoginDate);
    }

    /**
     * 금결원 바이오인증 축약서명 공개키 가져오기
     */
    public static String getLinkPUBKEY() {
        String dec = getString(HS_KFTCBIO_INFO, KFTCBioInfoPreferences.PREF_LINK_PUBKEY, "");
        if (TextUtils.isEmpty(dec)) {
            return "";
        }
        return dec;
    }

    /**
     * 금결원 바오이인증 축약서명 공개키 저장하기
     */
    public static void setLinkPUBKEY(String pubKey) {
        setString(HS_KFTCBIO_INFO, KFTCBioInfoPreferences.PREF_LINK_PUBKEY, pubKey);
    }

    /**
     * 금결원 바오이인증 축약서명 개인키 가져오기
     */
    public static String getLinkPRVKEY() {
        String dec = getString(HS_KFTCBIO_INFO, KFTCBioInfoPreferences.PREF_LINK_PRVKEY, "");
        if (TextUtils.isEmpty(dec)) {
            return "";
        }
        return dec;
    }

    /**
     * 금결원 바오이인증 축약서명 개인키 저장하기
     */
    public static void setLinkPRVKEY(String pubKey) {
        setString(HS_KFTCBIO_INFO, KFTCBioInfoPreferences.PREF_LINK_PRVKEY, pubKey);
    }





    public static void setMigrationVersion(int version) {
        setInt(HS_SETTING_INFO, SettingInfoPreferences.STEPS_MIGRATION_VERSION, version);
    }

    /**
     * 마스터 정보 마지막으로 가져온 날짜
     */
    public static String getMasterUpdate() {
        return getString(HS_SETTING_INFO, SettingInfoPreferences.MASTER_UPDATE_DAY, "");
    }

    public static void setMasterUpdate(String updateDay) {
        setString(HS_SETTING_INFO, SettingInfoPreferences.MASTER_UPDATE_DAY, updateDay);
    }

    /**
     * IACN(자산) 가져오기
     */
    public static String getIacnInAsset() {
        return getString(HS_ASSET_INFO, AssetPreferences.ASSET_IACN, "");
    }

    public static void setIacnInAsset(String iacn) {
        setString(HS_ASSET_INFO, AssetPreferences.ASSET_IACN, iacn);
    }

    /**
     * 환경설정 계좌비빌번호 저장
     */
    public static String getSettingAutoIacnPwd() {
        return CryptUtil.getDecodeToDes(getString(HS_SETTING_INFO, SettingInfoPreferences.PREF_IACN_PWD_SAVE, ""));
    }

    public static void setSettingAutoIacnPwd(String iacnPwd) {
        setString(HS_SETTING_INFO, SettingInfoPreferences.PREF_IACN_PWD_SAVE, CryptUtil.getEncodeToDes(iacnPwd));
    }

    /**
     * 환경설정 계좌비빌번호 상태 값 저장
     */
    public static String getSettingAutoIacnPwdYN() {
        return getString(HS_SETTING_INFO, SettingInfoPreferences.PREF_IACN_PWD_YN, "N");
    }

    public static void setSettingAutoIacnPwdYN(String state) {
        setString(HS_SETTING_INFO, SettingInfoPreferences.PREF_IACN_PWD_YN, state);
    }

    /**
     * 환경설정 지문인증 상태 값 저장
     */
    public static String getSettingFingerPrintYN() {
        return getString(HS_SETTING_INFO, SettingInfoPreferences.PREF_FINGER_PRINT_CHECK, "");
    }

    public static void setSettingFingerPrintYN(String state) {
        setString(HS_SETTING_INFO, SettingInfoPreferences.PREF_FINGER_PRINT_CHECK, state);
    }

    /**
     * Intro preview실행 여부
     */
    public static String getIntroPreview() {
        return getString(HS_SETTING_INFO, SettingInfoPreferences.PREF_INTRO_PREVIEW_CHECK, "Y");
    }

    public static void setIntroPreview(String state) {
        setString(HS_SETTING_INFO, SettingInfoPreferences.PREF_INTRO_PREVIEW_CHECK, state);
    }

    /**
     * Intro 6.0 미만 사용자 권한 팝업 체크 여부
     */
    public static void setIntroUserPermissionCheck(boolean state) {
        setBoolean(HS_SETTING_INFO, SettingInfoPreferences.PREF_INTRO_USER_PERMISSION_CHECK, state);
    }

    public static boolean getIntroUserPermissionCheck() {
        return getBoolean(HS_SETTING_INFO, SettingInfoPreferences.PREF_INTRO_USER_PERMISSION_CHECK, false);
    }

    public static String getRealServerIPRotate() {
        return getString(HS_SERVER_INFO, ServerInfoPreferences.REAL_SERVER_IP, "0");
    }

    public static void setRealServerIPRotate(String filter) {
        setString(HS_SERVER_INFO, ServerInfoPreferences.REAL_SERVER_IP, filter);
    }

    public static String getTestServerIPRotate() {
        return getString(HS_SERVER_INFO, ServerInfoPreferences.TEST_SERVER_IP, "0");
    }

    public static void setTestServerIPRotate(String filter) {
        setString(HS_SERVER_INFO, ServerInfoPreferences.TEST_SERVER_IP, filter);
    }


    /**
     * 공지사항 Snotice_msg 반환
     *
     * @return
     */
    public static String getNoticeMsg() {
        return getString(HS_EVENT_NOTICE, EventNotice.NOTICE_MSG, "");
    }

    /**
     * 공지사항 Snotice_msg 저장
     *
     * @param noticeMsg
     */
    public static void setNoticeMsg(String noticeMsg) {
        setString(HS_EVENT_NOTICE, EventNotice.NOTICE_MSG, noticeMsg);
    }

    /**
     * 이벤트 공지 팝업 내 다시보지않기, 오늘 하루만 보기 데이터
     */
    public static void setEventNoticeShowNomoreDay(String date) {
        setString(HS_EVENT_NOTICE, EventNotice.EVENT_SHOW_NOMORE_DAY, date);
    }

    public static void setEventNoticeShowNomoreDay1(String date) {
        setString(HS_EVENT_NOTICE, EventNotice.EVENT_SHOW_NOMORE_DAY_1, date);
    }

    public static String getEventNoticeShowNomoreDay() {
        return getString(HS_EVENT_NOTICE, EventNotice.EVENT_SHOW_NOMORE_DAY, "");
    }

    public static String getEventNoticeShowNomoreDay1() {
        return getString(HS_EVENT_NOTICE, EventNotice.EVENT_SHOW_NOMORE_DAY_1, "");
    }

    public static String getAssociateUserId() {
        return getString(HS_LOGIN_INFO, LoginInfoPreferences.AC_USER_ID, "");
    }

    public static void setAssociateUserId(String ac_user_id) {
        setString(HS_LOGIN_INFO, LoginInfoPreferences.AC_USER_ID, ac_user_id);
    }


    public static String[] getEventMymenuBadge() {
        String seqs = getString(HS_EVENT_NOTICE, EventNotice.EVENT_MYMENU_BADGE, "");
        if (TextUtils.isEmpty(seqs)) {
            return null;
        }
        String[] seq = seqs.split(",");
        return seq;
    }

    public static void setEventMymenuBadge(String[] seq) {
        if (seq == null) {
            setString(HS_EVENT_NOTICE, EventNotice.EVENT_MYMENU_BADGE, "");
            return;
        }

        String seqs = "";
        for (String s : seq) {
            if (!TextUtils.isEmpty(s)) {
                seqs += s + ",";
            }
        }
        setString(HS_EVENT_NOTICE, EventNotice.EVENT_MYMENU_BADGE, seqs);
    }

    public static boolean getEventBadgeCheckIn() {
        return getBoolean(HS_EVENT_NOTICE, EventNotice.EVENT_MYMENU_CHECK_IN_BADGE, false);
    }

    public static void setEventBadgeCheckIn(boolean checkin) {
        setBoolean(HS_EVENT_NOTICE, EventNotice.EVENT_MYMENU_CHECK_IN_BADGE, checkin);
    }

    /**
     * Adbrix key 등록 여부 반환
     *
     * @param name
     * @return
     */
    public static boolean isAdbrixKeys(String name) {
        boolean result = false;
        String strKeys = getString(HS_EVENT_NOTICE, EventNotice.EVENT_ADBRIX_KEYS, "");
        if (!TextUtils.isEmpty(strKeys)) {
            String[] keys = strKeys.split(",");
            for (String key : keys) {
                if (key.equals(name)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Adbrix key 등록
     *
     * @param key
     */
    public static void setAdbrixKey(String key) {
        String keys = getString(HS_EVENT_NOTICE, EventNotice.EVENT_ADBRIX_KEYS, "");
        if (!TextUtils.isEmpty(keys)) {
            keys += ",";
        }
        keys += key;
        setString(HS_EVENT_NOTICE, EventNotice.EVENT_ADBRIX_KEYS, keys);
    }

    public static void setEventItemValue(String key, String value) {
        setString(HS_EVENT_NOTICE, key, value);
    }

    public static String getEventItemValue(String key) {
        return getString(HS_EVENT_NOTICE, key, "");
    }

    /**
     * URL Scheme로 부터 전달받은 scrno 데이터
     *
     * @return
     */
    public static String getUrlSchemeResult() {
        return getString(HS_URL_SCHEME_INFO, UrlSchemeInfo.SCHEME_CREATE_ACCOUNT_RESULT, "");
    }

    public static void setUrlSchemeResult(String scrno) {
        setString(HS_URL_SCHEME_INFO, UrlSchemeInfo.SCHEME_CREATE_ACCOUNT_RESULT, scrno);
    }

    /**
     * 마지막 이체 이용 계좌번호
     */
    public static String getLastTransferAccount() {
        String decAccount = "";
        try {
            String account = getString(HS_TRANSFER_INFO, TransferInfoPreferences.TRANSFER_LAST_USING_ACCOUNT);
            decAccount = CryptUtil.getDecodeToDes(account);
        } catch (Exception e) {
        }

        return decAccount;
    }

    //user id 암호화하여 저장.
    public static void setLastTransferAccount(String account) {
        try {
            String encAccount = CryptUtil.getEncodeToDes(account);
            if (!TextUtils.isEmpty(encAccount) && !TextUtils.isEmpty(account)) {
                setString(HS_TRANSFER_INFO, TransferInfoPreferences.TRANSFER_LAST_USING_ACCOUNT, encAccount);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 테마 설정
     */
    public static Const.eThemeType getThemeType() {
        Const.eThemeType eThemeType = Const.eThemeType.WHITE;
        String themeType = getString(HS_SETTING_INFO, SettingInfoPreferences.PREF_THEME_TYPE, eThemeType.name());
        try {
            eThemeType = Const.eThemeType.valueOf(themeType);
        } catch (Exception e) {
        }
        return eThemeType;
    }

    public static void setThemeType(Const.eThemeType state) {
        setString(HS_SETTING_INFO, SettingInfoPreferences.PREF_THEME_TYPE, state.name());
    }

    /***********************************************************************************************
     * 파일 업로드 관련
     ***********************************************************************************************/

    /**
     * @param date
     */
    public static void setLastUpdateMockStockDate(Calendar date) {
        setLong(HS_FILE_UPLOAD, FileUploadPreferences.LAST_UPDATE_MOCK_STOCK_DATE, date.getTimeInMillis());
    }


    public static Calendar getLastUpdateMockStockDate() {
        long date = getLong(HS_FILE_UPLOAD, FileUploadPreferences.LAST_UPDATE_MOCK_STOCK_DATE, -1);
        if (date == -1) {
            return null;
        }

        Calendar result = Calendar.getInstance();
        result.setTimeInMillis(date);

        return result;
    }

    /***********************************************************************************************
     * 계좌개설 관련
     ***********************************************************************************************/

    /**
     * 계좌개설 시 사용한 고객명
     *
     * @return
     */
    public static String getCreateAccountUserName() {
        return getString(HS_CREATE_ACCOUNT_INFO, CreateAccountPreferences.USER_NAME, "");
    }

    public static void setCreateAccountUserName(String userName) {
        setString(HS_CREATE_ACCOUNT_INFO, CreateAccountPreferences.USER_NAME, userName);
    }

    /**
     * 계좌개설 시 사용한 주민번호
     *
     * @return
     */
    public static String getCreateAccountUserJumin() {
        String number = getString(HS_CREATE_ACCOUNT_INFO, CreateAccountPreferences.USER_JUMIN_OLD, "");
        if (!TextUtils.isEmpty(number)) {
            if (StringUtil.isNumber(CryptUtil.getDecodeToDes(number))) {
                char[] numbers = CryptUtil.getDecodeToDes(number).toCharArray();
                PreferenceFileUtil.setCreateAccountUserJumin(CryptUtil.encryptToGA(numbers));
                setString(HS_CREATE_ACCOUNT_INFO, CreateAccountPreferences.USER_JUMIN_OLD, "");
                Arrays.fill(numbers, (char) 0x20);
            }
        }
        return getString(HS_CREATE_ACCOUNT_INFO, CreateAccountPreferences.USER_JUMIN, "");
    }

    public static void setCreateAccountUserJumin(String encJuminNum) {
        setString(HS_CREATE_ACCOUNT_INFO, CreateAccountPreferences.USER_JUMIN, encJuminNum);
    }

    /*
    * OCR
    * */
    public static void setPreferOcrInfo(String _IdCardType, String _IdCardImageId, String _IdCardIssuesDate, String _IdCardEncLicenseNum,
                                        String _IdCardScpsCrtfAttcCnfmScd, String _IdCardSrcvSn, String _IdCardAttcCnfmCnteRspnCod,
                                        String _IdCardMaskRelmCtns, String _IdCardCroppedImageBase64) {
        setString(HS_UNTACT_CREATE_ACCOUNT_OCR_FACEPHI_INFO, OcrFacePhiPreferences.USER_IDCARD_TYPE, _IdCardType);
        setString(HS_UNTACT_CREATE_ACCOUNT_OCR_FACEPHI_INFO, OcrFacePhiPreferences.USER_IDCARD_IMAGEID, _IdCardImageId);
        setString(HS_UNTACT_CREATE_ACCOUNT_OCR_FACEPHI_INFO, OcrFacePhiPreferences.USER_IDCARD_ISSUESDATA, _IdCardIssuesDate);
        setString(HS_UNTACT_CREATE_ACCOUNT_OCR_FACEPHI_INFO, OcrFacePhiPreferences.USER_IDCARD_ENCLICENSENUM, _IdCardEncLicenseNum);
        setString(HS_UNTACT_CREATE_ACCOUNT_OCR_FACEPHI_INFO, OcrFacePhiPreferences.USER_IDCARD_SCPSCRTFATTCCNFMSCD, _IdCardScpsCrtfAttcCnfmScd);
        setString(HS_UNTACT_CREATE_ACCOUNT_OCR_FACEPHI_INFO, OcrFacePhiPreferences.USER_IDCARD_SRCVSN, _IdCardSrcvSn);
        setString(HS_UNTACT_CREATE_ACCOUNT_OCR_FACEPHI_INFO, OcrFacePhiPreferences.USER_IDCARD_ATTCCNFMCNTERSPNCOD, _IdCardAttcCnfmCnteRspnCod);
        setString(HS_UNTACT_CREATE_ACCOUNT_OCR_FACEPHI_INFO, OcrFacePhiPreferences.USER_IDCARD_MASKRELMCTNS, _IdCardMaskRelmCtns);
        setString(HS_UNTACT_CREATE_ACCOUNT_OCR_FACEPHI_INFO, OcrFacePhiPreferences.USER_IDCARD_CROPPEDIMAGEBASE64, _IdCardCroppedImageBase64);
    }

    /*
    * FACEPHI
    * */
    public static void setPreferFacePhiInfo(String _facialFileName) {
        setString(HS_UNTACT_CREATE_ACCOUNT_OCR_FACEPHI_INFO, OcrFacePhiPreferences.USER_FACEPHI_FACIAL_FILENAME, _facialFileName);
    }
    public static void setPreferFacePhiInfo(String _facialAwarRsltScd, String _facialImgDscmNo) {
        setString(HS_UNTACT_CREATE_ACCOUNT_OCR_FACEPHI_INFO, OcrFacePhiPreferences.USER_FACEPHI_FACIAL_AWARRSLTSCD, _facialAwarRsltScd);
        setString(HS_UNTACT_CREATE_ACCOUNT_OCR_FACEPHI_INFO, OcrFacePhiPreferences.USER_FACEPHI_FACIAL_IMAGDSCMNO, _facialImgDscmNo);
    }

    public static String getPreferOcrFacePhiInfo(String _key) {
        return getString(HS_UNTACT_CREATE_ACCOUNT_OCR_FACEPHI_INFO, _key, "");
    }



}

