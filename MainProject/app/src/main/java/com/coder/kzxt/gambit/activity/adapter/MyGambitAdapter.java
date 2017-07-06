package com.coder.kzxt.gambit.activity.adapter;

import android.content.Context;

import com.coder.kzxt.gambit.activity.bean.MyGambitBean;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;

import java.util.List;

/**
 * Created by pc on 2017/3/3.
 */

public class MyGambitAdapter extends PullRefreshAdapter<MyGambitBean.DataBean>
{
    public static final int PROMOTION_IMAGE_ONE = 1;
    public static final int PROMOTION_IMAGE_OTHER = 2;

    public MyGambitAdapter(Context context, List<MyGambitBean.DataBean> data, MyGambitDelegate orderListDelegate)
    {
        super(context, data, orderListDelegate);
    }


}
