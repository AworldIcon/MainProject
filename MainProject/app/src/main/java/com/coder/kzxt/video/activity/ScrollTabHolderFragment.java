package com.coder.kzxt.video.activity;

import android.widget.AbsListView;

import com.coder.kzxt.base.fragment.BaseFragment;

public abstract class ScrollTabHolderFragment extends BaseFragment implements ScrollTabHolder {

	protected ScrollTabHolder mScrollTabHolder;

	public void setScrollTabHolder(ScrollTabHolder scrollTabHolder) {
		mScrollTabHolder = scrollTabHolder;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition) {
		// nothing
	}

	@Override
	public void adjustScroll(int scrollHeight) {
		// TODO Auto-generated method stub
		
	}

}