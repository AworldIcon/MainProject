package com.coder.kzxt.utils;

import android.content.Context;

import com.app.utils.BaseSharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * SharedPreferences工具类
 */
public class SharedPreferencesUtil extends BaseSharedPreferencesUtil
{

//    private Context context;
//    private SharedPreferences sp;
//    private SharedPreferences userSp;
//    private SharedPreferences.Editor editor;
//    private SharedPreferences.Editor userEditor;

    public SharedPreferencesUtil(Context context)
    {
        super(context);
//        this.context = context;
//        if (this.context != null)
//        {
//            sp = this.context.getSharedPreferences(Constants.CUURRENT_SP, Context.MODE_PRIVATE);
//            editor = sp.edit();
//            userSp = this.context.getSharedPreferences(Constants.USERS_SP, Context.MODE_PRIVATE);
//            userEditor = userSp.edit();
//        }
    }


    /**
     * 获取用户类型
     *
     * @return
     */
    public String getUserType()
    {
        return userSp.getString(Constants.USER_TYPE, "0");
    }

    /**
     * 保存用户类型
     * 0 学生 1 老师
     *
     * @param userType
     */
    public void setUserType(String userType)
    {
        userEditor.putString(Constants.USER_TYPE, userType);
        userEditor.commit();
    }

    /**
     * 获取用户账号
     */
    public String getUserAccount()
    {
        return sp.getString(Constants.ACCOUNT, "");
    }

    /**
     * 获取用户账号
     *
     * @param account
     */
    public void setUserAccount(String account)
    {
        editor.putString(Constants.ACCOUNT, account);
        editor.commit();
    }


    /**
     * 是否显示qq登录入口
     */
    public String getShowQLogin()
    {
        return sp.getString(Constants.SHOW_Q_lOGIN, "");
    }

    public void setShowQLogin(String id)
    {
        editor.putString(Constants.SHOW_Q_lOGIN, id);
        editor.commit();
    }


    /**
     * 是否显示微信登录入口
     */
    public String getShowWLogin()
    {
        return sp.getString(Constants.SHOW_W_lOGIN, "");
    }

    public void setShowWLogin(String id)
    {
        editor.putString(Constants.SHOW_W_lOGIN, id);
        editor.commit();
    }

    //存放QQ登录的id和sk
    public String getQQID()
    {
        return sp.getString(Constants.QQ_ID, "");
    }

    public void setQQID(String id)
    {
        editor.putString(Constants.QQ_ID, id);
        editor.commit();
    }

    public void setQQSK(String id)
    {
        editor.putString(Constants.QQ_SK, id);
        editor.commit();
    }

    public String getQQSK()
    {
        return sp.getString(Constants.QQ_SK, "");
    }

    //存放微信登录的id和sk
    public void setWXID(String id)
    {
        editor.putString(Constants.WX_ID, id);
        editor.commit();
    }

    public String getWXID()
    {
        return sp.getString(Constants.WX_ID, "");
    }

    public void setWXSK(String id)
    {
        editor.putString(Constants.WX_SK, id);
        editor.commit();
    }

    public String getWXSK()
    {
        return sp.getString(Constants.WX_SK, "");
    }

    /**
     * 注册方式   默认返回3  2种方式都有
     * 0-不能注册  1-只有手机
     * 2-只有邮箱  3-都可以
     *
     * @return
     */
    public String getRegistType()
    {
        return sp.getString(Constants.REGISTER_METHODS, "3");
    }

    /**
     * 设置注册类型
     *
     * @param type
     */
    public void setRegistType(String type)
    {
        editor.putString(Constants.REGISTER_METHODS, type);
        editor.commit();
    }

    /**
     * 登录方式提示语    默认1
     * 可以学号登录    0-没有学号
     *
     * @return
     */
    public String getLoginInfo()
    {
        return sp.getString(Constants.LOGIN_METHODS, "1");
    }

    public void setLoginInfo(String info)
    {
        editor.putString(Constants.LOGIN_METHODS, info);
        editor.commit();
    }


