package com.coder.kzxt.video.adapter;

import android.content.Context;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.recyclerview.adapter.BaseDelegate;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.video.beans.ReplayBean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */

public class LiveReplayDelegate extends BaseDelegate {

    private Context context;

    public LiveReplayDelegate(Context context) {
        super(R.layout.live_replay_item);
        this.context =context;
    }
    @Override
    public void initCustomView(BaseViewHolder holder, List data, int position) {

        TextView time_tv = holder.findViewById(R.id.time_tv);
        TextView replay_title_tv = holder.findViewById(R.id.replay_title_tv);
        ReplayBean.ItemsBean itemsBean = (ReplayBean.ItemsBean) data.get(position);
        replay_title_tv.setText(itemsBean.getTitle());
        if(DateUtil.formatSecond(itemsBean.getVideo_size()).length()<6){
            time_tv.setText("00:"+DateUtil.formatSecond(itemsBean.getVideo_size()));
        }else {
            time_tv.setText(DateUtil.formatSecond(itemsBean.getVideo_size()));
        }

        if(getAdapter().getSelectPosition()==position){
            replay_title_tv.setTextColor(context.getResources().getColor(R.color.first_theme));
        }else {
            replay_title_tv.setTextColor(context.getResources().getColor(R.color.font_black));
        }


        super.initCustomView(holder, data, position);
    }
}
