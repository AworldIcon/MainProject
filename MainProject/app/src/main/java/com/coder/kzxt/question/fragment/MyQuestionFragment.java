package com.coder.kzxt.question.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.app.http.HttpGetOld;
import com.app.http.InterfaceHttpResult;
import com.app.utils.L;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.question.activity.MyQuestionActivity;
import com.coder.kzxt.question.adapter.QuestionListDelegate;
import com.coder.kzxt.question.beans.QuestionBean;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.PermissionsUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.Urls;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的问答列表
 */
public class MyQuestionFragment extends BaseFragment implements InterfaceHttpResult
{
    private MyQuestionActivity myQuestionActivity;
    //搜索的点击跳转区域
    private RelativeLayout relativeLayout;
    //下拉刷新 瀑布流和普通的
    private MyPullSwipeRefresh pullSwipeRefresh;
    private MyPullRecyclerView pullRecyclerView;

    private QuestionListDelegate orderListStageDelegate;
    private PullRefreshAdapter stageadapter;

    private List<QuestionBean.DataBean.QuestionListBean> data = new ArrayList<>();

    private int pageSize = 20;// 每页请求条数

    private SharedPreferencesUtil sharePreference;
    private PermissionsUtil permissionsUtil;
    private String type;
    private String publicCourse;

    private void initVariable()
    {
        sharePreference = new SharedPreferencesUtil(getActivity());
        type = getArguments().getString("Type");
        publicCourse = getArguments().getString("publicCourse");
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

        L.d(" fragment 1 onCreateView ");


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
        orderListStageDelegate = new QuestionListDelegate(getActivity());
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


    private void httpRequest()
    {
        new HttpGetOld(getActivity(), getActivity(), MyQuestionFragment.this, QuestionBean.class, Urls.GET_MY_QUESTION_LIST, type, stageadapter.getPageIndex() + "", pageSize + "", publicCourse).excute();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        myQuestionActivity = (MyQuestionActivity) context;

    }

    @Override
    public void getCallback(int requestCode, int code, String msg, Object baseBean)
    {
        hideLoadingView();
        if (code == Constants.HTTP_CODE_SUCCESS)
        {
            QuestionBean postersBeanResult = (QuestionBean) baseBean;
            List<QuestionBean.DataBean.QuestionListBean> beans = postersBeanResult.getData().getQuestionList();
            stageadapter.setTotalPage(postersBeanResult.getTotalPages());
            stageadapter.setPullData(beans);

            if (stageadapter.getShowCount() == 0)
            {
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

        } else if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
        {
            //重新登录
            NetworkUtil.httpRestartLogin(MyQuestionFragment.this, relativeLayout);
        } else
        {
            L.d("loadfail " + code);
            //加载失败
            showLoadFailView(view);
            NetworkUtil.httpNetErrTip(myQuestionActivity, relativeLayout);
        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (Constants.RESTART_LOGIN == requestCode && resultCode == Constants.LOGIN_BACK)
//        if (requestCode == 4 && resultCode == 4)
        {
            showLoadingView(view);
            stageadapter.resetPageIndex();
            httpRequest();
        } else if (requestCode == 4 && resultCode == 5)
        {
//            // 如果喜欢 评论 发生变化 进行修改
//            int position = data.getIntExtra("position", 0);
//            PostersBean bean = (PostersBean) data.getSerializableExtra("bean");
//            postersWaterAdapter.setIteam(position, bean);
//            postersListAdapter.setIteam(position, bean);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setPublicCourse(String publicCourse)
    {
        if (!this.publicCourse.equals(publicCourse))
        {
            this.publicCourse = publicCourse;
            this.stageadapter.resetPageIndex();
            if (getUserVisibleHint())
            {
                requestData();
            } else
            {
                isRequstData = false;
            }
        }
    }
}
