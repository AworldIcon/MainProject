package com.coder.kzxt.buildwork.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.app.utils.Utils;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.buildwork.adapter.StuUnfinishAdapter;
import com.coder.kzxt.buildwork.adapter.StuUnfinishDelegate;
import com.coder.kzxt.buildwork.adapter.WorkStuAdapter;
import com.coder.kzxt.buildwork.adapter.WorkStuDelegate;
import com.coder.kzxt.buildwork.entity.FinishWorkNum;
import com.coder.kzxt.buildwork.entity.UnfinishNum;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.stuwork.adapter.SearchWorkDelegate;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.SearchHistoryUtils;
import com.app.http.UrlsNew;
import java.util.ArrayList;
import java.util.List;

public class TeaSearchStuActivity extends BaseActivity implements HttpCallBack {
    private LinearLayout jiazai_layout;
    private MyRecyclerView listview;
    private ImageView leftImage;
    private TextView searchText;
    private TextView clear_history;
    private BaseRecyclerAdapter listAdapter;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;
    private ScrollView scrollView;
    private EditText titleEdit;
    private String keyword;
    private List<String> historyList = new ArrayList<>();
    private SearchHistoryUtils searchHistoryUtils;
    private FinishWorkNum finishWorkNum;
    private UnfinishNum unfinishNum;
    private WorkStuAdapter workStuAdapter;
    private WorkStuDelegate workStuDelegate;
    private StuUnfinishAdapter stuUnfinishAdapter;
    private StuUnfinishDelegate stuUnfinishDelegate;
    private ArrayList<FinishWorkNum.ItemsBean>listData=new ArrayList<>();
    private ArrayList<UnfinishNum.ItemsBean>list=new ArrayList<>();
    private String testId;
    private int workType;
    private int position;
    private String class_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tea_search_stu);
        testId = getIntent().getStringExtra("testId");
        workType = getIntent().getIntExtra("workType",1);
        position = getIntent().getIntExtra("position",0);
        class_name=getIntent().getStringExtra("class_name");

        leftImage = (ImageView) findViewById(R.id.leftImage);
        listview = (MyRecyclerView) findViewById(R.id.history_list);
        titleEdit = (EditText) findViewById(R.id.edit);
        searchText = (TextView) findViewById(R.id.searchText);
        clear_history = (TextView) findViewById(R.id.clear_history);
        clear_history = (TextView) findViewById(R.id.clear_history);
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        myPullRecyclerView = (MyPullRecyclerView) findViewById(R.id.myPullRecyclerView);


        //stage的初始化
        if(position==0){
            workStuDelegate=new WorkStuDelegate(this);
            workStuAdapter=new WorkStuAdapter(this,listData,workStuDelegate);
            myPullRecyclerView.setAdapter(workStuAdapter);
            workStuAdapter.setSwipeRefresh(myPullSwipeRefresh);
        }else {
            stuUnfinishDelegate=new StuUnfinishDelegate(this,class_name);
            stuUnfinishAdapter=new StuUnfinishAdapter(this,list,stuUnfinishDelegate);
            myPullRecyclerView.setAdapter(stuUnfinishAdapter);
            stuUnfinishAdapter.setSwipeRefresh(myPullSwipeRefresh);
        }


        if (position==0) {
            searchHistoryUtils = new SearchHistoryUtils(this, Constants.SEARCH_FINISH_STU);
        }else {
            searchHistoryUtils = new SearchHistoryUtils(this, Constants.SEARCH_UNFINISH_STU);
        }
        if(searchHistoryUtils.getHistory()!=null&&searchHistoryUtils.getHistory().size()>0&&!TextUtils.isEmpty(searchHistoryUtils.getHistory().get(0).trim())){
            historyList.addAll(searchHistoryUtils.getHistory());
        }
        listAdapter = new BaseRecyclerAdapter(TeaSearchStuActivity.this, historyList,new SearchWorkDelegate(TeaSearchStuActivity.this,searchHistoryUtils,clear_history));
        listview.setAdapter(listAdapter);
        if(listAdapter.getItemCount()>0){
            clear_history.setVisibility(View.VISIBLE);
        }
        scrollView.setVisibility(View.VISIBLE);

        InintEvent();
    }

    private void InintEvent()
    {
        leftImage.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                TeaSearchStuActivity.this.finish();
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
                    titleEdit.setTextColor(ContextCompat.getColor(TeaSearchStuActivity.this, R.color.font_gray));
                } else
                {
                    titleEdit.setTextColor(ContextCompat.getColor(TeaSearchStuActivity.this, R.color.font_black));
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
                if(position==0){
                    workStuAdapter.resetPageIndex();
                }else {
                    stuUnfinishAdapter.resetPageIndex();
                }
                ExecuteAsyncTask();

            }
        });

        myPullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener()
        {
            @Override
            public void addMoreListener()
            {
                // 直接请求服务器
                if(position==0){
                    workStuAdapter.addPageIndex();
                }else {
                    stuUnfinishAdapter.addPageIndex();
                }
                ExecuteAsyncTask();
            }
        });
        if(position==0) {
            workStuAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    String resultId = finishWorkNum.getItems().get(position).getId();//学生交卷后生成的结果id
                    String name = finishWorkNum.getItems().get(position).getProfile().getNickname();
                    String status_text = finishWorkNum.getItems().get(position).getStatus();//完成状态
                    Intent intent = new Intent(TeaSearchStuActivity.this, CheckStuWorkActivity.class);
                    intent.putExtra("nickname", name);
                    intent.putExtra("resultId", resultId);
//                    intent.putExtra("testid", testId);//原试卷发布到班级时候生成的id，还有一个是试卷本身的id暂时没有传
                    intent.putExtra("name", name);
                    intent.putExtra("status", status_text);
                    intent.putExtra("workType", workType);
                    startActivity(intent);
                }
            });
        }
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
        if(position==0){
            workStuAdapter.resetPageIndex();
        }else {
            stuUnfinishAdapter.resetPageIndex();
        }
        if(position==0){
            new HttpGetBuilder(TeaSearchStuActivity.this).setHttpResult(this)
                    .setUrl(UrlsNew.TEST_RESULT)
                    .setClassObj(FinishWorkNum.class)
                    .addQueryParams("page","1")
                    .addQueryParams("pageSize","20")
                    .addQueryParams("test_id",testId)
                    .addQueryParams("profile^nickname","all|"+keyword)
                    .build();
        }else {
            new HttpGetBuilder(this).setHttpResult(this)
                    .setUrl(UrlsNew.URLHeader+"/test/no_result")
                    .setClassObj(UnfinishNum.class)
                    .addQueryParams("page",stuUnfinishAdapter.getPageIndex()+"")
                    .addQueryParams("pageSize","20")
                    .addQueryParams("test_id",testId)
                    .addQueryParams("profile^nickname","all|"+keyword)
                    .build();
        }

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
        if(position==0){
            finishWorkNum= (FinishWorkNum) resultBean;
            workStuAdapter.setTotalPage(1);
            workStuAdapter.setPullData(finishWorkNum.getItems());
            if (workStuAdapter.getTotalPage() == 0) {
                Utils.makeToast(TeaSearchStuActivity.this, getResources().getString(R.string.found_no_content));
            } else {
                // 有数据的处理
                scrollView.setVisibility(View.GONE);
                myPullSwipeRefresh.setVisibility(View.VISIBLE);
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(titleEdit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }else {
            unfinishNum= (UnfinishNum) resultBean;
            stuUnfinishAdapter.setTotalPage(unfinishNum.getPaginate().getPageNum());
            stuUnfinishAdapter.setPullData(unfinishNum.getItems());
            if (stuUnfinishAdapter.getTotalPage() == 0) {
                Utils.makeToast(TeaSearchStuActivity.this, getResources().getString(R.string.found_no_content));
            } else {
                // 有数据的处理
                scrollView.setVisibility(View.GONE);
                myPullSwipeRefresh.setVisibility(View.VISIBLE);
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(titleEdit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }




    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {
        hideLoadingView();
        Utils.makeToast(TeaSearchStuActivity.this, msg);
    }

}
