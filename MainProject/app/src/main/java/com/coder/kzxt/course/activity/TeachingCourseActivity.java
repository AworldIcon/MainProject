package com.coder.kzxt.course.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.course.adapter.CourseClassAdapter;
import com.coder.kzxt.course.fragment.TeacherCourseFragmentNew;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import java.util.ArrayList;

/**
 * 在教课程
 */
public class TeachingCourseActivity extends BaseActivity{

    private Toolbar mToolBar;
    private ViewPager pager;
    private SharedPreferencesUtil spu;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private TabLayout tabLayout;
    private CourseClassAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_and_class);
        spu = new SharedPreferencesUtil(this);
        initView();
        initFragment();
        initListener();
        initData();
    }

    private void initView() {
        mToolBar = (Toolbar) findViewById(R.id.mToolBar);
        mToolBar.setTitle(getResources().getString(R.string.online_teaching_course));
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(1);
    }

    private void initListener() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                pager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void initFragment() {
        fragments.clear();// 清空
        Bundle bundle = new Bundle();
//        TeacherCourseFragment courseFragment = new TeacherCourseFragment();
        TeacherCourseFragmentNew courseFragment = new TeacherCourseFragmentNew();
        courseFragment.setArguments(bundle);
        fragments.add(courseFragment);

    }

    private void initData() {
        adapter = new CourseClassAdapter(this,getSupportFragmentManager(),fragments);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                setResult(1);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            setResult(1);
            finish();
            return true;
        }
        return false;
    }



    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
