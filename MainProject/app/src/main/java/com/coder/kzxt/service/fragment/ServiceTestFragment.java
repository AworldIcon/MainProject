package com.coder.kzxt.service.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.login.activity.LoginActivity;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.service.adapter.ServiceTestDelegate;
import com.coder.kzxt.service.beans.ServiceFilesResult;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务详情下的练习资料页面
 */
public class ServiceTestFragment extends BaseFragment implements HttpCallBack
{


    private RelativeLayout mainView;
    //下拉刷新
    private MyPullSwipeRefresh myStagePullSwipeRefresh;
    private MyPullRecyclerView myStagePullRecyclerView;
    private LinearLayout no_info_layout;
    private ImageView no_info_img;
    private TextView no_info_text;

    private ServiceTestDelegate pullDelegate;
    private PullRefreshAdapter pullRefreshAdapter;

    private List<ServiceFilesResult.Item> data = new ArrayList<>();

    private int pageSize = 20;// 每页请求条数

    private void initVariable()
    {
        Bundle args = getArguments();
        serviceId = args.getString("serviceId");
        memberedId = args.getInt("memberedId", -1);
    }

    private String serviceId;
    private int memberedId;
    private View view;


    public static Fragment newInstance(String serviceId, int memberedId)
    {
        ServiceTestFragment f = new ServiceTestFragment();
        Bundle b = new Bundle();
        b.putString("serviceId", serviceId);
        b.putInt("memberedId", memberedId);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_common_pull_recyclerview, container, false);

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

        no_info_layout = (LinearLayout) view.findViewById(R.id.no_info_layout);
        no_info_img = (ImageView) view.findViewById(R.id.no_info_img);
        no_info_text = (TextView) view.findViewById(R.id.no_info_text);
        mainView = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        myStagePullSwipeRefresh = (MyPullSwipeRefresh) view.findViewById(R.id.pullSwipeRefresh);
        myStagePullRecyclerView = (MyPullRecyclerView) view.findViewById(R.id.pullRecyclerView);

//        no_info_img.setImageResource(R.drawable.empty_live);
        no_info_text.setText("暂无资料");

    }

    private void initData()
    {
        //stage的初始化
        pullDelegate = new ServiceTestDelegate(getActivity());
        pullRefreshAdapter = new PullRefreshAdapter(getActivity(), data, pullDelegate);
        myStagePullRecyclerView.setAdapter(pullRefreshAdapter);
        pullRefreshAdapter.setSwipeRefresh(myStagePullSwipeRefresh);
    }

    private void initClick()
    {

        myStagePullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                pullRefreshAdapter.resetPageIndex();
                httpRequest();

            }
        });

        myStagePullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener()
        {
            @Override
            public void addMoreListener()
            {
                pullRefreshAdapter.addPageIndex();
                httpRequest();
            }
        });

        pullRefreshAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

                if (TextUtils.isEmpty(new SharedPreferencesUtil(getActivity()).getIsLogin()))
                {
                    getActivity().startActivityForResult(new Intent(getActivity(), LoginActivity.class), 1000);
                } else if (memberedId == -1)
                {
                    ToastUtils.makeText(getActivity(), getResources().getString(R.string.plase_join_service));
                } else
                {
                    ToastUtils.makeText(getActivity(), getResources().getString(R.string.please_go_pc_download));
                }

            }
        });

    }

    @Override
    protected void requestData()
    {
        showLoadingView(mainView);
        httpRequest();
    }

    private void httpRequest()
    {

        //获取直播
        new HttpGetBuilder(getActivity())
                .setHttpResult(this)
                .setClassObj(ServiceFilesResult.class)
                .addQueryParams("page", pullRefreshAdapter.getPageIndex() + "")
                .addQueryParams("pageSize", pageSize + "")
                .addQueryParams("service_id", serviceId)
                .addQueryParams("type", "0")
                .setUrl(UrlsNew.SERVICE_FILES)
                .build();

    }


    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {
        hideLoadingView();

        ServiceFilesResult liveListResult = (ServiceFilesResult) resultBean;
        pullRefreshAdapter.setTotalPage(liveListResult.getPaginate().getPageNum());
        pullRefreshAdapter.setPullData(liveListResult.getItems());
        if (pullRefreshAdapter.getShowCount() == 0)
        {
            no_info_layout.setVisibility(View.VISIBLE);
        } else
        {
            no_info_layout.setVisibility(View.GONE);
        }

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {
        hideLoadingView();
    }
}
