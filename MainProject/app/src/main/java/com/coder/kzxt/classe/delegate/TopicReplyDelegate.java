package com.coder.kzxt.classe.delegate;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.adapter.ReplyImageAdapter;
import com.coder.kzxt.classe.adapter.TopicReplyAapter;
import com.coder.kzxt.classe.beans.TopicDetailBean;
import com.coder.kzxt.classe.beans.TopicReplyBean;
import com.coder.kzxt.classe.mInterface.OnReplyListInterface;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.Utils;
import com.coder.kzxt.views.MyGridView;
import com.coder.kzxt.views.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangtingshun on 2017/6/15.
 * 话题回复
 */

public class TopicReplyDelegate extends PullRefreshDelegate<TopicReplyBean.TopicReply> implements OnReplyListInterface {

    private Context mContext;
    private String group_topic_id;
    private TopicDetailBean.TopicDetail detailBean;
    private ArrayList<String> Images = new ArrayList<>();
    private String selfRole;
    private TopicReplyAapter replyAapter;
    public String userId;


    public TopicReplyDelegate(Context context, String role,String uid) {
        super(R.layout.topic_detail_head_layout, R.layout.topic_reply_list_item);
        this.selfRole = role;
        this.mContext = context;
        this.userId = uid;
    }

    public void setTopicDetailData(TopicDetailBean.TopicDetail bean) {
        this.detailBean = bean;
    }

