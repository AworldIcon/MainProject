package com.coder.kzxt.setting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.setting.adapter.MyAddressAdapter;
import com.coder.kzxt.setting.fragment.PublicCourseAddressFragment;
import com.coder.kzxt.setting.fragment.SchoolAddressFragment;
import com.coder.kzxt.utils.Constants;

import java.util.ArrayList;

/**
 * 我的地址
 * Created by wangtingshun on 2017/3/8.
 */

public class MyAddressActivity extends BaseActivity {

    private ViewPager viewPager;
    private String publicCourse;
    private int localSwitch;  //本站地址开关 0：关闭 1：开启
    private int cloudSwitch;  //中心地址开关 0：关闭 1：开启
    private String userInfo;
    private TabLayout tabLayout;
    private Toolbar mToolbarView;
    private MyAddressAdapter adapter;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private SchoolAddressFragment schoolAddressFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_address_layout);
        getIntentData();
        initView();
        initFragment();
        initListener();
    }

    private void getIntentData() {
        publicCourse = getIntent().getStringExtra(Constants.IS_CENTER);
        localSwitch = getIntent().getIntExtra(Constants.LOCAL_SWITCH, -1);
        cloudSwitch = getIntent().getIntExtra(Constants.CLOUD_SWITCH, -1);
        userInfo = getIntent().getStringExtra(Constants.USER_INFO_ENTRANCE);
    }

    private void initView() {
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setTitle(getResources().getString(R.string.my_address));
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager.setOffscreenPageLimit(1);
    }

    private void initFragment() {
        fragments.clear();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.IS_CENTER, publicCourse);
        bundle.putString(Constants.USER_INFO_ENTRANCE, userInfo);
        if (localSwitch == 1 && cloudSwitch == 0) {
            bundle.putInt(Constants.LOCAL_SWITCH, localSwitch);
            createSchoolFragment(bundle);
        } else if (localSwitch == 0 && cloudSwitch == 1) {
            bundle.putInt(Constants.CLOUD_SWITCH,cloudSwitch);
            createPublicFragment(bundle);
        } else if (localSwitch == 1 && cloudSwitch == 1) {
            bundle.putInt(Constants.LOCAL_SWITCH, localSwitch);
            bundle.putInt(Constants.CLOUD_SWITCH,cloudSwitch);
            createSchoolFragment(bundle);
            createPublicFragment(bundle);
        } else {
         //TODO:待处理
            createSchoolFragment(bundle);
        }
        fillData(bundle);
    }

    /**
     * 本校
     * @param bundle
     */
    private void createSchoolFragment(Bundle bundle) {
        schoolAddressFragment = new SchoolAddressFragment();
        schoolAddressFragment.setArguments(bundle);
        fragments.add(schoolAddressFragment);
    }

    /**
     * 公开课
     * @param bundle
     */
    private void createPublicFragment(Bundle bundle) {
        PublicCourseAddressFragment publicCourseAddressFragment = new PublicCourseAddressFragment();
        publicCourseAddressFragment.setArguments(bundle);
        fragments.add(publicCourseAddressFragment);
    }

    private void fillData(Bundle bundle) {
        adapter = new MyAddressAdapter(this,getSupportFragmentManager(),bundle,fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initListener() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Bundle bundle = new Bundle();
                if (tab == tabLayout.getTabAt(0)) {
                    bundle.putInt(Constants.LOCAL_SWITCH, localSwitch);
                    bundle.putString(Constants.IS_CENTER, String.valueOf(0));
                } else if (tab == tabLayout.getTabAt(1)) {
                    bundle.putInt(Constants.CLOUD_SWITCH, cloudSwitch);
                    bundle.putString(Constants.IS_CENTER, String.valueOf(1));
                }
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(100);
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
            setResult(100);
            finish();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && resultCode == 10) {
            schoolAddressFragment.refreshData();
        }
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

}
