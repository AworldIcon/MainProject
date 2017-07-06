package com.coder.kzxt.sign.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.sign.beans.SignStudentResult;
import com.coder.kzxt.utils.DateUtil;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/3/16
 */

public class SignHistoryListStudentDelegate extends PullRefreshDelegate<SignStudentResult.ItemsBean>
{
    private Context context;
    private String courseName;

    public SignHistoryListStudentDelegate(Context context, String courseName)
    {
        super(R.layout.item_sign_history_student);
        this.context = context;
        this.courseName = courseName;
    }


    @Override
    public void initCustomView(BaseViewHolder holder, List<SignStudentResult.ItemsBean> data, int position)
    {
        TextView start_time = (TextView) holder.findViewById(R.id.start_time);
        TextView course_name = (TextView) holder.findViewById(R.id.course_name);
        TextView status = (TextView) holder.findViewById(R.id.status);
        TextView status_mark = (TextView) holder.findViewById(R.id.status_mark);
        MyRecyclerView myRecyclerView = (MyRecyclerView) holder.findViewById(R.id.myRecyclerView);
        SignStudentResult.ItemsBean signBean = data.get(position);
        start_time.setText(DateUtil.getDateAndSecond(signBean.getSign().getCreate_time()));
        course_name.setText(this.courseName);

        if (signBean.getStatus() == 1)
        {
            status.setText(context.getResources().getString(R.string.signed));
        } else
        {
            status.setText(context.getResources().getString(R.string.signed_not));
        }

        if (signBean.getInfo().getIs_offline() == 1 && signBean.getStatus() == 1)
        {
            status_mark.setText(context.getResources().getString(R.string.offline));
        } else if (signBean.getIs_rang() == 0 && signBean.getStatus() == 1)
        {
            status_mark.setText(context.getResources().getString(R.string.not_in_scope));
        } else
        {
            status_mark.setText("");
        }

        if (signBean.getIsRecord())
        {
            status.setTextColor(context.getResources().getColor(R.color.first_theme));
            status_mark.setTextColor(context.getResources().getColor(R.color.first_theme));
        } else
        {
            status.setTextColor(context.getResources().getColor(R.color.font_black));
            status_mark.setTextColor(context.getResources().getColor(R.color.font_gray));
        }


        if (signBean.getRecord().getTags().size() == 0)
        {
            myRecyclerView.setVisibility(View.GONE);
        } else
        {
            myRecyclerView.setVisibility(View.VISIBLE);
            myRecyclerView.setAdapter(new BaseRecyclerAdapter(context, signBean.getRecord().getTags(), new TagDelegate()));
        }
    }

}
