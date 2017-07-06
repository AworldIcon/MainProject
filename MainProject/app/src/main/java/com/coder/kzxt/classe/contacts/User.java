package com.coder.kzxt.classe.contacts;


/**
 * Created by wangtingshun on 2017/6/10.
 */
public class User {

    private String name;
    private String letter;
    private String icon;
    private String classMemberId; //班级成员id
    private String role;  //角色
    private String userId; //用户id

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getClassMemberId() {
        return classMemberId;
    }

    public void setClassMemberId(String classMemberId) {
        this.classMemberId = classMemberId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", letter='" + letter + '\'' +
                ", icon='" + icon + '\'' +
                ", classMemberId='" + classMemberId + '\'' +
                ", role='" + role + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
