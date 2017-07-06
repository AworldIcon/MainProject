package com.coder.kzxt.stuwork.util;

import com.coder.kzxt.stuwork.entity.AnswerSheet;
import com.coder.kzxt.stuwork.entity.CommitAnswer;
import com.coder.kzxt.stuwork.entity.QuestionContent;
import java.util.ArrayList;

public class DownloadData {
	/**
	 * 下载数据
	 */
//	public static ArrayList<HashMap<String, String>> groupList = new ArrayList<HashMap<String, String>>();
//	public static ArrayList<ArrayList<HashMap<String, String>>> childList = new ArrayList<ArrayList<HashMap<String, String>>>();

	/**
	 * 答题卡数据
	 */
	public static ArrayList<AnswerSheet> answerSheets = new ArrayList<AnswerSheet>();

	/**
	 * 提交的答案数据
	 */
	public static ArrayList<CommitAnswer> answerList = new ArrayList<CommitAnswer>();

	/**
	 * 收藏数据
	 */
	public static ArrayList<QuestionContent> collectionList = new ArrayList<QuestionContent>();
	
	/**
	 * 收藏公共课数据
	 */
	public static ArrayList<QuestionContent> collectionPublicCourseList = new ArrayList<QuestionContent>();
}
