package com.coder.kzxt.main.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.beans.LiveBean;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.main.adapter.LivePlayingAdapter;
import com.coder.kzxt.main.beans.LiveListResultBean;
import com.coder.kzxt.main.beans.LivePlayingBean;
import com.coder.kzxt.main.beans.SystemTimeResultBean;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by pc on 2017/2/23
 */

public class LivePlayingFragment extends BaseFragment implements HttpCallBack {

    private View v;
    private SharedPreferencesUtil spu;
    private MyPullSwipeRefresh swiperefresh;
    private ExpandableListView my_list;
    private RelativeLayout mainView;

    private LivePlayingAdapter livePlayingAdapter;
    private List<LivePlayingBean> mData;
    public CountDownTimer countDownTimer;

    private HttpGetBuilder httpGetBuilder1;
    private HttpGetBuilder httpGetBuilder2;
    private HttpGetBuilder httpGetBuilder3;
    private HttpGetBuilder httpGetBuilder4;
    private MyReceiver myReceiver;

    private LinearLayout no_info_layout;
    private ImageView no_info_img;
    private TextView no_info_text;

    private LinearLayout load_fail_layout;
    private Button load_fail_button;

    public static LivePlayingFragment newInstance(String param) {
        LivePlayingFragment fragment = new LivePlayingFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spu = new SharedPreferencesUtil(getActivity());
//        beanList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_live_playing, container, false);
            myReceiver = new MyReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.LOGIN_NOTICE);
            getContext().registerReceiver(myReceiver, intentFilter);
            mainView = (RelativeLayout) v.findViewById(R.id.mainView);
            swiperefresh = (MyPullSwipeRefresh) v.findViewById(R.id.swiperefresh);
            my_list = (ExpandableListView) v.findViewById(R.id.my_list);

            no_info_layout = (LinearLayout) v.findViewById(R.id.no_info_layout);
            no_info_img = (ImageView) v.findViewById(R.id.no_info_img);
            no_info_img.setImageResource(R.drawable.empty_live);
            no_info_text = (TextView) v.findViewById(R.id.no_info_text);
            no_info_text.setText("暂无直播");

            load_fail_layout = (LinearLayout) v.findViewById(R.id.load_fail_layout);
            load_fail_button = (Button) v.findViewById(R.id.load_fail_button);

