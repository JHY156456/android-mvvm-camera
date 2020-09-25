package com.example.mvvmappapplication.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.fragment.app.Fragment;

import com.example.mvvmappapplication.App;
import com.example.mvvmappapplication.R;

import java.util.ArrayList;

/**
 * Created by jaehyun on 2017. 5. 19..
 */

public class PermissionUtil {
    public final static int REQUEST_ESSENTIAL_PERMISSION = 100; // 필수 접근 권한
    public final static int REQUEST_CAMERA_PERMISSION = 101;
    public final static int REQUEST_LOCATION_PERMISSION = 102;
    public final static int REQUEST_PERMISSION_ALL = 200; // step 모든 접근 권한

    private static String[] ESSENTIAL_PERMISSION = {    // 필수 접근 권한
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_CONTACTS
    };

    private static String[] CAMERA_PERMISSION = {    // 카메라 접근 권한
            Manifest.permission.CAMERA
    };

    private static String[] LOCATION_PERMISSION = {    // 위치정보 접근 권한
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    /**
     * 위치 권한 요청 안내 팝업 및
     * 사용자가 확인 클릭 후 위치 권한 요청
     * @param activity activity
     */
    public static void requestPermissionLocation(Activity activity) {
        int requestCode = PermissionUtil.REQUEST_LOCATION_PERMISSION;
        int msg = R.string.permission_request_location_message;
        PermissionUtil.requestDialog(activity, msg,requestCode);
    }

    /**
     * 위치 권한 요청 안내 팝업 및
     * 사용자가 확인 클릭 후 위치 권한 요청(Fragment)
     * @param object object
     */
    public static void requestPermissionLocationFragment(Object object) {
        int requestCode = PermissionUtil.REQUEST_LOCATION_PERMISSION;
        int msg = R.string.permission_request_location_message;
        PermissionUtil.requestDialog(object, msg,requestCode);
    }

    /**
     * 카메라 권한 요청 안내 팝업 및
     * 사용자가 확인 클릭 후 카메라 권한 요청
     * @param object activity or fragment
     */
    public static void requestPermissionCamera(Object object) {
        int requestCode = PermissionUtil.REQUEST_CAMERA_PERMISSION;
        int msg = R.string.permission_request_camera_message;
        PermissionUtil.requestDialog(object, msg, requestCode);
    }

    /**
     * 안드로이드 퍼미션 요청 팝업
     * @param activity activity
     * @param permission 요청 퍼미션
     * @param requestCode 퍼미션 코드
     */
    private static void requestPermission(Activity activity , String[] permission , int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions( permission, requestCode);
        }
    }

