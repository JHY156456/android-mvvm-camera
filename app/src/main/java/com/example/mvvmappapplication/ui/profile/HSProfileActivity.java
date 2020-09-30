package com.example.mvvmappapplication.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.mvvmappapplication.App;
import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.consts.Const;
import com.example.mvvmappapplication.consts.MainEvent;
import com.example.mvvmappapplication.databinding.ActivityProfileBinding;
import com.example.mvvmappapplication.dialog.HSBottomSheetListDialog;
import com.example.mvvmappapplication.dto.SelectDialogDto;
import com.example.mvvmappapplication.ui.BaseDataBindingActivity;
import com.example.mvvmappapplication.utils.AlertUtil;
import com.example.mvvmappapplication.utils.LogUtil;
import com.example.mvvmappapplication.utils.PermissionUtil;
import com.example.mvvmappapplication.utils.ResourceUtil;
import com.example.mvvmappapplication.utils.inputfilter.EmojiInputFilter;

import java.io.File;
import java.io.IOException;


/**
 * 프로필 관리 화면
 *
 * @author ehjung
 */
public class HSProfileActivity extends BaseDataBindingActivity<ActivityProfileBinding, ProfileViewModel> {
    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private static final int REQUEST_CODE_PICK_CAMERA = 2;

    /********************************************************************************************************
     * Intent Factory
     *********************************************************************************************************/
    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, HSProfileActivity.class);
        return intent;
    }

    /********************************************************************************************************
     *
     *********************************************************************************************************/

    private Uri mPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_profile;
    }

    @Override
    public ProfileViewModel newViewModel() {
        return ViewModelProviders.of(this).get(ProfileViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRefreshScreen() {

    }

    private void initLayout() {
        getBinding().nickNameET.addTextChangedListener(mTextWatcher);
        getBinding().nickNameET.setOnEditorActionListener(mOnEditorActionListener);
    }

    private void initData() {
        //getViewModel().performCCS00540ToInquiry();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        getBinding().nickNameET.removeTextChangedListener(mTextWatcher);
        getBinding().nickNameET.setOnEditorActionListener(null);
        super.onDestroy();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PermissionUtil.REQUEST_CAMERA_PERMISSION) {
            if (PermissionUtil.isGrantedAll(permissions, grantResults)) {
                showCamera();
            } else {
                PermissionUtil.showDeniedDialog(this, false);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PICK_IMAGE:
                if (resultCode != RESULT_OK) return;
                Uri selectedImageUri = data.getData();
                //updateUIProfileImage(selectedImageUri);
                break;

            case REQUEST_CODE_PICK_CAMERA:
                if (resultCode != RESULT_OK) return;
                //updateUIProfileImage(mPhotoUri);
                break;
        }
    }

    /********************************************************************************************************
     * UI Update
     *********************************************************************************************************/

    /**
     * 이미지 또는 프로필명 변경시에만 버튼 활성화
     */
    private void setEnableConfirmBtn() {
        String text = getBinding().nickNameET.getText().toString();
        boolean isChange = !text.equals(App.userInfoData().getProfileName())
                || getViewModel().mIsUpdateDefaultImage
                || !TextUtils.isEmpty(getViewModel().mSelectImageFile);
        getBinding().confirmBT.setEnabled(isChange);
    }

    private void showImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "이미지 선택"), REQUEST_CODE_PICK_IMAGE);
    }

    private void showCamera() {
        if (!PermissionUtil.isGranted(getActivity(), PermissionUtil.REQUEST_CAMERA_PERMISSION)) {
            PermissionUtil.requestPermissionCamera(getActivity());
        } else {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        LogUtil.e(ex.getMessage());
                    }

                    if (photoFile != null) {
                        mPhotoUri = FileProvider.getUriForFile(this,
                                getPackageName(),
                                photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                        startActivityForResult(intent, REQUEST_CODE_PICK_CAMERA);
                    }

                }
            }
        }
    }


