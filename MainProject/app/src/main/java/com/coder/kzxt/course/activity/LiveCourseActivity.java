package com.coder.kzxt.course.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.MenuItem;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.adapter.PagerSlidingTabStripAdapter;
import com.coder.kzxt.course.fragment.LiveAdvanceFragment;
import com.coder.kzxt.course.fragment.LiveReplayFragment;
import com.coder.kzxt.course.fragment.LiveingFragment;

import java.util.ArrayList;

public class LiveCourseActivity extends BaseActivity
{

    private Toolbar mToolbarView;
    private TabLayout mTabLayout;
    private ViewPager pager;
    private PagerSlidingTabStripAdapter tabStripAdapter;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private ArrayList<String> userChannelList = new ArrayList<String>();
    private LiveingFragment liveingFragment;
    private LiveAdvanceFragment liveAdvanceFragment;
    private LiveReplayFragment liveReplayFragment;
    private int position;
    private String treeId="";

    public static void gotoActivity(Context mContext, int position)
    {

        mContext.startActivity(new Intent(mContext, LiveCourseActivity.class).putExtra("position", position));
    }


    private void initVariable()
    {
        position = getIntent().getIntExtra("position", 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        initVariable();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_course);

        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setTitle(R.string.live);
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        treeId = getIntent().getStringExtra("treeId");
        mTabLayout = (TabLayout) findViewById(R.id.mtablyout);
        pager = (ViewPager) findViewById(R.id.mpager);
        tabStripAdapter = new PagerSlidingTabStripAdapter(getSupportFragmentManager(), userChannelList, fragments);
        liveingFragment = (LiveingFragment) LiveingFragment.newInstance(treeId);
        liveAdvanceFragment = (LiveAdvanceFragment) LiveAdvanceFragment.newInstance(treeId);
        liveReplayFragment = (LiveReplayFragment) LiveReplayFragment.newInstance(treeId);
        initFragment();

    }

    private void initFragment()
    {
        fragments.clear();// 清空
        userChannelList.add(getResources().getString(R.string.liveing));
        userChannelList.add(getResources().getString(R.string.live_advance));
        if(TextUtils.isEmpty(treeId)){
            userChannelList.add(getResources().getString(R.string.live_replay));
        }else {
            userChannelList.add(getResources().getString(R.string.has_over));
        }

        fragments.add(liveingFragment);
        fragments.add(liveAdvanceFragment);
        fragments.add(liveReplayFragment);

        pager.setAdapter(tabStripAdapter);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        mTabLayout.setupWithViewPager(pager);
        pager.setOffscreenPageLimit(fragments.size());
        pager.setCurrentItem(position);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.main_menu, menu);
//        MenuItem searchItem = menu.findItem(R.id.menu_search);
//        MenuItem downloadItem = menu.findItem(R.id.menu_download);
//        MenuItem messageItem = menu.findItem(R.id.menu_message);
//        //1.alaways:这个值会使菜单项一直显示在ActionBar上。
//        //2.ifRoom:如果有足够的空间,这个值会使菜单显示在ActionBar上。
//        //3.never:这个值菜单永远不会出现在ActionBar是。
//        //4.withText:这个值使菜单和它的图标，菜单文本一起显示。
//        MenuItemCompat.setShowAsAction(searchItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
//        MenuItemCompat.setShowAsAction(downloadItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
//        MenuItemCompat.setShowAsAction(messageItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
//        downloadItem.setVisible(false);
//        messageItem.setVisible(false);
//        searchItem.setVisible(false);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;

//            case R.id.menu_search:
//                SearchLiveCourseActivity.gotoActivity(LiveCourseActivity.this);
//                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
