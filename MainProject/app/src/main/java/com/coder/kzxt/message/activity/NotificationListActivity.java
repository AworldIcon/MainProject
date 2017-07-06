package com.coder.kzxt.message.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpPutBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.adapter.PagerSlidingTabStripAdapter;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.message.fragment.NotificationMainFragment;
import com.coder.kzxt.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw on 2017/6/23
 * 通知列表
 */

public class NotificationListActivity extends BaseActivity
{
    private Toolbar toolbar,toolbar_stu;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private  NotificationMainFragment notificationMainFragment1;
    private  NotificationMainFragment notificationMainFragment2;
    private String role;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_stu = (Toolbar) findViewById(R.id.toolbar_stu);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        toolbar.setTitle("通知");
        toolbar_stu.setTitle("通知");
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.pics_menu));
        toolbar_stu.setOverflowIcon(getResources().getDrawable(R.drawable.pics_menu));
        role=getIntent().getStringExtra("role");
        if(role.equals("1")){
            setSupportActionBar(toolbar);
            toolbar_stu.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
        }else {
            setSupportActionBar(toolbar_stu);
            toolbar.setVisibility(View.GONE);
            toolbar_stu.setVisibility(View.VISIBLE);
        }
         notificationMainFragment1 = new NotificationMainFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 2);
        notificationMainFragment1.setArguments(bundle);
        Bundle bundle2 = new Bundle();
        bundle2.putInt("type", 1);
        notificationMainFragment2 = new NotificationMainFragment();
        notificationMainFragment2.setArguments(bundle2);

        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        if(role.equals("1")){
            titles.add(getResources().getString(R.string.receive));
            titles.add(getResources().getString(R.string.send_out));
            fragments.add(notificationMainFragment1);
            fragments.add(notificationMainFragment2);
        }else {
            titles.add(getResources().getString(R.string.receive));
            fragments.add(notificationMainFragment1);
            tabLayout.setVisibility(View.GONE);
        }

        viewPager.setAdapter(new PagerSlidingTabStripAdapter(getSupportFragmentManager(), titles, fragments));
        tabLayout.setupWithViewPager(viewPager);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_stu.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initClick();
    }
    //点击事件
    private void initClick() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    menuItem.setVisible(true);
                    menuItem1.setVisible(false);
                }else {
                    menuItem1.setVisible(true);
                    menuItem.setVisible(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public MenuItem menuItem;
    private MenuItem menuItem1;
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.notify_menu, menu);
         menuItem = menu.findItem(R.id.read_item).setTitle("设为已读").setVisible(false);
        menuItem1 = menu.findItem(R.id.add_notify);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==R.id.read_item){
        putReuqestRead();
        }else {
            startActivityForResult(new Intent(NotificationListActivity.this,SendNotificationActivity.class),1);
        }
        return super.onOptionsItemSelected(item);
    }
//设为已读
    private void putReuqestRead() {
        new HttpPutBuilder(this).setUrl(UrlsNew.PUT_NOTIFY_CONTENT).setClassObj(null).setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                notificationMainFragment1.refreshData();
            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {
                ToastUtils.makeText(NotificationListActivity.this,msg);

            }
        }).setPath(notificationMainFragment1.getIds()).build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==1){
            notificationMainFragment2.refreshData();
        }
    }
}
