package com.coder.kzxt.question.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.question.beans.QuestionListBean;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.DateUtil;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/3/13
 */

public class QuestionListDelegate extends PullRefreshDelegate<QuestionListBean.ItemsBean>
{
    private Context context;

    public QuestionListDelegate(Context context)
    {
        super(R.layout.item_my_question);
        this.context = context;
    }

    @Override
    public void initCustomView(BaseViewHolder convertView, List<QuestionListBean.ItemsBean> data, int position)
    {

        final ImageView user_head_img = (ImageView) convertView.findViewById(R.id.user_head_img);
        ImageView status_image = (ImageView) convertView.findViewById(R.id.status_image);
        TextView user_name_tx = (TextView) convertView.findViewById(R.id.user_name_tx);
        TextView publish_time = (TextView) convertView.findViewById(R.id.publish_time);
        TextView question_content_tx = (TextView) convertView.findViewById(R.id.question_content_tx);

        TextView question_look_num = (TextView) convertView.findViewById(R.id.question_look_num);
        TextView question_reply_num_tx = (TextView) convertView.findViewById(R.id.question_reply_num_tx);

        LinearLayout imgs_ly = (LinearLayout) convertView.findViewById(R.id.imgs_ly);
        ImageView question_single_img_iv = (ImageView) convertView.findViewById(R.id.question_single_img_iv);
        ImageView question_one_img_iv = (ImageView) convertView.findViewById(R.id.question_one_img_iv);
        ImageView question_two_img_iv = (ImageView) convertView.findViewById(R.id.question_two_img_iv);
        ImageView question_tr_img_iv = (ImageView) convertView.findViewById(R.id.question_tr_img_iv);


        user_name_tx.setText(data.get(position).getUser().getNickname());

        Glide.with(context).load(data.get(position).getUser().getAvatar()).asBitmap().override(180, 180).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.default_teacher_header).error(R.drawable.default_teacher_header).centerCrop().into(new BitmapImageViewTarget(user_head_img) {

            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCornerRadius(25);
                user_head_img.setImageDrawable(circularBitmapDrawable);
            }

        });
        publish_time.setText(DateUtil.getDayBef(data.get(position).getCreate_time()));
        question_content_tx.setText(data.get(position).getTitle());
        question_look_num.setText(data.get(position).getHit_num());
        question_reply_num_tx.setText(data.get(position).getPost_num());
        if(!data.get(position).getBest_answer_id().equals("0")){
            status_image.setImageResource(R.drawable.solve_image);
        }else {
            status_image.setImageResource(R.drawable.unsolve_image);

        }

//        if (imgs.size() == 0)
//        {
//            question_single_img_iv.setVisibility(View.GONE);
//            imgs_ly.setVisibility(View.GONE);
//        } else
//        {
//            if (imgs.size() == 1)
//            {
//                question_single_img_iv.setVisibility(View.VISIBLE);
//                imgs_ly.setVisibility(View.GONE);
//                GlideUtils.loadPorstersImg(context, imgs.get(0), question_single_img_iv);
//            } else if (imgs.size() == 2)
//            {
//
//                question_single_img_iv.setVisibility(View.GONE);
//                imgs_ly.setVisibility(View.VISIBLE);
//                question_one_img_iv.setVisibility(View.VISIBLE);
//                question_two_img_iv.setVisibility(View.VISIBLE);
//                question_tr_img_iv.setVisibility(View.INVISIBLE);
//                GlideUtils.loadPorstersImg(context, imgs.get(0), question_one_img_iv);
//                GlideUtils.loadPorstersImg(context, imgs.get(1), question_two_img_iv);
//
//            } else if (imgs.size() == 3)
//            {
//
//                question_single_img_iv.setVisibility(View.GONE);
//                imgs_ly.setVisibility(View.VISIBLE);
//                question_one_img_iv.setVisibility(View.VISIBLE);
//                question_two_img_iv.setVisibility(View.VISIBLE);
//                question_tr_img_iv.setVisibility(View.VISIBLE);
//                GlideUtils.loadPorstersImg(context, imgs.get(0), question_one_img_iv);
//                GlideUtils.loadPorstersImg(context, imgs.get(1), question_two_img_iv);
//                GlideUtils.loadPorstersImg(context, imgs.get(2), question_tr_img_iv);
//            } else
//            {
//
//                question_single_img_iv.setVisibility(View.GONE);
//                imgs_ly.setVisibility(View.VISIBLE);
//                question_one_img_iv.setVisibility(View.VISIBLE);
//                question_two_img_iv.setVisibility(View.VISIBLE);
//                question_tr_img_iv.setVisibility(View.VISIBLE);
//                GlideUtils.loadPorstersImg(context, imgs.get(0), question_one_img_iv);
//                GlideUtils.loadPorstersImg(context, imgs.get(1), question_two_img_iv);
//                GlideUtils.loadPorstersImg(context, imgs.get(2), question_tr_img_iv);
//            }
//
//        }
//
//        if (!TextUtils.isEmpty(audioUrl))
//        {
//            voice_ly.setVisibility(View.VISIBLE);
//            video_time_text.setText(audioDuration + "\"");
//
//            ViewGroup.LayoutParams layoutParams = video_question_bj_layout.getLayoutParams();
//            if (Integer.parseInt(audioDuration) <= 10)
//            {
//
//                layoutParams.width = (int) context.getResources().getDimension(R.dimen.woying_80_dip);
//                layoutParams.height = (int) context.getResources().getDimension(R.dimen.woying_30_dip);
//
//            } else if (Integer.parseInt(audioDuration) > 10 && Integer.parseInt(audioDuration) <= 30)
//            {
//
//                layoutParams.width = (int) context.getResources().getDimension(R.dimen.woying_120_dip);
//                layoutParams.height = (int) context.getResources().getDimension(R.dimen.woying_30_dip);
//
//            } else if (Integer.parseInt(audioDuration) > 30 && Integer.parseInt(audioDuration) <= 50)
//            {
//
//                layoutParams.width = (int) context.getResources().getDimension(R.dimen.woying_140_dip);
//                layoutParams.height = (int) context.getResources().getDimension(R.dimen.woying_30_dip);
//
//            } else if (Integer.parseInt(audioDuration) > 50 && Integer.parseInt(audioDuration) <= 60)
//            {
//
//                layoutParams.width = (int) context.getResources().getDimension(R.dimen.woying_150_dip);
//                layoutParams.height = (int) context.getResources().getDimension(R.dimen.woying_30_dip);
//
//            }
//
//        } else
//        {
//            voice_ly.setVisibility(View.GONE);
//        }
    }
}
