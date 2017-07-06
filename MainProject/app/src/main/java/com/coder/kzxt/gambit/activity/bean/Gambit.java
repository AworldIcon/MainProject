package com.coder.kzxt.gambit.activity.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Gambit implements Serializable {
	private static final long serialVersionUID = 1L;
	private String Gid;// 话题id
	private String Gtitle; // 话题标题
	private String GcontentText;//话题内容
	private String GhitNum;// 点击次数
	private String GisLiked;//是否赞过0否1是
	private String GlikeNum;//赞的数量
	private String GpostNum; // 回复次数
	private String GcreatedTime;// 发布时间
	private String GclassName;// 班级名字
	private String GclassId; // 班级id;
	private String GuserId;// 发布者id
	private String GuserName;// 发布者昵称
	private String GuserFace;// 发布者头像
	private String GuserGender; // 发布者性别
	private ArrayList<String> GimgList;// 图片数组
	private String GaudioId;// 语音id
	private String GaudioDuration;// 语音时长
	private String GaudioUrl; // 语音连接
	private String GaudioPlaying;// 是否正在播放语音
	private String GaudioProgress; // 是否正在加载语音
	private ArrayList<String> Gimgs;//图片集合
	private ArrayList<HashMap<String, String>> list_msg; // 话题内容
	private String Gtype;// 话题类型 0是文字话题 1是语音话题 2是文回复 3是语音回复
	private String GpostId;// 一条回复的id
	private String GpostUserId;// 回复者id
	private String GpostCommentNum;// 一条回复的评论数
	private ArrayList<Comment> comments; // 一条回复的评论内容
	private String publicCourse;

	public String getGid() {
		return Gid;
	}

	public void setGid(String gid) {
		Gid = gid;
	}

	public String getGtitle() {
		return Gtitle;
	}

	public void setGtitle(String gtitle) {
		Gtitle = gtitle;
	}

	public String getGhitNum() {
		return GhitNum;
	}

	public void setGhitNum(String ghitNum) {
		GhitNum = ghitNum;
	}

	public String getGpostNum() {
		return GpostNum;
	}

	public void setGpostNum(String gpostNum) {
		GpostNum = gpostNum;
	}

	public String getGcreatedTime() {
		return GcreatedTime;
	}

	public void setGcreatedTime(String gcreatedTime) {
		GcreatedTime = gcreatedTime;
	}

	public String getGuserId() {
		return GuserId;
	}

	public void setGuserId(String guserId) {
		GuserId = guserId;
	}

	public String getGpostUserId() {
		return GpostUserId;
	}

	public void setGpostUserId(String gpostUserId) {
		GpostUserId = gpostUserId;
	}

	public String getGpostId() {
		return GpostId;
	}

	public void setGpostId(String gpostId) {
		GpostId = gpostId;
	}

	public String getGuserName() {
		return GuserName;
	}

	public void setGuserName(String guserName) {
		GuserName = guserName;
	}

	public String getGuserFace() {
		return GuserFace;
	}

	public void setGuserFace(String guserFace) {
		GuserFace = guserFace;
	}

	public String getGuserGender() {
		return GuserGender;
	}

	public void setGuserGender(String guserGender) {
		GuserGender = guserGender;
	}

	public ArrayList<String> getGimgList() {
		return GimgList;
	}

	public void setGimgList(ArrayList<String> gimgList) {
		GimgList = gimgList;
	}

	public String getGaudioId() {
		return GaudioId;
	}

	public void setGaudioId(String gaudioId) {
		GaudioId = gaudioId;
	}

	public String getGaudioDuration() {
		return GaudioDuration;
	}

	public void setGaudioDuration(String gaudioDuration) {
		GaudioDuration = gaudioDuration;
	}

	public String getGaudioUrl() {
		return GaudioUrl;
	}

	public void setGaudioUrl(String gaudioUrl) {
		GaudioUrl = gaudioUrl;
	}

	public String getGaudioPlaying() {
		return GaudioPlaying;
	}

	public void setGaudioPlaying(String gaudioPlaying) {
		GaudioPlaying = gaudioPlaying;
	}

	public String getGaudioProgress() {
		return GaudioProgress;
	}

	public void setGaudioProgress(String gaudioProgress) {
		GaudioProgress = gaudioProgress;
	}

	public ArrayList<HashMap<String, String>> getList_msg() {
		return list_msg;
	}

	public void setList_msg(ArrayList<HashMap<String, String>> list_msg) {
		this.list_msg = list_msg;
	}

	public String getGtype() {
		return Gtype;
	}

	public void setGtype(String gtype) {
		Gtype = gtype;
	}

	public String getGpostCommentNum() {
		return GpostCommentNum;
	}

	public void setGpostCommentNum(String gpostCommentNum) {
		GpostCommentNum = gpostCommentNum;
	}

	public ArrayList<Comment> getComments() {
		return comments;
	}

	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}

	public String getGclassName() {
		return GclassName;
	}

	public void setGclassName(String gclassName) {
		GclassName = gclassName;
	}

	public String getGclassId() {
		return GclassId;
	}

	public void setGclassId(String gclassId) {
		GclassId = gclassId;
	}

	public String getCenter() {
		if (publicCourse == null) {
			return "";
		}
		return publicCourse;
	}

	public void setCenter(String center) {
		this.publicCourse = publicCourse;
	}

	public String getGcontentText() {
		return GcontentText;
	}

	public void setGcontentText(String gcontentText) {
		GcontentText = gcontentText;
	}

	public String getGisLiked() {
		return GisLiked;
	}

	public String getGlikeNum() {
		return GlikeNum;
	}

	public void setGlikeNum(String glikeNum) {
		GlikeNum = glikeNum;
	}

	public void setGisLiked(String gisLiked) {
		GisLiked = gisLiked;
	}

	public ArrayList<String> getGimgs() {
		return Gimgs;
	}

	public void setGimgs(ArrayList<String> gimgs) {
		Gimgs = gimgs;
	}
}
