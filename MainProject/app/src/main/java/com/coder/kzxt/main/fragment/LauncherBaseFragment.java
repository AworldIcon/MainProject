package com.coder.kzxt.main.fragment;

import com.coder.kzxt.base.fragment.BaseFragment;

/**
 * 引导页用的fragment抽象类
 * Created by Administrator on 2016/6/4.
 */
public abstract class LauncherBaseFragment extends BaseFragment
{
    public abstract void  startAnimation();
    public abstract void  stopAnimation();
}
