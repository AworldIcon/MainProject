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
import android.widget.Toast;
import com.app.image.ImageLoad;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.beans.LiveBean;
import com.coder.kzxt.course.activity.LiveSynopsisActivity;
import com.coder.kzxt.main.beans.ConfigResult;
import com.coder.kzxt.main.beans.CourseResult;
import com.coder.kzxt.main.beans.MainModelBean;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.EToast;
import com.coder.kzxt.video.activity.VideoViewActivity;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Administrator on 2016/9/13.
 */
public class CourseLiveVerticalThreeAdapter extends HolderBaseAdapter<MainModelBean.ItemsBean.ListBean> {

    private Activity mContext;
   // private PublicUtils pu;
    private MainModelBean.ItemsBean homeConfigBean;
    private List<List<MainModelBean.ItemsBean.ListBean>> dataList;
    private Boolean isShowTeacherAndNum;


    public CourseLiveVerticalThreeAdapter(Activity mContext, MainModelBean.ItemsBean showType, List<MainModelBean.ItemsBean.ListBean> data) {
        this.mContext = mContext;
        this.data = data;
        this.homeConfigBean = showType;
     //   pu = new PublicUtils(mContext);
        dataList = new ArrayList<>();
        initData();
    }

    private void initData() {
        isShowTeacherAndNum = MainLiveCourseAdapter.showTeacherAndNumber.contains(homeConfigBean.getModule_style());

//        int count = this.data.size() / 3;
//        if (this.data.size() % 3 != 0) {
//            count = count + 1;
//        }

        int count = 1;
        for (int i = 0; i < count; i++) {
            List<MainModelBean.ItemsBean.ListBean> courseLiveBeans = new ArrayList<>();
            courseLiveBeans.add(data.get(i * 3 + 0));
            if (i * 3 + 1 < this.data.size()) {
                courseLiveBeans.add(data.get(i * 3 + 1));
            }
            if (i * 3 + 2 < this.data.size()) {
                courseLiveBeans.add(data.get(i * 3 + 2));
            }

            dataList.add(courseLiveBeans);
        }
        setCount(count);
    }


