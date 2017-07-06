package com.coder.kzxt.course.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.coder.kzxt.activity.R;
import java.util.ArrayList;

/**
 * 课程班级适配器
 * Created by wangtingshun on 2017/3/10.
 */

public class CourseClassAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private ArrayList<Fragment> fragments;
    private String[] titles = new String[1];

    public CourseClassAdapter( Context context,FragmentManager fm,ArrayList<Fragment> fragments) {
        super(fm);
        this.mContext = context;
        this.fragments = fragments;
        this.titles[0] = context.getResources().getString(R.string.courses);
      //  this.titles[1] = context.getResources().getString(R.string.classs);
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
