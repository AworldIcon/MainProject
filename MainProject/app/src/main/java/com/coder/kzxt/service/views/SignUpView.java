package com.coder.kzxt.service.views;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.service.adapter.ServiceExamDelegate;
import com.coder.kzxt.service.beans.ServiceExamResult;
import com.coder.kzxt.service.beans.ServiceMemberResult;
import com.coder.kzxt.service.beans.ServiceRecordResult;

/**
 * Created by MaShiZhao on 2017/6/8
 * 报名考试
 */

public class SignUpView extends BaseView
{


    private TextView introduce, comment;
    private ImageView statusImage;
    private MyRecyclerView myRecyclerView;

    public SignUpView(Context context, ServiceMemberResult.ItemBean itemBean)
    {
        super(context, itemBean);
    }

    @Override
    protected View getMainView()
    {
        View view = View.inflate(mContext, R.layout.view_service_sign_up, null);
        introduce = (TextView) view.findViewById(R.id.introduce);
        comment = (TextView) view.findViewById(R.id.comment);
        statusImage = (ImageView) view.findViewById(R.id.statusImage);
        myRecyclerView = (MyRecyclerView) view.findViewById(R.id.myRecyclerView);
        return view;
    }

    @Override
    protected String getTitleString()
    {
//        "type": "服务类型 1就业服务 0认证服务",
        if (itemBean.getMyType() == 0)
        {
            setTitleGone();
        } else if (itemBean.getType() == 0)
        {
            return "2/3" + this.mContext.getResources().getString(R.string.sign_up_test);
        }
        return "";
    }


    protected void requestData()
    {
//        "entry": "报名表状态：0未开启 1等待 2通过 3失败 ",
//        "exam": "考试状态：0未开启 1等待 2通过 3失败 ",

        if (itemBean.getMyType() == 0)
        {
            setMainVisible();
        } else if (itemBean.getMyType() == 2)
        {
            //没有通过阶段考核则不可点击
            if (itemBean.getExamine() != 2)
            {
                setTitleUnEnable();
            } else if (itemBean.getEntry() != 2)
            {
                // 通过阶段考核但是没有完成包名考试则默认显示中间内容
                setMainVisible();
            }
        }

        if (itemBean.getEntry() == 0)
        {
            statusImage.setImageResource(R.drawable.service_status_passed);
            introduce.setText("请前往网页端提交考试报名表。\r\n 网站：" + UrlsNew.URLHeader);
            comment.setVisibility(View.GONE);
        } else if (itemBean.getEntry() == 1)
        {
            statusImage.setImageResource(R.drawable.service_status_wait);
            introduce.setText("考试报名表已提交，等待审核结果。");
            comment.setVisibility(View.GONE);
        } else if (itemBean.getEntry() == 2)
        {


            statusImage.setImageResource(R.drawable.service_status_passed);
            introduce.setText("考试报名表审核通过");

            //未开始考试 显示详情
            if (itemBean.getExam() == 2)
            {
                statusImage.setImageResource(R.drawable.service_status_passed);
                introduce.setText("考试通过");
            } else if (itemBean.getExam() == 3)
            {
                statusImage.setImageResource(R.drawable.service_status_unpassed);
                introduce.setText("考试未通过,请重考");
            }else{

                new HttpGetBuilder(mContext)
                        .setUrl(UrlsNew.SERVICE_RECORD_ITEM)
                        .setHttpResult(this)
                        .setClassObj(ServiceRecordResult.class)
                        .setPath(itemBean.getId() + "")
                        .addQueryParams("state", itemBean.getEntry() + "")
                        .addQueryParams("type", "entry")
                        .build();
            }


            new HttpGetBuilder(mContext)
                    .setUrl(UrlsNew.SERVICE_EXAM)
                    .setHttpResult(this)
                    .setClassObj(ServiceExamResult.class)
                    .addQueryParams("service_member_id", itemBean.getId() + "")
                    .addQueryParams("pageSize", "100")
                    .addQueryParams("page", "1")
                    .setRequestCode(1)
                    .build();

        }else {
            statusImage.setImageResource(R.drawable.service_status_unpassed);
            introduce.setText("考试报名表审核未通过，请前往网页端重新提交考试报名表。\r\n网站：" + UrlsNew.URLHeader);


            new HttpGetBuilder(mContext)
                    .setUrl(UrlsNew.SERVICE_RECORD_ITEM)
                    .setHttpResult(this)
                    .setClassObj(ServiceRecordResult.class)
                    .setPath(itemBean.getId() + "")
                    .addQueryParams("state", itemBean.getEntry() + "")
                    .addQueryParams("type", "entry")
                    .build();

        }


    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {
        if (requestCode == 1)
        {

            ServiceExamResult result = (ServiceExamResult) resultBean;
            myRecyclerView.setAdapter(new BaseRecyclerAdapter(mContext, result.getItem(), new ServiceExamDelegate(mContext)));
            myRecyclerView.setNestedScrollingEnabled(false);

        } else
        {
            ServiceRecordResult serviceRecordResult = (ServiceRecordResult) resultBean;
            String com = serviceRecordResult.getItem().getDesc();
            if (!TextUtils.isEmpty(com))
            {
                comment.setText(com);
                comment.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {

    }
}
