package com.coder.kzxt.message.adapter;

import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.message.beans.NotificationReadNumBean;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.GlideUtils;

import java.util.List;

/**
 * Created by zw on 2017/6/23
 */

public class NotificationNumberDelegate extends PullRefreshDelegate
{
    private String type;
    public NotificationNumberDelegate(FragmentActivity activity,String type)
    {
        super( R.layout.item_notice_stu);
        this.type=type;
    }


    @Override
    public void initCustomView(BaseViewHolder holder, List data, int position)
    {
        ImageView imageView = holder.findViewById(R.id.head_image);
        TextView class_name = holder.findViewById(R.id.class_name);
        TextView stu_name = holder.findViewById(R.id.stu_name);
        if(type.equals("1")){
            List<NotificationReadNumBean.ItemBean.ReadBean>readBeanList=data;
            GlideUtils.loadCircleHeaderOfCommon(holder.getConvertView().getContext(),readBeanList.get(position).getAvatar(),imageView);
            stu_name.setText(readBeanList.get(position).getNickname());
            class_name.setText(readBeanList.get(position).getCourse_class().substring(0,readBeanList.get(position).getCourse_class().length()-1));

        }else {
            List<NotificationReadNumBean.ItemBean.UnReadBean>readBeanList=data;
            GlideUtils.loadCircleHeaderOfCommon(holder.getConvertView().getContext(),readBeanList.get(position).getAvatar(),imageView);
            stu_name.setText(readBeanList.get(position).getNickname());
            class_name.setText(readBeanList.get(position).getCourse_class().substring(0,readBeanList.get(position).getCourse_class().length()-1));

        }
    }
}
