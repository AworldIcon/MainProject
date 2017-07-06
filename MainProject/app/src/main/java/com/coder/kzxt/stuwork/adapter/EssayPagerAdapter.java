package com.coder.kzxt.stuwork.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.stuwork.util.CommitTestAnswer;
import com.coder.kzxt.stuwork.util.DownloadData;
import com.coder.kzxt.stuwork.activity.AnswerCardActivity;
import com.coder.kzxt.stuwork.activity.TestPageActivity;
import com.coder.kzxt.stuwork.entity.AnswerSheet;
import com.coder.kzxt.stuwork.entity.CommitAnswer;
import com.coder.kzxt.stuwork.entity.QuestionContent;
import com.coder.kzxt.stuwork.util.TestPaperFavorites;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.views.MyListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * 问答题 adapter
 * @author Administrator
 * 
 */
public class EssayPagerAdapter extends PagerAdapter {
	private final ArrayList<View> pagerViews;
	private final ArrayList<QuestionContent> arrayList;
	private String resultId;
	private String score;
	private Activity context;
	private int favoritesIndex;
	private ArrayList<CommitAnswer> answers;
	private ArrayList<String> strings;
	private String showType;
	private String commitType;
	private String showFinish;
	private String isCenter;
	private Boolean isCourse;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case Constants.FAVORITES:
				arrayList.get(favoritesIndex).setFavorites("1");
				notifyDataSetChanged();

				break;

			case Constants.CANCE_FAVORITES:

				arrayList.get(favoritesIndex).setFavorites("0");
				notifyDataSetChanged();

