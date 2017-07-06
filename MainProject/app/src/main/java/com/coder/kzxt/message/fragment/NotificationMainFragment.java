package com.coder.kzxt.message.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.message.activity.NotificationDetailActivity;
import com.coder.kzxt.message.activity.NotificationListActivity;
import com.coder.kzxt.message.adapter.NoticePublishDelegate;
import com.coder.kzxt.message.beans.NotificationListBean;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import java.util.ArrayList;
import java.util.List;

public class NotificationMainFragment extends BaseFragment
{
    private View mainView;

    //下拉刷新 瀑布流和普通的
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;

    private NoticePublishDelegate noticePublishDelegate;
    private PullRefreshAdapter pullRefreshAdapter;


    private List<NotificationListBean.ItemsBean> pushBeen = new ArrayList<>();
    private List<NotificationListBean.ItemsBean> receiveBeen = new ArrayList<>();
    private int type;
    private NotificationListBean listBean;
    private NotificationListActivity listActivity;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listActivity= (NotificationListActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (mainView == null)
        {
            mainView = inflater.inflate(R.layout.fragment_notificattion, container, false);

            //获取从其他页面获取的变量 getIntent
            initVariable();
            //初始化 view findviewbyid
            initView();
            //需要初始化的数据
            initData();
            //响应事件click
            initClick();

        }

        ViewGroup parent = (ViewGroup) mainView.getParent();
        if (parent != null)
        {
            parent.removeView(mainView);
        }


        return mainView;
    }


    private void initVariable()
    {
        type = getArguments().getInt("type");
    }

    private void initView()
    {
        myPullSwipeRefresh = (MyPullSwipeRefresh) mainView.findViewById(R.id.myStagePullSwipeRefresh);
        myPullRecyclerView = (MyPullRecyclerView) mainView.findViewById(R.id.myStagePullRecyclerView);
    }

    private void initData()
    {
        //stage的初始化
        noticePublishDelegate = new NoticePublishDelegate(getActivity(),type);
        if(type==2){
            pullRefreshAdapter = new PullRefreshAdapter(getActivity(), receiveBeen, noticePublishDelegate);
        }else {
            pullRefreshAdapter = new PullRefreshAdapter(getActivity(), pushBeen, noticePublishDelegate);
        }
        myPullRecyclerView.setAdapter(pullRefreshAdapter);
        pullRefreshAdapter.setSwipeRefresh(myPullSwipeRefresh);
    }

    private void initClick()
    {
        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                pullRefreshAdapter.resetPageIndex();
                httpRequest();

            }
        });

        myPullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener()
        {
            @Override
            public void addMoreListener()
            {
                pullRefreshAdapter.addPageIndex();
                httpRequest();
            }

        });

    }


    @Override
    protected void requestData()
    {
        //网络请求
        ((BaseActivity) getActivity()).showLoadingView();
        httpRequest();
    }

    private void httpRequest()
    {
        new HttpGetBuilder(getContext()).setUrl(UrlsNew.GET_NOTIFY_READ).setClassObj(NotificationListBean.class).setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                ((BaseActivity) getActivity()).hideLoadingView();

                listBean = (NotificationListBean) resultBean;
                    pullRefreshAdapter.setTotalPage(1);
                if(type==2){
                    pullRefreshAdapter.setPullData(listBean.getItems());
                    if(listBean.getItems().size()==0){
                        listActivity.menuItem.setVisible(false);
                    }else {
                        listActivity.menuItem.setVisible(true);
                    }
                }else {
                    pullRefreshAdapter.setPullData(listBean.getItems());
                }
                    if (pullRefreshAdapter.getShowCount() == 0)
                    {
                        showEmptyView(mainView);
                    } else
                    {
                        hideEmptyView();
                    }
            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {
                ((BaseActivity) getActivity()).hideLoadingView();
                if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
                {
                    //重新登录
                    NetworkUtil.httpRestartLogin(NotificationMainFragment.this, mainView);
                } else
                {
                    //加载失败
                    showLoadFailView(mainView);
                    NetworkUtil.httpNetErrTip(getActivity(), mainView);
                    setOnLoadFailClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           hideLoadFailView();
                            requestData();
                        }
                    });
                }
            }
        }).addQueryParams("type",type+"").build();
    }
    public String getIds(){
        String ids="";
        for (int i=0;i<listBean.getItems().size();i++){
            ids+=listBean.getItems().get(i).getId()+",";
        }
    return ids;
    }
    public void  refreshData(){
        pullRefreshAdapter.resetPageIndex();
        requestData();
    }
}
