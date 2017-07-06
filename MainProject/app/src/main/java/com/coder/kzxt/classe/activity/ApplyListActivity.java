package com.coder.kzxt.classe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.adapter.ClassApplyAdapter;
import com.coder.kzxt.classe.beans.ClassApplyResult;
import com.coder.kzxt.classe.fragment.StudentApplyFragment;
import com.coder.kzxt.classe.fragment.TeacherApplyFragment;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.SharedPreferencesUtil;

import java.util.ArrayList;

/**
 * 申请列表
 * Created by wangtingshun on 2017/3/18.
 */

public class ApplyListActivity extends BaseActivity {

    private String classId;  //班级id
    private String className; //班级名字
    private SharedPreferencesUtil spu;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ClassApplyAdapter applyAdapter;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private StudentApplyFragment studentFragment;
    private TeacherApplyFragment teacherFragment;
    private ClassApplyResult.ClassApplyBean applyBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_list_layout);
        spu = new SharedPreferencesUtil(this);
        classId = getIntent().getStringExtra("classId");
        className = getIntent().getStringExtra("className");
        initView();
        initListener();
        initFragment();
        appendFragment();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.apply_list));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);
    }

    private void initListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initFragment() {
        studentFragment = new StudentApplyFragment();
        teacherFragment = new TeacherApplyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("classId",classId);
        bundle.putString("className",className);
        studentFragment.setArguments(bundle);
        teacherFragment.setArguments(bundle);
        fragments.add(studentFragment);
        fragments.add(teacherFragment);
    }

    private void appendFragment() {
        applyAdapter = new ClassApplyAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(applyAdapter);
        tabLayout.setupWithViewPager(viewPager);
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle extra = (Bundle) data.getBundleExtra("data");
        applyBean = (ClassApplyResult.ClassApplyBean) extra.getSerializable("applyBean");
        if (requestCode == Constants.APPLY_REQUEST_CODE && resultCode == 2) {
            if (applyBean.getIsTeacher().equals("0")) {
                viewPager.setCurrentItem(0);
                if (applyBean.isAgree() && studentFragment != null) {
                    studentFragment.onClickAgreeButton(applyBean);
                } else {
                    studentFragment.submitData(applyBean);
                }
                viewPager.setCurrentItem(0);
            } else {
                viewPager.setCurrentItem(1);
                if (applyBean.isAgree() && teacherFragment != null) {
                    teacherFragment.onClickAgreeButton(applyBean);
                } else {
                    teacherFragment.submitData(applyBean);
                }
            }
        }
    }
}
