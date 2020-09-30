package com.example.mvvmappapplication.ui.menu;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mvvmappapplication.ui.MainActivity;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;


/**
 * Created by BRB_LAB on 2016-06-07.
 * Modified by JS on 2019-04-03.
 */
public class Preview extends Thread {
    private final static String TAG = "Preview : ";

    private Size mPreviewSize;
    private Context mContext;
    private CameraDevice mCameraDevice;
    private CaptureRequest.Builder mPreviewBuilder;
    private CameraCaptureSession mPreviewSession;
    private TextureView mTextureView;
    private String mCameraId = "0";
    private Button mNormalAngleButton;
    private Button mWideAngleButton;
    private Button mCameraCaptureButton;
    private Button mCameraDirectionButton;
    private CameraViewActivity cameraViewActivity;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray(4);

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    public Preview(Context context, TextureView textureView, Button button1, Button button2, Button button3, Button button4) {
        mContext = context;
        mTextureView = textureView;
        cameraViewActivity = (CameraViewActivity) context;
        mNormalAngleButton = button1;
        mWideAngleButton = button2;
        mCameraCaptureButton = button3;
        mCameraDirectionButton = button4;

        mNormalAngleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onPause();
                mCameraId = "0";
                openCamera();
            }
        });

        mWideAngleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onPause();
                mCameraId = "2";
                openCamera();
            }
        });

        mCameraCaptureButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        mCameraDirectionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onPause();
                if (mCameraId.equals("1")) {
                    mCameraId = "0";
                } else {
                    mCameraId = "1";
                }
                openCamera();
            }
        });
    }

    private String getBackFacingCameraId(CameraManager cManager) {
        try {
            for (final String cameraId : cManager.getCameraIdList()) {
                CameraCharacteristics characteristics = cManager.getCameraCharacteristics(cameraId);
                int cOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (cOrientation == CameraCharacteristics.LENS_FACING_BACK) return cameraId;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void openCamera() {
        CameraManager manager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "openCamera E");
        try {
            String cameraId = getBackFacingCameraId(manager);
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            mPreviewSize = map.getOutputSizes(SurfaceTexture.class)[0];

            int permissionCamera = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
            if (permissionCamera == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CAMERA}, MainActivity.REQUEST_CAMERA);
            } else {
                manager.openCamera(mCameraId, mStateCallback, null);
            }
        } catch (CameraAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.e(TAG, "openCamera X");
    }

    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            
            Log.e(TAG, "onSurfaceTextureAvailable, width=" + width + ", height=" + height);
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface,
                                                int width, int height) {
            
            Log.e(TAG, "onSurfaceTextureSizeChanged");
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            
        }
    };

    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(CameraDevice camera) {
            
            Log.e(TAG, "onOpened");
            mCameraDevice = camera;
            startPreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            
            Log.e(TAG, "onDisconnected");
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            Log.e(TAG, "onError");
        }

    };

    protected void startPreview() {
        if (null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize) {
            Log.e(TAG, "startPreview fail, return");
        }

        SurfaceTexture texture = mTextureView.getSurfaceTexture();
        if (null == texture) {
            Log.e(TAG, "texture is null, return");
            return;
        }

        texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        Surface surface = new Surface(texture);

        try {
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            Log.e(TAG, "startPreview createCaptureRequest");
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        mPreviewBuilder.addTarget(surface);

        try {
            mCameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    Log.e(TAG, "createCaptureSession onConfigured");
                    mPreviewSession = session;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                    Toast.makeText(mContext, "onConfigureFailed", Toast.LENGTH_LONG).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    protected void updatePreview() {
        
        if (null == mCameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }

        mPreviewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        HandlerThread thread = new HandlerThread("CameraPreview");
        thread.start();
        Handler backgroundHandler = new Handler(thread.getLooper());

        try {
            mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, backgroundHandler);
            Log.e(TAG, "updatePreview setRepeatingRequest");
        } catch (CameraAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Runnable mDelayPreviewRunnable = new Runnable() {
        @Override
        public void run() {
            startPreview();
        }
    };

    protected void takePicture() {
        if (null == mCameraDevice) {
            Log.e(TAG, "mCameraDevice is null, return");
            return;
        }

        try {
            Size[] jpegSizes = null;
            CameraManager cameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(mCameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            if (map != null) {
                jpegSizes = map.getOutputSizes(ImageFormat.JPEG);
//                Log.d("TEST", "map != null " + jpegSizes.length);
            }
            int width = 640;
            int height = 480;
            if (jpegSizes != null && 0 < jpegSizes.length) {
//                for (int i = 0 ; i < jpegSizes.length; i++) {
//                    Log.d("TEST", "getHeight = " + jpegSizes[i].getHeight() + ", getWidth = " + jpegSizes[i].getWidth());
//                }
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }

            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(mTextureView.getSurfaceTexture()));

            final CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
//            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            captureBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);

            // Orientation
            int rotation = ((Activity) mContext).getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");

            final File file = new File(Environment.getExternalStorageDirectory() + "/DCIM", "pic_" + dateFormat.format(date) + ".jpg");

            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        Log.d(TAG, "takePicture");

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;

                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                        bitmap = GetRotatedBitmap(bitmap, 90);

                        Bitmap imgRoi;
                        OpenCVLoader.initDebug(); // 초기화

                        Mat matBase=new Mat();
                        Utils.bitmapToMat(bitmap ,matBase);
                        Mat matGray = new Mat();
                        Mat matCny = new Mat();

                        Imgproc.cvtColor(matBase, matGray, Imgproc.COLOR_BGR2GRAY); // GrayScale
                        Imgproc.Canny(matGray, matCny, 10, 100, 3, true); // Canny Edge 검출
                        Imgproc.threshold(matGray, matCny, 150, 255, Imgproc.THRESH_BINARY); //Binary

                        List<MatOfPoint> contours = new ArrayList<>();
                        Mat hierarchy = new Mat();
                        //노이즈제거
                        Imgproc.erode(matCny, matCny, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new org.opencv.core.Size(6, 6)));
                        Imgproc.dilate(matCny, matCny, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new org.opencv.core.Size(12, 12)));
                        //관심영역 추출
                        Imgproc.findContours(matCny, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);//RETR_EXTERNAL //RETR_TREE
                        Imgproc.drawContours(matBase, contours, -1, new Scalar(255, 0, 0), 5);

                        imgBase= Bitmap.createBitmap(matBase.cols(), matBase.rows(), Bitmap.Config.ARGB_8888); // 비트맵 생성
                        Utils.matToBitmap(matBase, imgBase); // Mat을 비트맵으로 변환
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
//                                runOnUiThread(new Runnable(){
//                                    @Override
//                                    public void run() {
//                                        imageView.setImageBitmap(imgBase);
//                                    }
//                                });
                                Log.e("dd","imageView에 셋팅");
                            }
                        }).start();
                        imgRoi= Bitmap.createBitmap(matCny.cols(), matCny.rows(), Bitmap.Config.ARGB_8888); // 비트맵 생성
                        /**
                         * 관심영역 생성
                         */
                        Utils.matToBitmap(matCny, imgRoi);

                        Intent intent = new Intent();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        roi.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        intent.putExtra("image", byteArray);
                        cameraViewActivity.setResult(100,intent);
                        cameraViewActivity.finish();
                        for(int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0]) {
                            MatOfPoint matOfPoint = contours.get(idx);
                            Rect rect = Imgproc.boundingRect(matOfPoint);

                            if (rect.width < 30 || rect.height < 30 || rect.width <= rect.height || rect.width <= rect.height * 3 || rect.width >= rect.height * 6)
                                continue; // 사각형 크기에 따라 출력 여부 결정
                            roi = Bitmap.createBitmap( imgRoi, (int)rect.tl().x, (int)rect.tl().y, rect.width, rect.height);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
//                                    runOnUiThread(new Runnable(){
//                                        @Override
//                                        public void run() {
//                                            imageResult.setImageBitmap(roi);
//                                            new AsyncTess().execute(roi);
//                                            btnTakePicture.setEnabled(false);
//                                            btnTakePicture.setText("텍스트 인식중...");
//                                        }
//                                    });
                                    Log.e("dd","텍스트 인식중..");
                                }
                            }).start();
                            break;
                        }
                    }  catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (image != null) {
                            image.close();
                            reader.close();
                        }
                    }
                }

                private void save(byte[] bytes) throws IOException {
                    OutputStream output = null;
                    try {
                        /**
                         * **수정**
                         * 이미지 촬영 후, 저장공간에 저장하는코드 : 권한이 필요함
                         */
//                        output = new FileOutputStream(file);
//                        output.write(bytes);
                    } finally {
                        if (null != output) {
                            output.close();
                        }
                    }
                }
            };

            HandlerThread thread = new HandlerThread("CameraPicture");
            thread.start();
            final Handler backgroundHandler = new Handler(thread.getLooper());
            reader.setOnImageAvailableListener(readerListener, backgroundHandler);

            final Handler delayPreview = new Handler();

            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session,
                                               CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Log.e(TAG, "CaptureCallback onCaptureCompleted");
                    Toast.makeText(mContext, "Saved:" + file, Toast.LENGTH_SHORT).show();
                    delayPreview.postDelayed(mDelayPreviewRunnable, 1000);
                    //startPreview();
                }

            };

            mCameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        Log.e(TAG, "createCaptureSession onConfigured");
                        session.capture(captureBuilder.build(), captureListener, backgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {

                }
            }, backgroundHandler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    Bitmap imgBase;
    Bitmap roi;
    public synchronized static Bitmap GetRotatedBitmap(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != b2) {
                    bitmap = b2;
                }
            } catch (OutOfMemoryError ex) {
                ex.printStackTrace();
            }
        }
        return bitmap;
    }


    public void setSurfaceTextureListener() {
        mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
    }

    public void onResume() {
        Log.d(TAG, "onResume");
        setSurfaceTextureListener();
    }

    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    public void onPause() {
        
        Log.d(TAG, "onPause");
        try {
            mCameraOpenCloseLock.acquire();
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
                Log.d(TAG, "CameraDevice Close");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.");
        } finally {
            mCameraOpenCloseLock.release();
        }
    }
}