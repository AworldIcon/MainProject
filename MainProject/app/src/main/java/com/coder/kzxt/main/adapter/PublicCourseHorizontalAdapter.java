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
 *
 * 公开课的横向展示
 */
public class PublicCourseHorizontalAdapter extends HolderBaseAdapter<MainModelBean.ItemsBean.ListBean> {

    private Activity mContext;
   // private PublicUtils pu;
    private ConfigResult.DataBean.HomePageConfigBean.ModulesBean homeConfigBean;

    public PublicCourseHorizontalAdapter(Activity mContext, List<MainModelBean.ItemsBean.ListBean> data) {
        this.mContext = mContext;
        this.data = data;
    //    this.homeConfigBean = showType;
       // pu = new PublicUtils(mContext);

    }

    @Override
    public ViewHolder getViewHolder(View convertView, ViewGroup parent, final int position) {
        ViewHolder mViewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_public_course_horizontal);
        ImageView courseImg = (ImageView) mViewHolder.findViewById(R.id.courseImg);
        ImageView coursePriceStatus = (ImageView) mViewHolder.findViewById(R.id.coursePriceStatus);
        LinearLayout liveTitleLy = (LinearLayout) mViewHolder.findViewById(R.id.liveTitleLy);
        TextView liveTitle = (TextView) mViewHolder.findViewById(R.id.liveTitle);
        TextView courseName = (TextView) mViewHolder.findViewById(R.id.courseName);
        ProgressBar progressBar = mViewHolder.findViewById(R.id.progress);
        TextView comeFrom = (TextView) mViewHolder.findViewById(R.id.comeFrom);
        TextView studyTime = (TextView) mViewHolder.findViewById(R.id.studyTime);

        final MainModelBean.ItemsBean.ListBean courseLiveBean = this.data.get(position);
        ImageLoad.loadImage(mContext,courseLiveBean.getHome_pic(),R.drawable.my_course_def,R.drawable.my_course_def,0, RoundedCornersTransformation.CornerType.ALL,courseImg);

        courseName.setText(courseLiveBean.getTitle());
        comeFrom.setText(mContext.getResources().getString(R.string.come_from) + courseLiveBean.getSource());
        studyTime.setText(courseLiveBean.getLesson_num()+mContext.getResources().getString(R.string.course_hours));
        liveTitleLy.getBackground().setAlpha(100);
        mViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VideoViewActivity.class);
                intent.putExtra("flag", Constants.ONLINE);
                intent.putExtra("treeid", courseLiveBean.getId()+"");
                intent.putExtra("tree_name", courseLiveBean.getTitle());
                intent.putExtra("pic", courseLiveBean.getMiddle_pic());
                intent.putExtra(Constants.IS_CENTER, "0");
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
//
//                } else {
//
//                    Intent intent = new Intent(mContext, VideoViewActivity.class);
//                    intent.putExtra("flag", "online");
//                    intent.putExtra("treeid", courseLiveBean.getCourseId());
//                    intent.putExtra("tree_name", courseLiveBean.getCourseTitle());
//                    intent.putExtra("pic", courseLiveBean.getCoverImage());
//                    intent.putExtra(Constants.IS_CENTER, courseLiveBean.getPublicCourse());
//                    mContext.startActivity(intent);
//                }
            }
        });

        return mViewHolder;
    }
}
