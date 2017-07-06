package com.coder.kzxt.classe.adapter;

import android.content.Context;

import com.coder.kzxt.classe.beans.TopicReplyBean;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import java.util.List;

/**
 * Created by wangtingshun on 2017/6/15.
 * 话题回复的adapter
 */

public class TopicReplyAdapter extends PullRefreshAdapter<TopicReplyBean.TopicReply> {

    public TopicReplyAdapter(Context context, List<TopicReplyBean.TopicReply> strings, PullRefreshDelegate pullRefreshDelegate) {
        super(context, strings, pullRefreshDelegate);
    }
}
