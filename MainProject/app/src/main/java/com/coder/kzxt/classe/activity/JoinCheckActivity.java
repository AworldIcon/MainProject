package com.coder.kzxt.classe.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPutBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.classe.beans.ClassDetailBean;
import com.coder.kzxt.classe.beans.ClassMemberApply;
import com.coder.kzxt.classe.delegate.MemberApplyDelegate;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.recyclerview.decoration.DividerItemDecoration;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangtingshun on 2017/6/9.
 * 加入审核
 */

public class JoinCheckActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
         MemberApplyDelegate.OnClassMemberApplyInterface{

    private Toolbar mToolbar;
    private SharedPreferencesUtil spu;
    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private RelativeLayout myLayout;
    private Button loadFailBtn;   //加载失败
    private MyPullSwipeRefresh pullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;
    private ClassDetailBean.ClassDetail detailBean;
    private int pageNum;
    private List<ClassMemberApply.MemberApplyBean> mData;
    private MemberApplyDelegate delegate;
    private PullRefreshAdapter<ClassMemberApply.MemberApplyBean>  pullRefreshAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_check_layout);
        detailBean = (ClassDetailBean.ClassDetail) getIntent().getSerializableExtra("detail");
        initView();
        initData();
        initListener();
        requestData();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.mToolBar);
        mToolbar.setTitle(getResources().getString(R.string.join_check));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout)findViewById(R.id.load_fail_layout);
        no_info_layout = (LinearLayout)findViewById(R.id.no_info_layout);
        myLayout = (RelativeLayout) findViewById(R.id.rl_layout);
        loadFailBtn = (Button) findViewById(R.id.load_fail_button);
        pullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        myPullRecyclerView = (MyPullRecyclerView) findViewById(R.id.myPullRecyclerView);
        myPullRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myPullRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    private void initListener() {
        pullSwipeRefresh.setOnRefreshListener(this);
        //重新加载
        loadFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestData();
            }
        });

        myPullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                pullRefreshAdapter.addPageIndex();
                getJoinCheckData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initData() {
        mData = new ArrayList<>();
        delegate = new MemberApplyDelegate(this);
        delegate.setOnClassMemberApplayListener(this);
        pullRefreshAdapter = new PullRefreshAdapter<>(this,mData,delegate);
        myPullRecyclerView.setAdapter(pullRefreshAdapter);
        pullRefreshAdapter.setSwipeRefresh(pullSwipeRefresh);
    }

    private void requestData() {
        loadingPage();
        getJoinCheckData();
    }

    private void getJoinCheckData() {
                new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_GROUP_MEMBER_APPLY)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        pullSwipeRefresh.setRefreshing(false);
                        ClassMemberApply memberBean = (ClassMemberApply) resultBean;
                        ClassMemberApply.Paginate paginate = memberBean.getPaginate();
                        if (paginate != null) {
                            pageNum = paginate.getPageNum();
                        }
                        ArrayList<ClassMemberApply.MemberApplyBean> items = memberBean.getItems();
                        if (items != null && items.size() > 0) {
                            visibleData();
                            adapteData(items);
                        } else {
                            noDataPage();
                        }
                        hideLoadingView();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(JoinCheckActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(JoinCheckActivity.this, myLayout);
                        }
                        pullSwipeRefresh.setRefreshing(false);
                        loadFailPage();
                        hideLoadingView();
                    }
                })
                .setClassObj(ClassMemberApply.class)
                .addQueryParams("page", pullRefreshAdapter.getPageIndex() + "")
                .addQueryParams("pageSize", "20")
                .addQueryParams("orderBy","create_time desc")
                .addQueryParams("group_id", detailBean.getId())
                .build();

    }

    private void adapteData(ArrayList<ClassMemberApply.MemberApplyBean> items) {
        pullRefreshAdapter.setTotalPage(pageNum);
        pullRefreshAdapter.setPullData(items);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //返回
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        pullRefreshAdapter.resetPageIndex();
        getJoinCheckData();
    }

    /**
     * 暂无数据
     */
    private void noDataPage() {
        load_fail_layout.setVisibility(View.GONE);
        pullSwipeRefresh.setVisibility(View.GONE);
        jiazai_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.VISIBLE);
    }

    /**
     * 加载失败页
     */
    private void loadFailPage() {
        load_fail_layout.setVisibility(View.VISIBLE);
        pullSwipeRefresh.setVisibility(View.GONE);
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
        pullSwipeRefresh.setVisibility(View.VISIBLE);
    }

    /**
     * 显示加载页
     */
    private void loadingPage() {
        jiazai_layout.setVisibility(View.VISIBLE);
        pullSwipeRefresh.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
    }

    /**
     * 申请同意
     * @param applyBean
     */
    @Override
    public void onAgreeApply(ClassMemberApply.MemberApplyBean applyBean) {
        getApplyCheck(applyBean.getId(),"1");
    }

    /**
     * 拒绝
     * @param applyBean
     */
    @Override
    public void onRefusedApply(ClassMemberApply.MemberApplyBean applyBean) {
        getApplyCheck(applyBean.getId(),"2");
    }

    private void getApplyCheck(String applyId,String applyStatus) {
                showLoadingView();
                 new HttpPutBuilder(this)
                .setUrl(UrlsNew.GET_GROUP_MEMBER_APPLY)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        getJoinCheckData();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(JoinCheckActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(JoinCheckActivity.this, myLayout);
                        }
                        hideLoadingView();
                    }
                })
                .setClassObj(null)
                .setPath(applyId)
                .addBodyParam("status",applyStatus)
                .build();
    }




}
