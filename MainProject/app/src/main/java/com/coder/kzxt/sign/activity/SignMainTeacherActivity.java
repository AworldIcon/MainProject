package com.coder.kzxt.sign.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.app.http.builders.HttpPutBuilder;
import com.app.utils.L;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.dialog.util.CustomListDialog;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.sign.beans.SignBean;
import com.coder.kzxt.sign.beans.SignResult;
import com.coder.kzxt.utils.Bimp;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.Counter;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.PermissionsUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.views.CustomNewDialog;
import com.coder.kzxt.views.RippleSpreadView;

/**
 * Created by MaShiZhao on 2017/4/18
 */

public class SignMainTeacherActivity extends BaseActivity implements AMapLocationListener, HttpCallBack
{

    private android.support.v7.widget.Toolbar toolbar;
    private View mainView;
    private TextView sign;
    private TextView sign_time;
    private TextView local;
    private TextView range;
    private RippleSpreadView rippleSpreadView;
    private ImageView rotateImage;
    private ImageView qrCode;
    private LinearLayout numberLy;
    private TextView signNumbers;
    private TextView unSignNumbers;
    private TextView sign_time_title;
    //扇形动画
    private ObjectAnimator animator;
    // 时间数据源
    private int[] timeData;
    private int timeSelect;
    //距离数据源
    private int[] rangeData;
    private int rangeSelect;
    // 维度 经度
    private String latitude;
    private String longitude;
    // 定位
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    // 跳转到地图页面需要code
    private static int REQUEST_CODE_MAP = 1001;
    /**
     * 网络请求
     */
    //获取签到详情
    private static final int API_CODE_SIGNING = 1000;
    //创建签到
    private static final int API_CODE_CREATE_SIGN = 1001;
    //结束签到
    private static final int API_CODE_CANCEL_SIGN = 1002;
    //修改签到距离
    private static final int API_CODE_RANGE_SIGN = 1003;
    //修改签到时间
    private static final int API_CODE_TIME_SIGN = 1004;


    // 声音
    private MediaPlayer playerRadar;
    private MediaPlayer playerOk;
    private MediaPlayer playerPop;

    private String courseId;
    private String classId;
    private String signId;
    private Boolean isSigning;


    public static void gotoActivity(Context mContext, String courseId, String classId)
    {
        mContext.startActivity(
                new Intent(mContext, SignMainTeacherActivity.class)
                        .putExtra("courseId", courseId)
                        .putExtra("classId", classId));
    }

