package com.coder.kzxt.buildwork.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.coder.kzxt.buildwork.adapter.QuestionResourseAdapter;
import com.coder.kzxt.buildwork.adapter.QuestionResourseDelegate;
import com.coder.kzxt.buildwork.adapter.WorkBankAdapter;
import com.coder.kzxt.buildwork.adapter.WorkBankDelegate;
import com.coder.kzxt.buildwork.entity.AlreadyPublishBean;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.stuwork.adapter.SearchWorkDelegate;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.SearchHistoryUtils;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.app.http.UrlsNew;
import java.util.ArrayList;
import java.util.List;

public class TeaSearchWorkActivity extends BaseActivity implements HttpCallBack {
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
    private QuestionResourseAdapter questionResourseAdapter;
    private QuestionResourseDelegate questionResourseDelegate;
    private WorkBankAdapter workBankAdapter;
    private WorkBankDelegate workBankDelegate;
    private List<AlreadyPublishBean.ItemsBean> itemsBeanList=new ArrayList<>();
    private int courseId;
    private int classId;
    private int workType;
    private int position;
    private AlreadyPublishBean publishBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tea_search_work);
        courseId = getIntent().getIntExtra("course_id", 0);
        classId = getIntent().getIntExtra("classId", 0);
        workType = getIntent().getIntExtra("workType",1);
        position = getIntent().getIntExtra("position",0);

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
            questionResourseDelegate=new QuestionResourseDelegate(this,myPullRecyclerView,courseId,workType);
            questionResourseAdapter=new QuestionResourseAdapter(this,itemsBeanList,questionResourseDelegate);
            myPullRecyclerView.setAdapter(questionResourseAdapter);
            questionResourseAdapter.setSwipeRefresh(myPullSwipeRefresh);
        }else {
            workBankDelegate=new WorkBankDelegate(this,myPullRecyclerView,workType,courseId);
            workBankAdapter=new WorkBankAdapter(this,itemsBeanList,workBankDelegate);
            myPullRecyclerView.setAdapter(workBankAdapter);
            workBankAdapter.setSwipeRefresh(myPullSwipeRefresh);
        }


        if (position==0) {
            searchHistoryUtils = new SearchHistoryUtils(this, Constants.SEARCH_WORK);
        }else {
            searchHistoryUtils = new SearchHistoryUtils(this, Constants.SEARCH_WORK_BANK);
        }
        if(searchHistoryUtils.getHistory()!=null&&searchHistoryUtils.getHistory().size()>0&&!TextUtils.isEmpty(searchHistoryUtils.getHistory().get(0).trim())){
            historyList.addAll(searchHistoryUtils.getHistory());
        }
        listAdapter = new BaseRecyclerAdapter(TeaSearchWorkActivity.this, historyList, new SearchWorkDelegate(TeaSearchWorkActivity.this,searchHistoryUtils,clear_history));
        listview.setAdapter(listAdapter);
        if(listAdapter.getItemCount()>0){
            clear_history.setVisibility(View.VISIBLE);
        }
        scrollView.setVisibility(View.VISIBLE);

        InintEvent();
    }

    private void InintEvent()
    {
        if(workBankDelegate!=null){
            workBankDelegate.setOnItemClickListener(new WorkBankDelegate.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    String coursId=courseId+"";
                    String testid=itemsBeanList.get(position).getId()+"";
                    String name=itemsBeanList.get(position).getTitle();
                    Intent intent=new Intent(TeaSearchWorkActivity.this,PublishWorkActivity.class);
                    intent.putExtra("testId",testid);
                    intent.putExtra("courseId",coursId);
                    intent.putExtra("title",name);
                    if(!new SharedPreferencesUtil(TeaSearchWorkActivity.this).getUid().trim().equals(itemsBeanList.get(position).getCreator().getId()+"")){
                        intent.putExtra("detail","not_self");
                    }
                    intent.putExtra("workType",workType);
                    intent.putExtra("status",itemsBeanList.get(position).getStatus());
                    startActivity(intent);
                }
            });
        }
        if(questionResourseDelegate!=null){
            questionResourseDelegate.setOnItemClickListener(new QuestionResourseDelegate.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    String coursId=courseId+"";
                    String testid=itemsBeanList.get(position).getId()+"";
                    String name=itemsBeanList.get(position).getTitle();
                    Intent intent=new Intent(TeaSearchWorkActivity.this,PublishWorkActivity.class);
                    intent.putExtra("testId",testid);
                    intent.putExtra("courseId",coursId);
                    intent.putExtra("title",name);
                    intent.putExtra("workType",workType);
                    if(!new SharedPreferencesUtil(TeaSearchWorkActivity.this).getUid().trim().equals(itemsBeanList.get(position).getCreator().getId()+"")){
                        intent.putExtra("detail","not_self");
                    }
                    intent.putExtra("status",3);//此页面都是已发布的
                    startActivity(intent);
                }
            });
        }

        leftImage.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                TeaSearchWorkActivity.this.finish();
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
                    titleEdit.setTextColor(ContextCompat.getColor(TeaSearchWorkActivity.this, R.color.font_gray));
                } else
                {
                    titleEdit.setTextColor(ContextCompat.getColor(TeaSearchWorkActivity.this, R.color.font_black));
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            public void afterTextChanged(Editable s)
            {
                if (listAdapter != null && listAdapter.getItemCount()!= 0)
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
                listAdapter.notifyDataSetChanged();
                clear_history.setVisibility(View.GONE);
                Log.d("zw--size",listAdapter.getItemCount()+"--size");

            }
        });

        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                if(position==0){
                    questionResourseAdapter.resetPageIndex();
                }else {
                    workBankAdapter.resetPageIndex();
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
                    questionResourseAdapter.addPageIndex();
                }else {
                    workBankAdapter.addPageIndex();
                }
                ExecuteAsyncTask();
            }
        });