    /**
     * 可以三方登录的方式(是否显示三方登录入口,或者显示哪一个入口,0-全显示  1-微信  2-QQ  3-全不显示)
     * 默认只显示公开课登录入口    QQ微信不显示   默认 3
     *
     * @return
     */
    public String getOauthType()
    {
        return sp.getString(Constants.OAUTH_INFO, "3");
    }

    public void setOauthType(String type)
    {
        editor.putString(Constants.OAUTH_INFO, type);
        editor.commit();
    }

    public String getOpenUid()
    {
        return userSp.getString(Constants.OPENOUID, "");
    }

    public void setOpenUid(String openUid)
    {
        userEditor.putString(Constants.OPENOUID, openUid);
        userEditor.commit();
    }

    public String getIdPhoto()
    {
        return userSp.getString(Constants.IDPHOTO, "");
    }

    public void setIdPhoto(String motto)
    {
        userEditor.putString(Constants.IDPHOTO, motto);
        userEditor.commit();
    }

    public String getStudentNum()
    {
        return userSp.getString(Constants.STUDENTNUM, "");
    }

    public void setStudentNum(String motto)
    {
        userEditor.putString(Constants.STUDENTNUM, motto);
        userEditor.commit();
    }

    /**
     * 获取昵称
     *
     * @return
     */
    public String getNickName()
    {
        return userSp.getString(Constants.NICKNAME, "");
    }

    /**
     * 保存昵称
     *
     * @param nickName
     */
    public void setNickName(String nickName)
    {
        userEditor.putString(Constants.NICKNAME, nickName);
        userEditor.commit();
    }

    /**
     * 获取签名
     *
     * @return
     */
    public String getMotto()
    {
        return userSp.getString(Constants.MOTTO, "");
    }

    /**
     * 保存签名-个人信息
     *
     * @param motto
     */
    public void setMotto(String motto)
    {
        userEditor.putString(Constants.MOTTO, motto);
        userEditor.commit();
    }

    /**
     * 获取用户头像
     *
     * @return
     */
    public String getUserHead()
    {
        return userSp.getString(Constants.USERHEAD, "");
    }

    /**
     * 保存用户头像
     *
     * @param userHead
     */
    public void setUserHead(String userHead)
    {
        userEditor.putString(Constants.USERHEAD, userHead);
        userEditor.commit();
    }

    /**
     * 保存轮播图
     *
     * @param url
     */
    public void setBannerUrl(String url)
    {
        userEditor.putString(Constants.PERBANNER, url);
        userEditor.commit();
    }

    /**
     * 获取轮播图
     *
     * @return
     */
    public String getBannerUrl()
    {
        return userSp.getString(Constants.PERBANNER, "");
    }

    /**
     * 获取用户性别
     *
     * @return
     */
    public String getSex()
    {
        return userSp.getString(Constants.SEX, "male");
    }

    /**
     * 保存用户性别
     *
     * @param sex
     */
    public void setSex(String sex)
    {
        userEditor.putString(Constants.SEX, sex);
        userEditor.commit();
    }

    /**
     * 获取用户余额
     *
     * @return
     */
    public String getBalance()
    {
        return userSp.getString(Constants.BALANCE, "");
    }

    /**
     * 保存用户余额
     *
     * @param balance
     */
    public void setBalance(String balance)
    {
        userEditor.putString(Constants.BALANCE, balance);
        userEditor.commit();
    }

    /**
     * 获取金币
     *
     * @return
     */
    public String getCoin()
    {
        return userSp.getString(Constants.COIN, "");
    }

    /**
     * 保存金币
     *
     * @param coin
     */
    public void setCoin(String coin)
    {
        userEditor.putString(Constants.COIN, coin);
        userEditor.commit();
    }

    /**
     * 获取用户手机号
     *
     * @return
     */
    public String getPhone()
    {
        return userSp.getString(Constants.PHONE, "");
    }

    /**
     * 保存用户手机号
     *
     * @param phone
     */
    public void setPhone(String phone)
    {
        userEditor.putString(Constants.PHONE, phone);
        userEditor.commit();
    }

    /**
     * 获取邮箱账号
     *
     * @return
     */
    public String getEmail()
    {
        return userSp.getString(Constants.EMAIL, "");
    }

    /**
     * 保存用户邮箱账号
     *
     * @param email
     */
    public void setEmail(String email)
    {
        userEditor.putString(Constants.EMAIL, email);
        userEditor.commit();
    }


