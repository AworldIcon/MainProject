package com.coder.kzxt.poster.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.http.utils.CancelPraise;
import com.coder.kzxt.http.utils.PraisePoster;
import com.coder.kzxt.login.activity.LoginActivity;
import com.coder.kzxt.poster.beans.PosterBeans;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.SharedPreferencesUtil;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/1/9
 */


public class MyPromotionDelegate extends PullRefreshDelegate<PosterBeans.ItemsBean> {

    private Context mContext;
    private Integer[] drawableResources;
    private SharedPreferencesUtil spu;


    public MyPromotionDelegate(Context context) {
        super(R.layout.item_poseter);
        this.mContext = context;
        spu = new SharedPreferencesUtil(context);
        drawableResources = new Integer[]{
                R.drawable.poster_template1,
                R.drawable.poster_template2,
                R.drawable.poster_template3,
                R.drawable.poster_template4,
                R.drawable.poster_template5,
                R.drawable.poster_template6,
                R.drawable.poster_template7,
                R.drawable.poster_template8,
                R.drawable.poster_template9
        };
    }


    @Override
    public void initCustomView(BaseViewHolder holder, List<PosterBeans.ItemsBean> obj, final int position) {
        LinearLayout allLayout = (LinearLayout) holder.findViewById(R.id.allLayout);
        ImageView userFace = (ImageView) holder.findViewById(R.id.userFace);
        TextView userName = (TextView) holder.findViewById(R.id.userName);
        TextView createTime = (TextView) holder.findViewById(R.id.createTime);
        RelativeLayout imageRy = (RelativeLayout) holder.findViewById(R.id.imageRy);
        ImageView stgImage = (ImageView) holder.findViewById(R.id.stgImage);
        TextView content1 = (TextView) holder.findViewById(R.id.content1);
        final TextView content2 = (TextView) holder.findViewById(R.id.content2);
        TextView comment_count = (TextView) holder.findViewById(R.id.comment_count);
        TextView look_count = (TextView) holder.findViewById(R.id.look_count);
        final TextView like_count = (TextView) holder.findViewById(R.id.like_count);
        LinearLayout likeLy = (LinearLayout) holder.findViewById(R.id.likeLy);
        final ImageView likeImage = (ImageView) holder.findViewById(R.id.likeImage);

        final PosterBeans.ItemsBean bean = obj.get(position);
//
        GlideUtils.loadCircleHeaderOfCommon(mContext, bean.getUser().getAvatar(), userFace);
        userName.setText(bean.getUser().getNickname());
        createTime.setText(DateUtil.getDistanceTime(bean.getCreate_time()));
        look_count.setText(bean.getPv());
        comment_count.setText(bean.getComment_count());
        like_count.setText(bean.getPoster_like());

        if (bean.getIs_like().equals("1")) {
            likeImage.setImageResource(R.drawable.like_select);
        } else {
            likeImage.setImageResource(R.drawable.like_unselect);
        }

        // 图文 排版 赋值 内容和图片
        if(bean.getType()==1){

            content2.setVisibility(View.GONE);
            imageRy.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(bean.getDesc())) {
                content1.setVisibility(View.VISIBLE);
                content1.setText(bean.getDesc());
            } else {
                content1.setVisibility(View.GONE);
            }
//            if (bean.getPic_width().equals("0") || bean.getPic_height().equals("0")) {
//                stgImage.mWidth = stgImage.mHeight = 1;
//            } else {
//                double a = Double.valueOf(bean.getPic_height()) / Double.valueOf(bean.getPic_width());
//                if (a > 2) {
//                    stgImage.mWidth = 1;
//                    stgImage.mHeight = 2;
//                    stgImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                } else if (a < 0.3) {
//                    stgImage.mWidth = 2;
//                    stgImage.mHeight = 1;
//                    stgImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                } else {
//                    stgImage.mWidth = Integer.valueOf(bean.getPic_width());
//                    stgImage.mHeight = Integer.valueOf(bean.getPic_height());
//                    stgImage.setScaleType(ImageView.ScaleType.FIT_XY);
//                }
//            }
        }else {
            content1.setVisibility(View.GONE);
            imageRy.setVisibility(View.GONE);
            content2.setVisibility(View.VISIBLE);
            content2.setText(bean.getDesc());
            content2.setBackgroundColor(Color.parseColor(bean.getBg_color()));

//            if (bean.getBgColorId().equals("1")) {
//                content2.setTextColor(ContextCompat.getColor(mContext, R.color.font_black));
//            } else {
//                content2.setTextColor(ContextCompat.getColor(mContext, R.color.white));
//            }

        }

//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) stgImage.getLayoutParams();
//        lp.height = stgImage.mHeight;
//        stgImage.setLayoutParams(lp);
//        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        allLayout.setLayoutParams(lp2);

        GlideUtils.loadPoseters(mContext, bean.getPic(), drawableResources[position % 8], stgImage);

        likeLy.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (TextUtils.isEmpty(spu.getIsLogin())) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                    return;
                }
                int likeNum = Integer.valueOf(bean.getPoster_like());
                if (bean.getIs_like().equals("1")) {
                    likeImage.setImageResource(R.drawable.posters_like);
                    Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.img_scale_in);
                    likeImage.startAnimation(animation);
                    bean.setIs_like("0");
                    bean.setPoster_like((likeNum - 1) + "");
                    like_count.setText(bean.getPoster_like());
                    //取消点赞
                    new CancelPraise(mContext,spu.getUid(),bean.getId());
                } else {
                    likeImage.setImageResource(R.drawable.posters_like_down);
                    Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.img_scale_in);
                    likeImage.startAnimation(animation);
                    bean.setIs_like("1");
                    bean.setPoster_like((likeNum +1) + "");
                    like_count.setText(bean.getPoster_like());
                    //点赞接口
                    new PraisePoster(mContext,spu.getUid(),bean.getId());
                }

            }
        });

//        allLayout.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, Posters_Particulars_Activity.class);
//                intent.putExtra("bean", bean);
//                intent.putExtra("position", position);
//                ((PosterActivity) mContext).startActivityForResult(intent, 4);
//            }
//        });

    }
}
