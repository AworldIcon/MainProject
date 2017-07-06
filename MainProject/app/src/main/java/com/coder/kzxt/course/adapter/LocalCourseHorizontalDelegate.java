package com.coder.kzxt.course.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.image.ImageLoad;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.course.activity.TeacherBriefActivity;
import com.coder.kzxt.main.beans.CourseMoreBean;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.video.activity.VideoViewActivity;
import java.util.List;


public class LocalCourseHorizontalDelegate extends PullRefreshDelegate<CourseMoreBean.ItemsBean>
{
    private Context mContext;
    public String model_key;
    public String model_style;

    public LocalCourseHorizontalDelegate(Context context,String model_key,String model_style)
    {
        super(R.layout.item_local_course_horizontal);
        this.mContext = context;
        this.model_key=model_key;
        this.model_style=model_style;
    }

    @Override
    public void initCustomView(BaseViewHolder mViewHolder, List<CourseMoreBean.ItemsBean> data, int position) {

        LinearLayout course_layout=mViewHolder.findViewById(R.id.course_layout);
        RelativeLayout famous_tea_layout=mViewHolder.findViewById(R.id.famous_tea_layout);
        ImageView courseImg = (ImageView) mViewHolder.findViewById(R.id.courseImg);
        final ImageView tea_face = (ImageView) mViewHolder.findViewById(R.id.tea_face);
        ImageView coursePriceStatus = (ImageView) mViewHolder.findViewById(R.id.coursePriceStatus);
        LinearLayout liveTitleLy = (LinearLayout) mViewHolder.findViewById(R.id.liveTitleLy);
        TextView liveTitle = (TextView) mViewHolder.findViewById(R.id.liveTitle);
        TextView tea_name = (TextView) mViewHolder.findViewById(R.id.tea_name);
        TextView tea_brief = (TextView) mViewHolder.findViewById(R.id.tea_brief);
        TextView courseName = (TextView) mViewHolder.findViewById(R.id.courseName);
        ProgressBar progressBar = mViewHolder.findViewById(R.id.progress);
        TextView studentNumber = (TextView) mViewHolder.findViewById(R.id.studentNumber);
        TextView lessonNum = (TextView) mViewHolder.findViewById(R.id.lessonNum);
        TextView source = (TextView) mViewHolder.findViewById(R.id.source);
        final CourseMoreBean.ItemsBean courseLiveBean = data.get(position);
        if(!model_key.equals("TEACHER")){
            course_layout.setVisibility(View.VISIBLE);
            famous_tea_layout.setVisibility(View.GONE);
            GlideUtils.loadCourseImg(mContext, courseLiveBean.getMiddle_pic(), courseImg);
            courseName.setText(courseLiveBean.getTitle());
            liveTitleLy.getBackground().setAlpha(100);
            lessonNum.setVisibility(View.VISIBLE);
            lessonNum.setText(courseLiveBean.getLesson_num() + mContext.getResources().getString(R.string.course_hours));

            if(model_key.equals("PUBLIC_COURSE")){
                studentNumber.setVisibility(View.GONE);
                source.setVisibility(View.VISIBLE);
                source.setText("来源:"+courseLiveBean.getSource());
            }else if(model_key.equals("SELLER")||model_key.equals("FREE_COURSE")){
                studentNumber.setVisibility(View.VISIBLE);
                studentNumber.setText(courseLiveBean.getStudent_num()+ mContext.getResources().getString(R.string.num_of_study));
                source.setVisibility(View.GONE);
            }else {
                studentNumber.setVisibility(View.GONE);
                source.setVisibility(View.GONE);
            }
            mViewHolder.getConvertView().setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Log.d("zw--",""+model_key);
                    Intent intent = new Intent(mContext, VideoViewActivity.class);
                    intent.putExtra("flag", Constants.ONLINE);
                    intent.putExtra("treeid", courseLiveBean.getId()+"");
                    intent.putExtra("tree_name", courseLiveBean.getTitle());
                    intent.putExtra("pic", courseLiveBean.getMiddle_pic());
                    intent.putExtra(Constants.IS_CENTER, "0");
                    mContext.startActivity(intent);
//                Intent intent = new Intent(mContext, VideoViewActivity.class);
//                intent.putExtra("flag", "online");
//                intent.putExtra("treeid", courseLiveBean.getCourseId());
//                intent.putExtra("tree_name", courseLiveBean.getCourseTitle());
//                intent.putExtra("pic", courseLiveBean.getCoverImage());
//                intent.putExtra(Constants.IS_CENTER, courseLiveBean.getPublicCourse());
//                mContext.startActivity(intent);
                    // VideoViewActivity.startToVidoViewActivity(mContext, "online",courseLiveBean.getService_id()+"",courseLiveBean.getTitle(),courseLiveBean.getMiddle_pic(),"0");
                }
            });
        }else {
            course_layout.setVisibility(View.GONE);
            famous_tea_layout.setVisibility(View.VISIBLE);
           // ImageLoad.loadImage(mContext,courseLiveBean.getHome_pic(),R.drawable.default_famous_teacher,tea_face);
            tea_name.setText(courseLiveBean.getName());
            tea_brief.setText(courseLiveBean.getBrief());

            if(model_style.equals("FAMOUS_TEACHER_ONE")){
                ImageLoad.loadCirCleImage(mContext,courseLiveBean.getUser_face(),R.drawable.default_teacher_header,tea_face);
            }else {
                Glide.with(mContext).load(courseLiveBean.getUser_face()).asBitmap().override(180, 180).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.default_teacher_header_fang).error(R.drawable.default_teacher_header_fang).centerCrop().into(new BitmapImageViewTarget(tea_face) {

                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(25);
                        tea_face.setImageDrawable(circularBitmapDrawable);
                    }

                });
            }
            mViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, TeacherBriefActivity.class).putExtra("brief",courseLiveBean.getBrief()).putExtra("faceUrl",courseLiveBean.getUser_face()).putExtra("model_key",model_style).putExtra("name",courseLiveBean.getName()));

                }
            });
        }

    }
}
