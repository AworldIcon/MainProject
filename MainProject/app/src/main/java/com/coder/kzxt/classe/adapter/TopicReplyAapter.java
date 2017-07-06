package com.coder.kzxt.classe.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.beans.TopicReplyBean;
import com.coder.kzxt.classe.mInterface.OnReplyListInterface;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.Utils;

import java.util.ArrayList;

/**
 * Created by wangtingshun on 2017/6/16.
 * 话题回复 的adapter
 */
public class TopicReplyAapter extends BaseAdapter {

    private Context mContext;
    private boolean expandAll; //展开全部
    private String blank = " ";
    private int modeWidth; //宽度
    private ArrayList<TopicReplyBean.Conmment> replyBeans;
    private String myId;
    private String selfRole;

    public TopicReplyAapter(Context context, boolean isExpend, ArrayList<TopicReplyBean.Conmment> data, String myId, String selfRole) {
        this.mContext = context;
        this.expandAll = isExpend;
        this.replyBeans = data;
        this.myId = myId;
        this.selfRole = selfRole;
    }

    public void setTopicReplyData(boolean isExpend, ArrayList<TopicReplyBean.Conmment> data) {
        this.replyBeans = data;
        this.expandAll = isExpend;
    }

    @Override
    public int getCount() {
        if (expandAll) {
            return replyBeans.size();
        } else {
            if (replyBeans.size() > 2) {
                return 2;
            }
        }
        return replyBeans.size();
    }

    @Override
    public Object getItem(int item) {
        return replyBeans.get(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.topic_reply_child_item, viewGroup, false);
            holder.replyUser = (TextView) convertView.findViewById(R.id.tv_reply_user); //回复人
            holder.reply = (TextView) convertView.findViewById(R.id.tv_reply);
            holder.byReply = (TextView) convertView.findViewById(R.id.tv_by_reply);  //被回复人
            holder.replyContent = (TextView) convertView.findViewById(R.id.reply_content);
            holder.date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.delete = (TextView) convertView.findViewById(R.id.tv_delete);
            holder.replyItem = (RelativeLayout) convertView.findViewById(R.id.reply_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final TopicReplyBean.Conmment conmment = replyBeans.get(position);
        TopicReplyBean.ReplyProfile userProfile = conmment.getUser_profile(); //回复人
        TopicReplyBean.ByProfile toUserProfile = conmment.getTo_user_profile(); //被回复人
        ArrayList<TopicReplyBean.ReplyContent> content = conmment.getContent();  //回复内容

        String userName = userProfile.getNickname();
        String toUserName = toUserProfile.getNickname();

        if (!TextUtils.isEmpty(toUserName) && !TextUtils.isEmpty(userName)) {
            holder.reply.setVisibility(View.VISIBLE);
            holder.replyUser.setText(userName);
            holder.byReply.setText(toUserName + ":");
        } else if(TextUtils.isEmpty(toUserName)){
            holder.reply.setVisibility(View.VISIBLE);
            holder.byReply.setText(":");
            holder.replyUser.setText(userName);
        } else {

        }

        if (selfRole.equals("2") || myId.equals(userProfile.getId())){
            holder.delete.setVisibility(View.VISIBLE);
        }else {
            holder.delete.setVisibility(View.GONE);
        }

        holder.date.setText(DateUtil.getDistanceTime(conmment.getCreate_time()));
        if (content != null && content.size() > 0) {
            for (int i = 0; i < content.size(); i++) {
                TopicReplyBean.ReplyContent replyContent = content.get(i);
                if (replyContent.getType().equals("text")) {
                    calculateDistance(holder, replyContent);
                }
            }
        }

        //点击删除
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onDeleteReplyListItem(conmment);
                }
            }
        });
        /**
         * 回复某人
         */
        holder.replyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isFastClick()) {
                    return;
                }
                if (!selfRole.equals("0")) {
                    if (listener != null) {
                        listener.onReplyToSb(conmment);
                    }
                }
            }
        });

        return convertView;
    }

    /**
     * 计算内容距离
     * @param holder
     * @param replyContent
     */
    public void calculateDistance(ViewHolder holder, TopicReplyBean.ReplyContent replyContent) {

        String replyUserText = holder.replyUser.getText().toString();
        String replyText = holder.reply.getText().toString();
        String byReplyText = holder.byReply.getText().toString();

        Paint mPaint = new Paint();
        TextView view = new TextView(mContext);
        view.setText(replyUserText + replyText + byReplyText);
        int stringWidth = (int) mPaint.measureText(view.getText().toString());
        String  model = android.os.Build.MODEL;
        if (model.equals("SM-N9002")) {
            modeWidth = 15;
        } else {
            modeWidth = 10;
        }
        int startWidth = modeWidth + stringWidth;
        //空格宽度
        int blankWidth = (int) mPaint.measureText(blank);
        //空格个数
        int blankSum = startWidth / blankWidth;

        StringBuilder sb = new StringBuilder();
        for (int j = 0; j <= blankSum; j++) {
            sb.append(blank);
        }
        holder.replyContent.setText(sb + replyContent.getContent());
    }

    static class ViewHolder {
        private TextView replyUser; //回复人
        private TextView reply; //回复
        private TextView byReply;   //被回复人
        private TextView replyContent; //回复内容
        private TextView date;    //回复时间
        private TextView delete;  //删除
        private RelativeLayout replyItem;
    }

    public OnReplyListInterface listener;

    public void setOnReplyListListener(OnReplyListInterface replyListListener) {
        this.listener = replyListListener;
    }

}
