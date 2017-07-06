package com.coder.kzxt.download;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.adapter.PagerSlidingTabStripAdapter;

import java.util.ArrayList;

public class Download_Center_Activity extends BaseActivity
{

    private Toolbar mToolbarView;
    private TabLayout mTabLayout;
    private ViewPager pager;
    private PagerSlidingTabStripAdapter tabStripAdapter;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private ArrayList<String> userChannelList = new ArrayList<String>();
    private DownloadCourseFragment downloadCourseFragment;
    private DownloadServiceFragment downloadServiceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_center);
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setTitle(R.string.my_download);
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTabLayout = (TabLayout) findViewById(R.id.mtablyout);
        pager = (ViewPager) findViewById(R.id.mpager);
        tabStripAdapter = new PagerSlidingTabStripAdapter(getSupportFragmentManager(), userChannelList, fragments);
        downloadCourseFragment = new DownloadCourseFragment();
        downloadServiceFragment = new DownloadServiceFragment();
        initFragment();

    }

    private void initFragment()
    {
        fragments.clear();// 清空
        userChannelList.add(getResources().getString(R.string.courses));
        userChannelList.add(getResources().getString(R.string.service));
        fragments.add(downloadCourseFragment);
        fragments.add(downloadServiceFragment);

        pager.setAdapter(tabStripAdapter);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        mTabLayout.setupWithViewPager(pager);
        pager.setOffscreenPageLimit(fragments.size());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
