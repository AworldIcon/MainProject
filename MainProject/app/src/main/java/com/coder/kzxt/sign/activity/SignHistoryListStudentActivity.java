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
import com.coder.kzxt.sign.adapter.SignHistoryListStudentDelegate;
import com.coder.kzxt.sign.beans.SignStudentResult;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Cread by MaShiZhao on 2017/3/15
 * 点名记录 列表
 */

public class SignHistoryListStudentActivity extends BaseActivity implements HttpCallBack
{

    private Toolbar toolbar;
    private RelativeLayout mainView;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;

    private PullRefreshAdapter<SignStudentResult.ItemsBean> pullRefreshAdapter;
    private SignHistoryListStudentDelegate messageMainDelegate;
    private List<SignStudentResult.ItemsBean> mData;

    private String courseId, classId, courseName;

    public static void gotoActivity(Context mContext, String courseId, String classId, String courseName)
    {
        mContext.startActivity(new Intent(mContext, SignHistoryListStudentActivity.class)
                .putExtra("courseId", courseId)
                .putExtra("courseName", courseName)
                .putExtra("classId", classId));
    }

    private void initVariable()
    {
        courseId = getIntent().getStringExtra("courseId");
        classId = getIntent().getStringExtra("classId");
        courseName = getIntent().getStringExtra("courseName");
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
        new HttpGetBuilder(SignHistoryListStudentActivity.this)
                .setUrl(UrlsNew.SIGN_STUDENT)
                .setHttpResult(SignHistoryListStudentActivity.this)
                .setClassObj(SignStudentResult.class)
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

        toolbar.setTitle(getResources().getString(R.string.signin_record));
        setSupportActionBar(toolbar);

        mData = new ArrayList<>();
        messageMainDelegate = new SignHistoryListStudentDelegate(this, courseName);
        pullRefreshAdapter = new PullRefreshAdapter<SignStudentResult.ItemsBean>(this, mData, messageMainDelegate);
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
                SignDetailStudentActivity.gotoActivity(SignHistoryListStudentActivity.this,
                        mData.get(position), courseName);
            }
        });

    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {
        hideLoadingView();

        SignStudentResult signListResult = (SignStudentResult) resultBean;
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
            NetworkUtil.httpRestartLogin(SignHistoryListStudentActivity.this, mainView);
        } else
        {
            NetworkUtil.httpNetErrTip(SignHistoryListStudentActivity.this, mainView);
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
