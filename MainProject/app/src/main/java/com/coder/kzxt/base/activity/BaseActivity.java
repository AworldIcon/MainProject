package com.coder.kzxt.base.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.mobstat.StatService;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.utils.PublicUtils;
import com.coder.kzxt.utils.SharedPreferencesUtil;

public class BaseActivity extends AppCompatActivity
{
    // 加载布局
    View loadingView;
    // 请求失败布局
    View loadFailView;
    /**
     * 空页面数据布局
     */
    View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    //显示加载布局
    public void showLoadingView()
    {

        //如果为null 则创建添加布局，否则根据显示状态进行显示
        if (loadingView == null)
        {
            loadingView = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null);
            addContentView(loadingView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        } else if (loadingView.getVisibility() == View.GONE)
        {

            loadingView.setVisibility(View.VISIBLE);
        }

        if (loadFailView != null && loadFailView.getVisibility() == View.VISIBLE)
        {
            loadFailView.setVisibility(View.GONE);
        }


    }

    //隐藏加载布局
    public void hideLoadingView()
    {
        if (loadingView != null && loadingView.getVisibility() == View.VISIBLE)
        {
            loadingView.setVisibility(View.GONE);
        }
    }

    //显示加载失败布局
    public void showLoadFailView(ViewGroup view)
    {

        //如果为null 则创建添加布局，否则根据显示状态进行显示
        if (loadFailView == null)
        {

            loadFailView = LayoutInflater.from(this).inflate(R.layout.load_fail, null);
            LinearLayout load_fail_layout = (LinearLayout) loadFailView.findViewById(R.id.load_fail_layout);
            load_fail_layout.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            loadFailView.setLayoutParams(params);


            if (view instanceof LinearLayout)
            {
                ((LinearLayout) view).addView(loadFailView, 0);
            } else
            {
                view.addView(loadFailView);
            }

        } else if (loadFailView.getVisibility() == View.GONE)
        {

            loadFailView.setVisibility(View.VISIBLE);

        }
        loadFailView.setOnClickListener(null);

    }

    //设置加载失败时，加载失败按钮的响应事件
    public void setOnLoadFailClickListener(View.OnClickListener click)
    {
        if (loadFailView == null)
        {
            return;
        }
        Button button = (Button) loadFailView.findViewById(R.id.load_fail_button);
        button.setOnClickListener(click);
    }


    //隐藏加载失败布局
    public void hideLoadFailView()
    {
        if (loadFailView != null && loadFailView.getVisibility() == View.VISIBLE)
        {
            loadFailView.setVisibility(View.GONE);
        }
    }


    //显示加载空页面布局
    protected void showEmptyView(ViewGroup view)
    {

        //如果为null 则创建添加布局，否则根据显示状态进行显示
        if (emptyView == null)
        {

            emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view, null);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            emptyView.setLayoutParams(params);

            View no_info_layout = emptyView.findViewById(R.id.no_info_layout);
            no_info_layout.setVisibility(View.VISIBLE);

            if (view instanceof LinearLayout)
            {
                ((LinearLayout) view).addView(emptyView, 0);
            } else
            {
                view.addView(emptyView);
            }

        } else if (emptyView.getVisibility() == View.GONE)
        {

            emptyView.setVisibility(View.VISIBLE);
        }
        emptyView.setOnClickListener(null);

    }

    //隐藏空页布局
    public void hideEmptyView()
    {
        if (emptyView != null && emptyView.getVisibility() == View.VISIBLE)
        {
            emptyView.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        checkBaiduPush();
        // 页面埋点，需要使用Activity的引用，以便代码能够统计到具体页面名
        StatService.onResume(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        // 页面结束埋点，需要使用Activity的引用，以便代码能够统计到具体页面名
        StatService.onPause(this);
    }

    private void checkBaiduPush()
    {

        if ( new SharedPreferencesUtil(BaseActivity.this).getIsLogin().equals("1"))
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, PublicUtils.getMetaValue(BaseActivity.this, "api_key"));
                }
            }).start();
        }


    }

}
