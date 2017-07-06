package com.coder.kzxt.service.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.service.adapter.ServiceMainDelegate;
import com.coder.kzxt.service.beans.ServiceBean;
import com.coder.kzxt.service.beans.ServiceResult;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Cread by MaShiZhao on 2017/3/15
 * 服务列表
 */

public class ServiceMainActivity extends BaseActivity implements HttpCallBack
{

    private Toolbar toolbar;
    private RelativeLayout mainView;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;

    private PullRefreshAdapter<ServiceBean> pullRefreshAdapter;
    private ServiceMainDelegate messageMainDelegate;
    private List<ServiceBean> mData;
    private String title;

    public static void gotoActivity(Context mContext, String title)
    {
        mContext.startActivity(new Intent(mContext, ServiceMainActivity.class).putExtra("title",title));
    }

    private void initVariable()
    {
        title = getIntent().getStringExtra("title");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //获取从其他页面获取的变量 getIntent
        initVariable();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_swiperefresh);
        //初始化 view findviewbyid
        initFindViewById();
        //需要初始化的数据
        initData();
        //响应事件click
        initClick();
        //网络请求
        showLoadingView();
        requestData();

    }

    private void requestData()
    {
        new HttpGetBuilder(ServiceMainActivity.this)
                .setUrl(UrlsNew.SERVICE)
                .setHttpResult(ServiceMainActivity.this)
                .setClassObj(ServiceResult.class)
                .addQueryParams("pageSize", "20")
                .addQueryParams("state", "2")
                .addQueryParams("page", pullRefreshAdapter.getPageIndex() + "")
                .build();
    }


    private void initFindViewById()
    {
        mainView = (RelativeLayout) findViewById(R.id.mainView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        myPullRecyclerView = (MyPullRecyclerView) findViewById(R.id.myPullRecyclerView);
    }

    private void initData()
    {

        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        mData = new ArrayList<>();
        messageMainDelegate = new ServiceMainDelegate(this);
        pullRefreshAdapter = new PullRefreshAdapter<ServiceBean>(this, mData, messageMainDelegate);
        myPullRecyclerView.setAdapter(pullRefreshAdapter);
        pullRefreshAdapter.setSwipeRefresh(myPullSwipeRefresh);

    }

    private void initClick()
    {

        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                pullRefreshAdapter.resetPageIndex();
                requestData();
            }
        });

        myPullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener()
        {
            @Override
            public void addMoreListener()
            {
                pullRefreshAdapter.addPageIndex();
                requestData();
            }
        });

        pullRefreshAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                ServiceDetailActivity.gotoActivity(ServiceMainActivity.this, mData.get(position));
            }
        });

    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {
        hideLoadingView();

        ServiceResult signListResult = (ServiceResult) resultBean;
        pullRefreshAdapter.setTotalPage(signListResult.getPaginate().getPageNum());
        pullRefreshAdapter.setPullData(signListResult.getItems());

        if (pullRefreshAdapter.getShowCount() == 0)
            showEmptyView(mainView);

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {
        hideLoadingView();
        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
        {
            //重新登录
            NetworkUtil.httpRestartLogin(ServiceMainActivity.this, mainView);
        } else
        {
            NetworkUtil.httpNetErrTip(ServiceMainActivity.this, mainView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
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

            case R.id.menu_search:
                SearchServiceActivity.gotoActivity(ServiceMainActivity.this);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
