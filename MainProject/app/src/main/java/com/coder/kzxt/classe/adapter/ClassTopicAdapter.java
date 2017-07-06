package com.coder.kzxt.classe.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by wangtingshun on 2017/6/12.
 */

public class ClassTopicAdapter extends FragmentPagerAdapter {


    private ArrayList<String> titles;

    private ArrayList<Fragment> topicFragments;

    public ClassTopicAdapter(FragmentManager fm,ArrayList<Fragment> fragments,ArrayList<String> tips) {
        super(fm);
        this.topicFragments = fragments;
        this.titles = tips;
    }

    @Override
    public Fragment getItem(int position) {
        return topicFragments.get(position);
    }

    @Override
    public int getCount() {
        return topicFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

}
