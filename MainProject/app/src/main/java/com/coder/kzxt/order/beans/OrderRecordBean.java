package com.coder.kzxt.order.beans;

import java.io.Serializable;

/**
 * 订单记录
 * Created by wangtingshun on 2017/5/5.
 */

public class OrderRecordBean implements Serializable {

    private String code;
    private String message;

    private RecordBean item;

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

    public RecordBean getItem() {
        return item;
    }

    public void setItem(RecordBean item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "MyOrderBean{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", item=" + item +
                '}';
    }

    public class RecordBean implements Serializable {

        private String id;
        private String update_time;
        private String total_price;
        private String amount;
        private String item_id;
        private String course_class_id;
        private String user_id;
        private String pay_type;
        private String item_type;
        private String is_delete;
        private String create_order_type;
        private String status_name;
        private String create_time;
        private String sn;
        private String status;
        private String create_source;
        private PayMent order_payment;
        private OrderClass course_class;
        private OrderCourse course;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getTotal_price() {
            return total_price;
        }

        public void setTotal_price(String total_price) {
            this.total_price = total_price;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getCourse_class_id() {
            return course_class_id;
        }

        public void setCourse_class_id(String course_class_id) {
            this.course_class_id = course_class_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public String getItem_type() {
            return item_type;
        }

        public void setItem_type(String item_type) {
            this.item_type = item_type;
        }

        public String getIs_delete() {
            return is_delete;
        }

        public void setIs_delete(String is_delete) {
            this.is_delete = is_delete;
        }

        public String getCreate_order_type() {
            return create_order_type;
        }

        public void setCreate_order_type(String create_order_type) {
            this.create_order_type = create_order_type;
        }

        public String getStatus_name() {
            return status_name;
        }

        public void setStatus_name(String status_name) {
            this.status_name = status_name;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreate_source() {
            return create_source;
        }

        public void setCreate_source(String create_source) {
            this.create_source = create_source;
        }

        public PayMent getOrder_payment() {
            return order_payment;
        }

        public void setOrder_payment(PayMent order_payment) {
            this.order_payment = order_payment;
        }

        public OrderClass getCourse_class() {
            return course_class;
        }

        public void setCourse_class(OrderClass course_class) {
            this.course_class = course_class;
        }

        public OrderCourse getCourse() {
            return course;
        }

        public void setCourse(OrderCourse course) {
            this.course = course;
        }

        public class OrderClass implements Serializable{
            private static final long serialVersionUID = -333910903520886189L;
            private String class_name;
            private String id;
            private String status;
            private String price;

            public String getClass_name() {
                return class_name;
            }

            public void setClass_name(String class_name) {
                this.class_name = class_name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            @Override
            public String toString() {
                return "OrderClass{" +
                        "class_name='" + class_name + '\'' +
                        ", id='" + id + '\'' +
                        ", status='" + status + '\'' +
                        ", price='" + price + '\'' +
                        '}';
            }
        }

        public class OrderCourse implements Serializable{
            private static final long serialVersionUID = -7380435897152854909L;
            private String status;
            private String id;
            private String title;
            private String middle_pic;
            private String lesson_num; //课程时长

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getMiddle_pic() {
                return middle_pic;
            }

            public void setMiddle_pic(String middle_pic) {
                this.middle_pic = middle_pic;
            }

            public String getLesson_num() {
                return lesson_num;
            }

            public void setLesson_num(String lesson_num) {
                this.lesson_num = lesson_num;
            }

            @Override
            public String toString() {
                return "OrderCourse{" +
                        "status='" + status + '\'' +
                        ", id='" + id + '\'' +
                        ", title='" + title + '\'' +
                        ", middle_pic='" + middle_pic + '\'' +
                        ", lesson_num='" + lesson_num + '\'' +
                        '}';
            }
        }

        public class PayMent implements Serializable{

            private String order_id;
            private String payment_type;
            private String payment_time;
            private String payment_source;
            private String payment_sn;
            private String payment_name;
            private String pay_type;

            public String getOrder_id() {
                return order_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public String getPayment_type() {
                return payment_type;
            }

            public void setPayment_type(String payment_type) {
                this.payment_type = payment_type;
            }

            public String getPayment_time() {
                return payment_time;
            }

            public void setPayment_time(String payment_time) {
                this.payment_time = payment_time;
            }

            public String getPayment_source() {
                return payment_source;
            }

            public void setPayment_source(String payment_source) {
                this.payment_source = payment_source;
            }

            public String getPayment_sn() {
                return payment_sn;
            }

            public void setPayment_sn(String payment_sn) {
                this.payment_sn = payment_sn;
            }

            public String getPayment_name() {
                return payment_name;
            }

            public void setPayment_name(String payment_name) {
                this.payment_name = payment_name;
            }

            public String getPay_type() {
                return pay_type;
            }

            public void setPay_type(String pay_type) {
                this.pay_type = pay_type;
            }
        }
    }
}
