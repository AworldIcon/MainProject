package com.coder.kzxt.stuwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.image.ImageLoad;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.poster.activity.Show_Image_Activity;
import com.coder.kzxt.stuwork.util.DownloadData;
import com.coder.kzxt.stuwork.entity.AnswerSheet;
import com.coder.kzxt.stuwork.entity.CommitAnswer;
import com.coder.kzxt.stuwork.entity.QuestionChoicesContent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * 选择题 选择list的 item adapter
 * 
 * @author Administrator
 * 
 */
public class ChoiceListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<QuestionChoicesContent> arrayList;
	private String questionType;
	private int arg1;
	private CommitAnswer answer;
	private ArrayList<String> strings;

	public ChoiceListAdapter(Context context, ArrayList<QuestionChoicesContent> arrayList, String questionType, int arg1, CommitAnswer answer) {
		super();
		this.context = context;
		this.arrayList = arrayList;
		this.questionType = questionType;
		this.arg1 = arg1;
		this.answer = answer;
		strings = new ArrayList<String>();
	}

	@Override
	public int getCount() {                                                            
		return arrayList == null ? 0 : arrayList.size();
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
			convertView = LayoutInflater.from(context).inflate(R.layout.choice_item, null);
		}

		RelativeLayout option_zong_ly = (RelativeLayout) convertView.findViewById(R.id.option_zong_ly);
		TextView option_tx = (TextView) convertView.findViewById(R.id.option_tx);
		TextView content = (TextView) convertView.findViewById(R.id.content);
		ListView option_lv = (ListView) convertView.findViewById(R.id.option_lv);
		ImageView pitchon_up = (ImageView) convertView.findViewById(R.id.pitchon_up);
		ImageView pitchon_down = (ImageView) convertView.findViewById(R.id.pitchon_down);
		final QuestionChoicesContent questionChoicesContent = arrayList.get(position);
		String key = questionChoicesContent.getKey();
		option_tx.setText(key + "、");

		String isChoices = questionChoicesContent.getIsChoices();
		if (isChoices.equals("0")) {
			pitchon_up.setVisibility(View.VISIBLE);
			pitchon_down.setVisibility(View.GONE);
			option_tx.setTextColor(context.getResources().getColor(R.color.font_black));
		} else {
			pitchon_up.setVisibility(View.GONE);
			pitchon_down.setVisibility(View.VISIBLE);
			option_tx.setTextColor(context.getResources().getColor(R.color.first_theme));
		}
		content.setText(questionChoicesContent.getArrayList().get(0).get("content"));
		final optionContentAdapter contentAdapter = new optionContentAdapter(context, questionChoicesContent.getArrayList(), position);

		//这次重构没有用这个
		//option_lv.setAdapter(contentAdapter);
		// 下面还有同样的事件
		option_zong_ly.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				setRescouceAndView(questionChoicesContent, contentAdapter);
			}

		});

		// 重新计算内部listview的高度
		setListViewHeightBasedOnChildren(option_lv);

		return convertView;
	}

	private void setRescouceAndView(QuestionChoicesContent questionChoicesContent, optionContentAdapter contentAdapter) {

		if (questionType.equals("single_choice")) {
			for (QuestionChoicesContent bean : arrayList) {
				bean.setIsChoices("0");
			}
			questionChoicesContent.setIsChoices("1");
		} else {
			if (questionChoicesContent.getIsChoices().equals("0")) {
				questionChoicesContent.setIsChoices("1");
			} else {
				questionChoicesContent.setIsChoices("0");
			}
		}

		// 如果是单选题选择之前先清空集合
		strings.clear();
		for (int i = 0; i < arrayList.size(); i++) {
			QuestionChoicesContent content = arrayList.get(i);
			if (content.getIsChoices().equals("1")) {
				strings.add(String.valueOf(i));
			}
		}

		for (int i = 0; i < DownloadData.answerSheets.size(); i++) {
			AnswerSheet answerSheet = DownloadData.answerSheets.get(i);
			String questionType2 = answerSheet.getQuestionType();
			if (questionType2.equals(questionType)) {
				ArrayList<HashMap<String, String>> arrayList2 = answerSheet.getArrayList();
				HashMap<String, String> hashMap = arrayList2.get(arg1);

				if (strings.size() > 0) {
					hashMap.put("isdone", "1");
				} else {
					hashMap.put("isdone", "0");
				}
			}
		}

		answer.setResultList(strings);
		ChoiceListAdapter.this.notifyDataSetChanged();
		contentAdapter.notifyDataSetChanged();
	}

	class optionContentAdapter extends BaseAdapter {
		private Context context;
		private ArrayList<HashMap<String, String>> list;
		private int position;


		optionContentAdapter(Context context, ArrayList<HashMap<String, String>> list, int position) {
			this.context = context;
			this.list = list;
			this.position = position;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int index, View convertView, ViewGroup parent) {

			convertView = LayoutInflater.from(context).inflate(R.layout.question_content_item, null);
			LinearLayout content_ly = (LinearLayout) convertView.findViewById(R.id.content_ly);

			final QuestionChoicesContent questionChoicesContent = arrayList.get(position);

			HashMap<String, String> hashMap = list.get(index);
			String type_msg = hashMap.get("type_msg");
			if (type_msg.equals("text")) {
				String content = hashMap.get("content");
				addTextView(content_ly, content, questionChoicesContent.getIsChoices());

			} else if (type_msg.equals("img")) {
				String content = hashMap.get("content");
				addImagView(content_ly, content);
			}

			content_ly.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setRescouceAndView(questionChoicesContent, optionContentAdapter.this);
				}
			});

			return convertView;
		}

		@Override
		public boolean isEnabled(int position) {
			return false;
		}

	}

	private void addTextView(LinearLayout myLayout, String texturl, String isChoices) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout v = (LinearLayout) inflater.inflate(R.layout.option_text, null);
		TextView textView = (TextView) v.findViewById(R.id.title_tx);
		v.setLayoutParams(params);
		textView.setText(texturl.trim());
		if (isChoices.equals("0")) {
			textView.setTextColor(context.getResources().getColor(R.color.font_black));
		} else {
			textView.setTextColor(context.getResources().getColor(R.color.first_theme));
		}
		myLayout.addView(v);

	}

	private void addImagView(LinearLayout myLayout, String texturl) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// 解析html
		Document document = Jsoup.parse(texturl);
		Elements imgElements = document.getElementsByTag("img");
		final String imageUrl = imgElements.attr("src");

		LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout v = (RelativeLayout) inflater.inflate(R.layout.option_img, null);
		ImageView add_imgview = (ImageView) v.findViewById(R.id.add_imgview);
		v.setLayoutParams(params);

		ImageLoad.loadImage(context,imageUrl,R.drawable.default_question_clalss,R.drawable.default_question_clalss,add_imgview);

		add_imgview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context, Show_Image_Activity.class);
				intent.putExtra("imgurl", imageUrl);
				context.startActivity(intent);

			}
		});

		myLayout.addView(v);
	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		((MarginLayoutParams) params).setMargins(10, 10, 10, 10);
		listView.setLayoutParams(params);
	}

}
