package com.coder.kzxt.poster.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.im.util.EmojiUtil;
import com.coder.kzxt.poster.activity.Posters_All_like_User_Activity;
import com.coder.kzxt.poster.activity.Show_Image_Activity;
import com.coder.kzxt.poster.beans.PosterBeans;
import com.coder.kzxt.poster.beans.PostersCommentBean;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.views.StgImageView;

import java.util.List;


/**
 * Created by tcy on 2017/3/2.
 */
public class PosterParticularsDelegate extends PullRefreshDelegate<PostersCommentBean.ItemsBean> {
    private Context mContext;
    private PosterBeans.ItemsBean postersHeaderBean;
    private StgImageView posters_img;
    private TextView posters_text_tx;
    private TextView posters_tx;
    private RelativeLayout show_img_tip_ly;
    private ImageView user_img;
    private TextView user_tx;
    private TextView user_time;
    private TextView browse_num_tx;
    private TextView  likeNum;
    private TextView  com_num_tx;
    private  String postersId;


    public PosterParticularsDelegate(Context context, PosterBeans.ItemsBean postersHeaderBean) {
        super(R.layout.posters_header,R.layout.posters_replay_item);
        this.mContext = context;
        this.postersHeaderBean = postersHeaderBean;
    }

    @Override
    public void initHeaderView(BaseViewHolder holder ) {
         posters_img = (StgImageView) holder.findViewById(R.id.posters_img);
         posters_text_tx = (TextView) holder.findViewById(R.id.posters_text_tx);
         posters_tx = (TextView) holder.findViewById(R.id.posters_tx);
         show_img_tip_ly = (RelativeLayout) holder.findViewById(R.id.show_img_tip_ly);
         user_img  = (ImageView) holder.findViewById(R.id.user_img);
         user_tx = (TextView) holder.findViewById(R.id.user_tx);
         user_time = (TextView) holder.findViewById(R.id.user_time);
         browse_num_tx = (TextView) holder.findViewById(R.id.browse_num_tx);
         likeNum = (TextView) holder.findViewById(R.id.likeNum);
         com_num_tx = (TextView) holder.findViewById(R.id.com_num_tx);

        postersId = postersHeaderBean.getId();
        String desc = postersHeaderBean.getDesc();
        String picHeight =postersHeaderBean.getPic_height();
        String picWidth = postersHeaderBean.getPic_width();
        final String picture = postersHeaderBean.getPic();
        String bgColor = postersHeaderBean.getBg_color();
        String userFace = postersHeaderBean.getUser().getAvatar();
        String userName = postersHeaderBean.getUser().getNickname();
        String ctm = postersHeaderBean.getCreate_time();
        String like = postersHeaderBean.getPoster_like();
        String comments = postersHeaderBean.getComment_count();
        String pv = postersHeaderBean.getPv();

        if (postersHeaderBean.getType()==1){
            posters_img.setVisibility(View.VISIBLE);
            posters_text_tx.setVisibility(View.GONE);
            if (TextUtils.isEmpty(desc)) {
                posters_tx.setVisibility(View.GONE);
            } else {
                posters_tx.setVisibility(View.VISIBLE);
                posters_tx.setText(desc.trim());
            }

            if (!TextUtils.isEmpty(picHeight) && !TextUtils.isEmpty(picWidth)) {
                if(picHeight.equals("0")||picWidth.equals("0")){
                    posters_img.mHeight = 1;
                    posters_img.mWidth = 1;
                }else {
                    double a = Integer.valueOf(picHeight) / Integer.valueOf(picWidth);
                    if (a > 2) {
                        posters_img.mWidth =1;
                        posters_img.mHeight = 2;
                        posters_img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        show_img_tip_ly.setVisibility(View.VISIBLE);
                    } else if (a < 0.3) {
                        posters_img.mWidth =2;
                        posters_img.mHeight = 1;
                        posters_img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    } else {
                        posters_img.mWidth = Integer.valueOf(picWidth);
                        posters_img.mHeight = Integer.valueOf(picHeight);
                        posters_img.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                }
            } else {
                posters_img.mHeight = 1;
                posters_img.mWidth = 1;
            }

            GlideUtils.loadPorstersImg(mContext,picture,posters_img);
        }else {
            posters_text_tx.setVisibility(View.VISIBLE);
            posters_img.setVisibility(View.GONE);
            posters_tx.setVisibility(View.GONE);
            posters_text_tx.setText(desc.trim());
            posters_text_tx.setBackgroundColor(Color.parseColor(bgColor));
        }
        GlideUtils.loadCircleHeaderOfCommon(mContext,userFace,user_img);
        user_tx.setText(userName);
        user_time.setText(DateUtil.getDistanceTime(ctm));
        GradientDrawable myGrad = (GradientDrawable) likeNum.getBackground();
        myGrad.setStroke(1, ContextCompat.getColor(mContext, R.color.font_gray));// 设置边框宽度与颜色
        likeNum.setText(like);
        if(like.equals("0")){
            likeNum.setVisibility(View.GONE);
        }else {
            likeNum.setVisibility(View.VISIBLE);
        }

        browse_num_tx.setText(pv);
        com_num_tx.setText("评论"+"("+comments+")");

        //全部喜欢的人
        likeNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Posters_All_like_User_Activity.class);
                intent.putExtra("postersId", postersId);
                intent.putExtra("type", "like");
                mContext.startActivity(intent);
            }
        });
        posters_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Show_Image_Activity.class);
                intent.putExtra("imgurl", picture);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public void initCustomView(BaseViewHolder holder, List<PostersCommentBean.ItemsBean> obj, int position) {

        RelativeLayout posters_zong_layout = (RelativeLayout) holder.findViewById(R.id.posters_zong_layout);
        ImageView user_head_img = (ImageView) holder.findViewById(R.id.user_head_img);
        TextView user_name = (TextView) holder.findViewById(R.id.user_name);
        TextView posters_content_text = (TextView) holder.findViewById(R.id.posters_content_text);
        TextView user_time = (TextView) holder.findViewById(R.id.user_time);

        final PostersCommentBean.ItemsBean commentBean = obj.get(position);
        final String com_content = commentBean.getContent();
        final String sendName = commentBean.getUser().getNickname();
        final String pid = commentBean.getPid();
        final String sendFace = commentBean.getUser().getAvatar();
        final String ctm = commentBean.getCreate_time();

        if(pid.equals("0")){
            posters_content_text.setText(com_content);
        }else {
            if(commentBean.getReply()!=null){
                String replyName = commentBean.getReply().getNickname();
                SpannableString spannableString = EmojiUtil.getInstace().getSpannableString(mContext, "回复 "+replyName+":"+com_content.toString());
                spannableString.setSpan(new TextAppearanceSpan(mContext, R.style.style_text0), 3, 3+replyName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                posters_content_text.setText(spannableString);
            }else {
                posters_content_text.setText(com_content);
            }

        }
        user_name.setText(sendName);
        user_time.setText(DateUtil.getDistanceTime(ctm));
        GlideUtils.loadCircleHeaderOfCommon(mContext,sendFace,user_head_img);

    }

}
