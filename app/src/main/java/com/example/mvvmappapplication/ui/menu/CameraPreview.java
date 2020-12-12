package com.example.mvvmappapplication.ui.menu;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.mvvmappapplication.custom.AutoFitTextureView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.opencv.core.CvType.CV_8U;

public class CameraPreview extends Thread {
    private final static String TAG = "Preview : ";

    /**
     * Conversion from screen rotation to JPEG orientation.
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final String FRAGMENT_DIALOG = "dialog";

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    /**
     * Camera state: Showing camera preview.
     */
    private static final int STATE_PREVIEW = 0;

    /**
     * Camera state: Waiting for the focus to be locked.
     */
    private static final int STATE_WAITING_LOCK = 1;

    /**
     * Camera state: Waiting for the exposure to be precapture state.
     */
    private static final int STATE_WAITING_PRECAPTURE = 2;

    /**
     * Camera state: Waiting for the exposure state to be something other than precapture.
     */
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;

    /**
     * Camera state: Picture was taken.
     */
    private static final int STATE_PICTURE_TAKEN = 4;

    /**
     * Max preview width that is guaranteed by Camera2 API
     */
    private static final int MAX_PREVIEW_WIDTH = 1920;

    /**
     * Max preview height that is guaranteed by Camera2 API
     */
    private static final int MAX_PREVIEW_HEIGHT = 1080;

