package com.coder.kzxt.poster.beans;

import java.io.Serializable;

/**
 * 海报
 * 
 */
public class PostersBean implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3421057992336495563L;
	private String id;
	private String bgColor;
	private String bgColorId;
	private String type;
	private String picture;
	private String desc;
	private String uid;
	private String ctm;
	private String mtm;
	private String pv;
	private String cid;
	private String picHeight;
	private String picWidth;
	private String like;
	private String status;
	private String webCode;
	private String recommend;
	private String owebPriv;
	private String comment;
	private String pType;
	private String userName;
	private String userFace;
	private String createTime;
	private String isGood;
	private String likePoster;

	public String getLikePoster() {
		if (likePoster == null) {
			return "";
		} else {
			return likePoster;
		}
	}

	public void setLikePoster(String likePoster) {
		this.likePoster = likePoster;
	}

	public String getId() {
		if (id == null) {
			return "";
		} else {
			return id;
		}
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBgColor() {
		if (bgColor == null) {
			return "";
		} else {
			return bgColor;
		}
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
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

	public String getPicture() {
		if (picture == null) {
			return "";
		} else {
			return picture;
		}
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getDesc() {
		if (desc == null) {
			return "";
		} else {
			return desc;
		}
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUid() {
		if (uid == null) {
			return "";
		} else {
			return uid;
		}
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCtm() {
		if (ctm == null) {
			return "";
		} else {
			return ctm;
		}
	}

	public void setCtm(String ctm) {
		this.ctm = ctm;
	}

	public String getMtm() {
		if (mtm == null) {
			return "";
		} else {
			return mtm;
		}
	}

	public void setMtm(String mtm) {
		this.mtm = mtm;
	}

	public String getPv() {
		if (pv == null) {
			return "";
		} else {
			return pv;
		}
	}

	public void setPv(String pv) {
		this.pv = pv;
	}

	public String getCid() {
		if (cid == null) {
			return "";
		} else {
			return cid;
		}
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getPicHeight() {
		if (picHeight == null) {
			return "";
		} else {
			return picHeight;
		}
	}

	public void setPicHeight(String picHeight) {
		this.picHeight = picHeight;
	}

	public String getPicWidth() {
		if (picWidth == null) {
			return "";
		} else {
			return picWidth;
		}
	}

	public void setPicWidth(String picWidth) {
		this.picWidth = picWidth;
	}

	public String getLike() {
		if (like == null) {
			return "";
		} else {
			return like;
		}
	}

	public void setLike(String like) {
		this.like = like;
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

	public String getWebCode() {
		if (webCode == null) {
			return "";
		} else {
			return webCode;
		}
	}

	public void setWebCode(String webCode) {
		this.webCode = webCode;
	}

	public String getRecommend() {
		if (recommend == null) {
			return "";
		} else {
			return recommend;
		}
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public String getOwebPriv() {
		if (owebPriv == null) {
			return "";
		} else {
			return owebPriv;
		}
	}

	public void setOwebPriv(String owebPriv) {
		this.owebPriv = owebPriv;
	}

	public String getComment() {
		if (comment == null) {
			return "";
		} else {
			return comment;
		}
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getpType() {
		if (null == pType) {
			return "";
		}
		return pType;
	}

	public void setpType(String pType) {
		this.pType = pType;
	}

	public String getUserName() {
		if (userName == null) {
			return "";
		} else {
			return userName;
		}
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserFace() {
		if (userFace == null) {
			return "";
		} else {
			return userFace;
		}
	}

	public void setUserFace(String userFace) {
		this.userFace = userFace;
	}

	public String getCreateTime() {
		if (createTime == null) {
			return "";
		} else {
			return createTime;
		}
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getIsGood() {
		if (isGood == null) {
			return "";
		} else {
			return isGood;
		}
	}

	public void setIsGood(String isGood) {
		this.isGood = isGood;
	}

	public String getBgColorId() {
		if (bgColorId == null) {
			return "";
		} else {
			return bgColorId;
		}
	}

	public void setBgColorId(String bgColorId) {
		this.bgColorId = bgColorId;
	}
}
