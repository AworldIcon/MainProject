package com.coder.kzxt.question.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.question.activity.CourseQuestionsActivity;
import com.coder.kzxt.question.activity.QuestionsDetailActivity;
import com.coder.kzxt.base.beans.TextImageBean;
import com.coder.kzxt.question.beans.CourseQuestionResultBean;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 2017/4/18.
 */

public class CourseQuestionDelegate extends PullRefreshDelegate<CourseQuestionResultBean.ItemsBean> {
    private Context context;
    private int type;
    private int isJoin;

    public CourseQuestionDelegate(Context context, int type, int isJoin) {
        super(R.layout.item_my_question);
        this.context = context;
        this.type = type;
        this.isJoin = isJoin;
    }

    @Override
    public void initCustomView(BaseViewHolder convertView, final List<CourseQuestionResultBean.ItemsBean> data, final int position) {

        final ImageView user_head_img = (ImageView) convertView.findViewById(R.id.user_head_img);
        ImageView status_image = (ImageView) convertView.findViewById(R.id.status_image);
        ImageView elite_image = (ImageView) convertView.findViewById(R.id.elite_image);
        TextView user_name_tx = (TextView) convertView.findViewById(R.id.user_name_tx);
        TextView publish_time = (TextView) convertView.findViewById(R.id.publish_time);
        TextView question_content_tx = (TextView) convertView.findViewById(R.id.question_content_tx);

        TextView question_look_num = (TextView) convertView.findViewById(R.id.question_look_num);
        TextView question_reply_num_tx = (TextView) convertView.findViewById(R.id.question_reply_num_tx);

        LinearLayout imgs_ly = (LinearLayout) convertView.findViewById(R.id.imgs_ly);
        ImageView question_one_img_iv = (ImageView) convertView.findViewById(R.id.question_one_img_iv);
        ImageView question_two_img_iv = (ImageView) convertView.findViewById(R.id.question_two_img_iv);
        ImageView question_tr_img_iv = (ImageView) convertView.findViewById(R.id.question_tr_img_iv);

        List<String> imageList = new ArrayList<>();
        imageList.clear();
        for (int i = 0; i < data.get(position).getContent().size(); i++) {
            TextImageBean contentBean = data.get(position).getContent().get(i);
            if (contentBean.getType() == 2) {
                imageList.add(contentBean.getContent());
            }
        }


        user_name_tx.setText(data.get(position).getUser().getNickname());
        GlideUtils.loadCircleHeaderOfCommon(context,data.get(position).getUser().getAvatar(),user_head_img);

//        Glide.with(context).load(data.get(position).getUser().getAvatar()).asBitmap().override(180, 180).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.default_teacher_header).error(R.drawable.default_teacher_header).centerCrop().into(new BitmapImageViewTarget(user_head_img) {
//
//            @Override
//            protected void setResource(Bitmap resource) {
//                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
//                circularBitmapDrawable.setCornerRadius(25);
//                user_head_img.setImageDrawable(circularBitmapDrawable);
//            }
//
//        });

        publish_time.setText(DateUtil.getDayBef(data.get(position).getCreate_time())+"  发布");
        question_content_tx.setText(data.get(position).getTitle());
        question_look_num.setText(data.get(position).getHit_num());
        question_reply_num_tx.setText(data.get(position).getPost_num());
        if (type == 1 || type == 2) {
            status_image.setVisibility(View.GONE);
        }

        if (!data.get(position).getBest_answer_id().equals("0")) {
            status_image.setImageResource(R.drawable.solve_image);
        } else {
            status_image.setImageResource(R.drawable.unsolve_image);

        }

        if (data.get(position).getIs_elite().equals("0")) {
            elite_image.setVisibility(View.GONE);
        } else {
            elite_image.setVisibility(View.VISIBLE);
        }
        if (type == 3) {
            elite_image.setVisibility(View.GONE);
        }
        if (imageList.size() > 0) {
            imgs_ly.setVisibility(View.VISIBLE);
            if (imageList.size() == 1) {
                GlideUtils.loadQuestionsImg(context, imageList.get(0), question_one_img_iv);
                question_one_img_iv.setVisibility(View.VISIBLE);
                question_two_img_iv.setVisibility(View.INVISIBLE);
                question_tr_img_iv.setVisibility(View.INVISIBLE);

            } else if (imageList.size() == 2) {
                GlideUtils.loadQuestionsImg(context, imageList.get(0), question_one_img_iv);
                GlideUtils.loadQuestionsImg(question_two_img_iv.getContext(), imageList.get(1), question_two_img_iv);
                question_one_img_iv.setVisibility(View.VISIBLE);
                question_two_img_iv.setVisibility(View.VISIBLE);
                question_tr_img_iv.setVisibility(View.INVISIBLE);
            } else if (imageList.size() >= 3) {
                GlideUtils.loadQuestionsImg(context, imageList.get(0), question_one_img_iv);
                GlideUtils.loadQuestionsImg(question_two_img_iv.getContext(), imageList.get(1), question_two_img_iv);
                GlideUtils.loadQuestionsImg(question_tr_img_iv.getContext(), imageList.get(2), question_tr_img_iv);
                question_one_img_iv.setVisibility(View.VISIBLE);
                question_two_img_iv.setVisibility(View.VISIBLE);
                question_tr_img_iv.setVisibility(View.VISIBLE);
            }
        } else {
            imgs_ly.setVisibility(View.GONE);
        }


        convertView.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CourseQuestionsActivity) context).startActivityForResult(new Intent(context, QuestionsDetailActivity.class)
                        .putExtra("questionId", data.get(position).getId())
                        .putExtra("isJoin", isJoin)
                        .putExtra("nickname", data.get(position).getUser().getNickname())
                        .putExtra("courseId", data.get(position).getCourse_id())
                        .putExtra("replyNum", data.get(position).getPost_num())
                        .putExtra("userId", data.get(position).getUser_id()), 1);

            }
        });
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
