package com.coder.kzxt.order.beans;

/**
 * 签名bean
 * Created by wangtingshun on 2017/4/14.
 */

public class OrderSignBean
{

    private String code;
    private String message;
    private SignItem item;

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

    public SignItem getItem() {
        return item;
    }

    public void setItem(SignItem item) {
        this.item = item;
    }

    public class SignItem{

        private String returnCode;
        private String transactionId;
        private String paymentType;
        private String returnMsg;
        private String paymentProvider;
        private String prepayId;
        private String codeURL;

        public String getReturnCode() {
            return returnCode;
        }

        public void setReturnCode(String returnCode) {
            this.returnCode = returnCode;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getReturnMsg() {
            return returnMsg;
        }

        public void setReturnMsg(String returnMsg) {
            this.returnMsg = returnMsg;
        }

        public String getPaymentProvider() {
            return paymentProvider;
        }

        public void setPaymentProvider(String paymentProvider) {
            this.paymentProvider = paymentProvider;
        }

        public String getPrepayId() {
            return prepayId;
        }

        public void setPrepayId(String prepayId) {
            this.prepayId = prepayId;
        }

        public String getSign() {
            return codeURL;
        }

        public void setCodeURL(String codeURL) {
            this.codeURL = codeURL;
        }
    }
}
