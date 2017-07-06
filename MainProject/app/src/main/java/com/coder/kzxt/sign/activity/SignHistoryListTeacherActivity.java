package com.coder.kzxt.sign.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
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
import com.coder.kzxt.sign.adapter.SignHistoryListTeacherDelegate;
import com.coder.kzxt.sign.beans.SignBean;
import com.coder.kzxt.sign.beans.SignListResult;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Cread by MaShiZhao on 2017/3/15
 * 点名记录 列表
 */

public class SignHistoryListTeacherActivity extends BaseActivity implements HttpCallBack
{

    private Toolbar toolbar;
    private RelativeLayout mainView;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;

    private PullRefreshAdapter<SignBean> pullRefreshAdapter;
    private SignHistoryListTeacherDelegate messageMainDelegate;
    private List<SignBean> mData;

    private String courseId, classId;

    public static void gotoActivity(Context mContext, String courseId, String classId)
    {
        mContext.startActivity(new Intent(mContext, SignHistoryListTeacherActivity.class)
                .putExtra("courseId", courseId)
                .putExtra("classId", classId));
    }

    private void initVariable()
    {
        courseId = getIntent().getStringExtra("courseId");
        classId = getIntent().getStringExtra("classId");
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
        new HttpGetBuilder(SignHistoryListTeacherActivity.this)
                .setUrl(UrlsNew.SIGN_TEACHER)
                .setHttpResult(SignHistoryListTeacherActivity.this)
                .setClassObj(SignListResult.class)
                .addQueryParams("course_id", courseId)
                .addQueryParams("class_id", classId)
                .addQueryParams("pageSize", "20")
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

        toolbar.setTitle(getResources().getString(R.string.call_record));
        setSupportActionBar(toolbar);

        mData = new ArrayList<>();
        messageMainDelegate = new SignHistoryListTeacherDelegate(this);
        pullRefreshAdapter = new PullRefreshAdapter<SignBean>(this, mData, messageMainDelegate);
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
                SignDetailListTeacherActivity.gotoActivity(SignHistoryListTeacherActivity.this,
                        mData.get(position).getId(), courseId, classId);
            }
        });

    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {
        hideLoadingView();

        SignListResult signListResult = (SignListResult) resultBean;
        pullRefreshAdapter.setTotalPage(signListResult.getPaginate().getPageNum());
        pullRefreshAdapter.setPullData(signListResult.getItems());

        if(pullRefreshAdapter.getShowCount() == 0)
        {
            showEmptyView(mainView);
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {
        hideLoadingView();
        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
        {
            //重新登录
            NetworkUtil.httpRestartLogin(SignHistoryListTeacherActivity.this, mainView);
        } else
        {
            NetworkUtil.httpNetErrTip(SignHistoryListTeacherActivity.this, mainView);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == 1000)
        {
            pullRefreshAdapter.resetPageIndex();
            requestData();
        }
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

}
