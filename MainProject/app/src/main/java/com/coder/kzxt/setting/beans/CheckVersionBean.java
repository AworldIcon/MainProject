package com.coder.kzxt.setting.beans;

/**
 * Created by Administrator on 2017/3/8.
 */

public class CheckVersionBean {


    /**
     * code : 200
     * message : ok
     * item : {"isForceUpdate":1,"name":"冰河时代","url":"www.sss.com","function":"","version":"111111"}
     */

    private int code;
    private String message;
    private ItemBean item;

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

    public ItemBean getItem() {
        return item;
    }

    public void setItem(ItemBean item) {
        this.item = item;
    }

    public static class ItemBean {
        /**
         * isForceUpdate : 1
         * name : 冰河时代
         * url : www.sss.com
         * function :
         * version : 111111
         */

        private int isForceUpdate;
        private String name;
        private String url;
        private String function;
        private String version;

        public int getIsForceUpdate() {
            return isForceUpdate;
        }

        public void setIsForceUpdate(int isForceUpdate) {
            this.isForceUpdate = isForceUpdate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getFunction() {
            return function;
        }

        public void setFunction(String function) {
            this.function = function;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
