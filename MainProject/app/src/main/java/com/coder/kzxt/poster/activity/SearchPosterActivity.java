package com.coder.kzxt.poster.activity;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.poster.adapter.MyPromotionStageAdapter;
import com.coder.kzxt.poster.adapter.MyPromotionStageDelegate;
import com.coder.kzxt.poster.adapter.SearchPosterDelegate;
import com.coder.kzxt.poster.beans.PosterBeans;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索海报
 */
public class SearchPosterActivity extends BaseActivity  implements HttpCallBack {

    private RelativeLayout myLayout;
    private MyRecyclerView listview;
    private ImageView leftImage;
    private TextView searchText;
    private TextView clear_history;

    private BaseRecyclerAdapter listAdapter;
    private MyPullSwipeRefresh pullStaggeredGridView;
    private MyPullRecyclerView staggeredGridView;
    private ScrollView scrollView;

    private MyPromotionStageDelegate orderListStageDelegate;
    private MyPromotionStageAdapter stageadapter;

    private SharedPreferencesUtil pu;
    private EditText titleEdit;
    private String keyword;
    private String[] dataHistory;
    private String historyStr;
    private List<PosterBeans.ItemsBean> data = new ArrayList<>();
    private List<String> dataHistoryList;

    private LinearLayout no_info_layout;
    private TextView no_info_text;
    private String pageSize = "20";// 每页请求条数

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poseter_search);
        pu = new SharedPreferencesUtil(SearchPosterActivity.this);
        myLayout = (RelativeLayout) findViewById(R.id.myLayout);
        leftImage = (ImageView) findViewById(R.id.leftImage);
        listview = (MyRecyclerView) findViewById(R.id.history_list);
        titleEdit = (EditText) findViewById(R.id.edit);
        searchText = (TextView) findViewById(R.id.searchText);
        clear_history = (TextView) findViewById(R.id.clear_history);
        clear_history = (TextView) findViewById(R.id.clear_history);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        pullStaggeredGridView = (MyPullSwipeRefresh) findViewById(R.id.pullStaggeredGridView);
        staggeredGridView = (MyPullRecyclerView) findViewById(R.id.staggeredGridView);

        //stage的初始化
        orderListStageDelegate = new MyPromotionStageDelegate(this);
        stageadapter = new MyPromotionStageAdapter(this, data, orderListStageDelegate);
        staggeredGridView.setAdapter(stageadapter);
        stageadapter.setSwipeRefresh(pullStaggeredGridView);


        no_info_layout = (LinearLayout) findViewById(R.id.no_info_layout);
        no_info_text = (TextView) findViewById(R.id.no_info_text);
        no_info_text.setText("没有搜索结果");


        historyStr = pu.getSearchHistoryPosters();
        dataHistoryList = new ArrayList<String>();

        dataHistory = historyStr.split(",");

        for (int i = 0; i < dataHistory.length; i++) {
            if (!"".equals(dataHistory[i])) {
                dataHistoryList.add(dataHistory[i]);
            }
        }
        listAdapter = new BaseRecyclerAdapter(SearchPosterActivity.this, dataHistoryList, new SearchPosterDelegate(SearchPosterActivity.this));
        listview.setAdapter(listAdapter);
        clear_history.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.VISIBLE);
        InintEvent();
    }

    private void InintEvent() {
        leftImage.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SearchPosterActivity.this.finish();
            }
        });

        this.searchText.setOnClickListener(new OnClickListener() {
            public void onClick(final View v) {
                searchClick();
            }

        });

        titleEdit.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString().trim()) && s.length() == 0) {
                    titleEdit.setTextColor(ContextCompat.getColor(SearchPosterActivity.this, R.color.font_gray));
                } else {
                    titleEdit.setTextColor(ContextCompat.getColor(SearchPosterActivity.this, R.color.font_black));
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (listAdapter != null && listAdapter.getShowCount() != 0) {
                    scrollView.setVisibility(View.VISIBLE);
                    no_info_layout.setVisibility(View.GONE);
                }
            }
        });

        titleEdit.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchClick();
                }
                return true;
            }
        });

        listAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                scrollView.setVisibility(View.GONE);
                keyword = dataHistory[position];
                listNotify(keyword);
                showLoadingView();
                ExecuteAsyncTask();
            }
        });

        clear_history.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                historyStr = "";
                pu.setSearchHistoryPosters("");
                listAdapter.resetData(new ArrayList<String>());
            }
        });

        pullStaggeredGridView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                stageadapter.resetPageIndex();
                ExecuteAsyncTask();

            }
        });

        staggeredGridView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                // 直接请求服务器
                stageadapter.addPageIndex();
                ExecuteAsyncTask();
            }
        });

        stageadapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(SearchPosterActivity.this, Posters_Particulars_Activity.class);
                intent.putExtra("bean", data.get(position));
                intent.putExtra("position", position);
                startActivityForResult(intent, 4);
            }
        });
    }


    private void searchClick() {
        keyword = titleEdit.getText().toString().trim();
        if (!TextUtils.isEmpty(keyword)) {
            scrollView.setVisibility(View.GONE);
            showLoadingView();
            listNotify(keyword);
            ExecuteAsyncTask();
            clear_history.setVisibility(View.VISIBLE);
        }

    }

    private void listNotify(String keyword) {
        if (TextUtils.isEmpty(historyStr)) {
            historyStr = keyword;
        } else {
            if (!historyStr.contains(keyword)) {
                historyStr = keyword + "," + historyStr;
            } else {
                historyStr += ",";
                historyStr = historyStr.replace(keyword + ",", "");
                historyStr = keyword + "," + historyStr;
            }
        }
        dataHistory = historyStr.split(",");


        List<String> dataHistoryList2 = new ArrayList<>();
        for (int i = 0; i < dataHistory.length; i++) {
            if (!"".equals(dataHistory[i])) {
                dataHistoryList2.add(dataHistory[i]);
            }
        }
        listAdapter.resetData(dataHistoryList2);
        pu.setSearchHistoryPosters(historyStr);
    }

    public void removeItem(String str) {
        historyStr += ",";
        historyStr = historyStr.replace(str + ",", "");
        pu.setSearchHistoryPosters(historyStr);
    }

    private void ExecuteAsyncTask() {
        titleEdit.clearFocus();
        HttpGetBuilder httpGetBuilder = new HttpGetBuilder(this);
        httpGetBuilder.setHttpResult(this);
        httpGetBuilder.setClassObj(PosterBeans.class);
        httpGetBuilder.setUrl(UrlsNew.GET_POSTER);
        httpGetBuilder.addQueryParams("page",stageadapter.getPageIndex()+"");
        httpGetBuilder.addQueryParams("pageSize",pageSize);
        httpGetBuilder.addQueryParams("title",  keyword);
        httpGetBuilder.build();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 4 && resultCode == 5) {
//            int position = data.getIntExtra("position", 0);
//            PostersBean bean = (PostersBean) data.getSerializableExtra("bean");
//            stageadapter.addData(position, bean);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (scrollView.getVisibility() == View.VISIBLE) {
                scrollView.setVisibility(View.GONE);
                return true;
            }
            finish();
        }
        return true;
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        hideLoadingView();
        PosterBeans posterBeans = (PosterBeans) resultBean;
        stageadapter.setTotalPage(posterBeans.getPaginate().getPageNum());
        stageadapter.setPullData(posterBeans.getItems());
         if (stageadapter.getShowCount()==0) {
               no_info_layout.setVisibility(View.VISIBLE);
            } else {
                // 有数据的处理
                no_info_layout.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                pullStaggeredGridView.setVisibility(View.VISIBLE);
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(titleEdit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        hideLoadingView();
        ToastUtils.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
            NetworkUtil.httpRestartLogin(SearchPosterActivity.this, myLayout);
        } else {
            NetworkUtil.httpNetErrTip(SearchPosterActivity.this, myLayout);
        }
    }
}