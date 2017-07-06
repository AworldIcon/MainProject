package com.coder.kzxt.question.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpDeleteBuilder;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.app.http.builders.HttpPutBuilder;
import com.bumptech.glide.Glide;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.login.activity.LoginActivity;
import com.coder.kzxt.question.adapter.QuestionReplyListAdapter;
import com.coder.kzxt.question.adapter.QuestionReplyListDelegate;
import com.coder.kzxt.question.beans.QuestionsDetailBean;
import com.coder.kzxt.question.beans.QuestionsReplyListBean;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.ScreenUtils;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.video.beans.CourseRoleResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * 问答详情
 * */
public class QuestionsDetailActivity extends BaseActivity implements HttpCallBack {
    private Button load_fail_button;
    private LinearLayout search_jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private Toolbar toolbar;
    private RelativeLayout mainView;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;
    private FloatingActionButton fab;
    private int questionId;
    private String nickname;
    private String userId;
    private String courseId;
    private String replyNum;
    private String courseName;
    private QuestionReplyListAdapter questionReplyListAdapter;
    private QuestionReplyListDelegate questionReplyListDelegate;
    private List<QuestionsReplyListBean.ItemsBean>beanList=new ArrayList<>();
    private View popView;
    private ImageView image_store,image_zan,image_elite,image_dele;
    private TextView text_store,text_zan,text_elite;
    private  LinearLayout image_elite_layout;
    private  LinearLayout image_dele_layout;
    private  LinearLayout pop_content;
    private ImageView reply_question;//回复按钮，已解决，则不显示回复按钮
    private boolean isClickElite;//如果是课程创建者或者此课程下的老师(此页面两个接口判断，有一符合则是true)，若true，则可以修改精华的image，否则不允许修改
    private boolean isSelf;//如果是问答创建者则展示删除按钮
    private int is_praise;//点赞0/1
    private int is_follow;//收藏0/1
    private int is_elite;//精华0/1
    private int isJoin;
    private PopupWindow popupWindow;
    private boolean network_Sucess;
    private boolean isScrollend=false;
    private boolean isScrollhead=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("问答详情");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initClick();
        if(new SharedPreferencesUtil(this).getUid().equals(userId)){
            isSelf=true;
        }
        if(isSelf||isJoin==2||isJoin==3){//问答创建者可以删除问答，而且老师也具有删除问答的权限
            image_dele_layout.setVisibility(View.VISIBLE);
        }else {
            image_dele_layout.setVisibility(View.GONE);
        }
        if(isJoin==2||isJoin==3){//只有老师身份下展示精华
            image_elite_layout.setVisibility(View.VISIBLE);
        }else {
            image_elite_layout.setVisibility(View.GONE);
        }

