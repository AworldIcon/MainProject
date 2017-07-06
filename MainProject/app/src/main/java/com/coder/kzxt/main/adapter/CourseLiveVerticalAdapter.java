package com.coder.kzxt.main.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.image.ImageLoad;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.beans.LiveBean;
import com.coder.kzxt.course.activity.LiveSynopsisActivity;
import com.coder.kzxt.main.beans.ConfigResult;
import com.coder.kzxt.main.beans.CourseResult;
import com.coder.kzxt.main.beans.MainModelBean;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.video.activity.VideoViewActivity;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Administrator on 2016/9/13.
 */
public class CourseLiveVerticalAdapter extends HolderBaseAdapter<MainModelBean.ItemsBean.ListBean> {

    private Activity mContext;
   // private PublicUtils pu;
    private MainModelBean.ItemsBean homeConfigBean;


    public CourseLiveVerticalAdapter(Activity mContext, MainModelBean.ItemsBean showType, List<MainModelBean.ItemsBean.ListBean> data) {
        this.mContext = mContext;
        this.data = data;
        this.homeConfigBean = showType;
       // pu = new PublicUtils(mContext);

    }

    @Override
    public ViewHolder getViewHolder(View convertView, ViewGroup parent, final int position) {
        ViewHolder mViewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_live_course_vertical);
        ImageView courseImg = (ImageView) mViewHolder.findViewById(R.id.courseImg);
        ImageView coursePriceStatus = (ImageView) mViewHolder.findViewById(R.id.coursePriceStatus);
        LinearLayout liveTitleLy = (LinearLayout) mViewHolder.findViewById(R.id.liveTitleLy);
        TextView liveTitle = (TextView) mViewHolder.findViewById(R.id.liveTitle);
        TextView courseName = (TextView) mViewHolder.findViewById(R.id.courseName);
        LinearLayout bottomLy = mViewHolder.findViewById(R.id.bottomLy);
        ProgressBar progressBar = mViewHolder.findViewById(R.id.progress);
        TextView courseTeacher = (TextView) mViewHolder.findViewById(R.id.courseTeacher);
        TextView personNum = (TextView) mViewHolder.findViewById(R.id.personNum);

        final MainModelBean.ItemsBean.ListBean courseLiveBean = this.data.get(position);
        ImageLoad.loadImage(mContext,courseLiveBean.getHome_pic(),R.drawable.my_course_def,R.drawable.my_course_def,0, RoundedCornersTransformation.CornerType.ALL,courseImg);
        courseName.setText(courseLiveBean.getLive_title());
        courseName.setVisibility(View.GONE);
        liveTitleLy.setVisibility(View.VISIBLE);
        liveTitleLy.getBackground().setAlpha(100);
        if (courseLiveBean.getLive_status().equals("1")) {
            progressBar.setVisibility(View.VISIBLE);
            if (android.os.Build.VERSION.SDK_INT > 22) {//android 6.0替换clip的加载动画
                final Drawable drawable =  mContext.getResources().getDrawable(R.drawable.liveing_anim_60);
                progressBar.setIndeterminateDrawable(drawable);
            }
            liveTitle.setText(mContext.getResources().getString(R.string.isliving) + courseLiveBean.getLive_title());
            personNum.setVisibility(View.VISIBLE);
        } else if (courseLiveBean.getLive_status().equals("0")){
            progressBar.setVisibility(View.GONE);
            liveTitle.setText(mContext.getResources().getString(R.string.soonLiving)+ courseLiveBean.getLive_title());
            personNum.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            liveTitle.setText("回放: "+courseLiveBean.getLive_title());
            personNum.setVisibility(View.GONE);
        }

        if (MainLiveCourseAdapter.showTeacherAndNumber.contains(homeConfigBean.getModule_style())) {
            bottomLy.setVisibility(View.VISIBLE);
            courseTeacher.setText(mContext.getResources().getString(R.string.lecturer)+courseLiveBean.getSpeaker());
            personNum.setText(courseLiveBean.getLearn_num());
        } else {
            bottomLy.setVisibility(View.GONE);
        }

        mViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveBean liveBean=new LiveBean();
                liveBean.setId(courseLiveBean.getId()+"");
                LiveSynopsisActivity.gotoActivity(mContext,liveBean);

            }
        });

        return mViewHolder;
    }
}
