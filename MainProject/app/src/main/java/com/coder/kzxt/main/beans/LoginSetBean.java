package com.coder.kzxt.main.beans;

/**
 * Created by tangcy on 2017/7/3.
 */

public class LoginSetBean {


    /**
     * code : 200
     * message : ok
     * item : {"login_page_background":"登录页面背景图","auto_login":"是否开启自动登录:0.关闭 1.开启","any_place_login":"是否开启多地登录:0.关闭 1.开启","have_to_login":"是否开启强制登录:0.关闭 1.开启","qrcode_login":"是否开启二维码登录:0.关闭 1.开启","social_account_login":{"qq":"是否开启qq登录:0.关闭 1.开启","wechat":"是否开启微信登录:0.关闭 1.开启"},"register_type":{"mobile":"是否开启手机注册:0.关闭 1.开启","email":"是否开启邮箱注册:0.关闭 1.开启"},"mobile_qq_setting":{"app_id":"移动端qq登录app_id","app_secret":"移动端qq登录app_secret"},"mobile_wechat_setting":{"app_id":"移动端微信登录app_id","app_secret":"移动端微信登录app_secret"},"bind_account":"使用第三方登陆后是否要绑定已有账号:0.关闭 1.开启","register_protocol":"注册协议","service_protocol":"服务协议"}
     */

    private String code;
    private String message;
    private ItemBean item;

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

    public ItemBean getItem() {
        return item;
    }

    public void setItem(ItemBean item) {
        this.item = item;
    }

    public static class ItemBean {
        /**
         * login_page_background : 登录页面背景图
         * auto_login : 是否开启自动登录:0.关闭 1.开启
         * any_place_login : 是否开启多地登录:0.关闭 1.开启
         * have_to_login : 是否开启强制登录:0.关闭 1.开启
         * qrcode_login : 是否开启二维码登录:0.关闭 1.开启
         * social_account_login : {"qq":"是否开启qq登录:0.关闭 1.开启","wechat":"是否开启微信登录:0.关闭 1.开启"}
         * register_type : {"mobile":"是否开启手机注册:0.关闭 1.开启","email":"是否开启邮箱注册:0.关闭 1.开启"}
         * mobile_qq_setting : {"app_id":"移动端qq登录app_id","app_secret":"移动端qq登录app_secret"}
         * mobile_wechat_setting : {"app_id":"移动端微信登录app_id","app_secret":"移动端微信登录app_secret"}
         * bind_account : 使用第三方登陆后是否要绑定已有账号:0.关闭 1.开启
         * register_protocol : 注册协议
         * service_protocol : 服务协议
         */

//        private String login_page_background;
//        private String auto_login;
//        private String any_place_login;
//        private String have_to_login;
//        private String qrcode_login;
        private SocialAccountLoginBean social_account_login;
//        private RegisterTypeBean register_type;
        private MobileQqSettingBean mobile_qq_setting;
        private MobileWechatSettingBean mobile_wechat_setting;
//        private String bind_account;
//        private String register_protocol;
//        private String service_protocol;

//        public String getLogin_page_background() {
//            return login_page_background;
//        }
//
//        public void setLogin_page_background(String login_page_background) {
//            this.login_page_background = login_page_background;
//        }
//
//        public String getAuto_login() {
//            return auto_login;
//        }
//
//        public void setAuto_login(String auto_login) {
//            this.auto_login = auto_login;
//        }
//
//        public String getAny_place_login() {
//            return any_place_login;
//        }
//
//        public void setAny_place_login(String any_place_login) {
//            this.any_place_login = any_place_login;
//        }
//
//        public String getHave_to_login() {
//            return have_to_login;
//        }
//
//        public void setHave_to_login(String have_to_login) {
//            this.have_to_login = have_to_login;
//        }
//
//        public String getQrcode_login() {
//            return qrcode_login;
//        }
//
//        public void setQrcode_login(String qrcode_login) {
//            this.qrcode_login = qrcode_login;
//        }