//    private void updateUIProfileImage(Uri imageUri) {
//        Glide.with(getActivity())
//                .asBitmap()
//                .load(imageUri)
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        Bitmap workingBitmap = null;
//                        if (resource.getWidth() >= resource.getHeight()) {
//                            workingBitmap = Bitmap.createBitmap(
//                                    resource,
//                                    resource.getWidth() / 2 - resource.getHeight() / 2,
//                                    0,
//                                    resource.getHeight(),
//                                    resource.getHeight()
//                            );
//
//                        } else {
//                            workingBitmap = Bitmap.createBitmap(
//                                    resource,
//                                    0,
//                                    resource.getHeight() / 2 - resource.getWidth() / 2,
//                                    resource.getWidth(),
//                                    resource.getWidth()
//                            );
//                        }
//
//                        int size = 170 * 3;//xxhdpi 기준 170dp 사이즈. 고해상도 기준으로 올린다.
//                        if (workingBitmap.getHeight() > size) {
//                            Bitmap bitmap = Bitmap.createScaledBitmap(workingBitmap, size, size, true);
//                            getViewModel().mSelectImageFile = BitmapUtil.saveBitmapToJpeg(getContext(), bitmap, "steps_profile_b");
//                            bitmap.recycle();
//                        } else {
//                            getViewModel().mSelectImageFile = BitmapUtil.saveBitmapToJpeg(getContext(), workingBitmap, "steps_profile_b");
//                        }
//
//
//                        size = 48 * 2;
//                        if (workingBitmap.getHeight() > size) {
//                            Bitmap bitmap = Bitmap.createScaledBitmap(workingBitmap, size, size, true);
//                            getViewModel().mSelectImageFileSmall = BitmapUtil.saveBitmapToJpeg(getContext(), bitmap, "steps_profile_s");
//                            bitmap.recycle();
//                        } else {
//                            getViewModel().mSelectImageFileSmall = BitmapUtil.saveBitmapToJpeg(getContext(), workingBitmap, "steps_profile_s");
//                        }
//
//                        workingBitmap.recycle();
//
//
////                        Glide.with(getContext()).load(getViewModel().mSelectImageFile)
////                                .apply(new RequestOptions()
////                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
////                                        .skipMemoryCache(true))
////                                .into(getBinding().profileImgIV);
//                        setEnableConfirmBtn();
//                    }
//
//
//                });
//    }

    public class UpdateProfileAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            //getViewModel().updateProfile(params[0], params[1]);
            return null;
        }
    }

    private File createImageFile() throws IOException {
        File dir = new File(Const.EXTERNAL_STORAGE_DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return File.createTempFile("steps_profile", ".jpg", dir);
    }

    /********************************************************************************************************
     * UI Event
     *********************************************************************************************************/
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.profileImgIV: /* 사진선택 */
                String[] items = new String[]{ResourceUtil.getString(R.string.profile_select_camara), ResourceUtil.getString(R.string.profile_select_gallery), ResourceUtil.getString(R.string.profile_select_default)};

                AlertUtil.list(getActivity(), ResourceUtil.getString(R.string.profile_select_title), items, -1, new HSBottomSheetListDialog.ISelectItemListener() {
                    @Override
                    public void onSelectedItem(int position, SelectDialogDto item) {
                        switch (position) {
                            case 0:
                                showCamera();
                                break;
                            case 1:
                                showImagePicker();
                                break;
                            case 2:
                                getViewModel().mIsUpdateDefaultImage = !TextUtils.isEmpty(App.userInfoData().getProfileThumbnailImageUrl());
                                if (getViewModel().mIsUpdateDefaultImage) {
                                    setEnableConfirmBtn();
                                    getBinding().profileImgIV.setImageResourceAttr(R.attr.img_profile_basic);
                                }
                                break;
                        }
                    }
                });
                break;

            case R.id.confirmBT: /* 확인 버튼 */
                String name = getBinding().nickNameET.getText().toString().replaceAll("\n", " ").trim();
                if (TextUtils.isEmpty(name)) {
                    AlertUtil.alert(getActivity(), "이름을 입력해주세요.");
                    return;
                }
                new UpdateProfileAsyncTask().execute(name, getViewModel().mSelectImageFile);
                break;
        }
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        String beforeText;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            beforeText = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String afterText = s.toString();
            if (afterText.length() > 12) {
                getBinding().nickNameET.setText(beforeText);
                getBinding().nickNameET.setSelection(getBinding().nickNameET.getText().length());
            } else {
                getBinding().byteTV.setText(String.valueOf(afterText.length()));
            }
            setEnableConfirmBtn();
        }
    };

    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onClick(getBinding().confirmBT);
                return true;
            }
            return false;
        }
    };

    /********************************************************************************************************
     * EventBus
     *********************************************************************************************************/

    @Override
    public void onEvent(MainEvent event) {
        super.onEvent(event);
        switch (event.mEvent) {
            case MainEvent.EVENT_CHANGE_PROFILE_NAME_ACTION: /* 닉네임 변경 */
                getBinding().nickNameET.setFilters(new InputFilter[] {});

                getBinding().nickNameET.setText(App.userInfoData().getProfileName());
                getBinding().nickNameET.setSelection(getBinding().nickNameET.getText().length());

                getBinding().nickNameET.setFilters(new InputFilter[]{
                        new EmojiInputFilter()
                });

                getViewModel().mNickName = "";
                setEnableConfirmBtn();
                break;

//            case MainEvent.EVENT_CHANGE_PROFILE_IMAGE_ACTION: /* 프로필 이미지 변경 */
//                Glide.with(getContext()).load(GlobalApp.userInfoData().getProfileOrignImageUrl())
//                        .apply(new RequestOptions()
//                                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                                .skipMemoryCache(true)
//                                .error(ResourceUtil.getThemeDrawableResId(R.attr.img_profile_basic)))
//                        .into(getBinding().profileImgIV);
//
//                getViewModel().mSelectImageFile = "";
//                getViewModel().mIsUpdateDefaultImage = false;
//                setEnableConfirmBtn();
//                break;
        }
    }
}
