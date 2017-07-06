package com.coder.kzxt.service.beans;

import com.coder.kzxt.base.beans.BaseBean;

import java.io.Serializable;

/**
 * Created by MaShiZhao on 2017/6/9
 */

public class ServiceMemberResult extends BaseBean
{

    /**
     * code : 200
     * item : {"id":43,"service_id":57,"service_class_id":66,"user_id":907231,"level_id":0,"join_type":2,"type":0,"graduate":0,"examine":0,"cert_state":0,"exam":0,"entry":0,"entry_url":"","entry_transform_state":0,"entry_transform_url":"","resume":0,"resume_url":"","resume_transform_state":0,"resume_transform_url":"","job_register":0,"job":0,"tech_interview":0,"global_interview":0,"ensure":0,"price":0,"locked":0,"status":0,"finish":0,"create_time":1496992972,"update_time":1496992972,"delete_time":null}
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

    public static class ItemBean implements Serializable
    {
        /**
         * {
         * "id": "ID",
         * "service_id": "服务id",
         * "create_time": "创建时间",
         * "service_class_id": "授课班id",
         * "user_id": "用户id",
         * "level_id": "0全款 1分期",
         * "join_type": "加入方式  ： 1、购买加入  2 、后台导入",
         * "type": "服务类型 1就业服务 0认证服务",
         * "graduate": "结业状态：0未开启 1等待 2通过 3失败 ",
         * "examine": "考核状态：0未开启 1等待 2通过 3失败 ",
         * "cert_state": "证书状态：0未开启 1等待 2通过 3失败 ",
         * "exam": "考试状态：0未开启 1等待 2通过 3失败 ",
         * "entry": "报名表状态：0未开启 1等待 2通过 3失败 ",
         * "entry_url": "报名表地址",
         * "resume_url": "简历地址",
         * "entry_transform_state": "报名表转换状态: 0 none 1 waiting 2 doing 3 success 4error",
         * "entry_transform_url": "报名表转换后的地址 用于预览",
         * "resume": "简历状态：0未开启 1等待 2通过 3失败",
         * "resume_transform_state": "简历转换状态: 0 none 1 waiting 2 doing 3 success 4error",
         * "resume_transform_url": "简历转换后的地址 用于预览",
         * "job_register": "就业登记状态：0未开启 1等待 2通过 3失败",
         * "job": "工作状态：0未开启 1等待 2通过 3失败",
         * "tech_interview": "技术面试状态：0未开启 1等待 2通过 3失败",
         * "global_interview": "综合面试状态：0未开启 1等待 2通过 3失败",
         * "ensure": "购买就业服务时的否保过状态 1保过 0不保",
         * "price": "购买就业服务时的价格",
         * "locked": "0未锁定 1锁定",
         * "update_time": "修改时间"
         * }
         * <p>
         * id : 43
         * service_id : 57
         * service_class_id : 66
         * user_id : 907231
         * level_id : 0
         * join_type : 2
         * type : 0
         * graduate : 0
         * examine : 0
         * cert_state : 0
         * exam : 0
         * entry : 0
         * entry_url :
         * entry_transform_state : 0
         * entry_transform_url :
         * resume : 0
         * resume_url :
         * resume_transform_state : 0
         * resume_transform_url :
         * job_register : 0
         * job : 0
         * tech_interview : 0
         * global_interview : 0
         * ensure : 0
         * price : 0
         * locked : 0
         * status : 0
         * finish : 0
         * create_time : 1496992972
         * update_time : 1496992972
         * delete_time : null
         */

        private int id;
        private int service_id;
        private int service_class_id;
        private int user_id;
        private int level_id;
        private int join_type;
        private int type;
        private int graduate;
        private int examine;
        private int cert_state;
        private int exam;
        private int entry;
        private int entry_transform_state;
        private String entry_transform_url;
        private int resume;
        private String resume_url;
        private int resume_transform_state;
        private String resume_transform_url;
        private int job_register;
        private int job;
        private int tech_interview;
        private int global_interview;
        private int ensure;
        private int price;
        private int locked;
        private int status;
        private int finish;
        private int create_time;
        private int update_time;


