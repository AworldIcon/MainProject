package com.coder.kzxt.service.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.service.activity.FindJobActivity1;
import com.coder.kzxt.service.activity.FindJobActivity3;
import com.coder.kzxt.service.beans.ServiceFormResult;
import com.coder.kzxt.service.beans.ServiceMemberResult;
import com.coder.kzxt.service.views.FindJobView1;
import com.coder.kzxt.service.views.FindJobView2;
import com.coder.kzxt.service.views.FindJobView3;
import com.coder.kzxt.service.views.FindJobView4;
import com.coder.kzxt.service.views.FindJobView5;
import com.coder.kzxt.service.views.SignUpView;

/**
 * 服务详情下的联系资料页面
 */
public class ServiceCertifyFragment extends BaseFragment
{

    private View view;
     private LinearLayout mainLy;
    private TextView form;
    private ServiceMemberResult.ItemBean itemBean;

    public static Fragment newInstance(ServiceMemberResult.ItemBean itemBean)
    {
        ServiceCertifyFragment f = new ServiceCertifyFragment();
        Bundle b = new Bundle();
        b.putSerializable("bean", itemBean);
        f.setArguments(b);
        return f;
    }

    private void initVariable()
    {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        itemBean = (ServiceMemberResult.ItemBean) getArguments().getSerializable("bean");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_service_certify, container, false);
            //获取从其他页面获取的变量 getIntent
            initVariable();
            //初始化 view findviewbyid
            initView();
            //响应事件click
            initClick();
        }

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null)
        {
            parent.removeView(view);
        }


        return view;
    }

    // 控件
    private void initView()
    {
         mainLy = (LinearLayout) view.findViewById(R.id.linearLayout);
         form = (TextView) view.findViewById(R.id.form);

//        "job_register": "就业登记状态：0未开启 1等待 2通过 3失败",

        if (itemBean.getJob_register() == 0)
        {
            form.setText("填写就业登记表");
        } else if (itemBean.getJob_register() == 1)
        {
            form.setText("就业登记表已提交，等待审核");
        }else if (itemBean.getJob_register() == 2)
        {
            form.setText("就业登记表审核通过");
        }else if (itemBean.getJob_register() == 3)
        {
            form.setText("就业登记表审核未通过，请重新填写");
        }

    }

    public void setFormWait(){
        form.setText("就业登记表已提交，等待审核");
        itemBean.setJob_register(1);
    }


    private void initClick()
    {

        form.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                "job_register": "就业登记状态：0未开启 1等待 2通过 3失败",
                if (itemBean.getJob_register() == 0)
                {
                    FindJobActivity1.gotoActivity(getActivity(), new ServiceFormResult.ItemBean(itemBean.getId()+""),itemBean.getJob_register());
                } else
                {
                    FindJobActivity3.gotoActivity(getActivity(), itemBean);
                }

            }
        });
    }

    @Override
    public void requestData()
    {

        //   "type": "服务类型 1就业服务 0认证服务",
        //   包过  认证服务
        //   不  	 报名考试
        //   就业服务

        int childCount = mainLy.getChildCount();
        if (childCount > 1)
        {
            mainLy.removeViews(1, childCount-1);
        }
        // 0 报名考试 1 就业服务 2 认证服务
        if (itemBean.getMyType() == 0)
        {
            mainLy.addView(new SignUpView(getActivity(), itemBean).getView());
        } else if (itemBean.getMyType() == 2)
        {
            mainLy.addView(new FindJobView1(getActivity(), itemBean).getView());
            mainLy.addView(new SignUpView(getActivity(), itemBean).getView());
            mainLy.addView(new FindJobView5(getActivity(), itemBean).getView());
        } else
        {
            mainLy.addView(new FindJobView1(getActivity(), itemBean).getView());
            mainLy.addView(new FindJobView2(getActivity(), itemBean).getView());
            mainLy.addView(new FindJobView3(getActivity(), itemBean).getView());
            mainLy.addView(new FindJobView4(getActivity(), itemBean).getView());
            mainLy.addView(new FindJobView5(getActivity(), itemBean).getView());
            form.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
