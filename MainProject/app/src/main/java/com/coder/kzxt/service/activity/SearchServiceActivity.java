package com.coder.kzxt.service.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.app.utils.Utils;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.course.adapter.SearchMoreCourseDelegate;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.service.adapter.ServiceMainDelegate;
import com.coder.kzxt.service.beans.ServiceBean;
import com.coder.kzxt.service.beans.ServiceResult;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SearchHistoryUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索服务
 */
public class SearchServiceActivity extends BaseActivity implements HttpCallBack
{

    private View mainView;
    private MyRecyclerView listview;
    private ImageView leftImage;
    private TextView searchText;
    private TextView clear_history;
    private EditText titleEdit;

    private BaseRecyclerAdapter listAdapter;
    private MyPullRecyclerView my_list;
    private ScrollView scrollView;
    private MyPullSwipeRefresh myPullSwipeRefresh;

    private String keyword;
    private List<String> historyList = new ArrayList<>();
    private SearchHistoryUtils searchHistoryUtils;


    private ServiceMainDelegate delegate;
    private PullRefreshAdapter pullRefreshAdapter;
    private List<ServiceBean> mData;



    public static void gotoActivity(Context context)
    {
        context.startActivity(new Intent(context, SearchServiceActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_common_search);
        mainView = findViewById(R.id.mainView);
        leftImage = (ImageView) findViewById(R.id.leftImage);
        listview = (MyRecyclerView) findViewById(R.id.history_list);
        titleEdit = (EditText) findViewById(R.id.edit);
        searchText = (TextView) findViewById(R.id.searchText);
        clear_history = (TextView) findViewById(R.id.clear_history);
        clear_history = (TextView) findViewById(R.id.clear_history);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        my_list = (MyPullRecyclerView) findViewById(R.id.my_list);

        keyword = "";

        mData = new ArrayList<>();

        delegate = new ServiceMainDelegate(SearchServiceActivity.this);
        pullRefreshAdapter = new PullRefreshAdapter(this, mData, delegate);
        my_list.setAdapter(pullRefreshAdapter);
        pullRefreshAdapter.setSwipeRefresh(myPullSwipeRefresh);

        searchHistoryUtils = new SearchHistoryUtils(this, Constants.SEARCH_SERVICE);
        if (searchHistoryUtils.getHistory() != null && searchHistoryUtils.getHistory().size() > 0 && !TextUtils.isEmpty(searchHistoryUtils.getHistory().get(0).trim()))
        {
            historyList.addAll(searchHistoryUtils.getHistory());
        }
        listAdapter = new BaseRecyclerAdapter(SearchServiceActivity.this, historyList, new SearchMoreCourseDelegate(SearchServiceActivity.this, searchHistoryUtils, clear_history));
        listview.setAdapter(listAdapter);

        clear_history.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.VISIBLE);

        InintEvent();
        showLoadingView();
        ExecuteAsyncTask();
    }

    private void InintEvent()
    {
        leftImage.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                SearchServiceActivity.this.finish();
            }
        });

        this.searchText.setOnClickListener(new OnClickListener()
        {
            public void onClick(final View v)
            {
                searchClick();
            }

        });

        titleEdit.addTextChangedListener(new TextWatcher()
        {

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (TextUtils.isEmpty(s.toString().trim()) && s.length() == 0)
                {
                    titleEdit.setTextColor(ContextCompat.getColor(SearchServiceActivity.this, R.color.font_gray));
                } else
                {
                    titleEdit.setTextColor(ContextCompat.getColor(SearchServiceActivity.this, R.color.font_black));
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            public void afterTextChanged(Editable s)
            {
                if (listAdapter != null && listAdapter.getShowCount() != 0)
                {
                    scrollView.setVisibility(View.VISIBLE);
                }
            }
        });

        titleEdit.setOnEditorActionListener(new OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    searchClick();
                }
                return true;
            }
        });

        listAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

                keyword = (String) listAdapter.getData().get(position);
                listNotify(keyword);
                showLoadingView();
                ExecuteAsyncTask();
            }
        });

        clear_history.setOnClickListener(new OnClickListener()
        {

            public void onClick(View v)
            {
                searchHistoryUtils.clearHistory();
                historyList.clear();
                listAdapter.resetData(historyList);
            }
        });


        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                pullRefreshAdapter.resetPageIndex();
                ExecuteAsyncTask();

            }
        });

        my_list.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener()
        {
            @Override
            public void addMoreListener()
            {
                // 直接请求服务器
                pullRefreshAdapter.addPageIndex();
                ExecuteAsyncTask();
            }
        });



        pullRefreshAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                ServiceDetailActivity.gotoActivity(SearchServiceActivity.this, mData.get(position));
            }
        });
    }


    private void searchClick()
    {
        keyword = titleEdit.getText().toString().trim();
        if (!TextUtils.isEmpty(keyword))
        {
            showLoadingView();
            listNotify(keyword);
            ExecuteAsyncTask();
            clear_history.setVisibility(View.VISIBLE);
        }

    }

    private void listNotify(String keyword)
    {
        listAdapter.resetData(searchHistoryUtils.resetHistory(keyword));
    }

    public void removeItem(String str)
    {
        searchHistoryUtils.deleteHistoryItem(str);
    }

    private void ExecuteAsyncTask()
    {
        titleEdit.clearFocus();

        new HttpGetBuilder(SearchServiceActivity.this)
                .setHttpResult(this)
                .setClassObj(ServiceResult.class)
                .addQueryParams("page", pullRefreshAdapter.getPageIndex() + "")
                .addQueryParams("pageSize", "20")
                .addQueryParams("state", "2")
                .addQueryParams("title", "all|" + keyword)
                .setUrl(UrlsNew.SERVICE)
                .setRequestCode(1001)
                .build();

    }


    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {

        hideLoadingView();
        ServiceResult liveListResult = (ServiceResult) resultBean;
        pullRefreshAdapter.setPullData(liveListResult.getItems());
        if (pullRefreshAdapter.getShowCount() == 0)
        {
            Utils.makeToast(SearchServiceActivity.this, getResources().getString(R.string.found_no_content));
        } else
        {
            // 有数据的处理
            scrollView.setVisibility(View.GONE);
            myPullSwipeRefresh.setVisibility(View.VISIBLE);
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(titleEdit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {
         hideLoadingView();
        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
        {
            //重新登录
            NetworkUtil.httpRestartLogin(SearchServiceActivity.this, mainView);
        } else
        {
            NetworkUtil.httpNetErrTip(SearchServiceActivity.this, mainView);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (scrollView.getVisibility() == View.VISIBLE)
            {
                scrollView.setVisibility(View.GONE);
                return true;
            }
            finish();
        }
        return true;
    }


}