    // 涉及到用户信息 在下面添加
    // 注 ：退出时会清空数据

    /**
     * 清除用户信息
     */
    public void clearUserInfo()
    {
        userEditor.clear().commit();
    }

    /**
     * 保存QQ名
     *
     * @param name
     */
    public void setQQName(String name)
    {
        userEditor.putString(Constants.QQNAME, name);
        userEditor.commit();
    }

    /**
     * 获取QQ名
     *
     * @return
     */
    public String getQQName()
    {
        return userSp.getString(Constants.QQNAME, "");
    }

    /**
     * 保存微信名
     *
     * @param name
     */
    public void setWXName(String name)
    {
        userEditor.putString(Constants.WXNAME, name);
        userEditor.commit();
    }

    /**
     * 获取微信名
     *
     * @return
     */
    public String getWXName()
    {
        return userSp.getString(Constants.WXNAME, "");
    }

    /**
     * 设置微信用户id
     *
     * @param id
     */
    public void setWXUserId(String id)
    {
        userEditor.putString(Constants.WXUSERID, id);
        userEditor.commit();
    }

    /**
     * 获取微信用户id
     *
     * @return
     */
    public String getWXUserId()
    {
        return userSp.getString(Constants.WXUSERID, "");
    }

    /**
     * 设置QQ用户id
     *
     * @param id
     */
    public void setQQUserId(String id)
    {
        userEditor.putString(Constants.QQUSERID, id);
        userEditor.commit();
    }

    /**
     * 获取QQ用户id
     *
     * @return
     */
    public String getQQUserId()
    {
        return userSp.getString(Constants.QQUSERID, "");
    }

    public String getDownloadFlag()
    {
        return sp.getString(Constants.OPEN_MOVE_NET, "");
    }

    public void setDownloadFlag(String downloadFlag)
    {
        editor.putString(Constants.OPEN_MOVE_NET, downloadFlag);
        editor.commit();
    }

    public boolean getShowLauncher()
    {
        return sp.getBoolean(Constants.SHOW_LAUNCHER, false);
    }

    public void setShowLauncher(Boolean show)
    {
        editor.putBoolean(Constants.SHOW_LAUNCHER, show);
        editor.commit();
    }

    public void setSpreadChannel(String channel)
    {
        editor.putString(Constants.SPREAD_CHANNEL, channel);
        editor.commit();
    }

    public String getSpreadChannel()
    {
        return sp.getString(Constants.SPREAD_CHANNEL, "");
    }


    public String getSpread()
    {
        return sp.getString(Constants.SPREAD_URL, "");
    }

    /**
     * 开屏广告url
     *
     * @param url
     */
    public void setSpread(String url)
    {
        editor.putString(Constants.SPREAD_URL, url);
        editor.commit();
    }


    /**
     * 外置sd卡路径
     */
    public void setSecondSdcard(String secondSd)
    {
        editor.putString(Constants.SECONDSDCARD, secondSd);
        editor.commit();
    }

    public String getSecondSdcard()
    {
        return sp.getString(Constants.SECONDSDCARD, "");
    }

    /**
     * 是否选择外置sd卡
     *
     * @param selectFlag
     */
    public void setSelectAddress(int selectFlag)
    {
        editor.putInt(Constants.SELECTFLAG, selectFlag);
        editor.commit();
    }

    public int getSelectAddress()
    {
        return sp.getInt(Constants.SELECTFLAG, 0);
    }


    /*海报相关*/

    /**
     * 0是单排 1是瀑布流
     */
    public Boolean getPostersType()
    {
        return sp.getBoolean(Constants.POSTERS_TYPE, true);
    }

    public void setPostersType(Boolean flag)
    {
        editor.putBoolean(Constants.POSTERS_TYPE, flag);
        editor.commit();
    }

    /**
     * 搜索海报墙历史记录
     *
     * @return
     */
    public String getSearchHistoryPosters()
    {
        return sp.getString(Constants.SEARCH_HISTORY_POSTERS, "");
    }

    public void setSearchHistoryPosters(String mobile)
    {
        editor.putString(Constants.SEARCH_HISTORY_POSTERS, mobile);
        editor.commit();
    }

