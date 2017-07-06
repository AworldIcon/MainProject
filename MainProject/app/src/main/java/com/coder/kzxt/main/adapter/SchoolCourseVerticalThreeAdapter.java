package com.coder.kzxt.main.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.image.ImageLoad;
import com.app.utils.L;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.main.beans.MainModelBean;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.video.activity.VideoViewActivity;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Administrator on 2016/9/13.
 */
public class SchoolCourseVerticalThreeAdapter extends HolderBaseAdapter<MainModelBean.ItemsBean.ListBean> {

    private Activity mContext;
   // private PublicUtils pu;
    private MainModelBean.ItemsBean homeConfigBean;
    private List<List<MainModelBean.ItemsBean.ListBean>> dataList;


    public SchoolCourseVerticalThreeAdapter(Activity mContext, MainModelBean.ItemsBean showType, List<MainModelBean.ItemsBean.ListBean> data) {
        this.mContext = mContext;
        this.data = data;
        this.homeConfigBean = showType;
        //pu = new PublicUtils(mContext);
        dataList = new ArrayList<>();
        initData();
    }

    private void initData() {

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
        L.d("count = "+dataList.size());
        setCount(count);
    }


    @Override
    public ViewHolder getViewHolder(View convertView, ViewGroup parent, int position) {
        ViewHolder mViewHolder;
        if (homeConfigBean.getShowTeacherAndNum()) {
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
            courseName1.setText(courseLiveBean.getTitle());
            liveTitleLy1.getBackground().setAlpha(100);
            if (homeConfigBean.getShowTeacherAndNum()) {
                bottomLy1.setVisibility(View.VISIBLE);
                courseTeacher1.setCompoundDrawables(null, null, null, null);
                if (homeConfigBean.getModuleType() == 1) {
                    personNum1.setVisibility(View.GONE);
                    courseTeacher1.setText(mContext.getResources().getString(R.string.come_from) + courseLiveBean.getSource());
                } else if (homeConfigBean.getModuleType() == 2) {
                    personNum1.setVisibility(View.GONE);
                    courseTeacher1.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.live_num_iv), null, null, null);
                    courseTeacher1.setCompoundDrawablePadding(6);
                    courseTeacher1.setText(courseLiveBean.getStudent_num()+mContext.getResources().getString(R.string.num_of_study));
                } else if (homeConfigBean.getModuleType() == 3) {
                    bottomLy1.setVisibility(View.GONE);
                    //personNum1.setVisibility(View.VISIBLE);
                    //courseTeacher1.setText(mContext.getResources().getString(R.string.tea_class) + courseLiveBean.getClassNum());
                } else {
                    personNum1.setVisibility(View.GONE);
                    courseTeacher1.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.class_time_small), null, null, null);
                    courseTeacher1.setCompoundDrawablePadding(6);
                    courseTeacher1.setText(courseLiveBean.getLesson_num() + mContext.getResources().getString(R.string.course_hours));
                }
            } else {
                bottomLy1.setVisibility(View.GONE);
            }


            courseImg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, VideoViewActivity.class);
                    intent.putExtra("flag", Constants.ONLINE);
                    intent.putExtra("treeid", courseLiveBean.getId()+"");
                    intent.putExtra("tree_name", courseLiveBean.getTitle());
                    intent.putExtra("pic", courseLiveBean.getMiddle_pic());
                    intent.putExtra(Constants.IS_CENTER, "0");
                    if(homeConfigBean.getModuleType() == 3){
                        intent.putExtra("isTeacher", "1");
                    }
                    mContext.startActivity(intent);
