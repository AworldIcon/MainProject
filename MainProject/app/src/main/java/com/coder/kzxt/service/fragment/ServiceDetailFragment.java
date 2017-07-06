package com.coder.kzxt.service.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.beans.CourseBean;
import com.coder.kzxt.base.delegate.TextImageDelegate;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.service.adapter.ServiceCourseDelegate;
import com.coder.kzxt.service.beans.ServiceBean;
import com.coder.kzxt.service.beans.ServiceCourseResult;
import com.coder.kzxt.service.beans.ServiceDetailResult;
import com.coder.kzxt.service.beans.ServiceTeacherResult;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.video.activity.VideoViewActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务详情下的服务详情页面
 */
public class ServiceDetailFragment extends BaseFragment implements HttpCallBack
{


    private static final int CODE_DETAIL = 1000;
    private static final int CODE_TEACHER = 1001;
    private static final int CODE_COURSE = 1002;

    private View view;
    private View mMainView;
    private ImageView mServiceImage;
    private TextView mServiceName;
    private TextView mServiceTeacher;
    private LinearLayout mCourseLy;
    private com.coder.kzxt.recyclerview.MyRecyclerView mCourseRecyclerView;
    private LinearLayout mDetailLy;
    private com.coder.kzxt.recyclerview.MyRecyclerView mParticularsRecyclerView;

    private ServiceBean serviceBean;
    private int classId;

    public static Fragment newInstance(ServiceBean serviceBean, int classId)
    {
        ServiceDetailFragment f = new ServiceDetailFragment();
        Bundle b = new Bundle();
        b.putSerializable("bean", serviceBean);
        b.putSerializable("classId", classId);
        f.setArguments(b);
        return f;
    }

    private void initVariable()
    {
        serviceBean = (ServiceBean) getArguments().getSerializable("bean");
        classId = getArguments().getInt("classId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_service_detail, container, false);

            //获取从其他页面获取的变量 getIntent
            initVariable();
            //初始化 view findviewbyid
            initView();
            //需要初始化的数据
            initData();
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
        mMainView = (View) view.findViewById(R.id.mainView);
        mServiceImage = (ImageView) view.findViewById(R.id.serviceImage);
        mServiceName = (TextView) view.findViewById(R.id.serviceName);
        mServiceTeacher = (TextView) view.findViewById(R.id.serviceTeacher);
        mCourseLy = (LinearLayout) view.findViewById(R.id.courseLy);
        mCourseRecyclerView = (com.coder.kzxt.recyclerview.MyRecyclerView) view.findViewById(R.id.courseRecyclerView);
        mDetailLy = (LinearLayout) view.findViewById(R.id.detailLy);
        mParticularsRecyclerView = (com.coder.kzxt.recyclerview.MyRecyclerView) view.findViewById(R.id.particularsRecyclerView);
        mParticularsRecyclerView.setNestedScrollingEnabled(false);
    }

    private void initData()
    {
        data = new ArrayList<>();
        courseAdapter = new BaseRecyclerAdapter(getActivity(), data, new ServiceCourseDelegate(getActivity()));
        mCourseRecyclerView.setAdapter(courseAdapter);

    }

    private void initClick()
    {
        courseAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

                CourseBean courseBean = data.get(position);
                Intent intent = new Intent(getActivity(), VideoViewActivity.class);
                intent.putExtra("flag", Constants.ONLINE);
                intent.putExtra("treeid", courseBean.getId());
                intent.putExtra("tree_name", courseBean.getTitle());
                intent.putExtra("pic", courseBean.getLarge_pic());
                intent.putExtra(Constants.IS_CENTER, "0");
                startActivity(intent);
            }
        });

    }

    @Override
    protected void requestData()
    {
        showLoadingView(mMainView);
        httpRequest();
    }

    private void httpRequest()
    {
        //获取详情
        new HttpGetBuilder(getActivity())
                .setUrl(UrlsNew.SERVICE)
                .setHttpResult(ServiceDetailFragment.this)
                .setClassObj(ServiceDetailResult.class)
                .setPath(serviceBean.getService_id() + "")
                .setRequestCode(CODE_DETAIL)
                .build();

        //获取老师列表
        new HttpGetBuilder(getActivity())
                .setUrl(UrlsNew.SERVICE_TEACHER)
                .setHttpResult(ServiceDetailFragment.this)
                .setClassObj(ServiceTeacherResult.class)
                .addQueryParams("service_id", serviceBean.getService_id() + "")
                .setRequestCode(CODE_TEACHER)
                .build();

        //获取配套课程
        new HttpGetBuilder(getActivity())
                .setUrl(UrlsNew.SERVICE_COURSE)
                .setHttpResult(ServiceDetailFragment.this)
                .setClassObj(ServiceCourseResult.class)
                .addQueryParams("id", serviceBean.getService_id() + "")
                .addQueryParams("status", "2")
                .setRequestCode(CODE_TEACHER)
                .setRequestCode(CODE_COURSE)
                .build();
    }

    private BaseRecyclerAdapter courseAdapter;
    private List<CourseBean> data;

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {

        if (requestCode == CODE_DETAIL)
        {
            hideLoadingView();
            ServiceDetailResult serviceDetailResult = (ServiceDetailResult) resultBean;
            serviceBean = serviceDetailResult.getItem();
            GlideUtils.loadCourseImg(getActivity(), serviceBean.getPicture(), mServiceImage);
            mServiceName.setText(serviceBean.getTitle());
            BaseRecyclerAdapter baseRecyclerAdapter = new BaseRecyclerAdapter( getActivity(), serviceBean.getDesc(), new TextImageDelegate(getActivity()));
            mParticularsRecyclerView.setAdapter(baseRecyclerAdapter);

        } else if (requestCode == CODE_TEACHER)
        {
            ServiceTeacherResult serviceTeacherResult = (ServiceTeacherResult) resultBean;
            mServiceTeacher.setText(getResources().getString(R.string.lecturer) + serviceTeacherResult.getTeacherName());
        } else if (requestCode == CODE_COURSE)
        {
            ServiceCourseResult serviceCourseResult = (ServiceCourseResult) resultBean;
            courseAdapter.resetData(serviceCourseResult.getItems());
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {
        hideLoadingView();
    }
}
