package com.coder.kzxt.service.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.buildwork.activity.CheckStuWorkActivity;
import com.coder.kzxt.recyclerview.adapter.BaseDelegate;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.service.beans.ServiceMyTestResult;
import com.coder.kzxt.stuwork.activity.TestPageActivity;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.ToastUtils;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/6/8
 */

public class FindJobDelegate extends BaseDelegate<ServiceMyTestResult.ItemsBean>
{
    private Context mContext;

    public FindJobDelegate(Context context)
    {
        super(R.layout.item_find_job1);
        this.mContext = context;
    }

    @Override
    public void initCustomView(BaseViewHolder holder, List<ServiceMyTestResult.ItemsBean> data, int position)
    {


        TextView title = holder.findViewById(R.id.title);
        TextView time = holder.findViewById(R.id.time);
        TextView status = holder.findViewById(R.id.status);
        final ServiceMyTestResult.ItemsBean items = data.get(position);
        title.setText(items.getTitle());
        time.setText(mContext.getResources().getString(R.string.end_time) + ": " + DateUtil.getDateOrderStrDot(items.getEnd_time()));
//        "status": "状态:1.未开始 2.进行中 3.已过期",
//        "result": "审核结果/状态 0未完成 1等待 2通过 3未通过",

        if (items.getStatus() == 3 && items.getTest_result().getResult() != 2)
        {
            status.setText("已过期");
        } else if (items.getStatus() == 1)
        {
            status.setText("");
        } else if (items.getTest_result().getResult() == 0)
        {
            status.setText("正在考核");
            status.setTextColor(ContextCompat.getColor(mContext, R.color.font_green));
        } else if (items.getTest_result().getResult() == 1)
        {
            status.setText("已提交，等待审核结果");
            status.setTextColor(ContextCompat.getColor(mContext, R.color.font_gray));
        } else if (items.getTest_result().getResult() == 2)
        {
            status.setText("通过");
            status.setTextColor(ContextCompat.getColor(mContext, R.color.font_gray));
        } else if (items.getTest_result().getResult() == 3)
        {
            status.setText("未通过");
            status.setTextColor(ContextCompat.getColor(mContext, R.color.font_red));
        }

        holder.getConvertView().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

    }
}
