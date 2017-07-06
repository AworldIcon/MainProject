package com.coder.kzxt.course.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.app.utils.Utils;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.course.adapter.LocalCourseHorizontalDelegate;
import com.coder.kzxt.course.adapter.SearchMoreCourseDelegate;
import com.coder.kzxt.main.beans.CourseMoreBean;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.SearchHistoryUtils;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.app.http.UrlsNew;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索本地课程
 */
public class SearchLocalCourseActivity extends BaseActivity implements HttpCallBack
{
    private LinearLayout jiazai_layout;

    private MyRecyclerView listview;
    private ImageView leftImage;
    private TextView searchText;
    private TextView clear_history;

    private BaseRecyclerAdapter listAdapter;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;
    private ScrollView scrollView;
    private LinearLayout no_info_layout;
    private ImageView no_info_img;
    private TextView no_info_text;

    private LocalCourseHorizontalDelegate orderListStageDelegate;
    private PullRefreshAdapter<CourseMoreBean.ItemsBean> stageadapter;

    private EditText titleEdit;
    private String keyword,model_key,style;
    private List<String> historyList = new ArrayList<>();
    private List<CourseMoreBean.ItemsBean> data = new ArrayList<>();
    private SearchHistoryUtils searchHistoryUtils;

    private SharedPreferencesUtil preferencesUtil;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_all_common_search);
        preferencesUtil=new SharedPreferencesUtil(this);
        model_key=getIntent().getStringExtra("model_key")!=null?getIntent().getStringExtra("model_key"):"";
        style=getIntent().getStringExtra("style")!=null?getIntent().getStringExtra("style"):"";
        leftImage = (ImageView) findViewById(R.id.leftImage);
        listview = (MyRecyclerView) findViewById(R.id.history_list);
        titleEdit = (EditText) findViewById(R.id.edit);
        searchText = (TextView) findViewById(R.id.searchText);
        clear_history = (TextView) findViewById(R.id.clear_history);
        clear_history = (TextView) findViewById(R.id.clear_history);
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        no_info_layout = (LinearLayout)findViewById(R.id.no_info_layout);
        no_info_img = (ImageView) findViewById(R.id.no_info_img);
        no_info_img.setImageResource(R.drawable.empty_search);
        no_info_text = (TextView) findViewById(R.id.no_info_text);
        no_info_text.setText("没有搜索结果");
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        myPullRecyclerView = (MyPullRecyclerView) findViewById(R.id.my_list);


        //stage的初始化
        orderListStageDelegate = new LocalCourseHorizontalDelegate(this,model_key,style);
        stageadapter = new PullRefreshAdapter(this, data, orderListStageDelegate);
        myPullRecyclerView.setAdapter(stageadapter);
        stageadapter.setSwipeRefresh(myPullSwipeRefresh);

        searchHistoryUtils = new SearchHistoryUtils(this, Constants.SEARCH_LIVEL_COURSE);
        if(searchHistoryUtils.getHistory()!=null&&searchHistoryUtils.getHistory().size()>0&&!TextUtils.isEmpty(searchHistoryUtils.getHistory().get(0).trim())){
            historyList.addAll(searchHistoryUtils.getHistory());
        }
        listAdapter = new BaseRecyclerAdapter(SearchLocalCourseActivity.this, historyList, new SearchMoreCourseDelegate(SearchLocalCourseActivity.this,searchHistoryUtils,clear_history));
        listview.setAdapter(listAdapter);
        if(listAdapter.getItemCount()>0){
            clear_history.setVisibility(View.VISIBLE);
            Log.d("zw---","ssss--"+listAdapter.getItemCount());
        }
        scrollView.setVisibility(View.VISIBLE);

        InintEvent();
    }

    private void InintEvent()
    {
        leftImage.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                SearchLocalCourseActivity.this.finish();
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
                    titleEdit.setTextColor(ContextCompat.getColor(SearchLocalCourseActivity.this, R.color.font_gray));
                } else
                {
                    titleEdit.setTextColor(ContextCompat.getColor(SearchLocalCourseActivity.this, R.color.font_black));
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
                scrollView.setVisibility(View.GONE);
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
                clear_history.setVisibility(View.GONE);

            }
        });

        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                stageadapter.resetPageIndex();
                ExecuteAsyncTask();

            }
        });

        myPullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener()
        {
            @Override
            public void addMoreListener()
            {
                // 直接请求服务器
                stageadapter.addPageIndex();
                ExecuteAsyncTask();
            }
        });

//        stageadapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(View view, int position)
//            {
//
//            }
//        });
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

    private void listNotify(String keyword)
    {
        listAdapter.resetData(searchHistoryUtils.resetHistory(keyword));
        if(listAdapter.getItemCount()>0){
            clear_history.setVisibility(View.VISIBLE);
        }
    }

    public void removeItem(String str)
    {
        searchHistoryUtils.deleteHistoryItem(str);
    }

    private void ExecuteAsyncTask()
    {
        titleEdit.clearFocus();

        stageadapter.resetPageIndex();
        new HttpGetBuilder(this)
                .setHttpResult(SearchLocalCourseActivity.this)
                .setClassObj(CourseMoreBean.class)
                .setUrl(UrlsNew.APP_MORE)
                .addQueryParams("orderBy", "create_time desc")
                .addQueryParams("page", stageadapter.getPageIndex() + "")
                .addQueryParams("pageSize", "40")
                .addQueryParams("type", model_key)
                .addQueryParams("keywords", "all|" + keyword)
                .addQueryParams("role",TextUtils.isEmpty(preferencesUtil.getUserRole())?"2":preferencesUtil.getUserRole())
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (requestCode == 4 && resultCode == 5)
        {
//            int position = data.getIntExtra("position", 0);
//            PostersBean bean = (PostersBean) data.getSerializableExtra("bean");
//            stageadapter.addData(position, bean);
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    public static void gotoActivity(Context context)
    {
        context.startActivity(new Intent(context, SearchLocalCourseActivity.class));
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {

        hideLoadingView();
        CourseMoreBean postersResult = (CourseMoreBean) resultBean;
        stageadapter.setTotalPage(postersResult.getPaginate().getPageNum());
        stageadapter.setPullData(postersResult.getItems());


        if (stageadapter.getTotalPage() == 0)
        {
            no_info_layout.setVisibility(View.VISIBLE);
        } else
        {
            no_info_layout.setVisibility(View.GONE);
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
        Utils.makeToast(SearchLocalCourseActivity.this, msg);
    }
}