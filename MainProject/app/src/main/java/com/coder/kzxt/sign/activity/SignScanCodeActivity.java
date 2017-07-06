//package com.coder.kzxt.sign.activity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.content.res.AssetFileDescriptor;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.os.Vibrator;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.view.MenuItem;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//import android.widget.Toast;
//
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
//import com.amap.api.location.AMapLocationListener;
//import com.app.http.HttpCallBack;
//import com.app.utils.L;
//import com.coder.kzxt.activity.R;
//import com.coder.kzxt.base.activity.BaseActivity;
//import com.coder.kzxt.classe.activity.QrCodeActivity;
//import com.coder.kzxt.utils.NetworkUtil;
//import com.coder.kzxt.utils.ToastUtils;
//import com.coder.kzxt.utils.Utils;
//import com.coder.kzxt.views.CustomNewDialog;
//import com.google.zxing.Result;
//import com.google.zxing.camera.CameraManager;
//import com.google.zxing.decode.CaptureActivityHandler;
//import com.google.zxing.decode.DecodeManager;
//import com.google.zxing.decode.InactivityTimer;
//import com.google.zxing.view.QrCodeFinderView;
//
//import java.io.IOException;
//
//
///**
// * 扫描 学生端
// *
// * @author pc
// */
//public class SignScanCodeActivity extends QrCodeActivity implements SurfaceHolder.Callback, HttpCallBack ,AMapLocationListener
//{
//    private Toolbar toolbar;
//
//    private boolean timefinish = false;
//    private MediaPlayer playerawe_end;
//
//    //扫码需要的弹框
//    private CustomNewDialog scanDialog;
//    //提交结果的显示弹框
//    private CustomNewDialog customDialog;
//
//
//    /**
//     * 声音和振动相关参数
//     */
//    private static final float BEEP_VOLUME = 0.10f;
//    private static final long VIBRATE_DURATION = 200L;
//    private MediaPlayer mMediaPlayer;
//    private boolean mPlayBeep;
//    private boolean mVibrate;
//    private boolean mHasSurface;
//    private boolean mPermissionOk;
//    private SurfaceView mSurfaceView;
//    private QrCodeFinderView mQrCodeFinderView;
//    private InactivityTimer mInactivityTimer;
//    private CaptureActivityHandler mCaptureActivityHandler;
//    private final DecodeManager mDecodeManager = new DecodeManager();
//    private Handler mHandler = new Handler()
//    {
//        @Override
//        public void handleMessage(Message msg)
//        {
//            super.handleMessage(msg);
//        }
//    };
//
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_scan);
//        initVariable();
//        initView();
//        initData();
//        initScanTime();
//    }
//
//
//    //控件
//    private void initView()
//    {
//
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        mSurfaceView = (SurfaceView) findViewById(R.id.preview_view);
//        mQrCodeFinderView = (QrCodeFinderView) findViewById(R.id.viewfinder_view);
//
//        scanDialog = new CustomNewDialog(this);
//        customDialog = new CustomNewDialog(this);
//        mHasSurface = false;
//        toolbar.setTitle(getString(R.string.scan_code_sign));
//        setSupportActionBar(toolbar);
//
//    }
//
//    private void initData()
//    {
//        //定位相关
//        mlocationClient = new AMapLocationClient(this.getApplicationContext());
//        mLocationOption = new AMapLocationClientOption();
//        // 设置定位模式为高精度模式
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        // 设置定位监听
//        mlocationClient.setLocationListener(this);
//        mLocationOption.setOnceLocation(true);
//        // 开启定位
//        mlocationClient.startLocation();
//
//
//        CameraManager.init(this);
//        mInactivityTimer = new InactivityTimer(this);
//    }
//
//    private Runnable r = new Runnable()
//    {
//        @Override
//        public void run()
//        {
//            currentScanResult();
//        }
//    };
//
//
//    private void initScanTime()
//    {
//        if (mHandler != null)
//        {
//            mHandler.postDelayed(r, 60 * 1000);
//        }
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder)
//    {
//
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
//    {
//        if (!mHasSurface)
//        {
//            mHasSurface = true;
//            initCamera(holder);
//        }
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder)
//    {
//        mHasSurface = false;
//    }
//
//    public Handler getCaptureActivityHandler()
//    {
//        return mCaptureActivityHandler;
//    }
//
//    private void initBeepSound()
//    {
//        if (mPlayBeep && mMediaPlayer == null)
//        {
//            // The volume on STREAM_SYSTEM is not adjustable, and users found it too loud,
//            // so we now play on the music stream.
//            setVolumeControlStream(AudioManager.STREAM_MUSIC);
//            mMediaPlayer = new MediaPlayer();
//            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mMediaPlayer.setOnCompletionListener(mBeepListener);
//
//            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
//            try
//            {
//                mMediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
//                file.close();
//                mMediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
//                mMediaPlayer.prepare();
//            } catch (IOException e)
//            {
//                mMediaPlayer = null;
//            }
//        }
//    }
//
//    private void playBeepSoundAndVibrate()
//    {
//        if (mPlayBeep && mMediaPlayer != null)
//        {
//            mMediaPlayer.start();
//        }
//        if (mVibrate)
//        {
//            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//            vibrator.vibrate(VIBRATE_DURATION);
//        }
//    }
//
//    /**
//     * When the beep has finished playing, rewind to queue up another one.
//     */
//    private final MediaPlayer.OnCompletionListener mBeepListener = new MediaPlayer.OnCompletionListener()
//    {
//        public void onCompletion(MediaPlayer mediaPlayer)
//        {
//            mediaPlayer.seekTo(0);
//        }
//    };
//
//
//    @Override
//    protected void onResume()
//    {
//        super.onResume();
//        checkPermission();
//        if (!mPermissionOk)
//        {
//            mDecodeManager.showPermissionDeniedDialog(this);
//            return;
//        }
//        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
//        if (mHasSurface)
//        {
//            initCamera(surfaceHolder);
//        } else
//        {
//            surfaceHolder.addCallback(this);
//            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        }
//
//        mPlayBeep = true;
//        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
//        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL)
//        {
//            mPlayBeep = false;
//        }
//        initBeepSound();
//        mVibrate = true;
//    }
//
//    @Override
//    protected void onPause()
//    {
//        super.onPause();
//        if (mCaptureActivityHandler != null)
//        {
//            mCaptureActivityHandler.quitSynchronously();
//            mCaptureActivityHandler = null;
//        }
//        CameraManager.get().closeDriver();
//    }
//
//    @Override
//    protected void onDestroy()
//    {
//        if (null != mInactivityTimer)
//        {
//            mInactivityTimer.shutdown();
//        }
//        if (mHandler != null)
//        {
//            mHandler.removeCallbacks(r);
//        }
//        super.onDestroy();
//    }
//
//    public void restartPreview()
//    {
//        if (null != mCaptureActivityHandler)
//        {
//            mCaptureActivityHandler.restartPreviewAndDecode();
//        }
//    }
//
//    /**
//     * 扫描结果
//     *
//     * @param result
//     */
//    @Override
//    public void handleDecode(Result result)
//    {
//        mInactivityTimer.onActivity();
//        playBeepSoundAndVibrate();
//        String resultString = result.getText();
//        if (null == resultString)
//        {
//            mDecodeManager.showCouldNotReadQrCodeFromScanner(this, new DecodeManager.OnRefreshCameraListener()
//            {
//                @Override
//                public void refresh()
//                {
//                    restartPreview();
//                }
//            });
//        } else
//        {
//            processResult(resultString);
//        }
//    }
//
//    /**
//     * 处理扫描结果
//     *
//     * @param resultString
//     */
//    private void processResult(String resultString)
//    {
//        L.d("processResult: resultString " + resultString);
////        String message = getResources().getString(R.string.no_sign_qrcode);
////        String signNumber = resultString.substring(0, resultString.length() - 1);
////        String signType = resultString.substring(resultString.length() - 1, resultString.length());
//
//        mHandler.removeCallbacks(r);
//
//        //满足签到规则 14位 纯数字 最后结尾是1/2
//        if (!isSignString(resultString))
//        {
//            ToastUtils.makeText(SignScanCodeActivity.this, "不是合法的签到");
//            restartPreview();
//            return;
//        }
//
//
////        //  1 是普通签到 2 是声波签到
////        if (signType.equals("1"))
////        {
////            if (!NetworkUtil.isNetworkAvailable(SignScanCodeActivity.this))
////            {
////                JSONObject jsonObject = new JSONObject();
////                try
////                {
////                    jsonObject.put("courseId", courseId);
////                    jsonObject.put("classId", classId);
////                    jsonObject.put("publicCourse", isCenter);
////                    jsonObject.put("signTime", System.currentTimeMillis() / 1000 + "");
////                    jsonObject.put("latitude", latitude + "");
////                    jsonObject.put("longitude", longitude + "");
////                    jsonArray.put(jsonObject);
////                } catch (JSONException e)
////                {
////                    e.printStackTrace();
////                }
////                mCache.put(pu.getUid() + Constants.SIGN_NORMAL_KEY, jsonArray, 72 * 60 * 60);
////                showCustomNewDialog(getResources().getString(R.string.sign_outline_remind), 0);
////            } else
////            {
////                new ShakeSign_AsynTask(signNumber).executeOnExecutor(Constants.exec);
////            }
////        } else if (signType.equals("2"))
////        {
////            JSONObject jsonObject = new JSONObject();
////            try
////            {
////                jsonObject.put("courseId", courseId);
////                jsonObject.put("classId", classId);
////                jsonObject.put("publicCourse", isCenter);
////                jsonObject.put("signTime", System.currentTimeMillis() / 1000 + "");
////                jsonObject.put("signNumber", signNumber);
////                jsonArray.put(jsonObject);
////            } catch (JSONException e)
////            {
////                e.printStackTrace();
////            }
////            mCache.put(pu.getUid() + Constants.SIGN_VOICE_KEY, jsonArray, 72 * 60 * 60);
////            if (NetworkUtil.isNetworkAvailable(SignScanCodeActivity.this))
////            {
////                pu.commitOffSign(SignScanCodeActivity.this);
////            } else
////            {
////                showCustomNewDialog(getResources().getString(R.string.sign_outline_remind), 0);
////            }
////        }
//    }
//
//    // 判断是否是签到字符串
//    private boolean isSignString(String resultString)
//    {
//
//        // 不是纯数字或者14位
//        if (TextUtils.isEmpty(resultString)  || !Utils.isAllDigital(resultString))
//            return false;
//        String signType = resultString.substring(resultString.length() - 1, resultString.length());
//        return signType.equals("1") || signType.equals("2");
//    }
//
//    /**
//     * 当前扫描结果弹框
//     */
//    private void currentScanResult()
//    {
//
//        scanDialog.setMessage(getResources().getString(R.string.read_code_again));
//        scanDialog.setCancelable(false);
//        scanDialog.setLeftClick(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                scanDialog.cancel();
//                finish();
//            }
//        });
//        scanDialog.setRightClick(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                //开启重复扫描
//                scanDialog.cancel();
//            }
//        });
//        scanDialog.show();
//    }
//
//    /**
//     * 其他提示弹框
//     *
//     * @param message
//     */
//    private void showCustomNewDialog(final String message)
//    {
//        customDialog.setMessage(message);
//        customDialog.setCancelable(false);
//        customDialog.setButtonOne(true);
//        customDialog.setRightClick(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                scanDialog.cancel();
//                finish();
//            }
//        });
//        customDialog.show();
//    }
//
//    //相机 权限检测
//
//    private void initCamera(SurfaceHolder surfaceHolder)
//    {
//        try
//        {
//            CameraManager.get().openDriver(surfaceHolder);
//        } catch (IOException e)
//        {
//            // 基本不会出现相机不存在的情况
//            Toast.makeText(this, getString(R.string.qr_code_camera_not_found), Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        } catch (RuntimeException re)
//        {
//            re.printStackTrace();
//            mDecodeManager.showPermissionDeniedDialog(this);
//            return;
//        }
//        if (mCaptureActivityHandler == null)
//        {
//            mCaptureActivityHandler = new CaptureActivityHandler(this);
//        }
//    }
//
//
//    private void checkPermission()
//    {
//        boolean hasHardware = checkCameraHardWare(this);
//        if (hasHardware)
//        {
//            if (!hasCameraPermission())
//            {
//                 mQrCodeFinderView.setVisibility(View.GONE);
//                mPermissionOk = false;
//            } else
//            {
//                mPermissionOk = true;
//            }
//        } else
//        {
//            mPermissionOk = false;
//            finish();
//        }
//    }
//
//    private boolean hasCameraPermission()
//    {
//        PackageManager pm = getPackageManager();
//        return PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.CAMERA", getPackageName());
//    }
//
//    /* 检测相机是否存在 */
//    private boolean checkCameraHardWare(Context context)
//    {
//        PackageManager packageManager = context.getPackageManager();
//        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        switch (item.getItemId())
//        {
//            case android.R.id.home:
//                finish();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//
//    @Override
//    public void setOnSuccessCallback(int requestCode, Object resultBean)
//    {
//
//        ToastUtils.makeText(SignScanCodeActivity.this, getString(R.string.sign_success));
//        setResult(1000);
//        finish();
//    }
//
//    @Override
//    public void setOnErrorCallback(int requestCode, int code, String msg)
//    {
//
////        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
////        {
////            //重新登录
////            NetworkUtil.httpRestartLogin(SignScanCodeActivity.this, mainView);
////        } else
////        {
////            NetworkUtil.httpNetErrTip(SignScanCodeActivity.this, mainView);
////        }
//
//    }
//
//    @Override
//    public void onLocationChanged(AMapLocation amapLocation)
//    {
//    }
//
//
//}