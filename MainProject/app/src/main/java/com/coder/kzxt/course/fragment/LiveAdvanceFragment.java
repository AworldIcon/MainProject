package com.coder.kzxt.course.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.app.utils.L;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.beans.LiveBean;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.course.beans.CourseBelowLiveBean;
import com.coder.kzxt.course.delegate.LiveDelegate;
import com.coder.kzxt.main.beans.LiveListResultBean;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 即将直播 的页面
 */
public class LiveAdvanceFragment extends BaseFragment implements HttpCallBack
{

    private SharedPreferencesUtil spu;
    private RelativeLayout mainView;
    //下拉刷新
    private MyPullSwipeRefresh myStagePullSwipeRefresh;
    private MyPullRecyclerView myStagePullRecyclerView;
    private LinearLayout no_info_layout;
    private ImageView no_info_img;
    private TextView no_info_text;
    private LiveDelegate pullDelegate;
    private PullRefreshAdapter pullRefreshAdapter;

    private List<LiveBean> data = new ArrayList<>();

    private int pageSize = 20;// 每页请求条数


    private void initVariable() {

    }

    private String treeId;
    private HttpGetBuilder httpGetBuilder;
    private View view;

    public static Fragment newInstance(String treeId) {
        LiveAdvanceFragment f = new LiveAdvanceFragment();
        Bundle b = new Bundle();
        b.putString("treeId",treeId);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        treeId = args != null ? args.getString("treeId") : "";
        spu = new SharedPreferencesUtil(getActivity());
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        L.d("onCreateView:  liveadvance");

        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_common_pull_recyclerview, container, false);

            //获取从其他页面获取的变量 getIntent
            initVariable();
            //初始化 view findviewbyid
            initView();
            //需要初始化的数据
            initData();
            //响应事件click
            initClick();
        }

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null)
        {
            parent.removeView(view);
        }


        return view;
    }

    // 控件
    private void initView()
    {
        no_info_layout = (LinearLayout) view.findViewById(R.id.no_info_layout);
        no_info_img = (ImageView) view.findViewById(R.id.no_info_img);
        no_info_img.setImageResource(R.drawable.empty_live);
        no_info_text = (TextView) view.findViewById(R.id.no_info_text);
        no_info_text.setText("暂无直播");
        mainView = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        myStagePullSwipeRefresh = (MyPullSwipeRefresh) view.findViewById(R.id.pullSwipeRefresh);
        myStagePullRecyclerView = (MyPullRecyclerView) view.findViewById(R.id.pullRecyclerView);

    }

    private void initData()
    {
        //stage的初始化
        pullDelegate = new LiveDelegate(getActivity(),0,"");
        pullRefreshAdapter = new PullRefreshAdapter(getActivity(), data, pullDelegate);
        myStagePullRecyclerView.setAdapter(pullRefreshAdapter);
        pullRefreshAdapter.setSwipeRefresh(myStagePullSwipeRefresh);
    }

    private void initClick()
    {

        myStagePullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                pullRefreshAdapter.resetPageIndex();
                httpRequest();

            }
        });

        myStagePullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener()
        {
            @Override
            public void addMoreListener()
            {
                pullRefreshAdapter.addPageIndex();
                httpRequest();
            }
        });

    }

    private void httpRequest() {

        if(TextUtils.isEmpty(treeId)){

            httpGetBuilder = new HttpGetBuilder(getActivity());
            httpGetBuilder.setHttpResult(this)
                    .setClassObj(LiveListResultBean.class)
                    .addQueryParams("page", pullRefreshAdapter.getPageIndex() + "")
                    .addQueryParams("pageSize", pageSize + "")
                    .addQueryParams("liveStatus", "0")
                    .addQueryParams("orderBy","start_time asc")
                    .addQueryParams("center", "1")
                    .setUrl(UrlsNew.GET_LIVE)
                    .setRequestCode(1001);

            if(!TextUtils.isEmpty(spu.getIsLogin())){
                httpGetBuilder.addQueryParams("custom","home");
            }else {
                httpGetBuilder .addQueryParams("isBindCourse","0");
            }
            httpGetBuilder.build();

        }else {
            //获取课程下的直播
            new HttpGetBuilder(getActivity())
                    .setHttpResult(this)
                    .setClassObj(CourseBelowLiveBean.class)
                    .addQueryParams("page", pullRefreshAdapter.getPageIndex() + "")
                    .addQueryParams("pageSize", pageSize + "")
                    .addQueryParams("course_id", treeId)
                    .addQueryParams("relation", "live")
                    .addQueryParams("center", "1")
                    .addQueryParams("non_uid", "1")
                    .setRequestCode(1002)
                    .setUrl(UrlsNew.GET_COURSE_lIVE)
                    .build();
         }
    }

    @Override
    protected void requestData()
    {
        showLoadingView(mainView);
        httpRequest();
        L.d("requestData: live advance ");

    }

    private long time;
    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {
       if (requestCode == 1001)
        {
            hideLoadingView();
            LiveListResultBean liveListResult = (LiveListResultBean) resultBean;
            pullDelegate.setSystomTime(time);
            pullRefreshAdapter.setTotalPage(liveListResult.getPaginate().getPageNum());
            pullRefreshAdapter.setPullData(liveListResult.getItems());

            if(pullRefreshAdapter.getShowCount()==0){
                no_info_layout.setVisibility(View.VISIBLE);
            }else {
                no_info_layout.setVisibility(View.GONE);
            }

        }else if(requestCode==1002){
            hideLoadingView();
            CourseBelowLiveBean courseBelowLiveBean = (CourseBelowLiveBean) resultBean;
            List<LiveBean> liveBeanList = new ArrayList<>();
            List<LiveBean> itemsBeanListL = courseBelowLiveBean.getItems();
            for (int i = 0; i < itemsBeanListL.size(); i++) {
                if(itemsBeanListL.get(i).getLive_status()==0){
                    liveBeanList.add(itemsBeanListL.get(i));
                }

            }
           pullRefreshAdapter.setTotalPage(courseBelowLiveBean.getPaginate().getPageNum());
            pullRefreshAdapter.setPullData(liveBeanList);
           if(pullRefreshAdapter.getShowCount()==0){
               no_info_layout.setVisibility(View.VISIBLE);
           }else {
               no_info_layout.setVisibility(View.GONE);
           }
        }

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        hideLoadingView();
        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004 || code == Constants.HTTP_CODE_8000) {
            NetworkUtil.httpRestartLogin(LiveAdvanceFragment.this, mainView);
        } else {
            NetworkUtil.httpNetErrTip(getActivity(), mainView);
        }

    }
}
