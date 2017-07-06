package com.coder.kzxt.message.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.message.activity.NotificationDetailActivity;
import com.coder.kzxt.message.activity.NotificationListActivity;
import com.coder.kzxt.message.beans.NotificationListBean;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.DateUtil;

import java.util.List;

/**
 * Created by zw on 2017/6/23.
 */

public class NoticePublishDelegate extends PullRefreshDelegate<NotificationListBean.ItemsBean>
{
    private Context context;
    private int type;

    public NoticePublishDelegate(Context context,int type)
    {
        super(R.layout.item_publish_notice);
        this.context = context;
        this.type=type;
    }

    @Override
    public void initCustomView(BaseViewHolder convertView, final List<NotificationListBean.ItemsBean> data, final int position)
    {
        ImageView notice_image=convertView.findViewById(R.id.notice_image);
        TextView notice_target=convertView.findViewById(R.id.notice_target);
        TextView notice_title=convertView.findViewById(R.id.notice_title);
        TextView receive_number=convertView.findViewById(R.id.receive_number);
        TextView notice_time=convertView.findViewById(R.id.notice_time);
        final View notify_view=convertView.findViewById(R.id.notify_view);
        notice_target.setVisibility(View.GONE);

        if(type==2){
            notice_title.setText(data.get(position).getTitle());
            receive_number.setText("发送人："+data.get(position).getProfile().getNickname());
            notice_time.setText(DateUtil.getDayBef(data.get(position).getUpdate_time()));
            notice_image.setImageResource(R.drawable.notification_pink);
            if(data.get(position).getIs_read()==1){
                notify_view.setVisibility(View.GONE);
            }else {
                notify_view.setVisibility(View.VISIBLE);
            }

        }else {
            notice_title.setText(data.get(position).getTitle());
            receive_number.setText(data.get(position).getRead_num()+"/"+data.get(position).getNumber());
            notice_time.setText(DateUtil.getDayBef(data.get(position).getUpdate_time()));
            notice_image.setImageResource(R.drawable.notification);
            notify_view.setVisibility(View.GONE);
        }
        convertView.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify_view.setVisibility(View.GONE);
                Intent intent = new Intent(context, NotificationDetailActivity.class);
                intent.putExtra("notifyId", data.get(position).getId());
                ((NotificationListActivity)context).startActivityForResult(intent, 4);
            }
        });
    }
}
