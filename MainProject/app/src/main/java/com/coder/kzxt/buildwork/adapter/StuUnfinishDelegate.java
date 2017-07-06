package com.coder.kzxt.buildwork.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.buildwork.entity.UnfinishNum;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.GlideUtils;

import java.util.List;

/**
 * Created by pc on 2017/4/7.
 */

public class StuUnfinishDelegate extends PullRefreshDelegate<UnfinishNum.ItemsBean> {
    private Context context;
    private String name;

    public StuUnfinishDelegate(Context context,String name) {
        super(R.layout.item_unfinish_work_stu);
        this.context=context;
        this.name=name;
    }

    @Override
    public void initCustomView(BaseViewHolder holder, List<UnfinishNum.ItemsBean> data, int position) {
        final ImageView header=holder.findViewById(R.id.stu_picture);
        TextView textView=holder.findViewById(R.id.stu_name);
        TextView class_name=holder.findViewById(R.id.class_name);
        class_name.setText(name);//http://192.168.3.8//uploads//avatar//20170325//5e26cd8441519b7fe06e2a744b5d99c2.png
//        Glide.with(context).load(data.get(position).getProfile().getAvatar()).asBitmap().override(180, 180).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.default_teacher_header).error(R.drawable.default_teacher_header).centerCrop().into(new BitmapImageViewTarget(header) {
//
//            @Override
//            protected void setResource(Bitmap resource) {
//                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
//                circularBitmapDrawable.setCornerRadius(25);
//                header.setImageDrawable(circularBitmapDrawable);
//            }
//
//        });
        GlideUtils.loadCircleHeaderOfCommon(context,data.get(position).getProfile().getAvatar(),header);

        textView.setText(data.get(position).getProfile().getNickname());

    }
}
