package com.coder.kzxt.course.delegate;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.course.beans.LearnBean;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.GlideUtils;

import java.text.NumberFormat;
import java.util.List;

/**
 * 课程列表的delegate
 * Created by wangtingshun on 2017/4/6.
 */
public class LearnListDelegate extends PullRefreshDelegate<LearnBean.ItemsBean> {

    private Context mContext;
    private String categoryId;

    public  LearnListDelegate(Context context, String id) {
        super(R.layout.item_course);
        this.mContext = context;
        this.categoryId = id;
    }


    @Override
    public void initCustomView(BaseViewHolder itemView, List<LearnBean.ItemsBean> data, int position) {
        TextView courseName = (TextView) itemView.findViewById(R.id.course_name_text);
        ImageView courseImage = (ImageView) itemView.findViewById(R.id.course_img);
        ImageView freeOrVipImage = (ImageView) itemView.findViewById(R.id.free_or_vip);
        TextView studyTime = (TextView) itemView.findViewById(R.id.study_time);
        LinearLayout course_ly = (LinearLayout) itemView.findViewById(R.id.course_ly);
        LinearLayout studyLayout = itemView.findViewById(R.id.ll_study_time);
        TextView tv_date = itemView.findViewById(R.id.tv_study_date);
        RelativeLayout progressLayout = itemView.findViewById(R.id.rl_progress); //学习进度
        TextView tv_progress = itemView.findViewById(R.id.tv_progress);
        ProgressBar progressBar = itemView.findViewById(R.id.progressBar);
        progressLayout.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(categoryId)) {
            studyLayout.setVisibility(View.GONE);
            studyTime.setVisibility(View.VISIBLE);
        } else {
            studyLayout.setVisibility(View.VISIBLE);
            studyTime.setVisibility(View.GONE);
        }

        LearnBean.ItemsBean itemsBean = data.get(position);
        int lesson_num = itemsBean.getLesson_num(); //课程总时长
        int learn_num = itemsBean.getLearn_num();  //课程学习时长

        if(lesson_num == 0){
            progressBar.setProgress(0);
            tv_progress.setText("已学完"+" 0%");
        } else {
            NumberFormat nf = NumberFormat.getPercentInstance();
            //设置百分数精确度2即保留两位小数
            nf.setMinimumFractionDigits(0);
            double percent = (double) learn_num / lesson_num;
            String format = nf.format(percent);
            String num = format.substring(0, format.length() - 1);
            int i = Integer.parseInt(num);
            if (i > 100) {
                progressBar.setProgress(100);
                tv_progress.setText("已学完" + 100 + "%");
            } else {
                progressBar.setProgress(i);
                tv_progress.setText("已学完" + format);
            }
        }

        if (IsEmpty(itemsBean.getPrice())) {
            freeOrVipImage.setBackgroundResource(R.drawable.live_free_iv);
        } else {
            freeOrVipImage.setBackgroundResource(R.drawable.live_vip_iv);
        }
        courseName.setText(itemsBean.getTitle());
        GlideUtils.loadCourseImg(mContext, itemsBean.getMiddle_pic(), courseImage);
        LearnBean.ItemsBean.LearnCourseBean lastLearn = itemsBean.getLast_learn();
        if(lastLearn != null){
            tv_date.setText(DateUtil.getYearMonthAndDay(Long.parseLong(lastLearn.getUpdate_time())));
        } else {
            tv_date.setText(DateUtil.getYearMonthAndDay(itemsBean.getJoin_time()));
        }

    }

    public boolean IsEmpty(String string) {
        if (TextUtils.isEmpty(string) || string.equals("0") || string.equals("0.0") || string.equals("0.00")) {
            return true;
        }
        return false;
    }

}
