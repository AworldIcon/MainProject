package com.coder.kzxt.main.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.main.activity.MainActivity;
import com.coder.kzxt.main.adapter.MainBannerAdapter;
import com.coder.kzxt.main.adapter.MainCoustomAdapter;
import com.coder.kzxt.main.adapter.MainFamousTeacherAdapter;
import com.coder.kzxt.main.adapter.MainFreeCourseAdapter;
import com.coder.kzxt.main.adapter.MainInformationAdapter;
import com.coder.kzxt.main.adapter.MainLiveCourseAdapter;
import com.coder.kzxt.main.adapter.MainMyCourseAdapter;
import com.coder.kzxt.main.adapter.MainNewCourseAdapter;
import com.coder.kzxt.main.adapter.MainNominateAdapter;
import com.coder.kzxt.main.adapter.MainPublicCourseAdapter;
import com.coder.kzxt.main.adapter.MainSchoolCourseAdapter;
import com.coder.kzxt.main.adapter.MainSellerCourseAdapter;
import com.coder.kzxt.main.adapter.MainTeacherCourseAdapter;
import com.coder.kzxt.main.adapter.MainTopicAdapter;
import com.coder.kzxt.main.beans.MainModelBean;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.sign.utils.CommitOffline;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.app.http.UrlsNew;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/2/3.
 */
public class MainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, HttpCallBack
{
    private View v;
    private SharedPreferencesUtil preferencesUtil;
    private MainActivity mainActivity;
    private RelativeLayout mainfragmentLayout;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private LinearLayout jiazai_layout;
    private LinearLayout loadfail_layout;
    private Button loadfail_button;
    private final static String
            BANNER = "BANNER", TEACHER = "TEACHER", LIVE = "LIVE",
            SCHOOL_COURSE = "SCHOOL_COURSE", INFORMATION = "INFORMATION",
            NOMINATE = "NOMINATE", PUBLIC_COURSE = "PUBLIC_COURSE",
            NEW_COURSE = "NEW_COURSE", TOPIC = "TOPIC",
            FREE_COURSE = "FREE_COURSE", SELLER = "SELLER",
            STUDENT_SELLER = "STUDENT_SELLER", STUDENT_COURSE = "STUDENT_COURSE",
            TEACHER_SELLER = "TEACHER_SELLER", TEACHER_COURSE = "TEACHER_COURSE", CUSTOM_83591 = "CUSTOM_83591";

