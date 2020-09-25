package com.example.mvvmappapplication.dto;

import android.text.TextUtils;

import com.example.mvvmappapplication.consts.MainEvent;
import com.example.mvvmappapplication.utils.CryptUtil;
import com.example.mvvmappapplication.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;


public class UserInfoDto {
    // 프로필 정보
    private String mProfileName;           // 닉네임
    private String mProfileImageKey;      // 썸네일 링크
    private static UserInfoDto instance = new UserInfoDto();
    public static UserInfoDto getInstance() {
        return instance;
    }
    private String mUserId;             // 회원 아이디

    /****************************************************************************************************************
     * 프로필 정보
     ****************************************************************************************************************/

    public void setProfileName(String nickName) {
        mProfileName = nickName;
        EventBus.getDefault().post(new MainEvent(MainEvent.EVENT_CHANGE_PROFILE_NAME_ACTION));
    }
    /**
     * 사용자 아이디 반환
     * @return 사용자 아이디
     */
    public String getUserId() {
        String decUserId = "";
        try {
            decUserId = CryptUtil.getDecodeToDes(mUserId);
        } catch(Exception e) {}

        if (TextUtils.isEmpty(decUserId)) {
            decUserId = "";
        }

        return decUserId;
    }
    public String getProfileName() {
        return mProfileName;
    }

    /**
     * 프로필 원본 이미지 링크 반환
     *
     * @return
     */
    public String getProfileOrignImageUrl() {
        return StringUtil.getProfileImageUrl(mProfileImageKey, false);
    }

    /**
     * 프로필 썸네일 이미지 링크 반환
     *
     * @return
     */
    public String getProfileThumbnailImageUrl() {
        return StringUtil.getProfileImageUrl(mProfileImageKey, true);
    }
}
