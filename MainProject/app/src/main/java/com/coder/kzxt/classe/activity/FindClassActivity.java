package com.coder.kzxt.classe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.classe.beans.MyCreateClass;
import com.coder.kzxt.classe.beans.NewClassListBean;
import com.coder.kzxt.classe.delegate.FindClassDelegate;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.recyclerview.decoration.DividerItemDecoration;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.views.RecyclerViewHeader;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by wangtingshun on 2017/6/6.
 * 查找班级
 */

public class FindClassActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,FindClassDelegate.OnFindItemClickInterface{

    private Toolbar mToolbar;
    private SharedPreferencesUtil spu;
    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private RelativeLayout myLayout;
    private Button loadFailBtn;   //加载失败
    private MyPullSwipeRefresh pullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;
    private RecyclerViewHeader recyclerHeader;
    private int pageNum;
    private List<NewClassListBean.ClassListBean> mData;
    private FindClassDelegate delegate;
    private PullRefreshAdapter<NewClassListBean.ClassListBean>  pullRefreshAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spu = new SharedPreferencesUtil(this);
        setContentView(R.layout.find_class_layout);
        initView();
        initData();
        initListener();
        requestData();
    }

    private void initData() {
        mData = new ArrayList<>();
        delegate = new FindClassDelegate(this);
        delegate.setOnFindItemClickListener(this);
        pullRefreshAdapter = new PullRefreshAdapter<>(this,mData,delegate);
        myPullRecyclerView.setAdapter(pullRefreshAdapter);
        pullRefreshAdapter.setSwipeRefresh(pullSwipeRefresh);
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.mToolBar);
        mToolbar.setTitle("查找班级");
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
        recyclerHeader = (RecyclerViewHeader)findViewById(R.id.header_layout);
        recyclerHeader.attachTo(myPullRecyclerView);
        //去除recyclerView默认刷新动画
        ((SimpleItemAnimator)myPullRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    private void initListener() {
        pullSwipeRefresh.setOnRefreshListener(this);
        //搜索
        recyclerHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindClassActivity.this,SearchClassActivity.class);
                startActivity(intent);
            }
        });
        //重新加载
        loadFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPage();
                getClasListRequest();
            }
        });

        myPullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                pullRefreshAdapter.addPageIndex();
                getClasListRequest();
            }
        });
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

    /**
     * 获取班级列表
     */
    private void requestData() {
         loadingPage();
         getClasListRequest();
    }

    private void getClasListRequest() {
                new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_CLASS_LIST)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        pullSwipeRefresh.setRefreshing(false);
                        hideLoadingView();
                        NewClassListBean calssBean = (NewClassListBean) resultBean;
                        NewClassListBean.Paginate paginate = calssBean.getPaginate();
                        if(paginate != null){
                            pageNum = paginate.getPageNum();
                        }
                        ArrayList<NewClassListBean.ClassListBean> items = calssBean.getItems();
                        if (items != null && items.size() > 0) {
                            visibleData();
                            adapteData(items);
                        } else {
                            noDataPage();
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(FindClassActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(FindClassActivity.this, myLayout);
                        }
                        pullSwipeRefresh.setRefreshing(false);
                        hideLoadingView();
                        loadFailPage();
                    }
                })
                .setClassObj(NewClassListBean.class)
                .addQueryParams("page", pullRefreshAdapter.getPageIndex() + "")
                .addQueryParams("pageSize", "20")
                .addQueryParams("orderBy","create_time desc")
                //.addQueryParams("user_id", spu.getUid())
                .build();
    }

    private void adapteData(ArrayList<NewClassListBean.ClassListBean> items) {
        pullRefreshAdapter.setTotalPage(pageNum);
        pullRefreshAdapter.setPullData(items);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.RESTART_LOGIN == requestCode &&
                resultCode == Constants.LOGIN_BACK) {
            requestData();
        } else if(requestCode == Constants.REQUEST_CODE && resultCode == 2){
            showLoadingView();
            getClasListRequest();
        } else if(requestCode == Constants.REQUEST_CODE && resultCode == 3){
            setResult(2);
            finish();
        }

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

    @Override
    public void onRefresh() {
        pullRefreshAdapter.resetPageIndex();
        getClasListRequest();
    }

    @Override
    public void onFindItem(NewClassListBean.ClassListBean classBean) {
        Intent intent = new Intent(this,ClassDetailActivity.class);
        MyCreateClass.CreateClassBean bean = new MyCreateClass.CreateClassBean();
        bean.setId(classBean.getId());
        bean.setSelf_role(classBean.getSelf_role());
        bean.setCategory_id(classBean.getCategory_id());
        bean.setLogo(classBean.getLogo());
        bean.setMember_count(classBean.getMember_count());
        bean.setName(classBean.getName());
        bean.setYear(classBean.getYear());
        intent.putExtra("class",bean);
        startActivityForResult(intent,Constants.REQUEST_CODE);
    }

}
