package com.coder.kzxt.stuwork.activity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpPutBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.dialog.util.CustomDialog;
import com.coder.kzxt.stuwork.entity.AnswerSheet;
import com.coder.kzxt.stuwork.util.DownloadData;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.views.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;

public class AnswerCardActivity extends BaseActivity {
	private ImageView leftImage;
	private TextView title;
	private TextView rightText;
	private ListView mainList;
	private TextView commit;
	private ArrayList<AnswerSheet> answerSheets;
	private MainAdapter mainAdapter;
	private String from;
	private String resultId;
	private String commitType;
	private boolean isCourse;
	private CustomDialog dialog;
	private int workType;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_answer_card);

		from = getIntent().getStringExtra("from"); 
		resultId = getIntent().getStringExtra("resultId");
		commitType = getIntent().getStringExtra("commitType");
		workType=getIntent().getIntExtra("workType",0);
		isCourse = getIntent().getBooleanExtra("isCourse",true);
		title = (TextView) findViewById(R.id.title);
		rightText = (TextView) findViewById(R.id.rightText);
		leftImage = (ImageView) findViewById(R.id.leftImage);
		mainList = (ListView) findViewById(R.id.mainList);
		commit = (TextView) findViewById(R.id.commit);

		GradientDrawable myGrad = (GradientDrawable) commit.getBackground();
		myGrad.setColor(getResources().getColor(R.color.first_theme));// 设置内部颜色

		if(workType==1){
			commit.setText("提交试卷");
		}else {
			commit.setText("提交作业");
		}

		answerSheets = DownloadData.answerSheets;

		mainAdapter = new MainAdapter();
		mainList.setAdapter(mainAdapter);
		title.setText(getResources().getString(R.string.test_card));
		//rightText.setText(getResources().getString(R.string.test_next_time));


		rightText.setVisibility(View.GONE);
		if (from.equals("coerce")) {
			rightText.setVisibility(View.GONE);
			leftImage.setVisibility(View.GONE);
		}

		InintEvent();
	}

	private void InintEvent() {

		leftImage.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AnswerCardActivity.this.finish();
			}
		});

		rightText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				setResult(2);
//				finish();

			}
		});

		commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog = new CustomDialog(AnswerCardActivity.this);
				//dialog.setButtonOne(false);
//				if (TextUtils.isEmpty(commitType)) {
//					dialog.setMessage(getResources().getString(R.string.ensure_submit));
//				} else {
//					dialog.setMessage(getResources().getString(R.string.ensure_submit) + commitType);
//				}
				if(workType==1){
					dialog.setMessage("是否确认提交试卷");
					dialog.setRightText("提交");
				}else {
					dialog.setMessage("是否确认提交作业");
					dialog.setRightText("提交");
				}
				dialog.show();

				dialog.setRightClick(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						if (NetworkUtil.isNetworkAvailable(AnswerCardActivity.this)) {
							new HttpPutBuilder(AnswerCardActivity.this).setClassObj(null).setUrl(isCourse?UrlsNew.TEST_FINISH:UrlsNew.TEST_FINISH_SERVICE)
									.setHttpResult(new HttpCallBack() {
										@Override
										public void setOnSuccessCallback(int requestCode, Object resultBean) {
											setResult(2);
											finish();
										}
										@Override
										public void setOnErrorCallback(int requestCode, int code, String msg) {

										}
									}).setPath(resultId).build();
						} else {
							Toast.makeText(getApplicationContext(), getResources().getString(R.string.net_inAvailable), Toast.LENGTH_SHORT).show();
						}

					}
				});

			}
		});

	}

	private class MainAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return answerSheets.size();
		}

		@Override
		public Object getItem(int position) {
			return answerSheets.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = LayoutInflater.from(AnswerCardActivity.this).inflate(R.layout.item_answer_card, null);
			}

			TextView text = (TextView) convertView.findViewById(R.id.text);
			MyGridView myGridView = (MyGridView) convertView.findViewById(R.id.myGridView);

			AnswerSheet answerSheet = answerSheets.get(position);
			String questionTitle = answerSheet.getQuestionTitle();
			text.setText(questionTitle);

			ArrayList<HashMap<String, String>> arrayList2 = answerSheet.getArrayList();
			myGridView.setAdapter(new GridAdapter(arrayList2, position));
			return convertView;
		}
	}

	private class GridAdapter extends BaseAdapter {

		private ArrayList<HashMap<String, String>> arrayList;
		private int groupPosition;

		public GridAdapter(ArrayList<HashMap<String, String>> arrayList, int groupPosition) {
			this.arrayList = arrayList;
			this.groupPosition = groupPosition;
		}

		@Override
		public int getCount() {
			return arrayList.size();
		}

		@Override
		public Object getItem(int position) {
			return arrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = LayoutInflater.from(AnswerCardActivity.this).inflate(R.layout.item_answer_card_gridview, null);
			}

			final TextView text = (TextView) convertView.findViewById(R.id.text);

			HashMap<String, String> hashMap = arrayList.get(position);
			text.setText((position + 1) + "");

			String istag = hashMap.get("istag");// 是否标记 0代表没有标记
			String isdone = hashMap.get("isdone");// 是否做过 0代表没有做过
			if (istag.equals("1")) {
				text.setBackgroundResource(R.drawable.card_tag);
				text.setTextColor(getResources().getColor(R.color.card_huang));
			} else {
				if (isdone.equals("0")) {
					text.setBackgroundResource(R.drawable.card_unanswer);
					text.setTextColor(getResources().getColor(R.color.font_gray));
				} else {
					text.setBackgroundResource(R.drawable.card_answer);
					text.setTextColor(getResources().getColor(R.color.lan11_new));
				}

			}

			text.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// 如果强制提交答案状态
					if (from.equals("coerce")) {
						// 点击没反应
					} else {

						if (from.equals("testpageactivity")) {

							Intent intent = new Intent();
							intent.putExtra("pagerGroupPosition", groupPosition);
							intent.putExtra("pagerChildPosition", position);
							setResult(1, intent);
							finish();

						} else {

							Intent intent = new Intent();
							intent.setAction(Constants.MY_REFRESG_TESTPAGER);
							intent.putExtra("pagerGroupPosition", groupPosition);
							intent.putExtra("pagerChildPosition", position);
							sendBroadcast(intent);
							finish();
						}
					}

				}
			});

			return convertView;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		if (requestCode == 1 && resultCode == 2) {
			setResult(2);
			finish();
		}

		super.onActivityResult(requestCode, resultCode, arg2);
	}

	@Override
	protected void onResume() {
		super.onResume();
		/**
		 * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
	//	StatService.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		/**
		 * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		//StatService.onPause(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (from.equals("coerce")) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.submit_result_first), Toast.LENGTH_SHORT).show();
			} else {
				AnswerCardActivity.this.finish();
			}

			return true;
		}

		return false;
	}

	@Override
	protected void onDestroy() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		super.onDestroy();
	}

}
