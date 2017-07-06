package com.coder.kzxt.buildwork.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.buildwork.entity.FinishWorkNum;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.GlideUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by pc on 2017/3/28.
 */

public class WorkStuDelegate extends PullRefreshDelegate<FinishWorkNum.ItemsBean> {
    private Context context;

    public WorkStuDelegate(Context context) {
        super(R.layout.finish_work);
        this.context=context;
    }


    @Override
    public void initCustomView(BaseViewHolder holder, List<FinishWorkNum.ItemsBean> data, int position) {
        ImageView headImage;
        TextView nickname;
        TextView finishTime;
        TextView status;
        TextView score;
        headImage= (ImageView) holder.findViewById(R.id.head_Img);
        nickname= (TextView) holder.findViewById(R.id.finish_nickname);
        finishTime= (TextView) holder.findViewById(R.id.finish_time);
        status= (TextView) holder.findViewById(R.id.status);
        score= (TextView) holder.findViewById(R.id.score);
        GlideUtils.loadCircleHeaderOfCommon(context,data.get(position).getProfile().getAvatar(),headImage);

        nickname.setText(data.get(position).getProfile().getNickname());
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        int i = Integer.parseInt(data.get(position).getFinished_time());
        String times = sdr.format(new Date(i * 1000L));
        finishTime.setText(times);
        final String status_text=data.get(position).getStatus();
        if(status_text.equals("2")){
            status.setText("待批");
            status.setTextColor(context.getResources().getColor(R.color.font_red));
            score.setText("--");
        }else if(status_text.equals("3")){
            status.setText("完成");
            status.setTextColor(context.getResources().getColor(R.color.font_gray));
            score.setText(data.get(position).getScore());

        }else if(status_text.equals("1")) {
            status.setText("答题中");
            status.setTextColor(context.getResources().getColor(R.color.font_gray));
            score.setText(data.get(position).getScore());
        }
    }
}
