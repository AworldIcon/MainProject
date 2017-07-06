package com.coder.kzxt.stuwork.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class AnswerSheet implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String questionType;//题型标记
	private String questionTitle; //答题卡一级名称
	private ArrayList<HashMap<String, String>> arrayList;//答题卡二级内容
	
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public String getQuestionTitle() {
		return questionTitle;
	}
	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}
	public ArrayList<HashMap<String, String>> getArrayList() {
		return arrayList;
	}
	public void setArrayList(ArrayList<HashMap<String, String>> arrayList) {
		this.arrayList = arrayList;
	}

	
}
