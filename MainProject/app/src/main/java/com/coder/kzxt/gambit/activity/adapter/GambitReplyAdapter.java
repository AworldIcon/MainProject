package com.coder.kzxt.gambit.activity.adapter;

import android.content.Context;
import com.coder.kzxt.gambit.activity.bean.Comment;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import java.util.List;

/**
 * Created by pc on 2017/3/16.
 */

public class GambitReplyAdapter extends PullRefreshAdapter<Comment>
{
    public static final int PROMOTION_IMAGE_ONE = 1;
    public static final int PROMOTION_IMAGE_OTHER = 2;

    public GambitReplyAdapter(Context context, List<Comment> data, GambitReplyDelegate orderListDelegate)
    {
        super(context, data, orderListDelegate);
    }


}
