package com.coder.kzxt.stuwork.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.stuwork.util.CommitTestAnswer;
import com.coder.kzxt.stuwork.util.DownloadData;
import com.coder.kzxt.stuwork.util.TestPaperFavorites;
import com.coder.kzxt.stuwork.util.TextUitl;
import com.coder.kzxt.stuwork.activity.AnswerCardActivity;
import com.coder.kzxt.stuwork.activity.TestPageActivity;
import com.coder.kzxt.stuwork.entity.AnswerSheet;
import com.coder.kzxt.stuwork.entity.CommitAnswer;
import com.coder.kzxt.stuwork.entity.QuestionContent;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.views.MyListView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 判断适配器
 * @author pc
 *
 */
public class DeteminPagerAdapter extends PagerAdapter {
	private final ArrayList<View> pagerViews;
	private final ArrayList<QuestionContent> arrayList;
	private String resultId;
	private String score;
	private Activity context;
	private int favoritesIndex;
	private ArrayList<CommitAnswer> answers;
    private String showType;
	private String showFinish;
	private String commitType;
	private String isCenter;
	private Boolean isCourse;

	private Handler handler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			
		   switch(msg.what){
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

	public DeteminPagerAdapter(final Activity context, final ArrayList<QuestionContent> arrayList,
							   String score, ArrayList<CommitAnswer> answers, String showType, String resultId, String showFinish, String commitType, String workType,Boolean isCourse) {
		this.context = context;
		this.arrayList = arrayList;
		this.resultId = resultId;
		this.score = score;
		this.pagerViews = new ArrayList<View>();
		this.answers = answers;
		this.showType = showType;
		this.commitType = commitType;
		this.showFinish = showFinish;
		this.isCenter = workType;
		this.isCourse = isCourse;
		
		if ((arrayList != null) && (arrayList.size() > 0)) {
			for (int i = 0; i < arrayList.size(); i++) {
				final View v = View.inflate(this.context, R.layout.item_detemin, null);
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
		final RadioButton detemin_correct = (RadioButton) v.findViewById(R.id.detemin_correct);
		final RadioButton detemin_error = (RadioButton) v.findViewById(R.id.detemin_error);

		final TextView tag = (TextView) v.findViewById(R.id.tag);
		final TextView favorites = (TextView) v.findViewById(R.id.favorites);

		final TextView finishTesT = (TextView) v.findViewById(R.id.finishTesT);
		
		final String testId = arrayList.get(arg1).getTestId();// 试卷id
		final String questionId = arrayList.get(arg1).getQuestionId();// 题目id
		final String isfavorites = arrayList.get(arg1).getFavorites();// 是否收藏
		String myDeteminChoice = arrayList.get(arg1).getMyDeteminChoiceAnswer();//我选择的答案
		
		 if(showType.equals("test")&&arrayList.size()-1==arg1&&showFinish.equals("1")){
			 finishTesT.setVisibility(View.VISIBLE);
		 }else {
			 finishTesT.setVisibility(View.GONE);
		 }
		
		if(myDeteminChoice!=null){
			if(myDeteminChoice.equals("1")){
		    	detemin_correct.setChecked(true);
		    	detemin_correct.setTextColor(context.getResources().getColor(R.color.first_theme));
			 }else {
				detemin_error.setChecked(true);
				detemin_error.setTextColor(context.getResources().getColor(R.color.first_theme));
			 }
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
				
				for(int i = 0; i< DownloadData.answerSheets.size(); i++){
					AnswerSheet answerSheet = DownloadData.answerSheets.get(i);
					String questionType = answerSheet.getQuestionType();
					if(questionType.equals("determine")){
						ArrayList<HashMap<String, String>> arrayList2 = answerSheet.getArrayList();
						HashMap<String, String> hashMap = arrayList2.get(arg1);
						String string = hashMap.get("istag");
						//如果此题没有标记过
						if(string.equals("0")){
							
							tag.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tag, 0, 0, 0);
							tag.setText(context.getResources().getString(R.string.cancel));
							hashMap.put("istag", "1");
						}else {
							
							tag.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cance_tag, 0, 0, 0);
							tag.setText(context.getResources().getString(R.string.tag));
							hashMap.put("istag", "0");
						}
				    		
					}
				}
				
				
			}
		});
		
		
		detemin_correct.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				detemin_correct.setTextColor(context.getResources().getColor(R.color.first_theme));
				detemin_error.setTextColor(context.getResources().getColor(R.color.font_black));
				setAnswerCard(arg1);
				ArrayList<String> strings = new ArrayList<String>();
				strings.add("1");
				answers.get(arg1).setResultList(strings);
				
			}
		});
		
		detemin_error.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				detemin_error.setTextColor(context.getResources().getColor(R.color.first_theme));
				detemin_correct.setTextColor(context.getResources().getColor(R.color.font_black));
				setAnswerCard(arg1);
				ArrayList<String> strings = new ArrayList<String>();
				strings.add("0");
				answers.get(arg1).setResultList(strings);
				
			}
		});
		

		favorites.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				favoritesIndex = arg1;
				new TestPaperFavorites(context).comit_favourite( isfavorites, favorites, testId, questionId, handler, isCenter);
			}
		});

		TextView question_type_tx = (TextView) v.findViewById(R.id.question_type_tx);
		TextView allScore = (TextView) v.findViewById(R.id.allScore);
		TextView currentNum = (TextView) v.findViewById(R.id.currentNum);
		TextView questionNum = (TextView) v.findViewById(R.id.questionNum);
		TextView itemScore = (TextView) v.findViewById(R.id.itemScore);
		DecimalFormat decimalFormat=new DecimalFormat("0.0");

		question_type_tx.setText(context.getResources().getString(R.string.judge_topic));
		allScore.setText("/"+decimalFormat.format(Double.valueOf(score)) +"分");
		questionNum.setText("/ "+ String.valueOf(arrayList.size()));
		currentNum.setText(String.valueOf(arg1+1));
		itemScore.setText((arg1 + 1)+".  ("+ decimalFormat.format( Double.valueOf(arrayList.get(arg1).getScore()))+"分)");

		questionsTitleList.setAdapter(new TextImgAdapter(context, arrayList.get(arg1).getQuestionList(),""));
		finishTesT.setVisibility(View.GONE);//不再显示此按钮

		finishTesT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new CommitTestAnswer(context).commit_Answer(answers, answers.size() - 1,isCenter,isCourse);

				for (int i = 0; i <answers.size() ; i++) {
					CommitAnswer commitAnswer = answers.get(i);
//					Log.v("tangcy",commitAnswer.getResultList().get(0));
				}

				Intent intent = new Intent(context, AnswerCardActivity.class);
				intent.putExtra("from", "essaypageradapter");
				intent.putExtra("resultId", resultId);
				intent.putExtra("commitType", commitType);
				intent.putExtra("isCourse", isCourse);
				intent.putExtra(Constants.IS_CENTER,isCenter);
				context.startActivityForResult(intent, 1);
			}
		});

		
		
		 LinearLayout answer_Ly = (LinearLayout)v.findViewById(R.id.answer_Ly);
		 LinearLayout analysis_ly = (LinearLayout)v.findViewById(R.id.analysis_ly);
		 TextView yourAnswer = (TextView) v.findViewById(R.id.yourAnswer);
		 TextView correctAnswer = (TextView)v.findViewById(R.id.correctAnswer);
		 TextView analysis_tx = (TextView)v.findViewById(R.id.analysis_tx);
		 MyListView analysis_lv = (MyListView)v.findViewById(R.id.analysis_lv);

         if(showType.equals(TestPageActivity.TEST_PAGE_STATUS_TEST)){
        	 answer_Ly.setVisibility(View.GONE);
			 analysis_ly.setVisibility(View.GONE);
			 tag.setVisibility(View.VISIBLE);
        	 
         }else {
        	 tag.setVisibility(View.GONE);
			 answer_Ly.setVisibility(View.VISIBLE);
			 analysis_ly.setVisibility(View.VISIBLE);
			 
			 if(arrayList.get(arg1).getMyAnswers().size()>0){
				 yourAnswer.setText(context.getResources().getString(R.string.your_result)+ TextUitl.detemineAnswersText(arrayList.get(arg1).getMyAnswers().get(0)));
			 }

			 if(arrayList.get(arg1).getAnswers()!=null){
				 correctAnswer.setText(context.getResources().getString(R.string.correct_result)+TextUitl.detemineAnswersText(arrayList.get(arg1).getAnswers().get(0)));
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

	/**
	 * @param arg1
	 * 记录答题卡数据
	 */
	private  void setAnswerCard(int arg1){
		
		for(int i=0;i<DownloadData.answerSheets.size();i++){
			AnswerSheet answerSheet = DownloadData.answerSheets.get(i);
			String questionType = answerSheet.getQuestionType();
			if(questionType.equals("determine")){
				ArrayList<HashMap<String, String>> arrayList2 = answerSheet.getArrayList();
				HashMap<String, String> hashMap = arrayList2.get(arg1);
			    hashMap.put("isdone", "1");
			}
		}
		
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
}
