package com.coder.kzxt.poster.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tangcy on 2017/5/18.
 */

public class PosterCategory implements Serializable{


    /**
     * code : 200
     * message : ok
     * paginate : {"currentPage":1,"pageSize":20,"pageNum":1,"totalNum":5}
     * items : [{"id":41,"name":"奇闻","status":"开启","type":"自定义","create_time":1493176138,"update_time":1493176138,"delete_time":null,"sort":11,"user_id":907008},{"id":37,"name":"赛事","status":"开启","type":"默认","create_time":1493170542,"update_time":1493170542,"delete_time":null,"sort":0,"user_id":0},{"id":38,"name":"通知","status":"开启","type":"默认","create_time":1493170542,"update_time":1493170542,"delete_time":null,"sort":0,"user_id":0},{"id":39,"name":"买卖","status":"开启","type":"默认","create_time":1493170542,"update_time":1493170542,"delete_time":null,"sort":0,"user_id":0},{"id":40,"name":"启事","status":"开启","type":"默认","create_time":1493170542,"update_time":1493170542,"delete_time":null,"sort":0,"user_id":0}]
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

    public static class ItemsBean implements Serializable{
        /**
         * id : 41
         * name : 奇闻
         * status : 开启
         * type : 自定义
         * create_time : 1493176138
         * update_time : 1493176138
         * delete_time : null
         * sort : 11
         * user_id : 907008
         */

        private int id;
        private String name;
        private int isSelectd;
        private String logo;
//        private String status;
//        private String type;
//        private int create_time;
//        private int update_time;
//        private Object delete_time;
//        private int sort;
//        private int user_id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIsSelectd() {
            return isSelectd;
        }

        public void setIsSelectd(int isSelectd) {
            this.isSelectd = isSelectd;
        }

        public String getLogo() {
            return logo;
        }
        public void setLogo(String logo) {
            this.logo = logo;
        }

        //        public String getStatus() {
//            return status;
//        }
//
//        public void setStatus(String status) {
//            this.status = status;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//
//        public int getCreate_time() {
//            return create_time;
//        }
//
//        public void setCreate_time(int create_time) {
//            this.create_time = create_time;
//        }
//
//        public int getUpdate_time() {
//            return update_time;
//        }
//
//        public void setUpdate_time(int update_time) {
//            this.update_time = update_time;
//        }
//
//        public Object getDelete_time() {
//            return delete_time;
//        }
//
//        public void setDelete_time(Object delete_time) {
//            this.delete_time = delete_time;
//        }
//
//        public int getSort() {
//            return sort;
//        }
//
//        public void setSort(int sort) {
//            this.sort = sort;
//        }
//
//        public int getUser_id() {
//            return user_id;
//        }
//
//        public void setUser_id(int user_id) {
//            this.user_id = user_id;
//        }
    }
}
