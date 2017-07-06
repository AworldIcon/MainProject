package com.coder.kzxt.question.adapter;

import android.content.Context;
import com.coder.kzxt.question.beans.CourseQuestionResultBean;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;

import java.util.List;

/**
 * Created by pc on 2017/4/18.
 */

public class CourseQuestionAdapter extends PullRefreshAdapter<CourseQuestionResultBean.ItemsBean>
{
    public static final int PROMOTION_IMAGE_ONE = 1;
    public static final int PROMOTION_IMAGE_OTHER = 2;

    public CourseQuestionAdapter(Context context, List<CourseQuestionResultBean.ItemsBean> data, CourseQuestionDelegate orderListDelegate)
    {
        super(context, data, orderListDelegate);
    }


}
