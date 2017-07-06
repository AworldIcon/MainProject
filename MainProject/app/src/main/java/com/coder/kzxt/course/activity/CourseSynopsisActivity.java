package com.coder.kzxt.course.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.beans.CourseBean;
import com.coder.kzxt.course.adapter.CourseCommentDelegate;
import com.coder.kzxt.course.beans.CommentBean;
import com.coder.kzxt.course.beans.CourseSynopsosBean;
import com.coder.kzxt.course.beans.TeachearBean;
import com.coder.kzxt.login.activity.LoginActivity;
import com.coder.kzxt.order.activity.OrderConfirmationActivity;
import com.coder.kzxt.order.beans.CourseClass;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.app.http.UrlsNew;

import java.util.ArrayList;
import java.util.List;

public class CourseSynopsisActivity extends BaseActivity implements HttpCallBack {

    private String treeId;
    private int isJoin;
    private SharedPreferencesUtil spu;
    private Toolbar toolbar;
    private LinearLayout my_layout;

    private RelativeLayout join_tip_ly;

    private MyPullRecyclerView myPullRecyclerView;
    private PullRefreshAdapter pullRefreshAdapter;
    private List<CommentBean.ItemsBean> commentBeens;
    private CourseCommentDelegate delegate;
    private LinearLayout join_ly;
    private Button ask_button;
    private Button join_button;
    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private Button load_fail_button;

    private RelativeLayout activity_course_synopsis;
    private int httpNum =0;
    private boolean isSetAdapter =false;
    private boolean resLastActivty =false;
    private boolean isPaySuccess = false;
    private double classPrice;
    private String course_class_id;
    private String classId;
    private ArrayList<CourseClass.ClassBean> classItems;


