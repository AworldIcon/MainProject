package com.coder.kzxt.main.beans;

import com.coder.kzxt.main.adapter.MainCoustomAdapter;
import com.coder.kzxt.main.adapter.MainFreeCourseAdapter;
import com.coder.kzxt.main.adapter.MainMyCourseAdapter;
import com.coder.kzxt.main.adapter.MainNewCourseAdapter;
import com.coder.kzxt.main.adapter.MainNominateAdapter;
import com.coder.kzxt.main.adapter.MainPublicCourseAdapter;
import com.coder.kzxt.main.adapter.MainSchoolCourseAdapter;
import com.coder.kzxt.main.adapter.MainSellerCourseAdapter;
import com.coder.kzxt.main.adapter.MainTeacherCourseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 2017/3/23.
 */

public class MainModelBean {


    public static String data="{\n" +
            "  \"code\": 200,\n" +
            "  \"message\": \"ok\",\n" +
            "  \"items\": [\n" +
            "    {\n" +
            "      \"module_key\": \"NEWS\",\n" +
            "      \"module_style\": \"INFORMATION\",\n" +
            "      \"module_name\": \"最新资讯\",\n" +
            "      \"count\": \"4\",\n" +
            "      \"list\": []\n" +
            "    },\n" +
            "    {\n" +
            "      \"module_key\": \"BANNER\",\n" +
            "      \"module_style\": \"HORIZONTAL_SCROLL_STYLE\",\n" +
            "      \"module_name\": \"首页轮播图\",\n" +
            "      \"count\": \"3\",\n" +
            "      \"list\": [\n" +
            "        {\n" +
            "          \"url\": \"__IMG__/app/BANNER/01.png\",\n" +
            "          \"link\": \"\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"url\": \"__IMG__/app/BANNER/02.png\",\n" +
            "          \"link\": \"\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"url\": \"__IMG__/app/BANNER/03.png\",\n" +
            "          \"link\": \"\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"url\": \"__IMG__/app/BANNER/04.png\",\n" +
            "          \"link\": \"\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"module_key\": \"LIVE\",\n" +
            "      \"module_style\": \"LIVE_ZHENG_FOUR_1\",\n" +
            "      \"module_name\": \"直播\",\n" +
            "      \"count\": \"4\",\n" +
            "      \"list\": []\n" +
            "    },\n" +
            "    {\n" +
            "      \"module_key\": \"SELLER\",\n" +
            "      \"module_style\": \"SELLER_SHUONE_HENGTWO\",\n" +
            "      \"module_name\": \"畅销好课\",\n" +
            "      \"count\": \"4\",\n" +
            "      \"list\": [\n" +
            "        {\n" +
            "          \"id\": 4,\n" +
            "          \"title\": \"网站编辑\",\n" +
            "          \"price\": \"0.00\",\n" +
            "          \"middle_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg\",\n" +
            "          \"source\": \"\",\n" +
            "          \"lesson_num\": 0,\n" +
            "          \"student_num\": 1,\n" +
            "          \"home_pic\": \"/uploads/20170323/46c1737e7891700de1c8a0a1f9662cae.png\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 13732,\n" +
            "          \"title\": \"RHCE在线虚拟实验室教学（Shell编程加强版）\",\n" +
            "          \"price\": \"1200.00\",\n" +
            "          \"middle_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/0252375e4b6d172833.jpg\",\n" +
            "          \"source\": \"\",\n" +
            "          \"lesson_num\": 0,\n" +
            "          \"student_num\": 0,\n" +
            "          \"home_pic\": \"/uploads/20170323/a6c91e58047ab551a0d881e874068da6.png\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 13734,\n" +
            "          \"title\": \"网站PHP开发初级\",\n" +
            "          \"price\": \"350.00\",\n" +
            "          \"middle_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/02523978f9a7318495.jpg\",\n" +
            "          \"source\": \"\",\n" +
            "          \"lesson_num\": 0,\n" +
            "          \"student_num\": 11,\n" +
            "          \"home_pic\": \"/uploads/20170323/895992e516e4e8e81874e0e0f7e6f1e8.png\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 13735,\n" +
            "          \"title\": \"CCNA网络工程师\",\n" +
            "          \"price\": \"610.00\",\n" +
            "          \"middle_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/02524085d41d493135.jpg\",\n" +
            "          \"source\": \"\",\n" +
            "          \"lesson_num\": 0,\n" +
            "          \"student_num\": 0,\n" +
            "          \"home_pic\": \"/uploads/20170323/1760e447caef7e1a7e66cd5be5a2bbd7.png\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"module_key\": \"SCHOOL_COURSE\",\n" +
            "      \"module_style\": \"SCHOOL_ZHENG_FOUR_1\",\n" +
            "      \"module_name\": \"本校课程\",\n" +
            "      \"count\": \"4\",\n" +
            "      \"list\": [\n" +
            "        {\n" +
            "          \"id\": 4,\n" +
            "          \"title\": \"网站编辑\",\n" +
            "          \"price\": \"0.00\",\n" +
            "          \"middle_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg\",\n" +
            "          \"source\": \"\",\n" +
            "          \"lesson_num\": 0,\n" +
            "          \"student_num\": 1,\n" +
            "          \"home_pic\": \"/uploads/20170323/0e62d01ff7b0d44589d53ca751a1fa8b.jpg\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 13732,\n" +
            "          \"title\": \"RHCE在线虚拟实验室教学（Shell编程加强版）\",\n" +
            "          \"price\": \"1200.00\",\n" +
            "          \"middle_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/0252375e4b6d172833.jpg\",\n" +
            "          \"source\": \"\",\n" +
            "          \"lesson_num\": 0,\n" +
            "          \"student_num\": 0,\n" +
            "          \"home_pic\": \"/uploads/20170323/84b3ea052786bd86a8aa27e74417f617.jpg\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 13734,\n" +
            "          \"title\": \"网站PHP开发初级\",\n" +
            "          \"price\": \"350.00\",\n" +
            "          \"middle_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/02523978f9a7318495.jpg\",\n" +
            "          \"source\": \"\",\n" +
            "          \"lesson_num\": 0,\n" +
            "          \"student_num\": 11,\n" +
            "          \"home_pic\": \"/uploads/20170323/2f6e4402a265fc3ca7a914716dc83aa2.png\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 13735,\n" +
            "          \"title\": \"CCNA网络工程师\",\n" +
            "          \"price\": \"610.00\",\n" +
            "          \"middle_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/02524085d41d493135.jpg\",\n" +
            "          \"source\": \"\",\n" +
            "          \"lesson_num\": 0,\n" +
            "          \"student_num\": 0,\n" +
            "          \"home_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/02524085d41d493135.jpg\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"module_key\": \"TOPIC\",\n" +
            "      \"module_style\": \"TOPIC_HENG_FOUR\",\n" +
            "      \"module_name\": \"精品专题\",\n" +
            "      \"count\": \"5\",\n" +
            "      \"list\": []\n" +
            "    },\n" +
            "    {\n" +
            "      \"module_key\": \"CUSTOM_83591\",\n" +
            "      \"module_style\": \"SCHOOL_SHANGTUO_TWO_1\",\n" +
            "      \"module_name\": \"测试自定义模\",\n" +
            "      \"count\": \"4\",\n" +
            "      \"list\": [\n" +
            "        {\n" +
            "          \"id\": 4,\n" +
            "          \"title\": \"网站编辑\",\n" +
            "          \"price\": \"0.00\",\n" +
            "          \"middle_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg\",\n" +
            "          \"source\": \"\",\n" +
            "          \"lesson_num\": 0,\n" +
            "          \"student_num\": 1,\n" +
            "          \"home_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 13732,\n" +
            "          \"title\": \"RHCE在线虚拟实验室教学（Shell编程加强版）\",\n" +
            "          \"price\": \"1200.00\",\n" +
            "          \"middle_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/0252375e4b6d172833.jpg\",\n" +
            "          \"source\": \"\",\n" +
            "          \"lesson_num\": 0,\n" +
            "          \"student_num\": 0,\n" +
            "          \"home_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/0252375e4b6d172833.jpg\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 13734,\n" +
            "          \"title\": \"网站PHP开发初级\",\n" +
            "          \"price\": \"350.00\",\n" +
            "          \"middle_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/02523978f9a7318495.jpg\",\n" +
            "          \"source\": \"\",\n" +
            "          \"lesson_num\": 0,\n" +
            "          \"student_num\": 11,\n" +
            "          \"home_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/02523978f9a7318495.jpg\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 13735,\n" +
            "          \"title\": \"CCNA网络工程师\",\n" +
            "          \"price\": \"610.00\",\n" +
            "          \"middle_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/02524085d41d493135.jpg\",\n" +
            "          \"source\": \"\",\n" +
            "          \"lesson_num\": 0,\n" +
            "          \"student_num\": 0,\n" +
            "          \"home_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/02524085d41d493135.jpg\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"module_key\": \"CENTER_COURSE\",\n" +
            "      \"module_style\": \"PUBLIC_ZHENG_FOUR_1\",\n" +
            "      \"module_name\": \"公开课联盟\",\n" +
            "      \"count\": \"4\",\n" +
            "      \"list\": [\n" +
            "        {\n" +
            "          \"id\": 4,\n" +
            "          \"title\": \"网站编辑\",\n" +
            "          \"price\": \"0.00\",\n" +
            "          \"middle_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg\",\n" +
            "          \"source\": \"\",\n" +
            "          \"lesson_num\": 0,\n" +
            "          \"student_num\": 1,\n" +
            "          \"home_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 13732,\n" +
            "          \"title\": \"RHCE在线虚拟实验室教学（Shell编程加强版）\",\n" +
            "          \"price\": \"1200.00\",\n" +
            "          \"middle_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/0252375e4b6d172833.jpg\",\n" +
            "          \"source\": \"\",\n" +
            "          \"lesson_num\": 0,\n" +
            "          \"student_num\": 0,\n" +
            "          \"home_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/0252375e4b6d172833.jpg\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 13734,\n" +
            "          \"title\": \"网站PHP开发初级\",\n" +
            "          \"price\": \"350.00\",\n" +
            "          \"middle_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/02523978f9a7318495.jpg\",\n" +
            "          \"source\": \"\",\n" +
            "          \"lesson_num\": 0,\n" +
            "          \"student_num\": 11,\n" +
            "          \"home_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/02523978f9a7318495.jpg\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 13735,\n" +
            "          \"title\": \"CCNA网络工程师\",\n" +
            "          \"price\": \"610.00\",\n" +
            "          \"middle_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/02524085d41d493135.jpg\",\n" +
            "          \"source\": \"\",\n" +
            "          \"lesson_num\": 0,\n" +
            "          \"student_num\": 0,\n" +
            "          \"home_pic\": \"http://wyzc.com/Public/files/course/2016/06-18/02524085d41d493135.jpg\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    /**
     * code : 200
     * message : ok
     * items : [{"module_key":"SELLER","module_style":"SELLER_SHUONE_HENGTWO","module_name":"畅销好课","count":"4","list":[{"id":4,"title":"网站编辑","price":"0.00","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg","source":"","lesson_num":0,"student_num":1,"home_pic":"/uploads/20170323/46c1737e7891700de1c8a0a1f9662cae.png"},{"id":13732,"title":"RHCE在线虚拟实验室教学（Shell编程加强版）","price":"1200.00","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/0252375e4b6d172833.jpg","source":"","lesson_num":0,"student_num":0,"home_pic":"/uploads/20170323/a6c91e58047ab551a0d881e874068da6.png"},{"id":13734,"title":"网站PHP开发初级","price":"350.00","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/02523978f9a7318495.jpg","source":"","lesson_num":0,"student_num":11,"home_pic":"/uploads/20170323/895992e516e4e8e81874e0e0f7e6f1e8.png"},{"id":13735,"title":"CCNA网络工程师","price":"610.00","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/02524085d41d493135.jpg","source":"","lesson_num":0,"student_num":0,"home_pic":"/uploads/20170323/1760e447caef7e1a7e66cd5be5a2bbd7.png"}]},{"module_key":"SCHOOL_COURSE","module_style":"SCHOOL_ZHENG_FOUR_1","module_name":"本校课程","count":"4","list":[{"id":4,"title":"网站编辑","price":"0.00","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg","source":"","lesson_num":0,"student_num":1,"home_pic":"/uploads/20170323/0e62d01ff7b0d44589d53ca751a1fa8b.jpg"},{"id":13732,"title":"RHCE在线虚拟实验室教学（Shell编程加强版）","price":"1200.00","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/0252375e4b6d172833.jpg","source":"","lesson_num":0,"student_num":0,"home_pic":"/uploads/20170323/84b3ea052786bd86a8aa27e74417f617.jpg"},{"id":13734,"title":"网站PHP开发初级","price":"350.00","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/02523978f9a7318495.jpg","source":"","lesson_num":0,"student_num":11,"home_pic":"/uploads/20170323/2f6e4402a265fc3ca7a914716dc83aa2.png"},{"id":13735,"title":"CCNA网络工程师","price":"610.00","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/02524085d41d493135.jpg","source":"","lesson_num":0,"student_num":0,"home_pic":"http://wyzc.com/Public/files/course/2016/06-18/02524085d41d493135.jpg"}]},{"module_key":"CUSTOM_83591","module_style":"SCHOOL_SHANGTUO_TWO_1","module_name":"测试自定义模","count":"4","list":[{"id":4,"title":"网站编辑","price":"0.00","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg","source":"","lesson_num":0,"student_num":1,"home_pic":"http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg"},{"id":13732,"title":"RHCE在线虚拟实验室教学（Shell编程加强版）","price":"1200.00","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/0252375e4b6d172833.jpg","source":"","lesson_num":0,"student_num":0,"home_pic":"http://wyzc.com/Public/files/course/2016/06-18/0252375e4b6d172833.jpg"},{"id":13734,"title":"网站PHP开发初级","price":"350.00","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/02523978f9a7318495.jpg","source":"","lesson_num":0,"student_num":11,"home_pic":"http://wyzc.com/Public/files/course/2016/06-18/02523978f9a7318495.jpg"},{"id":13735,"title":"CCNA网络工程师","price":"610.00","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/02524085d41d493135.jpg","source":"","lesson_num":0,"student_num":0,"home_pic":"http://wyzc.com/Public/files/course/2016/06-18/02524085d41d493135.jpg"}]},{"module_key":"CENTER_COURSE","module_style":"PUBLIC_ZHENG_FOUR_1","module_name":"公开课联盟","count":"4","list":[{"id":4,"title":"网站编辑","price":"0.00","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg","source":"","lesson_num":0,"student_num":1,"home_pic":"http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg"},{"id":13732,"title":"RHCE在线虚拟实验室教学（Shell编程加强版）","price":"1200.00","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/0252375e4b6d172833.jpg","source":"","lesson_num":0,"student_num":0,"home_pic":"http://wyzc.com/Public/files/course/2016/06-18/0252375e4b6d172833.jpg"},{"id":13734,"title":"网站PHP开发初级","price":"350.00","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/02523978f9a7318495.jpg","source":"","lesson_num":0,"student_num":11,"home_pic":"http://wyzc.com/Public/files/course/2016/06-18/02523978f9a7318495.jpg"},{"id":13735,"title":"CCNA网络工程师","price":"610.00","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/02524085d41d493135.jpg","source":"","lesson_num":0,"student_num":0,"home_pic":"http://wyzc.com/Public/files/course/2016/06-18/02524085d41d493135.jpg"}]}]
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
        if (items == null)
        {
            return new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * module_key : SELLER
         * module_style : SELLER_SHUONE_HENGTWO
         * module_name : 畅销好课
         * count : 4
         * list : [{"id":4,"title":"网站编辑","price":"0.00","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg","source":"","lesson_num":0,"student_num":1,"home_pic":"/uploads/20170323/46c1737e7891700de1c8a0a1f9662cae.png"},{"id":13732,"title":"RHCE在线虚拟实验室教学（Shell编程加强版）","price":"1200.00","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/0252375e4b6d172833.jpg","source":"","lesson_num":0,"student_num":0,"home_pic":"/uploads/20170323/a6c91e58047ab551a0d881e874068da6.png"},{"id":13734,"title":"网站PHP开发初级","price":"350.00","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/02523978f9a7318495.jpg","source":"","lesson_num":0,"student_num":11,"home_pic":"/uploads/20170323/895992e516e4e8e81874e0e0f7e6f1e8.png"},{"id":13735,"title":"CCNA网络工程师","price":"610.00","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/02524085d41d493135.jpg","source":"","lesson_num":0,"student_num":0,"home_pic":"/uploads/20170323/1760e447caef7e1a7e66cd5be5a2bbd7.png"}]
         */

        private String module_key;
        private String module_style;
        private String module_name;
        private String count;
        private List<ListBean> list;

        public String getModule_key() {
            return module_key;
        }

        public void setModule_key(String module_key) {
            this.module_key = module_key;
        }

        public String getModule_style() {
            return module_style;
        }

        public void setModule_style(String module_style) {
            this.module_style = module_style;
        }

        public String getModule_name() {
            return module_name;
        }

        public void setModule_name(String module_name) {
            this.module_name = module_name;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }
        /**
         * 判断是否需要显示多个元素 来源或者课时
         *
         * @return
         */
        public Boolean getShowTeacherAndNum() {
            return (MainSchoolCourseAdapter.showTeacherAndNumber +  // 本校課程
                    MainNominateAdapter.showTeacherAndNumber +      // 推荐课程
                    MainNewCourseAdapter.showTeacherAndNumber +     // 最新课程
                    MainPublicCourseAdapter.showTeacherAndNumber +   // 公共课程
                    MainSellerCourseAdapter.showTeacherAndNumber +   // 畅销好课
                    MainFreeCourseAdapter.showTeacherAndNumber +    // 免费好课
                    MainMyCourseAdapter.showTeacherAndNumber +      // 我的课程
                    MainTeacherCourseAdapter.showTeacherAndNumber+   // 我的授课
                    MainCoustomAdapter.showTeacherAndNumber   // 自定义
            ).contains(this.getModule_style());
        }

        /**
         * 判断显示 0课时 1来源 2人数 默认是0
         *
         * @return
         */
        public int getModuleType() {
            if (MainPublicCourseAdapter.showTeacherAndNumber.contains(this.module_style)) {
                return 1;
            }
            else if (MainSellerCourseAdapter.showTeacherAndNumber.contains(this.module_style) || MainFreeCourseAdapter.showTeacherAndNumber.contains(this.module_style)) {
                return 2;
            }
            else if (MainTeacherCourseAdapter.showTeacherAndNumber.contains(this.module_style)) {
                return 3;//代表是我教的课，老师身份登录
            } else {
                return 0;
            }
        }
        public static class ListBean {
            /**
             * id : 4
             * title : 网站编辑
             * price : 0.00
             * middle_pic : http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg
             * source :
             * lesson_num : 0
             * student_num : 1
             * home_pic : /uploads/20170323/46c1737e7891700de1c8a0a1f9662cae.png
             */

            private int id;
            private String title;
            private String price;
            private String middle_pic;
            private String source;
            private int lesson_num;
            private int student_num;
            private String home_pic;
            private String bannerImg;
            private String links;
            private String name;
            private String theme;
            private String user_face;
            private String brief;

            private String live_title;
            private String live_status;
            private String speaker;
            private String learn_num;

            private List<String> imgs;
            private String sourceUrl;
            private String time;

            public String getLinks() {
                return links;
            }

            public void setLinks(String links) {
                this.links = links;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getSourceUrl() {
                return sourceUrl;
            }

            public void setSourceUrl(String sourceUrl) {
                this.sourceUrl = sourceUrl;
            }

            public List<String> getImgs() {
                return imgs;
            }

            public void setImgs(List<String> imgs) {
                this.imgs = imgs;
            }

            public String getLive_title() {
                return live_title;
            }

            public void setLive_title(String live_title) {
                this.live_title = live_title;
            }

            public String getLive_status() {
                return live_status;
            }

            public void setLive_status(String live_status) {
                this.live_status = live_status;
            }

            public String getSpeaker() {
                return speaker;
            }

            public void setSpeaker(String speaker) {
                this.speaker = speaker;
            }

            public String getLearn_num() {
                return learn_num;
            }

            public void setLearn_num(String learn_num) {
                this.learn_num = learn_num;
            }

            public String getBrief() {
                return brief;
            }

            public void setBrief(String brief) {
                this.brief = brief;
            }

            public String getUser_face() {
                return user_face;
            }

            public void setUser_face(String user_face) {
                this.user_face = user_face;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTheme() {
                return theme;
            }

            public void setTheme(String theme) {
                this.theme = theme;
            }

            public String getBannerImg() {
                return bannerImg;
            }

            public void setBannerImg(String bannerImg) {
                this.bannerImg = bannerImg;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getMiddle_pic() {
                return middle_pic;
            }

            public void setMiddle_pic(String middle_pic) {
                this.middle_pic = middle_pic;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public int getLesson_num() {
                return lesson_num;
            }

            public void setLesson_num(int lesson_num) {
                this.lesson_num = lesson_num;
            }

            public int getStudent_num() {
                return student_num;
            }

            public void setStudent_num(int student_num) {
                this.student_num = student_num;
            }

            public String getHome_pic() {
                return home_pic;
            }

            public void setHome_pic(String home_pic) {
                this.home_pic = home_pic;
            }
        }
    }

}
