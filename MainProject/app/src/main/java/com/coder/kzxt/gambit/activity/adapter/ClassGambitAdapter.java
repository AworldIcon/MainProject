package com.coder.kzxt.gambit.activity.adapter;

import android.content.Context;

import com.coder.kzxt.gambit.activity.bean.GambitEntity;
import com.coder.kzxt.gambit.activity.bean.MyGambitBean;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;

import java.util.List;

/**
 * Created by pc on 2017/3/15.
 */

public class ClassGambitAdapter extends PullRefreshAdapter<GambitEntity>
{
    public static final int PROMOTION_IMAGE_ONE = 1;
    public static final int PROMOTION_IMAGE_OTHER = 2;

    public ClassGambitAdapter(Context context, List<GambitEntity> data, ClassGambitDelegate orderListDelegate)
    {
        super(context, data, orderListDelegate);
    }


}
