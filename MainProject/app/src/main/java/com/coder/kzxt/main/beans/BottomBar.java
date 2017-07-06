package com.coder.kzxt.main.beans;

import java.util.List;

/**
 * Created by pc on 2017/3/23.
 */

public class BottomBar {

    /**
     * code : 200
     * message : ok
     * items : [{"name_en":"HOMEPAGE","name_cn":"首页"},{"name_en":"CATEGORY","name_cn":"院系"},{"name_en":"DISCOVER","name_cn":"发现"},{"name_en":"ME","name_cn":"我的"}]
     */

    private int code;
    private String message;
    private List<ItemsBean> items;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * name_en : HOMEPAGE
         * name_cn : 首页
         */

        private String name_en;
        private String name_cn;

        public String getName_en() {
            return name_en;
        }

        public void setName_en(String name_en) {
            this.name_en = name_en;
        }

        public String getName_cn() {
            return name_cn;
        }

        public void setName_cn(String name_cn) {
            this.name_cn = name_cn;
        }
    }
}
