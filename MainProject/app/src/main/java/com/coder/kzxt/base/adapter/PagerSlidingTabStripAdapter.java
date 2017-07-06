package com.coder.kzxt.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class PagerSlidingTabStripAdapter extends FragmentPagerAdapter
{
	private List<String> userChannelList;
	private List<Fragment> fragments;


	public PagerSlidingTabStripAdapter(FragmentManager fm, List<String> userChannelList, List<Fragment> fragments) {
		super(fm);
		this.userChannelList = userChannelList;
		this.fragments = fragments;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return userChannelList.get(position);
	}

	@Override
	public int getCount() {
		return userChannelList.size();
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}
	
}
