package com.coder.kzxt.classe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.classe.beans.OrganizationBean;
import com.coder.kzxt.classe.delegate.OneLevelItemDelegate;
import com.coder.kzxt.classe.mInterface.ObserverListener;
import com.coder.kzxt.main.activity.MainActivity;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.TreeRecyclerViewAdapter;
import com.coder.kzxt.recyclerview.adapter.TreeRecyclerViewType;
import com.coder.kzxt.recyclerview.decoration.DividerItemDecoration;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.views.RecyclerViewHeader;

import java.util.ArrayList;

/**
 * Created by wangtingshun on 2017/6/19.
 * 组织架构
 */

public class OrganizationActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,ObserverListener {

    private Toolbar mToolbar;
    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private Button loadFailBtn;
    private RelativeLayout mylayout;
    private MainActivity mainActivity;
    private RecyclerView recyclerView;
    private RecyclerViewHeader recyclerHeader;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private TreeRecyclerViewAdapter treeRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organization_structure_layout);
        initView();
        initListener();
        requestData();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.mToolBar);
        mToolbar.setTitle(getResources().getString(R.string.organizational));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        myPullSwipeRefresh.setOnRefreshListener(this);
        jiazai_layout = (LinearLayout)findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout)findViewById(R.id.load_fail_layout);
        no_info_layout = (LinearLayout) findViewById(R.id.no_info_layout);
        loadFailBtn = (Button)findViewById(R.id.load_fail_button);
        mylayout = (RelativeLayout)findViewById(R.id.course_fragment);
        recyclerView = (RecyclerView)findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerHeader = (RecyclerViewHeader)findViewById(R.id.header_layout);
        recyclerHeader.attachTo(recyclerView);
        recyclerHeader.setVisibility(View.GONE);
        //去除recyclerView默认刷新动画
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }


    private void initListener() {
        //搜索
        recyclerHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //重新加载数据
        loadFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestData();
            }
        });
    }

    /**
     * 获取组织架构数据
     */
    private void requestData() {
        loadingPage();
        getOrganizationRequest();
    }

    private void getOrganizationRequest() {
        new HttpGetBuilder(this)
        .setUrl(UrlsNew.GET_ORGANICATION)
        .setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                visibleData();
                myPullSwipeRefresh.setRefreshing(false);
                ArrayList<OneLevelItemDelegate> treeBean = new ArrayList<>();
                OrganizationBean bean = (OrganizationBean) resultBean;
                ArrayList<OrganizationBean.OneLevelBean> departments = bean.getItems();
                if (departments != null && departments.size() > 0) {
                    for (int i = 0; i < departments.size(); i++) {
                        treeBean.add(new OneLevelItemDelegate(departments.get(i)));
                    }
                    treeRecyclerViewAdapter = new TreeRecyclerViewAdapter<>(OrganizationActivity.this,
                            treeBean, TreeRecyclerViewType.SHOW_COLLAPSE_CHILDS);
                    recyclerView.setAdapter(treeRecyclerViewAdapter);
                } else {
                    //无数据
                    noDataPage();
                }
            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {
                myPullSwipeRefresh.setRefreshing(false);
                if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                    NetworkUtil.httpRestartLogin(mainActivity, mylayout);
                } else {
                    NetworkUtil.httpNetErrTip(mainActivity, mylayout);
                }
                loadFailPage();
            }
        })
        .addQueryParams("page","1")
        .addQueryParams("pageSize","20")
        .setClassObj(OrganizationBean.class)
        .build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //返回
                finish();
                break;
            case R.id.right_item:  //完成

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 显示加载页
     */
    private void loadingPage() {
        jiazai_layout.setVisibility(View.VISIBLE);
        myPullSwipeRefresh.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
    }

    /**
     * 暂无数据
     */
    private void noDataPage() {
        load_fail_layout.setVisibility(View.GONE);
        myPullSwipeRefresh.setVisibility(View.GONE);
        jiazai_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.VISIBLE);
    }

    /**
     * 加载失败页
     */
    private void loadFailPage() {
        load_fail_layout.setVisibility(View.VISIBLE);
        myPullSwipeRefresh.setVisibility(View.GONE);
        jiazai_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
    }

    /**
     * 显示数据
     */
    private void visibleData() {
        jiazai_layout.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
        myPullSwipeRefresh.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onRefresh() {
        getOrganizationRequest();
    }

    @Override
    public void observerUpData(String id,String content) {
        Intent intent = new Intent();
        intent.putExtra("content",content);
        intent.putExtra("categoryId",id);
        setResult(8,intent);
        finish();
    }
}
