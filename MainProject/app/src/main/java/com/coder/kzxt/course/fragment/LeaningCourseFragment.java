package com.coder.kzxt.course.fragment;

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
import com.coder.kzxt.course.beans.LearnBean;
import com.coder.kzxt.course.delegate.LearnListDelegate;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.app.http.UrlsNew;
import com.coder.kzxt.video.activity.VideoViewActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 在学课程
 */

public class LeaningCourseFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
                              MyPullRecyclerView.OnAddMoreListener {

    private Context mContext;
    private View rootView;
    private MyPullSwipeRefresh swipeRefresh;
    private MyPullRecyclerView recyclerView;
    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private PullRefreshAdapter pullRefreshAdapter;
    private LearnListDelegate delegate;
    /** 是否已经请求了网络数据 */
    private boolean isPrepared = false;
    /**是否加载过数据*/
    private boolean hasLoad = false;
    private SharedPreferencesUtil spu;
    private RelativeLayout myLayout;
    private Button loadFailBtn;//加载失败
    private List<LearnBean.ItemsBean> data = new ArrayList<>();
    private int pageNum ;
    private int pageIndex = 1;

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
            rootView = inflater.inflate(R.layout.activity_course_and_class_layout, container, false);
            initView();
        }
        isPrepared = true;
        initData();
        initListener();
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(parent);
        }
        return rootView;
    }

    private void initData() {
        delegate = new LearnListDelegate(mContext,"");
        pullRefreshAdapter = new PullRefreshAdapter(mContext, data, delegate);
        recyclerView.setAdapter(pullRefreshAdapter);
        pullRefreshAdapter.setSwipeRefresh(swipeRefresh);
    }

    private void initView() {
        recyclerView = (MyPullRecyclerView) rootView.findViewById(R.id.recyclerView);
        swipeRefresh = (MyPullSwipeRefresh) rootView.findViewById(R.id.myPullSwipeRefresh);
        jiazai_layout = (LinearLayout) rootView.findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout) rootView.findViewById(R.id.load_fail_layout);
        no_info_layout = (LinearLayout) rootView.findViewById(R.id.no_info_layout);
        myLayout = (RelativeLayout) rootView.findViewById(R.id.rl_layout);
        loadFailBtn = (Button) rootView.findViewById(R.id.load_fail_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initListener() {
        swipeRefresh.setOnRefreshListener(LeaningCourseFragment.this);
        recyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                pullRefreshAdapter.addPageIndex();
                pageIndex = pullRefreshAdapter.getPageIndex();
                if (pageIndex == pageNum) {
                    pullRefreshAdapter.setPullData(null);
                }
                getLeanCourse();
            }
        });

        //重新加载
        loadFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPage();
                getLeanCourse();
            }
        });

        pullRefreshAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LearnBean.ItemsBean itemsBean = data.get(position);
                Intent intent = new Intent(mContext, VideoViewActivity.class);
                intent.putExtra("flag", Constants.ONLINE);
                intent.putExtra("treeid", itemsBean.getId());
                intent.putExtra("tree_name", itemsBean.getTitle());
                intent.putExtra("pic", itemsBean.getMiddle_pic());
                intent.putExtra(Constants.IS_CENTER, "0");
                startActivity(intent);
            }
        });

    }


    @Override
    protected void requestData() {
        loadingPage();
        getLeanCourse();
    }

    /**
     * 获取在学课程
     */
    private void getLeanCourse() {
                new HttpGetBuilder(mContext)
                .setUrl(UrlsNew.GET_COURSE_LEANING)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        swipeRefresh.setRefreshing(false);
                        LearnBean learnBean = (LearnBean) resultBean;
                        List<LearnBean.ItemsBean> items = learnBean.getItems();
                        if (items != null && items.size() > 0) {
                            visibleData();
                            pageNum = learnBean.getPaginate().getPageNum();
                            pullRefreshAdapter.setTotalPage(pageNum);
                            pullRefreshAdapter.setPullData(items);
                            pullRefreshAdapter.notifyDataSetChanged();
                        } else {
                            noDataPage();
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(LeaningCourseFragment.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(mContext, myLayout);
                        }
                        loadFailPage();
                    }
                })
                .setClassObj(LearnBean.class)
                .addQueryParams("user_id", spu.getUid())
                .addQueryParams("status", "2")
                .addQueryParams("page", String.valueOf(pageIndex))
                .addQueryParams("orderBy","create_time desc")
                .addQueryParams("pageSize","20")
                .build();
    }



    @Override
    public void onRefresh() {
        getLeanCourse();
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 加载更多
     */
    @Override
    public void addMoreListener() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 暂无数据
     */
    private void noDataPage() {
        load_fail_layout.setVisibility(View.GONE);
        swipeRefresh.setVisibility(View.GONE);
        jiazai_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.VISIBLE);
    }

    /**
     * 加载失败页
     */
    private void loadFailPage() {
        load_fail_layout.setVisibility(View.VISIBLE);
        swipeRefresh.setVisibility(View.GONE);
        jiazai_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
    }

    /**
     * 显示数据
     */
    private void visibleData() {
        jiazai_layout.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
        swipeRefresh.setVisibility(View.VISIBLE);
    }

    /**
     * 显示加载页
     */
    private void loadingPage() {
        jiazai_layout.setVisibility(View.VISIBLE);
        swipeRefresh.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
    }

}
