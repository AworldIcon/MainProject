package com.coder.kzxt.stuwork.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.stuwork.util.DownloadData;
import com.coder.kzxt.stuwork.entity.AnswerSheet;
import com.coder.kzxt.stuwork.entity.CommitAnswer;

import java.util.ArrayList;
import java.util.HashMap;

public class FillListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<String> arrayList;
	private int arg1;
	// 定义一个HashMap，用来存放EditText的值，Key是position
	private HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
	private CommitAnswer answer;
	private ArrayList<String> strings;
	private ArrayList<String> myAnswers;

	public FillListAdapter(Context context, ArrayList<String> arrayList, int arg1, CommitAnswer answer, ArrayList<String> myAnswers) {
		super();
		this.context = context;
		this.arrayList = arrayList;
		this.arg1 = arg1;
		this.answer = answer;
		this.myAnswers = myAnswers;
		strings = myAnswers;

		if (strings.size() == 0) {
			strings = new ArrayList<String>();
			for (int i = 0; i < this.arrayList.size(); i++) {
				strings.add(i, "");
			}
		}

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
		convertView = LayoutInflater.from(context).inflate(R.layout.fill_list_item, null);
		EditText fill_et = (EditText) convertView.findViewById(R.id.fill_et);

		if (myAnswers.size() > 0) {
			String string = myAnswers.get(position);

			if (TextUtils.isEmpty(string)) {

			} else {
				// 设置自己填写的数据
				fill_et.setText(string);
			}

		}


		fill_et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				for (int i = 0; i < DownloadData.answerSheets.size(); i++) {
					AnswerSheet answerSheet = DownloadData.answerSheets.get(i);
					String questionType = answerSheet.getQuestionType();
					if (questionType.equals("fill")) {
						ArrayList<HashMap<String, String>> arrayList2 = answerSheet.getArrayList();
						HashMap<String, String> hashMap = arrayList2.get(arg1);
						if (s.toString().length() > 0) {
							hashMap.put("isdone", "1");
						} else {
							hashMap.put("isdone", "0");
						}

					}
				}

				// 将editText中改变的值设置的HashMap中
				hashMap.put(position, s.toString());
				strings.set(position, s.toString());
				answer.setResultList(strings);
			}
		});

		return convertView;
	}

	/**
	 * 返回填空题的答案数据
	 */
	private HashMap<Integer, String> getFillDatas() {
		return hashMap;

	}

}