    /**
     * {@link TextureView.SurfaceTextureListener} handles several lifecycle events on a
     * {@link TextureView}.
     */
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }

    };

    /**
     * ID of the current {@link CameraDevice}.
     */
    private String mCameraId;

    /**
     * An {@link AutoFitTextureView} for camera preview.
     */
    private AutoFitTextureView mTextureView;

    /**
     * A {@link CameraCaptureSession } for camera preview.
     */
    private CameraCaptureSession mCaptureSession;

    /**
     * A reference to the opened {@link CameraDevice}.
     */
    private CameraDevice mCameraDevice;

    /**
     * The {@link android.util.Size} of camera preview.
     */
    private Size mPreviewSize;

    /**
     * {@link CameraDevice.StateCallback} is called when {@link CameraDevice} changes its state.
     */
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            // This method is called when the camera is opened.  We start camera preview here.
            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            Activity activity = mContext;
            if (null != activity) {
                activity.finish();
            }
        }

    };

    /**
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private HandlerThread mBackgroundThread;

    /**
     * A {@link Handler} for running tasks in the background.
     */
    private Handler mBackgroundHandler;

    /**
     * An {@link ImageReader} that handles still image capture.
     */
    private ImageReader mImageReader;

    /**
     * This is the output file for our picture.
     */
    private File mFile;

    /**
     * This a callback object for the {@link ImageReader}. "onImageAvailable" will be called when a
     * still image is ready to be saved.
     */
    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener
            = new ImageReader.OnImageAvailableListener() {

        @Override
        public void onImageAvailable(ImageReader reader) {
            mBackgroundHandler.post(new ImageSaver(reader.acquireNextImage(), mFile));
        }

    };

    /**
     * {@link CaptureRequest.Builder} for the camera preview
     */
    private CaptureRequest.Builder mPreviewRequestBuilder;

    /**
     * {@link CaptureRequest} generated by {@link #mPreviewRequestBuilder}
     */
    private CaptureRequest mPreviewRequest;

    /**
     * The current state of camera state for taking pictures.
     *
     * @see #mCaptureCallback
     */
    private int mState = STATE_PREVIEW;

    /**
     * A {@link Semaphore} to prevent the app from exiting before closing the camera.
     */
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    /**
     * Whether the current camera device supports Flash or not.
     */
    private boolean mFlashSupported;

    /**
     * Orientation of the camera sensor
     */
    private int mSensorOrientation;

    /**
     * A {@link CameraCaptureSession.CaptureCallback} that handles events related to JPEG capture.
     */
    private CameraCaptureSession.CaptureCallback mCaptureCallback
            = new CameraCaptureSession.CaptureCallback() {

        private void process(CaptureResult result) {
            switch (mState) {
                case STATE_PREVIEW: {
                    // We have nothing to do when the camera preview is working normally.
                    break;
                }
                case STATE_WAITING_LOCK: {
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    if (afState == null) {
                        captureStillPicture();
                    } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState ||
                            CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
                        // CONTROL_AE_STATE can be null on some devices
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (aeState == null ||
                                aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            mState = STATE_PICTURE_TAKEN;
                            captureStillPicture();
                        } else {
                            runPrecaptureSequence();
                        }
                    }
                    break;
                }
                case STATE_WAITING_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null ||
                            aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        mState = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                }
                case STATE_WAITING_NON_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        mState = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
                }
            }
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                        @NonNull CaptureRequest request,
                                        @NonNull CaptureResult partialResult) {
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
            process(result);
        }

    };

    /**
     * Shows a {@link Toast} on the UI thread.
     *
     * @param text The message to show
     */
    private void showToast(final String text) {
        final Activity activity = mContext;
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Given {@code choices} of {@code Size}s supported by a camera, choose the smallest one that
     * is at least as large as the respective texture view size, and that is at most as large as the
     * respective max size, and whose aspect ratio matches with the specified value. If such size
     * doesn't exist, choose the largest one that is at most as large as the respective max size,
     * and whose aspect ratio matches with the specified value.
     *
     * @param choices           The list of sizes that the camera supports for the intended output
     *                          class
     * @param textureViewWidth  The width of the texture view relative to sensor coordinate
     * @param textureViewHeight The height of the texture view relative to sensor coordinate
     * @param maxWidth          The maximum width that can be chosen
     * @param maxHeight         The maximum height that can be chosen
     * @param aspectRatio       The aspect ratio
     * @return The optimal {@code Size}, or an arbitrary one if none were big enough
     */
    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                                          int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    private Button mNormalAngleButton;
    private Button mFlash;
    private Button mCameraCaptureButton;
    private Button mCameraDirectionButton;
    private CameraViewActivity cameraViewActivity;
    private static CameraViewActivity mContext;
    private boolean mFlashOn = false;


    public CameraPreview(Context context, AutoFitTextureView textureView, Button button1,
                         Button button2, Button button3, Button button4) {
        mContext = (CameraViewActivity) context;
        mTextureView = textureView;
        cameraViewActivity = (CameraViewActivity) context;
        mNormalAngleButton = button1;
        mFlash = button2;
        mCameraCaptureButton = button3;
        mCameraDirectionButton = button4;
        mFile = new File(mContext.getExternalFilesDir(null), "pic.jpg");

        mNormalAngleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onPause();
                mCameraId = "0";
                openCamera(mTextureView.getWidth(), mTextureView.getHeight());
            }
        });

        mFlash.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                onPause();
