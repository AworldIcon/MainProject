package com.coder.kzxt.classe.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * 班级申请列表的adapter
 * Created by wangtingshun on 2017/3/18.
 */

public class ClassApplyAdapter extends FragmentPagerAdapter {


    private ArrayList<Fragment> applyFragments;
    private String[] titles = new String[]{
            "学生","老师"
    };

    public ClassApplyAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.applyFragments = fragments;
    }


    @Override
    public Fragment getItem(int position) {
        return applyFragments.get(position);
    }

    @Override
    public int getCount() {
        return applyFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
