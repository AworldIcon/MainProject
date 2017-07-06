package com.coder.kzxt.service.views;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.buildwork.activity.CheckStuWorkActivity;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.service.adapter.FindJobDelegate;
import com.coder.kzxt.service.beans.ServiceMemberResult;
import com.coder.kzxt.service.beans.ServiceMyTestResult;
import com.coder.kzxt.stuwork.activity.TestPageActivity;
import com.coder.kzxt.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaShiZhao on 2017/6/6
 */

public class FindJobView1 extends BaseView
{


    private TextView all_passed;
    private MyRecyclerView myRecyclerView;

    public FindJobView1(Context context, ServiceMemberResult.ItemBean itemBean)
    {
        super(context, itemBean);
    }

    @Override
    protected View getMainView()
    {
        View view = View.inflate(mContext, R.layout.view_find_service_step1, null);

        all_passed = (TextView) view.findViewById(R.id.all_passed);
        myRecyclerView = (MyRecyclerView) view.findViewById(R.id.myRecyclerView);

        return view;
    }

    @Override
    protected String getTitleString()
    {
        return getStage() + this.mContext.getResources().getString(R.string.service_step_1);
    }

    private String getStage()
    {
//        "type": "服务类型 1就业服务 0认证服务",
        if (itemBean.getType() == 0)
        {
            return "1/3";
        } else
        {
            return "1/5";
        }
    }

    private BaseRecyclerAdapter baseRecyclerAdapter;
    private List<ServiceMyTestResult.ItemsBean> data;

    @Override
    protected void requestData()
    {
//        考试状态：0未开启 1等待 2通过 3失败 ",
        if (itemBean.getExamine() == 2)
        {
            all_passed.setText(mContext.getString(R.string.test_all_passed));
            all_passed.setVisibility(View.VISIBLE);
        } else
        {
            setMainVisible();
        }


        data = new ArrayList<>();
        baseRecyclerAdapter = new BaseRecyclerAdapter(mContext, data, new FindJobDelegate(mContext));
        myRecyclerView.setAdapter(baseRecyclerAdapter);

        baseRecyclerAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

                ServiceMyTestResult.ItemsBean items = data.get(position);
                if (items.getStatus() == 3 && items.getTest_result().getResult() != 2)
                {
                    ToastUtils.makeText(mContext, "考试已过期");
                } else if (items.getTest_result().getResult() == 1)
                {
                    ToastUtils.makeText(mContext, "正在审核，不能查看");
                } else if (items.getTest_result().getResult() == 2)
                {
                    Intent intent = new Intent(mContext, CheckStuWorkActivity.class);
                    intent.putExtra("nickname", items.getTitle());
                    intent.putExtra("resultId", items.getTest_result().getId() + "");
                    intent.putExtra("workType", 1);
                    intent.putExtra("status", "3");
                    intent.putExtra("stu", 1);//代表学生完成
                    intent.putExtra("isCourse", false);//
                    mContext.startActivity(intent);
                } else
                {
                    Intent intent = new Intent(mContext, TestPageActivity.class);
                    intent.putExtra("test_paper_id", items.getTest_paper_id() + "");
                    intent.putExtra("test_result_id", items.getTest_result().getId() + "");
                    intent.putExtra("limit_time", items.getLimit_time());
                    intent.putExtra("showType", TestPageActivity.TEST_PAGE_STATUS_TEST);
                    intent.putExtra("workType", 1);
                    intent.putExtra("isCourse", false);//
                    ((BaseActivity) mContext).startActivityForResult(intent, 2000);
                }
            }
        });

        getData();
    }

    private void getData()
    {

        new HttpGetBuilder(mContext)
                .setUrl(UrlsNew.SERVICE_MY_TEST)
                .setHttpResult(this)
                .setClassObj(ServiceMyTestResult.class)
                .addQueryParams("service_id", itemBean.getService_id() + "")
                .addQueryParams("page", "1")
                .addQueryParams("pageSize", "100")
                .addQueryParams("type", "1")
                .addQueryParams("only_pass", itemBean.getExamine() == 2 ? "1" : "0")
                .build();

    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {
        ServiceMyTestResult serviceMyTestResult = (ServiceMyTestResult) resultBean;

        data.addAll(serviceMyTestResult.getItems());
        baseRecyclerAdapter.notifyDataSetChanged();

        if (baseRecyclerAdapter.getShowCount() == 0)
        {
            myRecyclerView.setVisibility(View.GONE);
            all_passed.setVisibility(View.VISIBLE);
            all_passed.setText("暂无考核");
        } else
        {
            myRecyclerView.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {

    }
}
