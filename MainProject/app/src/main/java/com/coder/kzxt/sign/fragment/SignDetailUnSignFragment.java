package com.coder.kzxt.sign.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.sign.adapter.SignDetailTeacherDelegate;
import com.coder.kzxt.sign.beans.SignInfoResult;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.PermissionsUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 签到详情列表 未签到
 *
 */
public class SignDetailUnSignFragment extends BaseFragment implements HttpCallBack
{

    private BaseActivity myActivity;
    //
    private RelativeLayout relativeLayout;

    private MyPullSwipeRefresh pullSwipeRefresh;
    private MyPullRecyclerView pullRecyclerView;

    private SignDetailTeacherDelegate orderListStageDelegate;
    private PullRefreshAdapter stageadapter;

    private List<SignInfoResult.SignInfoBean> data = new ArrayList<>();

    private int pageSize = 20;// 每页请求条数

    private SharedPreferencesUtil sharePreference;
    private PermissionsUtil permissionsUtil;
    private String course_id;
    private String class_id;
    private String signId;
    private int type;


    private void initVariable()
    {
        sharePreference = new SharedPreferencesUtil(getActivity());
        course_id = getArguments().getString("courseId");
        class_id = getArguments().getString("classId");
        signId = getArguments().getString("signId");
        type =0;
    }


    private View view;

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
        pullSwipeRefresh = (MyPullSwipeRefresh) view.findViewById(R.id.pullSwipeRefresh);
        pullRecyclerView = (MyPullRecyclerView) view.findViewById(R.id.pullRecyclerView);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
    }

    private void initData()
    {
        //stage的初始化
        orderListStageDelegate = new SignDetailTeacherDelegate(getActivity(),signId);
        stageadapter = new PullRefreshAdapter(getActivity(), data, orderListStageDelegate);
        pullRecyclerView.setAdapter(stageadapter);
        stageadapter.setSwipeRefresh(pullSwipeRefresh);

    }

    private void initClick()
    {

        pullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                stageadapter.resetPageIndex();
                httpRequest();

            }
        });

        pullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener()
        {
            @Override
            public void addMoreListener()
            {
                stageadapter.addPageIndex();
                httpRequest();
            }

        });


        stageadapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

             }
        });

    }


    @Override
    protected void requestData()
    {

        showLoadingView(view);
        //网络请求
        httpRequest();

    }


    public void httpRequest()
    {
        new HttpGetBuilder(getActivity()).setHttpResult(this)
                .setUrl(UrlsNew.SIGN_INFO)
                .setClassObj(SignInfoResult.class)
                .addQueryParams("page",stageadapter.getPageIndex()+"")
                .addQueryParams("pageSize",pageSize+"")
                .addQueryParams("course_id",course_id)
                .addQueryParams("class_id",class_id)
                .addQueryParams("sign_id",signId)
                .addQueryParams("type",type+"")
                .build();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        myActivity = (BaseActivity) context;

    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {
        hideLoadingView();

        SignInfoResult postersBeanResult = (SignInfoResult) resultBean;
        List<SignInfoResult.SignInfoBean> beans = postersBeanResult.getItems();
        stageadapter.setTotalPage(postersBeanResult.getPaginate().getPageNum());
        stageadapter.setPullData(beans);

        if (stageadapter.getShowCount() == 0)
        {
            isRequstData =false;
            showEmptyView(view);
            setOnEmptyClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    requestData();
                }
            });
        } else
        {
            hideEmptyView();
        }

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {
        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
        {
            //重新登录
            NetworkUtil.httpRestartLogin(SignDetailUnSignFragment.this, relativeLayout);
        } else
        {
            //加载失败
            showLoadFailView(view);
            NetworkUtil.httpNetErrTip(myActivity, relativeLayout);
        }

    }
}
