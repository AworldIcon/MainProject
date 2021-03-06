package com.coder.kzxt.poster.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.poster.activity.Posters_Particulars_Activity;
import com.coder.kzxt.poster.adapter.MyPromotionStageAdapter;
import com.coder.kzxt.poster.adapter.MyPromotionStageDelegate;
import com.coder.kzxt.poster.beans.PosterBeans;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 发布的海报
 */
public class PostersPublishFragment extends BaseFragment implements HttpCallBack{


    //下拉刷新 瀑布流
    private MyPullSwipeRefresh myStagePullSwipeRefresh;
    private MyPullRecyclerView myStagePullRecyclerView;

    private MyPromotionStageDelegate orderListStageDelegate;
    private MyPromotionStageAdapter stageadapter;

    private List<PosterBeans.ItemsBean> posterDate = new ArrayList<>();

    private String pageSize = "20";// 每页请求条数

    private SharedPreferencesUtil spu;
    private View view;
    private LinearLayout jiazai_layout;
    private RelativeLayout my_layout;
    private LinearLayout no_info_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_poster_my, container, false);

            spu = new SharedPreferencesUtil(getActivity());
            //初始化 view findviewbyid
            initView();
            //需要初始化的数据
            initData();
            //响应事件click
            initClick();
            httpRequest();
        }

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }


        return view;
    }

    // 控件
    private void initView() {
        jiazai_layout = (LinearLayout) view.findViewById(R.id.jiazai_layout);
        no_info_layout = (LinearLayout) view.findViewById(R.id.no_info_layout);
        jiazai_layout.setVisibility(View.VISIBLE);
        my_layout = (RelativeLayout) view.findViewById(R.id.my_layout);
        myStagePullSwipeRefresh = (MyPullSwipeRefresh) view.findViewById(R.id.myStagePullSwipeRefresh);
        myStagePullRecyclerView = (MyPullRecyclerView) view.findViewById(R.id.myStagePullRecyclerView);


    }

    private void initData() {

        //stage的初始化
        orderListStageDelegate = new MyPromotionStageDelegate(getActivity());
        stageadapter = new MyPromotionStageAdapter(getActivity(), posterDate, orderListStageDelegate);
        myStagePullRecyclerView.setAdapter(stageadapter);
        stageadapter.setSwipeRefresh(myStagePullSwipeRefresh);
    }

    private void initClick() {

        myStagePullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                stageadapter.resetPageIndex();
                httpRequest();

            }
        });

        myStagePullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                stageadapter.addPageIndex();
                httpRequest();
            }
        });


        stageadapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), Posters_Particulars_Activity.class);
                intent.putExtra("bean", posterDate.get(position));
                intent.putExtra("position", position);
                startActivityForResult(intent, 4);
            }
        });

    }

    public void refreshHttpRequest() {
        if (stageadapter != null) {
            jiazai_layout.setVisibility(View.VISIBLE);
            stageadapter.resetPageIndex();
            httpRequest();
        }
    }

    private void httpRequest() {

        HttpGetBuilder httpGetBuilder = new HttpGetBuilder(getActivity());
        httpGetBuilder.setHttpResult(PostersPublishFragment.this);
        httpGetBuilder.setClassObj(PosterBeans.class);
        httpGetBuilder.setUrl(UrlsNew.GET_POSTER);
        httpGetBuilder.addQueryParams("page",stageadapter.getPageIndex()+"");
        httpGetBuilder.addQueryParams("pageSize",pageSize);
        httpGetBuilder.addQueryParams("user_id",spu.getUid());
        httpGetBuilder.addQueryParams("orderBy","create_time desc");
        httpGetBuilder.build();

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 4 && resultCode == 4) {
            stageadapter.resetPageIndex();
            httpRequest();
        } else if (requestCode == 4 && resultCode == 5) {
//            int position = data.getIntExtra("position", 0);
//            posterDate.remove(position);
//            stageadapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void requestData() {

    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        PosterBeans posterBeans = (PosterBeans) resultBean;
        stageadapter.setTotalPage(posterBeans.getPaginate().getPageNum());
        stageadapter.setPullData(posterBeans.getItems());
        jiazai_layout.setVisibility(View.GONE);
        if(posterBeans.getItems().size()==0){
            no_info_layout.setVisibility(View.VISIBLE);
        }else {
            no_info_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        jiazai_layout.setVisibility(View.GONE);
        NetworkUtil.httpNetErrTip(getActivity(),my_layout);
        if(code== Constants.HTTP_CODE_2001||code==Constants.HTTP_CODE_2004){
            NetworkUtil.httpRestartLogin(PostersPublishFragment.this,my_layout);
        }
    }
}