        public SocialAccountLoginBean getSocial_account_login() {
            return social_account_login;
        }

        public void setSocial_account_login(SocialAccountLoginBean social_account_login) {
            this.social_account_login = social_account_login;
        }

//        public RegisterTypeBean getRegister_type() {
//            return register_type;
//        }
//
//        public void setRegister_type(RegisterTypeBean register_type) {
//            this.register_type = register_type;
//        }

        public MobileQqSettingBean getMobile_qq_setting() {
            return mobile_qq_setting;
        }

        public void setMobile_qq_setting(MobileQqSettingBean mobile_qq_setting) {
            this.mobile_qq_setting = mobile_qq_setting;
        }

        public MobileWechatSettingBean getMobile_wechat_setting() {
            return mobile_wechat_setting;
        }

        public void setMobile_wechat_setting(MobileWechatSettingBean mobile_wechat_setting) {
            this.mobile_wechat_setting = mobile_wechat_setting;
        }

//        public String getBind_account() {
//            return bind_account;
//        }
//
//        public void setBind_account(String bind_account) {
//            this.bind_account = bind_account;
//        }

//        public String getRegister_protocol() {
//            return register_protocol;
//        }
//
//        public void setRegister_protocol(String register_protocol) {
//            this.register_protocol = register_protocol;
//        }
//
//        public String getService_protocol() {
//            return service_protocol;
//        }
//
//        public void setService_protocol(String service_protocol) {
//            this.service_protocol = service_protocol;
//        }

        public static class SocialAccountLoginBean {
            /**
             * qq : 是否开启qq登录:0.关闭 1.开启
             * wechat : 是否开启微信登录:0.关闭 1.开启
             */

            private String qq;
            private String wechat;

            public String getQq() {
                if(qq==null){
                    qq = "";
                }
                return qq;
            }

            public void setQq(String qq) {
                this.qq = qq;
            }

            public String getWechat() {
                if(wechat==null){
                    wechat = "";
                }
                return wechat;
            }

            public void setWechat(String wechat) {
                this.wechat = wechat;
            }
        }

//        public static class RegisterTypeBean {
//            /**
//             * mobile : 是否开启手机注册:0.关闭 1.开启
//             * email : 是否开启邮箱注册:0.关闭 1.开启
//             */
//
//            private String mobile;
//            private String email;
//
//            public String getMobile() {
//                return mobile;
//            }
//
//            public void setMobile(String mobile) {
//                this.mobile = mobile;
//            }
//
//            public String getEmail() {
//                return email;
//            }
//
//            public void setEmail(String email) {
//                this.email = email;
//            }
//        }

        public static class MobileQqSettingBean {
            /**
             * app_id : 移动端qq登录app_id
             * app_secret : 移动端qq登录app_secret
             */

            private String app_id;
            private String app_secret;

            public String getApp_id() {
                if(app_id==null){
                    app_id="";
                }
                return app_id;
            }

            public void setApp_id(String app_id) {
                this.app_id = app_id;
            }

            public String getApp_secret() {
                if(app_secret==null){
                    app_secret="";
                }
                return app_secret;
            }

            public void setApp_secret(String app_secret) {
                this.app_secret = app_secret;
            }
        }

        public static class MobileWechatSettingBean {
            /**
             * app_id : 移动端微信登录app_id
             * app_secret : 移动端微信登录app_secret
             */

            private String app_id;
            private String app_secret;

            public String getApp_id() {
                if(app_id==null){
                    app_id="";
                }
                return app_id;
            }

            public void setApp_id(String app_id) {
                this.app_id = app_id;
            }

            public String getApp_secret() {
                if(app_secret==null){
                    app_secret="";
                }
                return app_secret;
            }

            public void setApp_secret(String app_secret) {
                this.app_secret = app_secret;
            }
        }
    }
}
