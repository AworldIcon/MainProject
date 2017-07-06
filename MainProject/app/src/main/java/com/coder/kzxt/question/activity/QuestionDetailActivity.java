package com.coder.kzxt.question.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.app.http.HttpGetOld;
import com.app.http.HttpPostOld;
import com.app.http.InterfaceHttpResult;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.dialog.util.CustomListDialog;
import com.coder.kzxt.question.adapter.QuestionDetailDelegate;
import com.coder.kzxt.question.beans.QuestionBean;
import com.coder.kzxt.question.beans.QuestionDetailBean;
import com.coder.kzxt.recyclerview.MyPullRecyclerView;
import com.coder.kzxt.recyclerview.MyPullSwipeRefresh;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.ShareSdkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.utils.Urls;

import java.util.ArrayList;
import java.util.List;


/**
 * 问答详情
 *
 * @author pc
 */

public class QuestionDetailActivity extends BaseActivity implements InterfaceHttpResult
{

    private Toolbar toolbar;
    private RelativeLayout answer_ly;
    private MyPullSwipeRefresh myPullSwipeRefresh;
    private MyPullRecyclerView myPullRecyclerView;

    private QuestionDetailDelegate detailDelegate;
    private PullRefreshAdapter pullRefreshAdapter;

    //上页传递过来的数据
    private QuestionBean.DataBean.QuestionListBean questionListBean;
    //本页接收数据
    private List<QuestionDetailBean.DataBean.AnswerListBean> mData;
    private QuestionDetailBean.DataBean.QuestionBean questionDataBean;

    private CustomListDialog customDialog;
    private SharedPreferencesUtil spu;
    private String publicCourse;
    private String uId;


    private void initVariable()
    {

        questionListBean = (QuestionBean.DataBean.QuestionListBean) getIntent().getSerializableExtra("bean");
        publicCourse = getIntent().getStringExtra("publicCourse");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        //获取从其他页面获取的变量 getIntent
        initVariable();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        //初始化 view findviewbyid
        initFindViewById();
        //需要初始化的数据
        initData();
        //响应事件click
        initListener();
        //网络请求
        showLoadingView();
        httpRequest();
    }

