package com.coder.kzxt.question.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.app.http.HttpGetOld;
import com.app.http.InterfaceHttpResult;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.adapter.PagerSlidingTabStripAdapter;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.question.beans.QuestionBean;
import com.coder.kzxt.question.fragment.MyQuestionFragment;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.Urls;

import java.util.ArrayList;


/**
 * 我的问答
 *
 * @author pc
 */
public class MyQuestionActivity extends BaseActivity implements InterfaceHttpResult
{

    private TabLayout tabs;
    private ViewPager pager;
    private Toolbar toolbar;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> userChannelList = new ArrayList<String>();

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
        initListener();
        httpRequest();
    }

    private void httpRequest()
    {
        showLoadingView();
        // 老师  学生进行区分
        if (pu.getUserType().equals("0"))
        {
            new HttpGetOld(this, this, this, QuestionBean.class, Urls.GET_MY_QUESTION, "studentNewReply").excute();
        } else
        {
            new HttpGetOld(this, this, this, QuestionBean.class, Urls.GET_MY_QUESTION, "teacherWaitAnswer").excute();
        }
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

    //监听器
    private void initListener()
    {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.change_center_menu, menu);

        MenuItem item = menu.findItem(R.id.change_menu);
        if (publicCourse.equals("0"))
        {
            getSupportActionBar().setTitle(getResources().getString(R.string.self_question));
            item.setIcon(R.drawable.poseter_water);
        } else
        {
            getSupportActionBar().setTitle(getResources().getString(R.string.all_question));
            item.setIcon(R.drawable.poseter_list);
        }
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
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
            case R.id.change_menu:
                if (publicCourse.equals("0"))
                {
                    publicCourse = "1";
                } else
                {
                    publicCourse = "0";
                }
                invalidateOptionsMenu();
                refreshFragments();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshFragments()
    {
        for (Fragment fragment : fragments)
        {
            ((MyQuestionFragment) fragment).setPublicCourse(publicCourse);
        }
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
            fragments.clear();// 清空
            for (QuestionBean.DataBean.TypeListBean bean : questionBean.getData().getTypeList())
            {
                Bundle bundle = new Bundle();
                bundle.putString("Type", bean.getType());
                bundle.putString("publicCourse", publicCourse);

                MyQuestionFragment fragment = new MyQuestionFragment();
                fragment.setArguments(bundle);

                fragments.add(fragment);
                userChannelList.add(bean.getName());
            }

            if(fragments.size() > 4)
            {
                   tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
            }

            tabStripAdapter = new PagerSlidingTabStripAdapter(getSupportFragmentManager(), userChannelList, fragments);
            pager.setAdapter(tabStripAdapter);
            int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
            pager.setPageMargin(pageMargin);
            tabs.setupWithViewPager(pager);
            pager.setOffscreenPageLimit(fragments.size());

        } else if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004)
        {
            //重新登录
            NetworkUtil.httpRestartLogin(MyQuestionActivity.this, pager);
        } else
        {
            NetworkUtil.httpNetErrTip(MyQuestionActivity.this, pager);
        }
    }

}