package com.coder.kzxt.poster.adapter;

import android.content.Context;

import com.coder.kzxt.poster.beans.PostersCommentBean;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;

import java.util.List;

/**
 * Created by tcy on 2017/3/2.
 */

public class PosterParticularsAdapter extends PullRefreshAdapter<PostersCommentBean.ItemsBean> {
    public PosterParticularsAdapter(Context context, List<PostersCommentBean.ItemsBean> strings, int headerCount, PullRefreshDelegate pullRefreshDelegate) {
        super(context, strings, headerCount, pullRefreshDelegate);
    }
}