    private void initFindViewById()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.pics_menu));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(questionListBean.getUsername());
        myPullSwipeRefresh = (MyPullSwipeRefresh) findViewById(R.id.myPullSwipeRefresh);
        myPullRecyclerView = (MyPullRecyclerView) findViewById(R.id.myPullRecyclerView);
        answer_ly = (RelativeLayout) findViewById(R.id.answer_ly);
    }

    private void initData()
    {

        spu = new SharedPreferencesUtil(this);
        mData = new ArrayList<>();
        if (publicCourse.equals("1"))
        {
            uId = spu.getOpenUid();
        } else
        {
            uId = spu.getUid();
        }


    }

    //监听器
    private void initListener()
    {
        myPullSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                pullRefreshAdapter.resetPageIndex();
                httpRequest();
            }
        });

        myPullRecyclerView.setOnAddMoreListener(new MyPullRecyclerView.OnAddMoreListener()
        {
            @Override
            public void addMoreListener()
            {
                pullRefreshAdapter.addPageIndex();
                httpRequest();
            }
        });

    }

    private void httpRequest()
    {
        int pageIndex;
        if (pullRefreshAdapter == null)
        {
            pageIndex = 1;
        } else
        {
            pageIndex = pullRefreshAdapter.getPageIndex();
        }
        new HttpGetOld(this, this, this, QuestionDetailBean.class, Urls.GET_QUESTION_DETAIL, questionListBean.getCourseId(), questionListBean.getQuestionId(), publicCourse, pageIndex + "", "20").excute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        if (questionDataBean == null)
        {
            return super.onCreateOptionsMenu(menu);
        }

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.question_detail_menu, menu);
        MenuItem share_menu = menu.findItem(R.id.share_menu);
        MenuItem collection_menu = menu.findItem(R.id.collection_menu);
        MenuItem report_menu = menu.findItem(R.id.report_menu);
        MenuItem delete_menu = menu.findItem(R.id.delete_menu);
        MenuItem resume_menu = menu.findItem(R.id.resume_menu);

        //是否收藏过
        if (questionDataBean.getIsCollect().equals("0"))
        {
            collection_menu.setTitle(getResources().getString(R.string.favorite));
        } else
        {
            collection_menu.setTitle(getResources().getString(R.string.has_favorited));
        }

        //显示删除还是显示恢复
        if (questionDataBean.getIsDelete().equals("0"))
        {
            resume_menu.setVisible(false);
        } else
        {
            delete_menu.setVisible(false);
        }


        //判断角色 老师显示 学生不显示
        if (spu.getUserType().equals("1"))
        {
            //显示删除还是显示恢复
            if (questionDataBean.getIsDelete().equals("0"))
            {
                resume_menu.setVisible(false);
                delete_menu.setVisible(true);

            } else
            {
                delete_menu.setVisible(false);
                resume_menu.setVisible(true);
            }
        } else
        {
            delete_menu.setVisible(false);
            resume_menu.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();

                break;
            case R.id.share_menu:

                pullRefreshAdapter.notifyDataSetChanged();
                if(!TextUtils.isEmpty(questionDataBean.getShareUrl())){
                    ShareSdkUtil shareSdkUtil = new ShareSdkUtil(spu);
                    shareSdkUtil.shareSDK(QuestionDetailActivity.this, questionDataBean.getShareUrl()
                            , getResources().getString(R.string.share_before) + getResources().getString(R.string.app_name)
                                    + getResources().getString(R.string.share_after));
                }

                break;
            case R.id.collection_menu:

                String type;
                if (questionDataBean.getIsCollect().equals("0"))
                {
                    type = "collect";
                    questionDataBean.setIsCollect("1");
                } else
                {
                    type = "cancel";
                    questionDataBean.setIsCollect("0");
                }
                new HttpPostOld(QuestionDetailActivity.this, QuestionDetailActivity.this, QuestionDetailActivity.this, BaseBean.class, Urls.COLLECT_QUESTION, questionDataBean.getCourseId(), questionDataBean.getQuestionId(), publicCourse, type).excute(1001);
                break;

            case R.id.report_menu:

                if (customDialog == null)
                {
                    customDialog = new CustomListDialog(QuestionDetailActivity.this);
                    customDialog.addData(getResources().getString(R.string.empty_message), getResources().getString(R.string.rubbish_ad), getResources().getString(R.string.yellow), getResources().getString(R.string.body_atrack), getResources().getString(R.string.anyotner), getResources().getString(R.string.cancel));
                    customDialog.getAdapter().setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(View view, int position)
                        {

                            if (position != 5)
                            {
                                new HttpPostOld(QuestionDetailActivity.this, QuestionDetailActivity.this, QuestionDetailActivity.this, BaseBean.class, Urls.REPORT_ACTION, questionDataBean.getQuestionId(), "question").excute(1002);
                            }
                            customDialog.dismiss();
                        }
                    });
                }
                customDialog.show();

                break;
            case R.id.delete_menu:

                questionDataBean.setIsDelete("1");
                new HttpPostOld(QuestionDetailActivity.this, QuestionDetailActivity.this, QuestionDetailActivity.this, BaseBean.class, Urls.CLOSE_QUESTION, questionDataBean.getCourseId(), questionDataBean.getQuestionId(), publicCourse).excute(1003);

                break;
            case R.id.resume_menu:

                questionDataBean.setIsDelete("0");
                new HttpPostOld(QuestionDetailActivity.this, QuestionDetailActivity.this, QuestionDetailActivity.this, BaseBean.class, Urls.REVERT_QUESTION, questionDataBean.getCourseId(), questionDataBean.getQuestionId(), publicCourse).excute(1003);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        hideLoadingView();

        if (Constants.RESTART_LOGIN == requestCode && resultCode == Constants.LOGIN_BACK)
        {
            httpRequest();

        } else if (requestCode == 4 && resultCode == 5)
        {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void getCallback(int requestCode, int code, String msg, Object baseBean)
    {
        hideLoadingView();

        // 先判断是否 执行成功
        if (code == Constants.HTTP_CODE_SUCCESS)
        {
            //收藏
            if (requestCode == 1001)
            {
                invalidateOptionsMenu();
                ToastUtils.makeText(QuestionDetailActivity.this, getResources().getString(R.string.operate_success));

            } else if (requestCode == 1002)
            {
                //举报
                ToastUtils.makeText(QuestionDetailActivity.this, getResources().getString(R.string.report_success));

            } else if (requestCode == 1003)
            {
                //删除 恢复
                ToastUtils.makeText(QuestionDetailActivity.this, getResources().getString(R.string.report_success));
                setResult(222);
                finish();

            } else
            {
                QuestionDetailBean questionDetailBean = (QuestionDetailBean) baseBean;
                questionDataBean = questionDetailBean.getData().getQuestion();
                if (detailDelegate == null)
                {
                    detailDelegate = new QuestionDetailDelegate(this, questionDataBean);
                    pullRefreshAdapter = new PullRefreshAdapter(this, mData, 1, detailDelegate);
                    myPullRecyclerView.setAdapter(pullRefreshAdapter);
                    pullRefreshAdapter.setSwipeRefresh(myPullSwipeRefresh);

                    //设置服务器没有的参数
                    questionDataBean.setPublicCourse(publicCourse);
                    questionDataBean.setMyId(uId);

                }
                //刷新右上角按钮
                invalidateOptionsMenu();

                 //问题没有删除 并且未解决 如果是自己的问题则显示
                if (questionDataBean.getIsDelete().equals("0") && questionDataBean.getQuestionStatus().equals("0") && questionDataBean.getUid().equals(uId))
                {
                    answer_ly.setVisibility(View.VISIBLE);
                } else
                {
                    answer_ly.setVisibility(View.GONE);

                }
                pullRefreshAdapter.setPullData(questionDetailBean.getData().getAnswerList());

            }

        } else if (code == Constants.HTTP_CODE_2001)

        {
            //重新登录
            NetworkUtil.httpRestartLogin(QuestionDetailActivity.this, answer_ly);
        } else

        {
            ToastUtils.makeText(QuestionDetailActivity.this, msg);
            NetworkUtil.httpNetErrTip(QuestionDetailActivity.this, answer_ly);
        }
    }

}