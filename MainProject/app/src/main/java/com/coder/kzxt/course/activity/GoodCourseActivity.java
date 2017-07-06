package com.coder.kzxt.course.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.http.HttpGetOld;
import com.app.http.InterfaceHttpResult;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.course.adapter.GoodCourseAdapter;
import com.coder.kzxt.course.adapter.GoodCourseDelegate;
import com.coder.kzxt.course.beans.GoodCourseResult;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.Urls;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.coder.kzxt.activity.R.id.recycler;

public class GoodCourseActivity extends BaseActivity {
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView recyclerView;
    private String titleString;
    private TextView title;
    private TextView no_info_text;
    private Button load_fail_button;
    private RelativeLayout activity_good_course;
    private LinearLayout search_jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private List<GoodCourseResult.DataBean.CourseBean> dataList;
    private ArrayList<HashMap<String, String>> courseList;
    private GoodCourseDelegate goodCourseDelegate;
    private GoodCourseAdapter goodCourseAdapter;
    private String isCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        dataList = new ArrayList<>();
        initView();
        getCourseDetails();
    }

    private void initView() {
        search_jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        search_jiazai_layout.setVisibility(View.VISIBLE);
        activity_good_course = (RelativeLayout) findViewById(R.id.network_set_layout);
        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        load_fail_button = (Button) findViewById(R.id.load_fail_button);
        no_info_layout = (LinearLayout) findViewById(R.id.no_info_layout);
        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText(R.string.boutique_course);
        recyclerView = (MyPullRecyclerView) findViewById(recycler);
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        goodCourseDelegate=new GoodCourseDelegate(this,0);
        goodCourseAdapter=new GoodCourseAdapter(this,dataList,goodCourseDelegate);
        recyclerView.setAdapter(goodCourseAdapter);
        goodCourseAdapter.setSwipeRefresh(myPullSwipeRefresh);

        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                goodCourseAdapter.resetPageIndex();
                getCourseDetails();
            }
        });
        load_fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCourseDetails();
            }
        });
    }

    private void getCourseDetails() {
        if(!myPullSwipeRefresh.isRefreshing()){
            search_jiazai_layout.setVisibility(View.VISIBLE);
        }

        HttpGetOld httpGet=new HttpGetOld(this, this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                search_jiazai_layout.setVisibility(View.GONE);
                myPullSwipeRefresh.setRefreshing(false);
                if(code== Constants.HTTP_CODE_SUCCESS){
                    dataList.clear();
                    GoodCourseResult resultBean = (GoodCourseResult)baseBean;
                    dataList.addAll(resultBean.getData().getCourse());
                    goodCourseAdapter.setTotalPage(1);
                    //刷新后的数据设置
                    goodCourseAdapter.notifyDataSetChanged();
                }else if(code==Constants.HTTP_CODE_2001||code==Constants.HTTP_CODE_2004){
                    //重新登录
                    NetworkUtil.httpRestartLogin(GoodCourseActivity.this,activity_good_course);
                }else {
                    //加载失败
                    load_fail_layout.setVisibility(View.VISIBLE);
                    NetworkUtil.httpNetErrTip(GoodCourseActivity.this,activity_good_course);
                }
            }
        },GoodCourseResult.class, Urls.GET_RECOMM_COURSE);
        httpGet.excute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==Constants.LOGIN_BACK){
            getCourseDetails();
        }
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }
}
