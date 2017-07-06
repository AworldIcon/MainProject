package com.coder.kzxt.im.conversation;

import android.content.Context;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.im.beans.GroupInfo;
import com.coder.kzxt.im.beans.GroupProfile;
import com.coder.kzxt.im.message.Message;
import com.coder.kzxt.im.message.TextMessage;
import com.coder.kzxt.utils.MyApplication;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMGroupReceiveMessageOpt;

/**
 * 好友或群聊的会话
 */
public class NomalConversation extends Conversation
{


    private TIMConversation conversation;

    //最后一条消息
    private Message lastMessage;
    //我们自己服务器的后台 关闭 成员不能聊天
    private Boolean isClosed;
    //班级名称
    private String className;

    public NomalConversation(TIMConversation conversation)
    {
        this.conversation = conversation;
        type = conversation.getType();
        identify = conversation.getPeer();
        isClosed = false;
    }


    public void setLastMessage(Message lastMessage)
    {
        this.lastMessage = lastMessage;
    }

    public Message getLastMessage()
    {
        return lastMessage;
    }

    public Boolean getClosed()
    {
        if (isClosed == null)
        {
            return false;
        }
        return isClosed;
    }

    public void setClosed(Boolean closed)
    {
        isClosed = closed;
    }

    @Override
    public String getAvatar()
    {
        if (type == TIMConversationType.Group)
        {
            GroupProfile groupProfile = GroupInfo.getInstance().getGroupProfile(GroupInfo.chatRoom, identify);
            return groupProfile == null ? "" : groupProfile.getAvatarUrl();
        } else
        {
            FriendProfile profile = FriendshipInfo.getInstance().getProfile(identify);
            return profile == null ? "" : profile.getAvatarUrl();
        }
    }

    /**
     * 跳转到聊天界面或会话详情
     *
     * @param context 跳转上下文
     */
    @Override
    public void navToDetail(Context context)
    {
//        ChatActivity.navToChat(context,identify,type);
    }

    /**
     * 获取最后一条消息摘要
     */
    @Override
    public String getLastMessageSummary()
    {
        if (conversation.hasDraft())
        {
            TextMessage textMessage = new TextMessage(conversation.getDraft());
            if (lastMessage == null || lastMessage.getMessage().timestamp() < conversation.getDraft().getTimestamp())
            {
                return MyApplication.getInstance().getApplicationContext().getString(R.string.conversation_draft) + textMessage.getSummary();
            } else
            {
                return lastMessage.getSummary();
            }
        } else
        {
            if (lastMessage == null) return "";
            return lastMessage.getSummary();
        }
    }

    /**
     * 获取名称
     */
    @Override
    public String getName()
    {
        if (type == TIMConversationType.Group)
        {
            name = GroupInfo.getInstance().getGroupName(identify);
            if (name.equals("")) name = identify;
        } else
        {
            FriendProfile profile = FriendshipInfo.getInstance().getProfile(identify);
            name = profile == null ? identify : profile.getName();
        }
        return name;
    }


    /**
     * 获取未读消息数量
     */
    @Override
    public long getUnreadNum()
    {
        if (conversation == null) return 0;
        return conversation.getUnreadMessageNum();
    }

    /**
     * 将所有消息标记为已读
     */
    @Override
    public void readAllMessage()
    {
        if (conversation != null)
        {
            conversation.setReadMessage();
        }
    }


    /**
     * 获取最后一条消息的时间
     */
    @Override
    public long getLastMessageTime()
    {
        if (conversation.hasDraft())
        {
            if (lastMessage == null || lastMessage.getMessage().timestamp() < conversation.getDraft().getTimestamp())
            {
                return conversation.getDraft().getTimestamp();
            } else
            {
                return lastMessage.getMessage().timestamp();
            }
        }
        if (lastMessage == null) return 0;
        return lastMessage.getMessage().timestamp();
    }

    /**
     * 获取会话类型
     */
    public TIMConversationType getType()
    {
        return conversation.getType();
    }

    @Override
    public TIMGroupReceiveMessageOpt getGroupReceiveMessageOpt()
    {

        if (conversation.getType() == TIMConversationType.Group)
        {
            return GroupInfo.getInstance().getMessageOpt(identify);
        }

        return TIMGroupReceiveMessageOpt.ReceiveAndNotify;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public String getClassName()
    {
        return className;
    }
}
