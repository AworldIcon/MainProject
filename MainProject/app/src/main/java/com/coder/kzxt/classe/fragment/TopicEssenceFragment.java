package com.coder.kzxt.classe.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.classe.activity.ClassTopicActivity;
import com.coder.kzxt.classe.activity.TopicDetailActivity;
import com.coder.kzxt.classe.adapter.ClassRefreshAdapter;
import com.coder.kzxt.classe.beans.ClassTopicBean;
import com.coder.kzxt.classe.beans.MyCreateClass;
import com.coder.kzxt.classe.delegate.ClassTopicDelegate;
import com.coder.kzxt.classe.mInterface.OnClassTopicInterface;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;

import java.util.ArrayList;


/**
 * Created by wangtingshun on 2017/6/12.
 * 话题精华的fragment
 */
public class TopicEssenceFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,HttpCallBack,OnClassTopicInterface{

    private ClassTopicActivity mContext;
    private View view;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;
    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private Button loadFailBtn;
    private RelativeLayout myLayout;
    private ClassRefreshAdapter pullRefreshAdapter;
    private ArrayList<ClassTopicBean.ClassTopic> data = new ArrayList<>();
    private ClassTopicDelegate delegate;
    private SharedPreferencesUtil spu;
    private MyCreateClass.CreateClassBean classBean;
    private String selfRole;
    private String joinState;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (ClassTopicActivity)context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         spu = new SharedPreferencesUtil(mContext);
         Bundle bundle = getArguments();
         classBean = (MyCreateClass.CreateClassBean) bundle.getSerializable("classBean");
         selfRole = bundle.getString("selfRole","");
         joinState = bundle.getString("joinState","");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_class_topic_layout, container, false);
            initView();
            initData();
            initListener();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void initData() {
        delegate = new ClassTopicDelegate(mContext);
        delegate.setOnClassTopicListener(this);
        pullRefreshAdapter = new ClassRefreshAdapter(mContext,data,delegate);
        myPullRecyclerView.setAdapter(pullRefreshAdapter);
        pullRefreshAdapter.setSwipeRefresh(myPullSwipeRefresh);
    }

    private void initView() {
        jiazai_layout = (LinearLayout) view.findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout) view.findViewById(R.id.load_fail_layout);
        no_info_layout = (LinearLayout) view.findViewById(R.id.no_info_layout);
        myLayout = (RelativeLayout) view. findViewById(R.id.rl_layout);
        loadFailBtn = (Button)  view.findViewById(R.id.load_fail_button);
        myPullSwipeRefresh = (MyPullSwipeRefresh) view.findViewById(R.id.myPullSwipeRefresh);
        myPullRecyclerView = (MyPullRecyclerView) view.findViewById(R.id.myPullRecyclerView);
//        myPullRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private void initListener() {
        myPullSwipeRefresh.setOnRefreshListener(this);

        myPullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                pullRefreshAdapter.addPageIndex();
                getClassTopic();
            }
        });
        loadFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPage();
                getClassTopic();
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
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void requestData() {
          loadingPage();
          getClassTopic();
    }

    private void getClassTopic() {
        HttpGetBuilder builder = new HttpGetBuilder(mContext);
        builder.setUrl(UrlsNew.GET_GROUP_TOPIC);
        builder.setHttpResult(this);
        builder.setClassObj(ClassTopicBean.class);
        builder.addQueryParams("page", pullRefreshAdapter.getPageIndex() + "");
        builder.addQueryParams("pageSize", "20");
        builder.addQueryParams("orderBy", "create_time desc");
        builder.addQueryParams("group_id", classBean.getId());  //班级id
        builder.addQueryParams("is_stick", "1");
        builder.addQueryParams("user_id", spu.getUid());
        builder.build();
    }

    @Override
    public void onRefresh() {
        pullRefreshAdapter.resetPageIndex();
        getClassTopic();
    }


    public void updataData(){
        getClassTopic();
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
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        myPullSwipeRefresh.setRefreshing(false);
        ClassTopicBean topicBean = (ClassTopicBean)resultBean;
        ArrayList<ClassTopicBean.ClassTopic> items = topicBean.getItems();
        if (items != null && items.size() > 0) {
            visibleData();
            pullRefreshAdapter.setTotalPage(topicBean.getPaginate().getPageNum());
            pullRefreshAdapter.setPullData(items);
        } else {
            noDataPage();
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
            NetworkUtil.httpRestartLogin(getActivity(), myLayout);
        } else {
            NetworkUtil.httpNetErrTip(mContext, myLayout);
        }
        myPullSwipeRefresh.setRefreshing(false);
        loadFailPage();
    }

    @Override
    public void onClassTopicItem(ClassTopicBean.ClassTopic classTopic) {
        Intent intent = new Intent(mContext, TopicDetailActivity.class);
        intent.putExtra("selfRole",selfRole);
        intent.putExtra("joinState",joinState);
        intent.putExtra("topicId",classTopic.getId());
        intent.putExtra("pageIndex",3);
        mContext.startActivityForResult(intent,Constants.REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.REQUEST_CODE && resultCode == 10){
            showLoadingView(null);
            getClassTopic();
        }
    }
}
