package com.coder.kzxt.classe.activity;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.classe.beans.ClassMemberBean;
import com.coder.kzxt.classe.delegate.AddClassMemberDelegate;
import com.coder.kzxt.classe.mInterface.OnAddClassMemberInterface;
import com.coder.kzxt.course.adapter.SearchMoreCourseDelegate;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.login.beans.UserInfoList;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SearchHistoryUtils;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.utils.ValidatorUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wangtingshun on 2017/6/8.
 * 搜索添加班级成员
 */

public class SearchAddClassMemberActivity extends BaseActivity implements OnAddClassMemberInterface{


    private SharedPreferencesUtil spu;
    private LinearLayout jiazai_layout;
    private MyRecyclerView listview;
    private ImageView leftImage;
    private TextView searchText;
    private TextView clear_history;
    private BaseRecyclerAdapter listAdapter;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;

    private LinearLayout no_info_layout;
    private ImageView no_info_img;
    private TextView no_info_text;
    private String keyword;

    private ScrollView scrollView;
    private RelativeLayout myLayout;
    private AddClassMemberDelegate delegate;
    private PullRefreshAdapter<UserInfoList.UserBean> stageadapter;
    private EditText titleEdit;
    private List<String> historyList = new ArrayList<>();
    private List<UserInfoList.UserBean> data = new ArrayList<>();
    private SearchHistoryUtils searchHistoryUtils;
    private Dialog asyDialog;
    private List<String> history;
    private int pageNum; //总页数
    private String classId;
    private ArrayList<ClassMemberBean.ClassMember> memberList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_common_search);
        spu = new SharedPreferencesUtil(this);
        classId = getIntent().getStringExtra("classId");
        Bundle bundle = getIntent().getBundleExtra("memberList");
        memberList = (ArrayList<ClassMemberBean.ClassMember>)bundle.getSerializable("bundle");
        initView();
        initListener();
    }

    private void initView() {
        leftImage = (ImageView) findViewById(R.id.leftImage);
        listview = (MyRecyclerView) findViewById(R.id.history_list);
        titleEdit = (EditText) findViewById(R.id.edit);
        searchText = (TextView) findViewById(R.id.searchText);
        clear_history = (TextView) findViewById(R.id.clear_history);
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        no_info_layout = (LinearLayout)findViewById(R.id.no_info_layout);
        no_info_img = (ImageView) findViewById(R.id.no_info_img);
        no_info_img.setImageResource(R.drawable.empty_order);
        no_info_text = (TextView) findViewById(R.id.no_info_text);
        no_info_text.setText("没有成员信息");
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        myPullRecyclerView = (MyPullRecyclerView) findViewById(R.id.my_list);
        myLayout = (RelativeLayout) findViewById(R.id.my_layout);
        titleEdit.setHint("请输入学员账号");
        //stage的初始化
        delegate = new AddClassMemberDelegate(this);
        delegate.setOnAddClassMemberListener(this);
        stageadapter = new PullRefreshAdapter(this, data, delegate);
        myPullRecyclerView.setAdapter(stageadapter);
        stageadapter.setSwipeRefresh(myPullSwipeRefresh);

        searchHistoryUtils = new SearchHistoryUtils(this, Constants.SEARCH_LOCAL_COURSE);
        history = searchHistoryUtils.getHistory();
        if (!TextUtils.isEmpty(history.get(0))) {
            historyList.addAll(history);
            clear_history.setVisibility(View.VISIBLE);
        }
        listAdapter = new BaseRecyclerAdapter(SearchAddClassMemberActivity.this, historyList, new SearchMoreCourseDelegate(SearchAddClassMemberActivity.this, searchHistoryUtils, clear_history));
        listview.setAdapter(listAdapter);
        scrollView.setVisibility(View.VISIBLE);

        InintEvent();
    }

    private void initListener() {

    }
    private void InintEvent() {
        leftImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideWindowSoftInput();
                setResult(90);
                SearchAddClassMemberActivity.this.finish();
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
                    titleEdit.setTextColor(ContextCompat.getColor(SearchAddClassMemberActivity.this, R.color.font_gray));
                } else {
                    titleEdit.setTextColor(ContextCompat.getColor(SearchAddClassMemberActivity.this, R.color.font_black));
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

        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                stageadapter.resetPageIndex();
                ExecuteAsyncTask();
            }
        });

        myPullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                // 直接请求服务器
                stageadapter.addPageIndex();
                ExecuteAsyncTask();
            }
        });

