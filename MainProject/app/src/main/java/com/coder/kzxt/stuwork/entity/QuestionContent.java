package com.coder.kzxt.stuwork.entity;

import com.coder.kzxt.stuwork.entity.QuestionChoicesContent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class QuestionContent implements Serializable {

	private static final long serialVersionUID = 1L;
	private String type;// 试题类型
	private String testId;// 试卷id
	private String questionId;// 题目id
	private String score;// 每个题的分数
	private String favorites; // 此题是否收藏
	private String createdTime; // 创建时间
	private String testpaper; // 来源
	private ArrayList<HashMap<String, String>> questionList; // 试题内容集合
	private ArrayList<String> answers;// 回答集合
	private ArrayList<HashMap<String, String>> analysisList; // 试题解析内容集合
	private ArrayList<QuestionChoicesContent> choicesList;// 试题选择内容
	private ArrayList<String> myAnswers; // 我的回答信息
	private String myDeteminChoiceAnswer; // 我的判断题答案
	private String myEssayAnswer; // 我的问答题答案
	private String publicCourse; // 是否是公共课

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getMyDeteminChoiceAnswer() {
		return myDeteminChoiceAnswer;
	}

	public void setMyDeteminChoiceAnswer(String myDeteminChoiceAnswer) {
		this.myDeteminChoiceAnswer = myDeteminChoiceAnswer;
	}

	public String getMyEssayAnswer() {
		return myEssayAnswer;
	}

	public void setMyEssayAnswer(String myEssayAnswer) {
		this.myEssayAnswer = myEssayAnswer;
	}

	public String getFavorites() {
		return favorites;
	}

	public void setFavorites(String favorites) {
		this.favorites = favorites;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getTestpaper() {
		if (testpaper == null) {
			return "";
		}
		return testpaper;
	}

	public void setTestpaper(String testpaper) {
		this.testpaper = testpaper;
	}

	public ArrayList<HashMap<String, String>> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(ArrayList<HashMap<String, String>> questionList) {
		this.questionList = questionList;
	}

	public ArrayList<String> getAnswers() {
		return answers;
	}

	public void setAnswers(ArrayList<String> answers) {
		this.answers = answers;
	}

	public ArrayList<String> getMyAnswers() {
		return myAnswers;
	}

	public void setMyAnswers(ArrayList<String> myAnswers) {
		this.myAnswers = myAnswers;
	}

	public ArrayList<HashMap<String, String>> getAnalysisList() {
		return analysisList;
	}

	public void setAnalysisList(ArrayList<HashMap<String, String>> analysisList) {
		this.analysisList = analysisList;
	}

	public ArrayList<QuestionChoicesContent> getChoicesList() {
		return choicesList;
	}

	public void setChoicesList(ArrayList<QuestionChoicesContent> choicesList) {
		this.choicesList = choicesList;
	}

	public String getCenter() {
		if (publicCourse == null) {
			return "";
		}
		return publicCourse;
	}

	public void setCenter(String center) {
		this.publicCourse = center;
	}
}