        search_jiazai_layout.setVisibility(View.VISIBLE);
        httpRequestIsCreator();//根据课程id获取是否是创建者,成功则请求数据展示的接口，刷新时候这借口不请求
    myPullRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

//            if(newState== AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
//                Glide.with(QuestionsDetailActivity.this).resumeRequests();
//                //和
//            }else {
//                Glide.with(QuestionsDetailActivity.this).pauseRequests();
//            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // 隐藏或者显示fab
            if(questionsDetailBean.getItem().getBest_answer_id()>0){

            }else {
                if(dy > 0) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }

        }
    });
    }



    private void initView() {
        mainView= (RelativeLayout) findViewById(R.id.activity_questions_detail);
        fab= (FloatingActionButton) findViewById(R.id.fab);
        load_fail_button = (Button) findViewById(R.id.load_fail_button);
        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        no_info_layout = (LinearLayout)findViewById(R.id.no_info_layout);
        search_jiazai_layout = (LinearLayout)findViewById(R.id.jiazai_layout);
        questionId=getIntent().getIntExtra("questionId",0);
        isJoin=getIntent().getIntExtra("isJoin",0);
        nickname=getIntent().getStringExtra("nickname");
        userId=getIntent().getStringExtra("userId");
        courseId=getIntent().getStringExtra("courseId");
        replyNum=getIntent().getStringExtra("replyNum");
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        myPullRecyclerView = (MyPullRecyclerView) findViewById(R.id.myPullRecyclerView);
//        reply_question= (ImageView) findViewById(R.id.reply_question);
        popView= LayoutInflater.from(this).inflate(R.layout.popview_question_detail,null);
        image_store= (ImageView) popView.findViewById(R.id.image_store);
        image_zan= (ImageView) popView.findViewById(R.id.image_zan);
        image_elite= (ImageView) popView.findViewById(R.id.image_elite);
        image_dele= (ImageView) popView.findViewById(R.id.image_dele);
        text_store= (TextView) popView.findViewById(R.id.text_store);
        text_zan= (TextView) popView.findViewById(R.id.text_zan);
        text_elite= (TextView) popView.findViewById(R.id.text_elite);

        pop_content= (LinearLayout) popView.findViewById(R.id.pop_content);
        image_elite_layout= (LinearLayout) popView.findViewById(R.id.image_elite_layout);
        image_dele_layout= (LinearLayout) popView.findViewById(R.id.image_dele_layout);

        popupWindow=new PopupWindow(this);
        popupWindow.setContentView(popView);
        popupWindow.setWidth(LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
//        popupWindow.setHeight((int) (ScreenUtils.getScreenHeight(this)*0.9));
        popupWindow.setHeight(LayoutParams.MATCH_PARENT);
       // Log.d("zw--h",popupWindow.getHeight()+"hhh");

        //设置PopupWindow弹出窗体可点击
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
    }
    private void initClick() {
        popView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               // Log.d("zw--h",event.getY()+"--vvv--"+(ScreenUtils.getScreenHeight(QuestionsDetailActivity.this)-pop_content.getHeight())+"--xxxx--"+ScreenUtils.getScreenHeight(QuestionsDetailActivity.this)+"--tt--"+pop_content.getHeight()+"--ppp");
                if(event.getY()<(ScreenUtils.getScreenHeight(QuestionsDetailActivity.this)-pop_content.getHeight())){
                    popupWindow.dismiss();
                }
                return false;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        load_fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(questionReplyListAdapter!=null){
                    questionReplyListAdapter.resetPageIndex();
                }
                load_fail_layout.setVisibility(View.GONE);
                no_info_layout.setVisibility(View.GONE);
                search_jiazai_layout.setVisibility(View.VISIBLE);
                httpRequestIsCreator();//根据课程id获取是否是创建者,成功则请求数据展示的接口，刷新时候这借口不请求
            }
        });
