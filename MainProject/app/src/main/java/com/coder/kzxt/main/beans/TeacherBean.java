package com.coder.kzxt.main.beans;

import java.io.Serializable;

public class TeacherBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2077705919126257219L;
	private String id;
	private String nickname;
	private String title;
	private String following;
	private String follower;
	private String avatar;

	public String getId() {
              if (id==null) {
                  return "";
              }
              else{
                  return id;
              }
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickname() {
              if (nickname==null) {
                  return "";
              }
              else{
                  return nickname;
              }
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getTitle() {
              if (title==null) {
                  return "";
              }
              else{
                  return title;
              }
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFollowing() {
              if (following==null) {
                  return "";
              }
              else{
                  return following;
              }
	}

	public void setFollowing(String following) {
		this.following = following;
	}

	public String getFollower() {
              if (follower==null) {
                  return "";
              }
              else{
                  return follower;
              }
	}

	public void setFollower(String follower) {
		this.follower = follower;
	}

	public String getAvatar() {
              if (avatar==null) {
                  return "";
              }
              else{
                  return avatar;
              }
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}
