package com.coder.kzxt.classe.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wangtingshun on 2017/6/19.
 * 组织架构bean
 */

public class OrganizationBean {

    private String code;
    private String message;
    private ArrayList<OneLevelBean> items;

    public ArrayList<OneLevelBean> getItems() {
        return items;
    }

    public void setItems(ArrayList<OneLevelBean> items) {
        this.items = items;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "OrganizationBean{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", items=" + items +
                '}';
    }

    /**
     * 一级
     */
    public class OneLevelBean implements Serializable{

        private static final long serialVersionUID = 485739212298902551L;
        private String id;
        private String name;
        private String pid;
        private String depth;
        private boolean hasChild;   //是否有子数据
        private ArrayList<TwoLevelBean> children;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getDepth() {
            return depth;
        }

        public void setDepth(String depth) {
            this.depth = depth;
        }

        public boolean isHasChild() {
            return hasChild;
        }

        public void setHasChild(boolean hasChild) {
            this.hasChild = hasChild;
        }

        public ArrayList<TwoLevelBean> getChildren() {
            return children;
        }

        public void setChildren(ArrayList<TwoLevelBean> children) {
            this.children = children;
        }

        @Override
        public String toString() {
            return "OneLevelBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", pid='" + pid + '\'' +
                    ", depth='" + depth + '\'' +
                    ", hasChild=" + hasChild +
                    ", children=" + children +
                    '}';
        }
    }

    /**
     * 二级
     */
    public class TwoLevelBean {
        private String id;
        private String name;
        private String pid;
        private String depth;
        private boolean hasChild;   //是否有子数据
        private ArrayList<ThreeLevelBean> children;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getDepth() {
            return depth;
        }

        public void setDepth(String depth) {
            this.depth = depth;
        }

        public boolean isHasChild() {
            return hasChild;
        }

        public void setHasChild(boolean hasChild) {
            this.hasChild = hasChild;
        }

        public ArrayList<ThreeLevelBean> getChildren() {
            return children;
        }

        public void setChildren(ArrayList<ThreeLevelBean> children) {
            this.children = children;
        }

        @Override
        public String toString() {
            return "TwoLevelBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", pid='" + pid + '\'' +
                    ", depth='" + depth + '\'' +
                    ", hasChild=" + hasChild +
                    ", children=" + children +
                    '}';
        }
    }

    /**
     * 三级
     */
    public class ThreeLevelBean{
        private String id;
        private String name;
        private String pid;
        private String depth;
        private boolean hasChild;   //是否有子数据
        private ArrayList<FourLevelBean> children;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getDepth() {
            return depth;
        }

        public void setDepth(String depth) {
            this.depth = depth;
        }

        public boolean isHasChild() {
            return hasChild;
        }

        public void setHasChild(boolean hasChild) {
            this.hasChild = hasChild;
        }

        public ArrayList<FourLevelBean> getChildren() {
            return children;
        }

        public void setChildren(ArrayList<FourLevelBean> children) {
            this.children = children;
        }

        @Override
        public String toString() {
            return "ThreeLevelBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", pid='" + pid + '\'' +
                    ", depth='" + depth + '\'' +
                    ", hasChild=" + hasChild +
                    ", children=" + children +
                    '}';
        }
    }

    /**
     * 四级
     */
    public class FourLevelBean{
        private String id;
        private String name;
        private String pid;
        private String depth;
        private boolean hasChild;   //是否有子数据
        private ArrayList<FiveLevelBean> children;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getDepth() {
            return depth;
        }

        public void setDepth(String depth) {
            this.depth = depth;
        }

        public boolean isHasChild() {
            return hasChild;
        }

        public void setHasChild(boolean hasChild) {
            this.hasChild = hasChild;
        }

        public ArrayList<FiveLevelBean> getChildren() {
            return children;
        }

        public void setChildren(ArrayList<FiveLevelBean> children) {
            this.children = children;
        }

        @Override
        public String toString() {
            return "FourLevelBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", pid='" + pid + '\'' +
                    ", depth='" + depth + '\'' +
                    ", hasChild=" + hasChild +
                    ", children=" + children +
                    '}';
        }
    }

    /**
     * 五级
     */
    public class FiveLevelBean{
        private String id;
        private String name;
        private String pid;
        private String depth;
        private boolean hasChild;   //是否有子数据
        private ArrayList<SixLevelBean> children;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getDepth() {
            return depth;
        }

        public void setDepth(String depth) {
            this.depth = depth;
        }

        public boolean isHasChild() {
            return hasChild;
        }

        public void setHasChild(boolean hasChild) {
            this.hasChild = hasChild;
        }