//        reply_question.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(new SharedPreferencesUtil(QuestionsDetailActivity.this).getIsLogin())){
                    startActivityForResult(new Intent(QuestionsDetailActivity.this, LoginActivity.class).putExtra("course_questions","course_questions"), 1);
                    return;
                }else if(isJoin==0){
                    ToastUtils.makeText(QuestionsDetailActivity.this,getResources().getString(R.string.remind_join));
                    return;
                } else {
                    startActivityForResult(new Intent(QuestionsDetailActivity.this,QuestionsReplyPublishActivity.class)
                            .putExtra("commitType","reply_question")
                            .putExtra("questionId",questionId)
                            .putExtra("role",isClickElite==true?1:2)//true代表此时是老师身份1，否则按学生身份传2
                            .putExtra("questionContent",questionsDetailBean.getItem().getTitle())
                            .putExtra("courseId",courseId),1);
                }
            }
        });
        myPullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                questionReplyListAdapter.addPageIndex();
                httpRequestOne();
            }
        });
        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isScrollhead=true;
                questionReplyListAdapter.resetPageIndex();
                httpRequestOne();
            }
        });
        image_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_follow==1){
                    is_follow=0;
                }else {
                    is_follow=1;
                }
                postHttpRequest(1001);
            }
        });
        image_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_praise==1){
                    is_praise=0;
                }else {
                    is_praise=1;
                }
                postHttpRequest(1002);
            }
        });
            image_elite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search_jiazai_layout.setVisibility(View.VISIBLE);
                    if(!isClickElite) {//只有创建者或者老师可以
                        ToastUtils.makeText(QuestionsDetailActivity.this,"只有老师可以操作“精华");
                    return;
                    }
                        if(is_elite==1){
                        is_elite=0;
                    }else {
                        is_elite=1;
                    }
                    new HttpPutBuilder(QuestionsDetailActivity.this).setUrl(UrlsNew.URLHeader+"/question/question").setHttpResult(new HttpCallBack() {
                        @Override
                        public void setOnSuccessCallback(int requestCode, Object resultBean) {
                            search_jiazai_layout.setVisibility(View.GONE);
                            sendBroadcast(new Intent().setAction(Constants.QUESTION_UPDATE).putExtra("position",3));
                            if(is_elite==1){
                                image_elite.setImageResource(R.drawable.question_elite);
                                text_elite.setText("取消精华");
                            }else {
                                image_elite.setImageResource(R.drawable.question_un_elite);
                                text_elite.setText("设为精华");
                            }
                        }

                        @Override
                        public void setOnErrorCallback(int requestCode, int code, String msg) {
                            search_jiazai_layout.setVisibility(View.GONE);
                            //失败数据再复原
                            if(is_elite==1){
                                is_elite=0;
                            }else {
                                is_elite=1;
                            }
                        }
                    })
                            .setPath(questionId+"")
                            .addBodyParam("is_elite",is_elite+"").build();
                }
            });

        image_dele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });


    }
    private QuestionsDetailBean questionsDetailBean=null;
    //获取问答头部详情
    private void httpRequestOne() {
        new HttpGetBuilder(this).setClassObj(QuestionsDetailBean.class).setUrl(UrlsNew.URLHeader+"/question/question")
                .setHttpResult(this)
                .setPath(questionId+"")
                .setRequestCode(1000)
                .build();
    }
    //获取回复列表
    private void httpRequestTwo() {
        new HttpGetBuilder(this).setClassObj(QuestionsReplyListBean.class).setUrl(UrlsNew.URLHeader+"/question/answer")
                .setHttpResult(this)
                .addQueryParams("page","1")
                .addQueryParams("pageSize","1000")
                .addQueryParams("orderBy","create_time desc")
                .addQueryParams("qa_id",questionId+"")
                .addQueryParams("pid","0")
                .setRequestCode(1002)
                .build();
    }
    //获取这个问答所属课程的信息
    private void httpRequestIsCreator() {
        new HttpGetBuilder(this).setUrl(UrlsNew.URLHeader+"/course").setClassObj(QuestionsDetailBean.class)
                .setPath(courseId).setHttpResult(this).setRequestCode(200).build();
    }
    //获取当前用户的这问答的点赞等等的状态
    private void httpRequestPopData(){
        new HttpGetBuilder(this).setUrl(UrlsNew.URLHeader+"/question/follow")
                .setClassObj(QuestionsDetailBean.class)
                .setRequestCode(201).setHttpResult(this)
                .addQueryParams("course_id",courseId)
                .addQueryParams("qa_id",questionId+"")
                .addQueryParams("user_id",new SharedPreferencesUtil(this).getUid())
                .setPath(questionId+"").build();
    }
    public QuestionsDetailBean getQuestion(){
        return questionsDetailBean;
    }
    private void postHttpRequest(int code){
        search_jiazai_layout.setVisibility(View.VISIBLE);
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("course_id",courseId);
            jsonObject.put("qa_id",questionId);
            if(code==1002){
                jsonObject.put("is_praise",is_praise);
            }else if(code==1001){
                jsonObject.put("is_follow",is_follow);
            }
            jsonObject.put("user_id",new SharedPreferencesUtil(QuestionsDetailActivity.this).getUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new HttpPostBuilder(QuestionsDetailActivity.this).setUrl(UrlsNew.URLHeader+"/question/follow")
                .setRequestCode(code)
                .setClassObj(null)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        search_jiazai_layout.setVisibility(View.GONE);
//                        ToastUtils.makeText(QuestionsDetailActivity.this,requestCode+"code"+questionId+"question"+new SharedPreferencesUtil(QuestionsDetailActivity.this).getUid(), Toast.LENGTH_LONG).show();
                        if(requestCode==1002){
                            if(is_praise==1){
                                image_zan.setImageResource(R.drawable.question_zan);
                                text_zan.setText("取消赞");
                            }else {
                                image_zan.setImageResource(R.drawable.question_un_zan);
                                text_zan.setText("赞");
                            }
                        }

                        if(requestCode==1001){
                            sendBroadcast(new Intent().setAction(Constants.QUESTION_UPDATE).putExtra("position",5));
                            if(is_follow==1){
                                image_store.setImageResource(R.drawable.question_store);
                                text_store.setText("取消收藏");
                            }else {
                                image_store.setImageResource(R.drawable.question_un_store);
                                text_store.setText("收藏");
                            }
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        search_jiazai_layout.setVisibility(View.GONE);
                        ToastUtils.makeText(QuestionsDetailActivity.this,msg);
                        //post失败需要再次变回之前的状态
                        if(requestCode==1002){
                            if(is_praise==1){
                                is_praise=0;
                            }else {
                                is_praise=1;
                            }
                        }

                        if(requestCode==1001){
                            if(is_follow==1){
                                is_follow=0;
                            }else {
                                is_follow=1;
                            }
                        }

                    }
                })
                .addBodyParam(jsonObject.toString())
                .build();
    }
    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        if(requestCode==2){
            CourseRoleResult courseClassBean = (CourseRoleResult) resultBean;
            //0未加入 1学生 2老师 3创建课程的老师
            isJoin = courseClassBean.getItem().getRole();
            //通知问答列表首页刷新
            sendBroadcast(new Intent().setAction("from_activity").putExtra("isJoin",isJoin));
            if(isJoin!=0){
                httpRequestIsCreator();
            }
            //该页面重新登录后请求接口成功后重新赋值再次判断
            if(isJoin==2||isJoin==3){
                image_dele_layout.setVisibility(View.VISIBLE);
            }else {
                image_dele_layout.setVisibility(View.GONE);
            }

        }


        QuestionsReplyListBean questionsReplyListBean;
        if(requestCode==200){
            QuestionsDetailBean bean= (QuestionsDetailBean) resultBean;//仅仅借用这个bean中的userId（获取课程创建者的id），因为都是item格式的对象
            courseName=bean.getItem().getTitle();
            if(new SharedPreferencesUtil(QuestionsDetailActivity.this).getUid().equals(bean.getItem().getUser_id())){
                isClickElite=true;
            }
            httpRequestPopData();
        }
        if(requestCode==201){
            QuestionsDetailBean detailBean= (QuestionsDetailBean) resultBean;//近借用这个bean只用来获取点赞以及收藏状态这俩值
            is_praise=detailBean.getItem().getIs_praise();
            is_follow=detailBean.getItem().getIs_follow();
            if(is_praise==1){
                image_zan.setImageResource(R.drawable.question_zan);
                text_zan.setText("取消赞");
            }else {
                image_zan.setImageResource(R.drawable.question_un_zan);
                text_zan.setText("赞");
            }
            if(is_follow==1){
                image_store.setImageResource(R.drawable.question_store);
                text_store.setText("取消收藏");
            }else {
                image_store.setImageResource(R.drawable.question_un_store);
                text_store.setText("收藏");
            }
            //获取页面数据
            httpRequestOne();
        }


        if(requestCode==1000){
            questionsDetailBean= (QuestionsDetailBean) resultBean;
            is_elite=questionsDetailBean.getItem().getIs_elite();
            if(questionsDetailBean.getItem().getIs_teachers()==1){
                isClickElite=true;
            }
            if(questionsDetailBean.getItem().getBest_answer_id()>0){//代表已解决，则不显示回复按钮
//                reply_question.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
            }else {
//                reply_question.setVisibility(View.VISIBLE);
                fab.setVisibility(View.VISIBLE);
            }
            if(isClickElite){//此时两次判断接口数据已经拿到
                image_elite_layout.setVisibility(View.VISIBLE);
            }else {
                image_elite_layout.setVisibility(View.GONE);
            }

            if(questionsDetailBean.getItem().getIs_elite()==1){
                image_elite.setImageResource(R.drawable.question_elite);
                text_elite.setText("取消精华");
            }else {
                image_elite.setImageResource(R.drawable.question_un_elite);
                text_elite.setText("设为精华");
            }
            httpRequestTwo();
        }
        if(requestCode==1002){
            if(menuItem!=null&&isJoin>1){
                menuItem.setVisible(true);
            }
            network_Sucess=true;

            search_jiazai_layout.setVisibility(View.GONE);
            myPullRecyclerView.setVisibility(View.VISIBLE);
            questionsReplyListBean= (QuestionsReplyListBean) resultBean;
            if(questionReplyListDelegate==null){
                questionReplyListDelegate=new QuestionReplyListDelegate(this,nickname,courseName,replyNum);
                questionReplyListAdapter=new QuestionReplyListAdapter(this,beanList,questionReplyListDelegate);
//                questionReplyListAdapter.setHeaderCount(1);
                myPullRecyclerView.setAdapter(questionReplyListAdapter);
                questionReplyListAdapter.setSwipeRefresh(myPullSwipeRefresh);
            }
            questionReplyListAdapter.setTotalPage(questionsReplyListBean.getPaginate().getPageNum());
            questionReplyListAdapter.setPullData(questionsReplyListBean.getItems());
            if(isScrollend){
                myPullRecyclerView.scrollToPosition(1);
                myPullRecyclerView.smoothScrollToPosition(1);
                //   myPullRecyclerView.smoothScrollBy(0,600);
                isScrollend=false;
                isScrollhead=false;
            }
            if(isScrollhead){
                myPullRecyclerView.smoothScrollToPosition(0);
                 myPullRecyclerView.smoothScrollBy(0,-600);
                isScrollhead=false;
            }

        }
        if(questionReplyListAdapter!=null){
            questionReplyListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if(position>=0){
                        Intent intent=new Intent(QuestionsDetailActivity.this, QuestionReplyDetailsActivity.class);
                        intent.putExtra("courseId",questionReplyListAdapter.getData().get(position).getCourse_id());
                        intent.putExtra("questionId",Integer.valueOf(questionReplyListAdapter.getData().get(position).getQa_id()));
                        intent.putExtra("reply_userId",questionReplyListAdapter.getData().get(position).getUser().getId());
                        intent.putExtra("pid",questionReplyListAdapter.getData().get(position).getId());
                        intent.putExtra("header", questionReplyListAdapter.getData().get(position));
                        intent.putExtra("best_answer_id",questionsDetailBean.getItem().getBest_answer_id());
                        intent.putExtra("isJoin",isJoin);
                        intent.putExtra("userId",userId);
                        intent.putExtra("nickname",questionReplyListAdapter.getData().get(position).getUser().getNickname());
                        if(isClickElite){
                            intent.putExtra("role",1);
                        }else {
                            intent.putExtra("role",2);
                        }
//                    Log.d("zw--",isClickElite+"+++");
                        Glide.get(view.getContext()).clearMemory();
                        startActivityForResult(intent,1);

                    }

                }
            });
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        if(menuItem!=null){
            network_Sucess=false;
        }
        menuItem.setVisible(false);
        myPullSwipeRefresh.setRefreshing(false);