    public static MainFragment newInstance(String param)
    {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param);
        fragment.setArguments(args);
        return fragment;
    }

    private LinearLayout mainView;
    private MainLiveCourseAdapter courseLiveAdapter;
    private MainSchoolCourseAdapter schoolCourseAdapter;
    private MainMyCourseAdapter myCourseAdapter;
    private MainBannerAdapter bannerAdapter;
    private MainFamousTeacherAdapter famousTeacherAdapter;
    private MainInformationAdapter informationAdapter;
    private MainNominateAdapter nominateAdAdapter;
    private MainPublicCourseAdapter publicCourseAdapter;
    private MainNewCourseAdapter newCoursedAdapter;
    private MainFreeCourseAdapter freeCourseAdapter;
    private MainSellerCourseAdapter sellerCourseAdapter;
    private MainTopicAdapter topicAdapter;
    private MainTeacherCourseAdapter teacherCourseAdapter;
    private ScrollView scrollView;
    private HashMap<String, MainCoustomAdapter> adapterHashMap = new HashMap<>();
    private MainModelBean modelBean;
    private MyReceiver myReceiver;
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 1:
                    getPageModel();
                    break;
                case 200:
                    addView();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (v == null)
        {
            v = inflater.inflate(R.layout.fragment_main, container, false);
        }
        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null)
        {
            parent.removeView(v);
        }
        View view = inflater.inflate(R.layout.mainly, null);
        myReceiver=new MyReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(Constants.LOGIN_NOTICE);
        getContext().registerReceiver(myReceiver,intentFilter);
        preferencesUtil=new SharedPreferencesUtil(getContext());
        mainView = (LinearLayout) view.findViewById(R.id.mainLy);
        mainfragmentLayout = (RelativeLayout) v.findViewById(R.id.mainFragment);
        scrollView = (ScrollView) v.findViewById(R.id.scollview);
        myPullSwipeRefresh = (MyPullSwipeRefresh) v.findViewById(R.id.swiperefresh);
        jiazai_layout = (LinearLayout) v.findViewById(R.id.jiazai_layout);
        loadfail_layout = (LinearLayout) v.findViewById(R.id.load_fail_layout);
        loadfail_button = (Button) v.findViewById(R.id.load_fail_button);
        jiazai_layout.setVisibility(View.VISIBLE);
        myPullSwipeRefresh.setOnRefreshListener(this);
        getPageModel();
        loadfail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jiazai_layout.setVisibility(View.VISIBLE);
                loadfail_layout.setVisibility(View.GONE);
                getPageModel();

            }
        });
        //检查离线签到 进行提交
        new CommitOffline(getActivity());
        return v;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    protected void requestData()
    {

    }


    @Override
    public void onRefresh()
    {
        getPageModel();
    }

    public void getPageModel()
    {
        new HttpGetBuilder(getContext()).setHttpResult(this).setClassObj(MainModelBean.class).setUrl(UrlsNew.GLOBAL_CONF_ACTION).addQueryParams("role", TextUtils.isEmpty(preferencesUtil.getUserRole())?"2":preferencesUtil.getUserRole()).build();
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {  loadfail_layout.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
        myPullSwipeRefresh.setRefreshing(false);
        modelBean = (MainModelBean) resultBean;

        for (int i = 0; i < modelBean.getItems().size(); i++)
        {
            String model_key = modelBean.getItems().get(i).getModule_key();
            MainModelBean.ItemsBean model_style = modelBean.getItems().get(i);
            List<MainModelBean.ItemsBean.ListBean> listBean = modelBean.getItems().get(i).getList();
            if (listBean!=null&&listBean.size() > 0)
            {
                if (model_key.contains("CUSTOM_"))
                {
                    adapterHashMap.put(model_key, new MainCoustomAdapter(getActivity(), model_style, listBean));
                }
                switch (model_key)
                {
                    case BANNER:
                        //首页banner mainBannerAdapter
                        bannerAdapter = new MainBannerAdapter(getActivity(), model_style, listBean);
                        break;
                    case TEACHER:
                        //名师推荐
                        famousTeacherAdapter = new MainFamousTeacherAdapter(getActivity(), model_style, listBean);
                        break;
                    case LIVE:
                        //直播课程
                        courseLiveAdapter = new MainLiveCourseAdapter(getActivity(), model_style, listBean);
                        break;
                    case SCHOOL_COURSE:
                        //本校课程
                        schoolCourseAdapter = new MainSchoolCourseAdapter(getActivity(), model_style, listBean);
                        break;
                    case INFORMATION:
                        //最新资讯  mainInFormationAdapter
                        informationAdapter = new MainInformationAdapter(getActivity(), model_style, listBean);
                        break;
                    case NOMINATE:
                        //推荐课程
                        nominateAdAdapter = new MainNominateAdapter(getActivity(), model_style, listBean);
                        break;
                    case PUBLIC_COURSE:
                        //公开课联盟
                        publicCourseAdapter = new MainPublicCourseAdapter(getActivity(), model_style, listBean);
                        break;
                    case NEW_COURSE:
                        //最新课程  mainNewCourseAdapter
                        newCoursedAdapter = new MainNewCourseAdapter(getActivity(), model_style, listBean);
                        break;
                    case FREE_COURSE:
                        //免费好课  mainFreeCourseAdapter
                        freeCourseAdapter = new MainFreeCourseAdapter(getActivity(), model_style, listBean);
                        break;
                    case SELLER:
                        //畅销好课  mainSellerCourseAdapter
                        sellerCourseAdapter = new MainSellerCourseAdapter(getActivity(), model_style, listBean);
                        break;
                    case TOPIC:
                        //精品區
                        topicAdapter = new MainTopicAdapter(getActivity(), model_style, listBean);
                        break;
                    case STUDENT_COURSE:
                        //我的课程  mainMyCourseAdapter
                        myCourseAdapter = new MainMyCourseAdapter(getActivity(), model_style, listBean);
                        break;

                    case TEACHER_COURSE:
                        //我的授课  mainTeacherCourseAdapter
                        teacherCourseAdapter = new MainTeacherCourseAdapter(getActivity(), model_style, listBean);
                        break;
                    default:

                        break;
                }
            }
        }
        handler.sendEmptyMessageDelayed(200, 200);
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {
        myPullSwipeRefresh.setRefreshing(false);
        jiazai_layout.setVisibility(View.GONE);
        loadfail_layout.setVisibility(View.VISIBLE);
        NetworkUtil.httpNetErrTip(getContext(),mainfragmentLayout);
        scrollView.setVisibility(View.GONE);

    }

    /**
     * 把所有的数据添加上去
     */
    private void addView()
    {
        mainView.removeAllViews();
        for (int i = 0; i < modelBean.getItems().size(); i++)
        {
            String model_key = modelBean.getItems().get(i).getModule_key();
            switch (model_key)
            {
                case BANNER:
                    if (bannerAdapter != null && modelBean.getItems().get(i).getList().size() > 0)
                    {
                        mainView.addView(bannerAdapter.getView());
                    }
                    break;
                case TEACHER:
                    if (famousTeacherAdapter != null && modelBean.getItems().get(i).getList().size() > 0)
                    {
                        mainView.addView(famousTeacherAdapter.getView());
                    }
                    break;
                case LIVE:
                    //直播课程  mainLiveCourseAdapter
                    if (courseLiveAdapter != null && modelBean.getItems().get(i).getList().size() > 0)
                    {

                        mainView.addView(courseLiveAdapter.getView());
                    }
                    break;
                case SCHOOL_COURSE:
                    //本校课程  mainSchoolCourseAdapter
                    if (schoolCourseAdapter != null && modelBean.getItems().get(i).getList().size() > 0)
                    {
                        mainView.addView(schoolCourseAdapter.getView());
                    }
                    break;
                case INFORMATION:
                    //最新资讯  mainInFormationAdapter
                    if (informationAdapter != null && modelBean.getItems().get(i).getList().size() > 0)
                    {
                        mainView.addView(informationAdapter.getView());
                    }
                    break;
                case NOMINATE:
                    //推荐课程  mainNominateAdapter
                    if (nominateAdAdapter != null && modelBean.getItems().get(i).getList().size() > 0)
                    {
                        mainView.addView(nominateAdAdapter.getView());
                    }
                    break;
                case PUBLIC_COURSE:
                    //公开课联盟  mainPublicCourseAdapter
                    if (publicCourseAdapter != null && modelBean.getItems().get(i).getList().size() > 0)
                    {
                        mainView.addView(publicCourseAdapter.getView());
                    }
                    break;
                case NEW_COURSE:
                    //最新课程  mainNewCourseAdapter
                    if (newCoursedAdapter != null && modelBean.getItems().get(i).getList().size() > 0)
                    {
                        mainView.addView(newCoursedAdapter.getView());
                    }
                    break;
                case FREE_COURSE:
                    //免费好课  mainFreeCourseAdapter
                    if (freeCourseAdapter != null && modelBean.getItems().get(i).getList().size() > 0)
                    {
                        mainView.addView(freeCourseAdapter.getView());
                    }
                    break;
                case SELLER:
                    //畅销好课  mainSellerCourseAdapter
                    if (sellerCourseAdapter != null && modelBean.getItems().get(i).getList().size() > 0)
                    {
                        mainView.addView(sellerCourseAdapter.getView());
                    }
                    break;
                case TOPIC:
                    //专题推荐
                    if (topicAdapter != null && modelBean.getItems().get(i).getList().size() > 0)
                    {
                        mainView.addView(topicAdapter.getView());
                    }
                    break;
                case STUDENT_COURSE:
                    //我的课程  mainMyCourseAdapter
                    if (myCourseAdapter != null && modelBean.getItems().get(i).getList().size() > 0)
                    {
                        mainView.addView(myCourseAdapter.getView());
                    }
                    break;
                case TEACHER_COURSE:
                    //我的授课  mainTeacherCourseAdapter
                    if (teacherCourseAdapter != null && modelBean.getItems().get(i).getList().size() > 0)
                    {
                        mainView.addView(teacherCourseAdapter.getView());
                    }
                    break;
                default:
                    break;
            }
            if (adapterHashMap.containsKey(model_key))
            {
                //if (mainCoustomAdapter != null && mainCoustomAdapter.getView() != null) {
                mainView.addView(adapterHashMap.get(model_key).getView());
                // }
            }
        }
        jiazai_layout.setVisibility(View.GONE);
        scrollView.removeAllViews();
        scrollView.addView(mainView);
    }

    public class MyReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Constants.LOGIN_NOTICE)){
            jiazai_layout.setVisibility(View.VISIBLE);
            getPageModel();
        }
    }
    }

    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(myReceiver);
        super.onDestroy();
    }
}
