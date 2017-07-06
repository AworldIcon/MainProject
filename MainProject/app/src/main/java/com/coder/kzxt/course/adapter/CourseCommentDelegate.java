package com.coder.kzxt.course.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.course.activity.CourseSynopsisActivity;
import com.coder.kzxt.course.activity.PostCourseCommentActivity;
import com.coder.kzxt.course.activity.TeacherBriefActivity;
import com.coder.kzxt.course.beans.CommentBean;
import com.coder.kzxt.course.beans.TeachearBean;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/8.
 */

public class CourseCommentDelegate extends PullRefreshDelegate<CommentBean.ItemsBean> {
    private SharedPreferencesUtil spu;
    private Context context;
    private CourseSynopsisActivity activity;
    private com.coder.kzxt.base.beans.CourseBean courseBean;
    private TeachearBean teachearBean;
    private String treeId;
    TextView comment_num_tv;
    private int isJoin;

    public CourseCommentDelegate(Context context, com.coder.kzxt.base.beans.CourseBean courseBean,
                                 TeachearBean teachearBean,String treeId,int isJoin) {
        super(R.layout.course_synopsis_header, R.layout.course_comment_item);
        this.context = context;
        this.courseBean =courseBean;
        this.teachearBean = teachearBean;
        this.isJoin = isJoin;
        this.treeId = treeId;
        activity = (CourseSynopsisActivity) context;
        spu = new SharedPreferencesUtil(context);
    }

