package com.coder.kzxt.question.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.app.http.builders.HttpPutBuilder;
import com.bumptech.glide.Glide;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.question.adapter.QuestionReplyDetaisListDelegate;
import com.coder.kzxt.question.beans.QuesReplyDetailsListBean;
import com.coder.kzxt.question.beans.QuestionsReplyListBean;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.app.http.UrlsNew;
import com.coder.kzxt.views.ResizeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionReplyDetailsActivity extends BaseActivity implements HttpCallBack {
    private int questionId;
    private String first_pid;//回复详情头部的id
    private String nickname;
    private String reply_userId;//被回复的用户id
    private String first_reply_userId;//头部回复详情的用户id
    private String courseId;
    private String userId;
    private int isJoin;
    private int role;//1老师。2学生
    private int best_answer_id;//大于0则已经解决，不显示回复输入
    private QuestionsReplyListBean.ItemsBean headerBean;

    private Button load_fail_button;
    private LinearLayout search_jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private Toolbar toolbar;
    private RelativeLayout mainView;
    private RelativeLayout reply_post_layout;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;
    private PullRefreshAdapter <QuesReplyDetailsListBean.ItemsBean>pullRefreshAdapter;
    private QuestionReplyDetaisListDelegate detaisListDelegate;
    private List<QuesReplyDetailsListBean.ItemsBean>beanList=new ArrayList<>();

    public EditText reply_write_edit;
    private TextView reply_post_button;
    private static final int BIGGER = 1;
    private static final int SMALLER = 2;
    private static final int MSG_RESIZE = 1;
    private InputHandler mHandler;

    public boolean isreply =false;
    private SharedPreferencesUtil sp;
    private boolean isScrollhead=true;

    class InputHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_RESIZE:
                    if (msg.arg1 == BIGGER) {
                        // 关闭
                        isreply = false;
                        //因为此时的键盘有可能是由于用户点击返回键收起后又直接点击输入框弹起来的（hint是评论），
                        // 需要将被回复的id变为一级id,否则还是二级id
                        reply_userId=first_reply_userId;

                        reply_write_edit.setText("");
                        reply_write_edit.setHint("回复");
                    } else {
                    }
                    break;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_reply_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        nickname=getIntent().getStringExtra("nickname");
        toolbar.setTitle(nickname+"的回复详情");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp=new SharedPreferencesUtil(this);
        initView();
        initClick();
        search_jiazai_layout.setVisibility(View.VISIBLE);
        httpRequest();
    }


    private void initView() {
        questionId=getIntent().getIntExtra("questionId",0);

        first_reply_userId=getIntent().getStringExtra("reply_userId");
        first_pid=getIntent().getStringExtra("pid");
        courseId=getIntent().getStringExtra("courseId");
        userId=getIntent().getStringExtra("userId");
        role=getIntent().getIntExtra("role",0);
        isJoin=getIntent().getIntExtra("isJoin",0);
        best_answer_id=getIntent().getIntExtra("best_answer_id",1);
        headerBean= (QuestionsReplyListBean.ItemsBean) getIntent().getSerializableExtra("header");
        reply_userId=first_reply_userId;//最初是默认一级的回复人id，当用户点击子回复时候，
        // reply_userId变为子回复人的id，且当键盘收起时候再重新变为默认的一级回复人的id
        mainView= (RelativeLayout) findViewById(R.id.activity_question_reply_details);
        load_fail_button = (Button) findViewById(R.id.load_fail_button);
        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        no_info_layout = (LinearLayout)findViewById(R.id.no_info_layout);
        search_jiazai_layout = (LinearLayout)findViewById(R.id.jiazai_layout);
        mHandler = new InputHandler();
        reply_post_button = (TextView) findViewById(R.id.reply_post_button);
        reply_write_edit= (EditText) findViewById(R.id.reply_write_edit);
         reply_post_layout= (RelativeLayout) findViewById(R.id.reply_post_layout);

        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        myPullRecyclerView = (MyPullRecyclerView) findViewById(R.id.myPullRecyclerView);
        detaisListDelegate=new QuestionReplyDetaisListDelegate(this,headerBean);
        pullRefreshAdapter=new PullRefreshAdapter(this,beanList,1,detaisListDelegate);
        myPullRecyclerView.setAdapter(pullRefreshAdapter);
        pullRefreshAdapter.setSwipeRefresh(myPullSwipeRefresh);
        if(headerBean.getIs_best_answer().equals("1")||best_answer_id>0||isJoin==0|| TextUtils.isEmpty(sp.getIsLogin())){
            reply_post_layout.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams=(RelativeLayout.LayoutParams)myPullSwipeRefresh.getLayoutParams();
            layoutParams.setMargins(0,0,0,0);
            myPullSwipeRefresh.setLayoutParams(layoutParams);
        }else {
            reply_post_layout.setVisibility(View.VISIBLE);
        }
        ResizeLayout layout = (ResizeLayout) findViewById(R.id.activity_question_reply_details);
        layout.setOnResizeListener(new ResizeLayout.OnResizeListener() {

            @Override
            public void OnResize(int w, int h, int oldw, int oldh) {
                int change = BIGGER;
                if (h < oldh) {
                    change = SMALLER;
                }

                Message msg = new Message();
                msg.what = 1;
                msg.arg1 = change;
                mHandler.sendMessage(msg);

            }
        });

    }

    private void initClick() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });
        load_fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_jiazai_layout.setVisibility(View.VISIBLE);
                load_fail_layout.setVisibility(View.GONE);
                httpRequest();
            }
        });
        reply_post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginHideSoftInputWindow();
                if(TextUtils.isEmpty(reply_write_edit.getText().toString().trim())){
                    ToastUtils.makeText(QuestionReplyDetailsActivity.this,"回复不能为空");
                    return;
                }
                search_jiazai_layout.setVisibility(View.VISIBLE);
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("course_id",courseId);
                    jsonObject.put("qa_id",questionId);
                    jsonObject.put("pid",first_pid);
                    jsonObject.put("reply_uid",reply_userId);
                    jsonObject.put("content",reply_write_edit.getText().toString().trim());
                    jsonObject.put("user_id",new SharedPreferencesUtil(QuestionReplyDetailsActivity.this).getUid());
                    jsonObject.put("role",role);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                reply_userId=first_reply_userId;//每次装载数据完毕后让reply_userId变为一级回复人的id
                isreply=false;//再次初始化
                new HttpPostBuilder(QuestionReplyDetailsActivity.this).setClassObj(null).setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        pullRefreshAdapter.resetPageIndex();
                        httpRequest();


                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        search_jiazai_layout.setVisibility(View.GONE);
                        ToastUtils.makeText(QuestionReplyDetailsActivity.this,msg);

                    }
                }).setUrl(UrlsNew.URLHeader+"/question/answer").addBodyParam(jsonObject.toString()).build();
            }
        });
        pullRefreshAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(headerBean.getIs_best_answer().equals("1")||best_answer_id>0||isJoin==0){
                    return;
                }
                isreply=true;
                reply_write_edit.setFocusable(true);
                reply_write_edit.setFocusableInTouchMode(true);
                reply_write_edit.requestFocus();
                if(position>=0){
                    reply_userId=pullRefreshAdapter.getData().get(position).getUser().getId()+"";
                    InputMethodManager inputManager = (InputMethodManager) reply_write_edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(reply_write_edit,0);
                    reply_write_edit.setText("");
                    reply_write_edit.setHint(getResources().getString(R.string.reply) + pullRefreshAdapter.getData().get(position).getUser().getNickname());
                }else if(position==-1){//点击头部
                    reply_userId=first_reply_userId;
                    InputMethodManager inputManager = (InputMethodManager) reply_write_edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(reply_write_edit,0);
                    reply_write_edit.setText("");
                    reply_write_edit.setHint("回复");
                }

            }
        });
        pullRefreshAdapter.setOnItemLongClickListener(new BaseRecyclerAdapter.setOnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isScrollhead=true;
                pullRefreshAdapter.resetPageIndex();
                httpRequest();
            }
        });
    }

    private void httpRequest() {
        new HttpGetBuilder(this).setClassObj(QuesReplyDetailsListBean.class).setUrl(UrlsNew.URLHeader+"/question/answer")
                .setHttpResult(this)
                .addQueryParams("page","1")
                .addQueryParams("pageSize","1000")
                .addQueryParams("orderBy","create_time asc")
                .addQueryParams("qa_id",questionId+"")
                .addQueryParams("pid",first_pid+"")
                .setRequestCode(1001)
                .build();
    }


    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        search_jiazai_layout.setVisibility(View.GONE);
        myPullRecyclerView.setVisibility(View.VISIBLE);
        QuesReplyDetailsListBean replyDetailsListBean= (QuesReplyDetailsListBean) resultBean;
        pullRefreshAdapter.setTotalPage(1);//不做分页
        pullRefreshAdapter.setPullData(replyDetailsListBean.getItems());
        if(isScrollhead){
            myPullRecyclerView.smoothScrollToPosition(0);
            myPullRecyclerView.smoothScrollBy(0,-600);
            isScrollhead=false;
        }else {
            myPullRecyclerView.scrollToPosition(pullRefreshAdapter.getData().size());
            myPullRecyclerView.smoothScrollBy(0,600);
        }

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        search_jiazai_layout.setVisibility(View.GONE);
        myPullRecyclerView.setVisibility(View.GONE);
        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
        {
            NetworkUtil.httpRestartLogin(QuestionReplyDetailsActivity.this, mainView);
        }else {
            load_fail_layout.setVisibility(View.VISIBLE);
            ToastUtils.makeText(QuestionReplyDetailsActivity.this,msg);
        }

    }
    //隐藏键盘
    public void loginHideSoftInputWindow()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
        {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==Constants.LOGIN_BACK){
            search_jiazai_layout.setVisibility(View.VISIBLE);
            httpRequest();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tea_work_menu, menu);
        menu.findItem(R.id.action_setting).setTitle("采纳").setIcon(0);
        menu.findItem(R.id.action_search).setVisible(false);
        //不是创建者本人的,不展示"采纳"
        if(!sp.getUid().equals(userId)||headerBean.getUser_id().equals(sp.getUid())){//采纳对非问答创建者以及回复创建者是当前用户情况下隐藏处理
            menu.findItem(R.id.action_setting).setVisible(false);
        }
        //已经是最佳答案(被采纳过)的或者未加入课程以及未登录，不显示
        if(headerBean.getIs_best_answer().equals("1")||best_answer_id>0||isJoin==0|| TextUtils.isEmpty(sp.getIsLogin())){
            menu.findItem(R.id.action_setting).setVisible(false);
            reply_post_layout.setVisibility(View.GONE);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if(item.getItemId()==R.id.action_setting){
            search_jiazai_layout.setVisibility(View.VISIBLE);
            httpPutRequestone(item);
        }
        return super.onOptionsItemSelected(item);
    }
    public void httpPutRequestone(final MenuItem item){
        new HttpPutBuilder(this).setUrl(UrlsNew.URLHeader+"/question/question").setClassObj(null)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                       httpPutRequestTwo(item);
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        search_jiazai_layout.setVisibility(View.GONE);
                        ToastUtils.makeText(QuestionReplyDetailsActivity.this,msg);

                    }
                })
                .addBodyParam("best_answer_id",headerBean.getId())
                .setPath(questionId+"")
                .build();
    }
    public void httpPutRequestTwo(final MenuItem item){
        new HttpPutBuilder(this).setUrl(UrlsNew.URLHeader+"/question/answer").setClassObj(null)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        ToastUtils.makeText(QuestionReplyDetailsActivity.this,"成功");
                        sendBroadcast(new Intent().setAction(Constants.QUESTION_UPDATE));//刷新问答首页page
                        item.setVisible(false);
                        reply_post_layout.setVisibility(View.GONE);
                        search_jiazai_layout.setVisibility(View.GONE);
                        headerBean.setIs_best_answer("1");
                        pullRefreshAdapter.notifyDataSetChanged();
                        RelativeLayout.LayoutParams layoutParams=(RelativeLayout.LayoutParams)myPullSwipeRefresh.getLayoutParams();
                        layoutParams.setMargins(0,0,0,0);
                        myPullSwipeRefresh.setLayoutParams(layoutParams);
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        search_jiazai_layout.setVisibility(View.GONE);
                        ToastUtils.makeText(QuestionReplyDetailsActivity.this,msg);

                    }
                })
                .addBodyParam("is_best_answer","1")
                .setPath(headerBean.getId())
                .build();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction()==KeyEvent.ACTION_DOWN){
          //  setResult(1);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        Glide.get(this).clearMemory();
        super.onDestroy();
    }
}