    /**
     * 设置搜索历史记录
     *
     * @return
     */
    public String getSearchHistory(String key)
    {
        return sp.getString(key, "");
    }

    public void setSearchHistory(String key, String mobile)
    {
        editor.putString(key, mobile);
        editor.commit();
    }


    public void clearSearchHistory(String key)
    {
        editor.putString(key, "");
        editor.commit();
    }

    // 获取课程消息推送状态
    public String getCourseNotify()
    {
        return userSp.getString(Constants.COURSE_NOTIFY, "1");
    }

    // 设置课程消息推送状态
    public void setCourseNotify(String motto)
    {
        userEditor.putString(Constants.COURSE_NOTIFY, motto);
        userEditor.commit();
    }

    // 获取班级消息推送状态
    public String getClassNotify()
    {
        return userSp.getString(Constants.CLASS_NOTIFY, "1");
    }

    // 设置班级消息推送状态
    public void setClassNotify(String motto)
    {
        userEditor.putString(Constants.CLASS_NOTIFY, motto);
        userEditor.commit();
    }

    // 获取海报消息推送状态
    public String getPosterNotify()
    {
        return userSp.getString(Constants.POSTER_NOTIFY, "0");
    }

    // 设置海报消息推送状态
    public void setPosterNotify(String motto)
    {
        userEditor.putString(Constants.POSTER_NOTIFY, motto);
        userEditor.commit();
    }

    // 获取签到通知推送状态
    public String getSignNotify()
    {
        return userSp.getString(Constants.SIGN_NOTIFY, "1");
    }

    // 设置签到通知推送状态
    public void setSignNotify(String motto)
    {
        userEditor.putString(Constants.SIGN_NOTIFY, motto);
        userEditor.commit();
    }

    // 获取会话通知推送状态
    public String getConversationNotify()
    {
        return userSp.getString(Constants.CONVERSATION_NOTIFY, "1");
    }

    // 设置会话通知推送状态
    public void setConversationNotify(String motto)
    {
        userEditor.putString(Constants.CONVERSATION_NOTIFY, motto);
        userEditor.commit();
    }

    // 获取夜间通知推送状态
    public String getNightNotify()
    {
        return userSp.getString(Constants.NIGHT_NOTIFY, "1");
    }

    // 设置夜间通知推送状态
    public void setNightNotify(String motto)
    {
        userEditor.putString(Constants.NIGHT_NOTIFY, motto);
        userEditor.commit();
    }

    /**
     * 班级设置头像
     *
     * @return
     */
    public String getClassImg()
    {
        return sp.getString(Constants.CLASSSETTINGIMH, "");
    }

    public void setClassImg(String motto)
    {
        editor.putString(Constants.CLASSSETTINGIMH, motto);
        editor.commit();
    }

    /**
     * 班级详情中申请数量
     *
     * @return
     */
    public String getApplyNum()
    {
        return sp.getString(Constants.APPLYNUM, "");
    }

    public void setApplyNum(String motto)
    {
        editor.putString(Constants.APPLYNUM, motto);
        editor.commit();
    }

    /**
     * 班级设置专业名称
     *
     * @return
     */
    public String getProfessionalName()
    {
        return sp.getString(Constants.CLASSSETTINGPROFESSIONAL, "");
    }

    public void setProfessionalName(String motto)
    {
        editor.putString(Constants.CLASSSETTINGPROFESSIONAL, motto);
        editor.commit();
    }

    /**
     * 班级设置班级介绍
     *
     * @return
     */
    public String getAboutText()
    {
        return sp.getString(Constants.CLASSSETTINGABOUTTEXT, "");
    }

    public void setAboutText(String motto)
    {
        editor.putString(Constants.CLASSSETTINGABOUTTEXT, motto);
        editor.commit();
    }

    /**
     * 班级设置黑板报
     *
     * @return
     */
    public String getBlackBoardText()
    {
        return sp.getString(Constants.CLASSBLACKBOARDTEXT, "");
    }

    public void setBlackboardText(String motto)
    {
        editor.putString(Constants.CLASSBLACKBOARDTEXT, motto);
        editor.commit();
    }

