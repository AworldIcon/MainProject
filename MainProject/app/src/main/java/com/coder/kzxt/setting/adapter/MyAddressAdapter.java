package com.coder.kzxt.setting.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.utils.Constants;

import java.util.ArrayList;

/**
 * Created by wangtingshun on 2017/3/8.
 * 我的地址适配器
 */

public class MyAddressAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private String[] titles = new String[2];
    private Bundle bundle;
    private int localSwitch;  //本站地址开关 0：关闭 1：开启
    private int cloudSwitch;  //中心地址开关 0：关闭 1：开启
    private ArrayList<Fragment> fragments;

    public MyAddressAdapter(Context context, FragmentManager fm,Bundle bundle,ArrayList<Fragment> fragments) {
        super(fm);
        this.mContext = context;
        this.fragments = fragments;
        this.bundle = bundle;
        this.localSwitch = bundle.getInt(Constants.LOCAL_SWITCH, -1);
        this.cloudSwitch = bundle.getInt(Constants.CLOUD_SWITCH, -1);
        changePage(context);
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

    private void changePage(Context context) {
        if (localSwitch == 1 && cloudSwitch != 1) {
            this.titles[0] = context.getResources().getString(R.string.school_self);
        } else if (localSwitch != 1 && cloudSwitch == 1) {
            this.titles[0] = context.getResources().getString(R.string.yun_league);
        } else if (localSwitch == 1 && cloudSwitch == 1) {
            this.titles[0] = context.getResources().getString(R.string.school_self);
            this.titles[1] = context.getResources().getString(R.string.yun_league);
        }
    }

}
