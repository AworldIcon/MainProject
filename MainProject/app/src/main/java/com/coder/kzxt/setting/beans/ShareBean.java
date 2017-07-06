package com.coder.kzxt.setting.beans;

/**
 * Created by Administrator on 2017/5/11.
 */

public class ShareBean {


    private String code;
    private String message;

    private ShareItem item;

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

    public ShareItem getItem() {
        return item;
    }

    public void setItem(ShareItem item) {
        this.item = item;
    }

    public class ShareItem {
        private String shareUrl;

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }
    }

}
