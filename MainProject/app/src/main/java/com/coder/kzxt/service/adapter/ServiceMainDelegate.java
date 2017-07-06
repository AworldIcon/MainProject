package com.coder.kzxt.service.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.service.beans.ServiceBean;
import com.coder.kzxt.utils.GlideUtils;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/3/16
 */

public class ServiceMainDelegate extends PullRefreshDelegate<ServiceBean>
{
    private Context context;
    private String courseName;

    public ServiceMainDelegate(Context context)
    {
        super(R.layout.item_service_main);
        this.context = context;
    }


    @Override
    public void initCustomView(BaseViewHolder holder, List<ServiceBean> data, int position)
    {
        TextView serviceName = (TextView) holder.findViewById(R.id.serviceName);
        TextView serviceType = (TextView) holder.findViewById(R.id.serviceType);
        ImageView serviceImage = (ImageView) holder.findViewById(R.id.serviceImage);

        ServiceBean bean = data.get(position);
        GlideUtils.loadCourseImg(context, bean.getPicture(), serviceImage);
        serviceName.setText(bean.getTitle());
        if (bean.getType() == 0)
        {
            serviceType.setText(context.getResources().getString(R.string.service_certify));
        } else
        {
            serviceType.setText(context.getResources().getString(R.string.service_find_job));
        }
    }

}