    private void initVariable()
    {
        courseId = getIntent().getStringExtra("courseId");
        classId = getIntent().getStringExtra("classId");
//        courseId = "20875";
//        classId = "22735";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //获取从其他页面获取的变量 getIntent
        initVariable();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_main_teacher);
        //初始化 view
        initFindViewById();
        //需要初始化的数据
        initData();
        //响应事件click
        initClick();
        //网络请求
        requestData();
    }


    private void initFindViewById()
    {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        mainView = findViewById(R.id.mainView);
        rippleSpreadView = (RippleSpreadView) findViewById(R.id.rippleSpreadView);
        rotateImage = (ImageView) findViewById(R.id.rotateImage);
        qrCode = (ImageView) findViewById(R.id.qr_code);
        sign = (TextView) findViewById(R.id.sign);
        sign_time_title = (TextView) findViewById(R.id.sign_time_title);
        sign_time = (TextView) findViewById(R.id.sign_time);
        local = (TextView) findViewById(R.id.local);
        range = (TextView) findViewById(R.id.range);
        numberLy = (LinearLayout) findViewById(R.id.numberLy);
        signNumbers = (TextView) findViewById(R.id.signNumbers);
        unSignNumbers = (TextView) findViewById(R.id.unSignNumbers);

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.right_menu, menu);
        MenuItem item = menu.findItem(R.id.right_item);
        item.setTitle(getResources().getString(R.string.call_record));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            case R.id.right_item:
                SignHistoryListTeacherActivity.gotoActivity(SignMainTeacherActivity.this, courseId, classId);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private PermissionsUtil permissionsUtil;

    private void initData()
    {

        isSigning = false;
        //扇形动画
        animator = ObjectAnimator.ofFloat(rotateImage, "rotation", 0f, 360.0f);
        animator.setDuration(2000);
        animator.setInterpolator(new LinearInterpolator());// 不停顿
        animator.setRepeatCount(-1);// 设置动画重复次数
        animator.setRepeatMode(ValueAnimator.RESTART);// 动画重复模式

        //设置初始时间和范围
        timeData = new int[]{5, 10, 20, 30, 60};
        rangeData = new int[]{0, 50, 100, 200, 300};
        timeSelect = 5;
        rangeSelect = 100;

        permissionsUtil = new PermissionsUtil(this);
        if (permissionsUtil.gpsPermissions())
        {
            startLocation();
        }

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
        mLocationOption.setOnceLocation(false);
        // 开启定位
        mlocationClient.startLocation();
    }

    private void initClick()
    {
        sign.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (Double.valueOf(latitude) <= 0)
                {
                    ToastUtils.makeText(SignMainTeacherActivity.this, "定位中,请稍后再试！");
                    return;
                }
                if (isSigning())
                {
                    showSignFinishDialog();
                } else
                {
                    startAnimal();
                    createSign();
                }
            }

        });

        sign_time.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v)
            {
                if (isSigning())
                {
                    ToastUtils.makeText(SignMainTeacherActivity.this, "签到中,暂不支持修改时间！");
                    return;
                }
                showTimeDialog();
            }

        });

        range.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v)
            {
                showRangeDialog();
            }
        });

        local.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(SignMainTeacherActivity.this, MapTeacherActivity.class);
                startActivityForResult(intent, REQUEST_CODE_MAP);
            }
        });

        qrCode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showQrCodeDialog();
            }
        });

    }

    //开始签到
    private void startSign()
    {
        mlocationClient.stopLocation();
        isSigning = true;
        qrCode.setVisibility(View.VISIBLE);
        sign_time_title.setTextColor(ContextCompat.getColor(SignMainTeacherActivity.this, R.color.font_gray));
        startRadar();
    }

    //结束
    private void endSign()
    {
        bitmap = null;
        sign.setText(getString(R.string.call));
        qrCode.setVisibility(View.GONE);
        sign_time_title.setTextColor(ContextCompat.getColor(SignMainTeacherActivity.this, R.color.font_black));
        if (isSigning)
        {
            stopRadar();
            startPop();
        }
        isSigning = false;
        mlocationClient.startLocation();

    }

    // 返回是否正在签到 是返回true 不是返回false
    private Boolean isSigning()
    {
        return isSigning;
    }

    //请求网络
    private void requestData()
    {
        getSigning();
    }


    //开始动画
    public void startAnimal()
    {
        if (rippleSpreadView != null && !rippleSpreadView.isStarting())
        {
            rippleSpreadView.start();
        }

        if (animator != null && !animator.isRunning())
        {
            rotateImage.setVisibility(View.VISIBLE);
            animator.start();
        }
    }

    //结束动画
    public void stopAnimal()
    {
        if (rippleSpreadView != null && rippleSpreadView.isStarting())
        {
            rippleSpreadView.stop();
        }
        if (animator != null && animator.isRunning())
        {
            rotateImage.setVisibility(View.GONE);
            animator.end();
        }
    }


    //时间选择框
    private CustomListDialog timeDialog;

    private void showTimeDialog()
    {
        if (timeDialog == null)
        {
            timeDialog = new CustomListDialog(SignMainTeacherActivity.this);
            timeDialog.addData("5分钟", "10分钟", "20分钟", "30分钟", "60分钟");
            timeDialog.getAdapter().setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    timeSelect = timeData[position];
                    sign_time.setText(timeDialog.getData().get(position).getMsg());
                    timeDialog.dismiss();
                }
            });
        }
        timeDialog.show();
    }

    //范围选择框
    private CustomListDialog rangeDialog;

    private void showRangeDialog()
    {
        if (rangeDialog == null)
        {
            rangeDialog = new CustomListDialog(SignMainTeacherActivity.this);
            rangeDialog.addData("不限", "50米以内", "100米以内", "200米以内", "300米以内");
            rangeDialog.getAdapter().setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    rangeSelect = rangeData[position];
                    range.setText(rangeDialog.getData().get(position).getMsg());
                    rangeDialog.dismiss();
                    if (isSigning()) putRangeOfSign();
                }
            });
        }
        rangeDialog.show();
    }

    //定位功能相关

    //定位
    @Override
    public void onLocationChanged(AMapLocation amapLocation)
    {
        if (amapLocation != null)
        {
            if (amapLocation.getErrorCode() == 0)
            {
                latitude = amapLocation.getLatitude()+"";// 获取纬度
                longitude = amapLocation.getLongitude()+"";// 获取经度
                local.setText(amapLocation.getAddress());
                // 停止定位
                mlocationClient.stopLocation();
            } else if (NetworkUtil.isNetworkAvailable(SignMainTeacherActivity.this))
            {
                // 12是缺少定位权限
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                L.d("onLocationChanged: errText " + errText);
                if (amapLocation.getErrorCode() == 12)
                {
                    showLocalDialog();
                }

            }
        }
    }

    //定位的提示弹框
    private CustomNewDialog localDialog;

    private void showLocalDialog()
    {
        if (localDialog == null)
        {
            localDialog = new CustomNewDialog(SignMainTeacherActivity.this);
            localDialog.setMessage("您未打开GPS或未获取到定位权限");
            localDialog.setButtonOne(true);
        } else if (localDialog.isShowing())
        {
            return;
        }
        localDialog.show();

    }

    //    显示签到结束对话框
    private CustomNewDialog signDetailDialog;

    private void showDetailDialog()
    {
        if (signBean == null)
            return;
        signDetailDialog = new CustomNewDialog(SignMainTeacherActivity.this, R.layout.dlg_sign_finish);
        signDetailDialog.setCancelable(false);
        TextView totalNumber = (TextView) signDetailDialog.findViewById(R.id.totalNumber);
        TextView signedNumber = (TextView) signDetailDialog.findViewById(R.id.signedNumber);
        TextView unSignNumber = (TextView) signDetailDialog.findViewById(R.id.unSignNumber);
        TextView detail = (TextView) signDetailDialog.findViewById(R.id.detail);
        TextView cancel = (TextView) signDetailDialog.findViewById(R.id.cancel);
        totalNumber.setText(signBean.getNum() + "\n" + getString(R.string.should_signed));
        signedNumber.setText(signBean.getSign_num() + "\n" + getString(R.string.signed));
        unSignNumber.setText(signBean.getUnsign_num() + "\n" + getString(R.string.signed_not));
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signDetailDialog.cancel();
            }
        });
        detail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SignDetailListTeacherActivity.gotoActivity(SignMainTeacherActivity.this, signId, courseId, classId);
            }
        });
        signDetailDialog.show();

    }


    //显示签到结束对话框
    private CustomListDialog signFinishDialog;

    private void showSignFinishDialog()
    {
        signFinishDialog = new CustomListDialog(SignMainTeacherActivity.this);
        signFinishDialog.addData("结束点名");
        if (timeSelect <= 55)
        {
            signFinishDialog.addData("延长5分钟");
        }

        signFinishDialog.getAdapter().setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                signFinishDialog.cancel();
                if (position == 0)
                {
                    endSign();
                    cancelCounter();
                    cancelSign();
                    stopAnimal();
                    showDetailDialog();
                } else
                {
                    showLoadingView();
                    timeSelect += 5;
                    putTimeOfSign();
                }
            }
        });
        signFinishDialog.show();

    }


    //显示二维码的dialog
    private CustomNewDialog qrCodeDialog;
    private ImageView codeImage;
    private Bitmap bitmap;

    private void showQrCodeDialog()
    {
        if (bitmap == null)
        {
            ToastUtils.makeText(SignMainTeacherActivity.this, "暂无开启中的签到!");
            return;
        }

        if (qrCodeDialog == null)
        {
            qrCodeDialog = new CustomNewDialog(SignMainTeacherActivity.this, R.layout.dlg_sign_qrcode, R.style.DialogWhite, Gravity.CENTER);
            qrCodeDialog.show();
            codeImage = (ImageView) qrCodeDialog.findViewById(R.id.codeImage);
            LinearLayout qr_code_ly = (LinearLayout) qrCodeDialog.findViewById(R.id.qr_code_ly);
            qr_code_ly.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    qrCodeDialog.dismiss();
                }
            });
        }

        if (bitmap != null)
        {
            codeImage.setImageBitmap(bitmap);
            qrCodeDialog.show();
        } else
        {
            ToastUtils.makeText(SignMainTeacherActivity.this, getResources().getString(R.string.open_sign_fail));
            qrCodeDialog.dismiss();
        }

    }

    //获取二维码需要的签到id 普通签到1结尾 声波是2结尾
    private void createBitmap()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                bitmap = Bimp.createQRImage(getSignString());
            }
        }).start();
    }

    public String getSignString()
    {
        return "scanCodeSignAction|" + courseId + "|" + classId + "|" + signId + "|" + "1";
    }


    //设置界面数据
    private Counter counter;
    private int timeCount = 5;
    private SignBean signBean;
    private String signNumber = "0";

    public void setView(SignBean signBean)
    {
        this.signBean = signBean;
        signId = signBean.getId();


        if (!(signBean.getSign_num()).equals(signNumber))
        {
            startOk();
        }
        signNumber = signBean.getSign_num();

        signNumbers.setText(signBean.getSign_num() + "\n" + getString(R.string.signed));
        unSignNumbers.setText(signBean.getUnsign_num() + "\n" + getString(R.string.signed_not));


        //不是倒计时中则设置下面布局
        if (isSigning())
        {
            int time = (int) (signBean.getTime() / 60);
            timeSelect = time;
            sign_time.setText(time + "分钟");

            rangeSelect = signBean.getRange();
            if (signBean.getRange() == 0)
            {
                range.setText("不限");
            } else
            {
                range.setText(signBean.getRange() + "米以内");
            }
            local.setText(signBean.getAddress());
            if (signBean.getOdd() > 0)
            {
                createCounter(signBean.getOdd());
            }
            startAnimal();
            numberLy.setVisibility(View.VISIBLE);
        } else
        {
            numberLy.setVisibility(View.GONE);
        }
    }

    private void createCounter(long odd)
    {
        cancelCounter();
        counter = new Counter(SignMainTeacherActivity.this, odd * 1000, 1000, new Counter.CounterTick()
        {
            @Override
            public void finish()
            {
                cancelCounter();
                cancelSign();
                stopAnimal();
                showDetailDialog();
            }

            @Override
            public void tickTime(long day, long hour, long minute, long second)
            {
                sign.setText(counter.resetString(minute) + ":" + counter.resetString(second));
                timeCount++;
                if (timeCount >= 5)
                {
                    timeCount = 1;
                    getSigning();
                }
            }
        });
        counter.start();
        hideLoadingView();
    }

    private void cancelCounter()
    {
        if (counter != null)
        {
            counter.cancel();
        }
    }


    /**
     * 获取当前是否存在签到
     */
    public void getSigning()
    {
        new HttpGetBuilder(SignMainTeacherActivity.this)
                .setHttpResult(SignMainTeacherActivity.this)
                .setUrl(UrlsNew.SIGN)
                .setClassObj(SignResult.class)
                .setRequestCode(API_CODE_SIGNING)
                .addQueryParams("course_id", courseId)
                .addQueryParams("class_id", classId)
                .build();

    }

    /**
     * 开启签到
     */
    private void createSign()
    {
        new HttpPostBuilder(SignMainTeacherActivity.this)
                .setHttpResult(SignMainTeacherActivity.this)
                .setUrl(UrlsNew.SIGN_TEACHER)
                .setClassObj(SignResult.class)
                .setRequestCode(API_CODE_CREATE_SIGN)
                .addBodyParam("course_id", courseId)
                .addBodyParam("class_id", classId)
                .addBodyParam("time", timeSelect * 60 + "")
                .addBodyParam("range", rangeSelect + "")
                .addBodyParam("latitude", latitude + "")
                .addBodyParam("longitude", longitude + "")
                .addBodyParam("address", local.getText().toString())
                .build();
    }


    /**
     * 结束签到
     */
    private void cancelSign()
    {
        new HttpPutBuilder(SignMainTeacherActivity.this)
                .setHttpResult(SignMainTeacherActivity.this)
                .setUrl(UrlsNew.SIGN_TEACHER)
                .setClassObj(BaseBean.class)
                .setRequestCode(API_CODE_CANCEL_SIGN)
                .setPath(signId)
                .addBodyParam("type", "cancel_sign")
                .build();
    }

    /**
     * 修改签到的距离
     */
    private void putRangeOfSign()
    {
        new HttpPutBuilder(SignMainTeacherActivity.this)
                .setHttpResult(SignMainTeacherActivity.this)
                .setUrl(UrlsNew.SIGN_TEACHER)
                .setClassObj(BaseBean.class)
                .setRequestCode(API_CODE_RANGE_SIGN)
                .setPath(signId)
                .addBodyParam("type", "update_range")
                .addBodyParam("range", rangeSelect + "")
                .build();
    }

    /**
     * 修改签到的时间
     */
    private void putTimeOfSign()
    {
        new HttpPutBuilder(SignMainTeacherActivity.this)
                .setHttpResult(SignMainTeacherActivity.this)
                .setUrl(UrlsNew.SIGN_TEACHER)
                .setClassObj(BaseBean.class)
                .setRequestCode(API_CODE_TIME_SIGN)
                .setPath(signId)
                .addBodyParam("type", "update_time")
                .addBodyParam("time", timeSelect * 60 + "")
                .build();
    }
    /**
     * 修改签到的请未读
     */
    private void putLocationOfSign()
    {
        new HttpPutBuilder(SignMainTeacherActivity.this)
                .setHttpResult(SignMainTeacherActivity.this)
                .setUrl(UrlsNew.SIGN_TEACHER)
                .setClassObj(BaseBean.class)
                .setRequestCode(API_CODE_RANGE_SIGN)
                .setPath(signId)
                .addBodyParam("latitude", latitude)
                .addBodyParam("longitude", longitude)
                .addBodyParam("type", "update_address")
                .addBodyParam("address", local.getText().toString())
                .build();
    }


    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {
        switch (requestCode)
        {
            // 获取正在签到接口
            case API_CODE_SIGNING:
                SignResult signResult = (SignResult) resultBean;
                //存在正在签到则返回数据 否则不返回
                if (signResult.getItem().getId() != null)
                {
                    startSign();
                    setView(signResult.getItem());
                    createBitmap();
                } else
                {
                    cancelCounter();
                    endSign();
                }
                break;
            //发起签到
            case API_CODE_CREATE_SIGN:
                SignResult signResult2 = (SignResult) resultBean;
                if (signResult2.getItem().getId() != null)
                {
                    signId = signResult2.getItem().getId();
                    startSign();
                }
                createBitmap();
                startAnimal();
                createCounter(timeSelect * 60);
                break;
            //结束签到
            case API_CODE_CANCEL_SIGN:
                numberLy.setVisibility(View.GONE);
                cancelCounter();
                endSign();
                break;
            case API_CODE_RANGE_SIGN:
                isSigning = false;
                getSigning();
                break;
            case API_CODE_TIME_SIGN:
                isSigning = false;
                getSigning();
                break;
            default:
                break;
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {
        hideLoadingView();
        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
        {
            //重新登录
            NetworkUtil.httpRestartLogin(SignMainTeacherActivity.this, mainView);
        } else if (requestCode == API_CODE_CREATE_SIGN)
        {

            numberLy.setVisibility(View.GONE);
            stopAnimal();
            cancelCounter();
            endSign();
            ToastUtils.makeText(SignMainTeacherActivity.this, msg);
        } else
        {
            NetworkUtil.httpNetErrTip(SignMainTeacherActivity.this, mainView);
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        isVisibility = true;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        isVisibility = false;
        stopAllPlayer();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        cancelCounter();
        releaseAllPlayer();

    }

    private Boolean isVisibility;

    //开启签到声音
    private void startRadar()
    {
        if (playerRadar != null && playerRadar.isPlaying() || !isVisibility) return;
        playerRadar = MediaPlayer.create(this, R.raw.radar);
        playerRadar.setLooping(true);
        playerRadar.start();
    }

    private void startOk()
    {
        if (playerOk != null && playerOk.isPlaying() || !isVisibility) return;
        playerOk = MediaPlayer.create(this, R.raw.ok);
        playerOk.setLooping(false);
        playerOk.start();
    }

    private void startPop()
    {
        if (playerPop != null && playerPop.isPlaying() || !isVisibility) return;
        playerPop = MediaPlayer.create(this, R.raw.pop);
        playerPop.setLooping(false);
        playerPop.start();
    }


    private void stopRadar()
    {
        if (playerRadar != null)
        {
            playerRadar.stop();
        }
    }

    private void stopAllPlayer()
    {
        if (playerRadar != null)
        {
            playerRadar.pause();
        }
        if (playerOk != null)
        {
            playerOk.pause();
        }
        if (playerPop != null)
        {
            playerPop.pause();
        }
    }

    private void releaseAllPlayer()
    {
        if (playerPop != null)
        {
            playerPop.release();
        }

        if (playerOk != null)
        {
            playerOk.release();
        }
        if (playerRadar != null)
        {
            playerRadar.release();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == REQUEST_CODE_MAP && resultCode == MapTeacherActivity.RESULTCODE_SIGNMANAGE)
        {
            local.setText(data.getExtras().getString(MapTeacherActivity.POSITION, ""));
            latitude = data.getExtras().getString(MapTeacherActivity.LATITUDE, "");
            longitude = data.getExtras().getString(MapTeacherActivity.LONGITUDE, "");
            // 停止定位
            mlocationClient.stopLocation();
            if (isSigning()) putLocationOfSign();

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

}
