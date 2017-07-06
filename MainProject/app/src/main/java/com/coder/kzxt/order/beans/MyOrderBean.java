package com.coder.kzxt.order.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 我的订单bean
 * Created by wangtingshun on 2017/4/13.
 */

public class MyOrderBean {

    private String code;
    private String message;
    private ArrayList<OrderBean> items;
    private Paginate paginate;

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

    public Paginate getPaginate() {
        return paginate;
    }

    public void setPaginate(Paginate paginate) {
        this.paginate = paginate;
    }

    public ArrayList<OrderBean> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrderBean> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "MyOrderBean{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", items=" + items +
                ", paginate=" + paginate +
                '}';
    }

    public class OrderBean implements Serializable {

        private static final long serialVersionUID = -6887892024379052007L;
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

        @Override
        public String toString() {
            return "OrderBean{" +
                    "id='" + id + '\'' +
                    ", update_time='" + update_time + '\'' +
                    ", total_price='" + total_price + '\'' +
                    ", amount='" + amount + '\'' +
                    ", item_id='" + item_id + '\'' +
                    ", course_class_id='" + course_class_id + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", pay_type='" + pay_type + '\'' +
                    ", item_type='" + item_type + '\'' +
                    ", is_delete='" + is_delete + '\'' +
                    ", create_order_type='" + create_order_type + '\'' +
                    ", status_name='" + status_name + '\'' +
                    ", create_time='" + create_time + '\'' +
                    ", sn='" + sn + '\'' +
                    ", status='" + status + '\'' +
                    ", create_source='" + create_source + '\'' +
                    ", course_class=" + course_class +
                    ", course=" + course +
                    '}';
        }
    }


    public class Paginate {
        private int currentPage;
        private int pageSize;
        private int pageNum;
        private int totalNum;

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }
    }

}
