package com.example.mvvmappapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;


import com.example.mvvmappapplication.App;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by Freeman-Mac on 2016. 9. 6..
 */
public class BitmapUtil {
    public static Bitmap creBitmapResId(int resId){
        return BitmapFactory.decodeResource(App.getContext().getResources(), resId);
    }
    // View를 Bitmap으로 변환
    public static Bitmap createDrawableFromView(Context context, View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

//    public static File downloadFromUrl(Context context, String imageURL) {
//        try {
//            URL url = new URL(imageURL);
//            File file = getTempFile(context, imageURL);
//
//			/* Open a connection to that URL. */
//            URLConnection ucon = url.openConnection();
//            InputStream is = ucon.getInputStream();
//            BufferedInputStream bis = new BufferedInputStream(is);
//
//            ByteArrayBuffer baf = new ByteArrayBuffer(1024);
//            int current = 0;
//            while ((current = bis.read()) != -1) {
//                baf.append((byte) current);
//            }
//
//            if (file != null) {
//                FileOutputStream fos = new FileOutputStream(file);
//                fos.write(baf.toByteArray());
//                fos.close();
//            }
//
//            return file;
//        } catch (IOException e) {
//            LogUtil.e("ImageManager", "Error: " + e);
//        }
//
//        return null;
//    }

    public static File getTempFile(Context ctx, String url) {
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            File file = File.createTempFile(fileName, null, ctx.getCacheDir());

            return file;
        } catch (Exception e) {}
        return null;
    }

    /**
     * 파일 uri 비트맵 byte[] 변환
     * @param uri 파일 uri
     * @return
     */
    public static byte[] createBitmapByte(String uri) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Bitmap bitmap = decodeFile(uri);
            // 사진 용량 줄이기
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, baos);
            if (Build.VERSION.SDK_INT > 10) {
                bitmap.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    /**
     * 파일 uri 비트맵  변환
     * @param uri 파일 uri
     * @return 리사이즈 bitmap
     */
    public static Bitmap decodeFile(String uri) {
        // 비트 맵 사이즈 체크 후 리사이즈
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uri, options);
        options.inSampleSize = getReductInSampleSize(options.outWidth, options.outHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(uri, options);

        try {
            ExifInterface exif = new ExifInterface(uri);
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifDegree = exifOrientationToDegrees(exifOrientation);
            bitmap = rotate(bitmap, exifDegree);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  bitmap;
    }

    /**
     * bitmap base64 변환
     * @param bitmap
     * @return bitmap base64 string
     */
    public static String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
            }
        }
    }


    /**
     * @title : 비트맵 이미지 리사이즈
     * @content :
     * @param width
     * @param height
     * @return
     */
    public static int getReductInSampleSize(int width, int height) {
        int ret = 1;

        if (width >= 3550 || height >= 3550) {
            ret = 6;
        } else if (width >= 2592 && width < 3550 || height >= 2592 && height < 3550) {
            ret = 4;
        } else if (width >= 640 && width < 1280 || height >= 640 && height < 1280) {
            ret = 2;
        }

        return ret;
    }

    /**
     * @title : EXIF정보를 회전각도로 변환하는 메서드
     * @description :
     * @param exifOrientation exifOrientation EXIF 회전각
     * @return 실제각도
     */
    private static int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    /**
     * @title : 이미지를 회전시킵니다.
     * @author : Dragon
     * @date : 2013. 11. 20. 오후 5:15:47
     * @description :
     * @param bitmap bitmap 비트맵 이미지
     * @param degrees degrees 회전 각도
     * @return 회전된 이미지
     */
    private static Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != converted) {
                    if (Build.VERSION.SDK_INT > 10) {
                        bitmap.recycle();
                    }
                    bitmap = converted;
                }
            } catch (OutOfMemoryError ex) {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap;
    }

    public static String saveBitmapToJpeg(Context context, Bitmap bitmap, String name){
        File storage = context.getFilesDir();
        File tempFile = new File(storage,name);

        try{
            tempFile.createNewFile();  // 파일을 생성해주고
            FileOutputStream out = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90 , out);
            out.close(); // 마무리로 닫아줍니다.
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tempFile.getAbsolutePath();
    }
}
