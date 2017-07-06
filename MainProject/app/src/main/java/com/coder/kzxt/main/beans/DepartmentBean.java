package com.coder.kzxt.main.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/21.
 */

public class DepartmentBean {

    private int code;
    private String msg;
    private String xhprof;
    private String runTm;
    private String mem;
    private String server;
    private String serverTime;

    private ArrayList<Department> items;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getXhprof() {
        return xhprof;
    }

    public void setXhprof(String xhprof) {
        this.xhprof = xhprof;
    }

    public String getRunTm() {
        return runTm;
    }

    public void setRunTm(String runTm) {
        this.runTm = runTm;
    }

    public String getMem() {
        return mem;
    }

    public void setMem(String mem) {
        this.mem = mem;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public ArrayList<Department> getItems() {
        return items;
    }

    public void setItems(ArrayList<Department> items) {
        this.items = items;
    }

    public static class Department implements Serializable {

        private static final long serialVersionUID = 1L;
        private String id;
        private String code;
        private String name;
        private String iconFromType;
        private String weight;
        private String parentId;
        private String level;
        private String isLeafNode;
        private String createUid;
        private String isDelete;
        private String webCode;
        private String owebPriv;
        private String isRecommend;
        private String isTest;
        private String RecommendName;
        private String initCateId;
        private String isHomeRecommend;
        private String homeRecommendName;
        private String iconUrl;
        private boolean hasChild;   //是否有子数据
        private String parent_id;
        private String is_node;

        private ArrayList<Department.DepartmentChild> child;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIconFromType() {
            return iconFromType;
        }

        public void setIconFromType(String iconFromType) {
            this.iconFromType = iconFromType;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getIsLeafNode() {
            return isLeafNode;
        }

        public void setIsLeafNode(String isLeafNode) {
            this.isLeafNode = isLeafNode;
        }

        public String getCreateUid() {
            return createUid;
        }

        public void setCreateUid(String createUid) {
            this.createUid = createUid;
        }

        public String getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(String isDelete) {
            this.isDelete = isDelete;
        }

        public String getWebCode() {
            return webCode;
        }

        public void setWebCode(String webCode) {
            this.webCode = webCode;
        }

        public String getOwebPriv() {
            return owebPriv;
        }

        public void setOwebPriv(String owebPriv) {
            this.owebPriv = owebPriv;
        }

        public String getIsTest() {
            return isTest;
        }

        public void setIsTest(String isTest) {
            this.isTest = isTest;
        }

        public String getIsRecommend() {
            return isRecommend;
        }

        public void setIsRecommend(String isRecommend) {
            this.isRecommend = isRecommend;
        }

        public String getRecommendName() {
            return RecommendName;
        }

        public void setRecommendName(String recommendName) {
            RecommendName = recommendName;
        }

        public String getInitCateId() {
            return initCateId;
        }

        public void setInitCateId(String initCateId) {
            this.initCateId = initCateId;
        }

        public String getIsHomeRecommend() {
            return isHomeRecommend;
        }

        public void setIsHomeRecommend(String isHomeRecommend) {
            this.isHomeRecommend = isHomeRecommend;
        }

        public String getHomeRecommendName() {
            return homeRecommendName;
        }

        public void setHomeRecommendName(String homeRecommendName) {
            this.homeRecommendName = homeRecommendName;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public String getIs_node() {
            return is_node;
        }

        public void setIs_node(String is_node) {
            this.is_node = is_node;
        }

        public ArrayList<DepartmentChild> getChild() {
            return child;
        }

        public void setChild(ArrayList<DepartmentChild> child) {
            this.child = child;
        }

        public boolean isHasChild() {
            return hasChild;
        }

        public void setHasChild(boolean hasChild) {
            this.hasChild = hasChild;
        }

        @Override
        public String toString() {
            return "Department{" +
                    "id='" + id + '\'' +
                    ", code='" + code + '\'' +
                    ", name='" + name + '\'' +
                    ", iconFromType='" + iconFromType + '\'' +
                    ", weight='" + weight + '\'' +
                    ", parentId='" + parentId + '\'' +
                    ", level='" + level + '\'' +
                    ", isLeafNode='" + isLeafNode + '\'' +
                    ", createUid='" + createUid + '\'' +
                    ", isDelete='" + isDelete + '\'' +
                    ", webCode='" + webCode + '\'' +
                    ", owebPriv='" + owebPriv + '\'' +
                    ", isRecommend='" + isRecommend + '\'' +
                    ", isTest='" + isTest + '\'' +
                    ", RecommendName='" + RecommendName + '\'' +
                    ", initCateId='" + initCateId + '\'' +
                    ", isHomeRecommend='" + isHomeRecommend + '\'' +
                    ", homeRecommendName='" + homeRecommendName + '\'' +
                    ", iconUrl='" + iconUrl + '\'' +
                    ", hasChild=" + hasChild +
                    ", parent_id='" + parent_id + '\'' +
                    ", is_node='" + is_node + '\'' +
                    ", child=" + child +
                    '}';
        }

        public static class DepartmentChild {
            private String id;
            private String code;
            private String name;
            private String iconFromType;
            private String weight;
            private String parentId;
            private String level;
            private String isLeafNode;
            private String createUid;
            private String isDelete;
            private String webCode;
            private String owebPriv;
            private String isRecommend;
            private String isTest;
            private String RecommendName;
            private String initCateId;
            private String isHomeRecommend;
            private String homeRecommendName;
            private String iconUrl;
            private boolean hasChild;   //是否有子数据
            private String parent_id;
            private String is_node;
            private ArrayList<DepartmentChild.ThreeLevelChild> child;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

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

            public String getIconFromType() {
                return iconFromType;
            }

            public void setIconFromType(String iconFromType) {
                this.iconFromType = iconFromType;
            }

            public String getWeight() {
                return weight;
            }

            public void setWeight(String weight) {
                this.weight = weight;
            }

            public String getParentId() {
                return parentId;
            }

            public void setParentId(String parentId) {
                this.parentId = parentId;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getIsLeafNode() {
                return isLeafNode;
            }

            public void setIsLeafNode(String isLeafNode) {
                this.isLeafNode = isLeafNode;
            }

            public String getCreateUid() {
                return createUid;
            }

            public void setCreateUid(String createUid) {
                this.createUid = createUid;
            }

            public String getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(String isDelete) {
                this.isDelete = isDelete;
            }

            public String getWebCode() {
                return webCode;
            }

            public void setWebCode(String webCode) {
                this.webCode = webCode;
            }

            public String getOwebPriv() {
                return owebPriv;
            }

            public void setOwebPriv(String owebPriv) {
                this.owebPriv = owebPriv;
            }

            public String getIsRecommend() {
                return isRecommend;
            }

            public void setIsRecommend(String isRecommend) {
                this.isRecommend = isRecommend;
            }

            public String getIsTest() {
                return isTest;
            }

            public void setIsTest(String isTest) {
                this.isTest = isTest;
            }

            public String getRecommendName() {
                return RecommendName;
            }

            public void setRecommendName(String recommendName) {
                RecommendName = recommendName;
            }

            public String getInitCateId() {
                return initCateId;
            }

            public void setInitCateId(String initCateId) {
                this.initCateId = initCateId;
            }

            public String getIsHomeRecommend() {
                return isHomeRecommend;
            }

            public void setIsHomeRecommend(String isHomeRecommend) {
                this.isHomeRecommend = isHomeRecommend;
            }

            public String getHomeRecommendName() {
                return homeRecommendName;
            }

            public void setHomeRecommendName(String homeRecommendName) {
                this.homeRecommendName = homeRecommendName;
            }

            public String getIconUrl() {
                return iconUrl;
            }

            public void setIconUrl(String iconUrl) {
                this.iconUrl = iconUrl;
            }

            public String getParent_id() {
                return parent_id;
            }

            public void setParent_id(String parent_id) {
                this.parent_id = parent_id;
            }

            public String getIs_node() {
                return is_node;
            }

            public void setIs_node(String is_node) {
                this.is_node = is_node;
            }

            public ArrayList<ThreeLevelChild> getChild() {
                return child;
            }

            public void setChild(ArrayList<ThreeLevelChild> child) {
                this.child = child;
            }

            public boolean isHasChild() {
                return hasChild;
            }

            public void setHasChild(boolean hasChild) {
                this.hasChild = hasChild;
            }

            @Override
            public String toString() {
                return "DepartmentChild{" +
                        "id='" + id + '\'' +
                        ", code='" + code + '\'' +
                        ", name='" + name + '\'' +
                        ", iconFromType='" + iconFromType + '\'' +
                        ", weight='" + weight + '\'' +
                        ", parentId='" + parentId + '\'' +
                        ", level='" + level + '\'' +
                        ", isLeafNode='" + isLeafNode + '\'' +
                        ", createUid='" + createUid + '\'' +
                        ", isDelete='" + isDelete + '\'' +
                        ", webCode='" + webCode + '\'' +
                        ", owebPriv='" + owebPriv + '\'' +
                        ", isRecommend='" + isRecommend + '\'' +
                        ", isTest='" + isTest + '\'' +
                        ", RecommendName='" + RecommendName + '\'' +
                        ", initCateId='" + initCateId + '\'' +
                        ", isHomeRecommend='" + isHomeRecommend + '\'' +
                        ", homeRecommendName='" + homeRecommendName + '\'' +
                        ", iconUrl='" + iconUrl + '\'' +
                        ", hasChild=" + hasChild +
                        ", parent_id='" + parent_id + '\'' +
                        ", is_node='" + is_node + '\'' +
                        ", child=" + child +
                        '}';
            }

            public static class ThreeLevelChild {
                private String id;
                private String code;
                private String name;
                private String iconFromType;
                private String weight;
                private String parentId;
                private String level;
                private String isLeafNode;
                private String createUid;
                private String isDelete;
                private String webCode;
                private String owebPriv;
                private String isRecommend;
                private String isTest;
                private String RecommendName;
                private String initCateId;
                private String isHomeRecommend;
                private String homeRecommendName;
                private String iconUrl;
                private boolean hasChild;   //是否有子数据
                private String parent_id;
                private String is_node;

                private ArrayList<ThreeLevelChild> child;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getIconFromType() {
                    return iconFromType;
                }

                public void setIconFromType(String iconFromType) {
                    this.iconFromType = iconFromType;
                }

                public String getWeight() {
                    return weight;
                }

                public void setWeight(String weight) {
                    this.weight = weight;
                }

                public String getParentId() {
                    return parentId;
                }

                public void setParentId(String parentId) {
                    this.parentId = parentId;
                }

                public String getLevel() {
                    return level;
                }

                public void setLevel(String level) {
                    this.level = level;
                }

                public String getIsLeafNode() {
                    return isLeafNode;
                }

                public void setIsLeafNode(String isLeafNode) {
                    this.isLeafNode = isLeafNode;
                }

                public String getCreateUid() {
                    return createUid;
                }

                public void setCreateUid(String createUid) {
                    this.createUid = createUid;
                }

                public String getIsDelete() {
                    return isDelete;
                }

                public void setIsDelete(String isDelete) {
                    this.isDelete = isDelete;
                }

                public String getWebCode() {
                    return webCode;
                }

                public void setWebCode(String webCode) {
                    this.webCode = webCode;
                }

                public String getOwebPriv() {
                    return owebPriv;
                }

                public void setOwebPriv(String owebPriv) {
                    this.owebPriv = owebPriv;
                }

                public String getIsRecommend() {
                    return isRecommend;
                }

                public void setIsRecommend(String isRecommend) {
                    this.isRecommend = isRecommend;
                }

                public String getIsTest() {
                    return isTest;
                }

                public void setIsTest(String isTest) {
                    this.isTest = isTest;
                }

                public String getRecommendName() {
                    return RecommendName;
                }

                public void setRecommendName(String recommendName) {
                    RecommendName = recommendName;
                }

                public String getInitCateId() {
                    return initCateId;
                }

                public void setInitCateId(String initCateId) {
                    this.initCateId = initCateId;
                }

                public String getIsHomeRecommend() {
                    return isHomeRecommend;
                }

                public void setIsHomeRecommend(String isHomeRecommend) {
                    this.isHomeRecommend = isHomeRecommend;
                }

                public String getHomeRecommendName() {
                    return homeRecommendName;
                }

                public void setHomeRecommendName(String homeRecommendName) {
                    this.homeRecommendName = homeRecommendName;
                }

                public String getIconUrl() {
                    return iconUrl;
                }

                public void setIconUrl(String iconUrl) {
                    this.iconUrl = iconUrl;
                }

                public String getParent_id() {
                    return parent_id;
                }

                public void setParent_id(String parent_id) {
                    this.parent_id = parent_id;
                }

                public String getIs_node() {
                    return is_node;
                }

                public void setIs_node(String is_node) {
                    this.is_node = is_node;
                }

                public ArrayList<ThreeLevelChild> getChild() {
                    return child;
                }

                public void setChild(ArrayList<ThreeLevelChild> child) {
                    this.child = child;
                }

                public boolean isHasChild() {
                    return hasChild;
                }

                public void setHasChild(boolean hasChild) {
                    this.hasChild = hasChild;
                }

                @Override
                public String toString() {
                    return "ThreeLevelChild{" +
                            "id='" + id + '\'' +
                            ", code='" + code + '\'' +
                            ", name='" + name + '\'' +
                            ", iconFromType='" + iconFromType + '\'' +
                            ", weight='" + weight + '\'' +
                            ", parentId='" + parentId + '\'' +
                            ", level='" + level + '\'' +
                            ", isLeafNode='" + isLeafNode + '\'' +
                            ", createUid='" + createUid + '\'' +
                            ", isDelete='" + isDelete + '\'' +
                            ", webCode='" + webCode + '\'' +
                            ", owebPriv='" + owebPriv + '\'' +
                            ", isRecommend='" + isRecommend + '\'' +
                            ", isTest='" + isTest + '\'' +
                            ", RecommendName='" + RecommendName + '\'' +
                            ", initCateId='" + initCateId + '\'' +
                            ", isHomeRecommend='" + isHomeRecommend + '\'' +
                            ", homeRecommendName='" + homeRecommendName + '\'' +
                            ", iconUrl='" + iconUrl + '\'' +
                            ", hasChild=" + hasChild +
                            ", parent_id='" + parent_id + '\'' +
                            ", is_node='" + is_node + '\'' +
                            ", child=" + child +
                            '}';
                }
            }

        }

    }

}
