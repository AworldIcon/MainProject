package com.coder.kzxt.classe.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.app.http.HttpGetOld;
import com.app.http.InterfaceHttpResult;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.activity.ClassParticularsActivity;
import com.coder.kzxt.classe.adapter.ClassListAdapter;
import com.coder.kzxt.classe.beans.ClassListBean;
import com.coder.kzxt.login.activity.LoginActivity;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.Urls;
import java.util.ArrayList;

/**
 * 班级列表
 * Created by wangtingshun on 2017/3/10.
 */

public class ClassListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        MyPullRecyclerView.OnAddMoreListener,ClassListAdapter.OnItemClickInterface{

    private Context mContext;
    private View rootView;
    private MyPullSwipeRefresh swipeRefresh;
    private MyPullRecyclerView recyclerView;
    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private String categoryId;
    private String page; //第一页
    private String totalPage; //总页数
    /** 是否已经请求了网络数据 */
    private boolean isPrepared = false;
    /**是否加载过数据*/
    private boolean hasLoad = false;
    private SharedPreferencesUtil spu;
    private RelativeLayout myLayout;
    private ClassListAdapter adapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spu = new SharedPreferencesUtil(mContext);
        categoryId = getArguments().getString("category_id");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.activity_course_and_class_layout,container,false);
            initView();
            initListener();
        }
        isPrepared = true;
        requestData();
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(parent);
        }
        return rootView;
    }

    private void initView() {
        recyclerView = (MyPullRecyclerView) rootView.findViewById(R.id.recyclerView);
        swipeRefresh = (MyPullSwipeRefresh) rootView.findViewById(R.id.myPullSwipeRefresh);
        swipeRefresh.setOnRefreshListener(ClassListFragment.this);
        jiazai_layout = (LinearLayout) rootView.findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout) rootView.findViewById(R.id.load_fail_layout);
        no_info_layout = (LinearLayout) rootView.findViewById(R.id.no_info_layout);
        myLayout = (RelativeLayout) rootView.findViewById(R.id.rl_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initListener() {
        recyclerView.setOnAddMoreListener(ClassListFragment.this);
    }


    @Override
    protected void requestData() {

        if(!isPrepared  ){
            return;
        }
        if (hasLoad){
            return;
        }
        getClassListData();
    }

    /**
     * 获取班级列表数据
     */
    private void getClassListData() {
        if(!hasLoad){
            loadingPage();
        }
        HttpGetOld httpGet = new HttpGetOld(mContext, this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                hasLoad = true;
                visibleData();
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
                    swipeRefresh.setRefreshing(false);
                    ClassListBean classBean = (ClassListBean) baseBean;
                    page = classBean.getPage();
                    totalPage = classBean.getTotalPage();
                    ArrayList<ClassListBean.ClassBean> classs = classBean.getData();
                    if (classs != null && classs.size() > 0) {
                        getClassListSuccess(classs);
                    } else {
                        noDataPage();
                    }
                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                    NetworkUtil.httpNetErrTip(mContext, myLayout);
                } else {
                    errorProcess(code, msg);
                }
            }
        }, ClassListBean.class, Urls.GET_CLASS_LIST_ACTION, spu.getUid(),spu.getIsLogin(),spu.getTokenSecret(),categoryId,spu.getDevicedId(),page,"20");
        httpGet.excute(1000);
    }

    /**
     * 异常处理
     * @param code
     * @param msg
     */
    private void errorProcess(int code, String msg) {
        loadFailPage();
    }

    /**
     * 获取班级列表数据成功
     * @param classs
     */
    private void getClassListSuccess(ArrayList<ClassListBean.ClassBean> classs) {
        adapter = new ClassListAdapter(mContext,classs);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(ClassListFragment.this);
    }

    @Override
    public void onRefresh() {
        getClassListData();
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
    public void addMoreListener() {
        Log.i("test","班级列表加载更多");
    }

    /**
     * 点击班级item
     * @param classBean
     */
    @Override
    public void onItemClick(ClassListBean.ClassBean classBean) {
        if (TextUtils.isEmpty(spu.getIsLogin())) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.putExtra("from", "classparticulars");
            intent.putExtra("classId", classBean.getClassId());
            intent.putExtra("myClassState", "1");
            mContext.startActivity(intent);
        } else {
            Intent intent = new Intent(mContext, ClassParticularsActivity.class);
            intent.putExtra("classId", classBean.getClassId());
            intent.putExtra("myClassState", "1");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
