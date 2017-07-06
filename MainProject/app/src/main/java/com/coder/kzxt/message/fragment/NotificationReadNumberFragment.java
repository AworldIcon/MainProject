package com.coder.kzxt.message.fragment;

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
import com.coder.kzxt.message.adapter.NotificationNumberDelegate;
import com.coder.kzxt.message.beans.NotificationReadNumBean;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw on 2017/6/24
 */

public class NotificationReadNumberFragment extends BaseFragment
{
    private View mainView;

    //下拉刷新 瀑布流和普通的
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;

    private NotificationNumberDelegate orderListStageDelegate;
    private NotificationNumberDelegate orderListStageDelegate1;
    private PullRefreshAdapter pullRefreshAdapter;
    private List<NotificationReadNumBean.ItemBean.ReadBean> readBeanList = new ArrayList<>();
    private List<NotificationReadNumBean.ItemBean.UnReadBean> unReadBeanList = new ArrayList<>();
    private String type;
    private int notifyId;
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
        type = getArguments().getString("type");
        notifyId=getArguments().getInt("notifyId",0);

    }

    private void initView()
    {
        myPullSwipeRefresh = (MyPullSwipeRefresh) mainView.findViewById(R.id.myStagePullSwipeRefresh);
        myPullRecyclerView = (MyPullRecyclerView) mainView.findViewById(R.id.myStagePullRecyclerView);
    }

    private void initData()
    {
        //stage的初始化
        orderListStageDelegate = new NotificationNumberDelegate(getActivity(),type);
        orderListStageDelegate1 = new NotificationNumberDelegate(getActivity(),type);
        if(type.equals("1")){
            pullRefreshAdapter = new PullRefreshAdapter(getActivity(), readBeanList, orderListStageDelegate);
        }else {
            pullRefreshAdapter = new PullRefreshAdapter(getActivity(), unReadBeanList, orderListStageDelegate1);
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


        pullRefreshAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
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
        //网络请求
        ((BaseActivity) getActivity()).showLoadingView();
        httpRequest();
    }

    private void httpRequest()
    {
        new HttpGetBuilder(getActivity()).setUrl(UrlsNew.GET_NOTIFY_READ).setClassObj(NotificationReadNumBean.class).setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                ((BaseActivity) getActivity()).hideLoadingView();
                NotificationReadNumBean readNumBean = (NotificationReadNumBean) resultBean;
                pullRefreshAdapter.setTotalPage(1);
                if(type.equals("1")){
                    pullRefreshAdapter.setPullData(readNumBean.getItem().getRead());
                }else {
                    pullRefreshAdapter.setPullData(readNumBean.getItem().getUn_read());
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
                    NetworkUtil.httpRestartLogin(NotificationReadNumberFragment.this, mainView);
                } else
                {
                    //加载失败
                    showLoadFailView(mainView);
                    NetworkUtil.httpNetErrTip(getActivity(), mainView);
                    setOnLoadFailClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hideLoadFailView();
                            requestData();
                        }
                    });
                }
            }
        }).setPath(""+notifyId).build();
    }

}
