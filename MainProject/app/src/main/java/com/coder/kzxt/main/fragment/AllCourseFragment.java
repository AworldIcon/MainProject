package com.coder.kzxt.main.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
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
import com.coder.kzxt.main.activity.MainActivity;
import com.coder.kzxt.main.activity.SearchAllActivity;
import com.coder.kzxt.main.beans.DepartmentBean;
import com.coder.kzxt.main.delegate.FirstLevelItem;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.TreeRecyclerViewAdapter;
import com.coder.kzxt.recyclerview.adapter.TreeRecyclerViewType;
import com.coder.kzxt.recyclerview.decoration.DividerItemDecoration;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.app.http.UrlsNew;
import com.coder.kzxt.views.RecyclerViewHeader;
import java.util.ArrayList;


public class AllCourseFragment extends BaseFragment implements HttpCallBack,SwipeRefreshLayout.OnRefreshListener {

    private View v;
    private Context mContext;
    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private Button loadFailBtn;
    private RelativeLayout mylayout;
    private MainActivity mainActivity;
    private RecyclerView recyclerView;
    private RecyclerViewHeader recyclerHeader;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private TreeRecyclerViewAdapter treeRecyclerViewAdapter;

    public static AllCourseFragment newInstance(String param) {
        AllCourseFragment fragment = new AllCourseFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_all_course, container, false);
            initView();
        }

        initListener();
        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null) {
            parent.removeView(v);
        }
        return v;
    }

    private void initListener() {
        //搜索
        recyclerHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mainActivity, SearchAllActivity.class));
            }
        });
        loadFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load_fail_layout.setVisibility(View.GONE);
                jiazai_layout.setVisibility(View.VISIBLE);
                initData();
            }
        });
    }

    private void initData() {
        new HttpGetBuilder(mainActivity)
                .setUrl(UrlsNew.GET_CATEGORY)
                .setHttpResult(AllCourseFragment.this)
                .addQueryParams("page","1")
                .addQueryParams("pageSize","20")
                .setClassObj(DepartmentBean.class)
                .build();
    }


    private void initView() {
        myPullSwipeRefresh = (MyPullSwipeRefresh) v.findViewById(R.id.myPullSwipeRefresh);
        myPullSwipeRefresh.setOnRefreshListener(AllCourseFragment.this);
        jiazai_layout = (LinearLayout) v.findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout) v.findViewById(R.id.load_fail_layout);
        no_info_layout = (LinearLayout) v.findViewById(R.id.no_info_layout);
        loadFailBtn = (Button) v.findViewById(R.id.load_fail_button);
        mylayout = (RelativeLayout) v.findViewById(R.id.course_fragment);
        recyclerView = (RecyclerView) v.findViewById(R.id.facultyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        recyclerView.addItemDecoration(new DividerItemDecoration(mainActivity, DividerItemDecoration.VERTICAL_LIST));
        recyclerHeader = (RecyclerViewHeader) v.findViewById(R.id.header_layout);
        recyclerHeader.attachTo(recyclerView);
        //去除recyclerView默认刷新动画
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    protected void requestData() {
        loadingPage();
        initData();
    }


    /**
     * 暂无数据
     */
    private void noDataPage() {
        load_fail_layout.setVisibility(View.GONE);
        myPullSwipeRefresh.setVisibility(View.GONE);
        jiazai_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.VISIBLE);
    }

    /**
     * 加载失败页
     */
    private void loadFailPage() {
        load_fail_layout.setVisibility(View.VISIBLE);
        myPullSwipeRefresh.setVisibility(View.GONE);
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
        myPullSwipeRefresh.setVisibility(View.VISIBLE);
    }


    /**
     * 显示加载页
     */
    private void loadingPage() {
        jiazai_layout.setVisibility(View.VISIBLE);
        myPullSwipeRefresh.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
    }


    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        visibleData();
        myPullSwipeRefresh.setRefreshing(false);
        ArrayList<FirstLevelItem> treeBean = new ArrayList<>();
        DepartmentBean bean = (DepartmentBean) resultBean;
        ArrayList<DepartmentBean.Department> departments = bean.getItems();
        if(departments != null && departments.size() > 0){
            for (int i = 0; i < departments.size(); i++) {
                treeBean.add(new FirstLevelItem(departments.get(i)));
            }
            treeRecyclerViewAdapter = new TreeRecyclerViewAdapter<>(mainActivity,
                    treeBean, TreeRecyclerViewType.SHOW_COLLAPSE_CHILDS);
            recyclerView.setAdapter(treeRecyclerViewAdapter);
        } else {
            //无数据
            noDataPage();
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        myPullSwipeRefresh.setRefreshing(false);
        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
            NetworkUtil.httpRestartLogin(mainActivity, mylayout);
        } else {
            NetworkUtil.httpNetErrTip(mainActivity, mylayout);
        }
        loadFailPage();
    }
}

