package com.coder.kzxt.message.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.im.conversation.NomalConversation;
import com.coder.kzxt.im.util.TimeUtil;
import com.coder.kzxt.message.activity.NotificationListActivity;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.tencent.TIMConversationType;
import com.tencent.TIMGroupReceiveMessageOpt;
import java.util.List;

/**
 * Created by zw on 2017/6/23
 */

public class MessageMainDelegate extends PullRefreshDelegate<NomalConversation>
{
    private Context context;

    public MessageMainDelegate(Context context)
    {
        super(R.layout.header_message_main, R.layout.item_message_main);
        this.context = context;
    }

    @Override
    public void initHeaderView(BaseViewHolder holder)
    {
        LinearLayout notificationLy = holder.findViewById(R.id.notificationLy);
        notificationLy.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                context.startActivity(new Intent(context, NotificationListActivity.class).putExtra("role",new SharedPreferencesUtil(context).getUserRole()));
            }
        });

    }

    @Override
    public void initCustomView(BaseViewHolder holder, List<NomalConversation> data, int position)
    {

        ImageView imageView = holder.findViewById(R.id.imageView);
        TextView courseName = holder.findViewById(R.id.courseName);
        TextView messageContent = holder.findViewById(R.id.messageContent);
        TextView time = holder.findViewById(R.id.time);
        TextView unReadNum = holder.findViewById(R.id.unReadNum);
        TextView className = holder.findViewById(R.id.className);
        ImageView no_disturb = holder.findViewById(R.id.no_disturb);

        final NomalConversation conversation = data.get(position);
        // 用户昵称
        courseName.setText(conversation.getName());
        // 班级名称 内容 头像
        messageContent.setText(conversation.getLastMessageSummary());

        if (conversation.getConversationType() == TIMConversationType.Group)
        {
            className.setText(conversation.getClassName());

//            String identify = conversation.getLastMessage().getSender();
//            FriendProfile profile = FriendshipInfo.getInstance().getProfile(identify);
//            String name = profile == null ? identify : profile.getName();
//            messageContent.setText(name + ":" + conversation.getLastMessageSummary());
            imageView.setBackgroundResource(R.drawable.default_group_round);
        } else
        {
            className.setText("");
            GlideUtils.loadHeaderOfTeacher(context, conversation.getAvatar(), imageView);
        }

        time.setText(TimeUtil.getTimeStr(conversation.getLastMessageTime()));
        long unRead = conversation.getUnreadNum();

        if (unRead <= 0)
        {
            unReadNum.setVisibility(View.GONE);
        } else
        {
            unReadNum.setVisibility(View.VISIBLE);
            String unReadStr = String.valueOf(unRead);
            if (unRead < 10)
            {
                if (conversation.getGroupReceiveMessageOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify)
                {
                    unReadNum.setBackgroundResource(R.drawable.chat_unread_red_point1);
                } else
                {
                    unReadNum.setBackgroundResource(R.drawable.chat_unread_gray_point1);
                }
            } else
            {
                if (conversation.getGroupReceiveMessageOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify)
                {
                    unReadNum.setBackgroundResource(R.drawable.chat_unread_red_point2);
                } else
                {
                    unReadNum.setBackgroundResource(R.drawable.chat_unread_gray_point2);
                }
                if (unRead > 99)
                {
                    unReadStr = context.getResources().getString(R.string.time_more);
                }
            }
            unReadNum.setText(unReadStr);
        }

        if (conversation.getGroupReceiveMessageOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify)
        {
            no_disturb.setVisibility(View.GONE);
        } else
        {
            no_disturb.setVisibility(View.VISIBLE);
        }

    }

}
