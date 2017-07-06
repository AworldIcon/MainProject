package com.coder.kzxt.message.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;

import com.app.http.HttpGetOld;
import com.app.http.InterfaceHttpResult;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.adapter.PagerSlidingTabStripAdapter;
import com.coder.kzxt.message.fragment.TestFragment;
import com.coder.kzxt.message.fragment.WorkFragment;
import com.coder.kzxt.question.beans.QuestionBean;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.Urls;

import java.util.ArrayList;


/**
 * 群组的功能列表包含作业 考试 签到
 *
 * @author pc
 */
public class FunctionListActivity extends BaseActivity implements InterfaceHttpResult
{

    private TabLayout tabs;
    private ViewPager pager;
    private Toolbar toolbar;

    private ArrayList<Fragment> fragments;
    private ArrayList<String> userChannelList;

    private PagerSlidingTabStripAdapter tabStripAdapter;
    private DisplayMetrics dm;
    private SharedPreferencesUtil pu;
    private String publicCourse;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_my);

        initView();
        initData();
        initListener();
        httpRequest();
    }

    private void httpRequest()
    {
        showLoadingView();
        // 获取课程下的考试 作业 签到 列表
        new HttpGetOld(this, this, this, QuestionBean.class, Urls.GET_MY_QUESTION, "studentNewReply").excute();

    }

    //控件
    private void initView()
    {

        dm = getResources().getDisplayMetrics();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (TabLayout) findViewById(R.id.tabs);

        setSupportActionBar(toolbar);

        pu = new SharedPreferencesUtil(this);
        publicCourse = "0";

    }

    private void initData()
    {
        Bundle bundle = new Bundle();
        bundle.putString("course_id", "4");
        bundle.putString("class_id", "1");

        userChannelList = new ArrayList<String>();
        userChannelList.add("考试");
        userChannelList.add("作业");
//        userChannelList.add("签到");

        fragments = new ArrayList<>();
        WorkFragment workFragment =new WorkFragment();
        workFragment.setArguments(bundle);
        TestFragment workFragment2 =new TestFragment();
        workFragment2.setArguments(bundle);
//        SignDetailSignedFragment workFragment3 =new SignDetailSignedFragment();
//        workFragment3.setArguments(bundle);

        fragments.add(workFragment);
        fragments.add(workFragment2);
//        fragments.add(workFragment3);

        if (fragments.size() > 4)
        {
            tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        }

        tabStripAdapter = new PagerSlidingTabStripAdapter(getSupportFragmentManager(), userChannelList, fragments);
        pager.setAdapter(tabStripAdapter);
        int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        tabs.setupWithViewPager(pager);
        pager.setOffscreenPageLimit(fragments.size());

    }

    //监听器
    private void initListener()
    {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        hideLoadingView();

        if (Constants.RESTART_LOGIN == requestCode && resultCode == Constants.LOGIN_BACK)
        {
            // 刷新列表
//            publish_Fragment.refreshHttpRequest();
            httpRequest();

        } else if (requestCode == 4 && resultCode == 5)
        {
            //刷新喜欢人数
//            String likeNum = data.getStringExtra("likeNum");
//            String postersId = data.getStringExtra("postersId");
//            publish_Fragment.onPostersLikeNumChange(postersId, likeNum);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void getCallback(int requestCode, int code, String msg, Object baseBean)
    {
        hideLoadingView();
        if (code == Constants.HTTP_CODE_SUCCESS)
        {
            QuestionBean questionBean = (QuestionBean) baseBean;


        } else if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
        {
            //重新登录
            NetworkUtil.httpRestartLogin(FunctionListActivity.this, pager);
        } else
        {
            NetworkUtil.httpNetErrTip(FunctionListActivity.this, pager);
        }
    }

    public static void gotoActivity(Context context)
    {
        context.startActivity(new Intent(context, FunctionListActivity.class));
    }

}