//        questionResourseAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(View view, int position)
//            {
//
////                Intent intent = new Intent(TeaSearchWorkActivity.this, Posters_Particulars_Activity.class);
////                intent.putExtra("bean", data.get(position));
////                intent.putExtra("position", position);
////                startActivityForResult(intent, 4);
//            }
//        });
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
//        if(listAdapter.getItemCount()>0){
//            clear_history.setVisibility(View.VISIBLE);
//        }
        Log.d("zw--size--click",listAdapter.getItemCount()+"--size--"+searchHistoryUtils.resetHistory(keyword).size());

    }

    public void removeItem(String str)
    {
        searchHistoryUtils.deleteHistoryItem(str);
    }

    private void ExecuteAsyncTask()
    {
        titleEdit.clearFocus();
        if(position==0){
            questionResourseAdapter.resetPageIndex();
        }else {
            workBankAdapter.resetPageIndex();
        }
        if(position==0){
            new HttpGetBuilder(this).setHttpResult(this)
                    .setUrl(UrlsNew.TEST_PAPER_PUBLISH)
                    .setClassObj(AlreadyPublishBean.class)
                    .addQueryParams("page","1")
                    .addQueryParams("pageSize","20")
                    .addQueryParams("orderBy","create_time desc")
                    .addQueryParams("course_id",courseId+"")
                    .addQueryParams("type",workType+"")
                    .addQueryParams("title","all|"+keyword)
                    .addQueryParams("status","3")
                    .build();
        }else {
            new HttpGetBuilder(this).setHttpResult(this)
                    .setUrl(UrlsNew.TEST_PAPER_PUBLISH)
                    .setClassObj(AlreadyPublishBean.class)
                    .addQueryParams("page","1")
                    .addQueryParams("pageSize","20")
                    .addQueryParams("orderBy","create_time desc")
                    .addQueryParams("course_id",courseId+"")
                    .addQueryParams("type",workType+"")
                    .addQueryParams("title","all|"+keyword)
                    .build();
        }

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
        context.startActivity(new Intent(context, TeaSearchWorkActivity.class));
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {

        hideLoadingView();
        publishBean= (AlreadyPublishBean) resultBean;
        if(position==0){
            questionResourseAdapter.setTotalPage(1);
            questionResourseAdapter.setPullData(publishBean.getItems());
            if (questionResourseAdapter.getTotalPage() == 0) {
                Utils.makeToast(TeaSearchWorkActivity.this, getResources().getString(R.string.found_no_content));
            } else {
                // 有数据的处理
                scrollView.setVisibility(View.GONE);
                myPullSwipeRefresh.setVisibility(View.VISIBLE);
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(titleEdit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }else {
            workBankAdapter.setTotalPage(1);
            workBankAdapter.setPullData(publishBean.getItems());
            if (workBankAdapter.getTotalPage() == 0) {
                Utils.makeToast(TeaSearchWorkActivity.this, getResources().getString(R.string.found_no_content));
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
        Utils.makeToast(TeaSearchWorkActivity.this, msg);
    }
}
