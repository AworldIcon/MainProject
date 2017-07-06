package com.coder.kzxt.message.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.adapter.PagerSlidingTabStripAdapter;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.message.fragment.NotificationReadNumberFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaShiZhao on 2017/3/16
 * 通知的 已读 未读人数列表
 */

public class NotificationReadNumberActivity extends BaseActivity
{


    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        toolbar.setTitle("接收成员");
        NotificationReadNumberFragment notificationMainFragment1 = new NotificationReadNumberFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", "1");
        int notifyId=0;
        notifyId=getIntent().getIntExtra("notifyId",0);
        bundle.putInt("notifyId",notifyId);
        notificationMainFragment1.setArguments(bundle);

        Bundle bundle2 = new Bundle();
        bundle2.putString("type", "2");
        bundle2.putInt("notifyId",notifyId);
        NotificationReadNumberFragment notificationMainFragment2 = new NotificationReadNumberFragment();
        notificationMainFragment2.setArguments(bundle2);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(notificationMainFragment1);
        fragments.add(notificationMainFragment2);
        List<String> titles = new ArrayList<>();
        titles.add(getResources().getString(R.string.read_number));
        titles.add(getResources().getString(R.string.unread_number));
        viewPager.setAdapter(new PagerSlidingTabStripAdapter(getSupportFragmentManager(), titles, fragments));
        tabLayout.setupWithViewPager(viewPager);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {


        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.change_center_menu, menu);

        MenuItem menu_set_read = menu.findItem(R.id.menu_set_read);
        MenuItem menu_setting = menu.findItem(R.id.menu_setting);
        MenuItem menu_publish = menu.findItem(R.id.menu_publish);

        if (viewPager.getCurrentItem() == 0)
        {
            menu_setting.setVisible(true);
            menu_publish.setVisible(false);
            menu_set_read.setVisible(true);
        } else
        {
            menu_setting.setVisible(false);
            menu_publish.setVisible(true);
            menu_set_read.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_set_read:

                break;
            case R.id.menu_setting:

                break;
            case R.id.menu_publish:

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
