package com.coder.kzxt.stuwork.adapter;

import android.content.Context;

import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;
import com.coder.kzxt.stuwork.entity.StuCourseWorkBean;

import java.util.List;

/**
 * Created by pc on 2017/4/6.
 */

public class StuCourseWorkAdapter extends PullRefreshAdapter<StuCourseWorkBean.ItemsBean> {
    public StuCourseWorkAdapter(Context context, List<StuCourseWorkBean.ItemsBean> data, StuCourseWorkDelegate stuCourseWorkDelegate)
    {
        super(context, data, stuCourseWorkDelegate);
    }
}
