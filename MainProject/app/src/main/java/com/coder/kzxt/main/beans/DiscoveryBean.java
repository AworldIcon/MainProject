package com.coder.kzxt.main.beans;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/2/8.
 */

public class DiscoveryBean implements Serializable {

    /**
     * code : 200
     * message : ok
     * item : {"module":[{"name":"TOPIC","title":"精品专题","image":"http://192.168.3.8/manage/app/DISCOVER/ico12.png"},{"name":"POSTER","title":"海报","image":"http://192.168.3.8/manage/app/DISCOVER/ico5.png"},{"name":"SERVICE","title":"服务","image":"http://192.168.3.8/manage/app/DISCOVER/ico4.png"},{"name":"CENTER_COURSE","title":"公开课联盟","image":"http://192.168.3.8/manage/app/DISCOVER/ico2.png"},{"name":"NEWS","title":"院系","image":"http://192.168.3.8/manage/app/DISCOVER/ico6.png"},{"name":"LIVE","title":"直播课","image":"http://192.168.3.8/manage/app/DISCOVER/ico3.png"},{"name":"CUSTOM_3E10A","title":"fdsafdsa","image":"http://192.168.3.8/manage/app/DISCOVER/ico1.png"},{"name":"CUSTOM_F5940","title":"fdsafd","image":"http://192.168.3.8/manage/app/DISCOVER/ico6.png"},{"name":"CUSTOM_42DD7","title":"霍贞光","image":"http://192.168.3.8/uploads/20170331/fb56857511e022d4c6d89f0cceeb6029.jpg"},{"name":"CUSTOM_51DAB","title":"字母字母aaaaa","image":"http://192.168.3.8/uploads/20170330/4d1d5f492571ba56eef2b27e6bfbcbdc.jpg"},{"name":"CUSTOM_048E4","title":"wode","image":"http://192.168.3.8/manage/app/DISCOVER/ico7.png"}],"style":"1"}
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
         * module : [{"name":"TOPIC","title":"精品专题","image":"http://192.168.3.8/manage/app/DISCOVER/ico12.png"},{"name":"POSTER","title":"海报","image":"http://192.168.3.8/manage/app/DISCOVER/ico5.png"},{"name":"SERVICE","title":"服务","image":"http://192.168.3.8/manage/app/DISCOVER/ico4.png"},{"name":"CENTER_COURSE","title":"公开课联盟","image":"http://192.168.3.8/manage/app/DISCOVER/ico2.png"},{"name":"NEWS","title":"院系","image":"http://192.168.3.8/manage/app/DISCOVER/ico6.png"},{"name":"LIVE","title":"直播课","image":"http://192.168.3.8/manage/app/DISCOVER/ico3.png"},{"name":"CUSTOM_3E10A","title":"fdsafdsa","image":"http://192.168.3.8/manage/app/DISCOVER/ico1.png"},{"name":"CUSTOM_F5940","title":"fdsafd","image":"http://192.168.3.8/manage/app/DISCOVER/ico6.png"},{"name":"CUSTOM_42DD7","title":"霍贞光","image":"http://192.168.3.8/uploads/20170331/fb56857511e022d4c6d89f0cceeb6029.jpg"},{"name":"CUSTOM_51DAB","title":"字母字母aaaaa","image":"http://192.168.3.8/uploads/20170330/4d1d5f492571ba56eef2b27e6bfbcbdc.jpg"},{"name":"CUSTOM_048E4","title":"wode","image":"http://192.168.3.8/manage/app/DISCOVER/ico7.png"}]
         * style : 1
         */

        private String style;
        private List<ModuleBean> module;

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public List<ModuleBean> getModule() {
            return module;
        }

        public void setModule(List<ModuleBean> module) {
            this.module = module;
        }

        public static class ModuleBean {
            /**
             * name : TOPIC
             * title : 精品专题
             * image : http://192.168.3.8/manage/app/DISCOVER/ico12.png
             */

            private String name;
            private String title;
            private String image;
            private String url;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public void setUrl(String url) {
                this.url = url;
            }
            public String getUrl() {
                if(TextUtils.isEmpty(url)){
                    return "";
                }
                return url;
            }
        }
    }
}
