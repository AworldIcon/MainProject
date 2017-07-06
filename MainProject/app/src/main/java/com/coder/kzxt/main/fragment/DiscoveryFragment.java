package com.coder.kzxt.main.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.main.activity.MainActivity;
import com.coder.kzxt.main.adapter.DiscoveryAdapter;
import com.coder.kzxt.main.beans.DiscoveryBean;
import com.coder.kzxt.main.delegate.DiscoveryDelegate;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.decoration.DividerGridItemDecoration;
import com.coder.kzxt.recyclerview.layoutmanager.MyLinearLayoutManager;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.app.http.UrlsNew;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoveryFragment extends BaseFragment implements HttpCallBack, SwipeRefreshLayout.OnRefreshListener {

    private View v;
    private MainActivity mainActivity;
    private FrameLayout fragment_dis;
    private MyPullRecyclerView myPullRecyclerView;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private RecyclerView.ItemDecoration gridItemDecoration;
    private LinearLayout jiazai_layout;
    private LinearLayout no_info_layout;
    private LinearLayout load_fail_layout;
    private Button load_fail_button;
    private boolean listFirst = true;
    private boolean gridFirst = true;

    public static DiscoveryFragment newInstance(String param) {
        DiscoveryFragment fragment = new DiscoveryFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_discovery, container, false);
            fragment_dis = (FrameLayout) v.findViewById(R.id.fragment_dis);
            myPullSwipeRefresh = (MyPullSwipeRefresh) v.findViewById(R.id.myPullSwipeRefresh);
            myPullRecyclerView = (MyPullRecyclerView) v.findViewById(R.id.myPullRecyclerView);
            jiazai_layout = (LinearLayout) v.findViewById(R.id.jiazai_layout);
            jiazai_layout.setVisibility(View.VISIBLE);
            no_info_layout = (LinearLayout) v.findViewById(R.id.no_info_layout);
            load_fail_layout = (LinearLayout) v.findViewById(R.id.load_fail_layout);
            load_fail_button = (Button) v.findViewById(R.id.load_fail_button);
            gridItemDecoration = new DividerGridItemDecoration(getActivity(),10);
        }

        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null) {
            parent.removeView(v);
        }

        initListener();
        return v;
    }

    private void initData(){
        new HttpGetBuilder(getActivity())
                .setHttpResult(this)
                .setClassObj(DiscoveryBean.class)
                .setUrl(UrlsNew.APP_DISCOVER)
                .build();
    }


    private void initListener(){
        myPullSwipeRefresh.setOnRefreshListener(DiscoveryFragment.this);
        load_fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jiazai_layout.setVisibility(View.VISIBLE);
                load_fail_layout.setVisibility(View.GONE);
                initData();
            }
        });
    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constants.LOGIN_BACK){
            jiazai_layout.setVisibility(View.VISIBLE);
            initData();
        }
    }

    @Override
    protected void requestData() {
        initData();
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        jiazai_layout.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        myPullRecyclerView.setVisibility(View.VISIBLE);
        myPullSwipeRefresh.setRefreshing(false);
        DiscoveryBean discoveryBean = (DiscoveryBean) resultBean;
        //后台可能把条目全部移除
        if(discoveryBean.getItem().getModule() != null && discoveryBean.getItem().getModule().size() > 0) {
            no_info_layout.setVisibility(View.GONE);
            /**
             * 需求规定，九宫格形式中，不足9个的，要展示空白item，所以，判断添加空数据
             */
            if (discoveryBean.getItem().getStyle().equals("2")) {
                for (int i = 0; i <= discoveryBean.getItem().getModule().size(); i++) {
                    if (discoveryBean.getItem().getModule().size() - 3 * i == 1) {
                        discoveryBean.getItem().getModule().add(null);
                        discoveryBean.getItem().getModule().add(null);
                    } else if (discoveryBean.getItem().getModule().size() - 3 * i == 2) {
                        discoveryBean.getItem().getModule().add(null);
                    }
                }
            }
            DiscoveryDelegate discoveryDelegate = new DiscoveryDelegate(getActivity(), discoveryBean.getItem().getStyle(), discoveryBean.getItem().getModule());
            DiscoveryAdapter discoveryAdapter = new DiscoveryAdapter(getActivity(), discoveryBean.getItem().getModule(), discoveryDelegate);
            myPullRecyclerView.setAdapter(discoveryAdapter);

            if (discoveryBean.getItem().getStyle().equals("1")) {
                        myPullRecyclerView.setLayoutManager(new MyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        if (listFirst) {
                            listFirst = false;
                        }
                    } else if (discoveryBean.getItem().getStyle().equals("2")) {
                        myPullRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false));
                        if (gridFirst) {
                            myPullRecyclerView.addItemDecoration(gridItemDecoration);
                            gridFirst = false;
                        }
               }

        }else {
            myPullRecyclerView.setVisibility(View.GONE);
            no_info_layout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        jiazai_layout.setVisibility(View.GONE);
        myPullSwipeRefresh.setRefreshing(false);
        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
               //重新登录
                myPullRecyclerView.setVisibility(View.GONE);
                NetworkUtil.httpRestartLogin(DiscoveryFragment.this, fragment_dis);
            }else{
                 load_fail_layout.setVisibility(View.VISIBLE);
                 myPullRecyclerView.setVisibility(View.GONE);
                 NetworkUtil.httpNetErrTip(mainActivity,fragment_dis);
            }

        }

}
