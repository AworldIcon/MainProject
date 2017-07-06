package com.coder.kzxt.order.beans;

/**
 * 订单id.
 * Created by wangtingshun on 2017/4/13.
 */

public class Order {

    private String code;
    private String message;

    private Item item;

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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public class Item{
        private String order_id;

        private String id; //支付方式id

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
