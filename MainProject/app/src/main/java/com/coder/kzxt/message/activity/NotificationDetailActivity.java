package com.coder.kzxt.message.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.message.beans.NotificationDetailBean;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;

/**
 * Created by zw on 2017/6/24
 * 通知详情
 */

public class NotificationDetailActivity extends BaseActivity
{
    private android.support.v7.widget.Toolbar mToolbar;
    private TextView mNotificationName;
    private TextView mSendName;
    private TextView mSendTime;
    private TextView readNum;
    private TextView unReadNum;
    private TextView check_detail;
    private TextView mNotificationContent;
    private LinearLayout notificationNumber;
    private int notifyId;
    private RelativeLayout mainView;
    private ScrollView data_view;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        mNotificationName = (TextView) findViewById(R.id.notificationName);
        mSendName = (TextView) findViewById(R.id.sendName);
        mSendTime = (TextView) findViewById(R.id.sendTime);
        readNum = (TextView) findViewById(R.id.readNum);
        unReadNum = (TextView) findViewById(R.id.unReadNum);
        check_detail= (TextView) findViewById(R.id.check_detail);
        mNotificationContent = (TextView) findViewById(R.id.notificationContent);
        notificationNumber = (LinearLayout) findViewById(R.id.notificationNumber);
        mainView= (RelativeLayout) findViewById(R.id.mainView);
        data_view= (ScrollView) findViewById(R.id.data_view);
        mToolbar.setTitle(getResources().getString(R.string.notification));
        setSupportActionBar(mToolbar);
        notifyId=getIntent().getIntExtra("notifyId",0);
        showLoadingView();
        httpRequest();
        check_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotificationDetailActivity.this,NotificationReadNumberActivity.class).putExtra("notifyId",notifyId));
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void httpRequest() {
        new HttpGetBuilder(this).setUrl(UrlsNew.GET_NOTIFY_DEATIL).setClassObj(NotificationDetailBean.class).setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                hideLoadingView();
                data_view.setVisibility(View.VISIBLE);
                NotificationDetailBean detailBean= (NotificationDetailBean) resultBean;
                mNotificationName.setText(detailBean.getItem().getTitle());
                mNotificationContent.setText(detailBean.getItem().getContent());
                mSendName.setText(getResources().getString(R.string.sender_name)+detailBean.getItem().getProfile().getNickname());
                mSendTime.setText(getResources().getString(R.string.send_time)+DateUtil.getDateAndMinute(detailBean.getItem().getUpdate_time()));
                readNum.setText(detailBean.getItem().getRead_num()+"");
                unReadNum.setText(detailBean.getItem().getNumber()+"");
                if(new SharedPreferencesUtil(NotificationDetailActivity.this).getUid().equals(detailBean.getItem().getProfile().getId()+"")){
                    notificationNumber.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {
                ToastUtils.makeText(NotificationDetailActivity.this,msg);
                data_view.setVisibility(View.GONE);
                hideLoadingView();
                showLoadFailView(mainView);
                setOnLoadFailClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideLoadFailView();
                        showLoadingView();
                        httpRequest();
                    }
                });
            }
        }).setPath(notifyId+"").build();
    }


}
