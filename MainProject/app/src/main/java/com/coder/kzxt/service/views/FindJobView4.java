package com.coder.kzxt.service.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPutBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.service.beans.ServiceMemberResult;
import com.coder.kzxt.service.beans.ServiceRecordResult;
import com.coder.kzxt.utils.ToastUtils;

/**
 * Created by MaShiZhao on 2017/6/6
 */

public class FindJobView4 extends BaseView
{

    public FindJobView4(Context context, ServiceMemberResult.ItemBean itemBean)
    {
        super(context, itemBean);
    }

    private Button button;
    private TextView introduce, comment;
    private ImageView statusImage;

    @Override
    protected View getMainView()
    {
        View view = View.inflate(mContext, R.layout.view_find_service_step3, null);
        button = (Button) view.findViewById(R.id.button);
        introduce = (TextView) view.findViewById(R.id.introduce);
        comment = (TextView) view.findViewById(R.id.comment);
        statusImage = (ImageView) view.findViewById(R.id.statusImage);

        button.setText("预约综合面试");
        return view;
    }

    @Override
    protected String getTitleString()
    {
        return "4/5" + this.mContext.getResources().getString(R.string.service_step_4);
    }

    @Override
    protected void requestData()
    {
        if (itemBean.getTech_interview() != 2)
        {
            setTitleUnEnable();
            return;
        }else if (itemBean.getGlobal_interview() != 2)
        {
            setMainVisible();
        }


//       tech_interview": "技术面试状态：0未开启 1等待 2通过 3失败",


        if (itemBean.getGlobal_interview() == 0)
        {
            button.setVisibility(View.VISIBLE);
            introduce.setVisibility(View.GONE);
            comment.setVisibility(View.GONE);
            statusImage.setVisibility(View.GONE);
        } else if (itemBean.getGlobal_interview() == 1)
        {
            button.setVisibility(View.VISIBLE);
            introduce.setVisibility(View.GONE);
            comment.setVisibility(View.GONE);
            statusImage.setVisibility(View.GONE);

            button.setText("预约成功，等待老师与您联系...");
            button.setTextColor(ContextCompat.getColor(mContext, R.color.font_black));
            button.setBackgroundResource(R.drawable.round_gray1);
        } else
        {
            new HttpGetBuilder(mContext)
                    .setUrl(UrlsNew.SERVICE_INTERVIEW_ITEM)
                    .setHttpResult(FindJobView4.this)
                    .setClassObj(ServiceRecordResult.class)
                    .setPath(itemBean.getId() + "")
                    .addQueryParams("type", "2")
                    .build();
        }

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new HttpPutBuilder(mContext)
                        .setUrl(UrlsNew.SERVICE_MEMBER)
                        .setHttpResult(FindJobView4.this)
                        .setClassObj(BaseBean.class)
                        .setPath(itemBean.getId() + "")
                        .addBodyParam("id", itemBean.getId() + "")
                        .addBodyParam("type", "global_interview")
                        .setRequestCode(1)
                        .build();

            }
        });

    }


    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {
        if (requestCode == 1)
        {
            ToastUtils.makeText(mContext, "预约综合面试成功");

            button.setVisibility(View.VISIBLE);
            introduce.setVisibility(View.GONE);
            comment.setVisibility(View.GONE);
            statusImage.setVisibility(View.GONE);

            button.setText("预约成功，等待老师与您联系...");
            button.setTextColor(ContextCompat.getColor(mContext, R.color.font_black));
            button.setBackgroundResource(R.drawable.round_gray1);
            itemBean.setGlobal_interview(1);
            return;
        }
        ServiceRecordResult serviceRecordResult = (ServiceRecordResult) resultBean;
        String com = serviceRecordResult.getItem().getRemark();

        introduce.setVisibility(View.VISIBLE);
        comment.setVisibility(View.VISIBLE);
        statusImage.setVisibility(View.VISIBLE);
        comment.setText(com);

        if (itemBean.getGlobal_interview() == 2)
        {
            button.setVisibility(View.GONE);
        } else if (itemBean.getGlobal_interview() == 3)
        {
            button.setVisibility(View.VISIBLE);
            statusImage.setImageResource(R.drawable.service_status_unpassed);
            introduce.setText("技术面试未通过");
        }

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {
        ToastUtils.makeText(mContext, msg);
    }
}
