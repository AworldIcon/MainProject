package com.coder.kzxt.login.beans;

import java.util.ArrayList;

/**
 * 头像解析
 * Created by Administrator on 2017/4/25.
 */

public class AvatarBean {

    private String code;
    private String message;
    private ArrayList<String> items;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

}
