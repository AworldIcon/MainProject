package com.coder.kzxt.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.course.adapter.SearchMoreCourseDelegate;
import com.coder.kzxt.course.beans.CourseBean;
import com.coder.kzxt.main.adapter.SearchAllAdapter;
import com.coder.kzxt.main.beans.LiveListResultBean;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SearchHistoryUtils;
import com.coder.kzxt.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SearchAllActivity extends BaseActivity implements HttpCallBack {

    private LinearLayout jiazai_layout;

    private MyRecyclerView listview;
    private ImageView leftImage;
    private TextView searchText;
    private TextView clear_history;

    private BaseRecyclerAdapter listAdapter;
    private ExpandableListView my_list;
    private ScrollView scrollView;
    private RelativeLayout myLayout;
    private SearchAllAdapter searchAllAdapter;
    private EditText titleEdit;
    private String keyword;
    private List<String> historyList = new ArrayList<>();
    private SearchHistoryUtils searchHistoryUtils;
    private List<String> history;
    private int httpNum;
    private ArrayList<List<HashMap<String,String>>> arrayList_all;
    private List<HashMap<String,String>> groupall;

    private LinearLayout no_info_layout;
    private ImageView no_info_img;
    private TextView no_info_text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_search);

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
        myLayout = (RelativeLayout) findViewById(R.id.my_layout);
        my_list = (ExpandableListView) findViewById(R.id.my_list);

        searchHistoryUtils = new SearchHistoryUtils(this, Constants.SEARCH_LOCAL_COURSE);

        history = searchHistoryUtils.getHistory();
        if (!TextUtils.isEmpty(history.get(0))) {
            historyList.addAll(history);
            clear_history.setVisibility(View.VISIBLE);
        }
