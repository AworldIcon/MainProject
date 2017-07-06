package com.coder.kzxt.sign.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.app.utils.L;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.classe.activity.ScanClassActivity;
import com.coder.kzxt.sign.beans.SignBean;
import com.coder.kzxt.sign.beans.SignResult;
import com.coder.kzxt.sign.utils.CommitOffline;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.Counter;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.PermissionsUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.views.CustomNewDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MaShiZhao on 2017/4/18
 * 学生签到页面
 */

public class SignMainStudentActivity extends BaseActivity implements AMapLocationListener, HttpCallBack
{

    private android.support.v7.widget.Toolbar toolbar;
    private LinearLayout signLy;
    private TextView sign;
    private TextView sign_time;
    private TextView local;
    private TextView scan_code;
    private TextView course_name;
    private TextView status;
    private TextView range;
    private TextView range_time;
    private ImageView statusImage;
    private View mainView;

    // 跳转到地图页面需要code
    private static int REQUEST_CODE_MAP = 1000;

    //获取签到详情
    private static final int API_CODE_SIGNING = 1000;
    //签到
    private static final int API_CODE_SIGN_SIGN = 1001;
    //离线签到
//    private static final int API_CODE_OFF_SIGN = 1002;
    //签到的详情
    private static final int API_CODE_SIGN_INFO = 1003;
    //修改签到距离
    private static final int API_CODE_RANGE_SIGN = 1003;

    private String courseId;
    private String classId;
    private String courseName;
    private String signId;

    //老师是否开启签到中；
    private Boolean isSigning;

    // 维度 经度
    private double latitude;
    private double longitude;
    private String teacherLatitude = "", teacherLongitude = "";
    // 定位
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private SharedPreferencesUtil sharedPreferencesUtil;


    public static void gotoActivity(Context mContext, String courseId, String classId, String courseName)
    {
        mContext.startActivity(
                new Intent(mContext, SignMainStudentActivity.class)
                        .putExtra("courseId", courseId)
                        .putExtra("classId", classId)
                        .putExtra("courseName", courseName));
    }

    private void initVariable()
    {
        courseId = getIntent().getStringExtra("courseId");
        classId = getIntent().getStringExtra("classId");
        courseName = getIntent().getStringExtra("courseName");
//        courseId = "20875";
//        classId = "22735";
//        courseName = "ccc";
        sharedPreferencesUtil = new SharedPreferencesUtil(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //获取从其他页面获取的变量 getIntent
        initVariable();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_main_student);
        //初始化 view findviewbyid
        initFindViewById();
        //需要初始化的数据
        initData();
        //响应事件click
        initClick();
        //提交离线签到数据
        new CommitOffline(this);
        //网络请求
        getSigning();
    }

    private void initFindViewById()
    {
        mainView = findViewById(R.id.mainView);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        signLy = (LinearLayout) findViewById(R.id.signLy);
        sign = (TextView) findViewById(R.id.sign);
        sign_time = (TextView) findViewById(R.id.time);
        local = (TextView) findViewById(R.id.search_location);
        scan_code = (TextView) findViewById(R.id.scan_code);
        course_name = (TextView) findViewById(R.id.course_name);
        range = (TextView) findViewById(R.id.range);
        range_time = (TextView) findViewById(R.id.range_time);
        status = (TextView) findViewById(R.id.status);
        statusImage = (ImageView) findViewById(R.id.statusImage);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.right_menu, menu);
        MenuItem item = menu.findItem(R.id.right_item);
        item.setTitle(getResources().getString(R.string.signin_record));
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
                SignHistoryListStudentActivity.gotoActivity(SignMainStudentActivity.this, courseId, classId, courseName);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private PermissionsUtil permissionsUtil;

