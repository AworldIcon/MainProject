package com.coder.kzxt.stuwork.entity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 要提交的结果
 */
public class CommitAnswer implements Serializable {

	private static final long serialVersionUID = 1L;
	private String questionType ;//问题类型
	private String resultId; //作业id
	private String testId; //试卷id
	private String questionId; //题目id
	private ArrayList<String> resultList;//答案
	private File imgfile;//问答题图片
	
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
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
	
	public String getResultId() {
		return resultId;
	}
	public void setResultId(String resultId) {
		this.resultId = resultId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public ArrayList<String> getResultList() {
		if(resultList==null){
			resultList = new ArrayList<String>();
		}
		return resultList;
	}
	public void setResultList(ArrayList<String> resultList) {
		this.resultList = resultList;
	}
	public File getImgfile() {
		return imgfile;
	}
	public void setImgfile(File imgfile) {
		this.imgfile = imgfile;
	}
	
	
}
