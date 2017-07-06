package com.coder.kzxt.main.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.course.adapter.SearchMoreCourseDelegate;
import com.coder.kzxt.course.beans.CourseBean;
import com.coder.kzxt.main.delegate.CourseDelegate;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.SearchHistoryUtils;
import com.coder.kzxt.utils.ToastUtils;
import com.app.http.UrlsNew;

import java.util.ArrayList;
import java.util.List;

public class SearchAllCourseActivity extends BaseActivity implements HttpCallBack {

    private MyRecyclerView listview;
    private ImageView leftImage;
    private TextView searchText;
    private TextView clear_history;
    private EditText titleEdit;


    private LinearLayout no_info_layout;
    private ImageView no_info_img;
    private TextView no_info_text;

    private BaseRecyclerAdapter listAdapter;
    private MyPullRecyclerView my_list;
    private ScrollView scrollView;
    private MyPullSwipeRefresh myPullSwipeRefresh;

    private String keyword;
    private List<String> historyList = new ArrayList<>();
    private SearchHistoryUtils searchHistoryUtils;
    private CourseDelegate courseDelegate;
    private PullRefreshAdapter livePlayingAdapter;
    private List<CourseBean.Course> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_all_common_search);
        keyword = getIntent().getStringExtra("keyword");
        leftImage = (ImageView) findViewById(R.id.leftImage);
        mData = new ArrayList<>();
        listview = (MyRecyclerView) findViewById(R.id.history_list);
        titleEdit = (EditText) findViewById(R.id.edit);
        searchText = (TextView) findViewById(R.id.searchText);
        clear_history = (TextView) findViewById(R.id.clear_history);
        clear_history = (TextView) findViewById(R.id.clear_history);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        my_list = (MyPullRecyclerView) findViewById(R.id.my_list);

        no_info_layout = (LinearLayout)findViewById(R.id.no_info_layout);
        no_info_img = (ImageView) findViewById(R.id.no_info_img);
        no_info_img.setImageResource(R.drawable.empty_search);
        no_info_text = (TextView) findViewById(R.id.no_info_text);
        no_info_text.setText("没有搜索结果");


        courseDelegate = new CourseDelegate(SearchAllCourseActivity.this,keyword);
        livePlayingAdapter = new PullRefreshAdapter(this, mData, courseDelegate);
        my_list.setAdapter(livePlayingAdapter);
        livePlayingAdapter.setSwipeRefresh(myPullSwipeRefresh);

        searchHistoryUtils = new SearchHistoryUtils(this, Constants.SEARCH_LOCAL_COURSE);
        if (searchHistoryUtils.getHistory() != null && searchHistoryUtils.getHistory().size() > 0 && !TextUtils.isEmpty(searchHistoryUtils.getHistory().get(0).trim()))
        {
            historyList.addAll(searchHistoryUtils.getHistory());
        }
        listAdapter = new BaseRecyclerAdapter(SearchAllCourseActivity.this, historyList, new SearchMoreCourseDelegate(SearchAllCourseActivity.this, searchHistoryUtils,clear_history));
        listview.setAdapter(listAdapter);

        clear_history.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.VISIBLE);

        InintEvent();
        showLoadingView();
        ExecuteAsyncTask();
    }

    private void InintEvent()
    {
        leftImage.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
               finish();
            }
        });

        this.searchText.setOnClickListener(new View.OnClickListener()
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
                    titleEdit.setTextColor(ContextCompat.getColor(SearchAllCourseActivity.this, R.color.font_gray));
                } else
                {
                    titleEdit.setTextColor(ContextCompat.getColor(SearchAllCourseActivity.this, R.color.font_black));
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
                    no_info_layout.setVisibility(View.GONE);
                }
            }
        });

        titleEdit.setOnEditorActionListener(new TextView.OnEditorActionListener()
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
                scrollView.setVisibility(View.GONE);
                keyword = (String) listAdapter.getData().get(position);
                listNotify(keyword);
                showLoadingView();
                ExecuteAsyncTask();
            }
        });

        clear_history.setOnClickListener(new View.OnClickListener()
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


    private void listNotify(String keyword)
    {
        listAdapter.resetData(searchHistoryUtils.resetHistory(keyword));
    }

    public void removeItem(String str)
    {
        searchHistoryUtils.deleteHistoryItem(str);
    }

    private void searchClick()
    {
        keyword = titleEdit.getText().toString().trim();
        if (!TextUtils.isEmpty(keyword))
        {
            scrollView.setVisibility(View.GONE);
            showLoadingView();
            listNotify(keyword);
            ExecuteAsyncTask();
            clear_history.setVisibility(View.VISIBLE);
        }

    }

    private void ExecuteAsyncTask(){
        titleEdit.clearFocus();
        new HttpGetBuilder(SearchAllCourseActivity.this)
                .setUrl(UrlsNew.GET_SEARCH_ALL_LIST)
                .setHttpResult(SearchAllCourseActivity.this)
                .setClassObj(CourseBean.class)
                .addQueryParams("page", livePlayingAdapter.getPageIndex() + "")
                .addQueryParams("pageSize", "20")
                .addQueryParams("status", "2")
                .setRequestCode(1)
                .addQueryParams("title", "all|" + keyword)
                .build();
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        hideLoadingView();
        CourseBean  courseBean = (CourseBean) resultBean;
        livePlayingAdapter.setTotalPage(courseBean.getPaginate().getPageNum());
        livePlayingAdapter.setPullData(courseBean.getItems());
        courseDelegate.setKeyword(keyword);
        if (livePlayingAdapter.getShowCount() == 0) {
            no_info_layout.setVisibility(View.VISIBLE);
        } else {
            // 有数据的处理
            no_info_layout.setVisibility(View.GONE);
            scrollView.setVisibility(View.GONE);
            myPullSwipeRefresh.setVisibility(View.VISIBLE);
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(titleEdit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        hideLoadingView();
        ToastUtils.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
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
