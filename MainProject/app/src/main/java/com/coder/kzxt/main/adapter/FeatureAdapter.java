package com.coder.kzxt.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.image.ImageLoad;
import com.app.utils.L;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.course.activity.LocalCourseActivity;
import com.coder.kzxt.main.beans.MainModelBean;
import com.coder.kzxt.utils.SharedPreferencesUtil;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class FeatureAdapter extends HolderBaseAdapter<MainModelBean.ItemsBean.ListBean> {

    private Context mContext;
    private SharedPreferencesUtil pu;
    private String unReadChatNumber;
    private String unReadNoticeNumber;
    private String num = "";
    private MainModelBean.ItemsBean moduleType;
    private static String FAMOUS_TEACHER_ONE="TOPIC_HENG_FOUR";
    private static String FAMOUS_TEACHER_TWO="TOPIC_ONEHENG_FOUR";
    public FeatureAdapter(Context mContext,MainModelBean.ItemsBean moduleType, List<MainModelBean.ItemsBean.ListBean> data) {
        super();
        this.mContext = mContext;
        this.data = data;
        this.moduleType=moduleType;
        pu = new SharedPreferencesUtil(mContext);
        unReadChatNumber = "";
        unReadNoticeNumber = "";
    }

    protected void setunReadChatNumber(int num) {
        if (!TextUtils.isEmpty(pu.getIsLogin()) && num != 0) {
            if (num > 99) {
                unReadChatNumber = "99+";
            } else {
                unReadChatNumber = num + "";
            }
        } else {
            unReadChatNumber = "";
        }
        L.d("featureadapter  unReadChatNumber = "+num);
        this.notifyDataSetChanged();
    }

    protected void setUnReadNoticeNumber(int num) {
        if (!TextUtils.isEmpty(pu.getIsLogin()) && num != 0) {
            if (num > 99) {
                unReadNoticeNumber = "99+";
            } else {
                unReadNoticeNumber = num + "";
            }
        } else {
            unReadNoticeNumber = "";
        }
        L.d("featureadapter  unReadNoticeNumber = "+num);
        this.notifyDataSetChanged();
    }
    @Override
    public ViewHolder getViewHolder(View convertView, ViewGroup parent, final int position) {
        ViewHolder mViewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_feature);
        final ImageView head = mViewHolder.findViewById(R.id.head);
        TextView name = mViewHolder.findViewById(R.id.name);
        TextView unReadNumber = mViewHolder.findViewById(R.id.unReadNumber);

        final MainModelBean.ItemsBean.ListBean bean = data.get(position);

        //精品专题
        if (!TextUtils.isEmpty(String.valueOf(bean.getId()))) {
            if(moduleType.getModule_style().equals(FAMOUS_TEACHER_ONE)){
                ImageLoad.loadCirCleImage(mContext,bean.getHome_pic(),R.drawable.default_topic, head);
            }else if(moduleType.getModule_style().equals(FAMOUS_TEACHER_TWO)){
                // 圆角矩形
                Glide.with(mContext).load(bean.getHome_pic()).asBitmap().override(180, 180).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.default_topic_fang).error(R.drawable.default_topic_fang).centerCrop().into(new BitmapImageViewTarget(head) {

                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(25);
                        head.setImageDrawable(circularBitmapDrawable);
                    }

                });
            }

        } else {
            if(pu.getUserType().equals("2")){
                if(moduleType.getModule_style().equals(FAMOUS_TEACHER_ONE)){
                    ImageLoad.loadImage(mContext,bean.getHome_pic(),R.drawable.default_teacher_seller,R.drawable.default_teacher_seller,90, RoundedCornersTransformation.CornerType.ALL,head);
                }else {
                    ImageLoad.loadImage(mContext,bean.getHome_pic(),R.drawable.default_teacher_seller,R.drawable.default_teacher_seller,head);
                }
            }else{
                if(moduleType.getModule_style().equals(FAMOUS_TEACHER_ONE)){
                    ImageLoad.loadImage(mContext,bean.getHome_pic(),R.drawable.default_student_seller,R.drawable.default_student_seller,90, RoundedCornersTransformation.CornerType.ALL,head);
                }else {
                    ImageLoad.loadImage(mContext,bean.getHome_pic(),R.drawable.default_student_seller,R.drawable.default_student_seller,head);
                }
            }
        }
        name.setText(bean.getName());




        mViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //精品专题
                if (!TextUtils.isEmpty(String.valueOf(bean.getId()))) {
                    mContext.startActivity(new Intent(mContext, LocalCourseActivity.class).putExtra("model_key","TOPIC_"+data.get(position).getId()).putExtra("title",data.get(position).getName()));
                    //mContext.startActivity(new Intent(mContext, CategoryCourseListActivity.class).putExtra("id", bean.getService_id()).putExtra("title", bean.getName()));
                } else {

                    }
                }


        });


        return mViewHolder;

    }
}
