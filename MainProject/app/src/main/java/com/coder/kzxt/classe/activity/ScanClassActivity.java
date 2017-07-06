package com.coder.kzxt.classe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.app.utils.L;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.classe.beans.ClassBean;
import com.coder.kzxt.order.beans.SingleCourse;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.PermissionsUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.utils.Utils;
import com.coder.kzxt.video.activity.VideoViewActivity;
import com.coder.kzxt.video.beans.CourseRoleResult;
import com.coder.kzxt.views.CustomNewDialog;
import com.coder.kzxt.webview.activity.HelpActivity;
import com.google.zxing.Result;
import com.google.zxing.camera.CameraManager;
import com.google.zxing.decode.CaptureActivityHandler;
import com.google.zxing.decode.DecodeImageCallback;
import com.google.zxing.decode.DecodeManager;
import com.google.zxing.decode.InactivityTimer;
import com.google.zxing.utils.QrUtils;
import com.google.zxing.view.QrCodeFinderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 扫码加入班级
 */
public class ScanClassActivity extends QrCodeActivity implements SurfaceHolder.Callback, HttpCallBack, AMapLocationListener
{

    /**
     * 返回键
     */
    private ImageView left_Image;
    private ImageView flash_light;
    private ImageView photo_pic;
    /**
     * 是否打开闪光灯
     */
    private boolean isOpen = false;
    private RelativeLayout identifyLayout;
    private CustomNewDialog dialog;
    private boolean isRisk = false;
    private String content;
    //    private ScanQrcodeBean qr;
    private String risk_url; //危险链接
    private String imgPath;

    private static final int REQUEST_SYSTEM_PICTURE = 0;
    private static final int MSG_DECODE_SCAN = 3;
    public static final int MSG_DECODE_SUCCEED = 1;
    public static final int MSG_DECODE_FAIL = 2;
    private boolean mHasSurface;
    private boolean mPermissionOk;
    private InactivityTimer mInactivityTimer;
    private QrCodeFinderView mQrCodeFinderView;
    private SurfaceView mSurfaceView;
    private final DecodeManager mDecodeManager = new DecodeManager();
    private CaptureActivityHandler mCaptureActivityHandler;
    /**
     * 声音和振动相关参数
     */
    private static final float BEEP_VOLUME = 0.10f;
    private static final long VIBRATE_DURATION = 200L;
    private MediaPlayer mMediaPlayer;
    private boolean mPlayBeep;
    private boolean mVibrate;
    private Handler mHandler;
    private SharedPreferencesUtil spu;
    private Executor mQrCodeExecutor;
    private RelativeLayout myLayout;
    private String timeStamp;  //时间戳
    private String classId;  //班级id
    private String courseId; //课程id
    private ArrayList<SingleCourse.Course> items;
    private List<CourseRoleResult.Item.ListBean> courseList;
    private int isJoin;  //是否加入
    private boolean isClose = false;


    //签到用到
    private PermissionsUtil permissionsUtil;

    private double latitude;
    private double longitude;
    // 定位
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    public static void gotoActivity(BaseActivity context)
    {
        context.startActivityForResult(new Intent(context, ScanClassActivity.class), 1000);
    }


