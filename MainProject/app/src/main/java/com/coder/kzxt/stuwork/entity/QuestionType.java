package com.coder.kzxt.stuwork.entity;

import java.io.Serializable;

public class QuestionType implements Serializable {
	private static final long serialVersionUID = 1L;
	private String questionTitle;//名称
	private String questionScore;//总分
	private String questionNumber;//数量
	private String questionType;//类型
	public String getQuestionTitle() {
		return questionTitle;
	}
	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}
	public String getQuestionScore() {
		return questionScore;
	}
	public void setQuestionScore(String questionScore) {
		this.questionScore = questionScore;
	}
	public String getQuestionNumber() {
		return questionNumber;
	}
	public void setQuestionNumber(String questionNumber) {
		this.questionNumber = questionNumber;
	}
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	
	
	
	
}

