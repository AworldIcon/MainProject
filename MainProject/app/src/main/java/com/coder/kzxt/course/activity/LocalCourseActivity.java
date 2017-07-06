package com.coder.kzxt.course.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.app.http.HttpCallBack;
import com.app.http.builders.BaseBuilder;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.course.adapter.LocalCourseHorizontalDelegate;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.main.beans.CourseMoreBean;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.app.http.UrlsNew;

import java.util.ArrayList;
import java.util.List;


/**
 * AppMore
 *
 * @author pc
 */

public class LocalCourseActivity extends BaseActivity implements HttpCallBack
{

    private Toolbar toolbar;
    private RelativeLayout mainView;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;

    private LocalCourseHorizontalDelegate detailDelegate;
    private PullRefreshAdapter<CourseMoreBean.ItemsBean> pullRefreshAdapter;

    //本页接收数据
     private List<CourseMoreBean.ItemsBean> mData;

    private SharedPreferencesUtil preferencesUtil;
    private String model_key,style;
    private String title;


    private void initVariable()
    {

//         publicCourse = getIntent().getStringExtra("publicCourse");

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
        initListener();
        //网络请求
        showLoadingView();
        requestData();
    }

    private void initFindViewById()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.pics_menu));
        setSupportActionBar(toolbar);
        model_key=getIntent().getStringExtra("model_key")!=null?getIntent().getStringExtra("model_key"):"";
        title=getIntent().getStringExtra("title")!=null?getIntent().getStringExtra("title"):"";
        style=getIntent().getStringExtra("style")!=null?getIntent().getStringExtra("style"):"";
        getSupportActionBar().setTitle(title);
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        myPullRecyclerView = (MyPullRecyclerView) findViewById(R.id.myPullRecyclerView);
        mainView = (RelativeLayout) findViewById(R.id.mainView);
    }

    private void initData()
    {

        preferencesUtil = new SharedPreferencesUtil(this);
        mData = new ArrayList<>();

        detailDelegate = new LocalCourseHorizontalDelegate(this,model_key,style);
        pullRefreshAdapter = new PullRefreshAdapter(this, mData, detailDelegate);
        myPullRecyclerView.setAdapter(pullRefreshAdapter);
        pullRefreshAdapter.setSwipeRefresh(myPullSwipeRefresh);

    }

    //监听器
    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

    }

    private void requestData()
    {
        BaseBuilder baseBuilder = new HttpGetBuilder(this)
                .setHttpResult(this)
                .setClassObj(CourseMoreBean.class)
                .setUrl(UrlsNew.APP_MORE)
                .addQueryParams("orderBy", "create_time desc")
                .addQueryParams("page", pullRefreshAdapter.getPageIndex() + "")
                .addQueryParams("pageSize", "20")
                .addQueryParams("type", model_key)
                .addQueryParams("style", style)
                .addQueryParams("role", TextUtils.isEmpty(preferencesUtil.getUserRole())?"2":preferencesUtil.getUserRole());
        baseBuilder.build();
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
        //SearchLocalCourseActivity.gotoActivity(this);
        Intent intent=new Intent(LocalCourseActivity.this,SearchLocalCourseActivity.class);
        intent.putExtra("model_key",model_key);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        hideLoadingView();

        if (Constants.RESTART_LOGIN == requestCode && resultCode == Constants.LOGIN_BACK)
        {
            requestData();

        } else if (requestCode == 4 && resultCode == 5)
        {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {

        hideLoadingView();
        CourseMoreBean questionDetailBean = (CourseMoreBean) resultBean;

        pullRefreshAdapter.setTotalPage(questionDetailBean.getPaginate().getPageNum());
        pullRefreshAdapter.setPullData(questionDetailBean.getItems());

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {
        hideLoadingView();
    }

}