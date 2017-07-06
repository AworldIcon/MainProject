package com.coder.kzxt.sign.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.sign.adapter.TagDelegate;
import com.coder.kzxt.sign.beans.SignBean;
import com.coder.kzxt.sign.beans.SignStudentResult;
import com.coder.kzxt.sign.utils.SignUtils;
import com.coder.kzxt.utils.DateUtil;


/**
 * 签到详情 学生端
 *
 * @author pc
 */
public class SignDetailStudentActivity extends BaseActivity
{
    private Toolbar toolbar;
    private TextView Sign_start_time;
    private TextView Sign_end_time;
    private TextView courseName;
    private TextView Sign_time_length;
    private TextView Sign_range;
    private TextView info_sign_time_title;
    private TextView Sign_location;
    private TextView Teacher;
    private TextView record_status;
    private TextView Status;
    private TextView info_status;
    private TextView info_status_mark;
    private TextView info_sign_time;
    private LinearLayout recordLy;
    private MyRecyclerView myRecyclerView;

    private SignStudentResult.ItemsBean signBean;
    private String courseNameS;

    public static void gotoActivity(Context context, SignStudentResult.ItemsBean signBean, String courseName)
    {
        context.startActivity(new Intent(context, SignDetailStudentActivity.class).putExtra("bean", signBean).putExtra("courseName", courseName));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_detail_student);

        signBean = (SignStudentResult.ItemsBean) getIntent().getSerializableExtra("bean");
        courseNameS = getIntent().getStringExtra("courseName");
        initView();
        initData();
        initListener();
    }

    //控件
    private void initView()
    {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recordLy = (LinearLayout) findViewById(R.id.recordLy);
        Sign_start_time = (TextView) findViewById(R.id.sign_start_time);
        Sign_end_time = (TextView) findViewById(R.id.sign_end_time);
        courseName = (TextView) findViewById(R.id.courseName);
        Sign_time_length = (TextView) findViewById(R.id.sign_time_length);
        Sign_range = (TextView) findViewById(R.id.sign_range);
        info_sign_time_title = (TextView) findViewById(R.id.info_sign_time_title);
        Sign_location = (TextView) findViewById(R.id.sign_location);
        Teacher = (TextView) findViewById(R.id.teacher);
        record_status = (TextView) findViewById(R.id.record_status);
        Status = (TextView) findViewById(R.id.status);
        info_status = (TextView) findViewById(R.id.info_status);
        info_status_mark = (TextView) findViewById(R.id.info_status_mark);
        info_sign_time = (TextView) findViewById(R.id.info_sign_time);
        myRecyclerView = (MyRecyclerView) findViewById(R.id.myRecyclerView);
        toolbar.setTitle(getString(R.string.sign_detail));
        setSupportActionBar(toolbar);

    }

    private void initData()
    {
        SignBean sign = signBean.getSign();
        courseName.setText(courseNameS);
        Sign_start_time.setText(DateUtil.getDateAndSecond(sign.getBegin_time()));
        Sign_end_time.setText(DateUtil.getDateAndSecond(sign.getEnd_time()));
        Sign_time_length.setText(DateUtil.getTime(sign.getTime()));
        Sign_range.setText(SignUtils.getRangeString(sign.getRange()));
        Sign_location.setText(sign.getAddress());

        if (signBean.getRecord().getNickname() != null)
        {
            SignStudentResult.ItemsBean.RecordBean recordBean = signBean.getRecord();
            recordLy.setVisibility(View.VISIBLE);
            Teacher.setText(recordBean.getNickname() + "老师编辑于" + DateUtil.getDateAndSecond(recordBean.getUpdate_time()));

            if (recordBean.getStatus() == 1)
            {
                Status.setText(getString(R.string.signed));
            } else
            {
                Status.setText(getString(R.string.signed_not));
            }

            if (signBean.getIsRecord())
            {
                Status.setTextColor(ContextCompat.getColor(SignDetailStudentActivity.this, R.color.first_theme));
            } else
            {
                Status.setTextColor(ContextCompat.getColor(SignDetailStudentActivity.this, R.color.font_gray));
            }

            if (recordBean.getTags().size() != 0)
            {
                myRecyclerView.setVisibility(View.VISIBLE);
                myRecyclerView.setAdapter(new BaseRecyclerAdapter(SignDetailStudentActivity.this, recordBean.getTags(), new TagDelegate()));
            } else
            {
                myRecyclerView.setVisibility(View.GONE);

            }
        } else
        {
            recordLy.setVisibility(View.GONE);
        }


        if (signBean.getInfo().getStatus() == 1)
        {
            info_status.setText(getString(R.string.signed));
            info_sign_time_title.setVisibility(View.VISIBLE);
            info_sign_time.setVisibility(View.VISIBLE);
            info_sign_time.setText(DateUtil.getDateAndSecond(signBean.getInfo().getSign_time()));

        } else
        {
            info_status.setText(getString(R.string.signed_not));
            info_sign_time_title.setVisibility(View.GONE);
            info_sign_time.setVisibility(View.GONE);
        }


        if (signBean.getInfo().getIs_offline() == 1 && signBean.getStatus() == 1)
        {
            info_status_mark.setText(getResources().getString(R.string.offline));
        } else if (signBean.getIs_rang() == 0 && signBean.getStatus() == 1)
        {
            info_status_mark.setText(getResources().getString(R.string.not_in_scope));
        } else
        {
            info_status_mark.setText("");
        }

    }

    //监听器
    private void initListener()
    {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}