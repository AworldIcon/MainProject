package com.coder.kzxt.question.adapter;

import android.content.Context;
import com.coder.kzxt.question.beans.QuestionsReplyListBean;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;

import java.util.List;

/**
 * Created by pc on 2017/4/19.
 */

public class QuestionReplyListAdapter extends PullRefreshAdapter<QuestionsReplyListBean.ItemsBean>
{

    public QuestionReplyListAdapter(Context context, List<QuestionsReplyListBean.ItemsBean> data, QuestionReplyListDelegate orderListDelegate)
    {
        super(context, data,1, orderListDelegate);
    }


}