//                mCameraId = "2";
//                openCamera(mTextureView.getWidth(), mTextureView.getHeight());
                flashlight();
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
                openCamera(mTextureView.getWidth(), mTextureView.getHeight());
            }
        });

    }

    private void flashlight() {

        mFlashOn = !mFlashOn;
        if (mFlashOn) {
            flashOn();
        } else {
            flashOff();
        }
    }

    // Flash On
    public void flashOn() {
        try {
            mPreviewRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
            mCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(), null, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //Flash Off
    public void flashOff() {
        try {
            mPreviewRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_OFF);
            mCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(), null, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        startBackgroundThread();

        // When the screen is turned off and turned back on, the SurfaceTexture is already
        // available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
        // a camera and start preview from here (otherwise, we wait until the surface is ready in
        // the SurfaceTextureListener).
        if (mTextureView.isAvailable()) {
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    public void onPause() {
        closeCamera();
        stopBackgroundThread();
    }

    /**
     * Sets up member variables related to camera.
     *
     * @param width  The width of available size for camera preview
     * @param height The height of available size for camera preview
     */
    @SuppressWarnings("SuspiciousNameCombination")
    private void setUpCameraOutputs(int width, int height) {
        Activity activity = mContext;
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics
                        = manager.getCameraCharacteristics(cameraId);

                // We don't use a front facing camera in this sample.
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                StreamConfigurationMap map = characteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }

                // For still image captures, we use the largest available size.
                Size largest = Collections.max(
                        Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                        new CompareSizesByArea());
                mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(),
                        ImageFormat.JPEG, /*maxImages*/2);
                mImageReader.setOnImageAvailableListener(
                        mOnImageAvailableListener, mBackgroundHandler);

                // Find out if we need to swap dimension to get the preview size relative to sensor
                // coordinate.
                int displayRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
                //noinspection ConstantConditions
                mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                boolean swappedDimensions = false;
                switch (displayRotation) {
                    case Surface.ROTATION_0:
                    case Surface.ROTATION_180:
                        if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                            swappedDimensions = true;
                        }
                        break;
                    case Surface.ROTATION_90:
                    case Surface.ROTATION_270:
                        if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                            swappedDimensions = true;
                        }
                        break;
                    default:
                        Log.e(TAG, "Display rotation is invalid: " + displayRotation);
                }

                Point displaySize = new Point();
                activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
                int rotatedPreviewWidth = width;
                int rotatedPreviewHeight = height;
                int maxPreviewWidth = displaySize.x;
                int maxPreviewHeight = displaySize.y;

                if (swappedDimensions) {
                    rotatedPreviewWidth = height;
                    rotatedPreviewHeight = width;
                    maxPreviewWidth = displaySize.y;
                    maxPreviewHeight = displaySize.x;
                }

                if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                    maxPreviewWidth = MAX_PREVIEW_WIDTH;
                }

                if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                    maxPreviewHeight = MAX_PREVIEW_HEIGHT;
                }

                // Danger, W.R.! Attempting to use too large a preview size could  exceed the camera
                // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
                // garbage capture data.
                mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                        rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                        maxPreviewHeight, largest);

                // We fit the aspect ratio of TextureView to the size of preview we picked.
                int orientation = mContext.getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    mTextureView.setAspectRatio(
                            mPreviewSize.getWidth(), mPreviewSize.getHeight());
                } else {
                    mTextureView.setAspectRatio(
                            mPreviewSize.getHeight(), mPreviewSize.getWidth());
                }

                // Check if the flash is supported.
                Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                mFlashSupported = available == null ? false : available;

                mCameraId = cameraId;
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.
//            ErrorDialog.newInstance(getString(R.string.camera_error))
//                    .show(getChildFragmentManager(), FRAGMENT_DIALOG);
        }
    }


    private void openCamera(int width, int height) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        setUpCameraOutputs(width, height);
        configureTransform(width, height);
        Activity activity = mContext;
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            manager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }

    /**
     * Closes the current {@link CameraDevice}.
     */
    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            if (null != mCaptureSession) {
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mImageReader) {
                mImageReader.close();
                mImageReader = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    /**
     * Starts a background thread and its {@link Handler}.
     */
    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    /**
     * Stops the background thread and its {@link Handler}.
     */
    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new {@link CameraCaptureSession} for camera preview.
     */
    private void createCameraPreviewSession() {
        try {
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;

            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            // This is the output Surface we need to start preview.
            Surface surface = new Surface(texture);

            // We set up a CaptureRequest.Builder with the output Surface.
            mPreviewRequestBuilder
                    = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);

            // Here, we create a CameraCaptureSession for camera preview.
            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            // The camera is already closed
                            if (null == mCameraDevice) {
                                return;
                            }

                            // When the session is ready, we start displaying the preview.
                            mCaptureSession = cameraCaptureSession;
                            try {
                                /**
                                 * Flash 기존방식 시작
                                 */
//                                // Auto focus should be continuous for camera preview.
//                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
//                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
//                                // Flash is automatically enabled when necessary.
//                                setAutoFlash(mPreviewRequestBuilder);

                                /**
                                 * Flash 기존방식 끝
                                 */
                                /**
                                 * Flash 새방식 시작
                                 */
                                mPreviewRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
                                /**
                                 * Flash 새방식 끝
                                 */
                                // Finally, we start displaying the camera preview.
                                mPreviewRequest = mPreviewRequestBuilder.build();
                                mCaptureSession.setRepeatingRequest(mPreviewRequest,
                                        mCaptureCallback, mBackgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(
                                @NonNull CameraCaptureSession cameraCaptureSession) {
                            showToast("Failed");
                        }
                    }, null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Configures the necessary {@link android.graphics.Matrix} transformation to `mTextureView`.
     * This method should be called after the camera preview size is determined in
     * setUpCameraOutputs and also the size of `mTextureView` is fixed.
     *
     * @param viewWidth  The width of `mTextureView`
     * @param viewHeight The height of `mTextureView`
     */
    private void configureTransform(int viewWidth, int viewHeight) {
        Activity activity = mContext;
        if (null == mTextureView || null == mPreviewSize || null == activity) {
            return;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }

    /**
     * Initiate a still image capture.
     */
    private void takePicture() {
        lockFocus();
    }

    /**
     * Lock the focus as the first step for a still image capture.
     */
    private void lockFocus() {
        try {
            // This is how to tell the camera to lock focus.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the lock.
            mState = STATE_WAITING_LOCK;
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Run the precapture sequence for capturing a still image. This method should be called when
     * we get a response in {@link #mCaptureCallback} from {@link #lockFocus()}.
     */
    private void runPrecaptureSequence() {
        try {
            // This is how to tell the camera to trigger.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the precapture sequence to be set.
            mState = STATE_WAITING_PRECAPTURE;
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Capture a still picture. This method should be called when we get a response in
     * {@link #mCaptureCallback} from both {@link #lockFocus()}.
     */
    private void captureStillPicture() {
        try {
            final Activity activity = mContext;
            if (null == activity || null == mCameraDevice) {
                return;
            }
            // This is the CaptureRequest.Builder that we use to take a picture.
            final CaptureRequest.Builder captureBuilder =
                    mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());

            // Use the same AE and AF modes as the preview.
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            setAutoFlash(captureBuilder);

            // Orientation
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

            CameraCaptureSession.CaptureCallback CaptureCallback
                    = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                               @NonNull CaptureRequest request,
                                               @NonNull TotalCaptureResult result) {
                    //showToast("Saved: " + mFile);
                    //Log.d(TAG, mFile.toString());
                    try {
                        unlockFocus();
                    } catch (Exception e) {

                    }
                }
            };

            mCaptureSession.stopRepeating();
            mCaptureSession.abortCaptures();
            mCaptureSession.capture(captureBuilder.build(), CaptureCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the JPEG orientation from the specified screen rotation.
     *
     * @param rotation The screen rotation.
     * @return The JPEG orientation (one of 0, 90, 270, and 360)
     */
    private int getOrientation(int rotation) {
        // Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus 5X)
        // We have to take that into account and rotate JPEG properly.
        // For devices with orientation of 90, we simply return our mapping from ORIENTATIONS.
        // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
        return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
    }

    /**
     * Unlock the focus. This method should be called when still image capture sequence is
     * finished.
     */
    private void unlockFocus() {
        try {
            // Reset the auto-focus trigger
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            setAutoFlash(mPreviewRequestBuilder);
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);
            // After this, the camera will go back to the normal state of preview.
            mState = STATE_PREVIEW;
            mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setAutoFlash(CaptureRequest.Builder requestBuilder) {
        if (mFlashSupported) {
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        }
    }

    /**
     * Saves a JPEG {@link Image} into the specified {@link File}.
     */
    private static class ImageSaver implements Runnable {

        /**
         * The JPEG image
         */
        private final Image mImage;
        /**
         * The file we save the image into.
         */
        private final File mFile;

        ImageSaver(Image image, File file) {
            mImage = image;
            mFile = file;
        }

        //1
        Bitmap imgBase;
        Bitmap roi;

        //2
        Bitmap image1;
        Bitmap img1;

        @Override
        public void run() {
            try {
                ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                FileOutputStream output = null;
                try {
                    output = new FileOutputStream(mFile);
                    output.write(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    mImage.close();
                    if (null != output) {
                        try {
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Intent intent = new Intent();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                /**
                 * OpenCv시작
                 */
//                Bitmap imgRoi;
                OpenCVLoader.initDebug(); // 초기화
////
////                Mat matBase = new Mat();
////                Utils.bitmapToMat(bmp, matBase);
////                Mat matGray = new Mat();
////                Mat matCny = new Mat();
////
////                Imgproc.cvtColor(matBase, matGray, Imgproc.COLOR_BGR2GRAY); // GrayScale
////                Imgproc.Canny(matGray, matCny, 10, 100, 3, true); // Canny Edge 검출
////                Imgproc.threshold(matGray, matCny, 150, 255, Imgproc.THRESH_BINARY); //Binary
////First convert Bitmap to Mat
//                Mat ImageMatin = new Mat(bmp.getHeight(), bmp.getWidth(), CV_8U, new Scalar(4));
//                Mat ImageMatout = new Mat(bmp.getHeight(), bmp.getWidth(), CV_8U, new Scalar(4));
//                Mat ImageMatBk = new Mat(bmp.getHeight(), bmp.getWidth(), CV_8U, new Scalar(4));
//                Mat ImageMatTopHat = new Mat(bmp.getHeight(), bmp.getWidth(), CV_8U, new Scalar(4));
//                Mat temp = new Mat(bmp.getHeight(), bmp.getWidth(), CV_8U, new Scalar(4));
//
//                Bitmap myBitmap32 = bmp.copy(Bitmap.Config.ARGB_8888, true);
//                Utils.bitmapToMat(myBitmap32, ImageMatin);
//
//
////Converting RGB to Gray.
//                Imgproc.cvtColor(ImageMatin, ImageMatBk, Imgproc.COLOR_RGB2GRAY, 8);
//
//                Imgproc.dilate(ImageMatBk, temp, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new org.opencv.core.Size(9, 9)));
//                Imgproc.erode(temp, ImageMatTopHat, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new org.opencv.core.Size(9, 9)));
//
////Core.absdiff(current, previous, difference);
//                Core.absdiff(ImageMatTopHat, ImageMatBk, ImageMatout);
//
////Sobel operator in horizontal direction.
//                Imgproc.Sobel(ImageMatout, ImageMatout, CV_8U, 1, 0, 3, 1, 0.4);
//
////Converting GaussianBlur
//                Imgproc.GaussianBlur(ImageMatout, ImageMatout, new org.opencv.core.Size(5, 5), 2);
//
//                Imgproc.dilate(ImageMatout, ImageMatout, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new org.opencv.core.Size(3, 3)));
//
//                Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new org.opencv.core.Size(17, 3));
//                Imgproc.morphologyEx(ImageMatout, ImageMatout, Imgproc.MORPH_CLOSE, element);
//
////threshold image
//                Imgproc.threshold(ImageMatout, ImageMatout, 0, 255, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY);
//                List<MatOfPoint> contours = new ArrayList<>();
//                ArrayList<RotatedRect> rects = new ArrayList<RotatedRect>();
//
//
//                Mat hierarchy = new Mat();
//                //노이즈제거
//                Imgproc.erode(ImageMatout, ImageMatout, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new org.opencv.core.Size(6, 6)));
//                Imgproc.dilate(ImageMatout, ImageMatout, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new org.opencv.core.Size(12, 12)));
//                //관심영역 추출
//                Imgproc.findContours(ImageMatout, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);//RETR_EXTERNAL //RETR_TREE
//                Imgproc.drawContours(ImageMatin, contours, -1, new Scalar(255, 0, 0), 5);
//
//                //imgBase : 관심영역 빨간색으로 표시됨
//                imgBase = Bitmap.createBitmap(ImageMatin.cols(), ImageMatin.rows(), Bitmap.Config.ARGB_8888); // 비트맵 생성
//                Utils.matToBitmap(ImageMatin, imgBase); // Mat을 비트맵으로 변환
//
//
//                imgRoi = Bitmap.createBitmap(ImageMatout.cols(), ImageMatout.rows(), Bitmap.Config.ARGB_8888); // 비트맵 생성
//                Utils.matToBitmap(ImageMatout, imgRoi);
//
//                for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0]) {
//                    MatOfPoint matOfPoint = contours.get(idx);
//                    Rect rect = Imgproc.boundingRect(matOfPoint);
//
//                    if (rect.width < 30 || rect.height < 30 || rect.width <= rect.height || rect.width <= rect.height * 3 || rect.width >= rect.height * 6)
//                        continue; // 사각형 크기에 따라 출력 여부 결정
//
//                    roi = imgRoi.createBitmap(bmp, (int) rect.tl().x, (int) rect.tl().y, rect.width, rect.height);
//                    break;
//                }

                ArrayList<Mat> resultMat = hello(bmp);
                /**
                 * OpenCv 끝
                 */

                /**
                 * OpenCv적용한 데이터 전달 시작
                 */

//                Mat matBase2 = new Mat();
//                Utils.bitmapToMat(roi, matBase2);
//                Mat matGray2 = new Mat();
//                Mat matCny2 = new Mat();
//
//                Imgproc.cvtColor(matBase2, matGray2, Imgproc.COLOR_BGR2GRAY); // GrayScale
//                Imgproc.Canny(matGray2, matCny2, 10, 100, 3, true); // Canny Edge 검출
//                Imgproc.threshold(matGray2, matCny2, 150, 255, Imgproc.THRESH_BINARY); //Binary
//
//                //노이즈제거
//                Imgproc.erode(matCny2, matCny2, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new org.opencv.core.Size(6, 6)));
//                Imgproc.dilate(matCny2, matCny2, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new org.opencv.core.Size(12, 12)));
//
//                Utils.matToBitmap(matBase, roi); // Mat을 비트맵으로 변환


                // *********************그레이 스케일 적용 *********************
//                Mat gray = new Mat();
//                Utils.bitmapToMat(roi, gray);
//                Imgproc.cvtColor(gray, gray, Imgproc.COLOR_RGBA2GRAY);
//                Bitmap grayBitmap = Bitmap.createBitmap(gray.cols(), gray.rows(), null);
                // *********************그레이 스케일 적용 *********************

                Bitmap resultBitmap = Bitmap.createBitmap(resultMat.get(0).cols(), resultMat.get(0).rows(), null);
                // 윗 부분 오류발생하면 마지막 param null 대신 Bitmap.Config.ARGB_8888
                Utils.matToBitmap(resultMat.get(0), resultBitmap);
                resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                /**
                 * OpenCv적용한 데이터 전달 끝
                 */

                //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                intent.putExtra("image", byteArray);
                intent.putExtra("file", mFile);
                mContext.setResult(Activity.RESULT_OK, intent);
                mContext.finish();

            } catch (Exception e) {
                Toast.makeText(mContext, "인식에 실패했습니다.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } finally {
                if (mImage != null) {
                    mImage.close();
                }
            }
        }
    }

    private static ArrayList<Mat> hello (Bitmap bmp){
        ArrayList<Mat> resultMat = new ArrayList<>();
        Mat originalBmpToMat = new Mat();
        Bitmap bmp32 = bmp.copy(Bitmap.Config.ARGB_8888, true);

        Utils.bitmapToMat(bmp32, originalBmpToMat);

        if(originalBmpToMat.channels() != 1 && originalBmpToMat.channels() != 3) {
            Imgproc.cvtColor(originalBmpToMat,originalBmpToMat,Imgproc.COLOR_BGRA2BGR);
        }

        Mat morph = preProcessing(bmp32);


        RotatedRect[] candidate = find_candidate(morph);
        ArrayList<Mat> fills = new ArrayList<>();

        for(int i=0; i< candidate.length;i++){
            if(candidate[i]!=null){
                org.opencv.core.Point center = candidate[i].center;
                fills.add(color_candidate_img(originalBmpToMat,center));
            }
        }
//        resultMat.add(fills.get(0));
//        return resultMat;
        //*******여기까지 정상 but python은 fills가 1개인데 여기는 3개,4개로나옴..**********

        ArrayList<RotatedRect[]> new_candis = new ArrayList<>();
        for(int i=0; i<fills.size(); i++){
                new_candis.add(find_candidate(fills.get(i)));
        }

        ArrayList<RotatedRect> rotatedRectnew_candis = new ArrayList<>();
        for(int i=0; i<new_candis.size();i++){
            try{
                if(new_candis.get(i)[0]!= null){
                    rotatedRectnew_candis.add(new_candis.get(i)[0]);
                }
            } catch (ArrayIndexOutOfBoundsException e){

            }
        }

        ArrayList<Mat> candidate_img = new ArrayList<>();
        for(int i=0; i<rotatedRectnew_candis.size();i++){
            candidate_img.add(rotate_plate(originalBmpToMat,rotatedRectnew_candis.get(i)));
        }

        for(int i=0; i<candidate_img.size(); i++){
            Mat candidate_mat = candidate_img.get(i);
            MatOfPoint matOfPoint = new MatOfPoint();
            Imgproc.boxPoints(rotatedRectnew_candis.get(i),matOfPoint);
            List<MatOfPoint> matOfPointList = new ArrayList<>();
            matOfPointList.add(matOfPoint);
            Imgproc.polylines(candidate_mat,matOfPointList,true,new Scalar(0,255,255),2);
            resultMat.add(candidate_mat);
        }
        return resultMat;
    }
    private static Mat rotate_plate(Mat originalBmpToMat,RotatedRect rotatedRects){
        //Imgproc.resize();
        org.opencv.core.Point center = rotatedRects.center;
        double w = rotatedRects.size.width;
        double h = rotatedRects.size.height;
        double angle = rotatedRects.angle;
        if(w<h){
            double temp = h;
            h = w;
            w = temp;
        }
        angle += 90;
        org.opencv.core.Size size = originalBmpToMat.size();

        //회전행렬 계산
        Mat rot_mat = Imgproc.getRotationMatrix2D(center,angle,1);
        Mat rot_img =new Mat();
        //전체 원본영상 회전
        Imgproc.warpAffine(originalBmpToMat,rot_img,rot_mat,size,Imgproc.INTER_CUBIC);

        Mat crop_img = new Mat();
        Imgproc.getRectSubPix(rot_img,new org.opencv.core.Size(w,h),center,crop_img);
        Imgproc.cvtColor(crop_img,crop_img,Imgproc.COLOR_BGR2GRAY);

        Mat resize_img = new Mat();
        Imgproc.resize(crop_img,resize_img,new org.opencv.core.Size(144,28));
        return crop_img;
    }

    private static RotatedRect[] find_candidate(Mat originalBmpToMat){
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(originalBmpToMat, contours, hierarchy,Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        RotatedRect[] minRect = new RotatedRect[contours.size()];
        for (int i = 0; i < contours.size(); i++) {
            minRect[i] = Imgproc.minAreaRect(new MatOfPoint2f(contours.get(i).toArray()));
        }

        ArrayList<RotatedRect> candidate = new ArrayList<>();
        for(int i=0; i<minRect.length;i++){
            if(verify_aspect_size(minRect[i].size)){
                candidate.add(minRect[i]);
            }
        }
        RotatedRect[] resultRect = new RotatedRect[candidate.size()];
        for(int i=0; i<candidate.size();i++){
            resultRect[i] = candidate.get(i);
        }
        return resultRect;
    }

    private static boolean verify_aspect_size(org.opencv.core.Size size){
        double w = size.width;
        double h = size.height;
        if (h == 0 || w == 0)return false;

        double aspect = 0.0;
        if (h > w) aspect = h / w; else aspect = w / h;
        boolean chk1 = false;
        if(200<(h*w) && (h*w)<10000) chk1 = true;
        boolean chk2 = false;
        if(2.0 < aspect && aspect < 6.5) chk2 = true;
        return (chk1 && chk2);
    }

    private static Mat color_candidate_img(Mat originalBmpToMat, org.opencv.core.Point center){
        int w = originalBmpToMat.width();
        int h = originalBmpToMat.height();
        Mat fill = Mat.zeros(new org.opencv.core.Size(w+2,h+2),CV_8U);
        Scalar dif1 = new Scalar(25,25,25);
        Scalar dif2 = new Scalar(25,25,25);
        int flags = 0xff00+4+Imgproc.FLOODFILL_FIXED_RANGE;
        flags += Imgproc.FLOODFILL_MASK_ONLY;

        ArrayList<Pair<Integer,Integer>> pts = new ArrayList<>();
        for(int i=0; i<20; i++){
            pts.add(new Pair<Integer, Integer>(
                    new Random().nextInt(31)-15+(int)center.x,
                    new Random().nextInt(31)-15+(int)center.y));
        }
        for(int i=0; i<20; i++){
            int x = pts.get(i).first;
            int y = pts.get(i).second;
            if((0<=x && x<w) && (0<=y && y< h)){
                Imgproc.floodFill(originalBmpToMat,
                        fill,
                        new org.opencv.core.Point(x,y),
                        new Scalar(255,255,255),
                        new Rect(),
                        dif1,dif2,flags);
            }
            Mat valueMat = new Mat();
            Imgproc.threshold(fill,valueMat,120,2500,Imgproc.THRESH_BINARY);
            return valueMat;
        }
        return null;
    }

    private static Mat preProcessing(Bitmap bmp){
        Mat originalBmpToMat = new Mat();
        Mat matGray = new Mat();
        Utils.bitmapToMat(bmp, originalBmpToMat);
        Imgproc.cvtColor(originalBmpToMat,matGray,Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(matGray,matGray,new org.opencv.core.Size(5,5));
        Imgproc.Sobel(matGray,matGray,CV_8U,1,0,3);
        Imgproc.threshold(matGray,matGray,120,255,Imgproc.THRESH_BINARY);
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new org.opencv.core.Size(5, 17));
        Imgproc.morphologyEx(matGray,matGray,Imgproc.MORPH_CLOSE,element);
        return matGray;
    }


    /**
     * Compares two {@code Size}s based on their areas.
     */
    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    /**
     * Shows an error message dialog.
     */
    public static class ErrorDialog extends DialogFragment {

        private static final String ARG_MESSAGE = "message";

        public static ErrorDialog newInstance(String message) {
            ErrorDialog dialog = new ErrorDialog();
            Bundle args = new Bundle();
            args.putString(ARG_MESSAGE, message);
            dialog.setArguments(args);
            return dialog;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Activity activity = mContext;
            return new AlertDialog.Builder(activity)
                    .setMessage(getArguments().getString(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.finish();
                        }
                    })
                    .create();
        }

    }

    /**
     * Shows OK/Cancel confirmation dialog about camera permission.
     */
    public static class ConfirmationDialog extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(mContext)
                    .setMessage("asdf")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mContext.requestPermissions(new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA_PERMISSION);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Activity activity = mContext;
                                    if (activity != null) {
                                        activity.finish();
                                    }
                                }
                            })
                    .create();
        }
    }

}