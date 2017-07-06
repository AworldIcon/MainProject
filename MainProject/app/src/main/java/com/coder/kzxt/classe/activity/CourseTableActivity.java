package com.coder.kzxt.classe.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.app.http.HttpGetOld;
import com.app.http.InterfaceHttpResult;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.adapter.SyllabusAdapter;
import com.coder.kzxt.classe.beans.CourseTableResult;
import com.coder.kzxt.classe.fragment.SyllabusFragmet;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.Urls;
import java.util.ArrayList;
import java.util.LinkedList;
import static com.coder.kzxt.activity.R.id.load_fail_button;
import static com.coder.kzxt.activity.R.id.my_layout;

/**
 * 课程表
 * Created by wangtinshun on 2017/3/14.
 */

public class CourseTableActivity extends BaseActivity {

    private SharedPreferencesUtil spu;
    private Toolbar mToolbar;
    private String classId;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RelativeLayout myLayout;
    private SyllabusAdapter adapter;
    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private Button loadFailBtn;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private ArrayList<String> tips = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_timetable);
        spu = new SharedPreferencesUtil(this);
        classId = getIntent().getStringExtra("classId");
        initView();
        initListener();
        startAsyncTask(classId);
    }

    private void initView() {
        mToolbar = (Toolbar)findViewById(R.id.mToolBar);
        mToolbar.setTitle(R.string.course_table);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout)findViewById(R.id.load_fail_layout);
        no_info_layout = (LinearLayout)findViewById(R.id.no_info_layout);
        loadFailBtn = (Button) findViewById(load_fail_button);
        myLayout = (RelativeLayout) findViewById(my_layout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager.setOffscreenPageLimit(1);
    }

    private void initListener() {
        loadFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAsyncTask(classId);
            }
        });
    }

    private void startAsyncTask(String classId) {
        loadingPage();
        new HttpGetOld(this, this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                visibleData();
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
                    CourseTableResult tableResult = (CourseTableResult) baseBean;
                    CourseTableResult.CourseTable day = tableResult.getData();
                    LinkedList<CourseTableResult.DayBean> dayList = initData(day);
                    if (dayList != null && dayList.size() > 0) {
                        initFragment(dayList);
                        loadFragment(fragments);
                    } else {
                        noDataPage();
                    }
                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                    NetworkUtil.httpNetErrTip(CourseTableActivity.this, myLayout);
                    if(!NetworkUtil.isNetworkAvailable(CourseTableActivity.this)){
                        loadFailPage();
                    }
                } else if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                    NetworkUtil.httpRestartLogin(CourseTableActivity.this,myLayout);
                } else {
                    errProcess(msg);
                }
            }
        }, CourseTableResult.class, Urls.GET_CLASS_SYLLABUS_ACTION,classId).excute(1000);
    }

    private void initFragment(LinkedList<CourseTableResult.DayBean> dayList) {
        SyllabusFragmet fragment = null;
        if (dayList != null && dayList.size() > 0) {
            for (int i = 0; i < dayList.size(); i++) {
                CourseTableResult.DayBean dayBean = dayList.get(i);
                String tip = dayBean.getTip();
                fragment = new SyllabusFragmet();
                Bundle bundle = new Bundle();
                bundle.putSerializable("syllabus", dayBean);
                fragment.setArguments(bundle);
                tips.add(tip);
                fragments.add(fragment);
            }
        }
    }

    /**
     * 初始化fragment
     */
    private void loadFragment(ArrayList<Fragment> fragments) {
        adapter = new SyllabusAdapter(getSupportFragmentManager(),fragments,tips);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private LinkedList<CourseTableResult.DayBean> initData(CourseTableResult.CourseTable day) {
        LinkedList<CourseTableResult.DayBean> dayList = new LinkedList<>();
        CourseTableResult.DayBean day_1 = day.getDay_1();
        CourseTableResult.DayBean day_2 = day.getDay_2();
        CourseTableResult.DayBean day_3 = day.getDay_3();
        CourseTableResult.DayBean day_4 = day.getDay_4();
        CourseTableResult.DayBean day_5 = day.getDay_5();
        CourseTableResult.DayBean day_6 = day.getDay_6();
        CourseTableResult.DayBean day_7 = day.getDay_7();
        dayList.add(day_1);
        dayList.add(day_2);
        dayList.add(day_3);
        dayList.add(day_4);
        dayList.add(day_5);
        dayList.add(day_6);
        dayList.add(day_7);
        return dayList;
    }

    private void errProcess(String msg) {
        loadFailPage();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 暂无数据
     */
    private void noDataPage() {
        load_fail_layout.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);
        jiazai_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.GONE);
    }

    /**
     * 加载失败页
     */
    private void loadFailPage() {
        load_fail_layout.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.GONE);
        jiazai_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
    }

    /**
     * 显示数据
     */
    private void visibleData() {
        jiazai_layout.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
        tabLayout.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
    }

    /**
     * 显示加载页
     */
    private void loadingPage() {
        jiazai_layout.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
    }

}