    @Override
    public void initHeaderView(BaseViewHolder holder) {
        super.initHeaderView(holder);
        //头部
        ImageView headImg = (ImageView) holder.findViewById(R.id.iv_detail_head);
        TextView headTitle = (TextView) holder.findViewById(R.id.tv_detail_title);
        TextView publicDate = (TextView) holder.findViewById(R.id.tv_detail_time);
        TextView title = (TextView) holder.findViewById(R.id.tv_middle_title);
        TextView detailContent = (TextView) holder.findViewById(R.id.tv_middle_content);
        final ImageView zanImage = (ImageView) holder.findViewById(R.id.iv_topic_zan);
        final TextView zanCount = (TextView) holder.findViewById(R.id.tv_topic_zan);
        final ImageView collectImage = (ImageView) holder.findViewById(R.id.iv_topic_collect);
        final TextView collectCount = (TextView) holder.findViewById(R.id.tv_topic_collect);
        TextView detailReply = (TextView) holder.findViewById(R.id.tv_detail_reply);
        RelativeLayout allReplayLayout = (RelativeLayout) holder.findViewById(R.id.reply_layout);
        LinearLayout topImageLayout = (LinearLayout) holder.findViewById(R.id.top_image_layout);
        ImageView topOneImage = (ImageView) holder.findViewById(R.id.topic_one_image);
        ImageView topicTwoImage = (ImageView) holder.findViewById(R.id.topic_two_image);
        ImageView topicThreeImage = (ImageView) holder.findViewById(R.id.topic_three_image);
        TextView delete = (TextView) holder.findViewById(R.id.tv_detail_delete);

        TopicDetailBean.Creator creator = detailBean.getCreator();
        ArrayList<TopicDetailBean.DetailContent> content = detailBean.getContent();
        if (creator != null) {
            GlideUtils.loadCircleHeaderOfCommon(mContext, creator.getAvatar(), headImg);
            headTitle.setText(creator.getNickname());
        }
        String create_time = detailBean.getCreate_time();
        publicDate.setText(DateUtil.getDistanceTime(create_time));
        title.setText(detailBean.getTitle());

        zanCount.setText(detailBean.getLike_count() + "人赞过");
        collectCount.setText(detailBean.getCollect_count() + "人收藏");
        detailReply.setText(detailBean.getReply_count() + "人回复");

        if (selfRole.equals("0")) { //未加入
            zanImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
            collectImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
//            delete.setVisibility(View.GONE);
        }
//        else if (selfRole.equals("1")) {
//            delete.setVisibility(View.VISIBLE);
//        } else
        if (selfRole.equals("2") || creator.getId().equals(userId)) {
            delete.setVisibility(View.VISIBLE);
        } else {
            delete.setVisibility(View.GONE);
        }

        if (Images != null) {
            Images.clear();
        }
        int size = content.size();
        for (int i = 0; i < size; i++) {
            TopicDetailBean.DetailContent topicDetail = content.get(i);
            String type = topicDetail.getType();
            if (type.equals("img")) {
                String urlImage = topicDetail.getContent();
                Images.add(urlImage);
                if (Images != null) {
                    int imageSize = Images.size();
                    if (imageSize == 1) {
                        topOneImage.setVisibility(View.VISIBLE);
                        topicTwoImage.setVisibility(View.GONE);
                        topicThreeImage.setVisibility(View.GONE);
                        GlideUtils.loadClassTopicImg(mContext, Images.get(0), topOneImage);
                    } else if (imageSize == 2) {
                        topOneImage.setVisibility(View.VISIBLE);
                        topicTwoImage.setVisibility(View.VISIBLE);
                        topicThreeImage.setVisibility(View.GONE);
                        GlideUtils.loadClassTopicImg(mContext, Images.get(0), topOneImage);
                        GlideUtils.loadClassTopicImg(mContext, Images.get(1), topicTwoImage);
                    } else if (imageSize == 3) {
                        topOneImage.setVisibility(View.VISIBLE);
                        topicTwoImage.setVisibility(View.VISIBLE);
                        topicThreeImage.setVisibility(View.VISIBLE);
                        GlideUtils.loadClassTopicImg(mContext, Images.get(0), topOneImage);
                        GlideUtils.loadClassTopicImg(mContext, Images.get(1), topicTwoImage);
                        GlideUtils.loadClassTopicImg(mContext, Images.get(2), topicThreeImage);
                    }
                }
            } else if (type.equals("text")) {
                detailContent.setText(topicDetail.getContent());
            }
        }

        if (detailBean.getHas_like().equals("1")) {
            zanImage.setBackgroundResource(R.drawable.topic_detail_zan_red);
        } else {
            zanImage.setBackgroundResource(R.drawable.topic_detail_zan_white);
        }

        if (detailBean.getHas_collect().equals("1")) {
            collectImage.setBackgroundResource(R.drawable.topic_detail_collect_red);
        } else {
            collectImage.setBackgroundResource(R.drawable.topic_detail_collect_white);
        }

        //话题详情删除
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (topicReplyListener != null) {
                    topicReplyListener.deleteTopicDetail();
                }
            }
        });
        //点赞
        zanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (topicReplyListener != null) {
                    if (detailBean.getHas_like().equals("1")) {
                        topicReplyListener.cancleZanRequest(zanImage, zanCount);
                    } else {
                        //点赞
                        topicReplyListener.zanRequest(zanImage, zanCount);
                    }
                }
            }
        });

        //收藏
        collectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (topicReplyListener != null) {
                    if (detailBean.getHas_collect().equals("1")) {
                        topicReplyListener.cancleCollect(collectImage, collectCount);
                    } else {
                        topicReplyListener.adddCollect(collectImage, collectCount);
                    }
                }
            }
        });

    }

    @Override
    public void initCustomView(BaseViewHolder holder, List<TopicReplyBean.TopicReply> data, int position) {
        super.initCustomView(holder, data, position);

        ImageView headImage = holder.findViewById(R.id.iv_head); //头像
        TextView nickName = holder.findViewById(R.id.tv_nick_name); //昵称
        TextView tv_time = holder.findViewById(R.id.tv_time);  //时间
        TextView headContent = holder.findViewById(R.id.tv_head_content); //头部回复
        Button deleteReply = holder.findViewById(R.id.btn_head_delete); //删除

        RelativeLayout oneLevelReply = holder.findViewById(R.id.topic_head_layout);  //一级item
        RelativeLayout replyLayout = holder.findViewById(R.id.topic_reply_list_layout); //回复的layout
        final MyListView myListView = holder.findViewById(R.id.myListView);   //
        final TextView moreData = holder.findViewById(R.id.tv_more_data);
        MyGridView imge_grid = holder.findViewById(R.id.imge_grid);
        final TopicReplyBean.TopicReply replyBean = data.get(position);

        TopicReplyBean.CreateProfile createProfile = replyBean.getUser_profile();
        GlideUtils.loadCircleHeaderOfCommon(mContext, createProfile.getAvatar(), headImage);
        nickName.setText(createProfile.getNickname());
        String createTime = replyBean.getCreate_time();
        tv_time.setText(DateUtil.getDistanceTime(createTime));
        group_topic_id = replyBean.getGroup_topic_id();

        ArrayList<TopicReplyBean.CreateContent> contents = replyBean.getContent();
        String replyContent = "";
        List<String> imageList = new ArrayList<>();
        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i).getType().equals("text")) {
                replyContent += contents.get(i).getContent() + "\n";
            } else {
                imageList.add(contents.get(i).getContent());
            }
        }
        headContent.setText(replyContent);
        if (imageList.size() > 0) {
            imge_grid.setVisibility(View.VISIBLE);
            imge_grid.setAdapter(new ReplyImageAdapter(mContext, imageList));
        } else {
            imge_grid.setVisibility(View.GONE);
        }

        if (selfRole.equals("2") || createProfile.getId().equals(userId) ) {
            deleteReply.setVisibility(View.VISIBLE);
        } else {
            deleteReply.setVisibility(View.GONE);
        }

        //二级回复数据
        final ArrayList<TopicReplyBean.Conmment> replyComments = replyBean.getComments();
        int replyList = replyComments.size();
        if (replyComments != null && replyList > 0) {
            replyLayout.setVisibility(View.VISIBLE);
            replyAapter = new TopicReplyAapter(mContext, false, replyComments,userId,selfRole);
            replyAapter.setOnReplyListListener(this);
            myListView.setAdapter(replyAapter);
        } else {
            replyLayout.setVisibility(View.GONE);
        }

        if (replyList > 2) {
            int sum = replyList - 2;
            moreData.setVisibility(View.VISIBLE);
            moreData.setText("还有" + sum + "条回复");
        } else {
            moreData.setVisibility(View.GONE);
        }
        //更多数据
        moreData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (replyAapter != null) {
                    replyAapter.setTopicReplyData(true, replyComments);
                    myListView.setAdapter(replyAapter);
                }
                moreData.setVisibility(View.GONE);
            }
        });

        //删除回复
        deleteReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isFastClick()) {
                    return;
                }
                if (topicReplyListener != null) {
                    topicReplyListener.deleteTopicReply(replyBean.getId());
                }
            }
        });

        //一级item
        oneLevelReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isFastClick()) {
                    return;
                }
                if (!selfRole.equals("0")) {
                    if (topicReplyListener != null) {
                        topicReplyListener.onOneLevelItem(replyBean);
                    }
                }
            }
        });
    }


    @Override
    public void onDeleteReplyListItem(TopicReplyBean.Conmment conmment) {
        if (topicReplyListener != null) {
            topicReplyListener.deleteTopicReply(conmment.getId());
        }
    }

    @Override
    public void onReplyToSb(TopicReplyBean.Conmment sb) {
        if (topicReplyListener != null) {
            topicReplyListener.onReplyToSb(group_topic_id, sb);
        }
    }


    public DeleteTopicReplyInterface topicReplyListener;

    public interface DeleteTopicReplyInterface {

        /**
         * 删除话题回复
         *
         * @param id
         */
        void deleteTopicReply(String id);

        /**
         * 一级item
         *
         * @param replyBean
         */
        void onOneLevelItem(TopicReplyBean.TopicReply replyBean);

        /**
         * 回复某人
         *
         * @param sb
         */
        void onReplyToSb(String group_topic_id, TopicReplyBean.Conmment sb);

        /**
         * 赞
         *
         * @param zanImage
         * @param zanCount
         */
        void zanRequest(ImageView zanImage, TextView zanCount);

        /**
         * 取消赞
         *
         * @param zanImage
         * @param zanCount
         */
        void cancleZanRequest(ImageView zanImage, TextView zanCount);

        /**
         * 添加收藏
         *
         * @param collectImage
         * @param collectCount
         */
        void adddCollect(ImageView collectImage, TextView collectCount);

        /**
         * 取消添加收藏
         *
         * @param collectImage
         * @param collectCount
         */
        void cancleCollect(ImageView collectImage, TextView collectCount);

        /**
         * 删除话题详情
         */
        void deleteTopicDetail();

    }

    public void setOnDeleteTopicReplyListener(DeleteTopicReplyInterface listener) {
        this.topicReplyListener = listener;
    }

}
