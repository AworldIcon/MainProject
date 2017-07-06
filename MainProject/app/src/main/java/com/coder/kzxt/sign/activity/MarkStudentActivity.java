package com.coder.kzxt.sign.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.sign.adapter.MarkStudentDelegate;
import com.coder.kzxt.sign.beans.SignInfoResult;
import com.coder.kzxt.sign.beans.SignTagBean;
import com.coder.kzxt.sign.beans.SignTagResult;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by MaShiZhao on 2017年05月24日16:11:01
 * 添加备注
 */

public class MarkStudentActivity extends BaseActivity implements HttpCallBack
{

    private android.support.v7.widget.Toolbar toolbar;
    private View mainView;
    private MyRecyclerView myRecyclerView;
    private RadioButton signed, unSigned;
    private RadioGroup radioGroup;

    private SignInfoResult.SignInfoBean signInfoBean;
    private String signId;

    public static void gotoActivity(BaseActivity context, SignInfoResult.SignInfoBean signInfoBean, String signId)
    {
        context.startActivityForResult(new Intent(context, MarkStudentActivity.class).putExtra("bean", signInfoBean)
                .putExtra("signId", signId), 1000);
    }

    private void initVariable()
    {
        signInfoBean = (SignInfoResult.SignInfoBean) getIntent().getSerializableExtra("bean");
        signId = getIntent().getStringExtra("signId");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //获取从其他页面获取的变量 getIntent
        initVariable();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_student);
        //初始化 view findviewbyid
        initFindViewById();
        //需要初始化的数据
        initData();
        //响应事件click
        initClick();
        //网络请求
        httpRequest();
    }

    private TextView signedNotice;
    private TextView unSignedNotice;

    private void initFindViewById()
    {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle(getResources().getString(R.string.mark));
        setSupportActionBar(toolbar);
        mainView = findViewById(R.id.mainView);
        myRecyclerView = (MyRecyclerView) findViewById(R.id.myRecyclerView);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        signed = (RadioButton) findViewById(R.id.signed);
        unSigned = (RadioButton) findViewById(R.id.unSigned);

        ImageView head = (ImageView) findViewById(R.id.head);
        TextView name = (TextView) findViewById(R.id.name);
        TextView mark = (TextView) findViewById(R.id.mark);
        TextView status = (TextView) findViewById(R.id.status);
        TextView status_mark = (TextView) findViewById(R.id.status_mark);
        TextView time = (TextView) findViewById(R.id.time);
        signedNotice = (TextView) findViewById(R.id.signedNotice);
        unSignedNotice = (TextView) findViewById(R.id.unSignedNotice);

        GlideUtils.loadCircleHeaderOfCommon(MarkStudentActivity.this, signInfoBean.getUser().getAvatar(), head);
        name.setText(signInfoBean.getUser().getNickname());
//        time.setText(DateUtil.getDateAndSecond(signInfoBean.getCreate_time()));

        if (signInfoBean.getStatus() == 0)
        {
            status.setText(getString(R.string.signed_not));
            radioGroup.check(unSigned.getId());

        } else
        {
            status.setText(getString(R.string.signed));
            radioGroup.check(signed.getId());
        }

        if (signInfoBean.getStatus() != signInfoBean.getStatus() || signInfoBean.getStatus() == 0)
        {
            status_mark.setText("");
        } else
        {
            if (signInfoBean.getSign().getIs_range() == 0)
            {
                status_mark.setText(getResources().getString(R.string.not_in_scope));
            } else if (signInfoBean.getSign().getIs_offline() == 1)
            {
                status_mark.setText(getResources().getString(R.string.offline));
            }
        }

    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.right_menu, menu);
        MenuItem item = menu.findItem(R.id.right_item);
        item.setTitle(getResources().getString(R.string.save));
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
                postRequest();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void httpRequest()
    {
        new HttpGetBuilder(MarkStudentActivity.this)
                .setUrl(UrlsNew.SIGN_RECORD)
                .setClassObj(SignTagResult.class)
                .setHttpResult(MarkStudentActivity.this)
                .build();
    }

    private void postRequest()
    {

        String statusString = radioGroup.getCheckedRadioButtonId() == signed.getId() ? "1" : "0";
        String tagsId = getTagsIds();
        if (statusString.equals(signInfoBean.getStatus() + "") && tagsId.equals(signInfoBean.getTagsIds()))
        {
            ToastUtils.makeText(MarkStudentActivity.this, "您没有变更学员签到信息!");
            return;
        }
        new HttpPostBuilder(MarkStudentActivity.this)
                .setUrl(UrlsNew.SIGN_RECORD)
                .setClassObj(BaseBean.class)
                .setHttpResult(MarkStudentActivity.this)
                .addBodyParam("sign_id", signId)
                .addBodyParam("student_id", signInfoBean.getUser().getId() + "")
                .addBodyParam("status", statusString)
                .addBodyParam("tag_id", tagsId)
                .addBodyParam("course_id", signInfoBean.getSign().getCourse_id())
                .addBodyParam("class_id", signInfoBean.getSign().getClass_id())
                .setRequestCode(1)
                .build();
    }

    public String getTagsIds()
    {
        StringBuilder stringBuffer = new StringBuilder();
        for (SignTagBean signTagBean : tagBeanList)
        {
            if (signTagBean.getSelect())
                stringBuffer.append(signTagBean.getId()).append(",");

        }
        return stringBuffer.toString();
    }

    private void initData()
    {
    }

    private void initClick()
    {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId)
            {

                if (checkedId == signed.getId())
                {
                    if (signInfoBean.getStatus() == 0)
                    {
                        signedNotice.setVisibility(View.VISIBLE);

                    } else
                    {
                        unSignedNotice.setVisibility(View.GONE);
                    }

                } else if (checkedId == unSigned.getId())
                {

                    if (signInfoBean.getStatus() == 1)
                    {
                        unSignedNotice.setVisibility(View.VISIBLE);

                    } else
                    {
                        signedNotice.setVisibility(View.GONE);
                    }
                }

            }
        });
    }


    private List<SignTagBean> tagBeanList = new ArrayList<>();

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {
        if (requestCode == 1)
        {
            ToastUtils.makeText(MarkStudentActivity.this, "修改成功");
            setResult(1000);
            finish();
        } else
        {

            SignTagResult signTagResult = (SignTagResult) resultBean;
            if (signTagResult.getItems() == null) return;
            tagBeanList = signTagResult.getItems();
            myRecyclerView.setAdapter(new BaseRecyclerAdapter(MarkStudentActivity.this, tagBeanList, new MarkStudentDelegate(signInfoBean)));

        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {
        hideLoadingView();
        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
        {
            //重新登录
            NetworkUtil.httpRestartLogin(MarkStudentActivity.this, mainView);
        } else
        {
            NetworkUtil.httpNetErrTip(MarkStudentActivity.this, mainView);
        }

    }
}
