package com.coder.kzxt.base.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.baidu.mobstat.StatService;
import com.coder.kzxt.activity.R;


public abstract class BaseFragment extends Fragment
{

    /**
     * fragment 是否创建过
     */
    protected boolean isViewCreated;

    /**
     * 是否请求过网络数据
     */
    protected boolean isRequstData;

    /**
     * 加载失败布局
     */
    private View loadFailView;
    /**
     * 空页面数据布局
     */
    private View emptyView;


    /**
     * 加载布局
     */
    private View loadingView;

    protected ViewPager pager;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint())
        {
            onVisible();
            if (isViewCreated && !isRequstData)
            {
                isRequstData = true;
                requestData();
            }
        } else
        {
            onInvisible();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        if (getUserVisibleHint())
        {
            isRequstData = true;
            requestData();
        }

    }


    //显示加载布局
    public void showLoadingView(View view)
    {
        if (getActivity() == null)
        {
            return;
        }

        //如果为null 则创建添加布局，否则根据显示状态进行显示
        if (loadingView == null)
        {
            loadingView = LayoutInflater.from(getActivity()).inflate(R.layout.loading_dialog, null);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            loadingView.setLayoutParams(params);

            if (view instanceof LinearLayout)
            {
                ((LinearLayout) view).addView(loadingView, 0);
            } else if (view instanceof RelativeLayout)
            {
                ((RelativeLayout) view).addView(loadingView);
            } else if (view instanceof ViewGroup)
            {
//                L.e("fragment view is not linearLa");
                ((ViewGroup) view).addView(loadingView);
            }

        } else if (loadingView.getVisibility() == View.GONE)
        {

            loadingView.setVisibility(View.VISIBLE);

        }
        loadingView.setOnClickListener(null);


        if (loadFailView != null && loadFailView.getVisibility() == View.VISIBLE)
        {
            loadFailView.setVisibility(View.GONE);
        }

        if (emptyView != null && emptyView.getVisibility() == View.VISIBLE)
        {
            emptyView.setVisibility(View.GONE);
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
    protected void showLoadFailView(View view)
    {

        //如果为null 则创建添加布局，否则根据显示状态进行显示
        if (loadFailView == null)
        {
            loadFailView = LayoutInflater.from(getActivity()).inflate(R.layout.load_fail, null);
            LinearLayout load_fail_layout = (LinearLayout) loadFailView.findViewById(R.id.load_fail_layout);
            load_fail_layout.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            loadFailView.setLayoutParams(params);

            if (view instanceof LinearLayout)
            {
                ((LinearLayout) view).addView(loadFailView, 0);
            } else if (view instanceof RelativeLayout)
            {
                ((RelativeLayout) view).addView(loadFailView);
            } else if (view instanceof ViewGroup)
            {
//                L.e("fragment view is not linearLa");
                ((ViewGroup) view).addView(loadFailView);
            }

        } else if (loadFailView.getVisibility() == View.GONE)
        {

            loadFailView.setVisibility(View.VISIBLE);

        }
        loadFailView.setOnClickListener(null);

    }

    //设置加载失败时，加载失败按钮的响应事件
    protected void setOnLoadFailClickListener(View.OnClickListener click)
    {
        if (loadFailView == null)
        {
            return;
        }
        Button button = (Button) loadFailView.findViewById(R.id.load_fail_button);
        button.setOnClickListener(click);
    }

    //隐藏加载失败布局
    protected void hideLoadFailView()
    {
        if (loadFailView != null && loadFailView.getVisibility() == View.VISIBLE)
        {
            loadFailView.setVisibility(View.GONE);
        }
    }


    //显示加载空页面布局
    protected void showEmptyView(View view)
    {

        //如果为null 则创建添加布局，否则根据显示状态进行显示
        if (emptyView == null)
        {
            emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.empty_view, null);
            LinearLayout no_info_layout = (LinearLayout) emptyView.findViewById(R.id.no_info_layout);
            no_info_layout.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            emptyView.setLayoutParams(params);

            if (view instanceof LinearLayout)
            {
                ((LinearLayout) view).addView(emptyView, 0);
            } else if (view instanceof RelativeLayout)
            {
                ((RelativeLayout) view).addView(emptyView);
            } else if (view instanceof ViewGroup)
            {
//                L.e("fragment view is not linearLa");
                ((ViewGroup) view).addView(emptyView);
            }

        } else if (emptyView.getVisibility() == View.GONE)
        {

            emptyView.setVisibility(View.VISIBLE);

        }
        emptyView.setOnClickListener(null);

    }

    //设置加载失败时，加载失败按钮的响应事件
    protected void setOnEmptyClickListener(View.OnClickListener click)
    {
        if (emptyView == null)
        {
            return;
        }
        LinearLayout linearLayout = (LinearLayout) emptyView.findViewById(R.id.no_info_layout);
        linearLayout.setOnClickListener(click);
    }

    //隐藏加载失败布局
    protected void hideEmptyView()
    {
        if (emptyView != null && emptyView.getVisibility() == View.VISIBLE)
        {
            emptyView.setVisibility(View.GONE);
        }
    }

    /**
     * 可见
     */
    protected void onVisible()
    {
    }


    /**
     * 不可见
     */
    protected void onInvisible()
    {

    }

    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void requestData();

    @Override
    public void onPause()
    {
        super.onPause();
        // 页面埋点
        StatService.onPause(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        // 页面埋点
        StatService.onResume(this);
    }

    public ViewPager getPager()
    {
        return pager;
    }


}
