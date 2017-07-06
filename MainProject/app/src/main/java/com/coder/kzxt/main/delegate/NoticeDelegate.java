package com.coder.kzxt.main.delegate;

import android.content.Context;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.beans.LiveBean;
import com.coder.kzxt.recyclerview.adapter.BaseDelegate;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/4/13
 */

public class NoticeDelegate extends BaseDelegate<LiveBean>
{
    public NoticeDelegate(Context context)
    {
        super(R.layout.tab_live_child2_item);
    }

    @Override
    public void initCustomView(BaseViewHolder holder, List<LiveBean> data, int position)
    {


    }
}
