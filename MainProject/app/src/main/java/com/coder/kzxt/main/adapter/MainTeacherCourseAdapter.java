package com.coder.kzxt.main.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.app.utils.L;
import com.coder.kzxt.course.activity.TeachingCourseActivity;
import com.coder.kzxt.main.beans.MainModelBean;
import com.coder.kzxt.views.MyGridView;
import com.coder.kzxt.views.MyListView;
import com.coder.kzxt.activity.R;

import java.util.List;

/**
 * 我的授课
 */
public class MainTeacherCourseAdapter {

    private Activity mContext;
    //private PublicUtils pu;
    private List<MainModelBean.ItemsBean.ListBean> data;
    private MainModelBean.ItemsBean configBean;

    //所有的类型；
    private static final String
            TEACHER_HENG_FOUR = "TEACHER_HENG_FOUR",
            TEACHER_SHU_TWO = "TEACHER_SHU_TWO",
            TEACHER_SHANGTUO_TWO = "TEACHER_SHANGTUO_TWO",
            TEACHER_HENGXIANG_TWO = "TEACHER_HENGXIANG_TWO",
            TEACHER_SHUONE_HENGTWO = "TEACHER_SHUONE_HENGTWO",
            TEACHER_SHUONE_HENGTWO_XIAHENGONE = "TEACHER_SHUONE_HENGTWO_XIAHENGONE",
            TEACHER_SHUONE_HENGTWO_XIASHUTWO = "TEACHER_SHUONE_HENGTWO_XIASHUTWO",
            TEACHER_ZHENGFOUR_XIAHENGONE = "TEACHER_ZHENGFOUR_XIAHENGONE";
    /**
     * 需要显示老师名称和学习人数的
     */
    public static String showTeacherAndNumber =
            TEACHER_HENG_FOUR + "," + TEACHER_SHU_TWO + "," +
                    TEACHER_SHANGTUO_TWO + "," + TEACHER_HENGXIANG_TWO + "," +
                    TEACHER_SHUONE_HENGTWO + "," + TEACHER_SHUONE_HENGTWO_XIAHENGONE + "," +
                    TEACHER_SHUONE_HENGTWO_XIASHUTWO + "," + TEACHER_ZHENGFOUR_XIAHENGONE;


    public static String types =
            TEACHER_HENG_FOUR + "," + TEACHER_SHU_TWO + "," +
                    TEACHER_SHANGTUO_TWO + "," + TEACHER_HENGXIANG_TWO + "," +
                    TEACHER_SHUONE_HENGTWO + "," + TEACHER_SHUONE_HENGTWO_XIAHENGONE + "," +
                    TEACHER_SHUONE_HENGTWO_XIASHUTWO + "," + TEACHER_ZHENGFOUR_XIAHENGONE;


    public MainTeacherCourseAdapter(Activity mContext, MainModelBean.ItemsBean moduleType, List<MainModelBean.ItemsBean.ListBean> data) {
        super();
        this.mContext = mContext;
        this.data = data;
        //this.pu = new PublicUtils(this.mContext);
        this.configBean = moduleType;
    }

    public View getView() {

        View view = null;
        if (this.data == null || this.data.size() == 0) return view;

        switch (configBean.getModule_style()) {
            case TEACHER_HENG_FOUR:
                view = disPlayGridView(view);
                break;
            case TEACHER_SHU_TWO:
                view = disPlayListView(view, 2);
                break;
            case TEACHER_SHANGTUO_TWO:
                view = setListViewGridView(view, 2);
                break;
            case TEACHER_HENGXIANG_TWO:
                view = disPlayListView(view, 1);
                break;
            //不规则
            case TEACHER_SHUONE_HENGTWO:
                view = setListViewThreeItem(view);
                break;
            case TEACHER_SHUONE_HENGTWO_XIAHENGONE:
                view = setListViewListView(view);
                break;
            case TEACHER_SHUONE_HENGTWO_XIASHUTWO:
                view = setGridViewThreeItem(view);
                break;
            case TEACHER_ZHENGFOUR_XIAHENGONE:
                view = setListViewGridView(view, 4);
                break;
            default:
                view = disPlayListView(view, 1);
                break;
        }

        if (view == null) {
            L.d("mainLiveCourseAdapter view is null");
            return view;
        }

        TextView moduleType = (TextView) view.findViewById(R.id.moduleTitle);
        TextView loadMore = (TextView) view.findViewById(R.id.loadMore);
        moduleType.setText(configBean.getModule_name());
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TeachingCourseActivity.class);
                mContext.startActivity(intent);

