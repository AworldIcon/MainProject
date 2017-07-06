package com.coder.kzxt.order.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.order.beans.MyOrderBean;
import com.coder.kzxt.order.delegate.DoneOrderDelegate;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.app.http.UrlsNew;

import java.util.ArrayList;
import java.util.List;

/**
 * 已完成的订单
 * Created by wangtingshun on 2017/4/13.
 */
public class DoneFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        MyPullRecyclerView.OnAddMoreListener{

    private Context mContext;
    private View rootView;
    private MyPullSwipeRefresh swipeRefresh;
    private MyPullRecyclerView recyclerView;
    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_order_layout;
    private PullRefreshAdapter pullRefreshAdapter;
    private DoneOrderDelegate delegate;
    private SharedPreferencesUtil spu;
    private RelativeLayout myLayout;
    private Button loadFailBtn;//加载失败
    private List<MyOrderBean.OrderBean> data = new ArrayList<>();
    public static final String DONE = "done";
    private int pageNum ;
    private int pageIndex = 1;
    private int pageSize = 20;

    private String status;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spu = new SharedPreferencesUtil(mContext);
        Bundle bundle = getArguments();
        status = bundle.getString("done","");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.my_order_fragment_layout, container, false);
            initView();
        }
        initData();
        initListener();
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(parent);
        }
        return rootView;
    }

    private void initView() {
        recyclerView = (MyPullRecyclerView) rootView.findViewById(R.id.recyclerView);
        swipeRefresh = (MyPullSwipeRefresh) rootView.findViewById(R.id.myPullSwipeRefresh);
        jiazai_layout = (LinearLayout) rootView.findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout) rootView.findViewById(R.id.load_fail_layout);
        no_order_layout = (LinearLayout) rootView.findViewById(R.id.no_order_info);
        myLayout = (RelativeLayout) rootView.findViewById(R.id.rl_layout);
        loadFailBtn = (Button) rootView.findViewById(R.id.load_fail_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initData() {
        delegate = new DoneOrderDelegate(mContext);
        pullRefreshAdapter = new PullRefreshAdapter(mContext, data, delegate);
        recyclerView.setAdapter(pullRefreshAdapter);
        pullRefreshAdapter.setSwipeRefresh(swipeRefresh);
    }

    private void initListener() {
        swipeRefresh.setOnRefreshListener(DoneFragment.this);
        recyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                pullRefreshAdapter.addPageIndex();
                pageIndex = pullRefreshAdapter.getPageIndex();
                if (pageIndex == pageNum) {
                    pullRefreshAdapter.setPullData(null);
                }
                getOrderList(status);
            }
        });

        //重新加载
        loadFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPage();
                getOrderList(status);
            }
        });

        pullRefreshAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }



    @Override
    protected void requestData() {
        loadingPage();
        getOrderList(status);
    }

    private void getOrderList(String status) {
        new HttpGetBuilder(mContext)
                .setUrl(UrlsNew.GET_ORDER_LIST)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        swipeRefresh.setRefreshing(false);
                        MyOrderBean orderBean = (MyOrderBean) resultBean;
                        ArrayList<MyOrderBean.OrderBean> items = orderBean.getItems();
                        if(items != null && items.size() > 0){
                            visibleData();
                            pageNum = orderBean.getPaginate().getPageNum();
                            pullRefreshAdapter.setTotalPage(pageNum);
                            pullRefreshAdapter.setPullData(items);
                            pullRefreshAdapter.notifyDataSetChanged();
                            if(pageIndex == pageNum){
                                if(!recyclerView.canScrollVertically(1)){
//                                    ToastUtils.makeText(mContext, "无更多数据", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            noDataPage();
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        swipeRefresh.setRefreshing(false);
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(DoneFragment.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(mContext, myLayout);
                        }
                        loadFailPage();
                    }
                })
                .setClassObj(MyOrderBean.class)
                .addQueryParams("status",status)
                .addQueryParams("user_id",spu.getUid())
                .addQueryParams("page",String.valueOf(pageIndex))
                .addQueryParams("pageSize",String.valueOf(pageSize))
                .build();

    }

    @Override
    public void onRefresh() {
        getOrderList(status);
    }

    @Override
    public void addMoreListener() {

    }

    /**
     * 获取完成的delegate
     * @return
     */
    public DoneOrderDelegate getDoneDelegate(){
        return delegate;
    }

    /**
     * 暂无数据
     */
    private void noDataPage() {
        load_fail_layout.setVisibility(View.GONE);
        swipeRefresh.setVisibility(View.GONE);
        jiazai_layout.setVisibility(View.GONE);
        no_order_layout.setVisibility(View.VISIBLE);
    }

    /**
     * 加载失败页
     */
    private void loadFailPage() {
        load_fail_layout.setVisibility(View.VISIBLE);
        swipeRefresh.setVisibility(View.GONE);
        jiazai_layout.setVisibility(View.GONE);
        no_order_layout.setVisibility(View.GONE);
    }

    /**
     * 显示数据
     */
    private void visibleData() {
        jiazai_layout.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        no_order_layout.setVisibility(View.GONE);
        swipeRefresh.setVisibility(View.VISIBLE);
    }

    /**
     * 显示加载页
     */
    private void loadingPage() {
        jiazai_layout.setVisibility(View.VISIBLE);
        swipeRefresh.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        no_order_layout.setVisibility(View.GONE);
    }
}
