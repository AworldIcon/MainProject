package com.coder.kzxt.main.beans;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

public class MyCourseBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6131853190190147314L;
	private String title;
	private String courseName;
	private String subtitle;
	private String status; // 是否发布 published unpublished
	private String classNum;
	private String type;
	private String price;
	private String expiryDay;
	private String timeLength; // 课程视频总时长(分钟)
	private String lessonNum; // 课时
	private String categoryId;
	private String studentNum; // 学习人数
	private String studyNum; // 学习人数
	private String appleForbid;
	private String pic;
	private List<TeacherBean> teachers;
	private String courseId;
	private String score;
	private String signInNumber;
	private String courseClassId;
	private int userSignInStatus; // 0是为签到 1是签到成功
	private String publicCourse;
	private String shareurl; // 课程详情 分享地址

	// 获取所有老师的信息
	public String getTeachersName() {
		if (teachers == null || teachers.size() == 0) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		for (TeacherBean teacherBean : teachers) {
			buffer.append(teacherBean.getNickname() + ",");
		}
		return buffer.substring(0, buffer.length() - 1);
	}

	public String getCourseClassId() {
		if (courseClassId == null) {
			return "";
		} else {
			return courseClassId;
		}
	}

	public void setCourseClassId(String courseClassId) {
		this.courseClassId = courseClassId;
	}

	public String getSignInNumber() {
		if (signInNumber == null) {
			return "";
		} else {
			return signInNumber;
		}
	}

	public void setSignInNumber(String signInNumber) {
		this.signInNumber = signInNumber;
	}

	public String getTitle() {
		if (title == null) {
			return "";
		} else {
			return title;
		}
	}

	public void setTitle(String title) {
		this.title = title;
		this.courseName = title;
	}

	public String getCourseName() {
		if (courseName == null) {
			return "";
		} else {
			return courseName;
		}
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
		this.title = courseName;
	}

	public String getSubtitle() {
		if (subtitle == null) {
			return "";
		} else {
			return subtitle;
		}
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getStatus() {
		if (status == null) {
			return "";
		} else {
			return status;
		}
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		if (type == null) {
			return "";
		} else {
			return type;
		}
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPrice() {
		if (price == null) {
			return "";
		} else {
			return price;
		}
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getExpiryDay() {
		if (expiryDay == null) {
			return "";
		} else {
			return expiryDay;
		}
	}

	public void setExpiryDay(String expiryDay) {
		this.expiryDay = expiryDay;
	}

	public String getLessonNum() {
		if (lessonNum == null) {
			return "";
		} else {
			return lessonNum;
		}
	}

	public void setLessonNum(String lessonNum) {
		this.lessonNum = lessonNum;
	}

	public String getCategoryId() {
		if (categoryId == null) {
			return "";
		} else {
			return categoryId;
		}
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getStudentNum() {
		if (TextUtils.isEmpty(studentNum)) {
			return "0";
		} else {
			return studentNum;
		}
	}

	public void setStudentNum(String studentNum) {
		this.studentNum = studentNum;
	}

	public String getAppleForbid() {
		if (appleForbid == null) {
			return "";
		} else {
			return appleForbid;
		}
	}

	public void setAppleForbid(String appleForbid) {
		this.appleForbid = appleForbid;
	}

	public String getPic() {
		if (pic == null) {
			return "";
		} else {
			return pic;
		}
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public List<TeacherBean> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<TeacherBean> teachers) {
		this.teachers = teachers;
	}

	public String getCourseId() {
		if (courseId == null) {
			return "";
		} else {
			return courseId;
		}
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getScore() {
		if (score == null) {
			return "";
		} else {
			return score;
		}
	}

	public void setScore(String score) {
		this.score = score;
	}

	public int getUserSignInStatus() {
		return userSignInStatus;
	}

	public void setUserSignInStatus(int userSignInStatus) {
		this.userSignInStatus = userSignInStatus;
	}

	public String getPublicCourse() {
		if (publicCourse == null) {
			return "0";
		}
		return publicCourse;
	}

	public void setPublicCourse(String publicCourse) {
		this.publicCourse = publicCourse;
	}

	public String getTimeLength() {
		if (timeLength == null) {
			return "";
		} else {
			return timeLength;
		}
	}

	public void setTimeLength(String timeLength) {
		this.timeLength = timeLength;
	}

	public String getStudyNum() {
		if (studyNum == null) {
			return "";
		} else {
			return studyNum;
		}
	}

	public void setStudyNum(String studyNum) {
		this.studyNum = studyNum;
	}

	public String getShareurl() {
		if (shareurl == null) {
			return "";
		} else {
			return shareurl;
		}
	}

	public void setShareurl(String shareurl) {
		this.shareurl = shareurl;
	}

	public String getClassNum() {
		if (classNum == null) {
			return "";
		}
		return classNum;
	}

	public void setClassNum(String classNum) {
		this.classNum = classNum;
	}

}