            mData = new ArrayList<>();
            my_list.setVisibility(View.GONE);
            livePlayingAdapter = new LivePlayingAdapter(getActivity(), mData);
            my_list.setAdapter(livePlayingAdapter);
            swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    initBuilder();
                    data();
                }
            });

            my_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    return true;
                }
            });
            load_fail_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    load_fail_layout.setVisibility(View.GONE);
                    showLoadingView(mainView);
                    initBuilder();
                    data();
                }
            });

        }

        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null) {
            parent.removeView(v);
        }
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void requestData() {
        showLoadingView(mainView);
        initBuilder();
        data();
    }


    private void data() {
        httpGetBuilder1.build();
        if (!TextUtils.isEmpty(spu.getIsLogin())) {
            httpGetBuilder2.addQueryParams("custom", "home");
            httpGetBuilder3.addQueryParams("custom", "home");
        }else {
            httpGetBuilder2.addQueryParams("isBindCourse", "0");
            httpGetBuilder3.addQueryParams("isBindCourse", "0");
        }

        httpGetBuilder2.build();
        httpGetBuilder3.build();

    }

    private void requestLiveData() {
        if (!TextUtils.isEmpty(spu.getIsLogin())) {
            httpGetBuilder4.addQueryParams("custom", "home");
        }else {
            httpGetBuilder4.addQueryParams("isBindCourse", "0");
        }
        httpGetBuilder4.build();
    }


    private String center = "1";

    private void initBuilder() {
        httpGetBuilder1 = new HttpGetBuilder(getActivity());
        httpGetBuilder1.setUrl(UrlsNew.SYSTEM_TIME)
                .setClassObj(SystemTimeResultBean.class)
                .setHttpResult(this)
                .setRequestCode(1000);


        httpGetBuilder2 = new HttpGetBuilder(getActivity());
        httpGetBuilder2.setHttpResult(this)
                .setClassObj(LiveListResultBean.class)
                .addQueryParams("page", "1")
                .addQueryParams("pageSize", "10")
                .addQueryParams("liveStatus", "1")
                .addQueryParams("center", center)
                .setUrl(UrlsNew.GET_LIVE)
                .setRequestCode(1001);

        httpGetBuilder3 = new HttpGetBuilder(getActivity());
        httpGetBuilder3.setHttpResult(this)
                .setClassObj(LiveListResultBean.class)
                .addQueryParams("page", "1")
                .addQueryParams("pageSize", "2")
                .addQueryParams("liveStatus", "2")
                .addQueryParams("center", center)
                .setUrl(UrlsNew.GET_LIVE)
                .setRequestCode(1003);

        httpGetBuilder4 = new HttpGetBuilder(getActivity());
        httpGetBuilder4.setHttpResult(this)
                .setClassObj(LiveListResultBean.class)
                .addQueryParams("page", "1")
                .addQueryParams("pageSize", "2")
                .addQueryParams("liveStatus", "0")
                .addQueryParams("orderBy", "start_time asc")
                .addQueryParams("center", center)
                .setUrl(UrlsNew.GET_LIVE)
                .setRequestCode(1002);
    }


    private List<LiveBean> liveList = new ArrayList<>();
    private List<LiveBean> noticeList = new ArrayList<>();
    private List<LiveBean> videoList = new ArrayList<>();
    private int count = 0;
    private long time;

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {

        load_fail_layout.setVisibility(View.GONE);
        if (requestCode == 1000) {
            SystemTimeResultBean systemTimeResultBean = (SystemTimeResultBean) resultBean;
            time = systemTimeResultBean.getItem().getTime();
            requestLiveData();

        } else if (requestCode == 1001) {
            LiveListResultBean liveListResult = (LiveListResultBean) resultBean;
            liveList = liveListResult.getItems();
            count++;
        } else if (requestCode == 1002) {
            LiveListResultBean liveListResult = (LiveListResultBean) resultBean;
            noticeList = liveListResult.getItems();
            count++;
        } else if (requestCode == 1003) {
            LiveListResultBean liveListResult = (LiveListResultBean) resultBean;
            videoList = liveListResult.getItems();
            count++;
        }


        if (count == 3) {
            hideLoadingView();
            mData.clear();
            if (liveList.size() == 0 && noticeList.size() == 0 && videoList.size() == 0) {
                no_info_layout.setVisibility(View.VISIBLE);
                return;
            }
            no_info_layout.setVisibility(View.GONE);
            if (liveList.size() != 0) {
                LivePlayingBean live = new LivePlayingBean();
                live.setType("live");
                live.setLiveCourseBean(liveList);
                mData.add(live);
            }
            if (noticeList.size() != 0) {
                LivePlayingBean live = new LivePlayingBean();
                live.setType("notice");
                live.setLiveCourseBean(noticeList);
                mData.add(live);
            }
            if (videoList.size() != 0) {
                LivePlayingBean live = new LivePlayingBean();
                live.setType("review");
                live.setVideoBean(videoList);
                mData.add(live);
            }

            my_list.setVisibility(View.VISIBLE);
            swiperefresh.setRefreshing(false);
            livePlayingAdapter.notifyDataSetChanged();
            livePlayingAdapter.setSysTime(time);
            counter();

            for (int i = 0; i < mData.size(); i++) {
                my_list.expandGroup(i);
            }
            count = 0;
        }

    }


    private void counter() {
        if (noticeList.size() == 0) {
            return;
        }

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

//        long leftTime = Long.parseLong(noticeList.get(0).getStart_time());
//        if (noticeList.size() == 2 && leftTime > Long.valueOf(noticeList.get(1).getStart_time())) {
//            leftTime = Long.valueOf(noticeList.get(1).getStart_time());
//        }

        countDownTimer = new CountDownTimer(31536000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                livePlayingAdapter.tickTime();
            }

            @Override
            public void onFinish() {
//                requestData();
            }
        };
        countDownTimer.start();

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        hideLoadingView();
        swiperefresh.setRefreshing(false);
        load_fail_layout.setVisibility(View.VISIBLE);
        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004 || code == Constants.HTTP_CODE_8000) {
            NetworkUtil.httpRestartLogin(LivePlayingFragment.this, mainView);
        } else {
            NetworkUtil.httpNetErrTip(getActivity(), mainView);
        }

    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.LOGIN_NOTICE)) {
                showLoadingView(mainView);
                initBuilder();
                data();
            }
        }
    }

    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(myReceiver);
        super.onDestroy();
    }
}
