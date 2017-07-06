package com.coder.kzxt.video.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.recyclerview.adapter.BaseDelegate;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/4/1.
 */

public class FunctionDelegate extends BaseDelegate
{

    private Context context;
    private List<String> strings;
    public static int workNum, examNum, liveNum, signNum;

    public FunctionDelegate(Context context, List<String> strings)
    {
        super(R.layout.video_function_view);
        this.context = context;
        this.strings = strings;
    }

    @Override
    public void initCustomView(BaseViewHolder holder, List data, int position)
    {
        TextView textView = holder.findViewById(R.id.func_tx);
        View work_notify_view = holder.findViewById(R.id.work_notify_view);
        work_notify_view.setVisibility(View.GONE);
        String string = (String) data.get(position);
        textView.setText(string);

        GradientDrawable myGrad = (GradientDrawable) textView.getBackground();
        if (position == 0)
        {
            myGrad.setColor(context.getResources().getColor(R.color.bg_video_im));// 设置内部颜色
        } else if (position == 1)
        {
            if (workNum > 0)
            {
                work_notify_view.setVisibility(View.VISIBLE);
            }
            myGrad.setColor(context.getResources().getColor(R.color.bg_video_work));// 设置内部颜色
        } else if (position == 2)
        {
            if (examNum > 0)
            {
                work_notify_view.setVisibility(View.VISIBLE);
            }
            myGrad.setColor(context.getResources().getColor(R.color.bg_video_exam));// 设置内部颜色
        } else if (position == 3)
        {
            myGrad.setColor(context.getResources().getColor(R.color.bg_video_live));// 设置内部颜色
            if (liveNum > 0)
            {
                work_notify_view.setVisibility(View.VISIBLE);
            }
        } else if (position == 4)
        {
            myGrad.setColor(context.getResources().getColor(R.color.bg_video_signin));// 设置内部颜色
            if (signNum > 0)
            {
                work_notify_view.setVisibility(View.VISIBLE);
            }
        } else if (position == 5)
        {
            myGrad.setColor(context.getResources().getColor(R.color.bg_video_question));// 设置内部颜色
        }

        super.initCustomView(holder, data, position);
    }
}
