package com.coder.kzxt.gambit.activity.adapter;

import android.content.Context;

import com.coder.kzxt.gambit.activity.bean.Gambit;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;

import java.util.List;

/**
 * Created by pc on 2017/3/3.
 */

public class GambitCommentAdapter extends PullRefreshAdapter<Gambit>
{
    public static final int PROMOTION_IMAGE_ONE = 1;
    public static final int PROMOTION_IMAGE_OTHER = 2;

    public GambitCommentAdapter(Context context, List<Gambit> data, GambitCommentDelegate orderListDelegate)
    {
        super(context, data, orderListDelegate);
    }


}
