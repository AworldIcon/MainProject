package com.coder.kzxt.service.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.service.beans.ServiceMemberResult;

/**
 * Created by MaShiZhao on 2017/6/8
 */

public abstract class BaseView implements HttpCallBack
{
    private TextView title;
    private LinearLayout mainView;
    private ImageView image;

    protected Context mContext;
    protected ServiceMemberResult.ItemBean itemBean;

    public BaseView(Context context, ServiceMemberResult.ItemBean itemBean)
    {
        this.mContext = context;
        this.itemBean = itemBean;
    }

    //设置titleBar不可用
    protected void setTitleUnEnable()
    {
        if (title != null)
        {
            title.setTextColor(ContextCompat.getColor(mContext, R.color.font_light_gray));
            title.setBackgroundResource(R.drawable.round_white_top);
        }
        if (image != null)
        {
            image.setVisibility(View.GONE);
        }
    }

    //设置titleBaGone
    protected void setTitleGone()
    {
        if (title != null)
        {
            title.setVisibility(View.GONE);
        }
        if (image != null)
        {
            image.setVisibility(View.GONE);
        }
    }

    //设置main显示
    protected void setMainVisible()
    {
        if (mainView != null)
        {
            mainView.setVisibility(View.VISIBLE);
        }
        if (image != null)
        {
            image.setImageResource(R.drawable.sign_arrow_up);
        }
    }

    public View getView()
    {
        View view = View.inflate(mContext, R.layout.view_service_base, null);
        title = (TextView) view.findViewById(R.id.title);
        mainView = (LinearLayout) view.findViewById(R.id.mainView);
        image = (ImageView) view.findViewById(R.id.image);

        title.setText(getTitleString());
        mainView.addView(getMainView());

        image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ;

                if (mainView.getVisibility() == View.VISIBLE)
                {
                    mainView.setVisibility(View.GONE);
                    image.setImageResource(R.drawable.sign_arrow_down);

                } else
                {
                    mainView.setVisibility(View.VISIBLE);
                    image.setImageResource(R.drawable.sign_arrow_up);

                }
            }
        });

        requestData();

        return view;
    }

    //设置中间的布局
    protected abstract View getMainView();

    //设置title名称
    protected abstract String getTitleString();

    //请求服务器
    protected abstract void requestData();

}
