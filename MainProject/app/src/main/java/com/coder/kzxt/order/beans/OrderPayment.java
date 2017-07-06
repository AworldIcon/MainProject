package com.coder.kzxt.order.beans;

import java.util.ArrayList;

/**
 * 获取支付方式
 * Created by wangtingshun on 2017/4/14.
 */

public class OrderPayment {

    private String code;
    private String message;

    private ArrayList<Item> items;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Item{

        private String appid;
        private String businessLogic;
        private String id;
        private String paymentProvider;
        private String paymentType;
        private String platformType;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getBusinessLogic() {
            return businessLogic;
        }

        public void setBusinessLogic(String businessLogic) {
            this.businessLogic = businessLogic;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPaymentProvider() {
            return paymentProvider;
        }

        public void setPaymentProvider(String paymentProvider) {
            this.paymentProvider = paymentProvider;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getPlatformType() {
            return platformType;
        }

        public void setPlatformType(String platformType) {
            this.platformType = platformType;
        }
    }
}
