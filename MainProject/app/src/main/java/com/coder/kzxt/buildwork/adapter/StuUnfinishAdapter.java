package com.coder.kzxt.buildwork.adapter;

import android.content.Context;

import com.coder.kzxt.buildwork.entity.UnfinishNum;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;

import java.util.List;

/**
 * Created by pc on 2017/4/7.
 */

public class StuUnfinishAdapter extends PullRefreshAdapter<UnfinishNum.ItemsBean> {
    public StuUnfinishAdapter(Context context, List<UnfinishNum.ItemsBean> data, StuUnfinishDelegate orderListDelegate) {
        super(context, data, orderListDelegate);
    }


}
