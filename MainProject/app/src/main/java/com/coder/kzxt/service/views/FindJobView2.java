package com.coder.kzxt.service.views;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.service.beans.ServiceMemberResult;
import com.coder.kzxt.service.beans.ServiceRecordResult;

/**
 * Created by MaShiZhao on 2017/6/6
 */

public class FindJobView2 extends BaseView
{


    public FindJobView2(Context context, ServiceMemberResult.ItemBean itemBean)
    {
        super(context, itemBean);
    }

    private TextView introduce, comment;
    private ImageView statusImage;

    @Override
    protected View getMainView()
    {
        View view = View.inflate(mContext, R.layout.view_find_service_step2, null);

        introduce = (TextView) view.findViewById(R.id.introduce);
        comment = (TextView) view.findViewById(R.id.comment);
        statusImage = (ImageView) view.findViewById(R.id.statusImage);

        return view;
    }

    @Override
    protected String getTitleString()
    {
        return "2/5" + this.mContext.getResources().getString(R.string.service_step_2);
    }

    @Override
    protected void requestData()
    {
        if (itemBean.getExamine() != 2)
        {
            setTitleUnEnable();
            return;
        } else if (itemBean.getResume() != 2)
        {
            setMainVisible();
        }

//        "resume": "简历状态：0未开启 1等待 2通过 3失败",

        if (itemBean.getResume() == 0)
        {
            statusImage.setImageResource(R.drawable.service_status_passed);
            introduce.setText("请前往网页端提交简历,完成简历审核。\r\n网站：" + UrlsNew.URLHeader);
            comment.setText("");
        } else if (itemBean.getResume() == 1)
        {
            statusImage.setImageResource(R.drawable.service_status_wait);
            introduce.setText("简历已提交，等待审核结果。");
            comment.setText("");
        } else
        {
            new HttpGetBuilder(mContext)
                    .setUrl(UrlsNew.SERVICE_RECORD_ITEM)
                    .setHttpResult(this)
                    .setClassObj(ServiceRecordResult.class)
                    .setPath(itemBean.getId() + "")
                    .addQueryParams("state", itemBean.getResume() + "")
                    .addQueryParams("type", "resume")
                    .build();
        }


    }


    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {

        ServiceRecordResult serviceRecordResult = (ServiceRecordResult) resultBean;
        String com = serviceRecordResult.getItem().getDesc();
        if (itemBean.getResume() == 3)
        {
            statusImage.setImageResource(R.drawable.service_status_unpassed);
            introduce.setText("简历审核未通过，请前往网页端重新提交简历。\r\n网站：" + UrlsNew.URLHeader);
            comment.setText(com);
        } else if (itemBean.getResume() == 2)
        {
            statusImage.setImageResource(R.drawable.service_status_passed);
            introduce.setText("简历审核通过");
            comment.setText(com);
        }

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {

    }
}