    /**
     * 접근 권한 체크
     * @param activity activity
     * @param requestCode 퍼미션 코드
     * @return 유저 동의 필요: true, 불필요: false
     */
    @SuppressLint("NewApi")
    public static boolean checkRequestPermission(final Activity activity, final int requestCode) {
        // build target 23부터 적용.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //6.0 미만 사용자 권한 확인 체크
            boolean user_per_check = PreferenceFileUtil.getIntroUserPermissionCheck();
            if(!user_per_check) {
                String message = activity.getString(R.string.intro_permission_title);
                String message1 = activity.getString(R.string.intro_permission_notice);
                String message2 = activity.getString(R.string.intro_permission_choice);
                AlertUtil.alertPermissionPopup(activity, message + message1 + message2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PreferenceFileUtil.setIntroUserPermissionCheck(true);
                    }
                });
            }
            return true;
        }

        ArrayList<String> deniedList = new ArrayList<>(); // 미동의 권한 리스트
        deniedList.clear();

        String[] permissions ;
        permissions =  getRequestPermissionStr(requestCode);

        for (String permission : permissions) {
            if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                deniedList.add(permission);
            }
        }

        if (deniedList.size() > 0) {
            final String[] deniedPermissions = deniedList.toArray(new String[deniedList.size()]);

            if (requestCode == REQUEST_ESSENTIAL_PERMISSION) { // 인트로에서 필수 권한 동의 전 안내팝업 띄운다.
                showEssentialInfoDialog(activity, deniedPermissions, requestCode);
            }
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * 인트로 필수 권한 안내 팝업
     * @param activity activity
     * @param deniedPermissions 접근 거부된 퍼미션
     * @param requestCode 퍼미션 코드
     */
    private static void showEssentialInfoDialog(final Activity activity, final String[] deniedPermissions, final int requestCode) {

        String message = activity.getString(R.string.intro_permission_title_2);
        String message2 = activity.getString(R.string.intro_permission_notice);
        AlertUtil.confirmPermissionPopup(activity, message + message2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        activity.requestPermissions(deniedPermissions, requestCode);
                    }
                } else {
                    //IntentUtil.finishApp(activity);
                }
            }
        }, "확인", "앱종료");
    }


    /**
     * 미동의 안내 팝업
     * 앱 종료 또는 팝업 닫기
     * @param activity activity
     * @param deniedTitleRes 미동의 타이틀 res
     * @param activityFinish  activity 종료여부
     */
    public static void showDeniedDialog(final Activity activity , int deniedTitleRes , final boolean activityFinish) {
        String message = activity.getString(deniedTitleRes)
                + activity.getString(R.string.permission_setting_message);
        showDeniedDialog(activity ,message ,activityFinish);
    }

    /**
     * 미동의 안내 팝업
     * 앱 종료 또는 팝업 닫기
     * @param activity activity
     * @param activityFinish  activity 종료여부
     */
    public static void showDeniedDialog(final Activity activity , final boolean activityFinish) {
        String message = activity.getString(R.string.intro_permission_denied_title)
                + activity.getString(R.string.permission_setting_message);

        showDeniedDialog(activity ,message ,activityFinish);
    }

    /**
     * 미동의 안내 팝업
     * 앱 종료 또는 팝업 닫기
     * @param activity activity
     * @param message message
     * @param activityFinish  activity 종료여부
     */
    private static void showDeniedDialog(final Activity activity, String message, final boolean activityFinish) {

        String negativeStr = "취소";
        if (activityFinish) {
            negativeStr = "종료";
        }

        AlertUtil.confirmPermissionPopup(activity, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    startPermissionSetting(activity);
                }
                if (activityFinish) {
                    //IntentUtil.finishApp(activity);
                }
            }
        }, "설정 바로가기", negativeStr);
    }

    /**
     * 사용자 선택적 권한 안내 요청 팝업
     * @param object activity or fragment
     * @param requestMsg 해당 퍼미션 권한 요청 msg
     * @param requestCode 해당 퍼미션 권한 요청 requestCode
     */
    private static void requestDialog(final Object object, int requestMsg, final int requestCode) {

        Activity activity;
        if (object instanceof Activity) {
            activity = (Activity) object;
        } else if (object instanceof Fragment) {
            activity = ((Fragment) object).getActivity();
        } else {
            return;
        }

        String message = ResourceUtil.getString(R.string.permission_request_title) + ResourceUtil.getString(requestMsg);
        AlertUtil.confirmPermissionPopup(activity, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    String[] requestPermissions = getRequestPermissionStr(requestCode);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        //권한요청
                        if (object instanceof Activity) {
                            ((Activity) object).requestPermissions(requestPermissions, requestCode);
                        } else {
                            ((Fragment) object).requestPermissions(requestPermissions, requestCode);
                        }
                    }
                }
            }
        }, "확인", "취소");
    }

    /**
     * 권한 허용 여부 체크
     * @param permissions 요청 퍼미션
     * @param grantResults 권한 값
     * @return
     */
    public static boolean isGrantedAll(String[] permissions, int[] grantResults) {
        boolean isGranted = true;
        for(int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
                break;
            }
        }
        return isGranted;
    }


    /**
     * 피미션 requestCode 코드에 대한 문자열 반환
     * @param requestCode 퍼미션 code
     * @return 퍼미션 문자열
     */
    private static String[] getRequestPermissionStr(int requestCode) {
        String[] permissions = null;
        if (requestCode == REQUEST_ESSENTIAL_PERMISSION) {
            permissions = ESSENTIAL_PERMISSION;
        } else if (requestCode == REQUEST_CAMERA_PERMISSION) {
            permissions = CAMERA_PERMISSION;
        } else if(requestCode == REQUEST_LOCATION_PERMISSION){
            permissions = LOCATION_PERMISSION;
        }else if(requestCode == REQUEST_PERMISSION_ALL) {
            String[] STEPS_PERMISSION = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.GET_ACCOUNTS,
                    Manifest.permission.READ_CONTACTS
            };
            permissions = STEPS_PERMISSION;
        }
        return permissions;
    }

    /**
     * 권한 허용 여부 체크
     * @param activity activity
     * @param requestCode 퍼미션 requestCode
     * @return 퍼미션 설정 값
     */
    public static boolean isGranted(final Activity activity , int requestCode ){
        return  PermissionUtil.checkRequestPermission(activity, requestCode);
    }

    /**
     * 권한 허용 여부 체크
     * @param activity activity
     * @param requestCode 퍼미션 requestCode
     * @return 퍼미션 허용 거부 여부
     */
    @SuppressLint("NewApi")
    public static int isDeniedSize(final Activity activity , int requestCode ){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return 0;
        }
        ArrayList<String> deniedList = new ArrayList<>(); // 미동의 권한 리스트
        String[] permissions ;
        permissions =  getRequestPermissionStr(requestCode);
        for (String permission : permissions) {
            if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                deniedList.add(permission);
            }
        }
        return deniedList.size();
    }

    /**
     * 앱 권한 설정 이동
     * @param activity activity
     */
    private static void startPermissionSetting(Activity activity) {
        // 단말 설정화면으로 이동
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", App.getContext().getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, 0);
    }

}