    @Override
    public ViewHolder getViewHolder(View convertView, ViewGroup parent, final int position) {
        ViewHolder mViewHolder;
        if (isShowTeacherAndNum) {
            mViewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_three_course_vertical);
        } else {
            mViewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_three_course_vertical2);

        }
        LinearLayout layout1 = (LinearLayout) mViewHolder.findViewById(R.id.layout1);
        ImageView courseImg1 = (ImageView) mViewHolder.findViewById(R.id.courseImg1);
        ImageView coursePriceStatus1 = (ImageView) mViewHolder.findViewById(R.id.coursePriceStatus1);
        LinearLayout liveTitleLy1 = (LinearLayout) mViewHolder.findViewById(R.id.liveTitleLy1);
        TextView liveTitle1 = (TextView) mViewHolder.findViewById(R.id.liveTitle1);
        TextView courseName1 = (TextView) mViewHolder.findViewById(R.id.courseName1);
        ProgressBar progressBar1 = mViewHolder.findViewById(R.id.progress1);
        LinearLayout bottomLy1 = mViewHolder.findViewById(R.id.bottomLy1);
        TextView courseTeacher1 = (TextView) mViewHolder.findViewById(R.id.courseTeacher1);
        TextView personNum1 = (TextView) mViewHolder.findViewById(R.id.personNum1);
        LinearLayout layout2 = (LinearLayout) mViewHolder.findViewById(R.id.layout2);
        ImageView courseImg2 = (ImageView) mViewHolder.findViewById(R.id.courseImg2);
        ImageView coursePriceStatus2 = (ImageView) mViewHolder.findViewById(R.id.coursePriceStatus2);
        LinearLayout liveTitleLy2 = (LinearLayout) mViewHolder.findViewById(R.id.liveTitleLy2);
        TextView liveTitle2 = (TextView) mViewHolder.findViewById(R.id.liveTitle2);
        TextView courseName2 = (TextView) mViewHolder.findViewById(R.id.courseName2);
        ProgressBar progressBar2 = mViewHolder.findViewById(R.id.progress2);
        LinearLayout bottomLy2 = mViewHolder.findViewById(R.id.bottomLy2);
        TextView courseTeacher2 = (TextView) mViewHolder.findViewById(R.id.courseTeacher2);
        TextView personNum2 = (TextView) mViewHolder.findViewById(R.id.personNum2);
        LinearLayout layout3 = (LinearLayout) mViewHolder.findViewById(R.id.layout3);
        ImageView courseImg3 = (ImageView) mViewHolder.findViewById(R.id.courseImg3);
        ImageView coursePriceStatus3 = (ImageView) mViewHolder.findViewById(R.id.coursePriceStatus3);
        LinearLayout liveTitleLy3 = (LinearLayout) mViewHolder.findViewById(R.id.liveTitleLy3);
        TextView liveTitle3 = (TextView) mViewHolder.findViewById(R.id.liveTitle3);
        TextView courseName3 = (TextView) mViewHolder.findViewById(R.id.courseName3);
        ProgressBar progressBar3 = mViewHolder.findViewById(R.id.progress3);
        LinearLayout bottomLy3 = mViewHolder.findViewById(R.id.bottomLy3);
        TextView courseTeacher3 = (TextView) mViewHolder.findViewById(R.id.courseTeacher3);
        TextView personNum3 = (TextView) mViewHolder.findViewById(R.id.personNum3);

        List<MainModelBean.ItemsBean.ListBean> courseLiveBeans = dataList.get(position);
        if (courseLiveBeans.size() > 0) {
            layout1.setVisibility(View.VISIBLE);
            final MainModelBean.ItemsBean.ListBean courseLiveBean = courseLiveBeans.get(0);

            ImageLoad.loadImage(mContext,courseLiveBean.getHome_pic(),R.drawable.my_course_def_vertical,R.drawable.my_course_def_vertical,0, RoundedCornersTransformation.CornerType.ALL,courseImg1);

            courseName1.setText(courseLiveBean.getLive_title());
            courseName1.setVisibility(View.GONE);
            liveTitleLy1.setVisibility(View.VISIBLE);
            liveTitleLy1.getBackground().setAlpha(100);
            if (courseLiveBean.getLive_status().equals("1")) {
                progressBar1.setVisibility(View.VISIBLE);
                if (android.os.Build.VERSION.SDK_INT > 22) {//android 6.0替换clip的加载动画
                    final Drawable drawable =  mContext.getResources().getDrawable(R.drawable.liveing_anim_60);
                    progressBar1.setIndeterminateDrawable(drawable);
                }
                liveTitle1.setText(mContext.getResources().getString(R.string.isliving) + courseLiveBean.getLive_title());
                personNum1.setVisibility(View.VISIBLE);
            } else if (courseLiveBean.getLive_status().equals("0")){
                progressBar1.setVisibility(View.GONE);
                liveTitle1.setText(mContext.getResources().getString(R.string.soonLiving) + courseLiveBean.getLive_title());
                personNum1.setVisibility(View.GONE);
            }else {
                progressBar1.setVisibility(View.GONE);
                liveTitle1.setText("回放: "+courseLiveBean.getLive_title());
                personNum1.setVisibility(View.GONE);
            }
            if (isShowTeacherAndNum) {
                bottomLy1.setVisibility(View.VISIBLE);
                courseTeacher1.setText(mContext.getResources().getString(R.string.lecturer) + courseLiveBean.getSpeaker());
                personNum1.setText(courseLiveBean.getLearn_num());
                personNum1.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.live_num_iv), null, null, null);
            } else {
                bottomLy1.setVisibility(View.GONE);
            }

            courseImg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LiveBean liveBean=new LiveBean();
                    liveBean.setId(courseLiveBean.getId()+"");
                    LiveSynopsisActivity.gotoActivity(mContext,liveBean);

                }
            });

        } else

        {
            layout1.setVisibility(View.GONE);
        }

        if (courseLiveBeans.size() > 1)

        {
            layout2.setVisibility(View.VISIBLE);
            final MainModelBean.ItemsBean.ListBean courseLiveBean = this.data.get(1);
            ImageLoad.loadImage(mContext,courseLiveBean.getHome_pic(),R.drawable.my_course_def,R.drawable.my_course_def,0, RoundedCornersTransformation.CornerType.ALL,courseImg2);

            courseName2.setText(courseLiveBean.getLive_title());
            courseName2.setVisibility(View.GONE);
            liveTitleLy2.setVisibility(View.VISIBLE);
            liveTitleLy2.getBackground().setAlpha(100);
            if (courseLiveBean.getLive_status().equals("1")) {
                progressBar2.setVisibility(View.VISIBLE);
                if (android.os.Build.VERSION.SDK_INT > 22) {//android 6.0替换clip的加载动画
                    final Drawable drawable =  mContext.getResources().getDrawable(R.drawable.liveing_anim_60);
                    progressBar2.setIndeterminateDrawable(drawable);
                }
                liveTitle2.setText(mContext.getResources().getString(R.string.isliving) + courseLiveBean.getLive_title());
                personNum2.setVisibility(View.VISIBLE);
            } else if (courseLiveBean.getLive_status().equals("0")) {
                progressBar2.setVisibility(View.GONE);
                liveTitle2.setText(mContext.getResources().getString(R.string.soonLiving) + courseLiveBean.getLive_title());
                personNum2.setVisibility(View.GONE);
            }else {
                progressBar2.setVisibility(View.GONE);
                liveTitle2.setText("回放: "+courseLiveBean.getLive_title());
                personNum2.setVisibility(View.GONE);
            }

            if (isShowTeacherAndNum) {
                bottomLy2.setVisibility(View.VISIBLE);
                courseTeacher2.setText(mContext.getResources().getString(R.string.lecturer) + courseLiveBean.getSpeaker());
                personNum2.setText(courseLiveBean.getLearn_num());
                personNum2.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.live_num_iv), null, null, null);
            } else {
                bottomLy2.setVisibility(View.GONE);
            }

            courseImg2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LiveBean liveBean=new LiveBean();
                    liveBean.setId(courseLiveBean.getId()+"");
                    LiveSynopsisActivity.gotoActivity(mContext,liveBean);
                }
            });

        } else

        {
            layout2.setVisibility(View.GONE);
        }

        if (courseLiveBeans.size() > 2)

        {
            layout3.setVisibility(View.VISIBLE);
            final MainModelBean.ItemsBean.ListBean courseLiveBean = this.data.get(2);
            ImageLoad.loadImage(mContext,courseLiveBean.getHome_pic(),R.drawable.my_course_def,R.drawable.my_course_def,0, RoundedCornersTransformation.CornerType.ALL,courseImg3);
            courseName3.setText(courseLiveBean.getLive_title());
            //新版不显示此字段
            courseName3.setVisibility(View.GONE);

            liveTitleLy3.setVisibility(View.VISIBLE);
            liveTitleLy3.getBackground().setAlpha(100);
            if (courseLiveBean.getLive_status().equals("1")) {
                progressBar3.setVisibility(View.VISIBLE);
                if (android.os.Build.VERSION.SDK_INT > 22) {//android 6.0替换clip的加载动画
                    final Drawable drawable =  mContext.getResources().getDrawable(R.drawable.liveing_anim_60);
                    progressBar3.setIndeterminateDrawable(drawable);
                }
                liveTitle3.setText(mContext.getResources().getString(R.string.isliving) + courseLiveBean.getLive_title());
                personNum3.setVisibility(View.VISIBLE);
            } else  if (courseLiveBean.getLive_status().equals("0")) {
                progressBar3.setVisibility(View.GONE);
                liveTitle3.setText(mContext.getResources().getString(R.string.soonLiving) + courseLiveBean.getLive_title());
                personNum3.setVisibility(View.GONE);
            }else {
                progressBar3.setVisibility(View.GONE);
                liveTitle3.setText("回放: "+courseLiveBean.getLive_title());
                personNum3.setVisibility(View.GONE);
            }

            if (isShowTeacherAndNum) {
                bottomLy3.setVisibility(View.VISIBLE);
                courseTeacher3.setText(mContext.getResources().getString(R.string.lecturer) + courseLiveBean.getSpeaker());
                personNum3.setText(courseLiveBean.getLearn_num());
                personNum3.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.live_num_iv), null, null, null);
            } else {
                bottomLy3.setVisibility(View.GONE);
            }

            courseImg3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LiveBean liveBean=new LiveBean();
                    liveBean.setId(courseLiveBean.getId()+"");
                    LiveSynopsisActivity.gotoActivity(mContext,liveBean);
                }
            });

        } else

        {
            layout3.setVisibility(View.GONE);
        }


        return mViewHolder;
    }
}
