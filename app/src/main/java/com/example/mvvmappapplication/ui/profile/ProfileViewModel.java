package com.example.mvvmappapplication.ui.profile;

import android.app.Application;
import android.text.TextUtils;

import com.example.mvvmappapplication.App;
import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.data.ProfileApiService;
import com.example.mvvmappapplication.data.RetrofitApiServiceBuilder;
import com.example.mvvmappapplication.data.eAPICategory;
import com.example.mvvmappapplication.ui.BaseNavigator;
import com.example.mvvmappapplication.ui.BaseViewModel;
import com.example.mvvmappapplication.utils.AlertUtil;
import com.example.mvvmappapplication.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 프로필 관리 ViewModel
 *
 * @author
 */
public class ProfileViewModel extends BaseViewModel<BaseNavigator> {

    /********************************************************************************************************
     * Consts
     *********************************************************************************************************/

    /********************************************************************************************************
     *
     *********************************************************************************************************/

    /* 변경할 파일 이미지 경로 */
    String mSelectImageFile;
    String mSelectImageFileSmall;
    /* 기본 이미지로 변경 여부 */
    boolean mIsUpdateDefaultImage = false;
    String mNickName;

    public ProfileViewModel(Application context) {
        super(context);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    /********************************************************************************************************
     * TR
     *********************************************************************************************************/

//    /**
//     * 프로필 조회
//     */
//    public void performCCS00540ToInquiry() {
//        NetworkProvider.getInstance1().performCCS00540("1", "", "", new NetworkProvider.ListenerChild() {
//            @Override
//            public void onResponse(NetworkResultDto object) {
//                FIDOrder tr = (FIDOrder) object.getObject();
//                ArrayList<XmlInOutItemDto> itemArr = tr.Item.RecvOutListRec.get(0).arr.get(0).ItemArr;
//
//                String name = FidUtil.getXmlOutItem(itemArr, "name").data;
//                if (TextUtils.isEmpty(name)) {
//                    // Steps2 신규 고객으로 등록된 닉네임이 없는 경우 서버에 유니크한 닉네임 발급 요청
//                    performCCS00540ToCreate();
//                } else {
//                    String logintime = FidUtil.getXmlOutItem(itemArr, "logintime").data;
//                    String id_level = FidUtil.getXmlOutItem(itemArr, "id_level").data;
//                    String link = FidUtil.getXmlOutItem(itemArr, "link").data;
//                    String docid = FidUtil.getXmlOutItem(itemArr, "docid").data;
//                    String result = FidUtil.getXmlOutItem(itemArr, "result").data;
//
//                    GlobalApp.userInfoData().setProfileName(name);
//                    GlobalApp.userInfoData().setProfileImageKey(link);
//                }
//            }
//
//            @Override
//            public void onError(NetworkErrorDto error) {
//                AlertUtil.toast(getActivity(), error.getMessage());
//            }
//        });
//    }

//    /**
//     * 신규 고객 닉네임 생성
//     */
//    private void performCCS00540ToCreate() {
//        NetworkProvider.getInstance1().performCCS00540("4", "", "", new NetworkProvider.ListenerChild() {
//            @Override
//            public void onResponse(NetworkResultDto object) {
//                FIDOrder tr = (FIDOrder) object.getObject();
//                ArrayList<XmlInOutItemDto> itemArr = tr.Item.RecvOutListRec.get(0).arr.get(0).ItemArr;
//
//                String name = FidUtil.getXmlOutItem(itemArr, "name").data;
//                String logintime = FidUtil.getXmlOutItem(itemArr, "logintime").data;
//                String id_level = FidUtil.getXmlOutItem(itemArr, "id_level").data;
//                String link = FidUtil.getXmlOutItem(itemArr, "link").data;
//                String docid = FidUtil.getXmlOutItem(itemArr, "docid").data;
//                String result = FidUtil.getXmlOutItem(itemArr, "result").data;
//
//                GlobalApp.userInfoData().setProfileName(name);
//                GlobalApp.userInfoData().setProfileImageKey(link);
//            }
//
//            @Override
//            public void onError(NetworkErrorDto error) {
//                AlertUtil.toast(getActivity(), error.getMessage());
//            }
//        });
//    }
//
//    /**
//     * 닉네임 변경
//     *
//     * @param name 닉네임
//     */
//    private void performCCS00540ToUpdateName(final String name) {
//        if (TextUtils.isEmpty(name)) {
//            return;
//        }
//        NetworkProvider.getInstance1().performCCS00540("3", name, "", new NetworkProvider.ListenerChild() {
//            @Override
//            public void onResponse(NetworkResultDto object) {
//                FIDOrder tr = (FIDOrder) object.getObject();
//                ArrayList<XmlInOutItemDto> itemArr = tr.Item.RecvOutListRec.get(0).arr.get(0).ItemArr;
//                String name = FidUtil.getXmlOutItem(itemArr, "name").data;
//                GlobalApp.userInfoData().setProfileName(name);
//                AlertUtil.toast(getActivity(), R.string.profile_update);
//            }
//
//            @Override
//            public void onError(NetworkErrorDto error) {
//                AlertUtil.alert(getActivity(), error.getMessage());
//            }
//        });
//    }
//
//    /**
//     * 이미지 링크 변경
//     *
//     * @param imageKey 이미지 링크
//     */
//    private void performCCS00540ToUpdatePhoto(final String imageKey) {
//        NetworkProvider.getInstance1().performCCS00540("2", "", imageKey, new NetworkProvider.ListenerChild() {
//            @Override
//            public void onResponse(NetworkResultDto object) {
//                GlobalApp.userInfoData().setProfileImageKey(imageKey);
//                if (TextUtils.isEmpty(mNickName)) {
//                    AlertUtil.toast(getActivity(), R.string.profile_update);
//                } else {
//                    performCCS00540ToUpdateName(mNickName);
//                }
//            }
//
//            @Override
//            public void onError(NetworkErrorDto error) {
//                AlertUtil.alert(getActivity(), error.getMessage());
//                performCCS00540ToUpdateName(mNickName);
//            }
//        });
//    }
//
    /**
     * 프로파일 이미지 업로드
     */
    private void uploadProfileImage(final boolean isThumbnail) {
        File file = new File(isThumbnail ? mSelectImageFileSmall : mSelectImageFile);

        final String userId = App.userInfoData().getUserId();
        String encSecureKey = StringUtil.getProfileImageSecureKey(userId);

        RequestBody secureKey = RequestBody.create(MediaType.parse("text/plain"), encSecureKey);
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody gbn = RequestBody.create(MediaType.parse("text/plain"), isThumbnail ? "S" : "B");
        MultipartBody.Part body = MultipartBody.Part.createFormData("up_file", file.getName(),
                RequestBody.create(MediaType.parse("image/*"), file));

        ProfileApiService service = (ProfileApiService) RetrofitApiServiceBuilder.create(eAPICategory.PROFILE).build();
        Call<ResponseBody> call = service.imageUpload(secureKey, id, gbn, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (isThumbnail) {
                        //performCCS00540ToUpdatePhoto(userId);
                    } else {
                        uploadProfileImage(true);
                    }
                } else {
                    //AlertUtil.toast(getActivity(), R.string.profile_image_upload_fail);
                    //performCCS00540ToUpdateName(mNickName);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //AlertUtil.toast(getActivity(), R.string.profile_image_upload_fail);
               // performCCS00540ToUpdateName(mNickName);
            }
        });
    }
//
//    /**
//     * 프로필 변경
//     *
//     * @param name      닉네임
//     * @param imagefile 변경 이미지 파일 경로
//     */
//    public void updateProfile(String name, String imagefile) {
//        if (!name.equals(App.userInfoData().getProfileName())) {
//            mNickName = name;
//        }
//        if (mIsUpdateDefaultImage) {
//            // 기본 이미지로 변경
//            performCCS00540ToUpdatePhoto("");
//        } else {
//            if (TextUtils.isEmpty(imagefile)) {
//                // 프로필명 변경
//                performCCS00540ToUpdateName(mNickName);
//            } else {
//                // 이미지 변경
//                uploadProfileImage(false);
//            }
//        }
//    }
}