//                    if (MainTeacherCourseAdapter.types.contains(homeConfigBean.getStyleKey())) {
//                        MyCourseBean bean = new MyCourseBean();
//                        bean.setCourseId(courseLiveBean.getCourseId());
//                        bean.setCourseName(courseLiveBean.getCourseTitle());
//                        bean.setClassNum(courseLiveBean.getClassNum());
//                        bean.setPublicCourse(courseLiveBean.getPublicCourse());
//                        bean.setPic(courseLiveBean.getCoverImage());
//                        bean.setStatus("published");
//                        Intent intent = new Intent(mContext, CourseDetailTeacherActivity.class);
//                        intent.putExtra("Bean", bean);
//                        mContext.startActivity(intent);
//                    } else {
//                        Intent intent = new Intent(mContext, VideoViewActivity.class);
//                        intent.putExtra("flag", "online");
//                        intent.putExtra("treeid", courseLiveBean.getCourseId());
//                        intent.putExtra("tree_name", courseLiveBean.getCourseTitle());
//                        intent.putExtra("pic", courseLiveBean.getCoverImage());
//                        intent.putExtra(Constants.IS_CENTER, courseLiveBean.getPublicCourse());
//                        mContext.startActivity(intent);
//                    }
                }
            });

            bottomLy1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (MainTeacherCourseAdapter.types.contains(homeConfigBean.getStyleKey())) {
//                        TeacherCourseDialog courseDialog = new TeacherCourseDialog(mContext, courseLiveBean);
//                        courseDialog.show();
//                    }
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

            courseName2.setText(courseLiveBean.getTitle());
            liveTitleLy2.getBackground().setAlpha(100);
            if (homeConfigBean.getShowTeacherAndNum()) {
                bottomLy2.setVisibility(View.VISIBLE);
                courseTeacher2.setCompoundDrawables(null, null, null, null);
                if (homeConfigBean.getModuleType() == 1) {
                    personNum2.setVisibility(View.GONE);
                    courseTeacher2.setText(mContext.getResources().getString(R.string.come_from) + courseLiveBean.getSource());
                } else if (homeConfigBean.getModuleType() == 2) {
                    personNum2.setVisibility(View.GONE);
                    courseTeacher2.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.live_num_iv), null, null, null);
                    courseTeacher2.setCompoundDrawablePadding(6);
                    courseTeacher2.setText( courseLiveBean.getStudent_num()+mContext.getResources().getString(R.string.num_of_study) );
                } else if (homeConfigBean.getModuleType() == 3) {
                    bottomLy1.setVisibility(View.GONE);
                    //personNum2.setVisibility(View.VISIBLE);
                   // courseTeacher2.setText(mContext.getResources().getString(R.string.tea_class) + courseLiveBean.getClassNum());
                } else {
                    personNum2.setVisibility(View.GONE);
                    courseTeacher2.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.class_time_small), null, null, null);
                    courseTeacher2.setCompoundDrawablePadding(6);
                    courseTeacher2.setText(courseLiveBean.getLesson_num() + mContext.getResources().getString(R.string.course_hours));
                }
            } else {
                bottomLy2.setVisibility(View.GONE);
            }

            courseImg2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, VideoViewActivity.class);
                    intent.putExtra("flag", Constants.ONLINE);
                    intent.putExtra("treeid", courseLiveBean.getId()+"");
                    intent.putExtra("tree_name", courseLiveBean.getTitle());
                    intent.putExtra("pic", courseLiveBean.getMiddle_pic());
                    intent.putExtra(Constants.IS_CENTER, "0");
                    if(homeConfigBean.getModuleType() == 3){
                        intent.putExtra("isTeacher", "1");
                    }
                    mContext.startActivity(intent);