//        stageadapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//            }
//        });
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
                new HttpGetBuilder(SearchAddClassMemberActivity.this)
                .setUrl(UrlsNew.GET_USER_LIST)               //使用用户列表
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        cancleLoadDialog();
                        hideLoadingView();
                        UserInfoList calssBean = (UserInfoList) resultBean;
                        pageNum = calssBean.getPaginate().getPageNum();
                        int totalNum = calssBean.getPaginate().getTotalNum();
                        stageadapter.setTotalPage(pageNum);
                        //判断是否已添加
                        ArrayList<UserInfoList.UserBean> items = calssBean.getItems();
                        processData(items);
                        stageadapter.setTotalPage(1);
                        stageadapter.setPullData(items);

                        if(totalNum == 0){
                            no_info_layout.setVisibility(View.VISIBLE);
                        } else {
                            no_info_layout.setVisibility(View.GONE);
                        }
                        if (stageadapter.getTotalPage() == 0) {

                        } else {
                            // 有数据的处理
                            scrollView.setVisibility(View.GONE);
                            myPullSwipeRefresh.setVisibility(View.VISIBLE);
                            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(titleEdit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(SearchAddClassMemberActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(SearchAddClassMemberActivity.this, myLayout);
                        }
                        no_info_layout.setVisibility(View.VISIBLE);
                        hideLoadingView();
                        cancleLoadDialog();
                    }
                })
                .setClassObj(UserInfoList.class)
                .addQueryParams("mobile",keyword)
                .addQueryParams("orderBy","create_time desc")
                .build();

    }

    /**
     * 处理用户数据
     * @param items
     */
    private void processData(ArrayList<UserInfoList.UserBean> items) {
        ArrayList<UserInfoList.UserBean> newItems = new ArrayList<>();
        if (items != null && items.size() > 0) {
            UserInfoList.UserBean userBean = items.get(0);
            if (memberList != null) {
                for (int i = 0; i < memberList.size(); i++) {
                    ClassMemberBean.ClassMember member = memberList.get(i);
                    if (userBean.getProfile().getId().equals(member.getProfile().getId())) {
                        userBean.setAdd(true);
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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

    public void laodProgressDialog(){
        asyDialog = MyPublicDialog.createLoadingDialog(this);
        asyDialog.show();
    }


    /**
     * 取消加载框
     */
    private void cancleLoadDialog(){
        if(asyDialog != null && asyDialog.isShowing()){
            asyDialog.cancel();
        }
    }
    

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 添加班级成员
     * @param userBean
     */
    @Override
    public void onAddClassMember(UserInfoList.UserBean userBean) {
        showLoadingView();
        boolean eleven = ValidatorUtil.isEleven(keyword);
        if (eleven) {
            addClassMemberRequest(addClassData(classId, keyword));
        } else {
            ToastUtils.makeText(this,"请输入合法账号",Toast.LENGTH_SHORT).show();
        }
    }

    private String addClassData(String classId, String account) {
        JSONObject object = new JSONObject();
        try {
            object.put("group_id", classId);
            object.put("account", account);
        } catch (Exception e) {
        }
        return object.toString();
    }

    private void addClassMemberRequest(String data) {
                 new HttpPostBuilder(this)
                .setUrl(UrlsNew.POST_ADD_CLASS_MEMBER)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        hideLoadingView();
                        setResult(9);
                        finish();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        hideLoadingView();
                        ToastUtils.makeText(SearchAddClassMemberActivity.this,msg, Toast.LENGTH_SHORT).show();
                    }
                })
                .addBodyParam(data)
                .build();
    }
}