    /**
     * 获取信息状态
     *
     * @return
     */
    public boolean getClassState()
    {
        return userSp.getBoolean(Constants.ClASSSTATE, false);
    }

    /**
     * 设置信息状态
     *
     * @param motto
     */
    public void setClassState(boolean motto)
    {
        userEditor.putBoolean(Constants.ClASSSTATE, motto);
        userEditor.commit();
    }


    /**
     * 最后观看视频的group id
     *
     * @return
     */
    public String getGroupId(String treeId)
    {
        return sp.getString(treeId + "last_tid", "");
    }

    public void setGroupId(String treeId, String groupId)
    {
        editor.putString(treeId + "last_tid", groupId);
        editor.commit();
    }

    /**
     * 最后观看视频的child id
     *
     * @return
     */
    public String getChildId(String treeId)
    {
        return sp.getString(treeId + "last_id", "");
    }

    public void setChildId(String treeId, String childId)
    {
        editor.putString(treeId + "last_id", childId);
        editor.commit();
    }

    /**
     * 最后的观看位置
     */
    public void setLastPosition(String treeId, String tid, String id, int lastPos)
    {
        editor.putInt(treeId + tid + id, lastPos);
        editor.commit();
    }

    public int getLastPosition(String treeId, String tid, String id)
    {
        return sp.getInt(treeId + tid + id, 0);
    }

    // 获取加密状态
    public boolean getEncr(String tid, String id)
    {
        return sp.getBoolean(Constants.ENCR + tid + id, false);
    }

    // 设置加密状态
    public void setEncr(String tid, String id, boolean encr)
    {
        editor.putBoolean(Constants.ENCR + tid + id, encr);
        editor.commit();
    }

    public String getBottomBarModelEn()
    {
        return sp.getString(Constants.BOTTOMBAREN, "HOMEPAGE,CATEGORY,DISCOVER,ME");
    }

    public void setBottomBarModelEn(String bottomBarModel)
    {
        editor.putString(Constants.BOTTOMBAREN, bottomBarModel);
        editor.commit();
    }

    public String getBottomBarModelCn()
    {
        return sp.getString(Constants.BOTTOMBARCN, "首页,院系,发现,我的");
    }

    public void setBottomBarModelCn(String bottomBarModel)
    {
        editor.putString(Constants.BOTTOMBARCN, bottomBarModel);
        editor.commit();
    }

    /**
     * 获取手机号
     *
     * @return
     */
    public String getMobile()
    {
        return userSp.getString(Constants.PHONE, "");
    }

    /**
     * 保存手机号
     *
     * @param mobile
     */
    public void setMobile(String mobile)
    {
        userEditor.putString(Constants.PHONE, mobile);
        userEditor.commit();
    }

    /**
     * 获取用户注册方式
     *
     * @return
     */
    public String getRegisterType()
    {
        return userSp.getString(Constants.REGISTER_TYPE, "");
    }

    /**
     * 保存用户注册方式
     *
     * @param type
     */
    public void setRegisterType(String type)
    {
        userEditor.putString(Constants.REGISTER_TYPE, type);
        userEditor.commit();
    }

    /**
     * 获取注册时间
     *
     * @return
     */
    public String getCreateTime()
    {
        return userSp.getString(Constants.CREATE_TIME, "");
    }

    /**
     * 保存注册时间
     *
     * @param time
     */
    public void setCreateTime(String time)
    {
        userEditor.putString(Constants.CREATE_TIME, time);
        userEditor.commit();
    }

    /**
     * 获取用户性别
     *
     * @return
     */
    public String getGender()
    {
        return userSp.getString(Constants.USER_GENDER, "");
    }

    /**
     * 保存用户性别
     *
     * @param gender
     */
    public void setGender(String gender)
    {
        userEditor.putString(Constants.USER_GENDER, gender);
        userEditor.commit();
    }

    /**
     * 获取用户大头像
     *
     * @return
     */
    public String getBigHead()
    {
        return userSp.getString(Constants.USER_BIG_HEAD, "");
    }

    /**
     * 保存用户大头像
     *
     * @param bigHead
     */
    public void setBigHead(String bigHead)
    {
        userEditor.putString(Constants.USER_BIG_HEAD, bigHead);
        userEditor.commit();
    }

