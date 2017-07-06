package com.coder.kzxt.course.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.base.beans.LiveBean;
import com.coder.kzxt.base.beans.VideoBean;
import com.coder.kzxt.course.beans.LiveSynopspsBean;
import com.coder.kzxt.im.business.ImLoginBusiness;
import com.coder.kzxt.login.activity.LoginActivity;
import com.coder.kzxt.main.beans.SystemTimeResultBean;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.app.http.UrlsNew;
import com.coder.kzxt.video.activity.LiveReplayPlayActivity;
import com.coder.kzxt.video.activity.VideoLiveActivity;

public class LiveSynopsisActivity extends BaseActivity implements HttpCallBack
{
    private Toolbar toolbar;
    private SharedPreferencesUtil spu;
    private ImageView live_iv;
    private TextView live_title_tv;
    private TextView live_time_tv;
    private ImageView teacher_iv;
    private TextView teahcer_tv;
    private TextView teahcer_desc_tv;
    private TextView live_desc_tv;
    private RelativeLayout bottom_ly;
    private RelativeLayout activity_live_synopsis;
    private Button btn;
    private LiveBean liveBean;
    private int live_status;
    private LinearLayout jiazai_layout;

    public static void gotoActivity(Context mContext, LiveBean liveBean)
    {
        Intent intent = new Intent(mContext, LiveSynopsisActivity.class);
        intent.putExtra("liveBean", liveBean);
        mContext.startActivity(intent);
    }

