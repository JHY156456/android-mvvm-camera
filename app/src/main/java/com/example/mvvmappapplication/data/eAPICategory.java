package com.example.mvvmappapplication.data;



/**
 * @author 안승진(seungjin.an)
 * @since 2018. 4. 5.
 */

public enum eAPICategory {

    PROFILE(ProfileApiService.class); //프로필 이미지

    private Class apiClass;

    eAPICategory(Class apiClass) {
        this.apiClass = apiClass;
    }
}