//        reply_question.setVisibility(View.GONE);
        fab.setVisibility(View.GONE);
        myPullRecyclerView.setVisibility(View.GONE);
        search_jiazai_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);

        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
        {
            NetworkUtil.httpRestartLogin(QuestionsDetailActivity.this, mainView);
        }else {
            load_fail_layout.setVisibility(View.VISIBLE);
            ToastUtils.makeText(QuestionsDetailActivity.this,msg);
        }
    }
    private MenuItem menuItem;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tea_work_menu, menu);
        menuItem=menu.findItem(R.id.action_setting);
        if(isJoin==0){
            menu.findItem(R.id.action_setting).setVisible(false);
        }
        menu.findItem(R.id.action_setting).setTitle("").setIcon(R.drawable.three_points);
        menu.findItem(R.id.action_search).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_setting&&network_Sucess){
            popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.trans_half));
            popupWindow.showAtLocation(findViewById(R.id.activity_questions_detail), Gravity.BOTTOM,0,0);
            popView.setAnimation(AnimationUtils.loadAnimation(this,R.anim.push_bottom_in_2));

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1&&resultCode==1){
            isScrollend=true;
            search_jiazai_layout.setVisibility(View.VISIBLE);
            questionReplyListAdapter.resetPageIndex();
            httpRequestOne();
        }
        if(requestCode==Constants.RESTART_LOGIN&&resultCode==Constants.LOGIN_BACK){
            search_jiazai_layout.setVisibility(View.VISIBLE);
            httpRequestIsCreator();//根据课程id获取是否是创建者,成功则请求数据展示的接口，刷新时候这借口不请求
        }
        if(requestCode==1&&resultCode==Constants.LOGIN_BACK){//点击编辑按钮登录后的返回，请求此课程下的角色接口
            sendBroadcast(new Intent().setAction(Constants.QUESTION_LOGIN));
            if(new SharedPreferencesUtil(this).getUid().equals(userId)){
                isSelf=true;
            }
            if(isSelf){//先判断是否是问答创建者，请求接口后继续判断是否是老师再次做判断
                image_dele_layout.setVisibility(View.VISIBLE);
            }else {
                image_dele_layout.setVisibility(View.GONE);
            }
            search_jiazai_layout.setVisibility(View.VISIBLE);
            //登录成功返回后请求此用户在该课程下的角色
            new HttpGetBuilder(QuestionsDetailActivity.this)
                    .setHttpResult(this)
                    .setClassObj(CourseRoleResult.class)
                    .setUrl(UrlsNew.GET_COURSE_ROLE)
                    .addQueryParams("course_id", courseId+"")
                    .setRequestCode(2)
                    .build();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.custom_dialog);
        builder.setMessage("您确定要删除吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                search_jiazai_layout.setVisibility(View.VISIBLE);
                new HttpDeleteBuilder(QuestionsDetailActivity.this).setUrl(UrlsNew.URLHeader+"/question/question")
                        .setClassObj(null).setPath(questionId+"").setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        search_jiazai_layout.setVisibility(View.GONE);
                        if(popupWindow.isShowing()){
                            popupWindow.dismiss();
                        }
                        setResult(1);
                        finish();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        search_jiazai_layout.setVisibility(View.GONE);
                        ToastUtils.makeText(QuestionsDetailActivity.this,msg);
                    }
                }).build();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        Glide.get(this).clearMemory();
        super.onDestroy();
    }
}
