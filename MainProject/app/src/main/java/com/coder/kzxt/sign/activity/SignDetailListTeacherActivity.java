package com.coder.kzxt.sign.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.adapter.PagerSlidingTabStripAdapter;
import com.coder.kzxt.sign.fragment.SignDetailSignedFragment;
import com.coder.kzxt.sign.fragment.SignDetailUnSignFragment;

import java.util.ArrayList;


/**
 * 签到 详情 包含已签到 未签到
 *
 * @author pc
 */
public class SignDetailListTeacherActivity extends BaseActivity
{

    private TabLayout tabs;
    private ViewPager pager;
    private Toolbar toolbar;

    private ArrayList<Fragment> fragments;
    private ArrayList<String> userChannelList;

    private PagerSlidingTabStripAdapter tabStripAdapter;
    private DisplayMetrics dm;

    private String signId;
    private String courseId;
    private String classId;

    public static void gotoActivity(BaseActivity context, String signId, String courseId, String classId)
    {
        context.startActivityForResult(new Intent(context, SignDetailListTeacherActivity.class)
                .putExtra("signId", signId)
                .putExtra("courseId", courseId)
                .putExtra("classId", classId), 1000
        );
    }

    private void initVariable()
    {
        signId = getIntent().getStringExtra("signId");
        courseId = getIntent().getStringExtra("courseId");
        classId = getIntent().getStringExtra("classId");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_my);
        initVariable();
        initView();
        initData();
        initListener();
        httpRequest();
    }

    private void httpRequest()
    {
    }

    //控件
    private void initView()
    {

        dm = getResources().getDisplayMetrics();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (TabLayout) findViewById(R.id.tabs);
        toolbar.setTitle(getResources().getString(R.string.sign_detail));
        setSupportActionBar(toolbar);

    }

    SignDetailSignedFragment signDetailSignedFragment;
    SignDetailUnSignFragment signDetailUnSignFragment;

    private void initData()
    {

        userChannelList = new ArrayList<String>();
        userChannelList.add(getResources().getString(R.string.signed));
        userChannelList.add(getResources().getString(R.string.signed_not));

        Bundle bundle = new Bundle();
        bundle.putString("signId", signId);
        bundle.putString("courseId", courseId);
        bundle.putString("classId", classId);

        signDetailSignedFragment = new SignDetailSignedFragment();
        signDetailSignedFragment.setArguments(bundle);
        signDetailUnSignFragment = new SignDetailUnSignFragment();
        signDetailUnSignFragment.setArguments(bundle);

        fragments = new ArrayList<>();
        fragments.add(signDetailSignedFragment);
        fragments.add(signDetailUnSignFragment);

        tabStripAdapter = new PagerSlidingTabStripAdapter(getSupportFragmentManager(), userChannelList, fragments);
        pager.setAdapter(tabStripAdapter);
        int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        tabs.setupWithViewPager(pager);
        pager.setOffscreenPageLimit(fragments.size());

    }

    //监听器
    private void initListener()
    {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == 1000)
        {
            signDetailSignedFragment.httpRequest();
            signDetailUnSignFragment.httpRequest();
            setResult(1000);
        }
    }
}