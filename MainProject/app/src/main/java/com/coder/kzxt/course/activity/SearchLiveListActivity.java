package com.coder.kzxt.course.activity;

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
import com.app.http.builders.HttpGetBuilder;
import com.app.utils.Utils;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.course.adapter.SearchMoreCourseDelegate;
import com.coder.kzxt.course.delegate.LiveDelegate;
import com.coder.kzxt.main.beans.LiveListResultBean;
import com.coder.kzxt.main.beans.LivePlayingBean;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.SearchHistoryUtils;
import com.app.http.UrlsNew;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索正在直播 即将直播
 */
public class SearchLiveListActivity extends BaseActivity implements HttpCallBack
{

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


    private LiveDelegate delegate;
    private PullRefreshAdapter livePlayingAdapter;
    private List<LivePlayingBean> mData;

    private String type;

    public static void gotoActivity(Context context, String type, String keyword)
    {
        context.startActivity(new Intent(context, SearchLiveListActivity.class).putExtra("type", type).putExtra("keyword", keyword));
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_common_search);
        leftImage = (ImageView) findViewById(R.id.leftImage);
        listview = (MyRecyclerView) findViewById(R.id.history_list);
        titleEdit = (EditText) findViewById(R.id.edit);
        searchText = (TextView) findViewById(R.id.searchText);
        clear_history = (TextView) findViewById(R.id.clear_history);
        clear_history = (TextView) findViewById(R.id.clear_history);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        my_list = (MyPullRecyclerView) findViewById(R.id.my_list);

        type = getIntent().getStringExtra("type");
        keyword = getIntent().getStringExtra("keyword");

        mData = new ArrayList<>();

        if(type.equals("live")){
            delegate = new LiveDelegate(SearchLiveListActivity.this,0,keyword);
        }else if(type.equals("notice")){
            delegate = new LiveDelegate(SearchLiveListActivity.this,1,keyword);
        }else {
            delegate = new LiveDelegate(SearchLiveListActivity.this,2,keyword);
        }

        livePlayingAdapter = new PullRefreshAdapter(this, mData, delegate);
        my_list.setAdapter(livePlayingAdapter);
        livePlayingAdapter.setSwipeRefresh(myPullSwipeRefresh);

        searchHistoryUtils = new SearchHistoryUtils(this, Constants.SEARCH_LOCAL_COURSE);
        if (searchHistoryUtils.getHistory() != null && searchHistoryUtils.getHistory().size() > 0 && !TextUtils.isEmpty(searchHistoryUtils.getHistory().get(0).trim()))
        {
            historyList.addAll(searchHistoryUtils.getHistory());
        }
        listAdapter = new BaseRecyclerAdapter(SearchLiveListActivity.this, historyList, new SearchMoreCourseDelegate(SearchLiveListActivity.this, searchHistoryUtils,clear_history));
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
                SearchLiveListActivity.this.finish();
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
                    titleEdit.setTextColor(ContextCompat.getColor(SearchLiveListActivity.this, R.color.font_gray));
                } else
                {
                    titleEdit.setTextColor(ContextCompat.getColor(SearchLiveListActivity.this, R.color.font_black));
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
                livePlayingAdapter.resetPageIndex();
                ExecuteAsyncTask();

            }
        });

        my_list.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener()
        {
            @Override
            public void addMoreListener()
            {
                // 直接请求服务器
                livePlayingAdapter.addPageIndex();
                ExecuteAsyncTask();
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

        if (type.equals("live")) {

            new HttpGetBuilder(SearchLiveListActivity.this)
                    .setHttpResult(this)
                    .setClassObj(LiveListResultBean.class)
                    .addQueryParams("page", livePlayingAdapter.getPageIndex() + "")
                    .addQueryParams("pageSize", "20")
                    .addQueryParams("liveStatus", "1")
                    .addQueryParams("live_title", "all|"+keyword)
                    .addQueryParams("center", "1")
                    .addQueryParams("isBindCourse","0")
                    .setUrl(UrlsNew.GET_LIVE)
                    .setRequestCode(1001)
                    .build();
        } else if(type.equals("notice")){
            new HttpGetBuilder(SearchLiveListActivity.this)
                    .setHttpResult(this)
                    .setClassObj(LiveListResultBean.class)
                    .addQueryParams("page", livePlayingAdapter.getPageIndex() + "")
                    .addQueryParams("pageSize", "20")
                    .addQueryParams("liveStatus", "0")
                    .addQueryParams("live_title","all|"+keyword)
                    .addQueryParams("center", "1")
                    .addQueryParams("isBindCourse","0")
                    .setUrl(UrlsNew.GET_LIVE)
                    .setRequestCode(1001)
                    .build();
        }else if(type.equals("all")){

            new HttpGetBuilder(SearchLiveListActivity.this)
                    .setHttpResult(this)
                    .setClassObj(LiveListResultBean.class)
                    .addQueryParams("page", livePlayingAdapter.getPageIndex() + "")
                    .addQueryParams("pageSize", "20")
                    .addQueryParams("live_title","all|"+keyword)
                    .addQueryParams("center", "1")
                    .addQueryParams("isBindCourse","0")
                    .setUrl(UrlsNew.GET_LIVE)
                    .setRequestCode(1001)
                    .build();

        }else {
            new HttpGetBuilder(SearchLiveListActivity.this)
                    .setHttpResult(this)
                    .setClassObj(LiveListResultBean.class)
                    .addQueryParams("page", livePlayingAdapter.getPageIndex() + "")
                    .addQueryParams("pageSize", "20")
                    .addQueryParams("liveStatus", "2")
                    .addQueryParams("live_title", "all|"+keyword)
                    .addQueryParams("center", "1")
                    .addQueryParams("isBindCourse","0")
                    .setUrl(UrlsNew.GET_LIVE)
                    .setRequestCode(1001)
                    .build();
        }
    }


    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {


        hideLoadingView();
        LiveListResultBean liveListResult = (LiveListResultBean) resultBean;
        livePlayingAdapter.setPullData(liveListResult.getItems());
        delegate.setKeyword(keyword);
        if (livePlayingAdapter.getShowCount() == 0)
        {
            Utils.makeToast(SearchLiveListActivity.this, getResources().getString(R.string.found_no_content));
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
        Utils.makeToast(SearchLiveListActivity.this, msg);
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