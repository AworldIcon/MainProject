package com.coder.kzxt.service.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.service.beans.ServiceFormResult;
import com.coder.kzxt.service.beans.ServiceMemberResult;
import com.coder.kzxt.utils.ToastUtils;


/**
 * 就业登记表3
 */

public class FindJobActivity3 extends BaseActivity implements HttpCallBack
{


    private Toolbar mToolbarView;
    private Button button;
    private TextView introduce;
    private ImageView statusImage;

    private ServiceMemberResult.ItemBean itemBean;

    public static void gotoActivity(Context context, ServiceMemberResult.ItemBean serviceBean)
    {
        ((BaseActivity) context).startActivityForResult(new Intent(context, FindJobActivity3.class)
                .putExtra("serviceBean", serviceBean), 3000
        );
    }

    private void initVariable()
    {
        itemBean = (ServiceMemberResult.ItemBean) getIntent().getSerializableExtra("serviceBean");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        //获取从其他页面获取的变量 getIntent
        initVariable();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_job3);
        //初始化 view findviewbyid
        initFindViewById();
        //响应事件click
        initClick();

    }

    private void initFindViewById()
    {
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        button = (Button) findViewById(R.id.button);
        introduce = (TextView) findViewById(R.id.introduce);
        statusImage = (ImageView) findViewById(R.id.statusImage);

        mToolbarView.setTitle(getResources().getString(R.string.find_job_form));
        setSupportActionBar(mToolbarView);
    }

    private void initClick()
    {

//                "job_register": "就业登记状态：0未开启 1等待 2通过 3失败",

        if (itemBean.getJob_register() == 0)
        {
            FindJobActivity1.gotoActivity(FindJobActivity3.this, null, itemBean.getId());
            finish();

        } else if (itemBean.getJob_register() == 1)
        {
            button.setVisibility(View.GONE);
            statusImage.setImageResource(R.drawable.service_status_wait);
            introduce.setText("就业登记表已提交，等待审核结果...");
        } else if (itemBean.getJob_register() == 2)
        {
            button.setVisibility(View.GONE);
            statusImage.setImageResource(R.drawable.service_status_passed);
            introduce.setText("就业登记表审核通过");
        } else if (itemBean.getJob_register() == 3)
        {
            button.setVisibility(View.VISIBLE);
            introduce.setText("就业登记表审核未通过,请重新填写");
            statusImage.setImageResource(R.drawable.service_status_unpassed);
        }


        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                new HttpGetBuilder(FindJobActivity3.this)
                        .setUrl(UrlsNew.SERVICE_JOB_REGISTER)
                        .setHttpResult(FindJobActivity3.this)
                        .setClassObj(ServiceFormResult.class)
                        .setPath(itemBean.getId() + "")
                        .build();
            }
        });

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

        ServiceFormResult itemBean = (ServiceFormResult) resultBean;
        FindJobActivity1.gotoActivity(FindJobActivity3.this, itemBean.getItem(), 3);
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {
        ToastUtils.makeText(FindJobActivity3.this, msg);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3000 && resultCode == 3000)
        {
            setResult(3000);
            finish();
        }

    }
}
