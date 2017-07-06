package com.coder.kzxt.classe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.coder.kzxt.classe.adapter.MyCreateClassAdapter;
import com.coder.kzxt.classe.beans.MyCreateClass;
import com.coder.kzxt.classe.mInterface.OnClassItemInterface;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;

import java.util.ArrayList;

/**
 * Created by wangtingshun on 2017/6/6.
 * 学生页面-我的班级
 */

public class MyClassStudentActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,OnClassItemInterface {


    private Toolbar mToolbar;
    private SharedPreferencesUtil spu;
    private MyPullSwipeRefresh pullSwipeRefresh;
    private MyCreateClassAdapter classSutdentAdapter; //创建班级的adapter
    private MyPullRecyclerView myPullRecyclerView;
    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private RelativeLayout myLayout;
    private Button loadFailBtn;//加载失败
    private int pageIndex = 1;
    private int pageNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_my_class_layout);
        spu = new SharedPreferencesUtil(this);
        initView();
        initListener();
        requestData();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.mToolBar);
        mToolbar.setTitle("我的班级");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        myPullRecyclerView = (MyPullRecyclerView) findViewById(R.id.myPullRecyclerView);
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout)findViewById(R.id.load_fail_layout);
        no_info_layout = (LinearLayout)findViewById(R.id.no_info_layout);
        myLayout = (RelativeLayout) findViewById(R.id.rl_layout);
        loadFailBtn = (Button) findViewById(R.id.load_fail_button);
    }

    private void initListener() {
        pullSwipeRefresh.setOnRefreshListener(this);
        loadFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPage();
            }
        });
    }

    /**
     * 请求数据
     */
    private void requestData() {
         loadingPage();
         getStudentMyClass();
    }

    private void getStudentMyClass() {
                new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_GROUP_MY)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        pullSwipeRefresh.setRefreshing(false);
                        MyCreateClass calssBean = (MyCreateClass) resultBean;
                        MyCreateClass.Paginate paginate = calssBean.getPaginate();
                        if (paginate != null) {
                            pageNum = paginate.getPageNum();
                            pageIndex = paginate.getCurrentPage();
                        }
                        ArrayList<MyCreateClass.CreateClassBean> items = calssBean.getItems();
                        if (items != null && items.size() > 0) {
                            visibleData();
                            adapterData(items);
                        } else {
                            noDataPage();
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(MyClassStudentActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(MyClassStudentActivity.this, myLayout);
                        }
                        pullSwipeRefresh.setRefreshing(false);
                        loadFailPage();
                    }
                })
                .setClassObj(MyCreateClass.class)
                .addQueryParams("page", String.valueOf(pageIndex))
                .build();
    }

    private void adapterData(ArrayList<MyCreateClass.CreateClassBean> items) {
        classSutdentAdapter = new MyCreateClassAdapter(this,items);
        myPullRecyclerView.setAdapter(classSutdentAdapter);
        classSutdentAdapter.setOnClassItemClickListener(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //返回
                finish();
                break;
            case R.id.right_item:  //查找
                findClass();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 查找班级
     */
    private void findClass() {
        Intent intent = new Intent(this,FindClassActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.right_menu,menu);
        MenuItem classItem = menu.findItem(R.id.right_item);
        classItem.setTitle("查找");
        MenuItemCompat.setShowAsAction(classItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onRefresh() {
        getStudentMyClass();
    }

    /**
     * 暂无数据
     */
    private void noDataPage() {
        load_fail_layout.setVisibility(View.GONE);
        pullSwipeRefresh.setVisibility(View.GONE);
        jiazai_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.VISIBLE);
    }

    /**
     * 加载失败页
     */
    private void loadFailPage() {
        load_fail_layout.setVisibility(View.VISIBLE);
        pullSwipeRefresh.setVisibility(View.GONE);
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
        pullSwipeRefresh.setVisibility(View.VISIBLE);
    }

    /**
     * 显示加载页
     */
    private void loadingPage() {
        jiazai_layout.setVisibility(View.VISIBLE);
        pullSwipeRefresh.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
    }

    @Override
    public void onClassItemClick(MyCreateClass.CreateClassBean item) {
        Intent intent = new Intent(MyClassStudentActivity.this,ClassDetailActivity.class);
        intent.putExtra("class",item);
        startActivityForResult(intent,1);
    }

}