    /**
     * 获取用户生日
     *
     * @return
     */
    public String getBirthDay()
    {
        return userSp.getString(Constants.BIRTH_DAY, "");
    }

    /**
     * 保存用户生日
     *
     * @param birthDay
     */
    public void setBirthDay(String birthDay)
    {
        userEditor.putString(Constants.BIRTH_DAY, birthDay);
        userEditor.commit();
    }

    /**
     * 个人简介
     *
     * @return
     */
    public String getDesc()
    {
        return userSp.getString(Constants.USER_DESC, "");
    }

    /**
     * 保存个人简介
     *
     * @param desc
     */
    public void setDesc(String desc)
    {
        userEditor.putString(Constants.USER_DESC, "");
        userEditor.commit();
    }

    /**
     * 获取备注
     *
     * @return
     */
    public String getRemark()
    {
        return userSp.getString(Constants.USER_REMARK, "");
    }

    /**
     * 保存备注
     *
     * @param remark
     */
    public void setRemark(String remark)
    {
        userEditor.putString(Constants.USER_REMARK, remark);
        userEditor.commit();
    }

    /**
     * 保存订单id
     *
     * @param orderId
     */
    public void setOrderId(String orderId)
    {
        editor.putString(Constants.ORDER_ID, orderId);
        editor.commit();
    }

    /**
     * 获取订单id
     *
     * @return
     */
    public String getOrderId()
    {
        return sp.getString(Constants.ORDER_ID, "");
    }


    /**
     * 获取未上报的课时id
     */
    public ArrayList<HashMap<String, String>> getlessinIdList()
    {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        int size = sp.getInt("Status_size", 0);
        for (int i = 0; i < size; i++)
        {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("id", sp.getString("id" + i, null));
//            hashMap.put("classId", sp.getString("classId" + i, null));
            list.add(hashMap);

        }
        return list;
    }

    /**
     * 存储上报失败的课时id
     */
    public void setLessinIdList(ArrayList<HashMap<String, String>> list)
    {
        ArrayList<HashMap<String, String>> alreadyList = getlessinIdList();
        int alreadyCount = alreadyList.size();
        List<String> Ids = new ArrayList<String>(alreadyCount);
        for (int i = 0; i < alreadyCount; i++)
        {
            HashMap<String, String> map = alreadyList.get(i);
            Ids.add(map.get("id"));
        }

        int nowCount = 0;
        for (int i = 0; i < list.size(); i++)
        {
            HashMap<String, String> hashMap = list.get(i);
            String id = hashMap.get("id");
//            String classId = hashMap.get("classId");
            if (!Ids.contains(id))
            {
                editor.putString("id" + (alreadyCount + nowCount), id);
//                editor.putString("classId" + (alreadyCount + nowCount), classId);
                nowCount++;
            }

        }
        editor.putInt("Status_size", nowCount + alreadyCount);
        editor.commit();
    }

    public void clearReceiveList()
    {
        editor.putInt("Status_size", 0);
        editor.commit();
    }


    /**
     * 保存用户角色   "是否老师:0.否 1.是"
     *
     * @param role
     */
    public void setUserRole(String role)
    {
        userEditor.putString(Constants.USER_ROLE, role != null ? role : "2");
        userEditor.commit();
    }

    /**
     * 获取用户角色
     *
     * @return
     */
    public String getUserRole()
    {
        return userSp.getString(Constants.USER_ROLE, "");
    }


    /**
     * 关于我们分享地址
     */
    public String getAboutUsShareUrl()
    {
        return sp.getString(Constants.ABOUT_US_SHARE_URL, "");
    }

    public void setAboutUsShareUrl(String url)
    {
        editor.putString(Constants.ABOUT_US_SHARE_URL, url);
        editor.commit();
    }

    /**
     * 离线签到数据
     *
     * @param signOffline 缓存内容
     */
    public void setSignOffline(String signOffline)
    {
        editor.putString(Constants.SIGN_OFF_LINE, signOffline);
        editor.commit();
    }

    /**
     * 离线签到数据
     */
    public String getSignOffline()
    {
        return sp.getString(Constants.SIGN_OFF_LINE, "");
    }

}
