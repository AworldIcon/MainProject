package com.coder.kzxt.sign.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.sign.beans.SignBean;
import com.coder.kzxt.sign.utils.SignUtils;
import com.coder.kzxt.utils.DateUtil;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/3/16
 */

public class SignHistoryListTeacherDelegate extends PullRefreshDelegate<SignBean>
{
    private Context context;

    public SignHistoryListTeacherDelegate(Context context)
    {
        super(R.layout.item_sign_history);
        this.context = context;
    }


    @Override
    public void initCustomView(BaseViewHolder holder, List<SignBean> data, int position)
    {
        TextView time = (TextView) holder.findViewById(R.id.time);
        TextView signed = (TextView) holder.findViewById(R.id.signed);
        ProgressBar progressbar_signed = (ProgressBar) holder.findViewById(R.id.progressbar_signed);
        TextView un_sign = (TextView) holder.findViewById(R.id.un_sign);
        ProgressBar progressbar_unsign = (ProgressBar) holder.findViewById(R.id.progressbar_unsign);
        final LinearLayout view_sign_list = (LinearLayout) holder.findViewById(R.id.view_sign_list);
        TextView total_numbers = (TextView) holder.findViewById(R.id.total_numbers);
        TextView sign_numbers = (TextView) holder.findViewById(R.id.sign_numbers);
        TextView un_sign_numbers = (TextView) holder.findViewById(R.id.un_sign_numbers);
        TextView sign_start_time = (TextView) holder.findViewById(R.id.sign_start_time);
        TextView sign_end_time = (TextView) holder.findViewById(R.id.sign_end_time);
        TextView sign_time_length = (TextView) holder.findViewById(R.id.sign_time_length);
        TextView sign_range = (TextView) holder.findViewById(R.id.sign_range);
        TextView sign_location = (TextView) holder.findViewById(R.id.sign_location);
        final ImageView down_image = holder.findViewById(R.id.down_image);

        SignBean signBean = data.get(position);
        time.setText(DateUtil.getDateAndSecond(signBean.getBegin_time()));
        progressbar_signed.setProgress(signBean.getSignProgress());
        progressbar_unsign.setProgress(signBean.getUnsignProgress());

        signed.setText(signBean.getSignProgress() + "%");
        un_sign.setText(signBean.getUnsignProgress() + "%");

        total_numbers.setText(signBean.getNum() + "\n" + context.getResources().getString(R.string.should_signed));
        sign_numbers.setText(signBean.getSign_num() + "\n" + context.getResources().getString(R.string.signed));
        un_sign_numbers.setText(signBean.getUnsign_num() + "\n" + context.getResources().getString(R.string.signed_not));
        sign_start_time.setText(DateUtil.getDateAndSecond(signBean.getBegin_time()));
        sign_end_time.setText(DateUtil.getDateAndSecond(signBean.getEnd_time()));
        sign_time_length.setText(DateUtil.getTime(signBean.getTime()));
        sign_range.setText(SignUtils.getRangeString(signBean.getRange()));
        sign_location.setText(signBean.getAddress());
        view_sign_list.setVisibility(View.GONE);
        down_image.setImageResource(R.drawable.sign_arrow_down);

        down_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (view_sign_list.getVisibility() == View.VISIBLE)
                {
                    view_sign_list.setVisibility(View.GONE);
                    down_image.setImageResource(R.drawable.sign_arrow_down);

                } else
                {
                    view_sign_list.setVisibility(View.VISIBLE);
                    down_image.setImageResource(R.drawable.sign_arrow_up);

                }
            }
        });
    }

}
