package com.example.mvvmappapplication.data;

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
    String BASE_URL =  "url";

    @Multipart
    @POST("file/profileImage/imageUpload.cmd")
    Call<ResponseBody> imageUpload(
            @Part("SECURE_KEY") RequestBody secureKey,      // userId를 암호화한 값
            @Part("user_id") RequestBody userId,            // 사용자 ID
            @Part("profile_gbn") RequestBody profileGbn,    // B, S 이미지 크기
            @Part MultipartBody.Part upFile);               // 프로필 이미지 파일
}
