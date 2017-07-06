package com.coder.kzxt.main.beans;

import java.util.ArrayList;

/**
 * 我的bean
 * Created by wangtingshun on 2017/3/30.
 */

public class MyBean {

    private String code;
    private String message;

    private ItemBean item;

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

    public ItemBean getItem() {
        return item;
    }

    public void setItem(ItemBean item) {
        this.item = item;
    }

    public class ItemBean {

        private ArrayList<ChildItemBean> TOPIC;
        private ArrayList<ChildItemBean> FOOTER;

        public ArrayList<ChildItemBean> getTOPIC() {
            return TOPIC;
        }

        public void setTOPIC(ArrayList<ChildItemBean> TOPIC) {
            this.TOPIC = TOPIC;
        }

        public ArrayList<ChildItemBean> getFOOTER() {
            return FOOTER;
        }

        public void setFOOTER(ArrayList<ChildItemBean> FOOTER) {
            this.FOOTER = FOOTER;
        }
    }

    public class ChildItemBean {
        private String name;
        private String title;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "ChildItemBean{" +
                    "name='" + name + '\'' +
                    ", title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }


}
