package com.coder.kzxt.service.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.db.DataBaseDao;
import com.coder.kzxt.login.activity.LoginActivity;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.service.adapter.ServiceVideoDelegate;
import com.coder.kzxt.service.beans.ServiceBean;
import com.coder.kzxt.service.beans.ServiceVideoResult;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.PlayVideoServer;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.video.activity.VideoViewActivity;
import com.coder.kzxt.video.beans.CatalogueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务详情下的辅导视频页面
 */
public class ServiceVideoFragment extends BaseFragment implements HttpCallBack
{


    private RelativeLayout mainView;
    //下拉刷新
    private MyPullSwipeRefresh myStagePullSwipeRefresh;
    private MyPullRecyclerView myStagePullRecyclerView;
    private LinearLayout no_info_layout;
    private ImageView no_info_img;
    private TextView no_info_text;

    private ServiceVideoDelegate pullDelegate;
    private PullRefreshAdapter pullRefreshAdapter;

    private List<ServiceVideoResult.ItemsBean> data = new ArrayList<>();

    private int pageSize = 20;// 每页请求条数
    private int memberedId;// 每页请求条数


    private ServiceBean serviceBean;
    private View view;

    public static Fragment newInstance(ServiceBean serviceId, int memberedId)
    {
        ServiceVideoFragment f = new ServiceVideoFragment();
        Bundle b = new Bundle();
        b.putSerializable("bean", serviceId);
        b.putInt("memberedId", memberedId);
        f.setArguments(b);
        return f;
    }

    private void initVariable()
    {
        serviceBean = (ServiceBean) getArguments().getSerializable("bean");
        memberedId = getArguments().getInt("memberedId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
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
        no_info_text = (TextView) view.findViewById(R.id.no_info_text);
        mainView = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        myStagePullSwipeRefresh = (MyPullSwipeRefresh) view.findViewById(R.id.pullSwipeRefresh);
        myStagePullRecyclerView = (MyPullRecyclerView) view.findViewById(R.id.pullRecyclerView);

//        no_info_img.setImageResource(R.drawable.empty_live);
        no_info_text.setText("暂无视频");

    }

    private MyReceiver receiver;

    private void initData()
    {
        //stage的初始化
        pullDelegate = new ServiceVideoDelegate((BaseActivity) getActivity(), serviceBean, memberedId);
        pullRefreshAdapter = new PullRefreshAdapter(getActivity(), data, pullDelegate);
        myStagePullRecyclerView.setAdapter(pullRefreshAdapter);
        pullRefreshAdapter.setSwipeRefresh(myStagePullSwipeRefresh);


        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.MY_DOWNLOAD_FAIL);
        filter.addAction(Constants.MY_DOWNLOAD_FINSH);
        filter.addAction(Constants.MY_DOWNLOAD_REFRESH);
        // 注册广播
        getActivity().registerReceiver(receiver, filter);

        resetDaoData();
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

        pullRefreshAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {


                if (TextUtils.isEmpty(new SharedPreferencesUtil(getActivity()).getIsLogin()))
                {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), 1000);
                } else if (memberedId == -1)
                {
                    ToastUtils.makeText(getActivity(), getActivity().getResources().getString(R.string.plase_join_service));
                } else
                {
                    ServiceVideoResult.ItemsBean itemsBean = data.get(position);
//
                    CatalogueBean.ItemsBean.VideoBean videoBean = new CatalogueBean.ItemsBean.VideoBean();
                    videoBean.setTitle(itemsBean.getTitle());
                    videoBean.setCourse_chapter_id(itemsBean.getService_id());
                    if (stringList.contains(itemsBean.getId()))
                    {
                        videoBean.setMediaUri(DataBaseDao.getInstance(getActivity()).queryVideoUrl(itemsBean.getId()));
                    } else
                    {
                        videoBean.setMediaUri(itemsBean.getMedia_uri());
                    }

                    try
                    {
                        new PlayVideoServer(getActivity()).start();
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(getActivity(), VideoViewActivity.class);
                    intent.putExtra("flag", Constants.OFFLINE);
                    intent.putExtra("treeid", itemsBean.getService_id());
                    intent.putExtra("treename", itemsBean.getTitle());
                    intent.putExtra("pic", "");
                    intent.putExtra("videoBean", videoBean);
                    startActivity(intent);

                }
            }
        });

    }

    @Override
    protected void requestData()
    {
        showLoadingView(mainView);
        httpRequest();
    }

    private void httpRequest()
    {
        new HttpGetBuilder(getActivity())
                .setHttpResult(this)
                .setClassObj(ServiceVideoResult.class)
                .addQueryParams("page", pullRefreshAdapter.getPageIndex() + "")
                .addQueryParams("pageSize", pageSize + "")
                .addQueryParams("service_id", serviceBean.getService_id() + "")
                .addQueryParams("status", "2")
                .setUrl(UrlsNew.SERVICE_LESSON)
                .build();
    }


    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {
        hideLoadingView();

        ServiceVideoResult liveListResult = (ServiceVideoResult) resultBean;
        pullRefreshAdapter.setTotalPage(liveListResult.getPaginate().getPageNum());
        pullRefreshAdapter.setPullData(liveListResult.getItems());
        if (pullRefreshAdapter.getShowCount() == 0)
        {
            no_info_layout.setVisibility(View.VISIBLE);
        } else
        {
            no_info_layout.setVisibility(View.GONE);
        }

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {
        hideLoadingView();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);

    }

    private class MyReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
//            if (action.equals(Constants.MY_DOWNLOAD_FAIL)){
//
//                String videoId = intent.getStringExtra("id");
//                DataBaseDao.getInstance(getActivity()).delete_item(videoId,serviceBean.getService_id()+"");
//
//            }

            if (action.equals(Constants.MY_DOWNLOAD_FINSH) || action.equals(Constants.MY_DOWNLOAD_REFRESH) || action.equals(Constants.MY_DOWNLOAD_FAIL))
            {
                resetDaoData();
            }
        }
    }

    List<String> stringList;

    private void resetDaoData()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                final ArrayList<String> videoIds = DataBaseDao.getInstance(getActivity()).getVideoIds();
                final ArrayList<String> strings = DataBaseDao.getInstance(getActivity()).query_All_DownStatus();
                stringList = DataBaseDao.getInstance(getActivity()).queryAllDownLoadedVideo(serviceBean.getService_id() + "");

                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        pullDelegate.setDataDao(videoIds, strings, stringList);
                    }
                });
            }
        }).start();

    }
}
