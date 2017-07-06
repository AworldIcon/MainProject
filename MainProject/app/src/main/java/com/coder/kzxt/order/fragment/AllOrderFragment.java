package com.coder.kzxt.order.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.order.activity.OrderDetailActivity;
import com.coder.kzxt.order.beans.MyOrderBean;
import com.coder.kzxt.order.delegate.AllOrderDelegate;
import com.coder.kzxt.order.mInterface.OnAllOrderInterface;
import com.coder.kzxt.order.mInterface.OnOrderInterface;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.recyclerview.decoration.DividerItemDecoration;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.app.http.UrlsNew;

import java.util.ArrayList;
import java.util.List;

/**
 * 全部订单fragment
 * Created by wangtinshun on 2017/4/13.
 */

public class AllOrderFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        MyPullRecyclerView.OnAddMoreListener,OnOrderInterface{

    private Context mContext;
    private View rootView;
    private MyPullSwipeRefresh swipeRefresh;
    private MyPullRecyclerView recyclerView;
    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_order_layout;
    private PullRefreshAdapter pullRefreshAdapter;
    private AllOrderDelegate delegate;
    private SharedPreferencesUtil spu;
    private RelativeLayout myLayout;
    private Button loadFailBtn;//加载失败
    private List<MyOrderBean.OrderBean> data = new ArrayList<>();
    private ArrayList<MyOrderBean.OrderBean> items;
    private int pageNum ;
    private int pageIndex = 1;
    private int pageSize = 20;
    private Dialog asyDialog;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spu = new SharedPreferencesUtil(mContext);
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
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initData() {
        delegate = new AllOrderDelegate(mContext);
        delegate.setOrderPayStateListener(AllOrderFragment.this);
        pullRefreshAdapter = new PullRefreshAdapter(mContext, data, delegate);
        recyclerView.setAdapter(pullRefreshAdapter);
        pullRefreshAdapter.setSwipeRefresh(swipeRefresh);
    }

    private void initListener() {
        swipeRefresh.setOnRefreshListener(AllOrderFragment.this);
        recyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                pullRefreshAdapter.addPageIndex();
                pageIndex = pullRefreshAdapter.getPageIndex();
                if (pageIndex == pageNum) {
                    pullRefreshAdapter.setPullData(null);
                }
               getOrderList();
            }
        });

        //重新加载
        loadFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPage();
                getOrderList();
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
        getOrderList();
    }

    public void refreshOrderList() {
        getOrderList();
    }

    private void getOrderList() {
                new HttpGetBuilder(mContext)
                .setUrl(UrlsNew.GET_ORDER_LIST)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        cancleLoadDialog();
                        swipeRefresh.setRefreshing(false);
                        MyOrderBean orderBean = (MyOrderBean) resultBean;
                        items = orderBean.getItems();
                        if (items != null && items.size() > 0) {
                            visibleData();
                            pageNum = orderBean.getPaginate().getPageNum();
                            pullRefreshAdapter.setTotalPage(pageNum);
                            pullRefreshAdapter.setPullData(items);
                            pullRefreshAdapter.notifyDataSetChanged();
                            if (pageIndex == pageNum) {
                                if (!recyclerView.canScrollVertically(1)) {
//                                    ToastUtils.makeText(mContext, "无更多数据", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            noDataPage();
                        }
                        if (listener != null) {
                            listener.onCancleLoading();
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        swipeRefresh.setRefreshing(false);
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(AllOrderFragment.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(mContext, myLayout);
                        }
                        loadFailPage();
                        cancleLoadDialog();
                    }
                })
                .setClassObj(MyOrderBean.class)
                .addQueryParams("user_id",spu.getUid())
                .addQueryParams("page",String.valueOf(pageIndex))
                .addQueryParams("pageSize",String.valueOf(pageSize))
                .build();

    }

    @Override
    public void onRefresh() {
        getOrderList();
    }

    @Override
    public void addMoreListener() {

    }


    public void laodProgressDialog(){
        asyDialog = MyPublicDialog.createLoadingDialog(mContext);
        asyDialog.show();
    }

    /**
     * 取消加载框
     */
    private void cancleLoadDialog(){
        if(asyDialog != null && asyDialog.isShowing()){
            asyDialog.cancel();
        }
    }
    /**
     * 返回AllOrderDelegate
     * @return
     */
    public AllOrderDelegate getAllOrderDelegate(){
        return delegate;
    }

    @Override
    protected void onVisible() {
        super.onVisible();
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


    /**
     * 取消订单
     * @param bean
     * @param delegate
     */
    @Override
    public void onOrderCancle(MyOrderBean.OrderBean bean, PullRefreshDelegate delegate) {
        if(listener != null){
            listener.onAllOrderCancle(bean,delegate);
        }
    }

    /**
     * 立即支付
     * @param bean
     */
    @Override
    public void onOrderPay(MyOrderBean.OrderBean bean) {
        if(listener != null){
            listener.onAllOrderPay(bean,delegate);
        }
    }

    /**
     * 点击item
     * @param bean
     */
    @Override
    public void onOrderItem(MyOrderBean.OrderBean bean) {
        Intent intent = new Intent(mContext,OrderDetailActivity.class);
        intent.putExtra("orderBean",bean);
        startActivityForResult(intent,1);
    }

    private OnAllOrderInterface listener;

    public void setOnAllOrderInterface(OnAllOrderInterface allListener){
        this.listener = allListener;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