        public ArrayList<SixLevelBean> getChildren() {
            return children;
        }

        public void setChildren(ArrayList<SixLevelBean> children) {
            this.children = children;
        }

        @Override
        public String toString() {
            return "FiveLevelBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", pid='" + pid + '\'' +
                    ", depth='" + depth + '\'' +
                    ", hasChild=" + hasChild +
                    ", children=" + children +
                    '}';
        }
    }

    /**
     * 六级
     */
    public class SixLevelBean{
        private String id;
        private String name;
        private String pid;
        private String depth;
        private boolean hasChild;   //是否有子数据
        private ArrayList<SevenLevelBean> children;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getDepth() {
            return depth;
        }

        public void setDepth(String depth) {
            this.depth = depth;
        }

        public boolean isHasChild() {
            return hasChild;
        }

        public void setHasChild(boolean hasChild) {
            this.hasChild = hasChild;
        }

        public ArrayList<SevenLevelBean> getChildren() {
            return children;
        }

        public void setChildren(ArrayList<SevenLevelBean> children) {
            this.children = children;
        }

        @Override
        public String toString() {
            return "SixLevelBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", pid='" + pid + '\'' +
                    ", depth='" + depth + '\'' +
                    ", hasChild=" + hasChild +
                    ", children=" + children +
                    '}';
        }
    }

    /**
     * 七级
     */
    public class SevenLevelBean{
        private String id;
        private String name;
        private String pid;
        private String depth;
        private boolean hasChild;   //是否有子数据
        private ArrayList<EightLevelBean> children;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public boolean isHasChild() {
            return hasChild;
        }

        public void setHasChild(boolean hasChild) {
            this.hasChild = hasChild;
        }

        public String getDepth() {
            return depth;
        }

        public void setDepth(String depth) {
            this.depth = depth;
        }

        public ArrayList<EightLevelBean> getChildren() {
            return children;
        }

        public void setChildren(ArrayList<EightLevelBean> children) {
            this.children = children;
        }

        @Override
        public String toString() {
            return "SevenLevelBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", pid='" + pid + '\'' +
                    ", depth='" + depth + '\'' +
                    ", hasChild=" + hasChild +
                    ", children=" + children +
                    '}';
        }
    }


    /**
     * 八级
     */
    public class EightLevelBean{
        private String id;
        private String name;
        private String pid;
        private String depth;
        private boolean hasChild;   //是否有子数据
        private ArrayList<NineLevelBean> children;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getDepth() {
            return depth;
        }

        public void setDepth(String depth) {
            this.depth = depth;
        }

        public boolean isHasChild() {
            return hasChild;
        }

        public void setHasChild(boolean hasChild) {
            this.hasChild = hasChild;
        }

        public ArrayList<NineLevelBean> getChildren() {
            return children;
        }

        public void setChildren(ArrayList<NineLevelBean> children) {
            this.children = children;
        }

        @Override
        public String toString() {
            return "EightLevelBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", pid='" + pid + '\'' +
                    ", depth='" + depth + '\'' +
                    ", hasChild=" + hasChild +
                    ", children=" + children +
                    '}';
        }
    }

    /**
     * 九级
     */
    public class NineLevelBean{
        private String id;
        private String name;
        private String pid;
        private String depth;
        private boolean hasChild;   //是否有子数据
        private ArrayList<TenLevelBean> children;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getDepth() {
            return depth;
        }

        public void setDepth(String depth) {
            this.depth = depth;
        }

        public boolean isHasChild() {
            return hasChild;
        }

        public void setHasChild(boolean hasChild) {
            this.hasChild = hasChild;
        }

        public ArrayList<TenLevelBean> getChildren() {
            return children;
        }

        public void setChildren(ArrayList<TenLevelBean> children) {
            this.children = children;
        }

        @Override
        public String toString() {
            return "NineLevelBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", pid='" + pid + '\'' +
                    ", depth='" + depth + '\'' +
                    ", hasChild=" + hasChild +
                    ", children=" + children +
                    '}';
        }
    }

    public class TenLevelBean{
        private String id;
        private String name;
        private String pid;
        private String depth;
        private boolean hasChild;   //是否有子数据
//        private ArrayList<TenLevelBean> children;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getDepth() {
            return depth;
        }

        public void setDepth(String depth) {
            this.depth = depth;
        }

        public boolean isHasChild() {
            return hasChild;
        }

        public void setHasChild(boolean hasChild) {
            this.hasChild = hasChild;
        }

        @Override
        public String toString() {
            return "TenLevelBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", pid='" + pid + '\'' +
                    ", depth='" + depth + '\'' +
                    ", hasChild=" + hasChild +
                    '}';
        }
    }


}
