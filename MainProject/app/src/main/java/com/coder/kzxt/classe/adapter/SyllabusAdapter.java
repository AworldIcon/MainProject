package com.coder.kzxt.classe.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;

/**
 * 课程表适配器
 * Created by wangtingshun on 2017/3/16.
 */

public class SyllabusAdapter extends FragmentPagerAdapter {

    private ArrayList<String> tips;
    private ArrayList<Fragment> dayFragment;

    public SyllabusAdapter(FragmentManager fm,ArrayList<Fragment> list,ArrayList<String> tips) {
        super(fm);
        this.dayFragment = list;
        this.tips = tips;
    }

    @Override
    public Fragment getItem(int position) {
        return dayFragment.get(position);
    }

    @Override
    public int getCount() {
        return dayFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tips.get(position);
    }
}