				break;

			}

			super.handleMessage(msg);
		}

	};

	public void cleanImageView() {
		if (pagerViews != null) {
			pagerViews.clear();
			context = null;
		}
	}

	public EssayPagerAdapter(final Activity context, final ArrayList<QuestionContent> arrayList, String score, ArrayList<CommitAnswer> answers
			, String showType, String resultId, String showFinish, String commitType, String workType,Boolean isCourse) {
		this.context = context;
		this.arrayList = arrayList;
		this.resultId = resultId;
		this.score = score;
		this.pagerViews = new ArrayList<View>();
		this.answers = answers;
		this.showType =showType;
		this.commitType = commitType;
		this.showFinish = showFinish;
		this.isCenter = workType;
		this.isCourse = isCourse;

		strings = new ArrayList<String>();

		if ((arrayList != null) && (arrayList.size() > 0)) {
			for (int i = 0; i < arrayList.size(); i++) {
				final View v = View.inflate(this.context, R.layout.item_essay, null);
				this.pagerViews.add(v);
			}
		}
		
	}

	public int getCount() {
		if (this.pagerViews != null) {
			return this.pagerViews.size();
		}
		return 0;
	}

	public boolean isViewFromObject(final View arg0, final Object arg1) {
		return arg0 == arg1;
	}

	public Object instantiateItem(final View arg0, final int arg1) {
		final View v = this.pagerViews.get(arg1);

		MyListView questionsTitleList = (MyListView) v.findViewById(R.id.questionsTitleList);

		final TextView tag = (TextView) v.findViewById(R.id.tag);
		final TextView favorites = (TextView) v.findViewById(R.id.favorites);
		final EditText edit = (EditText) v.findViewById(R.id.edit);
		final TextView addImageText = (TextView) v.findViewById(R.id.addImageText);

		final RelativeLayout imageRely = (RelativeLayout) v.findViewById(R.id.imageRely);
		final ImageView img = (ImageView) v.findViewById(R.id.img);
		final ImageView delImage = (ImageView) v.findViewById(R.id.delImage);

		final TextView finishTesT = (TextView) v.findViewById(R.id.finishTesT);

		final String testId = arrayList.get(arg1).getTestId();// 试卷id
		final String questionId = arrayList.get(arg1).getQuestionId();// 题目id
		final String isfavorites = arrayList.get(arg1).getFavorites();// 是否收藏

		 if(showType.equals("test")&&arrayList.size()-1==arg1&&showFinish.equals("1")){
			 finishTesT.setVisibility(View.VISIBLE);
		 }else {
			 finishTesT.setVisibility(View.GONE);
		 }
		
		// 未收藏
		if (isfavorites.equals("0")) {
			favorites.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorites, 0, 0, 0);
			favorites.setText(context.getResources().getString(R.string.favorite));
			// 已收藏
		} else {
			favorites.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorites_select, 0, 0, 0);
			favorites.setText(context.getResources().getString(R.string.cancel));
		}

		tag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				for (int i = 0; i < DownloadData.answerSheets.size(); i++) {
					AnswerSheet answerSheet = DownloadData.answerSheets.get(i);
					String questionType = answerSheet.getQuestionType();
					if (questionType.equals("essay")) {
						ArrayList<HashMap<String, String>> arrayList2 = answerSheet.getArrayList();
						HashMap<String, String> hashMap = arrayList2.get(arg1);
						String string = hashMap.get("istag");
						// 如果此题没有标记过
						if (string.equals("0")) {
							
							tag.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tag, 0, 0, 0);
							tag.setText(context.getResources().getString(R.string.cancel));
							hashMap.put("istag", "1");
						} else {
							
							tag.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cance_tag, 0, 0, 0);
							tag.setText(context.getResources().getString(R.string.tag));
							hashMap.put("istag", "0");
						}

					}
				}

			}
		});

		favorites.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				favoritesIndex = arg1;
				new TestPaperFavorites(context).comit_favourite( isfavorites, favorites, testId, questionId, handler, isCenter);
			}
		});

		if(arrayList.get(arg1).getMyAnswers().size()>0){
			edit.setText(arrayList.get(arg1).getMyAnswers().get(0));
		}
		edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				strings.clear();
			}

			@Override
			public void afterTextChanged(Editable s) {

				for (int i = 0; i < DownloadData.answerSheets.size(); i++) {
					AnswerSheet answerSheet = DownloadData.answerSheets.get(i);
					String questionType = answerSheet.getQuestionType();
					if (questionType.equals("essay")) {
						ArrayList<HashMap<String, String>> arrayList2 = answerSheet.getArrayList();
						HashMap<String, String> hashMap = arrayList2.get(arg1);
						if (s.toString().length() > 0) {
							hashMap.put("isdone", "1");
						} else {
							hashMap.put("isdone", "0");
						}

					}
				}

				strings.add(s.toString());
				answers.get(arg1).setResultList(strings);
			}
		});
		edit.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if ((v.getId() == R.id.posters_text && canVerticalScroll(edit))) {
					v.getParent().requestDisallowInterceptTouchEvent(true);
					if (event.getAction() == MotionEvent.ACTION_UP) {
						v.getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
				return false;
			}
		});
		TextView question_type_tx = (TextView) v.findViewById(R.id.question_type_tx);
		TextView allScore = (TextView) v.findViewById(R.id.allScore);
		TextView currentNum = (TextView) v.findViewById(R.id.currentNum);
		TextView questionNum = (TextView) v.findViewById(R.id.questionNum);
		TextView itemScore = (TextView) v.findViewById(R.id.itemScore);
		DecimalFormat decimalFormat=new DecimalFormat("0.0");

		question_type_tx.setText(context.getResources().getString(R.string.questions_topic));
		allScore.setText("共" + decimalFormat.format(Double.valueOf(score))  + "分");
		questionNum.setText("/ " + String.valueOf(arrayList.size()));
		currentNum.setText(String.valueOf(arg1 + 1));
		itemScore.setText((arg1 + 1) + ".  (" + decimalFormat.format( Double.valueOf(arrayList.get(arg1).getScore()))+ "分)");

		questionsTitleList.setAdapter(new TextImgAdapter(context, arrayList.get(arg1).getQuestionList(),""));
		finishTesT.setVisibility(View.GONE);//不再显示此按钮

		finishTesT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//
				new CommitTestAnswer(context).commit_Answer(answers, answers.size() - 1,isCenter,isCourse);
