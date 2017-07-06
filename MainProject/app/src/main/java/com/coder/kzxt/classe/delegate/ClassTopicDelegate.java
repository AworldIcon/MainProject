package com.coder.kzxt.classe.delegate;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.beans.ClassTopicBean;
import com.coder.kzxt.classe.mInterface.OnClassTopicInterface;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangtingshun on 2017/6/12.
 * 班级话题的delegate
 */

public class ClassTopicDelegate extends PullRefreshDelegate<ClassTopicBean.ClassTopic> {

    private Context mContext;

    public ClassTopicDelegate(Context context) {
        super(R.layout.class_topic_item);
        this.mContext = context;
    }


    @Override
    public void initCustomView(BaseViewHolder holder, List<ClassTopicBean.ClassTopic> data, int position) {
        super.initCustomView(holder, data, position);

        ImageView stickImage = holder.findViewById(R.id.iv_top); //置顶
        ImageView refinedImage = holder.findViewById(R.id.iv_refined);   //精华
        TextView topicTitle = holder.findViewById(R.id.class_topic_title);  //标题
        LinearLayout topImageLayout = holder.findViewById(R.id.top_image_layout); //顶部图片
        ImageView  oneImage = holder.findViewById(R.id.topic_one_image); // 第一张图
        ImageView  twoImage = holder.findViewById(R.id.topic_two_image); // 第二张图
        ImageView  threeImage = holder.findViewById(R.id.topic_three_image); // 第三张图

        ImageView headImage = holder.findViewById(R.id.iv_head_image);     //头像
        TextView topicNmae = holder.findViewById(R.id.tv_bottom_name);  //话题名字
        TextView topicTime =  holder.findViewById(R.id.tv_time);  //发表话题时间
        RelativeLayout praiseLayout = holder.findViewById(R.id.praise_layout);  //点赞
        TextView praiseSum = holder.findViewById(R.id.tv_praise_sum);
        RelativeLayout replyLayout = holder.findViewById(R.id.reply_layout); //回复
        TextView replySum = holder.findViewById(R.id.tv_reply_sum);
        RelativeLayout topicLayout = holder.findViewById(R.id.class_topic_layout); //话题item

        final ClassTopicBean.ClassTopic classTopic = data.get(position);
        String isTop = classTopic.getIs_top();
        String isStick = classTopic.getIs_stick();
        if (isTop.equals("1")) {
            stickImage.setVisibility(View.VISIBLE);
        } else {
            stickImage.setVisibility(View.GONE);
        }
        if (isStick.equals("1")) {
            refinedImage.setVisibility(View.VISIBLE);
        } else {
            refinedImage.setVisibility(View.GONE);
        }

        topicTitle.setText(classTopic.getTitle());

        ClassTopicBean.ClassTopic.Content content = classTopic.getContent();
        ArrayList<String> images = content.getImages();
        int size = images.size();
        if (size > 0) {
            topImageLayout.setVisibility(View.VISIBLE);
            if (size == 1) {
                GlideUtils.loadClassTopicImg(mContext, images.get(0), oneImage);
            } else if (size == 2) {
                GlideUtils.loadClassTopicImg(mContext, images.get(0), oneImage);
                GlideUtils.loadClassTopicImg(mContext, images.get(1), twoImage);
            } else {
                GlideUtils.loadClassTopicImg(mContext, images.get(0), oneImage);
                GlideUtils.loadClassTopicImg(mContext, images.get(1), twoImage);
                GlideUtils.loadClassTopicImg(mContext, images.get(2), threeImage);
            }
        } else {
            topImageLayout.setVisibility(View.GONE);
        }

        ClassTopicBean.ClassTopic.Creator creator = classTopic.getCreator();
        GlideUtils.loadCircleHeaderOfCommon(mContext,creator.getAvatar(),headImage);
        topicNmae.setText(creator.getNickname());
        String create_time = classTopic.getCreate_time();
        topicTime.setText(DateUtil.getDayBef(Long.parseLong(create_time)));
        praiseSum.setText(classTopic.getLike_count());
        replySum.setText(classTopic.getReply_count());

        praiseLayout.setOnClickListener(new View.OnClickListener() {   //点赞次数
            @Override
            public void onClick(View view) {

           }
        });

        replyLayout.setOnClickListener(new View.OnClickListener() {  //回复次数
            @Override
            public void onClick(View view) {

            }
        });

        //话题item
        topicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClassTopicListener != null) {
                    onClassTopicListener.onClassTopicItem(classTopic);
                }
            }
        });
    }

    public OnClassTopicInterface onClassTopicListener;


    public void setOnClassTopicListener(OnClassTopicInterface listener){
        this.onClassTopicListener = listener;
    }

}
