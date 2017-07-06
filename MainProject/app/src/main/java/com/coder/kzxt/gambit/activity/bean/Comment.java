package com.coder.kzxt.gambit.activity.bean;

import java.io.Serializable;

public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;
	private String Cid;//评论id
	private String Ccontent;//评论内容
	private String CcreatedTime;//评论时间
	private String CuserId;//评论者id
	private String CuserName;//评论的昵称
	private String CreplyName;//被回复的昵称
	public String getCid() {
		return Cid;
	}
	public void setCid(String cid) {
		Cid = cid;
	}
	public String getCcontent() {
		return Ccontent;
	}
	public void setCcontent(String ccontent) {
		Ccontent = ccontent;
	}
	public String getCcreatedTime() {
		return CcreatedTime;
	}
	public void setCcreatedTime(String ccreatedTime) {
		CcreatedTime = ccreatedTime;
	}
	public String getCuserId() {
		return CuserId;
	}
	public void setCuserId(String cuserId) {
		CuserId = cuserId;
	}
	public String getCuserName() {
		return CuserName;
	}
	public void setCuserName(String cuserName) {
		CuserName = cuserName;
	}
	public String getCreplyName() {
		return CreplyName;
	}
	public void setCreplyName(String creplyName) {
		CreplyName = creplyName;
	}
	
	
	
}