    public static void gotoActivity(Context mContext, VideoBean videoBean)
    {
        mContext.startActivity(new Intent(mContext, LiveSynopsisActivity.class).putExtra("videoBean", videoBean));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_synopsis);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        spu = new SharedPreferencesUtil(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("直播详情");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity_live_synopsis = (RelativeLayout) findViewById(R.id.activity_live_synopsis);
        live_iv = (ImageView) findViewById(R.id.live_iv);
        live_title_tv = (TextView) findViewById(R.id.live_title_tv);
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        live_time_tv = (TextView) findViewById(R.id.live_time_tv);
        teacher_iv = (ImageView) findViewById(R.id.teacher_iv);
        teahcer_tv = (TextView) findViewById(R.id.teahcer_tv);
        teahcer_desc_tv = (TextView) findViewById(R.id.teahcer_desc_tv);
        live_desc_tv = (TextView) findViewById(R.id.live_desc_tv);
        bottom_ly = (RelativeLayout) findViewById(R.id.bottom_ly);
        btn = (Button) findViewById(R.id.btn);

        GradientDrawable myGrad = (GradientDrawable) btn.getBackground();
        myGrad.setColor(getResources().getColor(R.color.first_theme));// 设置内部颜色

        liveBean = (LiveBean) getIntent().getSerializableExtra("liveBean");

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (TextUtils.isEmpty(spu.getIsLogin()))
                {
                    Intent intent = new Intent(LiveSynopsisActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }

                if (!NetworkUtil.isNetworkAvailable(LiveSynopsisActivity.this))
                {
                    NetworkUtil.httpNetErrTip(LiveSynopsisActivity.this, activity_live_synopsis);
                    return;
                }

                if (liveBean.getUser_id().equals(spu.getUid()) && live_status != 2)
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(LiveSynopsisActivity.this, R.style.custom_dialog);
                    builder.setMessage(getResources().getString(R.string.myself_not_live));
                    builder.setPositiveButton(getResources().getString(R.string.btn_sure), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            finish();
                        }
                    });
                    builder.show();
                    return;
                }

                if (!NetworkUtil.isWifiNetwork(LiveSynopsisActivity.this))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LiveSynopsisActivity.this, R.style.custom_dialog);
                    builder.setMessage(getResources().getString(R.string.no_wifi_to_flow));
                    builder.setPositiveButton(getResources().getString(R.string.password_dialog_ensure), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            if (live_status != 2)
                            {

                                VideoLiveActivity.gotoActivity(LiveSynopsisActivity.this, liveBean);
                            } else
                            {

                                LiveReplayPlayActivity.gotoActivity(LiveSynopsisActivity.this, liveBean);
                            }

                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener()
                    {

                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                } else
                {
                    if (live_status != 2)
                    {
                        VideoLiveActivity.gotoActivity(LiveSynopsisActivity.this, liveBean);

                    } else
                    {
                        LiveReplayPlayActivity.gotoActivity(LiveSynopsisActivity.this, liveBean);
                    }
                }

            }
        });

        RequestData();
    }


    private void RequestData()
    {
        jiazai_layout.setVisibility(View.VISIBLE);

        new HttpGetBuilder(LiveSynopsisActivity.this)
                .setHttpResult(this)
                .setClassObj(LiveSynopspsBean.class)
                .setUrl(UrlsNew.GET_LIVE)
                .addQueryParams("center", "1")
                .setPath(liveBean.getId())
                .setRequestCode(2)
                .build();

        new HttpPostBuilder(this)
                .setHttpResult(this)
                .setClassObj(BaseBean.class)
                .setUrl(UrlsNew.CHAT_ROOM_LIVE_MEMBER)
                .addBodyParam("live_id", liveBean.getId())
                .setRequestCode(1111)
                .build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {

        if (requestCode == 1)
        {
            jiazai_layout.setVisibility(View.GONE);
            SystemTimeResultBean systemTimeResultBean = (SystemTimeResultBean) resultBean;
            long time = systemTimeResultBean.getItem().getTime();
            long start_time, end_time;
            live_status = liveBean.getLive_status();
            if (live_status != 2)
            {
                start_time = Long.parseLong(liveBean.getStart_time());
                end_time = Long.parseLong(liveBean.getEnd_time());

                // 结束时间大于当前时间  当前时间 在开播30分钟之内的
                if ((end_time > time && time > (start_time - 30 * 60)) || liveBean.getLive_status() == 1)
                {
                    bottom_ly.setVisibility(View.VISIBLE);
                    btn.setText("观看直播");
                } else
                {
                    bottom_ly.setVisibility(View.GONE);
                }
            } else
            {
                bottom_ly.setVisibility(View.VISIBLE);
                btn.setText("观看回放");
            }
        } else if (requestCode == 2)
        {

            LiveSynopspsBean listResultBean = (LiveSynopspsBean) resultBean;
            liveBean = listResultBean.getItem();
            GlideUtils.loadCourseImg(LiveSynopsisActivity.this, liveBean.getPicture(), live_iv);
            live_title_tv.setText(liveBean.getLive_title());
            String createt = liveBean.getStart_time();
            String endt = liveBean.getEnd_time();
            live_time_tv.setText("直播时间： " + DateUtil.getDateString(Long.parseLong(createt)) + "---" + DateUtil.getHourMin(Long.parseLong(endt)));
            teahcer_tv.setText(liveBean.getUser_profile().getNickname());
            teahcer_desc_tv.setText(liveBean.getUser_profile().getDesc());
            GlideUtils.loadHeaderOfTeacher(LiveSynopsisActivity.this, liveBean.getUser_profile().getAvatar(), teacher_iv);
            live_desc_tv.setText(liveBean.getDescription());

            new HttpGetBuilder(LiveSynopsisActivity.this)
                    .setUrl(UrlsNew.SYSTEM_TIME)
                    .setClassObj(SystemTimeResultBean.class)
                    .setHttpResult(LiveSynopsisActivity.this)
                    .setRequestCode(1)
                    .build();

        }


    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == 6013 || resultCode == 6014)
        {

            final AlertDialog.Builder builder = new AlertDialog.Builder(LiveSynopsisActivity.this, R.style.custom_dialog);
            builder.setMessage("请重新登录聊天室");
            builder.setPositiveButton(getResources().getString(R.string.login), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    new ImLoginBusiness().login();
                    dialog.cancel();
                }
            });
            builder.show();

        } else if (resultCode == 10005)
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(LiveSynopsisActivity.this, R.style.custom_dialog);
            builder.setMessage(getResources().getString(R.string.dialog_livelesson_cloes));
            builder.setPositiveButton(getResources().getString(R.string.btn_sure), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.cancel();
                }
            });
            builder.show();

        } else if (resultCode == 2001)
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(LiveSynopsisActivity.this, R.style.custom_dialog);
            builder.setMessage(getResources().getString(R.string.dialog_livelesson_oneequipment));
            builder.setPositiveButton(getResources().getString(R.string.btn_sure), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.cancel();
                }
            });
            builder.show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
