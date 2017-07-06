package com.coder.kzxt.main.adapter;

import android.content.Context;

import com.coder.kzxt.main.beans.DiscoveryBean;
import com.coder.kzxt.main.delegate.DiscoveryDelegate;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/2/8.
 */

public class DiscoveryAdapter extends BaseRecyclerAdapter<DiscoveryBean.ItemBean.ModuleBean>
{

    public DiscoveryAdapter(Context context, List<DiscoveryBean.ItemBean.ModuleBean> module, DiscoveryDelegate discoveryDelegate)
    {
        super(context, module, discoveryDelegate);
        this.mContext = context;
        this.mData = module;
    }


}
