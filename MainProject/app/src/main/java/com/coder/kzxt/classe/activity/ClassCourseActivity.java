package com.coder.kzxt.classe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.http.HttpGetOld;
import com.app.http.InterfaceHttpResult;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.adapter.ClassCourseAdapter;
import com.coder.kzxt.classe.beans.ClassCourseResult;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.utils.Urls;
import com.coder.kzxt.video.activity.VideoViewActivity;

import java.util.ArrayList;

/**
 * 班级课程
 * Created by wangtinshun on 2017/3/14.
 */

public class ClassCourseActivity extends BaseActivity implements ClassCourseAdapter.OnItemClickInterface,
        SwipeRefreshLayout.OnRefreshListener{

    private SharedPreferencesUtil spu;
    private ClassCourseAdapter adapter;
    /**
     * 班级id
     */
    private String classId;
    /**
     * 加载进度匡
     */
    private LinearLayout loadLayout;
    /**
     * 暂无数据
     */
    private LinearLayout no_info_layout;
    /**
     * 加载失败
     */
    private LinearLayout load_fail_view;
    /**
     * 重新加载按钮
     */
    private Button reLoadButton;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView recyclerView;
    private RelativeLayout myLayout;
    private Toolbar mToolBar;
    private boolean isRefresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_course);
        classId = getIntent().getStringExtra("classId");
        spu = new SharedPreferencesUtil(this);
        initView();
        setListener();
        startAsyncData(classId);
    }

    /**
     * 初始化view
     */
    private void initView() {
        loadLayout = (LinearLayout) findViewById(R.id.jiazai_layout);
        no_info_layout = (LinearLayout) findViewById(R.id.no_info_layout);
        load_fail_view = (LinearLayout) findViewById(R.id.load_fail_layout);
        reLoadButton = (Button) findViewById(R.id.load_fail_button);
        myLayout = (RelativeLayout) findViewById(R.id.my_layout);
        recyclerView = (MyPullRecyclerView) findViewById(R.id.recyclerView);
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitle(getResources().getString(R.string.class_course));
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 设置监听
     */
    private void setListener() {
        reLoadButton.setOnClickListener(loadListener);
        myPullSwipeRefresh.setOnRefreshListener(ClassCourseActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
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
     * 获取数据
     *
     * @param classId
     */
    private void startAsyncData(String classId) {
        if(!isRefresh){
            loadingLayout();
        }
        new HttpGetOld(this, this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                visibleData();
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
                    myPullSwipeRefresh.setRefreshing(false);
                    ClassCourseResult  courseResult = (ClassCourseResult) baseBean;
                    ArrayList<ClassCourseResult.ClassCourseBean> courseList = courseResult.getData();
                    if(courseList != null && courseList.size() > 0){
                        adapterData(courseList);
                    } else {
                        noDataPage();
                    }
                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                    NetworkUtil.httpNetErrTip(ClassCourseActivity.this, myLayout);
                    loadFailPage();
                } else {
                    errProcess(msg);
                }
            }
        }, ClassCourseResult.class, Urls.GET_CLASS_COURSE_ACTION, "1", "20",classId).excute(1000);
    }

    private void adapterData(ArrayList<ClassCourseResult.ClassCourseBean> courseBeen) {
        adapter = new ClassCourseAdapter(ClassCourseActivity.this, courseBeen);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(ClassCourseActivity.this);
    }

    private void errProcess(String msg) {
        loadFailPage();
        ToastUtils.makeText(ClassCourseActivity.this,msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * 加载对话框
     */
    private void loadingLayout() {
        loadLayout.setVisibility(View.VISIBLE);
        no_info_layout.setVisibility(View.GONE);
        load_fail_view.setVisibility(View.GONE);
        myPullSwipeRefresh.setVisibility(View.GONE);
    }

    /**
     * 暂无数据
     */
    private void noDataPage() {
        no_info_layout.setVisibility(View.VISIBLE);
        myPullSwipeRefresh.setVisibility(View.GONE);
        load_fail_view.setVisibility(View.GONE);
        loadLayout.setVisibility(View.GONE);
    }

    /**
     * 网络加载失败
     */
    private void loadFailPage() {
        load_fail_view.setVisibility(View.VISIBLE);
        myPullSwipeRefresh.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
        loadLayout.setVisibility(View.GONE);
    }

    /**
     * 显示数据
     */
    private void visibleData() {
        no_info_layout.setVisibility(View.GONE);
        myPullSwipeRefresh.setVisibility(View.VISIBLE);
        load_fail_view.setVisibility(View.GONE);
        loadLayout.setVisibility(View.GONE);
    }

    /**
     *打开系统设置
     */
    private void openSetting() {
        if (Constants.API_LEVEL_11) {
            startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
        } else {
            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
    }

    /**
     * 重新加载
     */
    public View.OnClickListener loadListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startAsyncData(classId);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        startAsyncData(classId);
    }

    /**
     * 跳转到播放器
     * @param courseBean
     */
    @Override
    public void onItemClick(ClassCourseResult.ClassCourseBean courseBean) {
        Intent intent = new Intent(ClassCourseActivity.this, VideoViewActivity.class);
        intent.putExtra("flag", Constants.ONLINE);
        intent.putExtra("treeid", courseBean.getCourseId());
        intent.putExtra("tree_name", courseBean.getTitle());
        intent.putExtra("pic", courseBean.getPic());
        intent.putExtra(Constants.IS_CENTER, "0");
        startActivity(intent);
    }

}
