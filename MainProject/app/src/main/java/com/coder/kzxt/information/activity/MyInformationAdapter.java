package com.coder.kzxt.information.activity;

import android.content.Context;

import com.coder.kzxt.main.beans.InformationResult;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;

import java.util.List;

/**
 * Created by pc on 2017/3/3.
 */

public class MyInformationAdapter extends PullRefreshAdapter<InformationResult.ItemsBean>
{
    public static final int PROMOTION_IMAGE_ONE = 1;
    public static final int PROMOTION_IMAGE_OTHER = 2;

    public MyInformationAdapter(Context context, List<InformationResult.ItemsBean> data, InformationDelegate orderListDelegate)
    {
        super(context, data, orderListDelegate);
    }


    /**
     * 多模板的type返回
     */

    @Override
    public int getCommonViewType(int position)
    {
        if (mData.get(position).getImgs().size() == 1||mData.get(position).getImgs().size() == 2)
        {
            return PROMOTION_IMAGE_ONE;
        } else
        {
            return PROMOTION_IMAGE_OTHER;
        }
    }

}
