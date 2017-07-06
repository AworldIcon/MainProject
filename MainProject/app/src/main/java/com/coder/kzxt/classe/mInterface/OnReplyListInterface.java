package com.coder.kzxt.classe.mInterface;

import com.coder.kzxt.classe.beans.TopicReplyBean;

/**
 * Created by wangtingshun on 2017/6/23.
 * 回复列表
 */
public interface OnReplyListInterface {

    /**
     * 删除回复列表的item
     * @param conmment
     */
    void onDeleteReplyListItem(TopicReplyBean.Conmment conmment);

    /**
     * 回复某人
     * @param sb
     */
    void onReplyToSb(TopicReplyBean.Conmment sb);

}
