package com.coder.kzxt.poster.adapter;

import android.content.Context;

import com.coder.kzxt.poster.beans.PosterBeans;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/1/8
 */
public class MyPromotionStageAdapter extends PullRefreshAdapter<PosterBeans.ItemsBean>
{

    public MyPromotionStageAdapter(Context context, List<PosterBeans.ItemsBean> data, MyPromotionStageDelegate orderListDelegate)
    {
        super(context,data,orderListDelegate);
    }

}
