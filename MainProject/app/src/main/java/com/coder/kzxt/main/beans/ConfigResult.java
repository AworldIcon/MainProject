package com.coder.kzxt.main.beans;

import com.coder.kzxt.main.adapter.MainFreeCourseAdapter;
import com.coder.kzxt.main.adapter.MainMyCourseAdapter;
import com.coder.kzxt.main.adapter.MainNewCourseAdapter;
import com.coder.kzxt.main.adapter.MainNominateAdapter;
import com.coder.kzxt.main.adapter.MainPublicCourseAdapter;
import com.coder.kzxt.main.adapter.MainSchoolCourseAdapter;
import com.coder.kzxt.main.adapter.MainSellerCourseAdapter;
import com.coder.kzxt.main.adapter.MainTeacherCourseAdapter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MaShiZhao on 16/9/19.
 */
public class ConfigResult {

    /**
     * version : 1
     * homePageConfig : {"tabbar":{"nameCn":"首页","nameEn":"Home","imageActive":"","imageDefault":""},"modules":[{"modeName":"广告栏","modeKey":"BANNER","styleName":"水平滚动","styleKey":"SFFSDF","count":"4"},{"modeName":"名师推荐","modeKey":"TEACHER","styleName":"水平滚动","styleKey":"SFFSDF","count":"4"}]}
     */

    private DataBean data;
    /**
     * data : {"version":"1","homePageConfig":{"tabbar":{"nameCn":"首页","nameEn":"Home","imageActive":"","imageDefault":""},"modules":[{"modeName":"广告栏","modeKey":"BANNER","styleName":"水平滚动","styleKey":"SFFSDF","count":"4"},{"modeName":"名师推荐","modeKey":"TEACHER","styleName":"水平滚动","styleKey":"SFFSDF","count":"4"}]}}
     * code : 1000
     * msg : 成功
     * xhprof : no setup
     * runTm : s:1474270160.210 e:1474270160.266 tms=55ms
     * mem : 3.92 MB
     * server : wt1.nazhengshu.com
     * serverTime : 1474270160
     */

    private int code;
    private String msg;
    private String xhprof;
    private String runTm;
    private String mem;
    private String server;
    private String serverTime;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

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

    public static class DataBean implements Serializable {
        private String version;
        /**
         * tabbar : {"nameCn":"首页","nameEn":"Home","imageActive":"","imageDefault":""}
         * modules : [{"modeName":"广告栏","modeKey":"BANNER","styleName":"水平滚动","styleKey":"SFFSDF","count":"4"},{"modeName":"名师推荐","modeKey":"TEACHER","styleName":"水平滚动","styleKey":"SFFSDF","count":"4"}]
         */

        private HomePageConfigBean homePageConfig;
        private HomePageConfigBean categoryPageConfig;
        private HomePageConfigBean discoverPageConfig;

        public HomePageConfigBean getMePageConfig() {
            return mePageConfig;
        }

        public void setMePageConfig(HomePageConfigBean mePageConfig) {
            this.mePageConfig = mePageConfig;
        }

        public HomePageConfigBean getDiscoveryPageConfig() {
            return discoverPageConfig;
        }

        public void setDiscoveryPageConfig(HomePageConfigBean discoveryPageConfig) {
            this.discoverPageConfig = discoveryPageConfig;
        }

        public HomePageConfigBean getCategoryPageConfig() {
            return categoryPageConfig;
        }

        public void setCategoryPageConfig(HomePageConfigBean categoryPageConfig) {
            this.categoryPageConfig = categoryPageConfig;
        }

        private HomePageConfigBean mePageConfig;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public HomePageConfigBean getHomePageConfig() {
            return homePageConfig;
        }

        public void setHomePageConfig(HomePageConfigBean homePageConfig) {
            this.homePageConfig = homePageConfig;
        }

        public static class HomePageConfigBean {
            /**
             * nameCn : 首页
             * nameEn : Home
             * imageActive :
             * imageDefault :
             */

            private TabbarBean tabbar;
            /**
             * modeName : 广告栏
             * modeKey : BANNER
             * styleName : 水平滚动
             * styleKey : SFFSDF
             * count : 4
             */

            private List<ModulesBean> modules;

            public TabbarBean getTabbar() {
                return tabbar;
            }

            public void setTabbar(TabbarBean tabbar) {
                this.tabbar = tabbar;
            }

            public List<ModulesBean> getModules() {
                return modules;
            }

            public void setModules(List<ModulesBean> modules) {
                this.modules = modules;
            }

            public static class TabbarBean {
                private String nameCn;
                private String nameEn;
                private String imageActive;
                private String imageDefault;

                public String getNameCn() {
                    return nameCn;
                }

                public void setNameCn(String nameCn) {
                    this.nameCn = nameCn;
                }

                public String getNameEn() {
                    return nameEn;
                }

                public void setNameEn(String nameEn) {
                    this.nameEn = nameEn;
                }

                public String getImageActive() {
                    return imageActive;
                }

                public void setImageActive(String imageActive) {
                    this.imageActive = imageActive;
                }

                public String getImageDefault() {
                    return imageDefault;
                }

                public void setImageDefault(String imageDefault) {
                    this.imageDefault = imageDefault;
                }
            }

            public static class ModulesBean {
                private String moduleName;
                private String moduleKey;
                private String styleName;
                private String styleKey;
                private String count;

                public void setRequest(Boolean request) {
                    isRequest = request;
                }

                private Boolean isRequest;

                public Boolean getRequest() {
                    if (isRequest == null) return false;
                    return isRequest;
                }

                public String getModuleName() {
                    return moduleName;
                }

                public void setModuleName(String modeName) {
                    this.moduleName = modeName;
                }

                public String getModuleKey() {
                    return moduleKey;
                }

                public void setModuleKey(String modeKey) {
                    this.moduleKey = modeKey;
                }

                public String getStyleName() {
                    return styleName;
                }

                public void setStyleName(String styleName) {
                    this.styleName = styleName;
                }

                public String getStyleKey() {
                    return styleKey;
                }

                public void setStyleKey(String styleKey) {
                    this.styleKey = styleKey;
                }

                public String getCount() {
                    return count;
                }

                public void setCount(String count) {
                    this.count = count;
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
                            MainTeacherCourseAdapter.showTeacherAndNumber   // 我的授课
                    ).contains(this.getStyleKey());
                }

                /**
                 * 判断显示 0课时 1来源 2人数 默认是0
                 *
                 * @return
                 */
                public int getModuleType() {
                    if (MainPublicCourseAdapter.showTeacherAndNumber.contains(this.styleKey)) {
                        return 1;
                    }
                    else if (MainSellerCourseAdapter.showTeacherAndNumber.contains(this.styleKey) || MainFreeCourseAdapter.showTeacherAndNumber.contains(this.styleKey)) {
                        return 2;
                    }
                    else if (MainTeacherCourseAdapter.showTeacherAndNumber.contains(this.styleKey)) {
                        return 3;
                    } else {
                        return 0;
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
