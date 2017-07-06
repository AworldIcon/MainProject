package com.coder.kzxt.main.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.image.ImageLoad;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.dialog.util.TeacherCourseDialog;
import com.coder.kzxt.main.beans.MainModelBean;
import com.coder.kzxt.main.beans.MyCourseBean;
import com.coder.kzxt.main.beans.ConfigResult;
import com.coder.kzxt.main.beans.CourseResult;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.EToast;
import com.coder.kzxt.video.activity.VideoViewActivity;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Administrator on 2016/9/13.
 */
public class SchoolCourseVerticalAdapter extends HolderBaseAdapter<MainModelBean.ItemsBean.ListBean> {

    private Activity mContext;
   // private PublicUtils pu;
    private MainModelBean.ItemsBean homeConfigBean;

    public SchoolCourseVerticalAdapter(Activity mContext, MainModelBean.ItemsBean showType, List<MainModelBean.ItemsBean.ListBean> data) {
        this.mContext = mContext;
        this.data = data;
        this.homeConfigBean = showType;
       // pu = new PublicUtils(mContext);
    }

    @Override
    public ViewHolder getViewHolder(View convertView, ViewGroup parent, final int position) {
        ViewHolder mViewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_course_vertical);
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

        courseName.setText(courseLiveBean.getTitle());
        liveTitleLy.getBackground().setAlpha(100);
        if (homeConfigBean.getShowTeacherAndNum()) {
            bottomLy.setVisibility(View.VISIBLE);
            courseTeacher.setCompoundDrawables(null, null, null, null);
            if (homeConfigBean.getModuleType() == 1) {
                personNum.setVisibility(View.GONE);
                courseTeacher.setText(mContext.getResources().getString(R.string.come_from) + courseLiveBean.getSource());
            } else if (homeConfigBean.getModuleType() == 2) {
                personNum.setVisibility(View.GONE);
                courseTeacher.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.live_num_iv), null, null, null);
                courseTeacher.setCompoundDrawablePadding(6);
                courseTeacher.setText(courseLiveBean.getStudent_num() + mContext.getResources().getString(R.string.num_of_study));
            } else if (homeConfigBean.getModuleType() == 3) {
                bottomLy.setVisibility(View.GONE);
                //personNum.setVisibility(View.VISIBLE);
                //courseTeacher.setText(mContext.getResources().getString(R.string.tea_class) + courseLiveBean.getClassNum());
            } else {
                personNum.setVisibility(View.GONE);
                courseTeacher.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.class_time_small), null, null, null);
                courseTeacher.setCompoundDrawablePadding(6);
                courseTeacher.setText(courseLiveBean.getLesson_num() + mContext.getResources().getString(R.string.course_hours));
            }
        } else {
            bottomLy.setVisibility(View.GONE);
        }

        mViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
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
//                if (MainTeacherCourseAdapter.types.contains(homeConfigBean.getStyleKey())) {
//                    MyCourseBean bean = new MyCourseBean();
//                    bean.setCourseId(courseLiveBean.getCourseId());
//                    bean.setCourseName(courseLiveBean.getCourseTitle());
//                    bean.setClassNum(courseLiveBean.getClassNum());
//                    bean.setPublicCourse(courseLiveBean.getPublicCourse());
//                    bean.setPic(courseLiveBean.getCoverImage());
//                    bean.setStatus("published");
////                    Intent intent = new Intent(mContext, CourseDetailTeacherActivity.class);
////                    intent.putExtra("Bean", bean);
////                    mContext.startActivity(intent);
//                    EToast.makeText(mContext,"授课"+position, Toast.LENGTH_SHORT).show();
//                } else {
//                    Intent intent = new Intent(mContext, VideoViewActivity.class);
//                    intent.putExtra("flag", "online");
//                    intent.putExtra("treeid", courseLiveBean.getCourseId());
//                    intent.putExtra("tree_name", courseLiveBean.getCourseTitle());
//                    intent.putExtra("pic", courseLiveBean.getCoverImage());
//                    intent.putExtra(Constants.IS_CENTER, courseLiveBean.getPublicCourse());
//                    mContext.startActivity(intent);
//
//                }
            }
        });

        bottomLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainTeacherCourseAdapter.types.contains(homeConfigBean.getModule_style())) {
//                    TeacherCourseDialog courseDialog = new TeacherCourseDialog(mContext, courseLiveBean);
//                    courseDialog.show();
                }
            }
        });
        return mViewHolder;
    }
}
