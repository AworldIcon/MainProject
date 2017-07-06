package com.coder.kzxt.classe.beans;

import java.util.ArrayList;

/**
 * 班级详情bean
 * Created by wangtinshun on 2017/3/14.
 */

public class ClassDetailResult {

    private String code;
    private String msg;
    private String xhprof;
    private String runTm;
    private String mem;
    private String server;
    private String serverTime;

    private ClassDetailBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRunTm() {
        return runTm;
    }

    public void setRunTm(String runTm) {
        this.runTm = runTm;
    }

    public String getXhprof() {
        return xhprof;
    }

    public void setXhprof(String xhprof) {
        this.xhprof = xhprof;
    }

    public String getMem() {
        return mem;
    }

    public void setMem(String mem) {
        this.mem = mem;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public ClassDetailBean getData() {
        return data;
    }

    public void setData(ClassDetailBean data) {
        this.data = data;
    }

    public class ClassDetailBean {

       private Creater creater;
       private String classId;
       private String className;
       private String logo;
       private String about;
       private String owner;
       private String categoryName;
       private String announcement;

       private ArrayList<MemberBean> member;

       private String memberNum;
       private String applyNum;
       private String role;

       private AccessBean access;


       public Creater getCreater() {
           return creater;
       }

       public void setCreater(Creater creater) {
           this.creater = creater;
       }

       public String getClassId() {
           return classId;
       }

       public void setClassId(String classId) {
           this.classId = classId;
       }

       public String getClassName() {
           return className;
       }

       public void setClassName(String className) {
           this.className = className;
       }

       public String getLogo() {
           return logo;
       }

       public void setLogo(String logo) {
           this.logo = logo;
       }

       public String getAbout() {
           return about;
       }

       public void setAbout(String about) {
           this.about = about;
       }

       public String getOwner() {
           return owner;
       }

       public void setOwner(String owner) {
           this.owner = owner;
       }

       public String getCategoryName() {
           return categoryName;
       }

       public void setCategoryName(String categoryName) {
           this.categoryName = categoryName;
       }

       public String getAnnouncement() {
           return announcement;
       }

       public void setAnnouncement(String announcement) {
           this.announcement = announcement;
       }

       public ArrayList<MemberBean> getMember() {
           return member;
       }

       public void setMember(ArrayList<MemberBean> member) {
           this.member = member;
       }

       public String getMemberNum() {
           return memberNum;
       }

       public void setMemberNum(String memberNum) {
           this.memberNum = memberNum;
       }

       public String getApplyNum() {
           return applyNum;
       }

       public void setApplyNum(String applyNum) {
           this.applyNum = applyNum;
       }

       public String getRole() {
           return role;
       }

       public void setRole(String role) {
           this.role = role;
       }

       public AccessBean getAccess() {
           return access;
       }

       public void setAccess(AccessBean access) {
           this.access = access;
       }

       public class AccessBean{
           private String joinStatus;
           private String lookThread;
           private String setClass;
           private String applyList;

           public String getJoinStatus() {
               return joinStatus;
           }

           public void setJoinStatus(String joinStatus) {
               this.joinStatus = joinStatus;
           }

           public String getLookThread() {
               return lookThread;
           }

           public void setLookThread(String lookThread) {
               this.lookThread = lookThread;
           }

           public String getSetClass() {
               return setClass;
           }

           public void setSetClass(String setClass) {
               this.setClass = setClass;
           }

           public String getApplyList() {
               return applyList;
           }

           public void setApplyList(String applyList) {
               this.applyList = applyList;
           }
       }

       /**
        * 创建者
        */
       public class Creater{

           private String role;
           private String level;
           private String userFace;
           private String userId;
           private String userName;

           public String getRole() {
               return role;
           }

           public void setRole(String role) {
               this.role = role;
           }

           public String getLevel() {
               return level;
           }

           public void setLevel(String level) {
               this.level = level;
           }

           public String getUserFace() {
               return userFace;
           }

           public void setUserFace(String userFace) {
               this.userFace = userFace;
           }

           public String getUserId() {
               return userId;
           }

           public void setUserId(String userId) {
               this.userId = userId;
           }

           public String getUserName() {
               return userName;
           }

           public void setUserName(String userName) {
               this.userName = userName;
           }
       }

       /**
        * 成员bean
        */
       public class MemberBean{
           private String role;
           private String level;
           private String userFace;
           private String userId;
           private String userName;

           public String getRole() {
               return role;
           }

           public void setRole(String role) {
               this.role = role;
           }

           public String getLevel() {
               return level;
           }

           public void setLevel(String level) {
               this.level = level;
           }

           public String getUserFace() {
               return userFace;
           }

           public void setUserFace(String userFace) {
               this.userFace = userFace;
           }

           public String getUserId() {
               return userId;
           }

           public void setUserId(String userId) {
               this.userId = userId;
           }

           public String getUserName() {
               return userName;
           }

           public void setUserName(String userName) {
               this.userName = userName;
           }
       }

   }
}
