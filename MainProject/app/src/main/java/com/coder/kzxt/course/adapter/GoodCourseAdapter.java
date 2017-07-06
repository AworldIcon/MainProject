package com.coder.kzxt.course.adapter;

import android.content.Context;

import com.coder.kzxt.course.beans.GoodCourseResult;
import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by pc on 2017/3/11.
 */

public class GoodCourseAdapter extends PullRefreshAdapter<GoodCourseResult.DataBean.CourseBean> {
    public GoodCourseAdapter(Context context, List<GoodCourseResult.DataBean.CourseBean> list, GoodCourseDelegate goodCourseDelegate){
        super(context,list,goodCourseDelegate);
    }
}
