package com.coder.kzxt.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.image.ImageLoad;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.course.activity.TeacherBriefActivity;
import com.coder.kzxt.main.beans.MainModelBean;

import java.util.List;

public class TeacherAdapter extends HolderBaseAdapter<MainModelBean.ItemsBean.ListBean> {

    private Context mContext;
    //private PublicUtils pu;
    private static String FAMOUS_TEACHER_ONE="FAMOUS_TEACHER_ONE";
    private static String FAMOUS_TEACHER_TWO="FAMOUS_TEACHER_TWO";
    private MainModelBean.ItemsBean moduleType;
    public TeacherAdapter(Context mContext,MainModelBean.ItemsBean moduleType, List<MainModelBean.ItemsBean.ListBean> data) {
        super();
        this.mContext = mContext;
        this.data = data;
        this.moduleType=moduleType;
        //pu = new PublicUtils(mContext);

    }

    @Override
    public ViewHolder getViewHolder(View convertView, ViewGroup parent, final int position) {
        ViewHolder mViewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_teacher);
        final ImageView head = mViewHolder.findViewById(R.id.head);
        TextView name = mViewHolder.findViewById(R.id.name);

        final MainModelBean.ItemsBean.ListBean bean = data.get(position);
        if(moduleType.getModule_style().equals(FAMOUS_TEACHER_ONE)){
            ImageLoad.loadCirCleImage(mContext,bean.getUser_face(),R.drawable.default_teacher_header,head);
        }else {
            Glide.with(mContext).load(bean.getUser_face()).asBitmap().override(180, 180).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.default_teacher_header_fang).error(R.drawable.default_teacher_header_fang).centerCrop().into(new BitmapImageViewTarget(head) {

                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                    circularBitmapDrawable.setCornerRadius(25);
                    head.setImageDrawable(circularBitmapDrawable);
                }

            });
        }
        name.setText(bean.getName());

        mViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, TeacherBriefActivity.class).putExtra("brief",data.get(position).getBrief()).putExtra("faceUrl",data.get(position).getUser_face()).putExtra("model_key",moduleType.getModule_style()).putExtra("name",data.get(position).getName()));
            }
        });

        return mViewHolder;

    }
}