    @Override
    public void initHeaderView(BaseViewHolder holder) {

         ImageView course_img = holder.findViewById(R.id.course_img);
         TextView title_tv = holder.findViewById(R.id.title_tv);
         RatingBar ratingBar = holder.findViewById(R.id.ratingBar);
         TextView ratingbar_tv = holder.findViewById(R.id.ratingbar_tv);
         TextView study_num_tv= holder.findViewById(R.id.study_num_tv);
         TextView price_tv =holder.findViewById(R.id.price_tv);
         comment_num_tv =holder.findViewById(R.id.comment_num_tv);
         RelativeLayout comment_ly = holder.findViewById(R.id.comment_ly);
         ExpandableTextView expandableTextView = holder.findViewById(R.id.expand_text_view);

         LinearLayout teacher_ly1 =holder.findViewById(R.id.teacher_ly1);
         ImageView teacher_img1 = holder.findViewById(R.id.teacher_img1);
         TextView teacher_name1 = holder.findViewById(R.id.teacher_name1);

        LinearLayout teacher_ly2 =holder.findViewById(R.id.teacher_ly2);
        ImageView teacher_img2 = holder.findViewById(R.id.teacher_img2);
        TextView teacher_name2 = holder.findViewById(R.id.teacher_name2);

        LinearLayout teacher_ly3 =holder.findViewById(R.id.teacher_ly3);
        ImageView teacher_img3 = holder.findViewById(R.id.teacher_img3);
        TextView teacher_name3 = holder.findViewById(R.id.teacher_name3);

        String pic  = courseBean.getMiddle_pic();
        String title = courseBean.getTitle();
        String price = courseBean.getPrice();
        String desc = courseBean.getDesc();
        String student_num =  courseBean.getStudent_num();
        float grade = courseBean.getGrade();
        String is_free =courseBean.getIs_free();
        title_tv.setText(title);
        study_num_tv.setText(student_num+"人学过");
        if(is_free.equals("1")){
            price_tv.setText("￥"+price);
            price_tv.setTextColor(context.getResources().getColor(R.color.font_yew));
        }else {
            price_tv.setText("免费");
            price_tv.setTextColor(context.getResources().getColor(R.color.bg_green));

        }

        ratingbar_tv.setText(grade+"分");
        ratingBar.setStepSize((float) 0.5);
        int i=(int)grade;
        if(i==grade){
            ratingBar.setRating(grade);
        }else {
            ratingBar.setRating((float) ((float)i+0.5));
        }

        expandableTextView.setText(desc);
        GlideUtils.loadCourseImg(context,pic,course_img);

        List<TeachearBean.ItemsBean> itemsBeen = teachearBean.getItems();
        if(itemsBeen.size()==1){
            teacher_ly1.setVisibility(View.VISIBLE);
            final String nickName = itemsBeen.get(0).getProfile().getNickname();
            final String avatar =  itemsBeen.get(0).getProfile().getAvatar();
            GlideUtils.loadHeaderOfTeacher(context,avatar,teacher_img1);
            teacher_name1.setText(nickName);

            teacher_img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TeacherBriefActivity.class);
                    intent.putExtra("name",nickName);
                    intent.putExtra("faceUrl",avatar);
                    intent.putExtra("model_key", "FAMOUS_TEACHER_ONE");
                    context.startActivity(intent);
                }
            });

        }else {
            if (itemsBeen.size() == 2) {
                teacher_ly1.setVisibility(View.VISIBLE);
                teacher_ly2.setVisibility(View.VISIBLE);

                final String nickName = itemsBeen.get(0).getProfile().getNickname();
                final String avatar = itemsBeen.get(0).getProfile().getAvatar();
                GlideUtils.loadHeaderOfTeacher(context, avatar, teacher_img1);
                teacher_name1.setText(nickName);

                final String nickName1 = itemsBeen.get(1).getProfile().getNickname();
                final String avatar1 = itemsBeen.get(1).getProfile().getAvatar();
                GlideUtils.loadHeaderOfTeacher(context, avatar1, teacher_img2);
                teacher_name2.setText(nickName1);

                teacher_img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, TeacherBriefActivity.class);
                        intent.putExtra("name", nickName);
                        intent.putExtra("faceUrl", avatar);
                        intent.putExtra("model_key", "FAMOUS_TEACHER_ONE");
                        context.startActivity(intent);
                    }
                });


                teacher_img2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, TeacherBriefActivity.class);
                        intent.putExtra("name", nickName1);
                        intent.putExtra("faceUrl", nickName1);
                        intent.putExtra("model_key", "FAMOUS_TEACHER_ONE");
                        context.startActivity(intent);
                    }
                });


            } else if (itemsBeen.size() == 3) {
                teacher_ly1.setVisibility(View.VISIBLE);
                teacher_ly2.setVisibility(View.VISIBLE);
                teacher_ly3.setVisibility(View.VISIBLE);

                final String nickName = itemsBeen.get(0).getProfile().getNickname();
                final String avatar = itemsBeen.get(0).getProfile().getAvatar();
                GlideUtils.loadHeaderOfTeacher(context, avatar, teacher_img1);
                teacher_name1.setText(nickName);

                final String nickName1 = itemsBeen.get(1).getProfile().getNickname();
                final String avatar1 = itemsBeen.get(1).getProfile().getAvatar();
                GlideUtils.loadHeaderOfTeacher(context, avatar1, teacher_img2);
                teacher_name2.setText(nickName1);

                final String nickName2 = itemsBeen.get(2).getProfile().getNickname();
                final String avatar2 = itemsBeen.get(2).getProfile().getAvatar();
                GlideUtils.loadHeaderOfTeacher(context, avatar2, teacher_img3);
                teacher_name3.setText(nickName2);


                teacher_img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, TeacherBriefActivity.class);
                        intent.putExtra("name", nickName);
                        intent.putExtra("faceUrl", avatar);
                        intent.putExtra("model_key", "FAMOUS_TEACHER_ONE");
                        context.startActivity(intent);
                    }
                });


                teacher_img2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, TeacherBriefActivity.class);
                        intent.putExtra("name", nickName1);
                        intent.putExtra("faceUrl", nickName1);
                        intent.putExtra("model_key", "FAMOUS_TEACHER_ONE");
                        context.startActivity(intent);
                    }
                });


                teacher_img3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, TeacherBriefActivity.class);
                        intent.putExtra("name", nickName2);
                        intent.putExtra("faceUrl", nickName2);
                        intent.putExtra("model_key", "FAMOUS_TEACHER_ONE");
                        context.startActivity(intent);
                    }
                });
            }
        }

        comment_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(spu.getIsLogin())||isJoin==0){
                    activity.showJoinTip();
                    return;
                }
                Intent intent = new Intent(context,PostCourseCommentActivity.class);
                intent.putExtra("treeId",treeId);
                activity.startActivityForResult(intent,1);
            }
        });


        super.initHeaderView(holder);
    }

    @Override
    public void initCustomView(BaseViewHolder holder, List<CommentBean.ItemsBean> data, int position) {
        ImageView user_head_iv = holder.findViewById(R.id.user_head_iv);
        TextView nike_name_tv = holder.findViewById(R.id.nike_name_tv);
        TextView time_tv = holder.findViewById(R.id.time_tv);
        RatingBar com_rat = holder.findViewById(R.id.com_rat);
        ExpandableTextView expandableTextView = holder.findViewById(R.id.content_tx);
        CommentBean.ItemsBean itemsBean = data.get(position);
        String avatar=  itemsBean.getProfile().getAvatar();
        String nickName = itemsBean.getProfile().getNickname();
        int create_time = itemsBean.getCreate_time();
        int score = itemsBean.getScore();
        String content = itemsBean.getContent();
        int is_anon = itemsBean.getIs_anon();
        GlideUtils.loadHeaderOfTeacher(context,avatar,user_head_iv);

        com_rat.setRating(score);
        expandableTextView.setText(content);
        time_tv.setText(DateUtil.getDayBef(create_time));
        comment_num_tv.setText("学员评论("+data.size()+")");
        if(is_anon==1){
            nike_name_tv.setText("匿名");
        }else {
            nike_name_tv.setText(nickName);
        }

        super.initCustomView(holder, data, position);
    }
}