        /**
         * 0 报名考试 1 就业服务  2 认证服务
         * <p>
         * "type": "服务类型 1就业服务 0认证服务",
         * 包过  认证服务
         * 不   报名考试 (认证服务)
         * 就业服务
         *
         * @return int
         */
        public int getMyType()
        {
            return type == 1 ? 1 : ensure == 0 ? 0 : 2;
        }

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public int getService_id()
        {
            return service_id;
        }

        public void setService_id(int service_id)
        {
            this.service_id = service_id;
        }

        public int getService_class_id()
        {
            return service_class_id;
        }

        public void setService_class_id(int service_class_id)
        {
            this.service_class_id = service_class_id;
        }

        public int getUser_id()
        {
            return user_id;
        }

        public void setUser_id(int user_id)
        {
            this.user_id = user_id;
        }

        public int getLevel_id()
        {
            return level_id;
        }

        public void setLevel_id(int level_id)
        {
            this.level_id = level_id;
        }

        public int getJoin_type()
        {
            return join_type;
        }

        public void setJoin_type(int join_type)
        {
            this.join_type = join_type;
        }

        public int getType()
        {
            return type;
        }

        public void setType(int type)
        {
            this.type = type;
        }

        public int getGraduate()
        {
            return graduate;
        }

        public void setGraduate(int graduate)
        {
            this.graduate = graduate;
        }

        public int getExamine()
        {
            return examine;
        }

        public void setExamine(int examine)
        {
            this.examine = examine;
        }

        public int getCert_state()
        {
            return cert_state;
        }

        public void setCert_state(int cert_state)
        {
            this.cert_state = cert_state;
        }

        public int getExam()
        {
            return exam;
        }

        public void setExam(int exam)
        {
            this.exam = exam;
        }

        public int getEntry()
        {
            return entry;
        }

        public void setEntry(int entry)
        {
            this.entry = entry;
        }

        public int getEntry_transform_state()
        {
            return entry_transform_state;
        }

        public void setEntry_transform_state(int entry_transform_state)
        {
            this.entry_transform_state = entry_transform_state;
        }

        public String getEntry_transform_url()
        {
            return entry_transform_url;
        }

        public void setEntry_transform_url(String entry_transform_url)
        {
            this.entry_transform_url = entry_transform_url;
        }

        public int getResume()
        {
            return resume;
        }

        public void setResume(int resume)
        {
            this.resume = resume;
        }

        public String getResume_url()
        {
            return resume_url;
        }

        public void setResume_url(String resume_url)
        {
            this.resume_url = resume_url;
        }

        public int getResume_transform_state()
        {
            return resume_transform_state;
        }

        public void setResume_transform_state(int resume_transform_state)
        {
            this.resume_transform_state = resume_transform_state;
        }

        public String getResume_transform_url()
        {
            return resume_transform_url;
        }

        public void setResume_transform_url(String resume_transform_url)
        {
            this.resume_transform_url = resume_transform_url;
        }

        public int getJob_register()
        {
            return job_register;
        }

        public void setJob_register(int job_register)
        {
            this.job_register = job_register;
        }

        public int getJob()
        {
            return job;
        }

        public void setJob(int job)
        {
            this.job = job;
        }

        public int getTech_interview()
        {
            return tech_interview;
        }

        public void setTech_interview(int tech_interview)
        {
            this.tech_interview = tech_interview;
        }

        public int getGlobal_interview()
        {
            return global_interview;
        }

        public void setGlobal_interview(int global_interview)
        {
            this.global_interview = global_interview;
        }

        public int getEnsure()
        {
            return ensure;
        }

        public void setEnsure(int ensure)
        {
            this.ensure = ensure;
        }

        public int getPrice()
        {
            return price;
        }

        public void setPrice(int price)
        {
            this.price = price;
        }

        public int getLocked()
        {
            return locked;
        }

        public void setLocked(int locked)
        {
            this.locked = locked;
        }

        public int getStatus()
        {
            return status;
        }

        public void setStatus(int status)
        {
            this.status = status;
        }

        public int getFinish()
        {
            return finish;
        }

        public void setFinish(int finish)
        {
            this.finish = finish;
        }

        public int getCreate_time()
        {
            return create_time;
        }

        public void setCreate_time(int create_time)
        {
            this.create_time = create_time;
        }

        public int getUpdate_time()
        {
            return update_time;
        }

        public void setUpdate_time(int update_time)
        {
            this.update_time = update_time;
        }

    }
}