                // mContext.startActivity(new Intent(mContext, TeachingCourseActivity.class));
            }
        });
        return view;
    }

    /**
     * type 类型 区分加载不同的adapter
     * 1 垂直
     * 2 水平
     *
     * @param view
     * @param type
     * @return
     */
    private View disPlayListView(View view, int type) {
        view = View.inflate(mContext, R.layout.item_main_mylist, null);
        MyListView myListView = (MyListView) view.findViewById(R.id.myListView);
        if (type == 1) {
            myListView.setAdapter(new SchoolCourseVerticalBigAdapter(mContext, configBean, this.data));
        } else {
            myListView.setAdapter(new PublicCourseHorizontalAdapter(mContext, this.data));
        }

        return view;
    }

    private View disPlayGridView(View view) {
        view = View.inflate(mContext, R.layout.item_main_mygridview, null);
        MyGridView myGridView = (MyGridView) view.findViewById(R.id.myGridView);
        myGridView.setAdapter(new SchoolCourseVerticalAdapter(mContext, configBean, this.data));
        return view;
    }

    /**
     * 下面是listview item是3个的布局
     */
    public View setListViewThreeItem(View view) {
        view = View.inflate(mContext, R.layout.item_main_mylist, null);
        MyListView myListView = (MyListView) view.findViewById(R.id.myListView);
        myListView.setAdapter(new SchoolCourseVerticalThreeAdapter(mContext, configBean, this.data));
        return view;
    }

    /**
     * listView header是2或者4个的布局
     */
    public View setListViewGridView(View view, final int count) {
        view = View.inflate(mContext, R.layout.item_main_mylist, null);
        MyListView myListView = (MyListView) view.findViewById(R.id.myListView);

        View headView = View.inflate(mContext, R.layout.view_mygridview, null);
        MyGridView myGridView = (MyGridView) headView.findViewById(R.id.myGridView);
        myListView.addHeaderView(headView);
        L.d("size = " + data.size() + " ; count = " + count);
        int i = Math.min(count, this.data.size());
        myGridView.setAdapter(new SchoolCourseVerticalAdapter(mContext, configBean, this.data.subList(0, i)));
        SchoolCourseVerticalBigAdapter courseLiveVerticalBigAdapter;
        if (this.data.size() > count) {
            courseLiveVerticalBigAdapter = new SchoolCourseVerticalBigAdapter(mContext, configBean, this.data.subList(count, this.data.size()));
        } else {
            courseLiveVerticalBigAdapter = new SchoolCourseVerticalBigAdapter(mContext, configBean, null);
        }
        myListView.setAdapter(courseLiveVerticalBigAdapter);

        return view;
    }

    /**
     * listView header是3个不规则的布局 把它当做list看
     */
    public View setListViewListView(View view) {
        view = View.inflate(mContext, R.layout.item_main_mylist, null);
        MyListView myListView = (MyListView) view.findViewById(R.id.myListView);

        View headView = View.inflate(mContext, R.layout.view_mylistview, null);
        MyListView headerListView = (MyListView) headView.findViewById(R.id.myListView);
        myListView.addHeaderView(headView);

        int count = Math.min(this.data.size(), 3);
        headerListView.setAdapter(new SchoolCourseVerticalThreeAdapter(mContext, configBean, this.data.subList(0, count)));
        SchoolCourseVerticalBigAdapter courseLiveVerticalBigAdapter;
        if (this.data.size() > 3) {
            courseLiveVerticalBigAdapter = new SchoolCourseVerticalBigAdapter(mContext, configBean, this.data.subList(3, this.data.size()));
        } else {
            courseLiveVerticalBigAdapter = new SchoolCourseVerticalBigAdapter(mContext, configBean, null);
        }
        myListView.setAdapter(courseLiveVerticalBigAdapter);

        return view;
    }

    /**
     * 下面是gridview header是3个的布局 把它当做list看
     */

    public View setGridViewThreeItem(View view) {
        view = View.inflate(mContext, R.layout.item_main_gridview_header, null);
        MyGridView myGridView = (MyGridView) view.findViewById(R.id.myGridView);
        MyListView myListView = (MyListView) view.findViewById(R.id.myListView);

        int count = Math.min(this.data.size(), 3);
        myListView.setAdapter(new SchoolCourseVerticalThreeAdapter(mContext, configBean, this.data.subList(0, count)));
        if (this.data.size() > 3) {
            myGridView.setAdapter(new SchoolCourseVerticalAdapter(mContext, configBean, this.data.subList(3, this.data.size())));
        }

        return view;
    }

}
