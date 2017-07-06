package com.coder.kzxt.buildwork.entity;

/**
 * Created by pc on 2017/3/20.
 */

public class ClassPublish {
    private String classId;
    private String className;
    private String stuNum;
    private boolean isChecked;
    private boolean hasPublished;

    public boolean isHasPublished() {
        return hasPublished;
    }

    public void setHasPublished(boolean hasPublished) {
        this.hasPublished = hasPublished;
    }

    public String getClassId() {
        return classId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
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

    public String getStuNum() {
        return stuNum;
    }

    public void setStuNum(String stuNum) {
        this.stuNum = stuNum;
    }

    @Override
    public String toString() {
        return "ClassPublish{" +
                "classId='" + classId + '\'' +
                ", className='" + className + '\'' +
                ", stuNum='" + stuNum + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