//
				Intent intent = new Intent(context, AnswerCardActivity.class);
				intent.putExtra("from", "essaypageradapter");
				intent.putExtra("resultId", resultId);
				intent.putExtra("commitType", commitType);
				intent.putExtra("isCourse", isCourse);
				intent.putExtra(Constants.IS_CENTER,isCenter);
				context.startActivityForResult(intent, 1);
			}
		});

		
		 RelativeLayout answer_Ly = (RelativeLayout)v.findViewById(R.id.answer_Ly);
		 RelativeLayout true_answer_Ly = (RelativeLayout)v.findViewById(R.id.true_answer_Ly);
		 LinearLayout analysis_ly = (LinearLayout)v.findViewById(R.id.analysis_ly);
		 TextView analysis_tx = (TextView)v.findViewById(R.id.analysis_tx);
		 MyListView answerListView = (MyListView)v.findViewById(R.id.answerListView);
		 MyListView trueanswerListView = (MyListView)v.findViewById(R.id.trueanswerListView);
		 MyListView analysis_lv = (MyListView)v.findViewById(R.id.analysis_lv);
	
		    if(showType.equals(TestPageActivity.TEST_PAGE_STATUS_TEST)){
	        	 answer_Ly.setVisibility(View.GONE);
	        	 true_answer_Ly.setVisibility(View.GONE);
				 analysis_ly.setVisibility(View.GONE);
				 tag.setVisibility(View.VISIBLE);
	        	 
	         }else {
	        	 tag.setVisibility(View.GONE);
				 answer_Ly.setVisibility(View.VISIBLE);
				 true_answer_Ly.setVisibility(View.VISIBLE);
				 analysis_ly.setVisibility(View.VISIBLE);
				 
				 //显示我的答案
				 if(arrayList.get(arg1).getMyAnswers().size()>0){
					   String string = arrayList.get(arg1).getMyAnswers().get(0);
						if (!TextUtils.isEmpty(string)) {
							try {
								ArrayList<HashMap<String, String>> questionList = new ArrayList<HashMap<String, String>>();
								JSONArray stemArray = new JSONArray(string);
								if (stemArray.length() > 0) {
									for (int j = 0; j < stemArray.length(); j++) {
										HashMap<String, String> hashMap = new HashMap<String, String>();
										JSONObject stemObject = stemArray.getJSONObject(j);
										hashMap.put("type_msg", stemObject.getString("type"));
										hashMap.put("content", stemObject.getString("content"));
										questionList.add(hashMap);
									}
									answerListView.setVisibility(View.VISIBLE);
									answerListView.setAdapter(new TextImgAdapter(context, questionList,""));
								}else{
									answerListView.setVisibility(View.GONE);}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
				 }
				 	
				    //显示正确答案
					if(arrayList.get(arg1).getAnswers()!=null){
					   if(arrayList.get(arg1).getAnswers().size()>0){
						   String string = arrayList.get(arg1).getAnswers().get(0);
							if (!TextUtils.isEmpty(string)) {
								try {
									ArrayList<HashMap<String, String>> questionList = new ArrayList<HashMap<String, String>>();
									JSONArray stemArray = new JSONArray(string);
									if (stemArray.length() > 0) {
										for (int j = 0; j < stemArray.length(); j++) {
											HashMap<String, String> hashMap = new HashMap<String, String>();
											JSONObject stemObject = stemArray.getJSONObject(j);
											hashMap.put("type_msg", stemObject.getString("type"));
											hashMap.put("content", stemObject.getString("content"));
											questionList.add(hashMap);
										}
										trueanswerListView.setVisibility(View.VISIBLE);
										trueanswerListView.setAdapter(new TextImgAdapter(context, questionList,""));
									}else{
										trueanswerListView.setVisibility(View.GONE);}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						   
					   }
					}
					 
				 
				   // 如果有解析有内容
				  if(arrayList.get(arg1).getAnalysisList().size()>0){
					  analysis_tx.setText(context.getResources().getString(R.string.analysis)+"：");
					  analysis_lv.setAdapter(new TextImgAdapter(context, arrayList.get(arg1).getAnalysisList(),showType));
					  }else {
					   analysis_tx.setText(context.getResources().getString(R.string.analysis_no));
				  }
				 
			 }
		 

		((ViewPager) arg0).addView(this.pagerViews.get(arg1));
		return v;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	public void destroyItem(final View arg0, final int arg1, final Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}
	public void Commit(int position,int type,boolean isturn){
		new CommitTestAnswer(context).commit_Answer(answers, position,isCenter,isCourse);
//
		if(isturn){
			Intent intent = new Intent(context, AnswerCardActivity.class);
			intent.putExtra("from", "essaypageradapter");
			intent.putExtra("resultId", resultId);
			intent.putExtra("workType", type);
			intent.putExtra("commitType", commitType);
			intent.putExtra("isCourse", isCourse);
			intent.putExtra(Constants.IS_CENTER,isCenter);
			context.startActivityForResult(intent, 1);
		}

	}
	private boolean canVerticalScroll(EditText editText) {
		//滚动的距离
		int scrollY = editText.getScrollY();
		// 控件内容的总高度
		int scrollRange = editText.getLayout().getHeight();    //控件实际显示的高度
		int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
		//控件内容总高度与实际显示高度的差值
		int scrollDifference = scrollRange - scrollExtent;
		if(scrollDifference == 0) {      return false;    }
		return (scrollY > 0) || (scrollY < scrollDifference - 1);
	}
}
