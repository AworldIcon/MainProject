package com.coder.kzxt.stuwork.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.stuwork.activity.TestPageActivity;
import com.coder.kzxt.stuwork.adapter.ChoicePagerAdapter;
import com.coder.kzxt.stuwork.entity.CommitAnswer;
import com.coder.kzxt.stuwork.entity.QuestionContent;
import com.coder.kzxt.stuwork.util.CommitTestAnswer;
import com.coder.kzxt.stuwork.util.DownloadData;
import com.coder.kzxt.utils.Constants;

import java.util.ArrayList;

/**
 * 多选
 */
public class ChoiceFragment extends BaseFragment {

	private View v;
	//private PublicUtils pu;
	private String resultId;
	private String score;
	private String number;
	private String showType;
	private String commitType;
	private String showFinish;
	private ArrayList<QuestionContent> contents = new ArrayList<QuestionContent>();

	private ChoicePagerAdapter viewPageAdapter;
	
    private ArrayList<CommitAnswer> answers; //储存提交的答案对象
	
	private static int scrollState;
	private static int preSelectedPage = 0;
	private int position=0;
	private boolean scrollDirection;

	private String isCenter = "";
	private Boolean isCourse;
	private Handler handler;


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		TestPageActivity pageActivity=(TestPageActivity)activity;
		handler=pageActivity.getHandler();
	}
	public ChoicePagerAdapter getAdapter(){
		return viewPageAdapter;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//pu = new PublicUtils(getActivity());
		Bundle args = getArguments();
		resultId = args != null ? args.getString("resultId") : "";
		score = args != null ? args.getString("score") : "";
		number = args != null ? args.getString("number") : "";
		showType = args != null ? args.getString("showType") : "";
		commitType = args != null ? args.getString("commitType") : "";
		showFinish = args != null ? args.getString("showFinish") : "";
		isCourse = args == null || args.getBoolean("isCourse");
		if (args != null && args.getString(Constants.IS_CENTER) != null) {
			isCenter = args.getString(Constants.IS_CENTER);
		}
		contents = (ArrayList<QuestionContent>) (args != null ? args.getSerializable("contents") : "");
		answers = new ArrayList<CommitAnswer>();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (v == null) {
			v = inflater.inflate(R.layout.fragment_view_page, container, false);
			pager = (ViewPager) v.findViewById(R.id.viewPager);
			
			for(int i = 0; i< DownloadData.answerList.size(); i++){
				CommitAnswer commitAnswer = DownloadData.answerList.get(i);
				String questionType = commitAnswer.getQuestionType();
				if(questionType.equals("choice")){
					answers.add(commitAnswer);
				}
			}
			viewPageAdapter = new ChoicePagerAdapter(getActivity(), contents, score,answers,showType,resultId,showFinish,commitType,isCenter,isCourse);
			pager.setAdapter(viewPageAdapter);
			pager.setOffscreenPageLimit(contents.size());
		}
	   pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				position = arg0;
				handler.sendEmptyMessage(200);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				if (arg2!=0) {
					if (scrollState == 1) {//手指按下
					}else if (scrollState==2) {//手指滑动
						if(preSelectedPage == arg0){//往左拉
					        scrollDirection = true;
						}else{//表示往右拉
					        scrollDirection = false;
						}
					}
				}
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			   scrollState = arg0;
			   preSelectedPage = position;
			   //手指滑动完成
			   if(scrollState==0){
				   if(scrollDirection){
					   if(showType.equals("test")){
//						   Log.d("zw--12",preSelectedPage+"**");
						   if(preSelectedPage>0){
							   new CommitTestAnswer(getActivity()).commit_Answer(answers, preSelectedPage - 1, isCenter,isCourse);
						   }
					   }
				   }else {
					   if(showType.equals("test")){
//						   Log.d("zw--12",preSelectedPage+"**");
						   if(preSelectedPage>=0){
							   new CommitTestAnswer(getActivity()).commit_Answer(answers, preSelectedPage +1, isCenter,isCourse);
						   }
					   }
				   }
			   }
			}
		});
		ViewGroup parent = (ViewGroup) v.getParent();
		if (parent != null) {
			parent.removeView(v);
		}
		return v;
	}

	@Override
	protected void requestData() {
	}
	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		Bundle args = getArguments();
		int viewPagePosition = args != null ? args.getInt("pagerChildPosition") : -1;
		if (pager != null && viewPagePosition != -1) {
			pager.setCurrentItem(viewPagePosition);
		}
		super.onResume();
	}
}