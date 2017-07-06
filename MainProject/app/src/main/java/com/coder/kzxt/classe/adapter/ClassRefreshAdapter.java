package com.coder.kzxt.classe.adapter;

import android.content.Context;

import com.coder.kzxt.classe.beans.ClassTopicBean;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;

import java.util.List;

/**
 * Created by Administrator on 2017/6/12.
 */

public class ClassRefreshAdapter extends PullRefreshAdapter<ClassTopicBean.ClassTopic> {

    public ClassRefreshAdapter(Context context, List<ClassTopicBean.ClassTopic> strings, PullRefreshDelegate pullRefreshDelegate) {
        super(context, strings, pullRefreshDelegate);
    }


}
