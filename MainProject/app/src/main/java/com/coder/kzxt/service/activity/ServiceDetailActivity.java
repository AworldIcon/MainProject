package com.coder.kzxt.service.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.adapter.PagerSlidingTabStripAdapter;
import com.coder.kzxt.service.beans.ServiceBean;
import com.coder.kzxt.service.beans.ServiceMemberResult;
import com.coder.kzxt.service.fragment.ServiceCertifyFragment;
import com.coder.kzxt.service.fragment.ServiceDetailFragment;
import com.coder.kzxt.service.fragment.ServiceTestFragment;
import com.coder.kzxt.service.fragment.ServiceVideoFragment;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.ToastUtils;

import java.util.ArrayList;


/**
 * 服务详情
 */

public class ServiceDetailActivity extends BaseActivity implements HttpCallBack
{

    private static final int CODE_MEMBER = 1002;

    private View mainView;
    private Toolbar mToolbarView;
    private TabLayout mTabLayout;
    private Button bottomButton;
    private ViewPager pager;
    private PagerSlidingTabStripAdapter tabStripAdapter;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private ArrayList<String> userChannelList = new ArrayList<String>();
    private ServiceDetailFragment fragment1;
    private ServiceTestFragment fragment2;
    private ServiceVideoFragment fragment3;
    private ServiceCertifyFragment fragment4;

    //    private String serviceName, serviceId;
    private ServiceBean serviceBean;

    public static void gotoActivity(Context mContext, ServiceBean serviceBean)
    {
        mContext.startActivity(new Intent(mContext, ServiceDetailActivity.class)
                .putExtra("serviceBean", serviceBean)
        );
    }

    private void initVariable()
    {
        serviceBean = (ServiceBean) getIntent().getSerializableExtra("serviceBean");
//        serviceId = getIntent().getStringExtra("serviceId");
//        serviceName = getIntent().getStringExtra("serviceName");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        //获取从其他页面获取的变量 getIntent
        initVariable();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        //初始化 view findviewbyid
        initFindViewById();
        //响应事件click
        initClick();
        //网络请求
        showLoadingView();
        requestData();

    }

    private void initFindViewById()
    {
        mainView = findViewById(R.id.mainView);
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.mtablyout);
        pager = (ViewPager) findViewById(R.id.mpager);
        bottomButton = (Button) findViewById(R.id.bottomButton);

        mToolbarView.setTitle(serviceBean.getTitle());
        setSupportActionBar(mToolbarView);


        tabStripAdapter = new PagerSlidingTabStripAdapter(getSupportFragmentManager(), userChannelList, fragments);
        pager.setAdapter(tabStripAdapter);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        mTabLayout.setupWithViewPager(pager);
        pager.setOffscreenPageLimit(fragments.size());
    }

    private int memberedId = -1;

    private void initClick()
    {
        bottomButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ToastUtils.makeText(ServiceDetailActivity.this,"App 暂无法加入服务，请前往网页端操作");
            }
        });
    }

    private void requestData()
    {
        new HttpGetBuilder(ServiceDetailActivity.this)
                .setUrl(UrlsNew.SERVICE_MEMBERED)
                .setHttpResult(ServiceDetailActivity.this)
                .setClassObj(ServiceMemberResult.class)
                .setPath(serviceBean.getService_id() + "")
                .setRequestCode(CODE_MEMBER)
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
        hideLoadingView();
        ServiceMemberResult serviceMemberResult = (ServiceMemberResult) resultBean;
        initFragment(serviceMemberResult.getItem());
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {
        hideLoadingView();
        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
        {
            //重新登录
            NetworkUtil.httpRestartLogin(ServiceDetailActivity.this, mainView);
        } else
        {
            NetworkUtil.httpNetErrTip(ServiceDetailActivity.this, mainView);
        }
        initFragment(null);

    }

    private void initFragment(ServiceMemberResult.ItemBean item)
    {

        if (item != null)
        {
            memberedId = item.getId();
        }

        fragment1 = (ServiceDetailFragment) ServiceDetailFragment.newInstance(serviceBean, item == null ? 0 : item.getService_class_id());
        fragment2 = (ServiceTestFragment) ServiceTestFragment.newInstance(serviceBean.getService_id() + "", memberedId);
        fragment3 = (ServiceVideoFragment) ServiceVideoFragment.newInstance(serviceBean, memberedId);


        fragments.clear();// 清空
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);

        userChannelList.clear();
        userChannelList.add(getResources().getString(R.string.introduce));
        userChannelList.add(getResources().getString(R.string.service_test));
        userChannelList.add(getResources().getString(R.string.service_video));

        // 如果非空则加入成功
        if (item != null)
        {
            fragment4 = (ServiceCertifyFragment) ServiceCertifyFragment.newInstance(item);
            fragments.add(fragment4);

            // 0 报名考试 1 就业服务 2 认证服务
            if (item.getMyType() == 0)
            {
                userChannelList.add(getResources().getString(R.string.sign_up_test));
            } else if (item.getMyType() == 2)
            {
                userChannelList.add(getResources().getString(R.string.service_certify));
            } else
            {
                userChannelList.add(getResources().getString(R.string.service_find_job));
            }
            bottomButton.setVisibility(View.GONE);
        }
        tabStripAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        //登录返回刷新
        if (requestCode == 1000 && resultCode == 2)
        {
            requestData();
        } else if (requestCode == 2000)
        {
            //作业列表的刷新
            if (fragment4 != null)
            {
                fragment4.requestData();
            }
        } else if (requestCode == 3000 && resultCode == 3000)
        {
            //就业登记表的回调
            if (fragment4 != null)
            {
                fragment4.setFormWait();
            }
        }
    }
}
