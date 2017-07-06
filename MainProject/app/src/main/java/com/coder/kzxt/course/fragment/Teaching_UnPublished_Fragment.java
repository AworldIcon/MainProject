package com.coder.kzxt.course.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.beans.CourseBean;
import com.coder.kzxt.course.adapter.TeachingPunlishedDelegate;
import com.coder.kzxt.course.beans.LocalCourseResultBean;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.app.http.UrlsNew;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Teaching_UnPublished_Fragment extends BaseFragment implements HttpCallBack {

    private View v;
    private SharedPreferencesUtil spu;
    private RelativeLayout my_ly;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;
    private LinearLayout jiazai_layout;
    private LinearLayout no_info_layout;
    private LinearLayout load_fail_layout;
    private Button load_fail_button;
    private PullRefreshAdapter pullRefreshAdapter;
    private TeachingPunlishedDelegate delegate;
    private List<LocalCourseResultBean> courseResultBeens;
    private List<CourseBean> mData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spu = new SharedPreferencesUtil(getActivity());
        courseResultBeens = new ArrayList<>();
        mData = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_teaching_un_published, container, false);
            myPullSwipeRefresh = (MyPullSwipeRefresh)v.findViewById(R.id.myPullSwipeRefresh);
            myPullRecyclerView = (MyPullRecyclerView)v.findViewById(R.id.myPullRecyclerView);
            my_ly = (RelativeLayout) v.findViewById(R.id.my_ly);
            jiazai_layout = (LinearLayout) v.findViewById(R.id.jiazai_layout);
            jiazai_layout.setVisibility(View.VISIBLE);
            no_info_layout = (LinearLayout) v.findViewById(R.id.no_info_layout);
            load_fail_layout = (LinearLayout) v.findViewById(R.id.load_fail_layout);
            load_fail_button = (Button) v.findViewById(R.id.load_fail_button);

            delegate = new TeachingPunlishedDelegate(getActivity());

            pullRefreshAdapter = new PullRefreshAdapter(getActivity(), mData, delegate);
            myPullRecyclerView.setAdapter(pullRefreshAdapter);
            pullRefreshAdapter.setSwipeRefresh(myPullSwipeRefresh);
        }

        initOnclick();

        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null) {
            parent.removeView(v);
        }
        return v;
    }

    private void initOnclick(){

        pullRefreshAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });


        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullRefreshAdapter.resetPageIndex();
                requesHttp();
            }
        });

        myPullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                pullRefreshAdapter.addPageIndex();
                requesHttp();
            }
        });


        load_fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jiazai_layout.setVisibility(View.VISIBLE);
                requesHttp();
            }
        });

    }


    private void requesHttp(){
        new HttpGetBuilder(getActivity())
                .setHttpResult(this)
                .setClassObj(LocalCourseResultBean.class)
                .setUrl(UrlsNew.GET_COURSE)
                .addQueryParams("page", pullRefreshAdapter.getPageIndex() + "")
                .addQueryParams("pageSize", "20")
                .addQueryParams("orderBy", "create_time desc")
                .addBodyParam("status","2") //2未发布
                .addQueryParams("user_id",spu.getUid())
                .build();
    }

    @Override
    protected void requestData() {
        requesHttp();
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        jiazai_layout.setVisibility(View.GONE);

        LocalCourseResultBean questionDetailBean = (LocalCourseResultBean) resultBean;
        pullRefreshAdapter.setTotalPage(questionDetailBean.getPaginate().getPageNum());
        pullRefreshAdapter.setPullData(questionDetailBean.getItems());
        if(pullRefreshAdapter.getShowCount()==0){
            no_info_layout.setVisibility(View.VISIBLE);
        }else {
            no_info_layout.setVisibility(View.GONE);


        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        load_fail_layout.setVisibility(View.VISIBLE);
        jiazai_layout.setVisibility(View.GONE);
        NetworkUtil.httpNetErrTip(getActivity(),my_ly);
        if(code== Constants.HTTP_CODE_2001||code==Constants.HTTP_CODE_2004){
            NetworkUtil.httpRestartLogin(Teaching_UnPublished_Fragment.this,my_ly);
        }
    }
}
