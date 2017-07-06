package com.coder.kzxt.stuwork.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.stuwork.adapter.StuCourseWorkAdapter;
import com.coder.kzxt.stuwork.adapter.StuCourseWorkDelegate;
import com.coder.kzxt.stuwork.entity.StuCourseWorkBean;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.app.http.UrlsNew;

import java.util.ArrayList;
import java.util.List;

public class StuCourseWorkActivity extends AppCompatActivity implements HttpCallBack {
    private String course_id;
    private int workType;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;
    private StuCourseWorkAdapter stuCourseWorkAdapter;
    private StuCourseWorkDelegate stuCourseWorkDelegate;
    private List<StuCourseWorkBean.ItemsBean>itemsBeanList=new ArrayList<>();
    private TextView title;
    private LinearLayout search_jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private Button load_fail_button;
    private ImageView no_info_img;
    private RelativeLayout activity_stu_course_work;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_course_work);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        initEvents();
        httpRequst();
    }


    private void initView() {
        activity_stu_course_work= (RelativeLayout) findViewById(R.id.activity_stu_course_work);
        search_jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        search_jiazai_layout.setVisibility(View.VISIBLE);
        load_fail_layout = (LinearLayout)findViewById(R.id.load_fail_layout);
        load_fail_button = (Button)findViewById(R.id.load_fail_button);
        no_info_layout = (LinearLayout)findViewById(R.id.no_info_layout);
        no_info_img= (ImageView) findViewById(R.id.no_info_img);
        no_info_img.setImageResource(R.drawable.empty_work_info);
        search_jiazai_layout.setVisibility(View.VISIBLE);
        title = (TextView) findViewById(R.id.toolbar_title);
        workType=getIntent().getIntExtra("workType",2);
        course_id=getIntent().getStringExtra("course_id");
        if(workType==1){
            title.setText("我的考试");
        }else {
            title.setText("我的作业");
        }
        myPullSwipeRefresh= (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        myPullRecyclerView= (MyPullRecyclerView) findViewById(R.id.myPullRecyclerView);
        stuCourseWorkDelegate=new StuCourseWorkDelegate(this,workType);
        stuCourseWorkAdapter=new StuCourseWorkAdapter(this,itemsBeanList,stuCourseWorkDelegate);
        myPullRecyclerView.setAdapter(stuCourseWorkAdapter);
        stuCourseWorkAdapter.setSwipeRefresh(myPullSwipeRefresh);
    }
    private void initEvents() {
        load_fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_jiazai_layout.setVisibility(View.VISIBLE);
                load_fail_layout.setVisibility(View.GONE);
                httpRequst();
            }
        });
    myPullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
        @Override
        public void addMoreListener() {
            stuCourseWorkAdapter.addPageIndex();
            httpRequst();
        }
    });
        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                stuCourseWorkAdapter.resetPageIndex();
                httpRequst();
            }
        });
    }

    private void httpRequst() {
        new HttpGetBuilder(this).setClassObj(StuCourseWorkBean.class).setUrl(UrlsNew.URLHeader+"/my_test")
                .setHttpResult(this)
                .addQueryParams("page",stuCourseWorkAdapter.getPageIndex()+"")
                .addQueryParams("pageSize","20")
                .addQueryParams("orderBy","create_time desc")
                .addQueryParams("course_id",course_id)
                .addQueryParams("type",workType+"")
                .build();
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        myPullRecyclerView.setVisibility(View.VISIBLE);
        search_jiazai_layout.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        StuCourseWorkBean courseWorkBean= (StuCourseWorkBean) resultBean;
        stuCourseWorkAdapter.setTotalPage(courseWorkBean.getPaginate().getPageNum());
        stuCourseWorkAdapter.setPullData(courseWorkBean.getItems());
        if(stuCourseWorkAdapter.getItemCount()==0){
            no_info_layout.setVisibility(View.VISIBLE);
        }else {
            no_info_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
            search_jiazai_layout.setVisibility(View.GONE);
        myPullRecyclerView.setVisibility(View.GONE);
        if(code== Constants.HTTP_CODE_2001||code==Constants.HTTP_CODE_2004){
            //重新登录
            NetworkUtil.httpRestartLogin(StuCourseWorkActivity.this,activity_stu_course_work);
        }else {
            //加载失败
            load_fail_layout.setVisibility(View.VISIBLE);
            NetworkUtil.httpNetErrTip(StuCourseWorkActivity.this,activity_stu_course_work);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tea_work_menu, menu);
        menu.findItem(R.id.action_setting).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_search){
            Intent intent=new Intent(this,StuSearchWorkActivity.class);
            intent.putExtra("workType",workType);
            //startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Constants.LOGIN_BACK){
            httpRequst();
        }
        if(requestCode==1){
            stuCourseWorkAdapter.resetPageIndex();
            httpRequst();
        }
    }
}
