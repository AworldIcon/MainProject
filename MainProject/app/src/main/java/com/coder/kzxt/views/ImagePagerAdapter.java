/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package com.coder.kzxt.views;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.image.ImageLoad;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.EToast;
import com.coder.kzxt.video.activity.VideoViewActivity;
import com.coder.kzxt.webview.activity.TextView_Link_Activity;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * ImagePagerAdapter
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-23
 */
public class ImagePagerAdapter extends RecyclingPagerAdapter {

	private Context context;
	private List<HashMap<String, String>> imageIdList;

	private int size;
	private boolean isInfiniteLoop;
	//private PublicUtils pu;

	public ImagePagerAdapter(Context context, List<HashMap<String, String>> imageIdList) {
		this.context = context;
		this.imageIdList = imageIdList;
		this.size = imageIdList.size();
		isInfiniteLoop = false;
		//pu = new PublicUtils(context);
	}

	@Override
	public int getCount() {
		// Infinite loop
		return isInfiniteLoop ? Integer.MAX_VALUE : imageIdList.size();
	}

	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	private int getPosition(int position) {
		return isInfiniteLoop ? position % size : position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup container) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.main_viewpager_imgs, null);
			holder.imageView = (ImageView) view.findViewById(R.id.my_img);
			holder.textView = (TextView) view.findViewById(R.id.my_text);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final HashMap<String, String> hashMap = imageIdList.get(getPosition(position));

		final String title = hashMap.get("title");
		String img = hashMap.get("img");
		final String url = hashMap.get("url");
		if (!TextUtils.isEmpty(title)&&title.length() > 15) {
			holder.textView.setText(title.subSequence(0, 15) + "...");
		} else {
			//holder.textView.setText(title);
		}

		ImageLoad.loadImage(context,img,R.drawable.my_course_def,R.drawable.my_course_def,0, RoundedCornersTransformation.CornerType.ALL,holder.imageView);

		holder.imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(url)) {
					if (url.startsWith("http:") || url.startsWith("https:")) {
						Intent intent = new Intent(context, TextView_Link_Activity.class);
						intent.putExtra("web_url", url);
						//intent.putExtra("title", title);
						context.startActivity(intent);
					} else if (url.startsWith("go")) {
						String jumpString = getJumpString(url);
						// 跳转课程播放页
						if (jumpString.equals("gotocoursedetail")) {

							String jumpID = getJumpID(url);
							Intent intent = new Intent(context, VideoViewActivity.class);
							intent.putExtra("flag", Constants.ONLINE);
							intent.putExtra("treeid", jumpID);
							intent.putExtra("tree_name", title);
							intent.putExtra("pic", "");
							context.startActivity(intent);
//							EToast.makeText(context,"跳转课程播放页", Toast.LENGTH_SHORT).show();

							// 跳转班级详情
						} else if (jumpString.equals("gotoclassdetail")) {
//							String jumpID = getJumpID(url);
//							Intent intent = new Intent();
//							intent.setClass(context, Class_Particulars_Activity.class);
//							intent.putExtra("classId", jumpID);
//							intent.putExtra("myClassState", "");
//							context.startActivity(intent);
//							EToast.makeText(context,"跳转班级详情", Toast.LENGTH_SHORT).show();

							// 跳转任务奖励页面
						} else if (jumpString.equals("gotorewardpage")) {
//							if (TextUtils.isEmpty(pu.getIsLogin())) {
//								Intent intent = new Intent(context, LoginActivity.class);
//								context.startActivity(intent);
//							} else {
//								Intent intent = new Intent(context, TextView_Link_Activity.class);
//								intent.putExtra("web_url", pu.getTaskActivityPageUrl());
//								intent.putExtra("title", title);
//								context.startActivity(intent);
//
//							}
//							EToast.makeText(context,"跳转任务奖励页面", Toast.LENGTH_SHORT).show();

							// 跳转特定的考试/作业
						} else if (jumpString.equals("gotoexampage")) {

//							if (TextUtils.isEmpty(pu.getIsLogin())) {
//								Intent intent = new Intent(context, LoginActivity.class);
//								context.startActivity(intent);
//							} else {
//								String jumpID = getJumpID(url);
//								Intent intent = new Intent(context, TestpaperFirstActivity.class);
//								intent.putExtra("name", title);
//								intent.putExtra("testId", jumpID);
//								context.startActivity(intent);
//
//							}
//							EToast.makeText(context,"跳转特定的考试/作业", Toast.LENGTH_SHORT).show();


							// 跳转我的问答
						} else if (jumpString.equals("gotomyquestionlist")) {

//							if (TextUtils.isEmpty(pu.getIsLogin())) {
//								Intent intent = new Intent(context, LoginActivity.class);
//								context.startActivity(intent);
//							} else {
//								Intent intent = new Intent(context, My_Question_Activity.class);
//								context.startActivity(intent);
//
//							}
//							EToast.makeText(context,"跳转我的问答", Toast.LENGTH_SHORT).show();

							// 跳转我的课程列表
						} else if (jumpString.equals("gotomyclasslist")) {

//							if (TextUtils.isEmpty(pu.getIsLogin())) {
//								Intent intent = new Intent(context, LoginActivity.class);
//								context.startActivity(intent);
//							} else {
//								Intent intent = new Intent(context, LearnCourseActivity.class);
//								context.startActivity(intent);
//
//							}
//							EToast.makeText(context,"跳转我的课程列表", Toast.LENGTH_SHORT).show();


							// 跳转我的收藏
						} else if (jumpString.equals("gotomycollection")) {

//							if (TextUtils.isEmpty(pu.getIsLogin())) {
//								Intent intent = new Intent(context, LoginActivity.class);
//								context.startActivity(intent);
//							} else {
//
//								Intent intent = new Intent(context, My_Collection_Activity.class);
//								context.startActivity(intent);
//
//							}
//							EToast.makeText(context,"跳转我的收藏", Toast.LENGTH_SHORT).show();


							// 跳转我的考试
						} else if (jumpString.equals("gotomyexam")) {

//							if (TextUtils.isEmpty(pu.getIsLogin())) {
//								Intent intent = new Intent(context, LoginActivity.class);
//								context.startActivity(intent);
//							} else {
//
//								Intent intent = new Intent(context, My_Exam_Activity.class);
//								context.startActivity(intent);
//							}
//							EToast.makeText(context,"跳转我的考试", Toast.LENGTH_SHORT).show();

							// 跳转我的作业
						} else if (jumpString.equals("gotomyhomework")) {

//							if (TextUtils.isEmpty(pu.getIsLogin())) {
//								Intent intent = new Intent(context, LoginActivity.class);
//								context.startActivity(intent);
//							} else {
//								Intent intent = new Intent(context, My_HomeWork_Activity.class);
//								context.startActivity(intent);
//
//							}
//							EToast.makeText(context,"跳转我的作业", Toast.LENGTH_SHORT).show();


							// 跳转特定的话题详情
						} else if (jumpString.equals("gototopicdetail")) {
//							String jumpID = getJumpID(url);
//							Intent intent = new Intent(context, ClassGambitParticularsActivity.class);
//							intent.putExtra("gambitId", jumpID);
//							intent.putExtra("from", "MainFragment");
//							context.startActivity(intent);
//							EToast.makeText(context,"跳转特定的话题详情", Toast.LENGTH_SHORT).show();
						}
					}

				}

			}
		});

		return view;
	}

	private static class ViewHolder {

		ImageView imageView;
		TextView textView;
	}

	/**
	 * @return the isInfiniteLoop
	 */
	public boolean isInfiniteLoop() {
		return isInfiniteLoop;
	}

	/**
	 * @param isInfiniteLoop
	 *            the isInfiniteLoop to set
	 */
	public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}

	/**
	 * 获取冒号签名的字符
	 * 
	 * @param jumpUrl
	 * @return
	 */
	public String getJumpString(final String jumpUrl) {
		if (!jumpUrl.contains(":")) {
			return jumpUrl;
		}

		int startPos = -1;
		int endPos = -1;
		if (jumpUrl != null) {
			startPos = 0;
			endPos = jumpUrl.indexOf(":");
			if ((startPos == -1) || (endPos == -1) || (endPos < startPos)) {
				return "";
			} else {
				return jumpUrl.substring(startPos, endPos);
			}
		}
		return "";
	}

	/**
	 * @param jumpUrl
	 *            获取冒号后面的字符
	 */
	public String getJumpID(final String jumpUrl) {
		int startPos = -1;
		int endPos = -1;
		if (jumpUrl != null) {
			startPos = jumpUrl.indexOf(":");
			endPos = jumpUrl.length();
			if ((startPos == -1) || (endPos == -1) || (endPos < startPos)) {
				return "";
			} else {
				String str = URLDecoder.decode(jumpUrl.substring(startPos + 1, endPos));
				return str;
			}
		}
		return "";
	}

	public String getJumpStringHttp(final String jumpUrl) {
		int startPos = -1;
		int endPos = -1;
		if (jumpUrl != null) {
			startPos = jumpUrl.indexOf(":");
			endPos = jumpUrl.length();
			if ((startPos == -1) || (endPos == -1) || (endPos < startPos)) {
				return "";
			} else {
				String str = URLDecoder.decode(jumpUrl.substring(startPos + 1, endPos));
				return str;
			}
		}
		return "";
	}

}
