package com.coder.kzxt.question.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.fragment.BaseFragment;
import com.coder.kzxt.question.adapter.CourseQuestionAdapter;
import com.coder.kzxt.question.adapter.CourseQuestionDelegate;
import com.coder.kzxt.question.beans.CourseQuestionResultBean;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.app.http.UrlsNew;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 2017/4/18.
 */

public class CourseQuestionFragment extends BaseFragment implements HttpCallBack
{
    private Button load_fail_button;
    private LinearLayout search_jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private ImageView no_info_img;
    private TextView no_info_text;
    private View view;
    private RelativeLayout mainView;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;
    private CourseQuestionAdapter pullRefreshAdapter;
    private int isJoin;

    private CourseQuestionDelegate questionListDelegate;
    private List<CourseQuestionResultBean.ItemsBean> data;
    private int courseId;
    private int type;

    @Override
    protected void requestData()
    {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        courseId = getArguments().getInt("courseId", 0);
        type = getArguments().getInt("type", 0);
        isJoin = getArguments().getInt("isJoin", 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (view == null)
        {
            view = inflater.inflate(R.layout.fragmnet_course_questions, container, false);
            //初始化 view
            initView();
            //网络加载
            search_jiazai_layout.setVisibility(View.VISIBLE);
            httpRequest();
            //点击事件
            initClick();
        }

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null)
        {
            parent.removeView(view);
        }
        return view;
    }

    private void initView()
    {
        load_fail_button = (Button) view.findViewById(R.id.load_fail_button);
        load_fail_layout = (LinearLayout) view.findViewById(R.id.load_fail_layout);
        no_info_layout = (LinearLayout) view.findViewById(R.id.no_info_layout);
        search_jiazai_layout = (LinearLayout) view.findViewById(R.id.jiazai_layout);

        no_info_img = (ImageView) view.findViewById(R.id.no_info_img);
        no_info_text = (TextView) view.findViewById(R.id.no_info_text);
        no_info_img.setImageResource(R.drawable.no_course_questions);
        no_info_img.setPadding(0, 0, 0, 20);
        no_info_text.setPadding(30, 10, 0, 0);
        no_info_text.setText("暂无相关问答~");


        mainView = (RelativeLayout) view.findViewById(R.id.course_fragment);
        myPullSwipeRefresh = (MyPullSwipeRefresh) view.findViewById(R.id.myPullSwipeRefresh);
        myPullRecyclerView = (MyPullRecyclerView) view.findViewById(R.id.myPullRecyclerView);
        data = new ArrayList<>();
        questionListDelegate = new CourseQuestionDelegate(getContext(), type, isJoin);
        pullRefreshAdapter = new CourseQuestionAdapter(getContext(), data, questionListDelegate);
        myPullRecyclerView.setAdapter(pullRefreshAdapter);
        pullRefreshAdapter.setSwipeRefresh(myPullSwipeRefresh);
    }

    private void initClick()
    {
        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                pullRefreshAdapter.resetPageIndex();
                httpRequest();
            }
        });

        myPullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener()
        {
            @Override
            public void addMoreListener()
            {
                pullRefreshAdapter.addPageIndex();
                httpRequest();
            }
        });
        load_fail_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                load_fail_layout.setVisibility(View.GONE);
                no_info_layout.setVisibility(View.GONE);
                search_jiazai_layout.setVisibility(View.VISIBLE);
                pullRefreshAdapter.resetPageIndex();
                httpRequest();
            }
        });
    }

    private void httpRequest()
    {
        no_info_layout.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);

        new HttpGetBuilder(getContext())
                .setHttpResult(this)
                .setClassObj(CourseQuestionResultBean.class)
                .setUrl(UrlsNew.GET_QUESTION_QUESTION)
                .addQueryParams("page", pullRefreshAdapter.getPageIndex() + "")
                .addQueryParams("pageSize", "20")
                .addQueryParams("orderBy", "create_time desc")
                .addQueryParams("type", type + "")
                .addQueryParams("course_id", courseId + "")
                .build();
    }


    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {
        myPullRecyclerView.setVisibility(View.VISIBLE);
        search_jiazai_layout.setVisibility(View.GONE);
        CourseQuestionResultBean courseQuestionResultBean = (CourseQuestionResultBean) resultBean;
        pullRefreshAdapter.setTotalPage(courseQuestionResultBean.getPaginate().getPageNum());
        pullRefreshAdapter.setPullData(courseQuestionResultBean.getItems());
        if (pullRefreshAdapter.getItemCount() == 0)
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
        search_jiazai_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
        myPullSwipeRefresh.setRefreshing(false);
        myPullRecyclerView.setVisibility(View.GONE);
        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
        {
            NetworkUtil.httpRestartLogin(getActivity(), mainView);
        } else
        {
            load_fail_layout.setVisibility(View.VISIBLE);
        }
    }

    public void refresh()
    {
//        search_jiazai_layout.setVisibility(View.VISIBLE);
        if (pullRefreshAdapter != null)
        {
            pullRefreshAdapter.resetPageIndex();
            httpRequest();
        }

    }
}
