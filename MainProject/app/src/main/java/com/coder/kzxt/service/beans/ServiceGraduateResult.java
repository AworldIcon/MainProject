package com.coder.kzxt.service.beans;

import com.coder.kzxt.base.beans.BaseBean;

/**
 * Created by MaShiZhao on 2017/6/26
 */

public class ServiceGraduateResult extends BaseBean
{


    /**
     * item : {"id":"ID","create_time":"创建时间","item_id":"学习记录id","type":"1.就业 0.认证","id_card":"身份证","need_paper":"是否需要纸质证书 0=> 不需要 1=>需要","name":"姓名","img_url":"证件照地址"}
     */

    private ItemBean item;

    public ItemBean getItem()
    {
        return item;
    }

    public void setItem(ItemBean item)
    {
        this.item = item;
    }

    public static class ItemBean
    {
        /**
         * id : ID
         * create_time : 创建时间
         * item_id : 学习记录id
         * type : 1.就业 0.认证
         * id_card : 身份证
         * need_paper : 是否需要纸质证书 0=> 不需要 1=>需要
         * name : 姓名
         * img_url : 证件照地址
         */

        private String id;
        private String create_time;
        private String item_id;
        private String type;
        private String id_card;
        private int need_paper;
        private String name;
        private String img_url;

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getCreate_time()
        {
            return create_time;
        }

        public void setCreate_time(String create_time)
        {
            this.create_time = create_time;
        }

        public String getItem_id()
        {
            return item_id;
        }

        public void setItem_id(String item_id)
        {
            this.item_id = item_id;
        }

        public String getType()
        {
            return type;
        }

        public void setType(String type)
        {
            this.type = type;
        }

        public String getId_card()
        {
            return id_card;
        }

        public void setId_card(String id_card)
        {
            this.id_card = id_card;
        }

        public int getNeed_paper()
        {
            return need_paper;
        }

        public void setNeed_paper(int need_paper)
        {
            this.need_paper = need_paper;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getImg_url()
        {
            return img_url;
        }

        public void setImg_url(String img_url)
        {
            this.img_url = img_url;
        }
    }
}
