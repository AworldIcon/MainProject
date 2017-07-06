package com.coder.kzxt.utils;

import java.util.regex.Pattern;

/**
 * 校验器
 * Created by wangtingshun on 2017/3/25.
 */

public class ValidatorUtil {

    /**
     * 正则表达式：验证用户名
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";

    /**
     * 正则表达式：验证密码
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9_]{6,16}$";
                                            //^[a-zA-Z0-9]{6,16}$
                                            //
//    /**
//     * 正则表达式：验证手机号
//     */
//    public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 正则表达式：验证汉字
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";

//    /**
//     * 正则表达式：验证身份证
//     */
//    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";

    /**
     * 正则表达式：验证URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    /**
     * 正则表达式：验证IP地址
     */
    public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

    /**
     * 中国电信号码格式验证 手机段： 133,153,180,181,189,177,1700,173
     */
    private static final String CHINA_TELECOM_PATTERN = "(^1(33|53|7[37]|8[019])\\d{8}$)|(^1700\\d{7}$)";

    /**
     * 中国联通号码格式验证 手机段：130,131,132,155,156,185,186,145,176,1707,1708,1709
     */
    private static final String CHINA_UNICOM_PATTERN = "(^1(3[0-2]|4[5]|5[56]|7[6]|8[56])\\d{8}$)|(^170[7-9]\\d{7}$)";

    /**
     * 中国移动号码格式验证
     * 手机段：134,135,136,137,138,139,150,151,152,157,158,159,182,183,184
     * ,187,188,147,178,1705
     *
     */
    private static final String CHINA_MOBILE_PATTERN = "(^1(3[4-9]|4[7]|5[0-27-9]|7[8]|8[2-478])\\d{8}$)|(^1705\\d{7}$)";

    /**
     * 座机电话格式验证
     */
    private static final String PHONE_CALL_PATTERN = "^(\\(\\d{3,4}\\)|\\d{3,4}-)?\\d{7,8}(-\\d{1,4})?$";

    /**
     * 判断11位数字
     */
    private static final String NUMBER_ELEVEN_PATTERN = "^[\\d]{11}$";

    /**
     * 仅手机号格式校验
     */
    private static final String PHONE_PATTERN = new StringBuilder(300).append(CHINA_MOBILE_PATTERN)
            .append("|")
            .append(CHINA_TELECOM_PATTERN)
            .append("|")
            .append(CHINA_UNICOM_PATTERN)
            .toString();

    /**
     * 手机和座机号格式校验
     */
    private static final String PHONE_TEL_PATTERN=new StringBuilder(350).append(PHONE_PATTERN)
            .append("|")
            .append("(")
            .append(PHONE_CALL_PATTERN)
            .append(")")
            .toString();

    /**
     * 匹配函数
     * @param regex
     * @param input
     * @return
     */
    private static boolean match(String regex, String input) {
        return Pattern.matches(regex, input);
    }

    /**
     * 校验用户名
     *
     * @param username
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUsername(String username) {
        return match(REGEX_USERNAME, username);
    }

    public static boolean isEleven(String number){
        return match(NUMBER_ELEVEN_PATTERN,number);
    }

    /**
     * 校验密码
     *
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return match(REGEX_PASSWORD, password);
    }

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return match(PHONE_PATTERN, mobile);
    }

    /**
     * 验证电话号码的格式
     *
     * @author LinBilin
     * @param str
     *            校验电话字符串
     * @return 返回true,否则为false
     */
    public static boolean isPhoneCallNum(String str) {
        return match(PHONE_CALL_PATTERN, str);
    }

    /**
     * 手机号或座机号校验
     * @param input
     * @return
     */
    public static boolean isPhoneOrTel(String input){
        return match(PHONE_TEL_PATTERN, input);
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return match(REGEX_EMAIL, email);
    }

    /**
     * 校验汉字
     *
     * @param chinese
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        return match(REGEX_CHINESE, chinese);
    }

//    /**
//     * 校验身份证
//     *
//     * @param idCard
//     * @return 校验通过返回true，否则返回false
//     */
//    public static boolean isIDCard(String idCard) {
//        return match(REGEX_ID_CARD, idCard);
//    }

    /**
     * 校验URL
     *
     * @param url
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUrl(String url) {
        return match(REGEX_URL, url);
    }

    /**
     * 校验IP地址
     *
     * @param ipAddr
     * @return
     */
    public static boolean isIPAddr(String ipAddr) {
        return match(REGEX_IP_ADDR, ipAddr);
    }

}
