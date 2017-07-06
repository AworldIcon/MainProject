package com.coder.kzxt.poster.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.poster.adapter.LikeDelegate;
import com.coder.kzxt.poster.beans.LikePraiseBean;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;

import java.util.ArrayList;
import java.util.List;


public class Posters_All_like_User_Activity extends BaseActivity implements HttpCallBack {

    private Toolbar mToolbarView;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;
    private String postersId;
    private LikeDelegate likeDelegate;
    private PullRefreshAdapter pullRefreshAdapter;
    private List<LikePraiseBean.ItemsBean> list = new ArrayList<>();

    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private Button load_fail_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posters_all_like_user);
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        jiazai_layout.setVisibility(View.VISIBLE);
        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        no_info_layout = (LinearLayout) findViewById(R.id.no_info_layout);
        load_fail_button = (Button) findViewById(R.id.load_fail_button);
        mToolbarView  = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setTitle(R.string.like);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myPullSwipeRefresh = (MyPullSwipeRefresh)findViewById(R.id.myPullSwipeRefresh);
        myPullRecyclerView = (MyPullRecyclerView) findViewById(R.id.myPullRecyclerView);
        postersId  = getIntent().getStringExtra("postersId");

        //定义Delegate
        likeDelegate = new LikeDelegate(this);
        //定义适配器所有列表都定义PullRefreshAdapter
        pullRefreshAdapter = new PullRefreshAdapter(this,list,likeDelegate);
        //设置适配器
        myPullRecyclerView.setAdapter(pullRefreshAdapter);
        //适配器中设置下拉刷新
        pullRefreshAdapter.setSwipeRefresh(myPullSwipeRefresh);

        //下拉刷新
        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //把页面变为1
//                pullRefreshAdapter.resetPageIndex();
                //请求接口
                httpRequestLike();
            }
        });

//        //加载更多
//        myPullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
//            @Override
//            public void addMoreListener() {
//                //把页数加1
//                pullRefreshAdapter.addPageIndex();
//                //请求接口
//                httpRequestLike();
//            }
//        });

        load_fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_fail_layout.setVisibility(View.GONE);
                httpRequestLike();
            }
        });

        httpRequestLike();
    }

    private void httpRequestLike(){
        new HttpGetBuilder(this)
                .setClassObj(LikePraiseBean.class)
                .setHttpResult(this)
                .setUrl(UrlsNew.GET_POSTER_GETUSER)
                .addQueryParams("posterId",postersId)
                .build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        jiazai_layout.setVisibility(View.GONE);
        LikePraiseBean likePraiseBean = (LikePraiseBean) resultBean;
        pullRefreshAdapter.setPullData(likePraiseBean.getItems());
        load_fail_layout.setVisibility(View.GONE);
        if(likePraiseBean.getItems().size()==0){
            no_info_layout.setVisibility(View.VISIBLE);
        }else {
            no_info_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        jiazai_layout.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.VISIBLE);
    }
}