//
        listAdapter = new BaseRecyclerAdapter(SearchAllActivity.this, historyList,
                new SearchMoreCourseDelegate(SearchAllActivity.this, searchHistoryUtils,clear_history));
        listview.setAdapter(listAdapter);
        scrollView.setVisibility(View.VISIBLE);

        InintEvent();
    }

    private void InintEvent() {
        leftImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideWindowSoftInput();
                SearchAllActivity.this.finish();
            }
        });

        this.searchText.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                searchClick();
            }

        });

        titleEdit.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString().trim()) && s.length() == 0) {
                    titleEdit.setTextColor(ContextCompat.getColor(SearchAllActivity.this, R.color.font_gray));
                } else {
                    titleEdit.setTextColor(ContextCompat.getColor(SearchAllActivity.this, R.color.font_black));
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

        titleEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
                keyword = (String) listAdapter.getData().get(position);
                listNotify(keyword);
                showLoadingView();
                ExecuteAsyncTask();
            }
        });

        clear_history.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                searchHistoryUtils.clearHistory();
                historyList.clear();
                listAdapter.resetData(historyList);
            }
        });

        my_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
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
        }

    }

    private void listNotify(String keyword) {
        listAdapter.resetData(searchHistoryUtils.resetHistory(keyword));
    }

    public void removeItem(String str) {
        searchHistoryUtils.deleteHistoryItem(str);
    }

    private void ExecuteAsyncTask() {
        titleEdit.clearFocus();
        new HttpGetBuilder(SearchAllActivity.this)
                .setUrl(UrlsNew.GET_SEARCH_ALL_LIST)
                .setHttpResult(SearchAllActivity.this)
                .setClassObj(CourseBean.class)
                .setRequestCode(1)
                .addQueryParams("page", "1")
                .addQueryParams("pageSize", "200")
                .addQueryParams("status", "2")
                .addQueryParams("title", "all|" + keyword)
                .build();

        new HttpGetBuilder(SearchAllActivity.this)
                .setHttpResult(this)
                .setClassObj(LiveListResultBean.class)
                .addQueryParams("page", "1")
                .addQueryParams("pageSize", "200")
                .addQueryParams("is_designated_courses","0")
                .addQueryParams("live_title", "all|"+keyword)
                .addQueryParams("center", "1")
                .addQueryParams("isBindCourse","0")
                .setUrl(UrlsNew.GET_LIVE)
                .setRequestCode(2)
                .build();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            hideWindowSoftInput();
            if (scrollView.getVisibility() == View.VISIBLE) {
                scrollView.setVisibility(View.GONE);
                return true;
            }
            finish();
        }
        return true;
    }

    public static void gotoActivity(Context context) {
        context.startActivity(new Intent(context, SearchAllActivity.class));
    }

    private   CourseBean courseBean;
    private LiveListResultBean liveListResult;

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        httpNum++;
        if(requestCode==1){
           courseBean = (CourseBean) resultBean;
        }
        if(requestCode==2){
           liveListResult = (LiveListResultBean) resultBean;
        }


        if(httpNum>=2){
            arrayList_all =new ArrayList<>();
            groupall = new ArrayList<>();
            List<HashMap<String, String>> dataList0=new ArrayList<HashMap<String, String>>();
            List<HashMap<String, String>> dataList1=new ArrayList<HashMap<String, String>>();
            hideLoadingView();

            if(courseBean.getItems().size()>0){
                HashMap<String,String> al=new HashMap<String, String>();
                al.put("name",getResources().getString(R.string.courses));
                al.put("num",courseBean.getItems().size()+"");
                groupall.add(al);
                for(int i=0;i<courseBean.getItems().size();i++){
                        HashMap<String, String> cl = new HashMap<>();
                        cl.put("courseName",courseBean.getItems().get(i).getTitle());
                        cl.put("courseImage",courseBean.getItems().get(i).getMiddle_pic());
                        cl.put("studyTime",courseBean.getItems().get(i).getLesson_num());
                        cl.put("price",courseBean.getItems().get(i).getPrice());
                        cl.put("treeId",courseBean.getItems().get(i).getId());
                        cl.put("childtype","1");
                        dataList0.add(cl);
                }
                arrayList_all.add(dataList0);
            }

            if(liveListResult.getItems().size()>0){
                HashMap<String,String> al=new HashMap<String, String>();
                al.put("name",getResources().getString(R.string.live_coruse));
                al.put("num",liveListResult.getItems().size()+"");
                groupall.add(al);
                for(int i=0;i<liveListResult.getItems().size();i++){
                    if(liveListResult.getItems().get(i).getLive_status()!=3&&liveListResult.getItems().get(i).getLive_status()!=2){

                        HashMap<String, String> cl = new HashMap<>();
                        cl.put("course_img",liveListResult.getItems().get(i).getPicture());
                        cl.put("live_name_tv",liveListResult.getItems().get(i).getLive_title());
                        cl.put("host_name_tv",liveListResult.getItems().get(i).getUser_profile().getNickname());
                        cl.put("start_time",liveListResult.getItems().get(i).getStart_time());
                        cl.put("end_time",liveListResult.getItems().get(i).getEnd_time());
                        cl.put("end_time",liveListResult.getItems().get(i).getEnd_time());
                        cl.put("live_id",liveListResult.getItems().get(i).getId());
                        cl.put("status",String.valueOf(liveListResult.getItems().get(i).getLive_status()));
                        cl.put("childtype","2");
                        dataList1.add(cl);
                    }

                }
                arrayList_all.add(dataList1);
            }

            searchAllAdapter = new SearchAllAdapter(SearchAllActivity.this,groupall,arrayList_all,keyword);
            my_list.setAdapter(searchAllAdapter);
            for (int i=0; i<searchAllAdapter.getGroupCount(); i++) {
                my_list.expandGroup(i);
            }
            if (dataList0.size()==0&&dataList1.size()==0) {
                no_info_layout.setVisibility(View.VISIBLE);
            } else {
                no_info_layout.setVisibility(View.GONE);
                my_list.setVisibility(View.VISIBLE);
                // 有数据的处理
                scrollView.setVisibility(View.GONE);
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(titleEdit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        hideLoadingView();
        ToastUtils.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
            NetworkUtil.httpRestartLogin(SearchAllActivity.this, myLayout);
        } else {
            NetworkUtil.httpNetErrTip(SearchAllActivity.this, myLayout);
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideWindowSoftInput(){
        final View v = getWindow().peekDecorView();
        if (v != null && v.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