    private void initData()
    {
        permissionsUtil = new PermissionsUtil(this);
        if (permissionsUtil.gpsPermissions())
        {
            startLocation();
        }

        signId = "";
        isSigning = false;
        course_name.setText(courseName);
        setView(null);

        // 5秒去请求是否有新的签到
        createMainCounter();

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

    private void initClick()
    {
        signLy.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //   已完成的签到不可点击
//                if (signBean != null && signBean.getStatus() == 1 && signBean.getIs_range() == 1)
//                    return;
//                if (tenCounter != null) return;

                if (isClickable())
                {
                    sign();
                }
            }
        });

        scan_code.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                ScanClassActivity.gotoActivity(SignMainStudentActivity.this);
            }
        });

        local.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                MapStudentActivity.gotoActivity(SignMainStudentActivity.this,
                        teacherLatitude, teacherLongitude, isSigning());

            }
        });
    }


    //定位
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
            } else if (NetworkUtil.isNetworkAvailable(SignMainStudentActivity.this))
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
            localDialog = new CustomNewDialog(SignMainStudentActivity.this);
            localDialog.setMessage("您未打开GPS或未获取到定位权限");
            localDialog.setButtonOne(true);
        }
        localDialog.show();

    }


    //设置界面数据
    private SignBean signBean;

    public void setView(SignBean signBean)
    {
        this.signBean = signBean;

        //判断有没有网络
        if (NetworkUtil.isNetworkAvailable(SignMainStudentActivity.this))
        {
            //是否在签到
            if (isSigning() && signBean.getStatus() != 1)
            {
                //设置可点击
                setClickable();
            } else
            {
                //设置不可点击
                setUnclickable();
            }

            //是否在签到
            if (isSigning())
            {
                //范围提示
                if (isRanging())
                {
                    range.setVisibility(View.GONE);
                } else
                {
                    range.setVisibility(View.VISIBLE);
                    range.setText(getResources().getString(R.string.not_in_scope));
                }
            } else
            {
                //取消倒计时
                cancelSignCounter();
            }

        } else
        {
            setClickable();
        }

        status.setText(getStatusString());

    }


    //获取状态的文案 首先是网络状态 *  点名状态 * 签到范围
    public StringBuffer getStatusString()
    {

        StringBuffer stringBuffer = new StringBuffer();
        if (!NetworkUtil.isNetworkAvailable(SignMainStudentActivity.this))
        {
            stringBuffer.append("网络未连接");
            //网络image
            statusImage.setImageResource(R.drawable.sign_net_disconnect);
            return stringBuffer;
        }
        statusImage.setImageResource(R.drawable.sign_net_connected);
        stringBuffer.append("网络已连接");
        stringBuffer.append("  ");
        // 如果不在签到范围 则需要不显示网络状态
        if (isSigning())
        {
            stringBuffer.append("正在点名...");
        } else
        {
            stringBuffer.append("老师未发起点名");
        }
        return stringBuffer;
    }

    // 学生页面总体的倒计时 5秒发起一次请求
    private Counter mainCounter;
    private int timeCount = 1;

    private void createMainCounter()
    {
        cancelMainCounter();
        mainCounter = new Counter(SignMainStudentActivity.this, 10000 * 1000, 1000, new Counter.CounterTick()
        {
            @Override
            public void finish()
            {
                createMainCounter();
            }

            @Override
            public void tickTime(long day, long hour, long minute, long second)
            {

                timeCount++;
                if (timeCount == 5)
                {
                    timeCount = 1;
                    //获取当前是否有签到
                    getSigning();
                    //如果离线数据存在，就提交离线数据
                    new CommitOffline(SignMainStudentActivity.this);
                    //提示语
                    status.setText(getStatusString());

                }

            }
        });
        mainCounter.start();
    }

    private void cancelMainCounter()
    {
        if (mainCounter != null)
        {
            mainCounter.cancel();
        }
    }

    // 签到的倒计时
    private Counter signCounter;
    //判断老师是否延迟时间
    private long minute = 0;

    private void createSignCounter(long ood)
    {
        if (SignMainStudentActivity.this.minute * 60 + 120 >= ood) return;

        cancelSignCounter();
        signCounter = new Counter(SignMainStudentActivity.this, ood * 1000, 1000, new Counter.CounterTick()
        {
            @Override
            public void finish()
            {
                cancelSignCounter();
            }

            @Override
            public void tickTime(long day, long hour, long minute, long second)
            {
                SignMainStudentActivity.this.minute = minute;
                sign_time.setText(signCounter.resetString(minute) + ":" + signCounter.resetString(second));
            }
        });
        signCounter.start();
    }

    private void cancelSignCounter()
    {
        if (signCounter != null)
        {
            signCounter.cancel();
        }
        sign_time.setText("");
        minute = 0;
    }

    private Counter tenCounter;

    // 签到的倒计时
    private void createTenCounter()
    {
        cancelTenCounter();
        range_time.setVisibility(View.VISIBLE);
        tenCounter = new Counter(SignMainStudentActivity.this, 10 * 1000, 1000, new Counter.CounterTick()
        {
            @Override
            public void finish()
            {
                cancelTenCounter();
                setClickable();
            }

            @Override
            public void tickTime(long day, long hour, long minute, long second)
            {
                range_time.setText(tenCounter.resetString(second));
            }
        });
        tenCounter.start();
    }

    private void cancelTenCounter()
    {
        if (tenCounter != null)
        {
            tenCounter.cancel();
        }
        tenCounter = null;
    }


    //是否签到范围内
    private Boolean isRanging()
    {
        if (signBean == null)
            return true;

        if (signBean.getStatus() != 1)
            return true;

        return this.signBean.getIs_range() == 1;
    }

    //获取是否存在签到
    private Boolean isSigning()
    {
        return this.isSigning;
    }

    //设置现在是否存在签到
    private void setIsSigning(Boolean isSigning)
    {
        this.isSigning = isSigning;
    }

    /**
     * 获取学生现在是否可以点击 签到
     * 1.没有网络
     * 2.存在签到未签
     * 3.签到不在范围 10s后
     */
    private Boolean isClickable()
    {
        return sign.getCurrentTextColor() == ContextCompat.getColor(SignMainStudentActivity.this, R.color.font_blue);
    }

    //设置可点击
    private void setClickable()
    {
        signLy.setBackgroundResource(R.drawable.sign_background);
        sign.setTextColor(ContextCompat.getColor(SignMainStudentActivity.this, R.color.font_blue));
        range.setVisibility(View.GONE);
        range_time.setVisibility(View.GONE);
        range_time.setText("");
    }

    //设置不可点击
    private void setUnclickable()
    {
        //按钮状态
        signLy.setBackgroundResource(R.drawable.sign_background_unclick);
        sign.setTextColor(ContextCompat.getColor(SignMainStudentActivity.this, R.color.font_gray));
    }

    /**
     * 获取当前是否存在签到
     */
    public void getSigning()
    {
        new HttpGetBuilder(SignMainStudentActivity.this)
                .setHttpResult(SignMainStudentActivity.this)
                .setUrl(UrlsNew.SIGN)
                .setClassObj(SignResult.class)
                .setRequestCode(API_CODE_SIGNING)
                .addQueryParams("course_id", courseId)
                .addQueryParams("class_id", classId)
                .build();

    }

    /**
     * 获取当前签到的状态
     */
    public void getSignInfo(String signId)
    {
        new HttpGetBuilder(SignMainStudentActivity.this)
                .setHttpResult(SignMainStudentActivity.this)
                .setUrl(UrlsNew.SIGN_STUDENT)
                .setClassObj(SignResult.class)
                .setRequestCode(API_CODE_SIGN_INFO)
                .setPath(signId)
                .build();

    }


    /**
     * 签到
     */
    public void sign()
    {
        //  如果网络可用
        if (NetworkUtil.isNetworkAvailable(SignMainStudentActivity.this))
        {
            new HttpPostBuilder(SignMainStudentActivity.this)
                    .setHttpResult(SignMainStudentActivity.this)
                    .setUrl(UrlsNew.SIGN_STUDENT)
                    .setClassObj(SignResult.class)
                    .setRequestCode(API_CODE_SIGN_SIGN)
                    .addBodyParam("course_id", courseId)
                    .addBodyParam("class_id", classId)
                    .addBodyParam("sign_id", signId)
                    .addBodyParam("type", "0")
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
                jsonObject.put("course_id", courseId);
                jsonObject.put("class_id", classId);
                jsonObject.put("id", signId);
                jsonObject.put("type", "0");
                jsonObject.put("latitude", latitude + "");
                jsonObject.put("longitude", longitude + "");
                jsonObject.put("is_offline", "1");
                jsonObject.put("status", "1");
                jsonObject.put("sign_time", System.currentTimeMillis() / 1000 + "");
            } catch (JSONException e)
            {
                e.printStackTrace();
            }

            signId = "";
            sharedPreferencesUtil.setSignOffline(jsonObject.toString());

            //提示离线签到信息记录
//            new SnackbarUtils(SignMainStudentActivity.this)
//                    .Long(mainView, getString(R.string.sign_outline_remind))
//                    .show();

            showOfflineDialog();
        }
    }

    private AlertDialog dialog;

    private void showOfflineDialog()
    {
        if (dialog == null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignMainStudentActivity.this, R.style.custom_dialog);
            builder.setMessage(getResources().getString(R.string.sign_outline_remind));
            builder.setPositiveButton(getResources().getString(R.string.btn_sure), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
        }
        if (!dialog.isShowing())
            dialog.show();
    }


    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {
        switch (requestCode)
        {
            // 获取正在签到接口
            case API_CODE_SIGNING:
                SignResult signResult = (SignResult) resultBean;
                setIsSigning(signResult.getItem().getId() != null);
                if (isSigning())
                {
                    teacherLatitude = signResult.getItem().getLatitude();
                    teacherLongitude = signResult.getItem().getLongitude();

                    createSignCounter(signResult.getItem().getOdd());
                    if (!signId.equals(signResult.getItem().getId()))
                    {
                        sign.setText(getString(R.string.sign_string));
                        sign.setTextColor(ContextCompat.getColor(SignMainStudentActivity.this, R.color.font_blue));
                        signId = signResult.getItem().getId();
                        getSignInfo(signResult.getItem().getId());
                        //倒计时循环
                    }
                } else
                {
                    sign.setText(getString(R.string.sign_string));
                    cancelSignCounter();
                    setView(signResult.getItem());
                }

                break;

            // 签到详情
            case API_CODE_SIGN_INFO:
                SignResult signResult3 = (SignResult) resultBean;
                if (signResult3.getItem().getId() != null)
                {
                    cancelSignCounter();
                    sign.setText(getString(R.string.signed));
                    if (signResult3.getItem().getIs_range() == 0) createTenCounter();
                }
                setView(signResult3.getItem());
                break;

            //签到
            case API_CODE_SIGN_SIGN:
                SignResult signResult2 = (SignResult) resultBean;
                sign.setText(getString(R.string.signed));
                setUnclickable();
                cancelSignCounter();

                //设置当前签到状态
                if (signResult2.getItem().getIs_range() == 0 || signResult2.getItem().getIs_offline() == 1)
                {
                    createTenCounter();
                } else
                {
                    cancelTenCounter();
                }
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
            NetworkUtil.httpRestartLogin(SignMainStudentActivity.this, mainView);
        } else
        {
            sign.setText("签到");
            setClickable();
            status.setText(getStatusString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == 1000)
        {
            getSignInfo(signId);
        } else if (requestCode == REQUEST_CODE_MAP && resultCode == MapTeacherActivity.RESULTCODE_SIGNMANAGE)
        {
            local.setText(data.getExtras().getString(MapTeacherActivity.POSITION, ""));
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        cancelMainCounter();
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
