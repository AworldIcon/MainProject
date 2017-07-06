package com.coder.kzxt.views;

import android.annotation.SuppressLint;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;


public class RotateDownPageTransformer implements PageTransformer {
	private static final float ROT_MAX = 180.0f; //动画角度
	private float mRot;


	@SuppressLint("NewApi")
	public void transformPage(View view, float position){
			
		if (position <= 0) {
			mRot = (ROT_MAX * position);
			//从右向左滑动为当前View
			//设置旋转中心点；
			ViewHelper.setPivotX(view, view.getMeasuredWidth() * 0.5f);
			ViewHelper.setPivotY(view, view.getMeasuredHeight());
			ViewHelper.setRotation(view, mRot);
			
		} else if (position <= 1) {
			mRot = (ROT_MAX * position);
			//从左向右滑动为当前View
			//设置旋转中心点；
			ViewHelper.setPivotX(view, view.getMeasuredWidth() * 0.5f);
			ViewHelper.setPivotY(view, view.getMeasuredHeight());
			ViewHelper.setRotation(view, mRot);
		}
		
	}
}