    Handler handler = new Handler();
    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            join_tip_ly.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_synopsis);
        spu = new SharedPreferencesUtil(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.course_introduce));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        treeId = getIntent().getStringExtra("treeId");
        isJoin = getIntent().getIntExtra("isJoin",0);
        commentBeens = new ArrayList<>();
        intView();
        initOnClick();
        requesSynopsis();
        requesTeachers();
        requesComment(1);
        getOrderClass();
    }

    private void intView(){
        activity_course_synopsis = (RelativeLayout) findViewById(R.id.activity_course_synopsis);
        my_layout = (LinearLayout) findViewById(R.id.my_layout);
        myPullRecyclerView = (MyPullRecyclerView) findViewById(R.id.myPullRecyclerView);
        join_ly = (LinearLayout) findViewById(R.id.join_ly);
        ask_button = (Button) findViewById(R.id.ask_button);
        join_button = (Button) findViewById(R.id.join_button);
        join_tip_ly = (RelativeLayout) findViewById(R.id.join_tip_ly);

        jiazai_layout = (LinearLayout)findViewById(R.id.jiazai_layout);
        jiazai_layout.setVisibility(View.VISIBLE);
        load_fail_layout = (LinearLayout)findViewById(R.id.load_fail_layout);
        load_fail_button = (Button)findViewById(R.id.load_fail_button);

    }


    private void initOnClick(){

        load_fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpNum=0;
                jiazai_layout.setVisibility(View.VISIBLE);
                load_fail_layout.setVisibility(View.GONE);
                requesSynopsis();
                requesTeachers();
                requesComment(1);
            }
        });

        myPullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener() {
            @Override
            public void addMoreListener() {
                pullRefreshAdapter.addPageIndex();
                requesComment(pullRefreshAdapter.getPageIndex());
            }
        });

        ask_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(spu.getIsLogin())){
                    startActivityForResult(new Intent(CourseSynopsisActivity.this, LoginActivity.class),1);
                    return;
                }
            }
        });

        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(spu.getIsLogin())){
                    startActivityForResult(new Intent(CourseSynopsisActivity.this, LoginActivity.class),1);
                    return;
                }
                if (classPrice == 0.0 && (classItems != null && classItems.size() == 1)) { //直接加入学习
                    joinStudy(treeId,course_class_id);
                } else {
                    Intent intent = new Intent(CourseSynopsisActivity.this, OrderConfirmationActivity.class);
                    intent.putExtra("courseId", treeId);
                    startActivityForResult(intent, Constants.REQUEST_CODE);
                }

            }
        });
    }

    public void showJoinTip(){
        join_tip_ly.setVisibility(View.VISIBLE);
        handler.postDelayed(runnable, 2000);// 打开定时器，执行操作
    }

    private void requesSynopsis(){
        new HttpGetBuilder(this)
                .setHttpResult(this)
                .setClassObj(CourseSynopsosBean.class)
                .setUrl(UrlsNew.GET_COURSE_SYNOPSIS)
                .setPath(treeId)
                .setRequestCode(1)
                .build();
    }

    private void requesTeachers(){
        new HttpGetBuilder(this)
                .setHttpResult(this)
                .setClassObj(TeachearBean.class)
                .setUrl(UrlsNew.COURSE_TEADHCER)
                .addQueryParams("course_id",treeId)
                .setRequestCode(2)
                .build();
    }

    private void requesComment(int index){
        new HttpGetBuilder(this)
                .setHttpResult(this)
                .setClassObj(CommentBean.class)
                .setUrl(UrlsNew.GET_COURSE_REVIEW)
                .addQueryParams("course_id",treeId)
                .addQueryParams("page", index + "")
                .addQueryParams("pageSize", "20")
                .setRequestCode(3)
                .build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(resLastActivty){
                    setResult(Constants.RES_BACK_AC);
                }else if(isPaySuccess){
                    paySuccess();
                }
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void paySuccess() {
        Intent intent = new Intent();
        intent.putExtra("classId",classId);
        setResult(Constants.PAY_SUCCESS,intent);
    }

    private CourseBean courseBean;
    private TeachearBean teachearBean;
    private CommentBean commentBean;

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        httpNum++;
        if(requestCode==1){
                CourseSynopsosBean courseSynopsosBean = (CourseSynopsosBean) resultBean;
                 courseBean = courseSynopsosBean.getItem();
            }else if(requestCode==2){
                 teachearBean = (TeachearBean) resultBean;

        }else if(requestCode==3){
            commentBean = (CommentBean) resultBean;
            commentBeens.clear();
            commentBeens.addAll(commentBean.getItems());
        }

        if(httpNum>=3){
            if(!isSetAdapter){
                delegate = new CourseCommentDelegate(this,courseBean,teachearBean,treeId,isJoin);
                pullRefreshAdapter = new PullRefreshAdapter(CourseSynopsisActivity.this, commentBeens,1, delegate);
                myPullRecyclerView.setAdapter(pullRefreshAdapter);
                isSetAdapter = true;
            }
            pullRefreshAdapter.notifyDataSetChanged();
            jiazai_layout.setVisibility(View.GONE);
            my_layout.setVisibility(View.VISIBLE);
            if(isJoin!=0){
                join_ly.setVisibility(View.GONE);
            }else {
                join_ly.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        load_fail_layout.setVisibility(View.VISIBLE);
        jiazai_layout.setVisibility(View.GONE);
        NetworkUtil.httpNetErrTip(CourseSynopsisActivity.this,activity_course_synopsis);
        if(code== Constants.HTTP_CODE_2001||code==Constants.HTTP_CODE_2004){
            NetworkUtil.httpRestartLogin(CourseSynopsisActivity.this,activity_course_synopsis);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.LOGIN_BACK) {
            resLastActivty =true;
            join_ly.setVisibility(View.GONE);
        }else  if(requestCode==1&&resultCode==1){
            pullRefreshAdapter.resetPageIndex();
            requesComment(pullRefreshAdapter.getPageIndex());
        } else if (resultCode == Constants.PAY_SUCCESS) { //支付成功
            classId = data.getStringExtra("classId");
            isPaySuccess = true;
            if (TextUtils.isEmpty(classId)) {
                resLastActivty = true;
                join_ly.setVisibility(View.GONE);
                jiazai_layout.setVisibility(View.GONE);
            } else {
                joinStudy(treeId, classId);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(resLastActivty){
                setResult(Constants.RES_BACK_AC);
            } else if(isPaySuccess){
                paySuccess();
            }
            finish();
            return true;
        }

        return false;
    }

    /**
     * 获取课程下关联的班级
     */
    private void getOrderClass() {
                new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_COURSE_CLASS)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        CourseClass bean = (CourseClass) resultBean;
                        classItems = applyClass(bean.getItems());
                        changeJoinStudyState(classItems);
                        calculatePrice(classItems);
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(CourseSynopsisActivity.this, my_layout);
                        } else {
                            NetworkUtil.httpNetErrTip(CourseSynopsisActivity.this, my_layout);
                        }
                    }
                })
                .setClassObj(CourseClass.class)
                .addQueryParams("course_id", treeId)
                .build();
    }


    /**
     * 筛选加入的授课班
     * @param items
     * @return
     */
    private ArrayList<CourseClass.ClassBean> applyClass(ArrayList<CourseClass.ClassBean> items) {
        ArrayList<CourseClass.ClassBean> classList = new ArrayList<>();
        if (items != null && items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {
                CourseClass.ClassBean classBean = items.get(i);
                if (!classBean.getApply_status().equals("0")) {
                    classList.add(classBean);
                }
            }
            return classList;
        }
        return null;
    }

    private void calculatePrice(ArrayList<CourseClass.ClassBean> classItems)
    {
        if(null != classItems){
            for (int i = 0; i < classItems.size(); i++)
            {
                CourseClass.ClassBean classBean = classItems.get(i);
                if (classBean.getIs_default().equals("1"))
                {
                    classPrice = Double.parseDouble(classBean.getPrice());
                    course_class_id = classBean.getId();
                }
            }
        }
    }

    /**
     * 更改加入学习状态
     * @param classItems
     */
    private void changeJoinStudyState(ArrayList<CourseClass.ClassBean> classItems) {
        if(classItems != null && classItems.size() > 0){
            join_button.setClickable(true);
            join_button.setBackgroundResource(R.color.first_theme);
            join_button.setText(getResources().getString(R.string.join_study));
            join_button.setTextColor(getResources().getColor(R.color.bg_white));
        } else {
            join_button.setClickable(false);
            join_button.setBackgroundResource(R.color.bg_join_study);
            join_button.setText(getResources().getString(R.string.unable_to_join));
            join_button.setTextColor(getResources().getColor(R.color.font_no_join));
        }
    }

    /**
     *  加入学习
     */
    private void joinStudy(final String treeId, String calssId ){
                jiazai_layout.setVisibility(View.VISIBLE);
                new HttpPostBuilder(this)
                .setUrl(UrlsNew.POST_COURSE_CLASS_MEMBER)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        resLastActivty = true;
                        join_ly.setVisibility(View.GONE);
                        jiazai_layout.setVisibility(View.GONE);
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(CourseSynopsisActivity.this, my_layout);
                        } else {
                            NetworkUtil.httpNetErrTip(CourseSynopsisActivity.this, my_layout);
                        }
                        if (code == 3056) { //用户已加入
                            resLastActivty = true;
                            join_ly.setVisibility(View.GONE);
                        }
                        jiazai_layout.setVisibility(View.GONE);
                    }
                })
                .setClassObj(null)
                .addBodyParam("account",spu.getUserAccount())
                .addBodyParam("course_id", treeId)
                .addBodyParam("join_type","1")
                .addBodyParam("course_class_id", calssId)
                .build();

    }
}