//                    if (MainTeacherCourseAdapter.types.contains(homeConfigBean.getStyleKey())) {
//                        MyCourseBean bean = new MyCourseBean();
//                        bean.setCourseId(courseLiveBean.getCourseId());
//                        bean.setCourseName(courseLiveBean.getCourseTitle());
//                        bean.setClassNum(courseLiveBean.getClassNum());
//                        bean.setPublicCourse(courseLiveBean.getPublicCourse());
//                        bean.setPic(courseLiveBean.getCoverImage());
//                        bean.setStatus("published");
//                        Intent intent = new Intent(mContext, CourseDetailTeacherActivity.class);
//                        intent.putExtra("Bean", bean);
//                        mContext.startActivity(intent);
//                    } else {
//                        Intent intent = new Intent(mContext, VideoViewActivity.class);
//                        intent.putExtra("flag", "online");
//                        intent.putExtra("treeid", courseLiveBean.getCourseId());
//                        intent.putExtra("tree_name", courseLiveBean.getCourseTitle());
//                        intent.putExtra("pic", courseLiveBean.getCoverImage());
//                        intent.putExtra(Constants.IS_CENTER, courseLiveBean.getPublicCourse());
//                        mContext.startActivity(intent);
//                    }
                }
            });

            bottomLy2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (MainTeacherCourseAdapter.types.contains(homeConfigBean.getStyleKey())) {
//                        TeacherCourseDialog courseDialog = new TeacherCourseDialog(mContext, courseLiveBean);
//                        courseDialog.show();
//                    }
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
            courseName3.setText(courseLiveBean.getTitle());

            if (homeConfigBean.getShowTeacherAndNum()) {
                bottomLy3.setVisibility(View.VISIBLE);
                personNum3.setVisibility(View.GONE);
                if (homeConfigBean.getModuleType() == 1) {
                    courseTeacher3.setCompoundDrawables(null, null, null, null);
                    courseTeacher3.setText(mContext.getResources().getString(R.string.come_from) + courseLiveBean.getSource());
                } else if (homeConfigBean.getModuleType() == 2) {
                    courseTeacher3.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.live_num_iv), null, null, null);
                    courseTeacher3.setCompoundDrawablePadding(6);
                    courseTeacher3.setText( courseLiveBean.getStudent_num()+mContext.getResources().getString(R.string.num_of_study) );
                } else if (homeConfigBean.getModuleType() == 3) {
                    bottomLy1.setVisibility(View.GONE);
                    //personNum3.setVisibility(View.VISIBLE);
                   // courseTeacher3.setText(mContext.getResources().getString(R.string.tea_class) + courseLiveBean.getClassNum());
                } else {
                    courseTeacher3.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.class_time_small), null, null, null);
                    courseTeacher3.setCompoundDrawablePadding(6);
                    courseTeacher3.setText(courseLiveBean.getLesson_num() + mContext.getResources().getString(R.string.course_hours));
                }
            } else {
                bottomLy3.setVisibility(View.GONE);
            }

            courseImg3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, VideoViewActivity.class);
                    intent.putExtra("flag", Constants.ONLINE);
                    intent.putExtra("treeid", courseLiveBean.getId()+"");
                    intent.putExtra("tree_name", courseLiveBean.getTitle());
                    intent.putExtra("pic", courseLiveBean.getMiddle_pic());
                    intent.putExtra(Constants.IS_CENTER, "0");
                    if(homeConfigBean.getModuleType() == 3){
                        intent.putExtra("isTeacher", "1");//老师身份跳转课程详情
                    }
                    mContext.startActivity(intent);
//                    if (MainTeacherCourseAdapter.types.contains(homeConfigBean.getStyleKey())) {
//                        MyCourseBean bean = new MyCourseBean();
//                        bean.setCourseId(courseLiveBean.getCourseId());
//                        bean.setCourseName(courseLiveBean.getCourseTitle());
//                        bean.setClassNum(courseLiveBean.getClassNum());
//                        bean.setPublicCourse(courseLiveBean.getPublicCourse());
//                        bean.setPic(courseLiveBean.getCoverImage());
//                        bean.setStatus("published");
//                        Intent intent = new Intent(mContext, CourseDetailTeacherActivity.class);
//                        intent.putExtra("Bean", bean);
//                        mContext.startActivity(intent);
//                    } else {
//                        Intent intent = new Intent(mContext, VideoViewActivity.class);
//                        intent.putExtra("flag", "online");
//                        intent.putExtra("treeid", courseLiveBean.getCourseId());
//                        intent.putExtra("tree_name", courseLiveBean.getCourseTitle());
//                        intent.putExtra("pic", courseLiveBean.getCoverImage());
//                        intent.putExtra(Constants.IS_CENTER, courseLiveBean.getPublicCourse());
//                        mContext.startActivity(intent);
//                    }
                }
            });
            bottomLy3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (MainTeacherCourseAdapter.types.contains(homeConfigBean.getStyleKey())) {
//                        TeacherCourseDialog courseDialog = new TeacherCourseDialog(mContext, courseLiveBean);
//                        courseDialog.show();
//                    }
                }
            });

        } else

        {
            layout3.setVisibility(View.GONE);
        }


        return mViewHolder;
    }
}
