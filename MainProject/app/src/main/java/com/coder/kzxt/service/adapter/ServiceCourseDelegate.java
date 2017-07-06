package com.coder.kzxt.service.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.beans.CourseBean;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.GlideUtils;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/3/16
 */

public class ServiceCourseDelegate extends PullRefreshDelegate<CourseBean>
{
    private Context context;

    public ServiceCourseDelegate(Context context)
    {
        super(R.layout.item_service_course);
        this.context = context;
    }


    @Override
    public void initCustomView(BaseViewHolder holder, List<CourseBean> data, int position)
    {
        TextView courseName =   holder.findViewById(R.id.courseName);
        TextView price =   holder.findViewById(R.id.price);
        ImageView image =   holder.findViewById(R.id.image);

        CourseBean courseBean = data.get(position);
        GlideUtils.loadTopRoundService(context, courseBean.getMiddle_pic(), image);

        courseName.setText(courseBean.getTitle());
        if (courseBean.getPrice().equals("0.00"))
        {
            price.setText("免费");
            price.setTextColor(ContextCompat.getColor(context, R.color.font_green));
        } else
        {
            price.setText("VIP");
            price.setTextColor(ContextCompat.getColor(context, R.color.font_orange));
        }

    }

}