    private void initVariable()
    {
        courseId = getIntent().getStringExtra("courseId");
        classId = getIntent().getStringExtra("classId");
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        initVariable();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
            window.setNavigationBarColor(Color.BLACK);
        }
        super.onCreate(savedInstanceState);
        spu = new SharedPreferencesUtil(this);
        setContentView(R.layout.activity_scan_class_capture);
        initView();
        initData();
        setListener();
     }

    private void initView()
    {
        left_Image = (ImageView) findViewById(R.id.left_Image);
        flash_light = (ImageView) findViewById(R.id.iv_flash_light);
        photo_pic = (ImageView) findViewById(R.id.photo_pic);
        identifyLayout = (RelativeLayout) findViewById(R.id.rl_identify);
        mQrCodeFinderView = (QrCodeFinderView) findViewById(R.id.viewfinder_view);
        mSurfaceView = (SurfaceView) findViewById(R.id.preview_view);
        myLayout = (RelativeLayout) findViewById(R.id.my_layout);
        mHasSurface = false;
    }

    private void setListener()
    {
        left_Image.setOnClickListener(listener);
        flash_light.setOnClickListener(listener);
        photo_pic.setOnClickListener(listener);
    }

    private void initData()
    {
        CameraManager.init(this);
        mInactivityTimer = new InactivityTimer(this);
        mQrCodeExecutor = Executors.newSingleThreadExecutor();
        mHandler = new WeakHandler(this);
    }

    private void checkPermission()
    {
        boolean hasHardware = checkCameraHardWare(this);
        if (hasHardware)
        {
            if (!hasCameraPermission())
            {
                findViewById(R.id.qr_code_view_background).setVisibility(View.VISIBLE);
                mQrCodeFinderView.setVisibility(View.GONE);
                mPermissionOk = false;
            } else
            {
                mPermissionOk = true;
            }
        } else
        {
            mPermissionOk = false;
            finish();
        }


        permissionsUtil = new PermissionsUtil(this);
        if (permissionsUtil.gpsPermissions())
        {
            startLocation();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        checkPermission();
        if (!mPermissionOk)
        {
            mDecodeManager.showPermissionDeniedDialog(this);
            return;
        }
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        if (mHasSurface)
        {
            initCamera(surfaceHolder);
        } else
        {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        mPlayBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL)
        {
            mPlayBeep = false;
        }
        initBeepSound();
        mVibrate = true;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (mCaptureActivityHandler != null)
        {
            mCaptureActivityHandler.quitSynchronously();
            mCaptureActivityHandler = null;
        }
        CameraManager.get().closeDriver();
    }

    private void initCamera(SurfaceHolder surfaceHolder)
    {
        try
        {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException e)
        {
            // 基本不会出现相机不存在的情况
            Toast.makeText(this, getString(R.string.qr_code_camera_not_found), Toast.LENGTH_SHORT).show();
            finish();
            return;
        } catch (RuntimeException re)
        {
            re.printStackTrace();
            mDecodeManager.showPermissionDeniedDialog(this);
            return;
        }
        mQrCodeFinderView.setVisibility(View.VISIBLE);
        mSurfaceView.setVisibility(View.VISIBLE);
        findViewById(R.id.qr_code_view_background).setVisibility(View.GONE);
        if (mCaptureActivityHandler == null)
        {
            mCaptureActivityHandler = new CaptureActivityHandler(this);
        }
    }

    /**
     * 处理扫描结果
     *
     * @param result
     */
    public void handleDecode(Result result)
    {
        mInactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        identifyLayout.setVisibility(View.GONE);
        if (null == result)
        {
            mDecodeManager.showCouldNotReadQrCodeFromScanner(this, new DecodeManager.OnRefreshCameraListener()
            {
                @Override
                public void refresh()
                {
                    restartPreview();
                }
            });
        } else
        {
            processScan(result.getText());
        }

    }


    private void processScan(String resultString)
    {
        L.d("processScan: resultString " + resultString);
        if (resultString.contains("scanCodeSignAction"))
        {
            sign(resultString);

        } else if (resultString.contains("scanQrcodeJoinClassAction"))
        {
            processUrl(resultString);
        } else
        {
            if (resultString.contains("http") || resultString.contains("https") ||
                    resultString.contains("https://") || resultString.contains("ftp"))
            {
                isRisk = true;
                risk_url = resultString;
                content = getResources().getString(R.string.rick_url) + resultString;
                popDialog(content);
            } else
            {
                redundancyProcess(resultString);
            }
        }
    }

    //    scanCodeSignAction|courseod|classid|signid|1或2
    private void sign(String resultString)
    {
        String[] result = resultString.split("\\|");
        if (!isSignString(result))
        {
            ToastUtils.makeText(ScanClassActivity.this, getString(R.string.no_sign_qrcode));
            return;
        }
        String signId = result[3];
        //  如果网络可用
        if (NetworkUtil.isNetworkAvailable(ScanClassActivity.this))
        {
            new HttpPostBuilder(ScanClassActivity.this)
                    .setHttpResult(ScanClassActivity.this)
                    .setUrl(UrlsNew.SIGN_STUDENT)
                    .setClassObj(BaseBean.class)
                    .addBodyParam("course_id", result[1])
                    .addBodyParam("class_id", result[2])
                    .addBodyParam("sign_id", signId)
                    .addBodyParam("type", "1")
                    .addBodyParam("latitude", latitude + "")
                    .addBodyParam("longitude", longitude + "")
                    .addBodyParam("is_offline", "0")
                    .addBodyParam("status", "1")
                    .addBodyParam("sign_time", System.currentTimeMillis() / 1000 + "")
                    .build();
        } else
        {

            JSONObject jsonObject = new JSONObject();
            try
            {
                jsonObject.put("course_id", result[1]);
                jsonObject.put("class_id", result[2]);
                jsonObject.put("id", signId);
                jsonObject.put("type", "1");
                jsonObject.put("latitude", latitude + "");
                jsonObject.put("longitude", longitude + "");
                jsonObject.put("is_offline", "1");
                jsonObject.put("status", "1");
                jsonObject.put("sign_time", System.currentTimeMillis() / 1000 + "");
            } catch (JSONException e)
            {
                e.printStackTrace();
            }

            new SharedPreferencesUtil(ScanClassActivity.this).setSignOffline(jsonObject.toString());

            isRisk = false;
            popDialog(getString(R.string.sign_outline_remind));

        }
    }


    private void redundancyProcess(String resultString)
    {
        if (!TextUtils.isEmpty(resultString))
        {
            Intent intent = new Intent(this, ScanQrcodePageActivity.class);
            intent.putExtra("title", "扫描结果");
            intent.putExtra("web_url", resultString);
            startActivityForResult(intent, Constants.REQUEST_SCAN_OK);
        }
    }

    /**
     * 处理解析结果
     *
     * @param web_url
     */
    private void processUrl(String web_url)
    {
        Map<String, String> map = Utils.extracParameter(web_url);
        timeStamp = map.get("timeStamp");
        courseId = map.get("courseId");
        classId = map.get("classId");
        isJoinClass(courseId);
    }

    // 判断是否是签到字符串
    // scanCodeSignAction|courseod|classid|signid|1或2
    private boolean isSignString(String[] results)
    {
        if (results.length != 5) return false;
        if (!results[0].equals("scanCodeSignAction")) return false;
        // 不是纯数字
        if (!Utils.isAllDigital(results[1]) ||!Utils.isAllDigital(results[2]) || !Utils.isAllDigital(results[3]))
        {
            return false;
        }
        return  results[4].equals("1") || results[4].equals("2");
     }

    private void requestData(String courseId)
    {
        new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_SINGLE_COURSE)
                .setHttpResult(new HttpCallBack()
                {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean)
                    {
                        SingleCourse singleCourse = (SingleCourse) resultBean;
                        items = singleCourse.getItems();
                        if (isJoin == 0)
                        {  //未加入
                            getClassEndTime(timeStamp, classId);
                        } else
                        {  //已加入
                            if ((classId.equals(courseList.get(0).getId())))
                            {  //扫码班级id等于加入的班级id
                                jumpActivity(items.get(0));
                            } else
                            {
                                content = getResources().getString(R.string.already_join_class);
                                popDialog(content);
                            }
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg)
                    {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
                        {
                            NetworkUtil.httpRestartLogin(ScanClassActivity.this, myLayout);
                        } else
                        {
                            NetworkUtil.httpNetErrTip(ScanClassActivity.this, myLayout);
                        }
                    }
                })
                .setClassObj(SingleCourse.class)
                .addQueryParams("id", courseId)
                .build();

    }


    public void getClassEndTime(final String currentTime, String classId)
    {
        new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_COURSE_CLASS)
                .setHttpResult(new HttpCallBack()
                {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean)
                    {
                        ClassBean bean = (ClassBean) resultBean;
                        ClassBean.ClassItem classItem = bean.getItem();
                        if (Long.parseLong(currentTime) > Long.parseLong(classItem.getEnd_time()) ||
                                classItem.getApply_status().equals("0"))
                        {
                            isClose = true;
                            content = getResources().getString(R.string.class_end_time);
                        } else if (Long.parseLong(currentTime) < Long.parseLong(classItem.getStart_time()))
                        {
                            isClose = true;
                            content = getResources().getString(R.string.class_already_close);
                        }
                        if (!TextUtils.isEmpty(content))
                        {
                            popDialog(content);
                        } else
                        {
                            jumpActivity(items.get(0));
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg)
                    {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
                        {
                            NetworkUtil.httpRestartLogin(ScanClassActivity.this, myLayout);
                        } else
                        {
                            NetworkUtil.httpNetErrTip(ScanClassActivity.this, myLayout);
                        }
                    }
                })
                .setClassObj(ClassBean.class)
                .setPath(classId)
                .build();
    }

    /**
     * 判断是否加入班级
     *
     * @param courseId
     */
    public void isJoinClass(final String courseId)
    {
        new HttpGetBuilder(ScanClassActivity.this)
                .setHttpResult(new HttpCallBack()
                {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean)
                    {
                        CourseRoleResult courseClassBean = (CourseRoleResult) resultBean;
                        //0未加入 1学生 2老师 3创建课程的老师
                        isJoin = courseClassBean.getItem().getRole();
                        courseList = courseClassBean.getItem().getList();
                        requestData(courseId);
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg)
                    {
                        requestData(courseId);
                    }
                })
                .setClassObj(CourseRoleResult.class)
                .setUrl(UrlsNew.GET_COURSE_ROLE)
                .addQueryParams("course_id", courseId)
                .build();

    }


    private boolean hasCameraPermission()
    {
        PackageManager pm = getPackageManager();
        return PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.CAMERA", getPackageName());
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener mBeepListener = new OnCompletionListener()
    {
        public void onCompletion(MediaPlayer mediaPlayer)
        {
            mediaPlayer.seekTo(0);
        }
    };

    private void initBeepSound()
    {
        if (mPlayBeep && mMediaPlayer == null)
        {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(mBeepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try
            {
                mMediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mMediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mMediaPlayer.prepare();
            } catch (IOException e)
            {
                mMediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate()
    {
        if (mPlayBeep && mMediaPlayer != null)
        {
            mMediaPlayer.start();
        }
        if (mVibrate)
        {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * 按钮监听器
     */
    private OnClickListener listener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.left_Image: //返回键
                    finish();
                    break;
                case R.id.iv_flash_light:  //闪光灯
                    if (!isOpen)
                    {
                        isOpen = true;
                        CameraManager.get().setFlashLight(true);
                        flash_light.setBackgroundResource(R.drawable.open_flash_lamp);
                    } else
                    {
                        isOpen = false;
                        CameraManager.get().setFlashLight(false);
                        flash_light.setBackgroundResource(R.drawable.close_flash_lamp);
                    }
                    break;
                case R.id.photo_pic://打开相册
                    openPicture();
                    break;
                case R.id.leftTextView:    //取消
                    if (dialog.isShowing())
                    {
                        content = "";
                        dialog.cancel();
                    }
                    restartPreview();
                    break;
                case R.id.rightTextView:  //确定
                    if (dialog.isShowing())
                    {
                        dialog.cancel();
                        content = "";
                        if (isRisk)
                        {
                            redundancyProcess(risk_url);
                        } else
                        {
                            if (isClose)
                            {
                                finish();
                            } else if (courseList != null)
                            {
                                jumpActivity(items.get(0));
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        if (!mHasSurface)
        {
            mHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        mHasSurface = false;
    }

    /***
     * 打开帮助与反馈
     */
    private void helpAndFeedBack()
    {
        //课程关闭情况下
        Intent intent = new Intent(this, HelpActivity.class);
        intent.putExtra(Constants.SCAN_QRCODE, "scanQrcode");
        startActivity(intent);
        finish();
    }

    /**
     * 打开相册
     */
    private void openPicture()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        if (Build.VERSION.SDK_INT < 19)
        {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else
        {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_SYSTEM_PICTURE);
    }

    /**
     * 解析图片
     */
    private void decodeImage()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Result result = QrUtils.scanningImage(imgPath);
                if (result == null)
                {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "没有二维码图片", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else
                {
                    // 数据返回
                    String recode = QrUtils.recode(result.toString());
                    Message msg = Message.obtain();
                    msg.what = MSG_DECODE_SCAN;
                    msg.obj = recode;
                    mHandler.sendMessageDelayed(msg, 500);
                }
            }
        }).start();
    }

    /**
     * 获取图库中的图片url
     *
     * @param data
     */
    private void getImagePath(Intent data)
    {
        Cursor cursor = null;
        try
        {
            String[] proj = {MediaStore.Images.Media.DATA};  // 获取选中图片的路径
            cursor = getContentResolver().query(data.getData(), proj, null, null, null);
            if (cursor.moveToFirst())
            {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                imgPath = cursor.getString(column_index);
                if (imgPath == null)
                {
                    imgPath = QrUtils.getPath(getApplicationContext(), data.getData());
                }
                decodeImage();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }

    private DecodeImageCallback mDecodeImageCallback = new DecodeImageCallback()
    {
        @Override
        public void decodeSucceed(Result result)
        {
            mHandler.obtainMessage(MSG_DECODE_SUCCEED, result).sendToTarget();
        }

        @Override
        public void decodeFail(int type, String reason)
        {
            mHandler.sendEmptyMessage(MSG_DECODE_FAIL);
        }
    };


    private class WeakHandler extends Handler
    {
        private WeakReference<QrCodeActivity> mWeakQrCodeActivity;
        private DecodeManager mDecodeManager = new DecodeManager();

        public WeakHandler(QrCodeActivity imagePickerActivity)
        {
            super();
            this.mWeakQrCodeActivity = new WeakReference<>(imagePickerActivity);
        }

        @Override
        public void handleMessage(Message msg)
        {
            QrCodeActivity qrCodeActivity = mWeakQrCodeActivity.get();
            switch (msg.what)
            {
                case MSG_DECODE_SUCCEED:
                    Result result = (Result) msg.obj;
                    if (null == result)
                    {
                        mDecodeManager.showCouldNotReadQrCodeFromPicture(qrCodeActivity);
                    } else
                    {
                        String resultString = result.getText();
                        handleResult(resultString);
                    }
                    break;
                case MSG_DECODE_SCAN:
                    String scanResult = (String) msg.obj;
                    handleResult(scanResult);
                    break;
                case MSG_DECODE_FAIL:
                    mDecodeManager.showCouldNotReadQrCodeFromPicture(qrCodeActivity);
                    break;
            }
            super.handleMessage(msg);
        }

        private void handleResult(String resultString)
        {
            processScan(resultString);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
        {
            return;
        }
        if (requestCode == REQUEST_SYSTEM_PICTURE
                && resultCode == RESULT_OK)
        {
            getImagePath(data);
        } else if (requestCode == Constants.REQUEST_SCAN_OK
                && resultCode == Constants.RESULT_SCAN_OK)
        {
            finish();
        }

    }


    private void jumpActivity(SingleCourse.Course bean)
    {
        Intent intent = new Intent(this, VideoViewActivity.class);
        intent.putExtra("flag", Constants.ONLINE);
        intent.putExtra("treeid", bean.getId() + "");
        intent.putExtra("classId", classId);
        intent.putExtra("scanQrcode", "scanQrcode");
        intent.putExtra("tree_name", bean.getTitle());
        intent.putExtra("pic", bean.getMiddle_pic());
        intent.putExtra(Constants.IS_CENTER, "0");
        startActivity(intent);
        finish();
    }


    private void popDialog(String content)
    {
        dialog = new CustomNewDialog(this);
        TextView message = (TextView) dialog.findViewById(R.id.message);
        TextView cancle = (TextView) dialog.findViewById(R.id.leftTextView);
        TextView confirm = (TextView) dialog.findViewById(R.id.rightTextView);
        confirm.setText(getResources().getString(R.string.knows));
        cancle.setVisibility(View.GONE);
        message.setText(content);
        if (isRisk)
        {
            confirm.setText(getResources().getString(R.string.open_url));
            cancle.setVisibility(View.VISIBLE);
            cancle.setOnClickListener(listener);
        }
        confirm.setOnClickListener(listener);
        dialog.show();
    }

    public void restartPreview()
    {
        if (null != mCaptureActivityHandler)
        {
            mCaptureActivityHandler.restartPreviewAndDecode();
        }
    }

    /* 检测相机是否存在 */
    private boolean checkCameraHardWare(Context context)
    {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    protected void onDestroy()
    {
        if (null != mInactivityTimer)
        {
            mInactivityTimer.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {
        ToastUtils.makeText(ScanClassActivity.this, getString(R.string.sign_success));
        setResult(1000);
        finish();
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {
        ToastUtils.makeText(ScanClassActivity.this, getString(R.string.sign_fail) + msg);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation)
    {
        if (amapLocation != null)
        {
            if (amapLocation.getErrorCode() == 0)
            {
                latitude = amapLocation.getLatitude();// 获取纬度
                longitude = amapLocation.getLongitude();// 获取经度
//                local.setText(amapLocation.getAddress());
                // 停止定位
                mlocationClient.stopLocation();
            } else if (NetworkUtil.isNetworkAvailable(ScanClassActivity.this))
            {
                // 12是缺少定位权限
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                L.d("onLocationChanged: errText " + errText);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (permissionsUtil.permissionsResult(requestCode, permissions, grantResults))
        {
            startLocation();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void startLocation()
    {
        //定位相关
        mlocationClient = new AMapLocationClient(this.getApplicationContext());
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置定位监听
        mlocationClient.setLocationListener(this);
        mLocationOption.setOnceLocation(true);
        // 开启定位
        mlocationClient.startLocation();
    }

}