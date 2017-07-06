package com.coder.kzxt.stuwork.activity;

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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.app.utils.Utils;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.stuwork.adapter.SearchWorkDelegate;
import com.coder.kzxt.stuwork.adapter.StuCourseWorkAdapter;
import com.coder.kzxt.stuwork.adapter.StuCourseWorkDelegate;
import com.coder.kzxt.stuwork.entity.StuCourseWorkBean;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.SearchHistoryUtils;
import com.app.http.UrlsNew;

import java.util.ArrayList;
import java.util.List;

public class StuSearchWorkActivity extends BaseActivity implements HttpCallBack {
    private MyRecyclerView listview;
    private ImageView leftImage;
    private TextView searchText;
    private TextView clear_history;
    private BaseRecyclerAdapter listAdapter;
    private ScrollView scrollView;
    private EditText titleEdit;
    private String keyword;
    private List<String> historyList = new ArrayList<>();
    private SearchHistoryUtils searchHistoryUtils;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;
    private StuCourseWorkAdapter stuCourseWorkAdapter;
    private StuCourseWorkDelegate stuCourseWorkDelegate;
    private List<StuCourseWorkBean.ItemsBean>itemsBeanList=new ArrayList<>();
    private int workType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tea_search_stu);
        workType = getIntent().getIntExtra("workType",1);
        leftImage = (ImageView) findViewById(R.id.leftImage);
        listview = (MyRecyclerView) findViewById(R.id.history_list);
        titleEdit = (EditText) findViewById(R.id.edit);
        searchText = (TextView) findViewById(R.id.searchText);
        clear_history = (TextView) findViewById(R.id.clear_history);
        clear_history = (TextView) findViewById(R.id.clear_history);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        myPullRecyclerView = (MyPullRecyclerView) findViewById(R.id.myPullRecyclerView);


        //stage的初始化
            stuCourseWorkDelegate=new StuCourseWorkDelegate(this,workType);
            stuCourseWorkAdapter=new StuCourseWorkAdapter(this,itemsBeanList,stuCourseWorkDelegate);
            myPullRecyclerView.setAdapter(stuCourseWorkAdapter);
            stuCourseWorkAdapter.setSwipeRefresh(myPullSwipeRefresh);
        
            searchHistoryUtils = new SearchHistoryUtils(this, Constants.SEARCH_STU_WORK);

        if(searchHistoryUtils.getHistory()!=null&&searchHistoryUtils.getHistory().size()>0&&!TextUtils.isEmpty(searchHistoryUtils.getHistory().get(0).trim())){
            historyList.addAll(searchHistoryUtils.getHistory());
        }
        listAdapter = new BaseRecyclerAdapter(StuSearchWorkActivity.this, historyList, new SearchWorkDelegate(StuSearchWorkActivity.this,searchHistoryUtils,clear_history));
        listview.setAdapter(listAdapter);
        if(listAdapter.getItemCount()>0){
            clear_history.setVisibility(View.VISIBLE);
        }        scrollView.setVisibility(View.VISIBLE);

        InintEvent();
    }

    private void InintEvent()
    {
        leftImage.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                StuSearchWorkActivity.this.finish();
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
                    titleEdit.setTextColor(ContextCompat.getColor(StuSearchWorkActivity.this, R.color.font_gray));
                } else
                {
                    titleEdit.setTextColor(ContextCompat.getColor(StuSearchWorkActivity.this, R.color.font_black));
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
                clear_history.setVisibility(View.GONE);
            }
        });

        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                    stuCourseWorkAdapter.resetPageIndex();
                ExecuteAsyncTask();

            }
        });

        myPullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener()
        {
            @Override
            public void addMoreListener()
            {
                // 直接请求服务器
                    stuCourseWorkAdapter.addPageIndex();
               
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
        stuCourseWorkAdapter.resetPageIndex();

        new HttpGetBuilder(this).setClassObj(StuCourseWorkBean.class).setUrl(UrlsNew.URLHeader+"/my_test")
                .setHttpResult(this)
                .addQueryParams("page",stuCourseWorkAdapter.getPageIndex()+"")
                .addQueryParams("pageSize","20")
                .addQueryParams("orderBy","create_time desc")
                .addQueryParams("course_id","4")
                .addQueryParams("type",workType+"")
                .build();
        

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if ( resultCode == 200)
        {//是否需要
            ExecuteAsyncTask();

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

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {

            hideLoadingView();
        StuCourseWorkBean courseWorkBean= (StuCourseWorkBean) resultBean;
        stuCourseWorkAdapter.setTotalPage(courseWorkBean.getPaginate().getPageNum());
        stuCourseWorkAdapter.setPullData(courseWorkBean.getItems());
            if (stuCourseWorkAdapter.getTotalPage() == 0) {
                Utils.makeToast(StuSearchWorkActivity.this, getResources().getString(R.string.found_no_content));
            } else {
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
        Utils.makeToast(StuSearchWorkActivity.this, msg);
    }

}
