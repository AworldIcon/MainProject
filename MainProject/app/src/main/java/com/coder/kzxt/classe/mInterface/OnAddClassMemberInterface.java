package com.coder.kzxt.classe.mInterface;

import com.coder.kzxt.login.beans.UserInfoList;

/**
 * Created by wangtingshun on 2017/6/19.
 * 添加班级成员的接口
 */

public interface OnAddClassMemberInterface {

    /**
     * 添加班级成员
     * @param userBean
     */
    void onAddClassMember(UserInfoList.UserBean userBean);

}
