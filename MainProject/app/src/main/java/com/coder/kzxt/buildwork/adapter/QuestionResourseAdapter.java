package com.coder.kzxt.buildwork.adapter;

import android.content.Context;
import com.coder.kzxt.buildwork.entity.AlreadyPublishBean;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import java.util.List;

/**
 * Created by pc on 2017/3/18.
 */

public class QuestionResourseAdapter extends PullRefreshAdapter<AlreadyPublishBean.ItemsBean>
{
    public static final int PROMOTION_IMAGE_ONE = 1;
    public static final int PROMOTION_IMAGE_OTHER = 2;

    public QuestionResourseAdapter(Context context, List<AlreadyPublishBean.ItemsBean> data, QuestionResourseDelegate orderListDelegate)
    {
        super(context, data, orderListDelegate);
    }


}
