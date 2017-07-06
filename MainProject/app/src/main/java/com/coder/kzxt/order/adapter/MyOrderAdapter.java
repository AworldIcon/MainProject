package com.coder.kzxt.order.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.coder.kzxt.activity.R;
import java.util.ArrayList;

/**
 * 我的订单适配器
 * Created by wangtingshun on 2017/4/13.
 */

public class MyOrderAdapter extends FragmentPagerAdapter {


    private Context mContext;
    private ArrayList<Fragment> fragments;
    private String[] titles = new String[3];

    public MyOrderAdapter(Context context,FragmentManager fm,ArrayList<Fragment> fragments) {
        super(fm);
        this.mContext = context;
        this.fragments = fragments;
        this.titles[0] = context.getResources().getString(R.string.all);
        this.titles[1] = context.getResources().getString(R.string.wait_pay);
        this.titles[2] = context.getResources().getString(R.string.already_complete);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
