package com.coder.kzxt.service.adapter;

import android.content.Context;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.recyclerview.adapter.BaseDelegate;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.service.beans.ServiceExamResult;
import com.coder.kzxt.utils.DateUtil;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/3/16
 */

public class ServiceExamDelegate extends BaseDelegate<ServiceExamResult.ItemsBean>
{
    private Context context;

    public ServiceExamDelegate(Context context)
    {
        super(R.layout.item_service_exam);
        this.context = context;
    }


    @Override
    public void initCustomView(BaseViewHolder holder, List<ServiceExamResult.ItemsBean> data, int position)
    {
        TextView testName = (TextView) holder.findViewById(R.id.testName);
        TextView time = (TextView) holder.findViewById(R.id.time);
        TextView area = (TextView) holder.findViewById(R.id.area);
        TextView notice_message = (TextView) holder.findViewById(R.id.notice_message);

        ServiceExamResult.ItemsBean bean = data.get(position);

        testName.setText(bean.getTitle());
        time.setText(DateUtil.getDateAndSecond(bean.getExam_time()));
        area.setText(bean.getPlace());
        notice_message.setText(bean.getRemark());

    }

}
