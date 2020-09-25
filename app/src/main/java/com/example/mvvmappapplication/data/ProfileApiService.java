package com.example.mvvmappapplication.data;

import com.example.mvvmappapplication.App;
import com.example.mvvmappapplication.consts.Const;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 프로필 이미지
 *
 * @author ehjung
 * @since 2018. 11. 8.
 */
public interface ProfileApiService {

    String IMAGE_DOWNLOAD = "file/profileImage/imageDownload.cmd";
    String BASE_URL =  "http://test.steps.plus:8099/";

    @Multipart
    @POST("file/profileImage/imageUpload.cmd")
    Call<ResponseBody> imageUpload(
            @Part("SECURE_KEY") RequestBody secureKey,      // userId를 암호화한 값
            @Part("user_id") RequestBody userId,            // 사용자 ID
            @Part("profile_gbn") RequestBody profileGbn,    // B, S 이미지 크기
            @Part MultipartBody.Part upFile);               // 프로필 이미지 파일


    /*
     * 개발기 접속 정보 : http://test.steps.plus:8099/~
     * 1. 업로드 : ~/file/profileImage/imageUpload.cmd
     * 2. 다운로드 :  ~/file/profileImage/imageDownload.cmd
     *
     *     <파라미터 정의>
     *     * SECURE_KEY: User_Id를 암호화한 값
     *         KEY = (yyyyMMdd).replaceAll("2","!")
     *         (암호화는 mAppQnaList.cmd 호출 부분 참조)
     *      * user_id : 사용자 ID
     *      * profile_gbn : B,  S 이미지 크기
     *      * up_file : multipart 파일
     *
     * 3. 오류 코드 정의 (STATUS=E0001 패턴)
     *    -E0001 : SECURE_KEY 파라미터 오류
     *    -E0002 : user_id 파라미터 오류
     *    -E0003 : profile_gbn (B 나 S가 아닐경우)
     *    -E1001 : 복호화 오류 (KEY = (yyyyMMdd).replaceAll("2","!")
     *    -E2001 : user_id 와 복호화 값이 다름
     *    -E9006 : 저장중 오류
     *    -E9008 : ContentType이 multipart가 아님
     *    -E9009 : 기타오류
     */
}
